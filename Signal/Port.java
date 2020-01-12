package signal;

public enum Port {
	HEAD(50001),
	RECEPTION(50002),
	RECEIVE(50003),
	RELAY(0);

	private final String PC_BT_ADDRESS = "";
	private final String EV3_BT_ADDRESS = "";
	public int portNum;
	public String address;
	
	private Port(int i) {
		portNum = i;
		if (i == 0) {
			address = EV3_BT_ADDRESS;
		} else {
			address = PC_BT_ADDRESS;
		}
	}
}
