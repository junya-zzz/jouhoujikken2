package signal;

public enum Port {
	HEAD_PORT(50001),
	RECEPTION_PORT(50002),
	RECEIVE_PORT(50003);
	
	public int portNum;
	
	private Port(int i) {
		portNum = i;
	}
}
