package recordSystem;

import java.io.Serializable;

public class Luggage implements Serializable{

	/**
	 * 荷物ID
	 */
	private Integer luggageID;
	
	private String luggageName;

	private RequestInformation requestInformation;
	
	public Luggage(Integer luggageID, String luggageName,
			RequestInformation requestInformation) {
		super();
		this.luggageID = luggageID;
		this.luggageName = luggageName;
		this.requestInformation = requestInformation;
	}
	
	public Luggage(){
		super();
		this.luggageID = 1;
		this.luggageName = "test";
		this.requestInformation = new RequestInformation();
		
	}

	public Integer getLuggageID() {
		return luggageID;
	}

	public void setLuggageID(Integer luggageID) {
		this.luggageID = luggageID;
	}

	
	
	public String getLuggageName() {
		return luggageName;
	}

	public void setLuggageName(String luggageName) {
		this.luggageName = luggageName;
	}

	public RequestInformation getRequestInformation() {
		return requestInformation;
	}

	public void setRequestInformation(RequestInformation requestInformation) {
		this.requestInformation = requestInformation;
	}

	@Override
	public String toString() {
		String separator = System.getProperty("line.separator");
		return "荷物ID　：　" + luggageID + separator + "荷物名　：　 "
				+ luggageName + separator + "[顧客情報]------------------" + separator + requestInformation;
	}
	
	

}
