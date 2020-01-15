package recordSystem;

import java.util.HashMap;
import java.util.Date;
//import java.util.ArrayList;
public class DeliveryRecordList {

	//private ArrayList<DeliveryRecord> DeliveryRecordList= new ArrayList<>();
	HashMap<Integer, DeliveryRecord> deliveryRecordMap = new HashMap<Integer, DeliveryRecord>();
	/**
	 * 驟埼＃險倬鹸繝ｪ繧ｹ繝医↓霑ｽ蜉�☆繧�
	 * 
	 * 謇矩�
	 * 繝｡繧ｽ繝�ラ縺ｫ蟇ｾ蠢懊☆繧区桃菴懊ｒ陦後≧
	 */
	public void addDeliveryRecord(DeliveryRecord deliveryRecord) {
		deliveryRecordMap.put(deliveryRecord.getDeliveryID(),deliveryRecord);
	}

	/**
	 * 蜿励￠蜿悶▲縺滓凾髢薙�遞ｮ鬘槭°繧画峩譁ｰ縺吶∋縺埼�驕碑ｨ倬鹸縺ｮ譎る俣鬆�岼繧剃ｸ�э縺ｫ豎ｺ螳壹〒縺阪ｋ縲ゆｾ九∴縺ｰ縲∝女縺大叙縺｣縺滓凾髢薙�遞ｮ鬘槭′縲碁�驕泌ｮ御ｺ�凾髢薙�縺ｪ繧峨�驟埼＃險倬鹸縺ｫ縺翫￠繧矩�驕泌ｮ御ｺ�凾髢薙ｒ譖ｴ譁ｰ縺吶ｋ縲�
	 * 
	 * 謇矩�
	 * 闕ｷ迚ｩID繧剃ｽｿ縺��繝ｪ繧ｹ繝医°繧蛾�驕碑ｨ倬鹸繧呈爾縺�
	 * 譎る俣縺ｮ遞ｮ鬘槭↓蠢懊§縺ｦ縲∵凾髢薙ｒ譖ｴ譁ｰ縺吶ｋ
	 * 譎る俣縺ｮ遞ｮ鬘槭↓蠢懊§縺ｦ闕ｷ迚ｩ迥ｶ諷九ｂ螟画峩縺吶ｋ
	 */
	public void updateDeliveryRecord(int luggageID, LuggageCondition luggagecondition, Date time) {
		
		 switch(luggagecondition) {
         case unshipped:
        	 
             break;
         case shipping:
        	 deliveryRecordMap.get(luggageID).setShipTime(time);
             break;
         case waitDelivering:
        	 System.out.println(deliveryRecordMap);
        	 deliveryRecordMap.get(luggageID).setArrivalTime(time);
             break;
         case delivering:
        	 deliveryRecordMap.get(luggageID).setStartTime(time);
             break;
         case delivered:
        	 //蟾ｮ蛻�ｒ蜿励￠蜿悶▲縺ｦ繧九�縺ｧ菫ｮ豁｣縺励↑縺�→
        	 deliveryRecordMap.get(luggageID).setReceiveTime(time);
             break;
         case finished:
        	 deliveryRecordMap.get(luggageID).setFinishTime(time);
             break;     
         case wrongAddress:
             break;     
         case absence:
                 
             break;     
             

		 }
		 deliveryRecordMap.get(luggageID).setLuggageCondition(luggagecondition);
	}

	@Override
	public String toString() {
		return "驟埼＃險倬鹸繝ｪ繧ｹ繝医�\n " + deliveryRecordMap;
	}
	
	
	
}
