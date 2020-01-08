package receptionSystem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
	private int portNum;
	private String readClientName;
	private String readReceiverName;
	private String readClientPhoneNum;
	private String readAddress;
	private String readLuggageName;

	public SocketServer(int portNum) {
		this.portNum=portNum;
	}

	public  void read () {
		// サーバーソケットを生成＆待機
		try {
			ServerSocket serverSocket = new ServerSocket(portNum) ;
		      Socket socket = serverSocket.accept();
		      System.out.println("接続されました "
                      + socket.getRemoteSocketAddress() );
		      BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		      this.readClientName=reader.readLine();
		      this.readReceiverName=reader.readLine();
		      this.readClientPhoneNum=reader.readLine();
		      this.readAddress=reader.readLine();
		      this.readLuggageName=reader.readLine();

		      System.out.println("clientName = "+this.readClientName);
		      System.out.println("readLuggageName"+this.readLuggageName);

		      serverSocket.close();
		    } catch (Exception e) {
		      e.printStackTrace();
		    }

	}
}
