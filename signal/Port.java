package signal;

public enum Port {
	HEAD(50001),
	RECEPTION(50002),
	RECEIVE(50003),
	RELAY(0);

	private final String PC_BT_ADDRESS = "00:1b:dc:f2:2c:33";
	//private final String PC_BT_ADDRESS = "00:1b:dc:f2:2c:2c";
	private final String EV3_BT_ADDRESS = "00:16:53:4f:9e:db";
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
