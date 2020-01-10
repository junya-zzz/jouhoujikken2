package signal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lejos.hardware.lcd.LCD;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.BTConnection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * ev3�p�̒ʐM�N���X
 */
public class EV3Signal {
	
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	private BTConnection btConnection = null;
	private BTConnector btConnector = null;
	private boolean isServerMode;

	/**
	 * ev3����device�ɐڑ�����
	 * @param device �ڑ�����Bluetooth�f�o�C�X��MAC�A�h���X
	 * @return ������:true ���s��:false
	 */
	public boolean openSig(String device) {
		isServerMode = false;
		btConnector = new BTConnector();
		btConnection = btConnector.connect(device, BTConnection.RAW);
		
		if (btConnection == null) {
			return false;
		}
		
		dis = btConnection.openDataInputStream();
		dos = btConnection.openDataOutputStream();
		
		return true;
	}

	/**
	 * ev3��ڑ��҂���Ԃɂ���
	 * @return ������:true ���s��:false
	 */
	public boolean waitSig() {
		isServerMode = true;
		btConnector = new BTConnector();
		btConnection = btConnector.waitForConnection(0, BTConnection.RAW);
		if (btConnection == null) {
			return false;
		}
		dis = btConnection.openDataInputStream();
		dos = btConnection.openDataOutputStream();
		return true;
	}

	/**
	 * �I�u�W�F�N�g����M����
	 * @return ��M�����I�u�W�F�N�g
	 * @throws IOException
	 */
	public Object getSig() throws IOException{
		Object object = null;
		int byteSize = dis.readInt();
		byte[] b = new byte[byteSize];
		dis.read(b);
		try {
			object = deserialize(b);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			while(true); 
		}
		return object;
	}
	
	public boolean getBoolSig() throws IOException {
		return dis.readBoolean();
	}

	/**
	 * �I�u�W�F�N�g�𑗐M����
	 * @param data ���M����I�u�W�F�N�g
	 * @throws IOException
	 */
	public void sendSig(Object data) throws IOException{
		byte[] bytes = serialize(data);
		dos.writeInt(bytes.length);
		dos.write(bytes);
		dos.flush();
	}

	/**
	 * �ʐM�����
	 * @return
	 * @throws IOException
	 */
	public void closeSig() throws IOException{
		dis.close();
		dos.close();
		
		if (isServerMode) {
			btConnector.close();
		}
		btConnection.close();
		
		return;
	}

    // �I�u�W�F�N�g���o�C�g�z��ɕϊ�����
	// �ϊ�����N���X�ɂ� implements Serializable ������
    private static byte[] serialize(Object obj) throws IOException {
        try (ByteArrayOutputStream b = new ByteArrayOutputStream()) {
            try (ObjectOutputStream o = new ObjectOutputStream(b)) {
                o.writeObject(obj);
            }
            return b.toByteArray();
        }
    }

    // �o�C�g�z����I�u�W�F�N�g�ɕϊ�����
    private static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream b = new ByteArrayInputStream(bytes)) {
            try (ObjectInputStream o = new ObjectInputStream(b)) {
                return o.readObject();
            }
        }
    }
}
