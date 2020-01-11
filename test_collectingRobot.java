import robotSystem.*;
import recordSystem.Luggage;
import recordSystem.RequestInformation;
import relaySystem.*;
public class test_collectingRobot {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws ClassNotFoundException{
		CollectingRobot cr = new CollectingRobot();
		RelayStation rs = new RelayStation();
		RequestInformation ri = new RequestInformation("keita","saito","000-1111-2222",1);
		Luggage lug = new Luggage(1,"keita",ri);
		
		cr.setLuggage(lug);
		cr.sendLug();
		
	}

}
