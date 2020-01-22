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
 * ���W�S�����{�b�g�N���X
 * <PRE>
 * ��t������ו����󂯎��A���p���܂łƂǂ���
 * </PRE>
 * @author bp17048
 */

public class CollectingRobot extends Robot {

	/**
	 * �ו������p���Ɉ����n���ꂽ���ǂ���������킷
	 */
	private Boolean isDelivery;

	/**
	 * �ʐM�����邽�߂̃C���X�^���X
	 */
	private EV3Signal signal;

	/**
	 * EV3�Ŏ��s�����Ƃ��ɌĂ΂��
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
	 * �z�B�̐���/���s��؂�ւ���
	 * @param result �z�B�̐���
	 */
	private void changeIsDelivery(boolean result) {
		isDelivery = result;
	}

	/**
	 * �ו��𒆌p���Ɉ����n��
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
	 * ���p�������z��t���Ɉړ�����
	 */
	private void relayStationToReception() {
		lineTrace(12000, RIGHT, 350);
		stopOnGray(RIGHT);
		turn(LEFT, 180);
	}

	/**
	 * �z�B���������������s���������z��t���ɑ��M����
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
	 * ��z��t�����璆�p���Ɉړ�����
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
	 * ��z��t������ו����󂯎��
	 * @return �ו������݂��Ȃ��A���͒ʐM�Ɏ��s�����Ƃ���false��Ԃ��B����ȊO��true��Ԃ��B
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
