package recordSystem;

import java.io.Serializable;
//import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
//import java.util.ArrayList;

public class DeliveryRecord implements Serializable{
	
	private Integer deliveryID;
	
	//private  ArrayList<Luggage> luggageList = new ArrayList<Luggage>();
	private Luggage luggage;
	
	private Date receiptTime;
	
	private Date shipTime;

	private Date arrivalTime;

	private Date startTime;
	
	private Date receiveTime;

	private Date finishTime;
	
	

	

	

	public DeliveryRecord(Integer deliveryID, Luggage luggage/*ArrayList<Luggage> luggageList*/) {
		super();
		this.deliveryID = deliveryID;
		//this.luggageList = luggageList;
		this.luggage = luggage;
		this.luggageCondition = LuggageCondition.unshipped;
	}

	private LuggageCondition luggageCondition;

	public void setShipTime(Date time) {
		shipTime = time;
	}

	public void setArrivalTime(Date time) {
		arrivalTime = time;
	}

	public void setStartTime(Date time) {
		startTime = time;
	}

	public void setLuggageCondition(LuggageCondition condition) {
		luggageCondition = condition;
	}

	public void setFinishTime(Date time) {
		finishTime = time;
	}

	public void setReceiveTime(Date time /*, int elapsedTime*/) {
		/*Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		calendar.add(Calendar.SECOND, elapsedTime);
		receiveTime = calendar.getTime();*/
		receiveTime = time; 
	}
	
	public void setDeliveryID(Integer id) {
		deliveryID = id;
	}


	public Date getArrivalTime() {
		return arrivalTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public Date getReceiptTime() {
		return receiptTime;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public Date getShipTime() {
		return shipTime;
	}
	
	public Integer getDeliveryID() {
		return deliveryID;
	}

	public LuggageCondition getLuggageCondition() {
		return luggageCondition;
	}
	
	public Luggage getLuggage() {
		return luggage;
	}

	public void setLuggage(Luggage luggage) {
		this.luggage = luggage;
	}
	
	
	/*public void luggageAdd(Luggage luggage){
		
		luggageList.add(luggage);
		
	}*/
	
	/*public ArrayList<Luggage> getLuggageList(){
		
		return luggageList;
		
	}*/
	

	public void setReceiptTime(Date receiptTime) {
		this.receiptTime = receiptTime;
	}

	@Override
	public String toString() {
		return "【配達記録ID : " + deliveryID + "】\n[荷物内容]------------------\n"
				+ luggage  +"[配達情報]------------------\n"
				+ "荷物状態　: " + luggageCondition + "\n受取時間　: "
				+ showDate(receiptTime) + "\n発送時間　: "
				+ showDate(shipTime) + "\n中継所到着時間　: " + showDate(arrivalTime) + "\n配達開始時間　: "
				+ showDate(startTime) + "\n受け取り完了時間　: " + showDate(receiveTime) + "\n配達完了時間　: "
				+ showDate(finishTime) +  "\n";
	}
	
	String showDate(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy'年'MM'月'dd'日'E'曜日'k'時'mm'分'ss'秒'");
		String result = "";
		if(date==null)result = "未完了";
		else result += sdf.format(date);
		return result;	
	}

	
	
	
}