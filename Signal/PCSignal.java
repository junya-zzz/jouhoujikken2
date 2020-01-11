package signal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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

	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	private ServerSocket ss = null;
	private Socket sc = null;
	
	/**
	 * pcからev3に接続する
	 * @param command
	 * @param device 接続するev3のMACアドレス
	 * @return 成功時:true 失敗時:false
	 * @throws IOException
	 */
	/*
	public boolean openSig(String device) throws IOException{
		isServerMode = false;
		con = Connector.open("btspp://" + device + ":1");
		if (con == null)
			return false;
		dos = new DataOutputStream(((OutputConnection)con).openOutputStream());
		dis = new DataInputStream(((InputConnection)con).openInputStream());
		return true;
	}
	*/

	/**
	 * システムを接続待ち状態にする
	 * @return 成功時:true 失敗時:false
	 * @throws IOException
	 */
	public Boolean waitSig(Port p) throws IOException {
		ss = new ServerSocket(p.portNum);
		sc = ss.accept();
		ois = new ObjectInputStream(sc.getInputStream());
		oos = new ObjectOutputStream(sc.getOutputStream());
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
		oos.writeBoolean(true);
		oos.writeObject(data);
		oos.flush();
	}
	
	/**
	 * 接続を閉じる
	 * @return
	 * @throws IOException
	 */
	public String closeSig() throws IOException {
		oos.writeBoolean(false);
		ois.close();
		oos.close();
		ss.close();
		sc.close();
		return "OK";
	}
}
