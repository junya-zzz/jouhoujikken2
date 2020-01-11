import java.sql.Date;

import recordSystem.Luggage;
import recordSystem.RequestInformation;
import relaySystem.*;

public class test_relaystation {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		RelayStation rs = new RelayStation();
		
		// 荷物の配達結果を本部に報告する
		//rs.receiveDeliveryResult();
		
		// 収集担当ロボットから荷物を受け取る
		//rs.receiveLugfromCollectingRobot();
		
		// 荷物の配達結果を本部に報告する
		/*try{
			Date time = new Date(1200);
			//rs.reportDeliveryResult("wrongAddress", 1, time);
			RequestInformation ri = new RequestInformation("keita","saito","000-1111-2222",1);
			Luggage lug = new Luggage(1,"keita",ri);
			rs.getLuggageList().add(lug);
			rs.reportReceivingToHeadquarters(time);
		}catch(Exception e){
			System.out.println("error.");
			throw e;
		}*/
		
		RequestInformation ri = new RequestInformation("keita","saito","000-1111-2222",1);
		Luggage lug = new Luggage(1,"keita",ri);
		
		// 配達担当ロボットへ荷物を渡す
		rs.getLuggageList().add(lug);
		rs.sendLugtoDeliveryRobot();
	}

}
