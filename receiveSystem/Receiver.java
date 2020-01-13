package receiveSystem;
import java.io.IOException;
import signal.*;
public class Receiver {
/**
 * ���l��
 */
	private static String receiverName;
	/**
	 * ���l�Z��
	 */
	private static Integer receiverAddress;


	Receiver(String name,Integer ad){

		receiverName = name;

		receiverAddress = ad;

	}

	public static void main(String[] args) {
		new Receiver("1",1);//�����ŏ���ς���

		while(true){
			getLug();
		}
		
	}


	/**
	 * �z�B�S�����{�b�g����ו����󂯎��
	 */
	private static void getLug(){
		try{
			PCSignal sig1 = new PCSignal();

			sig1.waitSig();

			sig1.getSig();

			sig1.sendSig("Exist");

			if(sig1.getSig().equals("false")) sig1.closeSig();    //false��10�b�ȓ��ɕԐM�ł��Ȃ�����

			else{

				sig1.sendSig(receiverName);

				sig1.sendSig(receiverAddress);


				if(sig1.getSig().equals("false")) sig1.closeSig();

				else{

					sig1.getSig();     //�ו��󂯎��

					sig1.closeSig();

				}

			}

		}catch(Exception e){
			System.out.println("error.");
		}  
		
	}

}
