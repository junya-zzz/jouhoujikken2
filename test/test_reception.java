package test;

import java.io.IOException;

import signal.PCSignal;
import signal.Port;

public class test_reception {
	public static void main(String[] args) throws IOException{
		PCSignal signal = new PCSignal();
		signal.waitSig(Port.RECEPTION);
		System.out.println(signal.getSig() + "");
		signal.closeSig();
	}
}
