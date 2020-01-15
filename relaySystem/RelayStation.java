package relaySystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import recordSystem.*;
import signal.*;

public class RelayStation {

	/**
	 * �ۊǉו�
	 */
	private ArrayList<Luggage> luggageList;

	/**
	 * ����ԈႢ�ۊǉו�
	 */
	private ArrayList<Luggage> wrongLugList;

	/**
	 * �ʐM�p�I�u�W�F�N�g
	 */
	private EV3Signal signal;

	public RelayStation(){
		luggageList = new ArrayList<Luggage>();
		wrongLugList = new ArrayList<Luggage>();
		signal = new EV3Signal();
	}

	public static void main(String[] args) throws Exception{
		RelayStation rs = new RelayStation();
		rs.receiveLugfromCollectingRobot();
		rs.sendLugtoDeliveryRobot();
		rs.receiveDeliveryResult();
	}
	/**
	 * ���W�S�����{�b�g����ו����󂯎��
	 *
	 * �菇
	 * ���\�b�h�ɑΉ����鑀����s��
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
			reportReceivingToHeadquarters(new Date(1200));
		}catch(Exception e){
			System.out.println("Sorry, Don't receive.");
		}
	}

	/**
	 * ���W�S�����{�b�g����̉ו��̎󂯎���{���ɕ񍐂���
	 *
	 * �菇
	 * ���\�b�h�ɑΉ����鑀����s��
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
	 * �ו��̔z�B���ʂ̕񍐂���,�{���ɂ��񍐂���
	 *
	 * �菇
	 * ���\�b�h�ɑΉ����鑀����s��
	 */
	public void receiveDeliveryResult() throws Exception{
		int id = 0;
		Date time = null;
		Luggage luggage = null;
		
		//try{
			//signal.openSig("00:16:53:5D:D3:BC");
		LCD.clear();
		LCD.drawString("p", 0, 2);
		LCD.refresh();
			signal.waitSig();
			LCD.clear();
			LCD.drawString("pass", 0, 2);
			LCD.refresh();
			Delay.msDelay(3000);
			LuggageCondition result = (LuggageCondition)signal.getSig();
			LCD.clear();
			LCD.drawString("result:"+result, 0, 2);
			LCD.refresh();
			Delay.msDelay(3000);
			if(result == LuggageCondition.finished){
				time = new Date((long)signal.getSig());
				id = (int)signal.getSig();
				LCD.clear();
				LCD.drawString("time"+time, 0, 2);
				Delay.msDelay(5000);
				LCD.refresh();
				//System.out.println(time + "," + id);
			}
			else if(result == LuggageCondition.absence){
				luggage = (Luggage)signal.getSig();
				id = luggage.getLuggageID();
				LCD.clear();
				LCD.drawString("luggage:"+luggage.getLuggageID(), 0, 2);
				Delay.msDelay(10000);
				LCD.refresh();
				luggageList.add(luggage);
				LCD.clear();
				LCD.drawString("ID:"+luggageList.get(0).getLuggageID(), 0, 2);
				Delay.msDelay(10000);
				LCD.refresh();
				//System.out.println(luggageList.get(0).getLuggageID());
			}
			else if(result == LuggageCondition.wrongAddress){
				luggage = (Luggage)signal.getSig();
				id = luggage.getLuggageID();
				wrongLugList.add(luggage);
				LCD.clear();
				LCD.drawString("ID:"+wrongLugList.get(0).getLuggageID(), 0, 2);
				Delay.msDelay(2000);
				LCD.refresh();
				//System.out.println(wrongLugList.get(0).getLuggageID());
			}
			signal.closeSig();
			if(result == LuggageCondition.finished){
				//this.reportDeliveryResult(LuggageCondition.delivered, id, );
				this.reportDeliveryResult(result, id, time);
			}else {
				reportDeliveryResult(result, id, time);
			}
		/*}catch(Exception e){
			System.out.println("Sorry, Don't receive report.");
			throw e;
		}*/
	}

	/**
	 * �z�B�S�����{�b�g�ɉו���n���A�{���ɕ񍐂���
	 *
	 * �菇
	 * ���\�b�h�ɑΉ����鑀����s��
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
	 * �ו��̔z�B���ʂ�{���ɕ񍐂���
	 *
	 * �菇
	 * ���\�b�h�ɑΉ����鑀����s��
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
	 * �z�B�S�����{�b�g�ɉו���n�������Ƃ�{���ɕ񍐂���
	 *
	 * �菇
	 * ���\�b�h�ɑΉ����鑀����s��
	 *
	 *
	 */
	public void reportDeliveryStart(int id) {
		try{
			//signal.openSig("Headquarters");
			signal.openSig(Port.HEAD);
			//signal.waitSig();
			signal.sendSig(2);
			signal.sendSig(id);
			signal.sendSig(LuggageCondition.delivering);
			signal.sendSig(new Date(0));
			signal.closeSig();
		}catch(Exception e){
			System.out.println("Sorry, Don't report.");
		}
	}
	
	public ArrayList<Luggage> getLuggageList(){
		return luggageList;
	}

}
