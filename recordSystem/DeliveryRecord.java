package recordSystem;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;


public class DeliveryRecord implements Serializable{
	
	
	/**
	 * �z�B�L�^ID
	 */
	private Integer deliveryID;
	
	/**
	 * �ו�
	 */
	private Luggage luggage;
	
	/**
	 * �ו��a���莞��
	 */
	private Date receiptTime;
	
	/**
	 * �ו������J�n����
	 */
	private Date shipTime;

	/**
	 * ���p����������
	 */
	private Date arrivalTime;

	/**
	 * �z�B�J�n����
	 */
	private Date startTime;
	
	/**
	 * �󂯎�芮������
	 */
	private Date receiveTime;

	/**
	 * �z�B��������
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
		return "�y�z�B�L�^ID : " + deliveryID + "�z" + separator + "[�ו����e]------------------" + separator
				+ luggage  +"[�z�B���]------------------" + separator
				+ "�ו���ԁ@: " + luggageCondition + separator + "��掞�ԁ@: "
				+ showDate(receiptTime) + separator + "�������ԁ@: "
				+ showDate(shipTime) + separator + "���p���������ԁ@: " + showDate(arrivalTime) + separator + "�z�B�J�n���ԁ@: "
				+ showDate(startTime) + separator + "�󂯎�芮�����ԁ@: " + showDate(receiveTime) + separator + "�z�B�������ԁ@: "
				+ showDate(finishTime) +  separator;
	}
	
	/**
	 *�t�B�[���h��Date����t�H�[�}�b�g���w�肵�ĕ������Ԃ�
	 * 
	 * @param date
	 * @return
	 */
	String showDate(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy'�N'MM'��'dd'��'E'�j��'k'��'mm'��'ss'�b'");
		String result = "";
		if(date==null)result = "������";
		else result += sdf.format(date);
		return result;	
	}

	
	
	
}