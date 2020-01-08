package receptionSystem;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient {
	private int portNum;

	public SocketClient(int portNum) {
		this.portNum=portNum;
	}

	public  void send(String readClientName,String readReceiverName,String readClientPhoneNum,String readAddress, String readLuggageName) {
		try {
			Socket socket = new Socket("localhost",portNum);
			System.out.println("接続しました");
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			writer.println(readClientName);
			writer.println(readReceiverName);
			writer.println(readClientPhoneNum);
			writer.println(readAddress);
			writer.println(readLuggageName);
			socket.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
