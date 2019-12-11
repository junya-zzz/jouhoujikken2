package receptionSystem;

import recordSystem.LuggageList;

public class Reception {

    private LuggageList lugList;

    private Boundary boundary;

    private Signal signal;


    public Reception(){
	this.ligList = new LuggageList();
	this.boundary = new Boundary();
	this.signal = new Signal();
    }
    /*配達記録を本部に送る*/
    public void sendDeliveryRecord() {

    }

    /*発送時間を本部に報告する*/
    public void sendShipTime() {

    }
    /*中継所との引渡し結果を得る*/
    public void getIsDelivery() {

    }
    /*荷物を収集担当ロボットに渡す*/
    public void sendLug() {

    }
    /*荷物を依頼人から受け取る*/
    public void getLug() {

    }

}
