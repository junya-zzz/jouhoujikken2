package signal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *パソコン用の通信クラス
 */
public class PCSignal {

	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	private ServerSocket ss = null;
	private Socket sc = null;
	private boolean isConnectToBT;
	private boolean isServer;
	
	/**
	 * 他のシステムに接続する
	 * @param p 接続するシステムのPort
	 * @return 成功時:true 失敗時:false
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
	 * システムを接続待ち状態にする
	 * @param p 自分のシステムを入れる receiveSystemだったらPort.RECEIVE_PORT
	 * @return 成功時:true 失敗時:false
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
			System.exit(1);
		}
		return object;
	}

	/**
	 * オブジェクトを送信する
	 * @param data 送信するオブジェクト
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
	 * 接続を閉じる
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
