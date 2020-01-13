package signal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lejos.hardware.lcd.LCD;
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
	private boolean isConnectToEV3;
	private boolean isServerMode;

	/**
	 * ev3����device�ɐڑ�����
	 * @param p �ڑ�����V�X�e���̃|�[�g Port.RECEIVE_PORT �Ȃ�
	 * @return ������:true ���s��:false
	 */
	public boolean openSig(Port p) throws IOException{
		LCD.clear();
		LCD.drawString("connecting to", 0, 0);
		LCD.drawString(p.address, 0, 1);
		LCD.drawInt(p.portNum, 0, 2);
		LCD.refresh();
		isServerMode = false;
		btConnector = new BTConnector();
		btConnection = btConnector.connect(p.address, BTConnection.RAW);

		if (btConnection == null) {
			return false;
		}
		if (p == Port.RELAY) {
			isConnectToEV3 = true;
		}

		LCD.clear();
		LCD.drawString("connected.", 0, 3);
		LCD.refresh();
		DataOutputStream dos = btConnection.openDataOutputStream();
		DataInputStream dis = btConnection.openDataInputStream();
		
		oos = new ObjectOutputStream(dos);
		oos.flush();
		ois = new ObjectInputStream(dis);
		LCD.clear();
		LCD.drawString("connection opened.", 0, 3);
		LCD.refresh();

		// �ڑ�����V�X�e���̃|�[�g�ԍ��𑗂�
		oos.writeInt(p.portNum);
		oos.flush();

		// �ڑ��\����M
		if (!ois.readBoolean()) {
			ois.close();
			oos.close();
			btConnection.close();
			return false;
		}

		return true;
	}

	/**
	 * ev3��ڑ��҂���Ԃɂ���
	 * @return ������:true ���s��:false
	 */
	public boolean waitSig() throws IOException{
		LCD.clear();
		LCD.drawString("waiting...", 0, 3);
		LCD.refresh();
		isServerMode = true;
		isConnectToEV3 = true;
		btConnector = new BTConnector();
		btConnection = btConnector.waitForConnection(0, BTConnection.RAW);
		if (btConnection == null) {
			return false;
		}
		DataOutputStream dos = btConnection.openDataOutputStream();
		DataInputStream dis = btConnection.openDataInputStream();
		oos = new ObjectOutputStream(dos);
		oos.flush();
		ois = new ObjectInputStream(dis);
		
		LCD.clear();
		LCD.drawString("connection opened.", 0, 3);
		LCD.refresh();

		// �ڑ�����V�X�e���̃|�[�g�ԍ����󂯎�邯�ǎg��Ȃ�����̂Ă�
		ois.readInt();
		// �ڑ��\�𑗐M����
		oos.writeBoolean(true);
		oos.flush();

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
		if (!isConnectToEV3) {
			oos.writeBoolean(true);
			oos.flush();
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
		if (!isConnectToEV3) {
			oos.writeBoolean(false);
			oos.flush();
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
