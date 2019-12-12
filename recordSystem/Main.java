package recordSystem;

import java.util.ArrayList;

//テストプログラム
//※標準のtoStringは変えてないから少し見にくい

public class Main {
	
	
	public static void main(String args[]){
		RequestInformation info1 = new RequestInformation("金子","斉藤","080-3110-kaneko","神奈川県");
		RequestInformation info2 = new RequestInformation("斉藤","金子","080-3110-saito","埼玉県");
		Luggage lug1 = new Luggage(12345,5,info1);
		Luggage lug2 = new Luggage(54321,10,info1);
		Luggage lug3 = new Luggage(678910,15,info2);
		ArrayList<Luggage> luggageList1 = new ArrayList<Luggage>();
		ArrayList<Luggage> luggageList2 = new ArrayList<Luggage>();
		luggageList1.add(lug1);
		luggageList1.add(lug2);
		luggageList2.add(lug3);
		DeliveryRecord DR1 = new DeliveryRecord(1,luggageList1);
		DeliveryRecord DR2 = new DeliveryRecord(2,luggageList2);
		
		DeliveryRecordList DRL =new DeliveryRecordList();
		
		DRL.addDeliveryRecord(DR1);
		DRL.addDeliveryRecord(DR2);
		
		System.out.println(DRL);
		
		
	
	}

}
