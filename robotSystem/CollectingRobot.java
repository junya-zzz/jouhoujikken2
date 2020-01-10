package robotSystem;

import java.io.IOException;

import lejos.utility.Delay;
import lejos.utility.Stopwatch;
import recordSystem.Luggage;
import signal.EV3Signal;

public class CollectingRobot extends Robot {

	private Boolean isDelivery;
	private EV3Signal signal;
	private final String relaySystem = "";
	private final String reception = "";
	
	public static void main(String[] args) {
		initSensors();
		receptionToRelayStation();
	}

	/**
	 * �z�B�̐���/���s��؂�ւ���
	 * @param result �z�B�̐���
	 */
	private void changeIsDelivery(boolean result) {
		isDelivery = result;
	}

	/**
	 * �ו��𒆌p���Ɉ����n��
	 */
	public void sendLug() {
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
	public void sendIsDelivery() {
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
	public void getLugfrom() {
		signal = new EV3Signal();
		try {
			while (signal.openSig(reception)) {
				Delay.msDelay(3000);
			}
			boolean existsLug = (boolean) signal.getSig();
			if (existsLug) {
				setLuggage((Luggage) signal.getSig());
			}
			signal.closeSig();
		} catch (IOException e) {
			System.exit(1);
		}
	}

}
