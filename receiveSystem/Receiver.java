package receiveSystem;
import java.io.IOException;
import signal.PCSignal;
public class Receiver {
	
	  private static String receiverName;

	    private static Integer receiverAddress;

	 
	    Receiver(String name,Integer ad){

		receiverName = name;

		receiverAddress = ad;

	    }

	    public static void main(String[] args) {
	    	new Receiver("�ē�",3);//�����ŏ���ς���
	    	try{
	    	while(true){
	    	getLug();
	    	}
	    	}catch(IOException e){
	    		e.printStackTrace();
	    	}
	    	}
	    
	    
	    
	    private static void getLug() throws IOException{

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

	    }

}
