package signal;

import java.io.*;
import java.net.Socket;

import javax.microedition.io.*;

public class BTController extends Thread{
	private StreamConnectionNotifier scn = null;
	private StreamConnection connection = null;
	private ObjectInputStream btois;
	private ObjectOutputStream btoos;
	private ObjectInputStream cois;
	private ObjectOutputStream coos;

	public static void main(String[] args) throws IOException, ClassNotFoundException{
		BTController controller = new BTController();
		while (true) {
			int port = controller.waitBTSig();
			controller.connectToSubSystem(port);
			controller.start();
			controller.systemToObject();
		}
	}
	
	// EV3����V�X�e���ɃI�u�W�F�N�g�𗬂�
	public void run(){
		try {
			// ����I�u�W�F�N�g���󂯎��O�� bool ���󂯎���� close ���邩���肷��
			while(btois.readBoolean()) {
				coos.writeObject(btois.readObject());
			}
			btois.close();
			btoos.close();
			cois.close();
			coos.close();
			scn.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	// �V�X�e������EV3�ɃI�u�W�F�N�g�𗬂�
	public void systemToObject() throws IOException, ClassNotFoundException{
		while (cois.readBoolean()) {
			btoos.writeObject(cois.readObject());
		}
	}
	
	/**
	 * pc��EV3����ڑ��҂���Ԃɂ���
	 * @return ������:true ���s��:false
	 * @throws IOException
	 */
	private int waitBTSig() throws IOException {
		scn = (StreamConnectionNotifier) Connector.open("btspp://localhost:1");
		if (scn == null) {
			System.exit(1);
		}
		connection = scn.acceptAndOpen();
		
		btois = new ObjectInputStream(connection.openDataInputStream());
		btoos = new ObjectOutputStream(connection.openDataOutputStream());
		
		// �ڑ�����|�[�g�ԍ���Ԃ�
		return btois.readInt();
	}
	
	//�e�T�u�V�X�e���ɐڑ�
	private void connectToSubSystem(int port) throws IOException{
		Socket sc = new Socket("localhost", port);
		cois = new ObjectInputStream(sc.getInputStream());
		coos = new ObjectOutputStream(sc.getOutputStream());
		coos.writeBoolean(true);
	}
}
