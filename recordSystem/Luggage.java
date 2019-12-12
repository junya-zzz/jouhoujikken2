package recordSystem;

public class Luggage {

	/**
	 * 荷物ID
	 */
	private Integer luggageID;
	
	private Integer luggageAmount;

	private RequestInformation requestInformation;
	
	public Luggage(Integer luggageID, Integer luggageAmount,
			RequestInformation requestInformation) {
		super();
		this.luggageID = luggageID;
		this.luggageAmount = luggageAmount;
		this.requestInformation = requestInformation;
	}

	public Integer getLuggageID() {
		return luggageID;
	}

	public void setLuggageID(Integer luggageID) {
		this.luggageID = luggageID;
	}

	public Integer getAmount() {
		return luggageAmount;
	}

	public void setAmount(Integer amount) {
		luggageAmount = amount;
	}

	public RequestInformation getRequestInformation() {
		return requestInformation;
	}

	public void setRequestInformation(RequestInformation requestInformation) {
		this.requestInformation = requestInformation;
	}

	@Override
	public String toString() {
		return "荷物ID　" + luggageID + ",　数量 "
				+ luggageAmount + "個\n顧客情報\n" + requestInformation;
	}
	
	
	

}
