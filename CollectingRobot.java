package robotSystem;

import java.io.IOException;

import lejos.utility.Delay;

import signal.EV3Signal;

public class CollectingRobot extends Robot {

	private Boolean isDelivery;
	private EV3Signal signal;
	private final String relaySystem = "";
	private final String reception = "";
	
	public static void main(String[] args) {
		initSensors();
		lineTrace(2000, Robot.RIGHT);
		closeSensors();
	}

	/**
	 * 配達の成功/失敗を切り替える
	 * @param result 配達の成否
	 */
	private void changeIsDelivery(boolean result) {
		isDelivery = result;
	}

	/**
	 * 荷物を中継所に引き渡す
	 */
	public void sendLug() {
		signal = new EV3Signal();
		boolean connectionStat = signal.openSig(relaySystem, 10000);
		if (!connectionStat) {
			changeIsDelivery(false);
			return;
		}
		try {
			signal.sendSig(getLuggage());
			signal.getSig();
			changeIsDelivery(true);
			signal.closeSig();
		} catch (IOException e) {
			// todo
		}
		return;
	}

	/**
	 * 中継所から宅配受付所に移動する
	 */
	private void relayStationToReception() {

	}

	/**
	 * 配達が成功したか失敗したかを宅配受付所に送信する
	 */
	public void sendIsDelivery() {
		signal = new EV3Signal();
		try {
			signal.openSig(reception, 0);
			signal.sendSig(reception);
			if (!isDelivery) {
				signal.sendSig(getLuggage());
			}
			signal.closeSig();
		} catch (IOException e) {
			// TODO: handle exception
		}
	}

	/**
	 * 宅配受付所から中継所に移動する
	 */
	private void receptionToRelayStation() {

	}

	/**
	 * 宅配受付所から荷物を受け取る
	 */
	public void getLugfrom() {
		signal = new EV3Signal();
		try {
			signal.openSig(reception, 0);
			boolean existsLug = (boolean) signal.getSig();
			if (existsLug) {
				setLuggage((signal.Luggage) signal.getSig());
			}
			signal.closeSig();
		} catch (IOException e) {
			// todo:
		}
	}

}
