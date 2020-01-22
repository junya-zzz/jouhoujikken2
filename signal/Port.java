package signal;

/**
 * 本部、受付所、受取人宅、中継所のポートを表す列挙型
 * @author bp17048
 *
 */

public enum Port {
	/**
	 * 本部
	 */
	HEAD(50001),
	/**
	 * 受付所
	 */
	RECEPTION(50002),
	/**
	 * 受取人宅
	 */
	RECEIVE(50009),
	/**
	 * 中継所
	 */
	RELAY(0);

	/**
	 * PCに挿すBluetoothドングルのMACアドレス
	 */
	private final String PC_BT_ADDRESS = "00:1b:dc:f2:2c:33";
	//private final String PC_BT_ADDRESS = "00:1b:dc:f2:2c:2c";
	/**
	 * 中継所に使うEV3のMACアドレス
	 */
	private final String EV3_BT_ADDRESS = "00:16:53:4f:9e:db";
	/**
	 * 該当システムのポート番号
	 */
	public int portNum;
	/**
	 * 該当システムのMACアドレス
	 */
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
