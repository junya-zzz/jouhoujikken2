package robotSystem;

import java.io.IOException;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;
import signal.EV3Signal;
import signal.Port;
import recordSystem.Luggage;
import relaySystem.RelayStation;

public class CollectingRobot extends Robot {

	private Boolean isDelivery;
	private EV3Signal signal;
	private final String reception = "";
	
	public static void main(String[] args) throws IOException {
		CollectingRobot c = new CollectingRobot();
		//c.initSensors();
			if (c.getLugfrom()){
				//c.receptionToRelayStation();
				c.sendLug();
				//c.relayStationToReception();
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
	public void sendLug() throws IOException{
		signal = new EV3Signal();
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.reset();
		while (!signal.openSig(Port.RELAY)) {
			if (10000 < stopwatch.elapsed()) {
				changeIsDelivery(false);
				return;
			}
		}
		try {
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
	private static void relayStationToReception() {
		lineTrace(10000, RIGHT, 350);
		stopOnGray(RIGHT);
		turn(LEFT, 180);
	}

	/**
	 * 配達が成功したか失敗したかを宅配受付所に送信する
	 */
	public void sendIsDelivery() {
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
	private static void receptionToRelayStation() {
		lineTrace(3500, RIGHT, 350);
		Delay.msDelay(1000);
		stopOnGray(RIGHT);
		Delay.msDelay(1000);
		while (robotExists(RIGHT, 90, 0.2f)){
			turn(LEFT, 90);
		}
		turn(LEFT, 90);
		lineTrace(200, RIGHT, 300);
		lineTrace(1000, LEFT, 300);
		stopOnGray(LEFT);
		turn(LEFT, 180);
	}


	/**
	 * 宅配受付所から荷物を受け取る
	 */
	public boolean getLugfrom() {
		signal = new EV3Signal();
		boolean result = false;
		try {
			signal.sendSig(0);
			while (!signal.openSig(Port.RECEPTION)) {
				Delay.msDelay(3000);
			}
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
