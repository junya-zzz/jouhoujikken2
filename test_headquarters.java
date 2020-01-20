import headquartersSystem.Headquarters;
import recordSystem.*;

public class test_headquarters {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Headquarters hq = new Headquarters();
		RequestInformation ri = new RequestInformation("keita","saito","000-1111-2222",1);
		Luggage lug = new Luggage(1,"keita",ri);
		DeliveryRecord dr = new DeliveryRecord(lug.getLuggageID(), lug);
		hq.addDeliveryRecord(dr);
		hq.systemExe();
	}

}
