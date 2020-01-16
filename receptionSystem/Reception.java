package receptionSystem;

import recordSystem.*;
import signal.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import headquartersSystem.Headquarters;




public class Reception extends Thread{
	public static final String EXIST_LUGGAGE = "existLuggage";

	private ArrayList<Luggage> lugList;
	//private DeliveryRecordList deliList;
	//private ArrayDeque<DeliveryRecord>; = new ArrayDeque<DeliveryRecord>();
	private Boundary boundary;
	private int idNum=0;

	private PCSignal signal;

	public static void main(String[] args) {
		/*
		Reception reception = new Reception();
		reception.start();
		while (true) {
			reception.choseFunction();
		}
		 */
		Reception reception = new Reception();
		GUI gui = new GUI("delivery system", reception);
		gui.setVisible(true);
		while (true) {
			reception.choseFunction();
		}
	}

	private void choseFunction() {
		try {
			PCSignal sig = new PCSignal();
			sig.waitSig(Port.RECEPTION);
			int flag = (int) sig.getSig();
			if (flag == 0) {
				sendLug(sig);
			} else if (flag == 1) {
				getIsDelivery(sig);
			}
		}catch (IOException e){
			e.printStackTrace();
			System.exit(1);
		}
	}

	public Reception(){
		this.lugList = new ArrayList<Luggage>();
		//this.deliList = new DeliveryRecordList();
		this.boundary = new Boundary();
		this.signal = new PCSignal();
	}


	// 荷物を依頼人から受け取ったことを本部に報告する
	public void sendReceiptTime(DeliveryRecord d) {
		try {
			signal.openSig(Port.HEAD);
			/**配達記録リストからサーチ**/
			signal.sendSig(0);
			signal.sendSig(d); 



			/**************/
			//signal.sendSig(deliveryRecord);
			signal.closeSig();

		}catch(Exception e) {
			//例外処理
			//
			//

		}
	}


	/*荷物を収集担当ロボットに渡したことを本部に報告する*/
	public void sendShipTime(Luggage lug) {
		try {
			signal.openSig(Port.HEAD);
			/**配達記録リストからサーチ**/
			signal.sendSig(1);
			signal.sendSig(lug.getLuggageID()); 
			signal.sendSig(LuggageCondition.shipping); 
			signal.sendSig(new Date()); 


			/**************/
			//signal.sendSig(deliveryRecord);
			signal.closeSig();

		}catch(Exception e) {
			//例外処理
			//
			//

		}
	}


	/*中継所との引渡し結果を得る*/
	public void getIsDelivery(PCSignal sig) {
		try {
			boolean isDelivery = (boolean) sig.getSig();

			if (!isDelivery) {
				Luggage receiveLuggage = (Luggage) sig.getSig();
				lugList.add(receiveLuggage);
				sig.closeSig();
				sig.openSig(Port.HEAD);
				sig.sendSig(1);
				sig.sendSig(receiveLuggage.getLuggageID());
				sig.sendSig(LuggageCondition.relay_absence);
				sig.sendSig(null);
			}
			sig.closeSig();
		} catch (IOException e){
			e.printStackTrace();
			System.exit(1);
		}
	}


	/*荷物を収集担当ロボットに渡す*/
	public void sendLug(PCSignal sig) {
		try {
			Luggage sendLug = null;
			String getMessage = (String)sig.getSig();
			if(getMessage.contentEquals(EXIST_LUGGAGE)) { //ロボットからのメッセージが正しかったら
				if(!this.lugList.isEmpty()) { //荷物リストに荷物があったら
					sig.sendSig(true);
					sendLug = this.lugList.remove(0);  //荷物リストから先頭の要素を取り出して送る
					System.out.println("send lug:" + sendLug);
					sig.sendSig(sendLug);
					/******渡した荷物に対応する配達記録に発送時間を追記**/
					//deliList.updateDeliveryRecord(sendLug.getLuggageID(), LuggageCondition.delivering, new Date());
				} else {
					System.out.println("no lug.");
					sig.sendSig(false);
				}
			}
			sig.closeSig();
			if (sendLug != null) {
				sendShipTime(sendLug/*new DeliveryRecord(sendLug.getLuggageID(), sendLug)*/);
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}


	/*荷物を依頼人から受け取る*/
	public Luggage getLug(RequestInformation requestInformation, String luggageName) {
		int id = setLuggageIDNum();
		Luggage lug = new Luggage(id,luggageName, requestInformation);                      /****ID,Amount追加****/
		lugList.add(lug);   //荷物リストに追加
		//DeliveryRecord deliveryRecord = new DeliveryRecord(id, lug); //荷物の配達記録生成
		//deliveryRecord.setReceiveTime(new Date()); //配達記録に受付時間を追加
		//deliList.addDeliveryRecord(deliveryRecord); //配達記録リストに追加
		sendReceiptTime(new DeliveryRecord(lug.getLuggageID(),lug));
		return lug;
	}

	// 荷物の状態を本部に問い合わせて取得し、表示する
	public DeliveryRecord luggageTracking(int id) {
		// int id;
		DeliveryRecord dr = null;
		try {
			signal.openSig(Port.HEAD);
			signal.sendSig(Headquarters.TRACK_LUGGAGE); //荷物問い合わせ
			signal.sendSig(id);
			dr = (DeliveryRecord)signal.getSig();
			signal.closeSig();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return dr;
	}

	private int setLuggageIDNum() { //荷物IDを設定
		idNum++;
		return idNum;
	}
}
