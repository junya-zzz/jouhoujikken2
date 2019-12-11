package receptionSystem;

import java.io.*;

public class Boundary {
    
   

    public RequestInformation inputReqInfo() {
	String str1 = null;
	String str2 = null;
	String str3 = null;
	String str4 = null;
	BufferedReader br = new BufferedReader(new InputStreamReader
					       (System.in));

	try{
	    System.out.print("clientName => ");
	    str1 = br.readLine();
	    System.out.print("clentPhoneNum => ");
	    str2 = br.readLine();
	    System.out.print("receiverName => ");
	    str3 = br.readLine();	    
	    System.out.print("receiverAddress => ");
	    str4 = br.readLine();

	    return new RequestInformation(str1,str2,str3,str4);



	}catch(IOException e){
	    e.printStackTree();
	}
    }
        
}




