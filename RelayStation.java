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
	 * �ו��̔z�B���ʂ̕񍐂��󂯂�
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
	}

	/**
	 * �z�B�S�����{�b�g�ɉו���n��
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
		}
		else{
			Luggage luggage = luggagelist.get(0);
			signal.sendData(luggagelist.isLuggage(),"DeliveryRobot");
			signal.sendData(luggage,"DeliveryRobot");
		}
		signal.closeSig("FINISH","DeliveryRobot");
	}

	/**
	 * �ו��̔z�B���ʂ�{���ɕ񍐂���
	 * 
	 * �菇
	 * ���\�b�h�ɑΉ����鑀����s��
	 */
	public void reportDeliveryResult() {
		signal.openSig("START","Headquarters")
		signal.waitSig();
		/* if(�z�B����){
		 * 	sendData(�ו�ID,"Headquarters");
		 * 	sendData(�z�B��������,"Headquarters");
		 * 	sendData(�o�ߎ���,"Headquarters");
		 * }
		 * else{
		 * 	sendData(�ו�ID,"Headquarters");
		 * }
		 */
		signal.closeSig("FINISH","Headquarters");
	}

	/**
	 * �z�B�S�����{�b�g�ɉו���n�������Ƃ�{���ɕ񍐂���
	 * 
	 * �菇
	 * ���\�b�h�ɑΉ����鑀����s��
	 * 
	 * 
	 * 
	 */
	public void reportDeliveryStart() {
		signal.openSig("START","Headquarters");
		signal.waitSig();
		/* sendData(�ו�ID,"Headquarters");
		 * sendData(�z�B�J�n����,"Headquarters");
		 */
		signal.closeSig("FINISH","Headquarters");
	}

}
