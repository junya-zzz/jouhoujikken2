package recordSystem;
public class Main {
	
	
	public static void main(String args[]){
		RequestInformation info1 = new RequestInformation("驥大ｭ�,"譁芽陸","080-3110-kaneko",123);
		RequestInformation info2 = new RequestInformation("譁芽陸","驥大ｭ�,"080-3110-saito",321);
		Luggage lug1 = new Luggage(12345,"ddfdf",info1);
		Luggage lug2 = new Luggage(543210,"aaaa",info2);
		DeliveryRecord DR1 = new DeliveryRecord(lug1.getLuggageID(),lug1);
		DeliveryRecord DR2 = new DeliveryRecord(lug2.getLuggageID(),lug2);
		
		DeliveryRecordList DRL =new DeliveryRecordList();
		
		DRL.addDeliveryRecord(DR1);
		DRL.addDeliveryRecord(DR2);
		DRL.addDeliveryRecord(new DeliveryRecord(1));
		DRL.addDeliveryRecord(new DeliveryRecord(2));
		
		System.out.println(DRL);
		
		
	
	}

}
