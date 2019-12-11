package recordSystem;

import java.util.Calendar;
import java.util.Date;

public class DeliveryRecord {

	private Date arrivalTime;


	private Date startTime;

	private Date finishTime;

	private Date receiptTime;

	private Date receiveTime;

	private Date shipTime;

	private Enum luggageCondition;

	public void writeShipTime(Date time) {
		shipTime = time;
	}

	public void writeArrivalTime(Date time) {
		arrivalTime = time;
	}

	public void writeStartTime(Date time) {
		startTime = time;
	}

	public void changeLC(String condition) {
		luggageCondition = condition;
	}

	public void writeFinishTime(Date time) {
		finishTime = time;
	}

	public void writeReceiveTime(Date time, int elapsedTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		calendar.add(Calendar.SECOND, elapsedTime);
		receiveTime = calendar.getTime();
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

	public Enum getLuggageCondition() {
		return luggageCondition;
	}
}
