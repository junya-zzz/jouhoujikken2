package receiveSystem;
import java.io.IOException;
import signal.PCSignal;
public class Receiver {
	
	  private static String receiverName;
	  private static Integer receiverAddress;

	  public Receiver(String name,Integer ad){
		receiverName = name;
		receiverAddress = ad;
	  }

	  /*public static void main(String[] args) {
		  Receiver r = new Receiver("saito",3);//‚±‚±‚Åî•ñ‚ğ•Ï‚¦‚é
	      try{
	    	while(true){
	    		r.getLug();
	    	}
	      }catch(IOException e){
	    	e.printStackTrace();
	      }
	  }*/
	       
	  public void getLug() throws IOException{
	    PCSignal sig1 = new PCSignal();
		sig1.waitSig();
		sig1.getSig();
		sig1.sendSig("Exist");
		if(sig1.getSig().equals("false")) sig1.closeSig();    //false¨10•bˆÈ“à‚É•ÔM‚Å‚«‚È‚©‚Á‚½
		else{
		    sig1.sendSig(receiverName);
		    sig1.sendSig(receiverAddress);
		    if(sig1.getSig().equals("false")) sig1.closeSig();
		    else{
		    	sig1.getSig();     //‰×•¨ó‚¯æ‚è
		    	System.out.println("received.");
		    	sig1.closeSig();
		    }
		}
	  }

}
