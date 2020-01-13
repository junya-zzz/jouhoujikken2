package signal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *�p�\�R���p�̒ʐM�N���X
 */
public class PCSignal {

	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	private ServerSocket ss = null;
	private Socket sc = null;
	private boolean isConnectToBT;
	private boolean isServer;
	
	/**
	 * ���̃V�X�e���ɐڑ�����
	 * @param p �ڑ�����V�X�e����Port
	 * @return ������:true ���s��:false
	 * @throws IOException
	 */
	public boolean openSig(Port p) throws IOException{
		System.out.println("connecting to " + p.name() + " : " + p.portNum);
		sc = new Socket("localhost", p.portNum);
		oos = new ObjectOutputStream(sc.getOutputStream());
		ois = new ObjectInputStream(sc.getInputStream());
		oos.writeBoolean(false);
		oos.flush();
		isServer = false;
		System.out.println("signal opened to " + p.name());
		return true;
	}

	/**
	 * �V�X�e����ڑ��҂���Ԃɂ���
	 * @param p �����̃V�X�e�������� receiveSystem��������Port.RECEIVE_PORT
	 * @return ������:true ���s��:false
	 * @throws IOException
	 */

	public Boolean waitSig(Port p) throws IOException {
		System.out.println("waiting... : " + p.portNum);
		ss = new ServerSocket(p.portNum);
		sc = ss.accept();
		oos = new ObjectOutputStream(sc.getOutputStream());
		ois = new ObjectInputStream(sc.getInputStream());
		isConnectToBT = ois.readBoolean();
		isServer = true;
		System.out.println("signal opened.");
		return true;
	}

	/**
	 * �I�u�W�F�N�g����M����
	 * @return ��M�����I�u�W�F�N�g
	 * @throws IOException
	 */
	public Object getSig() throws IOException{
		Object object = null;
		try {
			object = ois.readObject();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
			System.exit(1);
		}
		return object;
	}

	/**
	 * �I�u�W�F�N�g�𑗐M����
	 * @param data ���M����I�u�W�F�N�g
	 * @param device
	 * @throws IOException
	 */
	public void sendSig(Object data) throws IOException {
		if (isConnectToBT) {
			oos.writeBoolean(true);
			oos.flush();
		}
		oos.writeObject(data);
		oos.flush();
	}
	
	/**
	 * �ڑ������
	 * @return
	 * @throws IOException
	 */
	public String closeSig() throws IOException {
		if (isConnectToBT) {
			oos.writeBoolean(false);
			oos.flush();
		}
		ois.close();
		oos.close();
		sc.close();
		if (isServer) {
			ss.close();
		}
		return "OK";
	}
}
