package recordSystem;

public class RequestInformation {

	/**
	 * 依頼人名
	 */
	private String clientName;

	/**
	 * 受取人名
	 */
	private String receiverName;

	/**
	 * 依頼人電話番号
	 */
	private String clientPhoneNum;

	/**
	 * 受取人住所
	 */
	private int receiverAddress;
	
	
	

	public RequestInformation(String clientName, String receiverName,
			String clientPhoneNum, int receiverAddress) {
		super();
		this.clientName = clientName;
		this.receiverName = receiverName;
		this.clientPhoneNum = clientPhoneNum;
		this.receiverAddress = receiverAddress;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getClientPhoneNum() {
		return clientPhoneNum;
	}

	public void setClientPhoneNum(String clientPhoneNum) {
		this.clientPhoneNum = clientPhoneNum;
	}

	public int getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(int receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	@Override
	public String toString() {
		return "依頼人　" + clientName
				+ "\n受取人　" + receiverName + "\n依頼人電話番号　"
				+ clientPhoneNum + "\n受取人宅住所　" + receiverAddress+"\n";
	}
	
	

	
}
