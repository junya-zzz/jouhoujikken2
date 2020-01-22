package robotSystem;

import java.io.IOException;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;
import signal.EV3Signal;
import signal.Port;
import recordSystem.Luggage;
import relaySystem.RelayStation;

/**
 * 収集担当ロボットクラス
 * <PRE>
 * 受付所から荷物を受け取り、中継所までとどける
 * </PRE>
 * @author bp17048
 */

public class CollectingRobot extends Robot {

	/**
	 * 荷物が中継所に引き渡されたかどうかをあらわす
	 */
	private Boolean isDelivery;

	/**
	 * 通信をするためのインスタンス
	 */
	private EV3Signal signal;

	/**
	 * EV3で実行したときに呼ばれる
	 */
	public static void main(String[] args){
		CollectingRobot c = new CollectingRobot();
		c.initSensors();
		while (true) {
			while (!c.getLugfrom()) {
				Delay.msDelay(3000);
			}
			c.receptionToRelayStation();
			c.sendLug();
			c.relayStationToReception();
			c.sendIsDelivery();
		}
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
	private void sendLug() {
		signal = new EV3Signal();
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.reset();
		try {
			while (!signal.openSig(Port.RELAY)) {
				if (10000 < stopwatch.elapsed()) {
					changeIsDelivery(false);
					return;
				}
			}
			signal.sendSig(RelayStation.SEND_LUG_TO_RELAY);
			signal.sendSig(getLuggage());
			signal.getSig();
			LCD.clear();
			LCD.drawString("ok", 0, 2);
			Delay.msDelay(5000);
			LCD.refresh();
			changeIsDelivery(true);
			signal.closeSig();
		} catch (IOException e) {
			System.exit(1);
		}
		return;
	}

	/**
	 * 中継所から宅配受付所に移動する
	 */
	private void relayStationToReception() {
		lineTrace(12000, RIGHT, 350);
		stopOnGray(RIGHT);
		turn(LEFT, 180);
	}

	/**
	 * 配達が成功したか失敗したかを宅配受付所に送信する
	 */
	private void sendIsDelivery() {
		signal = new EV3Signal();
		try {
			signal.openSig(Port.RECEPTION);
			signal.sendSig(1);
			signal.sendSig(isDelivery);
			if (!isDelivery) {
				signal.sendSig(getLuggage());
				setLuggage(null);
			}
			signal.closeSig();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * 宅配受付所から中継所に移動する
	 */
	private void receptionToRelayStation() {
		lineTrace(3500, RIGHT, 350);
		Delay.msDelay(1000);
		stopOnGray(RIGHT);
		Delay.msDelay(1000);
		turn(LEFT, 30);
		while (robotExists(RIGHT, 120, 1.0f)){
			turn(LEFT, 120);
			Delay.msDelay(10000);
		}
		turn(LEFT, 90);
		lineTrace(200, RIGHT, 300);
		lineTrace(1000, LEFT, 300);
		stopOnGray(LEFT);
		turn(LEFT, 180);
	}


	/**
	 * 宅配受付所から荷物を受け取る
	 * @return 荷物が存在しない、又は通信に失敗したときはfalseを返す。それ以外はtrueを返す。
	 */
	private boolean getLugfrom() {
		signal = new EV3Signal();
		boolean result = false;
		try {
			while (!signal.openSig(Port.RECEPTION)) {
				Delay.msDelay(3000);
			}
			signal.sendSig(0);
			signal.sendSig("existLuggage");
			boolean existsLug = (boolean) signal.getSig();
			if (existsLug) {
				setLuggage((Luggage)signal.getSig());
				result = true;
			} else result = false;
			signal.closeSig();
		} catch (IOException e) {
			System.exit(1);
		}
		return result;
	}

}
