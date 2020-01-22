package signal;

/**
 * �{���A��t���A���l��A���p���̃|�[�g��\���񋓌^
 * @author bp17048
 *
 */

public enum Port {
	/**
	 * �{��
	 */
	HEAD(50001),
	/**
	 * ��t��
	 */
	RECEPTION(50002),
	/**
	 * ���l��
	 */
	RECEIVE(50009),
	/**
	 * ���p��
	 */
	RELAY(0);

	/**
	 * PC�ɑ}��Bluetooth�h���O����MAC�A�h���X
	 */
	private final String PC_BT_ADDRESS = "00:1b:dc:f2:2c:33";
	//private final String PC_BT_ADDRESS = "00:1b:dc:f2:2c:2c";
	/**
	 * ���p���Ɏg��EV3��MAC�A�h���X
	 */
	private final String EV3_BT_ADDRESS = "00:16:53:4f:9e:db";
	/**
	 * �Y���V�X�e���̃|�[�g�ԍ�
	 */
	public int portNum;
	/**
	 * �Y���V�X�e����MAC�A�h���X
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
