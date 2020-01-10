package recordSystem;

public class Luggage {

	/**
	 * â◊ï®ID
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
		return "â◊ï®IDÅ@" + luggageID + ",Å@â◊ï®ñº "
				+ luggageName + "\nå⁄ãqèÓïÒ\n" + requestInformation;
	}
	
	
	

}
