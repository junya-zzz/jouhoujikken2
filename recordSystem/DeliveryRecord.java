package recordSystem;

//import java.util.Calendar;
import java.util.Date;
//import java.util.ArrayList;

public class DeliveryRecord {
	
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
	

	@Override
	public String toString() {
		return "\n�y�z�B�L�^ID " + deliveryID + "�z\n�ו����e\n"
				+ luggage + "\n�ו���ԁ@" + luggageCondition +"\n--���ԏ��--\n��掞�ԁ@" + receiptTime + "\n�������ԁ@"
				+ shipTime + "\n���p���������ԁ@" + arrivalTime + "\n�z�B�J�n���ԁ@"
				+ startTime + "\n�󂯎�芮�����ԁ@" + receiveTime + "\n�z�B�������ԁ@"
				+ finishTime +  "\n";
	}

	
	
	
}