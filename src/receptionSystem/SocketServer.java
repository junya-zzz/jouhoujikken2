package receptionSystem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class SocketServer {
	private int portNum;
	private String clientName;
	private String receiverName;
	private String clientPhoneNum;
	private String receiverAddress;
	private String luggageName;

	public SocketServer(int portNum) {
		this.portNum=portNum;
	}

	public  String[] read () {
		// サーバーソケットを生成＆待機

		try {
			ServerSocket serverSocket = new ServerSocket(portNum) ;
		      Socket socket = serverSocket.accept();
		      System.out.println("接続されました "
                      + socket.getRemoteSocketAddress() );
		      BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		      this.clientName=reader.readLine();
		      this.receiverName=reader.readLine();
		      this.clientPhoneNum=reader.readLine();
		      this.receiverAddress=reader.readLine();
		      this.luggageName=reader.readLine();

		      System.out.println("clientName = "+this.clientName);
		      System.out.println("readLuggageName"+this.luggageName);

		      serverSocket.close();
		      String[] result={clientName,receiverName,clientPhoneNum,receiverAddress,luggageName};
		      return result;
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
			return null;


	}
}
