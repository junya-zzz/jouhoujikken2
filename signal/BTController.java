package signal;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.Socket;
import java.util.List;

import javax.microedition.io.*;

import com.intel.bluetooth.BlueCoveImpl;

public class BTController extends Thread{
	public StreamConnectionNotifier scn = null;
	private StreamConnection connection = null;
	private ObjectInputStream btois;
	private ObjectOutputStream btoos;
	private ObjectInputStream cois;
	private ObjectOutputStream coos;

	public static void main(String[] args) throws IOException, ClassNotFoundException{
		BTController controller = new BTController();
		controller.scn = (StreamConnectionNotifier) Connector.open("btspp://localhost:1");
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
				coos.flush();
			}
			btois.close();
			btoos.close();
			connection.close();
			System.out.println("closed connection.");
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
				btoos.flush();
			}
			cois.close();
			coos.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * PCをEV3から接続待ち状態にする
	 * @return 成功時:true 失敗時:false
	 * @throws IOException
	 */
	private int waitBTSig() throws IOException {
		if (scn == null) {
			System.exit(1);
		}
		System.out.println("waiting ev3 sig...");
		connection = scn.acceptAndOpen();
		
		DataOutputStream dos = connection.openDataOutputStream();
		DataInputStream dis = connection.openDataInputStream();
		btoos = new ObjectOutputStream(dos);
		btoos.flush();
		btois = new ObjectInputStream(dis);
		
		System.out.println("connected to ev3.");
		// 接続するポート番号を返す
		return btois.readInt();
	}
	
	//各サブシステムに接続
	private void connectToSubSystem(int port) throws IOException{
		try {
			System.out.println("connecting to system...");
			System.out.println("port : " + port);
			Socket sc = new Socket("localhost", port);
			coos = new ObjectOutputStream(sc.getOutputStream());
			coos.flush();
			cois = new ObjectInputStream(sc.getInputStream());
			coos.writeBoolean(true);
			coos.flush();
		} catch (IOException e) {
			// サブシステムに接続できなかったらEV3側に接続不可を知らせて接続を閉じる
			btoos.writeBoolean(false);
			btoos.flush();
			btoos.close();
			btois.close();
			connection.close();
			return;
		}
		System.out.println("connected to system.");
		// サブシステムに接続できたらEV3側に接続可能を知らせて通信開始
		btoos.writeBoolean(true);
		btoos.flush();
		start();
		systemToObject();
	}
	
}