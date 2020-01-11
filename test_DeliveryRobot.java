import recordSystem.Luggage;
import recordSystem.RequestInformation;
import robotSystem.DeliveryRobot;
public class test_DeliveryRobot {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DeliveryRobot dr = new DeliveryRobot();
		RequestInformation ri = new RequestInformation("keita","saito","000-1111-2222",1);
		Luggage lug = new Luggage(1,"keita",ri);
		
		dr.setLuggage(lug);
		//dr.sendDeliveryRecord();
		
		dr.getLug();

	}

}
