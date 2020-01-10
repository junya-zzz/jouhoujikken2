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
 *パソコン用の通信クラス
 */
public class PCSignal {

	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	private StreamConnectionNotifier scn = null;
	private StreamConnection connection = null;
	private Connection con = null;
	private boolean isServerMode;

	/**
	 * pcからev3に接続する
	 * @param command
	 * @param device 接続するev3のMACアドレス
	 * @return 成功時:true 失敗時:false
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
	 * pcを接続待ち状態にする
	 * @return 成功時:true 失敗時:false
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
	 * オブジェクトを受信する
	 * @return 受信したオブジェクト
	 * @throws IOException
	 */
	public Object getSig() throws IOException, ClassNotFoundException{
		ObjectInputStream ois = new ObjectInputStream(dis);
		return ois.readObject();
	}

	/**
	 * オブジェクトを送信する
	 * @param data 送信するオブジェクト
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
	 * 接続を閉じる
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

    // オブジェクトをバイト配列に変換する
	// 変換するクラスには implements Serializable をつける
    private static byte[] serialize(Object obj) throws IOException {
        try (ByteArrayOutputStream b = new ByteArrayOutputStream()) {
            try (ObjectOutputStream o = new ObjectOutputStream(b)) {
                o.writeObject(obj);
            }
            return b.toByteArray();
        }
    }

    // バイト配列をオブジェクトに変換する
    private static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream b = new ByteArrayInputStream(bytes)) {
            try (ObjectInputStream o = new ObjectInputStream(b)) {
                return o.readObject();
            }
        }
    }
}
