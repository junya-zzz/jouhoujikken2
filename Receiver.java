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

		if(sig1.getSig().equals("false")) sig1.closeSig("FINISH",DeliveryRobot);    //false→10秒以内に返信できなかった

		else{

		    sig1.sendSig(receiverName,DeliveryRobot);

		    sig1.sendSig(receiverAddress,DerliveryRobot);

		
		    if(sig1.getSig().equals("false")) sig1.closeSig();

		    else{

			sig1.getSig();     //荷物受け取り

			sig1.closeSig("FINISH",DeliveryRobot);

		    }

		}

	    }

}
