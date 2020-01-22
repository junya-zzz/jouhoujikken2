package recordSystem;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;


public class DeliveryRecord implements Serializable{
	
	
	/**
	 * 配達記録ID
	 */
	private Integer deliveryID;
	
	/**
	 * 荷物
	 */
	private Luggage luggage;
	
	/**
	 * 荷物預かり時間
	 */
	private Date receiptTime;
	
	/**
	 * 荷物発送開始時間
	 */
	private Date shipTime;

	/**
	 * 中継所到着時間
	 */
	private Date arrivalTime;

	/**
	 * 配達開始時間
	 */
	private Date startTime;
	
	/**
	 * 受け取り完了時間
	 */
	private Date receiveTime;

	/**
	 * 配達完了時間
	 */
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
	 *フィールドのDateからフォーマットを指定して文字列を返す
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