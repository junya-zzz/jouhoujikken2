package relaySystem;

import java.util.ArrayList;
import java.util.Date;
import recordSystem.*;
import signal.*;

public class RelayStation {

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

	public RelayStation(){
		luggageList = new ArrayList<Luggage>();
		wrongLugList = new ArrayList<Luggage>();
		signal = new EV3Signal();
	}

	/**
	 * 収集担当ロボットから荷物を受け取る
	 *
	 * 手順
	 * メソッドに対応する操作を行う
	 */
	public void receiveLugfromGatheringRobot() {
		try{
			signal.openSig("GatheringRobot");
			signal.waitSig();
			Luggage luggage = (Luggage)signal.getSig();
			luggageList.add(luggage);
			signal.sendSig("Receiving completed.");
			signal.closeSig();
		}catch(Exception e){
			System.out.println("Sorry, Don't receive.");
		}
	}

	/**
	 * 収集担当ロボットからの荷物の受け取りを本部に報告する
	 *
	 * 手順
	 * メソッドに対応する操作を行う
	 */
	public void reportReceivingToHeadquarters() {
		try{
			signal.openSig("Headquarters");
			signal.waitSig();
			signal.sendSig(luggageList.get(0).getLuggageID());
			signal.closeSig();
		}catch(Exception e){
			System.out.println("Sorry, Don't report.");
		}
	}

	/**
	 * 荷物の配達結果の報告を受け,本部にも報告する
	 *
	 * 手順
	 * メソッドに対応する操作を行う
	 */
	public void receiveDeliveryResult() {
		int id = 0;
		Date time = null;
		
		try{
			signal.openSig("DeliveryRobot");
			signal.waitSig();
			String result = (String)signal.getSig();
			if(result.equals("finished")){
				time = (Date)signal.getSig();
				id = (int)signal.getSig();
			}
			else if(result.equals("absence")){
				Luggage luggage = (Luggage)signal.getSig();
				luggageList.add(luggage);
			}
			else if(result.equals("wrongAddress")){
				Luggage luggage = (Luggage)signal.getSig();
				wrongLugList.add(luggage);
			}
			signal.closeSig();
			this.reportDeliveryResult(result, id, time);
		}catch(Exception e){
			System.out.println("Sorry, Don't receive report.");
		}
	}

	/**
	 * 配達担当ロボットに荷物を渡し、本部に報告する
	 *
	 * 手順
	 * メソッドに対応する操作を行う
	 */
	public void sendLugtoDeliveryRobot() {
		try{
			signal.openSig("DeliveryRobot");
			signal.waitSig();
			signal.getSig();
			if(luggageList.isEmpty()){
				signal.sendSig(luggageList.isEmpty());
				signal.closeSig();
			}
			else{
				Luggage luggage = luggageList.get(0);
				int id = luggage.getLuggageID();
				DeliveryRecord dr = new DeliveryRecord(id, luggageList.get(0));
				Date start = dr.getStartTime();
				signal.sendSig(luggageList.isEmpty());
				signal.sendSig(luggage);
				signal.closeSig();
				this.reportDeliveryStart(id, start);
			}
		}catch(Exception e){
			System.out.println("Sorry, Don't report.");
		}
	}

	/**
	 * 荷物の配達結果を本部に報告する
	 *
	 * 手順
	 * メソッドに対応する操作を行う
	 */
	public void reportDeliveryResult(String result, int lug_id, Date fin_time) {
		try{
			signal.openSig("00:1b:dc:f2:2c:2c");
			//signal.openSig("Headquarters");
			//signal.waitSig();
			if(result.equals("finished")){
			  signal.sendSig(1);
			  signal.sendSig(lug_id);
			  signal.sendSig(LuggageCondition.finished);
			  signal.sendSig(fin_time);
			}
			else{
		  	  signal.sendSig(lug_id);
			}
			signal.closeSig();
		}catch(Exception e){
			System.out.println("Sorry, Don't report.");
		}
	}

	/**
	 * 配達担当ロボットに荷物を渡したことを本部に報告する
	 *
	 * 手順
	 * メソッドに対応する操作を行う
	 *
	 *
	 */
	public void reportDeliveryStart(int id, Date start) {
		try{
			signal.openSig("Headquarters");
			signal.waitSig();
			signal.sendSig(id);
			signal.sendSig(start);
			signal.closeSig();
		}catch(Exception e){
			System.out.println("Sorry, Don't report.");
		}
	}

}
