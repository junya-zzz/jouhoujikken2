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
		
		// �ו��̔z�B���ʂ�{���ɕ񍐂���
		//rs.receiveDeliveryResult();
		
		// ���W�S�����{�b�g����ו����󂯎��
		//rs.receiveLugfromCollectingRobot();
		
		// �ו��̔z�B���ʂ�{���ɕ񍐂���
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
		
		// �z�B�S�����{�b�g�։ו���n��
		rs.getLuggageList().add(lug);
		rs.sendLugtoDeliveryRobot();
	}

}
