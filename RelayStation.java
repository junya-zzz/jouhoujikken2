package relaySystem;

import java.util.ArrayList;

import sun.misc.Signal;

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
	private Signal signal;

	public RelayStation(){
		luglist = new ArrayList<Luggage>();
		wrongLugList = new ArrayList<Luggage>();
		signal = new Signal();
	}

	/**
	 * 収集担当ロボットから荷物を受け取り、本部に報告する
	 *
	 * 手順
	 * メソッドに対応する操作を行う
	 */
	public void receiveLugfromGatheringRobot() {
		try {
			signal.openSig("START","GatheringRobot");
			signal.waitSig();
			Luggage luggage = (Luggage)signal.getData();
			luggagelist.add(luggage);
			signal.sendData("Receiving completed.","GatheringRobot");
			signal.closeSig("FINISH","GatheringRobot");
			this.reportReceivingToHeadquarters(luggage.getLuggageID());
		}catch(Exception e) {
			System.out.println("Sorry. Don't receive luggage.");
		}
	}

	/**
	 * 収集担当ロボットからの荷物の受け取りを本部に報告する
	 *
	 * 手順
	 * メソッドに対応する操作を行う
	 */
	private void reportReceivingToHeadquarters(int id) {
		signal.openSig("START","Headquarters");
		signal.waitSig();
		signal.sendData(id,"Headquarters");
		signal.closeSig("FINISH","Headquarters");
	}

	/**
	 * 荷物の配達結果の報告を受け,本部にも報告する
	 *
	 * 手順
	 * メソッドに対応する操作を行う
	 */
	public void receiveDeliveryResult() {
		try {
			signal.openSig("START","DeliveryRobot");
			signal.waitSig();
			String result = (String)signal.getData();
			if(result.equals("finished")){
				String time = (String)getData();
				int id = (int)getData();
			}
			else if(result.equals("absence")){
				Luggage luggage = (Luggage)signal.getData();
				luggagelist.add(luggage);
			}
			else if(result.equals("wrongAddress")){
				Luggage luggage = (Luggage)signal.getData();
				wrongLugList.add(luggage);
			}
			signal.closeSig("FINISH","DeliveryRobot");
			this.reportDeliveryResult(result, id, time);
		}catch(Exception e) {
			System.out.println("Sorry. Don't receive report.");
		}
	}

	/**
	 * 配達担当ロボットに荷物を渡し、本部に報告する
	 *
	 * 手順
	 * メソッドに対応する操作を行う
	 */
	public void sendLugtoDeliveryRobot() {
		try {
			signal.openSig("START","DeliveryRobot");
			signal.waitSig();
			signal.getData((String)"荷物があるかどうか","DeliveryRobot");
			if(luggagelist.isEmpty()){
				signal.sendData(luggagelist.isEmpty(),"DeliveryRobot");
				signal.closeSig("FINISH","DeliveryRobot");
			}
			else{
				Luggage luggage = luggagelist.get(0);
				int id = luggage.getLuggageID();
				DeliveryRecord dr = new DeliveryRecord(id, luggagelist);
				Date start = dr.getStartTime();
				signal.sendData(luggagelist.isLuggage(),"DeliveryRobot");
				signal.sendData(luggage,"DeliveryRobot");
				signal.closeSig("FINISH","DeliveryRobot");
				this.reportDeliveryStart(id, start);
			}
		}catch(Exception e) {
			System.out.println("Sorry. Error happened.");
		}
	}

	/**
	 * 荷物の配達結果を本部に報告する
	 *
	 * 手順
	 * メソッドに対応する操作を行う
	 */
	private void reportDeliveryResult(String result, int id, String fin_time) {
		signal.openSig("START","Headquarters");
		signal.waitSig();
		if(result.equals("finished")){
		  sendData(id,"Headquarters");
		  sendData(fin_time,"Headquarters");
		}
		else{
	  	  sendData(id,"Headquarters");
		}
		signal.closeSig("FINISH","Headquarters");
	}

	/**
	 * 配達担当ロボットに荷物を渡したことを本部に報告する
	 *
	 * 手順
	 * メソッドに対応する操作を行う
	 *
	 *
	 */
	private void reportDeliveryStart(int id, Date start) {
		signal.openSig("START","Headquarters");
		signal.waitSig();
		signal.sendData(id,"Headquarters");
		signal.sendData(start,"Headquarters");
		signal.closeSig("FINISH","Headquarters");
	}

}
