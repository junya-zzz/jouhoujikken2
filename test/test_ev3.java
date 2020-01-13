package test;

import java.io.IOException;

import signal.EV3Signal;
import signal.Port;

public class test_ev3 {
	public static void main(String[] args) throws IOException{
		EV3Signal signal = new EV3Signal();
		signal.openSig(Port.RECEPTION);
		signal.sendSig(123);
		signal.closeSig();
	}
}
