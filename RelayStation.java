package relaySystem;

import java.util.ArrayList;
import java.util.Date;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
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
	public void receiveLugfromCollectingRobot() {
		try{
			signal.waitSig();
			Luggage luggage = (Luggage)signal.getSig();
			luggageList.add(luggage);
			LCD.clear();
			LCD.drawString("luggage:"+luggageList.get(0).getLuggageID(), 0, 2);
			Delay.msDelay(5000);
			LCD.refresh();
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
	public void reportReceivingToHeadquarters(Date ship_time) {
		try{
			signal.openSig("00:1b:dc:f2:2c:33");
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
	 *
	 * 手順
	 * メソッドに対応する操作を行う
	 */
	public boolean receiveDeliveryResult() throws Exception{
		int id = 0;
		Date time = null;
		Luggage luggage = null;
		
		try{
			//signal.openSig("00:16:53:5D:D3:BC");
			signal.waitSig();
			LCD.clear();
			LCD.drawString("pass", 0, 2);
			Delay.msDelay(3000);
			String result = (String)signal.getSig();
			LCD.clear();
			LCD.drawString("result:"+result, 0, 2);
			Delay.msDelay(3000);
			if(result.equals("finished")){
				time = new Date((long)signal.getSig());
				id = (int)signal.getSig();
				LCD.clear();
				LCD.drawString("time"+time, 0, 2);
				Delay.msDelay(5000);
				LCD.refresh();
				return true;
				//System.out.println(time + "," + id);
			}
			else if(result.equals("absence")){
				luggage = (Luggage)signal.getSig();
				LCD.clear();
				LCD.drawString("luggage:"+luggage.getLuggageID(), 0, 2);
				Delay.msDelay(10000);
				LCD.refresh();
				luggageList.add(luggage);
				LCD.clear();
				LCD.drawString("ID:"+luggageList.get(0).getLuggageID(), 0, 2);
				Delay.msDelay(10000);
				LCD.refresh();
				return true;
				//System.out.println(luggageList.get(0).getLuggageID());
			}
			else if(result.equals("wrongAddress")){
				luggage = (Luggage)signal.getSig();
				wrongLugList.add(luggage);
				LCD.clear();
				LCD.drawString("ID:"+wrongLugList.get(0).getLuggageID(), 0, 2);
				Delay.msDelay(2000);
				LCD.refresh();
				return true;
				//System.out.println(wrongLugList.get(0).getLuggageID());
			}
			signal.closeSig();
			return false;
			//this.reportDeliveryResult(result, id, time);
		}catch(Exception e){
			System.out.println("Sorry, Don't receive report.");
			throw e;
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
			signal.waitSig();
			signal.getSig();
			LCD.clear();
			LCD.drawString("message received.", 0, 2);
			Delay.msDelay(2000);
			LCD.refresh();
			if(luggageList.isEmpty()){
				LCD.clear();
				LCD.drawString("empty.", 0, 2);
				Delay.msDelay(2000);
				LCD.refresh();
				signal.sendSig("true");
				signal.closeSig();
			}
			else{
				Luggage luggage = luggageList.get(0);
				int id = luggage.getLuggageID();
				DeliveryRecord dr = new DeliveryRecord(id, luggageList.get(0));
				Date start = dr.getStartTime();
				//signal.sendSig(luggageList.isEmpty());
				signal.sendSig(luggage);
				signal.closeSig();
				//this.reportDeliveryStart(id, start);
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
			signal.openSig("00:1b:dc:f2:2c:33");
			//signal.openSig("Headquarters");
			//signal.waitSig();
			if(result.equals("finished")){
			  signal.sendSig(1);
			  signal.sendSig(lug_id);
			  signal.sendSig(LuggageCondition.finished);
			  signal.sendSig(fin_time);
			}
			else if(result.equals("absence")){
			  signal.sendSig(1);	
			  signal.sendSig(lug_id);
			  signal.sendSig(LuggageCondition.absence);
			  signal.sendSig(fin_time);
			}
			else if(result.equals("wrongAddress")){
			  signal.sendSig(1);	
			  signal.sendSig(lug_id);
		      signal.sendSig(LuggageCondition.wrongAddress);
		      signal.sendSig(fin_time);
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
			//signal.openSig("Headquarters");
			signal.openSig("00:1b:dc:f2:2c:2c");
			//signal.waitSig();
			signal.sendSig(id);
			signal.sendSig(start);
			signal.closeSig();
		}catch(Exception e){
			System.out.println("Sorry, Don't report.");
		}
	}
	
	public ArrayList<Luggage> getLuggageList(){
		return luggageList;
	}

}
