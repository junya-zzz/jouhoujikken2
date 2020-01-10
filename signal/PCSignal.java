package signal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.InputConnection;
import javax.microedition.io.OutputConnection;
import javax.microedition.io.Connection;
import javax.microedition.io.StreamConnectionNotifier;
import javax.microedition.io.StreamConnection;

/**
 *�p�\�R���p�̒ʐM�N���X
 */
public class PCSignal {

	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	private StreamConnectionNotifier scn = null;
	private StreamConnection connection = null;
	private Connection con = null;
	private boolean isServerMode;

	/**
	 * pc����ev3�ɐڑ�����
	 * @param command
	 * @param device �ڑ�����ev3��MAC�A�h���X
	 * @return ������:true ���s��:false
	 * @throws IOException
	 */
	public boolean openSig(String device) throws IOException{
		isServerMode = false;
		con = Connector.open("btspp://" + device + ":1");
		if (con == null)
			return false;
		dos = new DataOutputStream(((OutputConnection)con).openOutputStream());
		dis = new DataInputStream(((InputConnection)con).openInputStream());
		return true;
	}

	/**
	 * pc��ڑ��҂���Ԃɂ���
	 * @return ������:true ���s��:false
	 * @throws IOException
	 */
	public boolean waitSig() throws IOException {
		isServerMode = true;
		scn = (StreamConnectionNotifier) Connector.open("btspp://localhost:1");
		if (scn == null) {
			return false;
		}
		connection = scn.acceptAndOpen();
		
		dis = connection.openDataInputStream();
		dos = connection.openDataOutputStream();
		
		return true;
	}

	/**
	 * �I�u�W�F�N�g����M����
	 * @return ��M�����I�u�W�F�N�g
	 * @throws IOException
	 */
	public Object getSig() throws IOException, ClassNotFoundException{
		ObjectInputStream ois = new ObjectInputStream(dis);
		return ois.readObject();
	}

	/**
	 * �I�u�W�F�N�g�𑗐M����
	 * @param data ���M����I�u�W�F�N�g
	 * @param device
	 * @throws IOException
	 */
	public void sendSig(Object data) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(dos);
		oos.writeObject(data);
		oos.flush();
	}
	
	public void sendBoolSig(boolean b) throws IOException{
		dos.writeBoolean(b);
		dos.flush();
	}

	/**
	 * �ڑ������
	 * @param command
	 * @param device
	 * @return
	 * @throws IOException
	 */
	public String closeSig() throws IOException {
		dis.close();
		dos.close();
		if (isServerMode) {
			scn.close();
			connection.close();
		} else {
			con.close();
		}
		return "OK";
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
