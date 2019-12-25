package recordSystem.headquartersSystem;

import java.io.IOException;
import java.util.Date;

import recordSystem.DeliveryRecord;
import recordSystem.DeliveryRecordList;
import recordSystem.LuggageCondition;
import signal.PCSignal;

public class Headquarters {
	/**
	 * 配達記録リスト
	 */
	private DeliveryRecordList DeliveryRecordList;

	/**
	 * 通信受付状態に入る
	 */
	
	public void systemExe(){
		while(true){
			PCSignal sig = new PCSignal();
			try{
				sig.waitSig();	
				Integer methodFlag = (int)sig.getSig();
				if(methodFlag==0){
					DeliveryRecord deliveryrecord = (DeliveryRecord)sig.getSig();
					addDeliveryRecord(deliveryrecord);
				}else if(methodFlag==1){
					String luggageID = (String)sig.getSig();
					LuggageCondition luggageCondition = (LuggageCondition)sig.getSig();
					Date time = (Date)sig.getSig();
					updateDeliveryRecord(luggageID,luggageCondition,time);	
				}else if(methodFlag==2){
					sig.sendSig(new Date());
				}
				sig.closeSig();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 配達記録を更新する
	 * 
	 * 手順
	 * メソッドに対応する操作を行う
	 * 
	 * 
	 */
	public void updateDeliveryRecord(String luggageID, LuggageCondition luggagecondition, Date time) {
		DeliveryRecordList.updateDeliveryRecord(luggageID, luggagecondition, time);
	}

	/**
	 * 配達記録を追加する
	 * 
	 * 手順
	 * メソッドに対応する操作を行う
	 * 
	 */
	public void addDeliveryRecord(DeliveryRecord deliveryrecord) {
		DeliveryRecordList.addDeliveryRecord(deliveryrecord);

	}


}
