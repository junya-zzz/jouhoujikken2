package signal;

import java.io.*;
import java.net.Socket;

import javax.microedition.io.*;

public class BTController extends Thread{
	private StreamConnectionNotifier scn = null;
	private StreamConnection connection = null;
	private ObjectInputStream btois;
	private ObjectOutputStream btoos;
	private ObjectInputStream cois;
	private ObjectOutputStream coos;

	public static void main(String[] args) throws IOException, ClassNotFoundException{
		BTController controller = new BTController();
		while (true) {
			int port = controller.waitBTSig();
			controller.connectToSubSystem(port);
		}
	}
	
	// EV3からシステムにオブジェクトを流す
	public void run(){
		try {
			// 毎回オブジェクトを受け取る前に bool を受け取って close するか決定する
			while(btois.readBoolean()) {
				coos.writeObject(btois.readObject());
			}
			btois.close();
			btoos.close();
			cois.close();
			coos.close();
			connection.close();
			scn.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	// システムからEV3にオブジェクトを流す
	public void systemToObject(){
		try {
			while (cois.readBoolean()) {
				btoos.writeObject(cois.readObject());
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * pcをEV3から接続待ち状態にする
	 * @return 成功時:true 失敗時:false
	 * @throws IOException
	 */
	private int waitBTSig() throws IOException {
		scn = (StreamConnectionNotifier) Connector.open("btspp://localhost:1");
		if (scn == null) {
			System.exit(1);
		}
		connection = scn.acceptAndOpen();
		
		btois = new ObjectInputStream(connection.openDataInputStream());
		btoos = new ObjectOutputStream(connection.openDataOutputStream());
		
		// 接続するポート番号を返す
		return btois.readInt();
	}
	
	//各サブシステムに接続
	private void connectToSubSystem(int port) throws IOException{
		try {
			Socket sc = new Socket("localhost", port);
			cois = new ObjectInputStream(sc.getInputStream());
			coos = new ObjectOutputStream(sc.getOutputStream());
			coos.writeBoolean(true);
		} catch (IOException e) {
			// サブシステムに接続できなかったらEV3側に接続不可を知らせて接続を閉じる
			btoos.writeBoolean(false);
			btoos.close();
			btois.close();
			connection.close();
			scn.close();
		}
		// サブシステムに接続できたらEV3側に接続可能を知らせて通信開始
		btoos.writeBoolean(true);
		start();
		systemToObject();
	}
}
