package signal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.BTConnection;
import lejos.utility.Delay;

/**
 * ev3用の通信クラス
 */
public class EV3Signal {
	
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	private BTConnection btConnection = null;
	private BTConnector btConnector = null;
	private boolean isServerMode;
	private final String BTAddress = "";

	/**
	 * ev3からdeviceに接続する
	 * @param p 接続するシステムのポート Port.RECEIVE_PORT など
	 * @return 成功時:true 失敗時:false
	 */
	public boolean openSig(Port p) throws IOException{
		isServerMode = false;
		btConnector = new BTConnector();
		btConnection = btConnector.connect(BTAddress, BTConnection.RAW);
		
		if (btConnection == null) {
			return false;
		}
		
		ois = new ObjectInputStream(btConnection.openDataInputStream());
		oos = new ObjectOutputStream(btConnection.openDataOutputStream());
		
		// 接続するシステムのポート番号を送る
		oos.writeInt(p.portNum);
		
		return true;
	}

	/**
	 * ev3を接続待ち状態にする
	 * @return 成功時:true 失敗時:false
	 */
	public boolean waitSig() throws IOException{
		isServerMode = true;
		btConnector = new BTConnector();
		btConnection = btConnector.waitForConnection(0, BTConnection.RAW);
		if (btConnection == null) {
			return false;
		}
		ois = new ObjectInputStream(btConnection.openDataInputStream());
		oos = new ObjectOutputStream(btConnection.openDataOutputStream());
		return true;
	}

	/**
	 * オブジェクトを受信する
	 * @return 受信したオブジェクト
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
	 * オブジェクトを送信する
	 * @param data 送信するオブジェクト
	 * @throws IOException
	 */
	public void sendSig(Object data) throws IOException{
		oos.writeBoolean(true);
		oos.writeObject(data);
		oos.flush();
	}

	/**
	 * 通信を閉じる
	 * @return
	 * @throws IOException
	 */
	public void closeSig() throws IOException{
		oos.writeBoolean(false);
		ois.close();
		oos.close();
		
		if (isServerMode) {
			btConnector.close();
		}
		btConnection.close();
		
		return;
	}
}
