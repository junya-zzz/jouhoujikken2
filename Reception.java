package receptionSystem;

import recordSystem.*;
import java.util.ArrayList;


public static final String EXIST_LUGGAGE = "existLuggage";


public class Reception {

    private ArrayList<Luggage> lugList;
    private ArrayList<DeliveryRecord> deliList
    private Boundary boundary;

    private Signal signal;


    public Reception(){
	this.lugList = new ArrayList<Luggage>();
	this.deliList = new ArrayList<DeliveryRecord>();
	this.boundary = new Boundary();
	this.signal = new Signal();
    }
    /*配達記録を本部に送る*/
    public void sendDeliveryRecord() {

    }

    /*発送時間を本部に報告する*/
    public void sendShipTime() {
    	try {
    		signal.openSig("START","Headquarters");
    		/**配達記録リストからサーチ**/


    		//新規か追記かのシグナルも送信したほうが良き?

    		/**************/
    		signal.sendSig(deliveryRecord);
    		signal.closeSig("FINISH","Headquarters");

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
    		signal.openSig("START","CollectingRobot");
    		String getMessage = signal.getSig();
    		if(getMessage.contentEquals(EXIST_LUGGAGE)) { //ロボットからのメッセージが正しかったら
    			if(!this.lugList.isEmpty()) { //荷物リストに荷物があったら
    				signal.sendSig(true);
    				signal.sendSig(this.lugList.remove(0));//荷物リストから先頭の要素を取り出して送る

    				/******渡した荷物に対応する配達記録に発送時間を追記（サーチ機能がいる？）**/





    				/************************************************************/
    			}
    		}
    		signal.closeSig("FINISH","CollectingRobot");

    	}catch(Exception e) {
    		//例外処理
    	}
    }


    /*荷物を依頼人から受け取る*/
    public void getLug() {
    	RequestInfomation info = this.boundary.inputReqInfo(); //バウンダリから配達情報を入力
    	Luggage lug = new Luggage(info);
    	lugList.add(lug);   //荷物リストに追加
    	DeliveryRecord deliveryRecord = new DeliveryRecord(lug); //荷物の配達記録生成
    	deliveryRecord.setReceiptTime(new Date()); //配達記録に受付時間を追加
    	deliList.add(deliveryRecord); //配達記録リストに追加

    }

}
