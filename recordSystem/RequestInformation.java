package recordSystem;

import java.io.Serializable;

public class RequestInformation implements Serializable{

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
	
	
	

	public RequestInformation() {
		super();
		this.clientName = "clientName";
		this.receiverName = "receiverName";
		this.clientPhoneNum = "clientPhoneNum";
		this.receiverAddress = 1;
	}

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
		return "萓晞�莠ｺ蜷阪��壹�" + clientName
				+ "\n蜿怜叙莠ｺ蜷阪��壹�" + receiverName + "\n萓晞�莠ｺ髮ｻ隧ｱ逡ｪ蜿ｷ縲�ｼ壹�"
				+ clientPhoneNum + "\n蜿怜叙莠ｺ螳�ｽ乗園縲�ｼ壹�" + receiverAddress+"\n";
	}
	
	

	
}
