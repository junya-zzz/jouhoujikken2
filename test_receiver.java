import java.io.IOException;

import receiveSystem.Receiver;
public class test_receiver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Receiver r = new Receiver("saito",3);//�����ŏ���ς���
	      try{
	    	r.getLug();
	      }catch(IOException e){
	    	e.printStackTrace();
	      }
	}

}
