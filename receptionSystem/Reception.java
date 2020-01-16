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

	private PCSignal signal;

	public static void main(String[] args) {
		// テスト用
		Reception reception = new Reception();
		reception.run();
		while (true) {
			reception.sendLug();
		}
	}

	public Reception(){
		this.lugList = new ArrayList<Luggage>();
		//this.deliList = new DeliveryRecordList();
		this.boundary = new Boundary();
		this.signal = new PCSignal();
	}
	/*配達記録を本部に送る
    public void sendDeliveryRecord() {

    }

	 */
	
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


	/*発送時間を本部に報告する*/
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
	public void getIsDelivery() {

	}


	/*荷物を収集担当ロボットに渡す*/
	public void sendLug() {
		try {
			Luggage sendLug = null;
			System.out.println("send lug.");
			signal.waitSig(Port.RECEPTION);
			String getMessage = (String)signal.getSig();
			if(getMessage.contentEquals(EXIST_LUGGAGE)) { //ロボットからのメッセージが正しかったら
				if(!this.lugList.isEmpty()) { //荷物リストに荷物があったら
					signal.sendSig(true);
					sendLug = this.lugList.remove(0);  //荷物リストから先頭の要素を取り出して送る
					System.out.println("send lug:" + sendLug);
					signal.sendSig(sendLug);
					/******渡した荷物に対応する配達記録に発送時間を追記**/
					//deliList.updateDeliveryRecord(sendLug.getLuggageID(), LuggageCondition.delivering, new Date());
					
				} else {
					signal.sendSig(false);
				}
			}
			signal.closeSig();
			sendShipTime(sendLug/*new DeliveryRecord(sendLug.getLuggageID(), sendLug)*/);
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public int idNum=0;
	
	// 操作選択
	@Override
	public void run() {
		while(true) {
			String input = null;
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("操作を選択");
				System.out.println("依頼 : 0, 荷物状態確認 : 1 -> ");
				input = br.readLine();
			} catch (IOException e){
				e.printStackTrace();
				System.exit(1);
			}
			if (input.equals("0")){
				getLug();
			} else if (input.equals("1")){
				luggageTracking();
			}
		}
	}
	

	/*荷物を依頼人から受け取る*/
	public void getLug() {
		try {
			RequestInformation info = this.boundary.inputReqInfo(); //バウンダリから配達情報を入力
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String luggageName = br.readLine();
			int id = setLuggageIDNum();
			Luggage lug = new Luggage(id,luggageName, info);                      /****ID,Amount追加****/
			lugList.add(lug);   //荷物リストに追加
			//DeliveryRecord deliveryRecord = new DeliveryRecord(id, lug); //荷物の配達記録生成
			//deliveryRecord.setReceiveTime(new Date()); //配達記録に受付時間を追加
			//deliList.addDeliveryRecord(deliveryRecord); //配達記録リストに追加
			sendReceiptTime(new DeliveryRecord(lug.getLuggageID(),lug));
		} catch (IOException e){
			//例外
		}
	}
	
	// 荷物の状態を本部に問い合わせて取得し、表示する
	private void luggageTracking() {
		int id;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("荷物IDを入力 -> ");
			id = Integer.parseInt(br.readLine());
			signal.openSig(Port.HEAD);
			signal.sendSig(Headquarters.TRACK_LUGGAGE); //荷物問い合わせ
			signal.sendSig(id);
			DeliveryRecord dr = (DeliveryRecord)signal.getSig();
			if (dr == null) {
				System.out.println("荷物が見つかりません");
			} else {
				System.out.println(dr);
			}
			signal.closeSig();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (NumberFormatException e) {
			System.out.println("数字を入力してください");
		}
	}

	private int setLuggageIDNum() { //荷物IDを設定
		idNum++;
		return idNum;
	}
}
