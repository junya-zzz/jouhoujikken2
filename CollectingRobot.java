package robotSystem;

import java.io.IOException;

import lejos.utility.Delay;
import lejos.utility.Stopwatch;
import signal.EV3Signal;

public class CollectingRobot extends Robot {

	private Boolean isDelivery;
	private EV3Signal signal;
	private final String relaySystem = "";
	private final String reception = "";
	
	public static void main(String[] args) {
		initSensors();
		lineTrace(2000, Robot.RIGHT, 300);
		closeSensors();
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
			// todo
		}
		return;
	}

	/**
	 * ���p�������z��t���Ɉړ�����
	 */
	private void relayStationToReception() {

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
	private void receptionToRelayStation() {

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
				setLuggage((signal.Luggage) signal.getSig());
			}
			signal.closeSig();
		} catch (IOException e) {
			// todo:
		}
	}

}