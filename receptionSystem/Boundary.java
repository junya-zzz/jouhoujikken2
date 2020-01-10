package receptionSystem;

import java.io.*;
import recordSystem.*;

public class Boundary {



	public RequestInformation inputReqInfo() {
		RequestInformation requestInformation = null;
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

			requestInformation = new RequestInformation(str1,str2,str3,Integer.parseInt(str4));

		}catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}
		return requestInformation;
	}

}
