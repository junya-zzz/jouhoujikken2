package receiveSystem;

public class Receiver {
	
	  private String receiverName;

	    aruiprivate String receiverAddress;

	 
	    Receiver(String name,String ad){

		receiverName = name;

		receiverAddress = ad;

	    }

	  
	    public void getLug() {

	    Signal sig1 = new Signal();
	    	
		sig1.waitSig();

		sig1.getSig();

		sig1.sendSig("Exist",DeliveryRobot);

		if(sig1.getSig().equals("false")) sig1.closeSig("FINISH",DeliveryRobot);    //falseÅ®10ïbà»ì‡Ç…ï‘êMÇ≈Ç´Ç»Ç©Ç¡ÇΩ

		else{

		    sig1.sendSig(receiverName,DeliveryRobot);

		    sig1.sendSig(receiverAddress,DerliveryRobot);

		
		    if(sig1.getSig().equals("false")) sig1.closeSig();

		    else{

			sig1.getSig();     //â◊ï®éÛÇØéÊÇË

			sig1.closeSig("FINISH",DeliveryRobot);

		    }

		}

	    }

}
