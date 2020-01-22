package signal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * パソコン用の通信クラス
 * @author bp17048
 *
 */
public class PCSignal {

	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	private ServerSocket ss = null;
	private Socket sc = null;
	/**
	 * ev3と接続しているときはtrue、パソコンと接続しているときはfalse
	 */
	private boolean isConnectToBT;
	/**
	 * サーバーとして接続したときtrue
	 */
	private boolean isServer;
	
	/**
	 * 他のシステムに接続する
	 * @param p 接続するシステムのPort
	 * @return 成功時:true 失敗時:false
	 * @throws IOException 接続に何らかの失敗があったとき
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
	 * システムを接続待ち状態にする
	 * @param p 自分のシステムを入れる receiveSystemだったらPort.RECEIVE_PORT
	 * @return 成功時:true 失敗時:false
	 * @throws IOException 失敗したとき
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
	 * オブジェクトを受信する
	 * @return 受信したオブジェクト
	 * @throws IOException 受信に失敗
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
	 * @throws IOException 送信に失敗
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
	 * @return 成功したとき"OK"を返す
	 * @throws IOException 閉じることを失敗したとき
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
