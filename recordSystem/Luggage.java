package recordSystem;

import java.io.Serializable;

/** �ו��N���X
 * 
 * @author bp17102,bp17108
 *<OL>
 *<LI>public Integer getLuggageID()
 *<LI>public void setLuggageID(Integer luggageID)
 *<LI>public String getLuggageName()
 *<LI>public void setLuggageName(String luggageName)
 *<LI>public RequestInformation getRequestInformation()
 *<LI>public void setRequestInformation(RequestInformation requestInformation)
 *<LI>public String toString()
 *</OL>
 */
public class Luggage implements Serializable{

	/**
	 * �ו�ID
	 */
	private Integer luggageID;
	/**
	 * �ו���
	 */
	private String luggageName;
	/**
	 * �˗����
	 */
	private RequestInformation requestInformation;
	/**
	 * �ו��h�c�Ɖו����ƈ˗������������ו����̃��\�b�h
	 * @param luggageID�@�ו��h�c
	 * @param luggageName�@�ו���
	 * @param requestInformation �˗����
	 */
	public Luggage(Integer luggageID, String luggageName,
			RequestInformation requestInformation) {
		super();
		this.luggageID = luggageID;
		this.luggageName = luggageName;
		this.requestInformation = requestInformation;
	}
	/**
	 * �ו��̃R���X�g���N�^
	 */
	public Luggage(){
		super();
		this.luggageID = 1;
		this.luggageName = "test";
		this.requestInformation = new RequestInformation();
		
	}
/**
 * �ו�ID����肷�郁�\�b�h
 * @return luggageID �ו�ID
 */
	public Integer getLuggageID() {
		return luggageID;
	}
/**
 * ID��ݒ肷�郁�\�b�h
 * @param luggageID �ו�ID
 */
	public void setLuggageID(Integer luggageID) {
		this.luggageID = luggageID;
	}

	
	/**
	 * �ו�������肷�郁�\�b�h
	 * @return luggageName �ו���
	 */
	public String getLuggageName() {
		return luggageName;
	}
/**
 * �ו�����ݒ肷�郁�\�b�h
 * @param luggageName�@�ו���
 */
	public void setLuggageName(String luggageName) {
		this.luggageName = luggageName;
	}
/**
 * �˗�������肷�郁�\�b�h
 * @return requestInformation �˗����
 */
	public RequestInformation getRequestInformation() {
		return requestInformation;
	}
/**
 * �˗�����ݒ肷�郁�\�b�h
 * @param requestInformation �˗����
 */
	public void setRequestInformation(RequestInformation requestInformation) {
		this.requestInformation = requestInformation;
	}

	@Override
	
	public String toString() {
		String separator = System.getProperty("line.separator");
		return "�ו�ID" + luggageID + separator + "�ו���"
				+ luggageName + separator + "[�ڋq���]------------------" + separator + requestInformation;
	}
	
	

}
