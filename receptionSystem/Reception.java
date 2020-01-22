package receptionSystem;

import recordSystem.*;
import signal.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import headquartersSystem.Headquarters;

/** 宅配受付所クラス
 * <PRE>
 * 依頼人から荷物を受け取る。
 * 収集担当ロボット、本部と通信する
 * </PRE>
 * <OL>
 * 	<LI>public static void main(String[] args)
 *  <LI>private void choseFunction()
 *  <LI>public void sendReceiptTime(DeliveryRecord d)
 *  <LI>public void sendShipTime(Luggage lug)
 *  <LI>public void getIsDelivery(PCSignal sig)
 *  <LI>public void sendLug(PCSignal sig)
 *  <LI>public Luggage getLug(RequestInformation requestInformation, String luggageName)
 *  <LI>public DeliveryRecord luggageTracking(int id) 
 *  <LI>private int setLuggageIDNum() 
 * </OL>
 * @author
 * @version 1.0
 */




public class Reception extends Thread{
	/**
	 * 
	 */
	public static final String EXIST_LUGGAGE = "existLuggage";

	/**
	 * 荷物リスト
	 */
	private ArrayList<Luggage> lugList;
	//private DeliveryRecordList deliList;
	//private ArrayDeque<DeliveryRecord>; = new ArrayDeque<DeliveryRecord>();
	/**
	 * 荷物ID
	 */
	private int idNum=0;
	/**
	 * 通信シグナル
	 */
	private PCSignal signal;
	/**
	 * mainメソッド
	 * @param args コマンドライン引数
	 * 
	 */
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

	/**
	 * 収集担当ロボットに荷物を送るメソッドか配達結果のメソッドどちらを呼び出すか判定するメソッド
	 * 
	 */
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
	/**
	 * 宅配受付所クラスのインスタンスを生成するコンストラクタ
	 */
	public Reception(){
		this.lugList = new ArrayList<Luggage>();
		this.signal = new PCSignal();
	}


	/**
	 *  荷物を依頼人から受け取ったことを本部に報告する
	 * @param d 配達記録
	 * 
	 */
	public void sendReceiptTime(DeliveryRecord d) {
		try {
			signal.openSig(Port.HEAD);
			signal.sendSig(0);
			signal.sendSig(d); 
			signal.closeSig();
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}


	/**
	 * 荷物を収集担当ロボットに渡したことを本部に報告する
	 *@param lug 荷物
	 * 
	 */
	public void sendShipTime(Luggage lug) {
		try {
			signal.openSig(Port.HEAD);
			signal.sendSig(1);
			signal.sendSig(lug.getLuggageID()); 
			signal.sendSig(LuggageCondition.shipping); 
			signal.sendSig(new Date()); 
			signal.closeSig();
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}


	/**
	 * 中継所との引渡し結果を得る
	 * @param sig PCSignal
	 * 
	 */
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


	/**
	 * 荷物を収集担当ロボットに渡す
	 * @param sig PCSignal
	 */
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
				} else {
					System.out.println("no lug.");
					sig.sendSig(false);
				}
			}
			sig.closeSig();
			if (sendLug != null) {
				sendShipTime(sendLug);
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}


	/**
	 * 荷物を依頼人から受け取る
	 * @param requestInformation 依頼情報
	 * @param luggageName　荷物の名前
	 */
	public Luggage getLug(RequestInformation requestInformation, String luggageName) {
		int id = setLuggageIDNum();
		Luggage lug = new Luggage(id,luggageName, requestInformation);                      /****ID,Amount追加****/
		lugList.add(lug);   //荷物リストに追加
		sendReceiptTime(new DeliveryRecord(lug.getLuggageID(),lug));
		return lug;
	}

	/**
	 * 荷物の状態を本部に問い合わせて取得し、表示する
	 * @param id 荷物ID
	 */
	public DeliveryRecord luggageTracking(int id) {
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

	/**
	 * 荷物IDを設定
	 * 
	 */
	private int setLuggageIDNum() { //荷物IDを設定
		idNum++;
		return idNum;
	}
}
