package recordSystem;

public class Luggage {

	/**
	 * 荷物ID
	 */
	private Integer luggageID;
	
	private String luggageName;

	private RequestInformation requestInformation;
	
	public Luggage(int luggageID){
		super();
		this.luggageID = luggageID;
		this.luggageName = "test";
		this.requestInformation = new RequestInformation();
		
	}
	
	public Luggage(Integer luggageID, String luggageName,
			RequestInformation requestInformation) {
		super();
		this.luggageID = luggageID;
		this.luggageName = luggageName;
		this.requestInformation = requestInformation;
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
		return "荷物ID　：　" + luggageID + "\n荷物名　：　 "
				+ luggageName + "\n[顧客情報]------------------\n" + requestInformation;
	}
	
	
	

}
