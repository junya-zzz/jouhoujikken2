package recordSystem;

import java.io.Serializable;

/** 荷物クラス
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
	 * 荷物ID
	 */
	private Integer luggageID;
	/**
	 * 荷物名
	 */
	private String luggageName;
	/**
	 * 依頼情報
	 */
	private RequestInformation requestInformation;
	/**
	 * 荷物ＩＤと荷物名と依頼情報をもった荷物情報のメソッド
	 * @param luggageID　荷物ＩＤ
	 * @param luggageName　荷物名
	 * @param requestInformation 依頼情報
	 */
	public Luggage(Integer luggageID, String luggageName,
			RequestInformation requestInformation) {
		super();
		this.luggageID = luggageID;
		this.luggageName = luggageName;
		this.requestInformation = requestInformation;
	}
	/**
	 * 荷物のコンストラクタ
	 */
	public Luggage(){
		super();
		this.luggageID = 1;
		this.luggageName = "test";
		this.requestInformation = new RequestInformation();
		
	}
/**
 * 荷物IDを入手するメソッド
 * @return luggageID 荷物ID
 */
	public Integer getLuggageID() {
		return luggageID;
	}
/**
 * IDを設定するメソッド
 * @param luggageID 荷物ID
 */
	public void setLuggageID(Integer luggageID) {
		this.luggageID = luggageID;
	}

	
	/**
	 * 荷物名を入手するメソッド
	 * @return luggageName 荷物名
	 */
	public String getLuggageName() {
		return luggageName;
	}
/**
 * 荷物名を設定するメソッド
 * @param luggageName　荷物名
 */
	public void setLuggageName(String luggageName) {
		this.luggageName = luggageName;
	}
/**
 * 依頼情報を入手するメソッド
 * @return requestInformation 依頼情報
 */
	public RequestInformation getRequestInformation() {
		return requestInformation;
	}
/**
 * 依頼情報を設定するメソッド
 * @param requestInformation 依頼情報
 */
	public void setRequestInformation(RequestInformation requestInformation) {
		this.requestInformation = requestInformation;
	}

	@Override
	
	public String toString() {
		String separator = System.getProperty("line.separator");
		return "荷物ID" + luggageID + separator + "荷物名"
				+ luggageName + separator + "[顧客情報]------------------" + separator + requestInformation;
	}
	
	

}
