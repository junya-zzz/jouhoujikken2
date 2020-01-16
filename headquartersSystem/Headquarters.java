package headquartersSystem;

import java.io.IOException;
import java.util.Date;

import javax.swing.plaf.basic.BasicSliderUI.TrackListener;

import java.util.Calendar;

import recordSystem.DeliveryRecord;
import recordSystem.DeliveryRecordList;
import recordSystem.LuggageCondition;
import signal.EV3Signal;
import signal.PCSignal;
import signal.Port;

public class Headquarters{
	public static final int ADD_RECORD = 0;
	public static final int UPDATE_RECORD = 1;

	public static final int TRACK_LUGGAGE = 3;

	private DeliveryRecordList DeliveryRecordList = new DeliveryRecordList();

	public static void main(String[] args) {
		Headquarters h = new Headquarters();
		h.systemExe();
	}


	public void systemExe() {
		while(true){
			PCSignal sig = new PCSignal();
			try{
				sig.waitSig(Port.HEAD);	
				Integer methodFlag = (int)sig.getSig();
				if(methodFlag==0){
					DeliveryRecord deliveryrecord = (DeliveryRecord)sig.getSig();
					addDeliveryRecord(deliveryrecord);
				}else if(methodFlag==1){
					int luggageID = (int)sig.getSig();
					LuggageCondition luggageCondition = (LuggageCondition)sig.getSig();
					Date time = (Date)sig.getSig();
					System.out.println("ID:"+luggageID+"LC:"+luggageCondition+"time:"+time);
					updateDeliveryRecord(luggageID,luggageCondition,time);	
				}else if(methodFlag==2){
					int luggageID = (int)sig.getSig();
					LuggageCondition luggageCondition = (LuggageCondition)sig.getSig();
					Date time = (Date)sig.getSig();
					Date result = new Date(new Date().getTime()+time.getTime());
					/*Calendar calendar = Calendar.getInstance();
					calendar.setTime(time);
					calendar.add(Calendar.getInstance().setTime(new Date()));
					time = calendar.getTime;*/
					System.out.println("ID:"+luggageID+"LC:"+luggageCondition+"time:"+result);
					updateDeliveryRecord(luggageID,luggageCondition,result);	
				} else if (methodFlag == TRACK_LUGGAGE) {
					sendLuggageInfomation(sig);
				}
				System.out.println(getDeliveryRecordList());
				sig.closeSig();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public void updateDeliveryRecord(int luggageID, LuggageCondition luggagecondition, Date time) {
		DeliveryRecordList.updateDeliveryRecord(luggageID, luggagecondition, time);
	}


	public void addDeliveryRecord(DeliveryRecord deliveryrecord) {
		deliveryrecord.setReceiptTime(new Date());
		DeliveryRecordList.addDeliveryRecord(deliveryrecord);
		System.out.println("list added.");
	}

	public DeliveryRecordList getDeliveryRecordList() {
		return DeliveryRecordList;
	}

	public void setDeliveryRecordList(DeliveryRecordList deliveryRecordList) {
		DeliveryRecordList = deliveryRecordList;
	}

	private void sendLuggageInfomation(PCSignal sig) {
		try {
			int id = (int) sig.getSig();
			sig.sendSig(DeliveryRecordList.getDeliveryRecord(id));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
