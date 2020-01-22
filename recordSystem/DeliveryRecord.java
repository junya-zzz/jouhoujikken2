package recordSystem;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;


public class DeliveryRecord implements Serializable{
	
	/**
	 * @param deliveryID 配達記録ID
	 * @param luggage　荷物
	 * @param receiptTime　荷物預かり時間
	 * @param shiptime　荷物発送開始時間
	 * @param arrivalTime　中継所到着時間
	 * @param startTime　配達開始時間
	 * @param receiveTime　受け取り完了時間
	 * @param finishTime　配達完了時間
	 */
	private Integer deliveryID;
	
	private Luggage luggage;
	
	private Date receiptTime;
	
	private Date shipTime;

	private Date arrivalTime;

	private Date startTime;
	
	private Date receiveTime;

	private Date finishTime;
	
	public DeliveryRecord(Integer deliveryID, Luggage luggage) {
		super();
		this.deliveryID = deliveryID;
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

	public void setReceiveTime(Date time ) {
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
	
	public void setReceiptTime(Date receiptTime) {
		this.receiptTime = receiptTime;
	}

	@Override
	public String toString() {
		String separator = System.getProperty("line.separator");
		return "【配達記録ID : " + deliveryID + "】" + separator + "[荷物内容]------------------" + separator
				+ luggage  +"[配達情報]------------------" + separator
				+ "荷物状態　: " + luggageCondition + separator + "受取時間　: "
				+ showDate(receiptTime) + separator + "発送時間　: "
				+ showDate(shipTime) + separator + "中継所到着時間　: " + showDate(arrivalTime) + separator + "配達開始時間　: "
				+ showDate(startTime) + separator + "受け取り完了時間　: " + showDate(receiveTime) + separator + "配達完了時間　: "
				+ showDate(finishTime) +  separator;
	}
	
	/**
	 *フィールドのDateからフォーマットを指定して表示する
	 * 
	 * @param date
	 * @return
	 */
	String showDate(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy'年'MM'月'dd'日'E'曜日'k'時'mm'分'ss'秒'");
		String result = "";
		if(date==null)result = "未完了";
		else result += sdf.format(date);
		return result;	
	}

	
	
	
}