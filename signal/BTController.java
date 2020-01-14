package signal;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.Socket;
import java.util.List;

import javax.microedition.io.*;

import com.intel.bluetooth.BlueCoveImpl;

public class BTController extends Thread{
	public StreamConnectionNotifier scn = null;
	private StreamConnection connection = null;
	private ObjectInputStream btois;
	private ObjectOutputStream btoos;
	private ObjectInputStream cois;
	private ObjectOutputStream coos;

	public static void main(String[] args) throws IOException, ClassNotFoundException{
		BTController controller = new BTController();
		controller.scn = (StreamConnectionNotifier) Connector.open("btspp://localhost:1");
		while (true) {
			int port = controller.waitBTSig();
			controller.connectToSubSystem(port);
		}
	}
	
	// EV3����V�X�e���ɃI�u�W�F�N�g�𗬂�
	public void run(){
		try {
			// ����I�u�W�F�N�g���󂯎��O�� bool ���󂯎���� close ���邩���肷��
			while(btois.readBoolean()) {
				coos.writeObject(btois.readObject());
				coos.flush();
			}
			btois.close();
			btoos.close();
			connection.close();
			System.out.println("closed connection.");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	// �V�X�e������EV3�ɃI�u�W�F�N�g�𗬂�
	public void systemToObject(){
		try {
			while (cois.readBoolean()) {
				btoos.writeObject(cois.readObject());
				btoos.flush();
			}
			cois.close();
			coos.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * PC��EV3����ڑ��҂���Ԃɂ���
	 * @return ������:true ���s��:false
	 * @throws IOException
	 */
	private int waitBTSig() throws IOException {
		if (scn == null) {
			System.exit(1);
		}
		System.out.println("waiting ev3 sig...");
		connection = scn.acceptAndOpen();
		
		DataOutputStream dos = connection.openDataOutputStream();
		DataInputStream dis = connection.openDataInputStream();
		btoos = new ObjectOutputStream(dos);
		btoos.flush();
		btois = new ObjectInputStream(dis);
		
		System.out.println("connected to ev3.");
		// �ڑ�����|�[�g�ԍ���Ԃ�
		return btois.readInt();
	}
	
	//�e�T�u�V�X�e���ɐڑ�
	private void connectToSubSystem(int port) throws IOException{
		try {
			System.out.println("connecting to system...");
			System.out.println("port : " + port);
			Socket sc = new Socket("localhost", port);
			coos = new ObjectOutputStream(sc.getOutputStream());
			coos.flush();
			cois = new ObjectInputStream(sc.getInputStream());
			coos.writeBoolean(true);
			coos.flush();
		} catch (IOException e) {
			// �T�u�V�X�e���ɐڑ��ł��Ȃ�������EV3���ɐڑ��s��m�点�Đڑ������
			btoos.writeBoolean(false);
			btoos.flush();
			btoos.close();
			btois.close();
			connection.close();
			return;
		}
		System.out.println("connected to system.");
		// �T�u�V�X�e���ɐڑ��ł�����EV3���ɐڑ��\��m�点�ĒʐM�J�n
		btoos.writeBoolean(true);
		btoos.flush();
		start();
		systemToObject();
	}
	
}