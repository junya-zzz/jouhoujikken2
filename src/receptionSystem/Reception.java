package receptionSystem;

import java.util.ArrayList;

import recordSystem.*;






public class Reception {

    private ArrayList<Luggage> lugList;
    private DeliveryRecordList deliList;
    private Boundary boundary;
    private Signal signal;
    public final String  EXIST_LUGGAGE = "existLuggage";
    public final String  DELIVERY_FAILURE="failure";

   

    public Reception(){
	this.lugList = new ArrayList<Luggage>();
	this.deliList = new DeliveryRecordList();
	this.boundary = new Boundary();
	this.signal = new Signal();
    }
    /*配達記録を本部に送る
    public void sendDeliveryRecord() {

    }

	*/


    /*発送時間を本部に報告する*/
    public void sendShipTime() {
    	try {
    		signal.openSig("START","Headquarters");
    		/**配達記録リストからサーチ**/




    		/**************/
    		signal.sendSig(deliveryRecord);
    		signal.closeSig("FINISH","Headquarters");

    	}catch(Exception e) {
    		//例外処理
    		e.printStackTrace();
    		
    	}
    }


    /*中継所との引渡し結果を得る*/
    public void getIsDelivery() {
    	try {
    		signal.openSig("START","CollectingRobot");
    		String getMessage=signal.getSig();
    		if(getMessage.equals(DELIVERY_FAILURE)) {
    			Luggage lug = signal.getSig();
    			this.lugList.add(lug);
    			
    		}
    		signal.closeSig("FINISH","CollectingRobot");
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }


    /*荷物を収集担当ロボットに渡す*/
    public void sendLug() {
    	try {
    		signal.openSig("START","CollectingRobot");
    		String getMessage = signal.getSig();
    		if(getMessage.contentEquals(EXIST_LUGGAGE)) { //ロボットからのメッセージが正しかったら
    			if(!this.lugList.isEmpty()) { //荷物リストに荷物があったら
    				signal.sendSig(true);
    				Luggage sendLug = this.LugList.remove(0);  //荷物リストから先頭の要素を取り出して送る
    				signal.sendSig(sendLug);
    				/******渡した荷物に対応する配達記録に発送時間を追記**/
    				deliList.updateDeliveryRecord(sendLug.getLuggageID, LuggageCondition.delivering, new Date());


    			}
    		}
    		signal.closeSig("FINISH","CollectingRobot");



    	}catch(Exception e) {
    		//例外処理
    		e.printStackTrace();
    	}
    }

    
    /*荷物を依頼人から受け取る*/
    public void getLug() {
    	RequestInfomation info = this.boundary.inputReqInfo(); //バウンダリから配達情報を入力
    	Luggage lug = new Luggage(setLuggageIDNum(),info);                      /****ID,Amount追加****/
    	lugList.add(lug);   //荷物リストに追加
    	DeliveryRecord deliveryRecord = new DeliveryRecord(lug); //荷物の配達記録生成
    	deliveryRecord.setReceiptTime(new Date()); //配達記録に受付時間を追加
    	deliList.addDeliveryRecord(deliveryRecord); //配達記録リストに追加

    	
    }
    
    public int idNum=0; //荷物ID
    
    private int setLuggageIDNum() { //荷物IDを設定
		idNum++;
		return idNum;
	}

}
