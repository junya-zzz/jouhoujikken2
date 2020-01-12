package signal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.BTConnection;
import lejos.utility.Delay;

/**
 * ev3�p�̒ʐM�N���X
 */
public class EV3Signal {
	
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	private BTConnection btConnection = null;
	private BTConnector btConnector = null;
	private boolean isConnectToPC;
	private boolean isServerMode;

	/**
	 * ev3����device�ɐڑ�����
	 * @param p �ڑ�����V�X�e���̃|�[�g Port.RECEIVE_PORT �Ȃ�
	 * @return ������:true ���s��:false
	 */
	public boolean openSig(Port p) throws IOException{
		isServerMode = false;
		btConnector = new BTConnector();
		btConnection = btConnector.connect(p.address, BTConnection.RAW);
		
		if (btConnection == null) {
			return false;
		}
		if (p == Port.RELAY) {
			isConnectToPC = true;
		}
		
		oos = new ObjectOutputStream(btConnection.openDataOutputStream());
		ois = new ObjectInputStream(btConnection.openDataInputStream());
		
		// �ڑ�����V�X�e���̃|�[�g�ԍ��𑗂�
		oos.writeInt(p.portNum);

		// �ڑ��\����M
		if (!ois.readBoolean()) {
			closeSig();
			return false;
		}
		
		return true;
	}

	/**
	 * ev3��ڑ��҂���Ԃɂ���
	 * @return ������:true ���s��:false
	 */
	public boolean waitSig() throws IOException{
		isServerMode = true;
		isConnectToPC = false;
		btConnector = new BTConnector();
		btConnection = btConnector.waitForConnection(0, BTConnection.RAW);
		if (btConnection == null) {
			return false;
		}
		oos = new ObjectOutputStream(btConnection.openDataOutputStream());
		ois = new ObjectInputStream(btConnection.openDataInputStream());

		// �ڑ�����V�X�e���̃|�[�g�ԍ����󂯎�邯�ǎg��Ȃ�����̂Ă�
		ois.readInt();
		// �ڑ��\�𑗐M����
		oos.writeBoolean(true);

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
			Delay.msDelay(10000);
			System.exit(1);
		}
		return object;
	}

	/**
	 * �I�u�W�F�N�g�𑗐M����
	 * @param data ���M����I�u�W�F�N�g
	 * @throws IOException
	 */
	public void sendSig(Object data) throws IOException{
		if (isConnectToPC) {
			oos.writeBoolean(true);
		}
		oos.writeObject(data);
		oos.flush();
	}

	/**
	 * �ʐM�����
	 * @return
	 * @throws IOException
	 */
	public void closeSig() throws IOException{
		if (isConnectToPC) {
			oos.writeBoolean(false);
		}
		ois.close();
		oos.close();
		
		if (isServerMode) {
			btConnector.close();
		}
		btConnection.close();
		
		return;
	}
}
