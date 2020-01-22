package recordSystem;

import java.util.HashMap;
import java.util.Date;
//import java.util.ArrayList;
public class DeliveryRecordList {

	//private ArrayList<DeliveryRecord> DeliveryRecordList= new ArrayList<>();
	HashMap<Integer, DeliveryRecord> deliveryRecordMap = new HashMap<Integer, DeliveryRecord>();
	/**
	   * �z�B�L�^���X�g�ɒǉ�����
	   *
	   * �菇
		 * ���\�b�h�ɑΉ����鑀����s�� 
		 */
	public void addDeliveryRecord(DeliveryRecord deliveryRecord) {
		deliveryRecordMap.put(deliveryRecord.getDeliveryID(),deliveryRecord);
	}

	/**
 	 * �󂯎�������Ԃ̎�ނ���X�V���ׂ��z�B�L�^�̎��ԍ��ڂ���ӂɌ���ł���B�Ⴆ�΁A�󂯎�������Ԃ̎�ނ��u�z�B�������ԁv�Ȃ�A�z�B�L�^�ɂ�����z�B�������Ԃ��X�V����B
   *
   * �菇
   * �ו�ID���g���A���X�g����z�B�L�^��T��
	 * ���Ԃ̎�ނɉ����āA���Ԃ��X�V����
	 * ���Ԃ̎�ނɉ����ĉו���Ԃ��ύX����
	 */
	public void updateDeliveryRecord(int luggageID, LuggageCondition luggagecondition, Date time) {
		
		 switch(luggagecondition) {
         case unshipped:
        	 
             break;
         case shipping:
        	 deliveryRecordMap.get(luggageID).setShipTime(time);
             break;
         case waitDelivering:
        	 deliveryRecordMap.get(luggageID).setArrivalTime(time);
             break;
         case delivering:
        	 deliveryRecordMap.get(luggageID).setStartTime(time);
             break;
         case delivered:
        	 deliveryRecordMap.get(luggageID).setReceiveTime(time);
             break;
         case finished:
        	 deliveryRecordMap.get(luggageID).setFinishTime(time);
             break;     
         case wrongAddress:
             break;     
         default: 
             break;     
		 }
		 deliveryRecordMap.get(luggageID).setLuggageCondition(luggagecondition);
	}
	
	/**
	 * �z�B�L�^���X�g.�z�B�L�^�̉ו���ύX����
	 * @param luggageID �ו�ID
	 * @param lug�@�ו�
	 */
	public void fixDeliveryRecord(int luggageID, Luggage lug){
		 deliveryRecordMap.get(luggageID).setLuggage(lug);
		 deliveryRecordMap.get(luggageID).setLuggageCondition(LuggageCondition.waitDelivering);
	}
	public DeliveryRecord getDeliveryRecord(int id) {
		return deliveryRecordMap.get(id);
	}

	@Override
	public String toString() {
		String result = "�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\\n";
		for (DeliveryRecord dr : deliveryRecordMap.values()) {
			result += dr+"�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\\n";
		}
		return "�z�B�L�^���X�g�@\n " + result;
	}
	
	
}
