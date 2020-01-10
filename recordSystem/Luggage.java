package recordSystem;

public class Luggage {

	/**
	 * �ו�ID
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
		return "�ו�ID�@" + luggageID + ",�@�ו��� "
				+ luggageName + "\n�ڋq���\n" + requestInformation;
	}
	
	
	

}
