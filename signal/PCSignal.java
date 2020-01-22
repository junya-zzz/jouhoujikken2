package signal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * �p�\�R���p�̒ʐM�N���X
 * @author bp17048
 *
 */
public class PCSignal {

	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	private ServerSocket ss = null;
	private Socket sc = null;
	/**
	 * ev3�Ɛڑ����Ă���Ƃ���true�A�p�\�R���Ɛڑ����Ă���Ƃ���false
	 */
	private boolean isConnectToBT;
	/**
	 * �T�[�o�[�Ƃ��Đڑ������Ƃ�true
	 */
	private boolean isServer;
	
	/**
	 * ���̃V�X�e���ɐڑ�����
	 * @param p �ڑ�����V�X�e����Port
	 * @return ������:true ���s��:false
	 * @throws IOException �ڑ��ɉ��炩�̎��s���������Ƃ�
	 */
	public boolean openSig(Port p) throws IOException{
		System.out.println("connecting to " + p.name() + " : " + p.portNum);
		sc = new Socket("localhost", p.portNum);
		oos = new ObjectOutputStream(sc.getOutputStream());
		oos.flush();
		ois = new ObjectInputStream(sc.getInputStream());
		oos.writeBoolean(false);
		oos.flush();
		isServer = false;
		System.out.println("connection opened to " + p.name());
		if (p == Port.HEAD || p == Port.RECEIVE||p==Port.RECEPTION) {
			isConnectToBT = false;
		} else {
			isConnectToBT = true;
		}
		return true;
	}

	/**
	 * �V�X�e����ڑ��҂���Ԃɂ���
	 * @param p �����̃V�X�e�������� receiveSystem��������Port.RECEIVE_PORT
	 * @return ������:true ���s��:false
	 * @throws IOException ���s�����Ƃ�
	 */

	public Boolean waitSig(Port p) throws IOException {
		System.out.println("waiting... : " + p.portNum);
		ss = new ServerSocket(p.portNum);
		sc = ss.accept();
		oos = new ObjectOutputStream(sc.getOutputStream());
		oos.flush();
		ois = new ObjectInputStream(sc.getInputStream());
		isConnectToBT = ois.readBoolean();
		System.out.println(isConnectToBT);
		isServer = true;
		System.out.println("connection opened.");
		return true;
	}

	/**
	 * �I�u�W�F�N�g����M����
	 * @return ��M�����I�u�W�F�N�g
	 * @throws IOException ��M�Ɏ��s
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
	 * @throws IOException ���M�Ɏ��s
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
	 * @return ���������Ƃ�"OK"��Ԃ�
	 * @throws IOException ���邱�Ƃ����s�����Ƃ�
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
