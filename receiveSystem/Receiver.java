package receiveSystem;
import java.io.IOException;
import signal.*;
public class Receiver {
/**
 * 受取人名
 */
	private static String receiverName;
	/**
	 * 受取人住所
	 */
	private static Integer receiverAddress;


	Receiver(String name,Integer ad){

		receiverName = name;

		receiverAddress = ad;

	}

	public static void main(String[] args) {
		new Receiver("1",1);//ここで情報を変える

		while(true){
			getLug();
		}
		
	}


	/**
	 * 配達担当ロボットから荷物を受け取る
	 */
	private static void getLug(){
		try{
			PCSignal sig1 = new PCSignal();

			sig1.waitSig();

			sig1.getSig();

			sig1.sendSig("Exist");

			if(sig1.getSig().equals("false")) sig1.closeSig();    //false→10秒以内に返信できなかった

			else{

				sig1.sendSig(receiverName);

				sig1.sendSig(receiverAddress);


				if(sig1.getSig().equals("false")) sig1.closeSig();

				else{

					sig1.getSig();     //荷物受け取り

					sig1.closeSig();

				}

			}

		}catch(Exception e){
			System.out.println("error.");
		}  
		
	}

}
