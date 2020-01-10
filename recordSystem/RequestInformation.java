package recordSystem;

public class RequestInformation {

	/**
	 * �˗��l��
	 */
	private String clientName;

	/**
	 * ���l��
	 */
	private String receiverName;

	/**
	 * �˗��l�d�b�ԍ�
	 */
	private String clientPhoneNum;

	/**
	 * ���l�Z��
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
		return "�˗��l�@" + clientName
				+ "\n���l�@" + receiverName + "\n�˗��l�d�b�ԍ��@"
				+ clientPhoneNum + "\n���l��Z���@" + receiverAddress+"\n";
	}
	
	

	
}
