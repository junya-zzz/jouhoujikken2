package headquartersSystem;

import java.io.IOException;
import java.util.Date;

import recordSystem.DeliveryRecord;
import recordSystem.DeliveryRecordList;
import recordSystem.LuggageCondition;
import signal.PCSignal;

public class Headquarters {
	/**
	 * 配達記録ッFゃP�
	 */
	private DeliveryRecordList DeliveryRecordList = new DeliveryRecordList();

	/**
	 * 通�\�B受�T�状態に允E�
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
					int luggageID = (int)sig.getSig();
					LuggageCondition luggageCondition = (LuggageCondition)sig.getSig();
					Date time = (Date)sig.getSig();
					updateDeliveryRecord(luggageID,luggageCondition,time);	
				}else if(methodFlag==2){
					sig.sendSig(new Date());
				}
				System.out.println(getDeliveryRecordList());
				sig.closeSig();
			} catch (IOException e) {
				// TODO 臃F動生成された catch ブロヂ�
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 配達記録を更斁[する
	 * 
	 * 手�
	 * チBゃXッ�ぃH寃Z応する操作を行う
	 * 
	 * 
	 */
	public void updateDeliveryRecord(int luggageID, LuggageCondition luggagecondition, Date time) {
		DeliveryRecordList.updateDeliveryRecord(luggageID, luggagecondition, time);
	}

	/**
	 * 配達記録を�\�X劁��
	 * 
	 * 手�
	 * チBゃXッ�ぃH寃Z応する操作を行う
	 * 
	 */
	public void addDeliveryRecord(DeliveryRecord deliveryrecord) {
		DeliveryRecordList.addDeliveryRecord(deliveryrecord);
		System.out.println("list added.");
	}

	public DeliveryRecordList getDeliveryRecordList() {
		return DeliveryRecordList;
	}

	public void setDeliveryRecordList(DeliveryRecordList deliveryRecordList) {
		DeliveryRecordList = deliveryRecordList;
	}


	
}
