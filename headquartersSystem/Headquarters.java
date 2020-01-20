package headquartersSystem;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Date;

import javax.swing.plaf.basic.BasicSliderUI.TrackListener;

import java.util.Calendar;

import recordSystem.DeliveryRecord;
import recordSystem.DeliveryRecordList;
import recordSystem.Luggage;
import recordSystem.LuggageCondition;
import recordSystem.RequestInformation;
import signal.EV3Signal;
import signal.PCSignal;
import signal.Port;

public class Headquarters{
	public static final int ADD_RECORD = 0;
	public static final int UPDATE_RECORD = 1;
	public static final int CLUC_RECEIPTTIME = 2;
	public static final int TRACK_LUGGAGE = 3;
	public static final int UPDATE_FIXEDLUGLIST = 4;
	public static final int SEND_FIXEDLUG = 5;

	private DeliveryRecordList DeliveryRecordList = new DeliveryRecordList();
	private ArrayDeque<Luggage> fixedLugList = new ArrayDeque<>();

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
					System.out.println("ID:"+luggageID+"LC:"+luggageCondition+"time:"+new Date());
					updateDeliveryRecord(luggageID,luggageCondition,new Date());	
				}else if(methodFlag==2){
					int luggageID = (int)sig.getSig();
					LuggageCondition luggageCondition = (LuggageCondition)sig.getSig();
					Date relayTime = (Date)sig.getSig();
					if (luggageCondition == LuggageCondition.finished) {
						Date result = new Date(DeliveryRecordList.getDeliveryRecord(luggageID).getStartTime().getTime()+relayTime.getTime());
						updateDeliveryRecord(luggageID, LuggageCondition.delivered, result);
					}
					updateDeliveryRecord(luggageID,luggageCondition,new Date());	
					
				} else if (methodFlag == TRACK_LUGGAGE) {
					sendLuggageInfomation(sig);
					
					
				}else if (methodFlag == UPDATE_FIXEDLUGLIST){//宛先間違い修正　受付所-本部
					Luggage fixedLug = (Luggage)sig.getSig();
					fixedLugList.add(fixedLug);
					DeliveryRecordList.fixDeliveryRecord(fixedLug.getLuggageID(),fixedLug);
					
				}else if(methodFlag == SEND_FIXEDLUG){//宛先間違い修正　中継所-本部
					if(fixedLugList.isEmpty())sig.sendSig(0);
					else sig.sendSig(fixedLugList.size());
					
					for(int i = 0;i<fixedLugList.size();i++){
						sig.sendSig(fixedLugList.remove());
					}
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
