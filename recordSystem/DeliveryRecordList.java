package recordSystem;

import java.util.HashMap;
import java.util.Date;
//import java.util.ArrayList;
public class DeliveryRecordList {

	//private ArrayList<DeliveryRecord> DeliveryRecordList= new ArrayList<>();
	HashMap<Integer, DeliveryRecord> deliveryRecordMap = new HashMap<Integer, DeliveryRecord>();
	/**
   * 配達記録リストに追加する
   *
   * 手順
	 * メソッドに対応する操作を行う 
	 */
	public void addDeliveryRecord(DeliveryRecord deliveryRecord) {
		deliveryRecordMap.put(deliveryRecord.getDeliveryID(),deliveryRecord);
	}

	/**
 	 * 受け取った時間の種類から更新すべき配達記録の時間項目を一意に決定できる。例えば、受け取った時間の種類が「配達完了時間」なら、配達記録における配達完了時間を更新する。
   *
   * 手順
   * 荷物IDを使い、リストから配達記録を探す
	 * 時間の種類に応じて、時間を更新する
	 * 時間の種類に応じて荷物状態も変更する
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
           //差分を受け取っているので修正しないと
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
		return "配達記録リスト\n " + deliveryRecordMap;
	}
	
	
	
}
