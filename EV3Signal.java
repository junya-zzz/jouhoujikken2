package signal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.BTConnection;
import lejos.utility.Stopwatch;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * ev3用の通信クラス
 */
public class EV3Signal {
	
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	private BTConnection btConnection = null;
	private BTConnector btConnector = null;
	private boolean isServerMode;

	/**
	 * ev3からdeviceに接続する
	 * @param device 接続するBluetoothデバイスのMACアドレス
	 * @param timeout タイムアウトまでの時間 タイムアウトしたくない時は0にする
	 * @return 成功時:"OK" タイムアウトしたとき:"failed"
	 */
	public String openSig(String device, int timeout) {
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.reset();
		isServerMode = false;
		btConnector = new BTConnector();
		btConnection = null;
		while (btConnection == null) {
			btConnection = btConnector.connect(device, BTConnection.RAW);
			if (timeout > 0 && stopwatch.elapsed() > timeout)
				break;
		}
		
		if (btConnection == null) {
			return "failed";
		}
		
		dis = btConnection.openDataInputStream();
		dos = btConnection.openDataOutputStream();
		
		return "OK";
	}

	/**
	 * ev3を接続待ち状態にする
	 * @return 成功時:"OK" 失敗時:"failed"
	 */
	public String waitSig() {
		isServerMode = true;
		btConnector = new BTConnector();
		btConnection = btConnector.waitForConnection(0, BTConnection.RAW);
		if (btConnection == null) {
			return "failed";
		}
		dis = btConnection.openDataInputStream();
		dos = btConnection.openDataOutputStream();
		return "OK";
	}

	/**
	 * オブジェクトを受信する
	 * @return 受信したオブジェクト
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

		}
		return object;
	}

	/**
	 * オブジェクトを送信する
	 * @param data 送信するオブジェクト
	 * @throws IOException
	 */
	public void sendSig(Object data) throws IOException{
		byte[] bytes = serialize(data);
		dos.writeInt(bytes.length);
		dos.write(bytes);
		dos.flush();
	}

	/**
	 * 通信を閉じる
	 * @return
	 * @throws IOException
	 */
	public String closeSig() throws IOException{
		dis.close();
		dos.close();
		
		if (isServerMode) {
			btConnector.close();
		}
		btConnection.close();
		
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
