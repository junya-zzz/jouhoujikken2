package relaySystem;

import java.util.ArrayList;
import recordSystem.LuggageList;
import sun.misc.Signal;

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
	private Signal signal;

	public RelayStation(){
		luglist = new ArrayList<Luggage>;
		wrongLugList = new ArrayList<Luggage>;
		signal = new Signal();
	}

	/**
	 * ���W�S�����{�b�g����ו����󂯎��
	 *
	 * �菇
	 * ���\�b�h�ɑΉ����鑀����s��
	 */
	public void receiveLugfromGatheringRobot() {
		signal.openSig("START","GatheringRobot");
		signal.waitSig();
		Luggage luggage = (Luggage)signal.getData();
		luggagelist.add(luggage);
		signal.sendData("Receiving completed.","GatheringRobot");
		signal.closeSig("FINISH","GatheringRobot");
	}

	/**
	 * ���W�S�����{�b�g����̉ו��̎󂯎���{���ɕ񍐂���
	 *
	 * �菇
	 * ���\�b�h�ɑΉ����鑀����s��
	 */
	public void reportReceivingToHeadquarters() {
		signal.openSig("START","Headquarters");
		signal.waitSig();
		signal.sendData(luglist.getLug().luggageID,"Headquarters");
		signal.closeSig("FINISH","Headquarters");
	}

	/**
	 * �ו��̔z�B���ʂ̕񍐂���,�{���ɂ��񍐂���
	 *
	 * �菇
	 * ���\�b�h�ɑΉ����鑀����s��
	 */
	public void receiveDeliveryResult() {
		signal.openSig("START","DeliveryRobot")
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
	}

	/**
	 * �z�B�S�����{�b�g�ɉו���n���A�{���ɕ񍐂���
	 *
	 * �菇
	 * ���\�b�h�ɑΉ����鑀����s��
	 */
	public void sendLugtoDeliveryRobot() {
		signal.openSig("START","DeliveryRobot");
		signal.waitSig();
		signal.getData((String)"�ו������邩�ǂ���","DeliveryRobot");
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
	}

	/**
	 * �ו��̔z�B���ʂ�{���ɕ񍐂���
	 *
	 * �菇
	 * ���\�b�h�ɑΉ����鑀����s��
	 */
	private void reportDeliveryResult(String result, int lug_id, String fin_time) {
		signal.openSig("START","Headquarters")
		signal.waitSig();
		if(result.equals("finished")){
		  sendData(lug_id,"Headquarters");
		  sendData(fin_time,"Headquarters");
		}
		else{
	  	  sendData(lug_id,"Headquarters");
		}
		signal.closeSig("FINISH","Headquarters");
	}

	/**
	 * �z�B�S�����{�b�g�ɉו���n�������Ƃ�{���ɕ񍐂���
	 *
	 * �菇
	 * ���\�b�h�ɑΉ����鑀����s��
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
