import java.io.IOException;

import receiveSystem.Receiver;
public class test_receiver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Receiver r = new Receiver("saito",3);//‚±‚±‚Åî•ñ‚ğ•Ï‚¦‚é
	      try{
	    	r.getLug();
	      }catch(IOException e){
	    	e.printStackTrace();
	      }
	}

}
