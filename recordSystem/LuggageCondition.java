package recordSystem;

public enum LuggageCondition {

	

	/**
	 * 未発送
	 */
	unshipped,

	shipping,

	waitDelivering,

	/**
	 * 配達中
	 */
	delivering,

	/**
	 * 配達済み
	 */
	delivered,

	/**
	 * 配達完了済み（配達ロボット帰宅）
	 */
	finished,

	/**
	 * 宛先間違い
	 */
	wrongAddress,

	/**
	 * 不在
	 */
	absence

}
