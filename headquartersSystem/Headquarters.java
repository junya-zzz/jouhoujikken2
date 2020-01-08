package headquartersSystem;

import java.io.IOException;
import java.util.Date;

import recordSystem.DeliveryRecord;
import recordSystem.DeliveryRecordList;
import recordSystem.LuggageCondition;
import signal.PCSignal;

public class Headquarters {
	/**
	 * 驟埼＃險倬鹸繝ｪ繧ｹ繝�
	 */
	private DeliveryRecordList DeliveryRecordList = new DeliveryRecordList();

	/**
	 * 騾壻ｿ｡蜿嶺ｻ倡憾諷九↓蜈･繧�
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
				// TODO 閾ｪ蜍慕函謌舌＆繧後◆ catch 繝悶Ο繝�け
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 驟埼＃險倬鹸繧呈峩譁ｰ縺吶ｋ
	 * 
	 * 謇矩�
	 * 繝｡繧ｽ繝�ラ縺ｫ蟇ｾ蠢懊☆繧区桃菴懊ｒ陦後≧
	 * 
	 * 
	 */
	public void updateDeliveryRecord(int luggageID, LuggageCondition luggagecondition, Date time) {
		DeliveryRecordList.updateDeliveryRecord(luggageID, luggagecondition, time);
	}

	/**
	 * 驟埼＃險倬鹸繧定ｿｽ蜉�☆繧�
	 * 
	 * 謇矩�
	 * 繝｡繧ｽ繝�ラ縺ｫ蟇ｾ蠢懊☆繧区桃菴懊ｒ陦後≧
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
