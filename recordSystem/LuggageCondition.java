package recordSystem;

public enum LuggageCondition {

	

	/**
	 * ������
	 */
	unshipped,

	shipping,

	waitDelivering,

	/**
	 * �z�B��
	 */
	delivering,

	/**
	 * �z�B�ς�
	 */
	delivered,

	/**
	 * �z�B�����ς݁i�z�B���{�b�g�A��j
	 */
	finished,

	/**
	 * ����ԈႢ
	 */
	wrongAddress,

	/**
	 * ���p���s��
	 */
	relay_absence,
	
	/**
	 * ���l�s��
	 */
	receive_absence
}
