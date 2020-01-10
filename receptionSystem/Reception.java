package receptionSystem;

import recordSystem.*;
import signal.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;




public class Reception {
	public static final String EXIST_LUGGAGE = "existLuggage";

	private ArrayList<Luggage> lugList;
	private DeliveryRecordList deliList;
	private Boundary boundary;

	private PCSignal signal;


	public Reception(){
		this.lugList = new ArrayList<Luggage>();
		this.deliList = new DeliveryRecordList();
		this.boundary = new Boundary();
		this.signal = new PCSignal();
	}
	/*配達記録を本部に送る
    public void sendDeliveryRecord() {

    }

	 */


	/*発送時間を本部に報告する*/
	public void sendShipTime() {
		try {
			signal.openSig("Headquarters");
			/**配達記録リストからサーチ**/




			/**************/
			signal.sendSig(deliveryRecord);
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
			signal.openSig("CollectingRobot");
			String getMessage = (String)signal.getSig();
			if(getMessage.contentEquals(EXIST_LUGGAGE)) { //ロボットからのメッセージが正しかったら
				if(!this.lugList.isEmpty()) { //荷物リストに荷物があったら
					signal.sendSig(true);
					Luggage sendLug = this.lugList.remove(0);  //荷物リストから先頭の要素を取り出して送る
					signal.sendSig(sendLug);
					/******渡した荷物に対応する配達記録に発送時間を追記**/
					deliList.updateDeliveryRecord(sendLug.getLuggageID(), LuggageCondition.delivering, new Date());


				}
			}
			signal.closeSig();



		}catch(Exception e) {
			//例外処理
		}
	}

	public int idNum=0;

	/*荷物を依頼人から受け取る*/
	public void getLug() {
		try {
			RequestInformation info = this.boundary.inputReqInfo(); //バウンダリから配達情報を入力
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String luggageName = br.readLine();
			int id = setLuggageIDNum();
			Luggage lug = new Luggage(id,luggageName, info);                      /****ID,Amount追加****/
			lugList.add(lug);   //荷物リストに追加
			DeliveryRecord deliveryRecord = new DeliveryRecord(id, lug); //荷物の配達記録生成
			deliveryRecord.setReceiveTime(new Date()); //配達記録に受付時間を追加
			deliList.addDeliveryRecord(deliveryRecord); //配達記録リストに追加
		} catch (IOException e){
			//例外
		}
	}

	private int setLuggageIDNum() { //荷物IDを設定
		idNum++;
		return idNum;
	}
}
