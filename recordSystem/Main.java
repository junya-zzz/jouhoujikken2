package recordSystem;
public class Main {
	
	
	public static void main(String args[]){
		RequestInformation info1 = new RequestInformation("���q","�ē�","080-3110-kaneko","�_�ސ쌧");
		RequestInformation info2 = new RequestInformation("�ē�","���q","080-3110-saito","��ʌ�");
		Luggage lug1 = new Luggage(12345,"ddfdf",info1);
		Luggage lug2 = new Luggage(543210,"aaaa",info2);
		//Luggage lug3 = new Luggage(678910,"dfdf",info2);
		//ArrayList<Luggage> luggageList1 = new ArrayList<Luggage>();
		//ArrayList<Luggage> luggageList2 = new ArrayList<Luggage>();
		//luggageList1.add(lug1);
		//luggageList1.add(lug2);
		//luggageList2.add(lug3);
		DeliveryRecord DR1 = new DeliveryRecord(lug1.getLuggageID(),lug1);
		DeliveryRecord DR2 = new DeliveryRecord(lug2.getLuggageID(),lug2);
		
		DeliveryRecordList DRL =new DeliveryRecordList();
		
		DRL.addDeliveryRecord(DR1);
		DRL.addDeliveryRecord(DR2);
		
		System.out.println(DRL);
		
		
	
	}

}
