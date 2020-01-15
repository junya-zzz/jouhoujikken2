package recordSystem;

import java.util.Date;

public class Main {
	
	
	public static void main(String args[]){
		RequestInformation info1 = new RequestInformation("a","b","080-3110-kaneko",123);
		RequestInformation info2 = new RequestInformation("b","a","080-3110-saito",321);
		Luggage lug1 = new Luggage(12345,"ddfdf",info1);
		Luggage lug2 = new Luggage(543210,"aaaa",info2);
		DeliveryRecord DR1 = new DeliveryRecord(lug1.getLuggageID(),lug1);
		DeliveryRecord DR2 = new DeliveryRecord(lug2.getLuggageID(),lug2);
		DR1.setReceiptTime(new Date());
		
		DeliveryRecordList DRL =new DeliveryRecordList();
		
		DRL.addDeliveryRecord(DR1);
		DRL.addDeliveryRecord(DR2);
		
		System.out.println(DRL);
		
		
	
	}

}
