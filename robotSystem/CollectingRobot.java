package robotSystem;

import java.io.IOException;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;
import recordSystem.Luggage;
import signal.EV3Signal;

public class CollectingRobot extends Robot {

	private static Boolean isDelivery;
	private static EV3Signal signal;
	private final static String relaySystem = "";
	private final static String reception = "00:1b:dc:f2:2c:2c";
	
	public static void main(String[] args) throws Exception{
		getLugfrom();
		LCD.clear();
		LCD.drawString(getLuggage().toString(), 0, 0);
		LCD.refresh();
		Delay.msDelay(1000000000);
	}

	/**
	 * �z�B�̐���/���s��؂�ւ���
	 * @param result �z�B�̐���
	 */
	private static void changeIsDelivery(boolean result) {
		isDelivery = result;
	}

	/**
	 * �ו��𒆌p���Ɉ����n��
	 */
	public static void sendLug() {
		signal = new EV3Signal();
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.reset();
		while (signal.openSig(relaySystem)) {
			if (10000 < stopwatch.elapsed()) {
				changeIsDelivery(false);
				return;
			}
		}
		try {
			signal.sendSig(getLuggage());
			signal.getSig();
			changeIsDelivery(true);
			signal.closeSig();
		} catch (IOException e) {
			 System.exit(1);
		}
		return;
	}

	/**
	 * ���p�������z��t���Ɉړ�����
	 */
	private static void relayStationToReception() {
		lineTrace(5000, RIGHT, 350);
		stopOnGray(RIGHT);
		turn(LEFT, 180);
	}

	/**
	 * �z�B���������������s���������z��t���ɑ��M����
	 */
	public static void sendIsDelivery() {
		signal = new EV3Signal();
		try {
			signal.openSig(reception);
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
	 * ��z��t�����璆�p���Ɉړ�����
	 */
	private static void receptionToRelayStation() {
		lineTrace(3500, RIGHT, 350);
		Delay.msDelay(1000);
		stopOnGray(RIGHT);
		Delay.msDelay(1000);
		robotExists(RIGHT, 90, 50);
		turn(LEFT, 90);
		lineTrace(200, RIGHT, 300);
		lineTrace(1000, LEFT, 300);
		stopOnGray(LEFT);
		turn(LEFT, 180);
	}

	/**
	 * ��z��t������ו����󂯎��
	 */
	public static void getLugfrom() throws Exception{
		LCD.clear();
		LCD.drawString("wait luggage", 0, 0);
		LCD.refresh();
		boolean existsLug = false;
		signal = new EV3Signal();
		//try {
			while (!existsLug) {
				while (!signal.openSig(reception)) {
					Delay.msDelay(3000);
				}
				LCD.drawString("connected.", 0, 1);
				LCD.refresh();
				signal.sendSig("existLuggage");
				existsLug = signal.getBoolSig();
				if (existsLug) {
					Luggage l = (Luggage) signal.getSig();
					setLuggage(l);
				}
				signal.closeSig();
			}
			LCD.drawString("luggage accepted.", 0, 3);
		/*} catch (IOException e) {
			LCD.clear();
			LCD.refresh();
			e.printStackTrace();
			while(true);
		}*/
	}

}
