package receptionSystem;

import java.util.ArrayList;
import java.util.Date;

import recordSystem.*;






public class Reception {
	private final int portNum = 8080;
    private ArrayList<Luggage> lugList;
   // private DeliveryRecordList deliList;
    private ArrayList<DeliveryRecord> deliList;

    private Signal signal;
    public final String  EXIST_LUGGAGE = "existLuggage";
    public final String  DELIVERY_FAILURE="failure";



    public Reception(){
	this.lugList = new ArrayList<Luggage>();
	this.deliList = new ArrayList<DeliveryRecord>();

	this.signal = new Signal();
    }


    /*発送時間を本部に報告する*/    //未完成
    public void sendShipTime() {
    		DeliveryRecord deliveryRecord = this.deliList.remove(0);
    	try {
    		signal.openSig("START","Headquarters");

    		signal.sendSig(deliveryRecord);
    		signal.closeSig("FINISH","Headquarters");


    		this.getIsDelivery();
    	}catch(Exception e) {
    		//例外処理
    		e.printStackTrace();

    	}
    	this.getIsDelivery(deliveryRecord);
    }


    /*中継所との引渡し結果を得る*/   //未完成
    public void getIsDelivery(DeliveryRecord deliveryRecord) {
    	try {
    		signal.openSig("START","CollectingRobot");
    		String getMessage=signal.getSig();
    		if(getMessage.equals(DELIVERY_FAILURE)) {
    			Luggage lug = signal.getSig();
    			this.lugList.add(lug);
    			this.deliList.add(deliveryRecord);

    		}
    		signal.closeSig("FINISH","CollectingRobot");
    	}catch(Exception e) {
    		e.printStackTrace();
    	}

    	this.sendLug();
    }


    /*荷物を収集担当ロボットに渡す*/ //未完成
    public void sendLug() {
    	try {
    		signal.openSig("START","CollectingRobot");
    		String getMessage = signal.getSig();
    		if(getMessage.contentEquals(EXIST_LUGGAGE)) { //ロボットからのメッセージが正しかったら
    			while(true) {
	    			if(!this.lugList.isEmpty()) { //荷物リストに荷物があったら
	    				signal.sendSig(true);
	    				Luggage sendLug = this.LugList.remove(0);  //荷物リストから先頭の要素を取り出して送る
	    				signal.sendSig(sendLug);
	    				/******渡した荷物に対応する配達記録に発送時間を追記**/
	    				DeliveryRecord a=deliList.remove(0).setStartTime(new Date());
	    				deliList.add(0,a);
	    				signal.closeSig("FINISH","CollectingRobot");

	    				this.sendShipTime();

	    			}else {
	    				continue;
	    			}
    			}
    		}



    	}catch(Exception e) {
    		//例外処理
    		e.printStackTrace();
    	}
    }


    /*荷物を依頼人から受け取る*/	 //完成
    public void getLug() {
    	while(true) {
	    	SocketServer socketServer = new SocketServer(portNum);
	    	String[] readInformation=socketServer.read();
		    if(readInformation!=null) {
		    	RequestInformation reqInfo = new RequestInformation(readInformation[0],readInformation[1],readInformation[2],readInformation[3]);
		    	Luggage lug = new Luggage(setLuggageIDNum(),readInformation[4],reqInfo);                      /****ID,Amount追加****/
		    	lugList.add(lug);   //荷物リストに追加
		    	DeliveryRecord deliveryRecord = new DeliveryRecord(lug); //荷物の配達記録生成
		    	deliveryRecord.setReceiptTime(new Date()); //配達記録に受付時間を追加
		    	deliList.add(deliveryRecord); //配達記録リストに追加


	    	}
    	}

    }

    public  static int idNum=0; //荷物ID

    private int setLuggageIDNum() { //荷物IDを設定   //完成
		idNum++;
		return idNum;
	}

}
