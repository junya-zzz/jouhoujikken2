package relaySystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import lejos.hardware.lcd.LCD;
import recordSystem.*;
import signal.*;

/**
 * 中継所クラス
 * <PRE>
 * 荷物を保管したり、配達結果を本部に報告する。
 * 次のメソッドを持つ。
 * </PRE>
 * <OL>
 *  <LI> void choseFunction()
 *  <LI> void receiveLugfromCollectingRobot()
 *  <LI> void reportReceivingToHeadquarters(Date ship_time)
 *  <LI> void receiveDeliveryResult()
 *  <LI> void sendLugtoDeliveryRobot()
 *  <LI> void reportDeliveryResult(LuggageCondition result, int lug_id, Date fin_time)
 *  <LI> void reportDeliveryStart(int id)
 *  <LI> ArrayList&lt;Luggage&gt;getLuggageList()
 * </OL>
 * @author bp17027 Keita Kaneko
 * @version 1.0
 */
public class RelayStation {

	/**
	 * フラグ0<br>
	 * 中継所が荷物を受けとる
	 */
	public static int SEND_LUG_TO_RELAY = 0;
	
	/**
	 * フラグ1<br>
	 * 配達担当ロボットへ荷物を引き渡す
	 */
	public static int SEND_LUG_TO_DELIVERY = 1;
	
	/**
	 * フラグ2<br>
	 * 配達結果を引き渡す
	 */
	public static int REPORT_DELIVERY_RESULT = 2;

	/**
	 * 保管荷物
	 */
	private ArrayList<Luggage> luggageList;

	/**
	 * 宛先間違い保管荷物
	 */
	private ArrayList<Luggage> wrongLugList;

	/**
	 * 通信用オブジェクト
	 */
	private EV3Signal signal;

	/**
	 * コンストラクタ
	 */
	public RelayStation(){
		luggageList = new ArrayList<Luggage>();
		wrongLugList = new ArrayList<Luggage>();
		signal = new EV3Signal();
	}

	/**
	 * main文<br>
	 * システム起動時はこのメソッドを使用する
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		RelayStation rs = new RelayStation();
		while (true) {
			rs.choseFunction();
		}
	}

	/**
	 * 処理を区別するメソッド<br>
	 * 荷物の受け取り or 荷物の引き渡し or 配達結果の報告
	 * @throws IOException
	 */
	public void choseFunction() throws IOException{
		signal.waitSig();
		int methodFlag = (int) signal.getSig();
		if (methodFlag == SEND_LUG_TO_RELAY) {
			receiveLugfromCollectingRobot();
		} else if (methodFlag == SEND_LUG_TO_DELIVERY) {
			sendLugtoDeliveryRobot();
		} else if (methodFlag == REPORT_DELIVERY_RESULT) {
			receiveDeliveryResult();
		}
	}
	
	/**
	 * 収集担当ロボットから荷物を受け取る
	 */
	public void receiveLugfromCollectingRobot() {
		try{
			Luggage luggage = (Luggage)signal.getSig();
			luggageList.add(luggage);
			LCD.clear();
			LCD.drawString("luggage:"+luggageList.get(0).getLuggageID(), 0, 2);
			LCD.refresh();
			signal.sendSig("Receiving completed.");
			signal.closeSig();
			reportReceivingToHeadquarters(new Date(1200));
		}catch(Exception e){
			System.out.println("Sorry, Don't receive.");
		}
	}

	/**
	 * 収集担当ロボットからの荷物の受け取りを本部に報告する
	 * @param ship_time 発送時間
	 */
	public void reportReceivingToHeadquarters(Date ship_time) {
		try{
			signal.openSig(Port.HEAD);
			//signal.waitSig();
			signal.sendSig(1);
			signal.sendSig(luggageList.get(0).getLuggageID());
			//signal.sendSig(1);
			signal.sendSig(LuggageCondition.waitDelivering);
			signal.sendSig(ship_time);
			signal.closeSig();
		}catch(Exception e){
			System.out.println("Sorry, Don't report.");
		}
	}

	/**
	 * 荷物の配達結果の報告を受け,本部にも報告する
	 * @throws IOException
	 */
	public void receiveDeliveryResult() throws IOException{
		int id = 0;
		Date time = null;
		Luggage luggage = null;

		LuggageCondition result = (LuggageCondition)signal.getSig();
		if(result == LuggageCondition.finished){
			time = new Date((long)signal.getSig());
			id = (int)signal.getSig();
			LCD.clear();
			LCD.drawString("time"+time, 0, 2);
			LCD.refresh();
		}
		else if(result == LuggageCondition.receive_absence){
			luggage = (Luggage)signal.getSig();
			id = luggage.getLuggageID();
			luggageList.add(luggage);
		}
		else if(result == LuggageCondition.wrongAddress){
			luggage = (Luggage)signal.getSig();
			id = luggage.getLuggageID();
			wrongLugList.add(luggage);
			LCD.clear();
			LCD.drawString("ID:"+wrongLugList.get(0).getLuggageID(), 0, 2);
			LCD.refresh();
		}
		signal.closeSig();
		this.reportDeliveryResult(result, id, time);
	}

	/**
	 * 配達担当ロボットに荷物を渡し、本部に報告する
	 */
	public void sendLugtoDeliveryRobot() {
		try{
			signal.getSig();
			LCD.clear();
			LCD.drawString("message received.", 0, 2);
			LCD.refresh();
			if(luggageList.isEmpty()){
				LCD.clear();
				LCD.drawString("empty.", 0, 2);
				LCD.refresh();
				signal.sendSig("true");
				signal.closeSig();
			}
			else{
				Luggage luggage = luggageList.remove(0);
				int id = luggage.getLuggageID();
				//DeliveryRecord dr = new DeliveryRecord(id, luggageList.get(0));
				//Date start = dr.getStartTime();
				//signal.sendSig(luggageList.isEmpty());
				signal.sendSig(luggage);
				signal.closeSig();
				this.reportDeliveryStart(id);
			}
		}catch(Exception e){
			System.out.println("Sorry, Don't report.");
		}
	}

	/**
	 * 荷物の配達結果を本部に報告する
	 * @param result 荷物の配達結果
	 * @param lug_id 荷物ID
	 * @param fin_time 配達完了時間
	 * @throws IOException
	 */
	public void reportDeliveryResult(LuggageCondition result, int lug_id, Date fin_time)throws IOException {
		//try{
		signal.openSig(Port.HEAD);
		//signal.openSig("Headquarters");
		//signal.waitSig();
		signal.sendSig(2);
		signal.sendSig(lug_id);
		signal.sendSig(result);
		signal.sendSig(fin_time);
		signal.closeSig();
		/*}catch(Exception e){
			System.out.println("Sorry, Don't report.");

		}*/
	}

	/**
	 * 配達担当ロボットに荷物を渡したことを本部に報告する
	 * @param id 荷物ID
	 */
	public void reportDeliveryStart(int id) {
		try{
			//signal.openSig("Headquarters");
			signal.openSig(Port.HEAD);
			//signal.waitSig();
			signal.sendSig(1);
			signal.sendSig(id);
			signal.sendSig(LuggageCondition.delivering);
			signal.sendSig(new Date(0));
			signal.closeSig();
		}catch(Exception e){
			System.out.println("Sorry, Don't report.");
		}
	}

	/**
	 * 荷物リストのgetterメソッド
	 * @return 荷物リスト
	 */
	public ArrayList<Luggage> getLuggageList(){
		return luggageList;
	}

}
