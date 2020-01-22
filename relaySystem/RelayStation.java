package relaySystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import lejos.hardware.lcd.LCD;
import recordSystem.*;
import signal.*;

/**
 * ���p���N���X
 * <PRE>
 * �ו���ۊǂ�����A�z�B���ʂ�{���ɕ񍐂���B
 * ���̃��\�b�h�����B
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
	 * �t���O0<br>
	 * ���p�����ו����󂯂Ƃ�
	 */
	public static int SEND_LUG_TO_RELAY = 0;
	
	/**
	 * �t���O1<br>
	 * �z�B�S�����{�b�g�։ו��������n��
	 */
	public static int SEND_LUG_TO_DELIVERY = 1;
	
	/**
	 * �t���O2<br>
	 * �z�B���ʂ������n��
	 */
	public static int REPORT_DELIVERY_RESULT = 2;

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

	/**
	 * �R���X�g���N�^
	 */
	public RelayStation(){
		luggageList = new ArrayList<Luggage>();
		wrongLugList = new ArrayList<Luggage>();
		signal = new EV3Signal();
	}

	/**
	 * main��<br>
	 * �V�X�e���N�����͂��̃��\�b�h���g�p����
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
	 * ��������ʂ��郁�\�b�h<br>
	 * �ו��̎󂯎�� or �ו��̈����n�� or �z�B���ʂ̕�
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
	 * ���W�S�����{�b�g����ו����󂯎��
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
	 * ���W�S�����{�b�g����̉ו��̎󂯎���{���ɕ񍐂���
	 * @param ship_time ��������
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
	 * �z�B�S�����{�b�g�ɉו���n���A�{���ɕ񍐂���
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
	 * �ו��̔z�B���ʂ�{���ɕ񍐂���
	 * @param result �ו��̔z�B����
	 * @param lug_id �ו�ID
	 * @param fin_time �z�B��������
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
	 * �z�B�S�����{�b�g�ɉו���n�������Ƃ�{���ɕ񍐂���
	 * @param id �ו�ID
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
	 * �ו����X�g��getter���\�b�h
	 * @return �ו����X�g
	 */
	public ArrayList<Luggage> getLuggageList(){
		return luggageList;
	}

}
