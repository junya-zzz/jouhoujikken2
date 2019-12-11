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
		stopOnGray();
		closeSensors();
	}

	private void changeIsDelivery(boolean result) {
		isDelivery = result;
	}

	public void sendLug() {
		signal = new EV3Signal();
		String connectionStat = signal.openSig(relaySystem, 10000);
		if (connectionStat.equals("failed")) {
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

	private void relayStationToReception() {

	}

	public void sendIsDelivery() {
		signal = new EV3Signal();
		try {
			String connectionStat = signal.openSig(reception, 0);
			signal.sendSig(reception);
			if (!isDelivery) {
				signal.sendSig(getLuggage());
			}
			signal.closeSig();
		} catch (IOException e) {
			// TODO: handle exception
		}
	}

	private void receptionToRelayStation() {

	}

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
