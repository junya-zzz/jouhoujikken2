package robotSystem;

import java.util.Date;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
public class DeliveryRobot extends Robot {

	private String deliveryResult;

	/**
	 * 受け取り時間を得るために中継所から受取人宅までの時間を計り、配達開始時間に足して、受け取り時間を作る
	 */
	private Date elapsedTime;
    //荷物のフィールド名をminamiと仮定
	private Signal signal;

	private Signal signal;

	private Signal signal;

	private void changeDeliveryResult(String result) {
	    deliveryResult=result;
	}

	private void relayStationToWaiting() {

	}

	private void receiverToRelayStation() {

	}

	private void waitingToRelayStation() {

	}

	/**
	 * 中継所から受取人宅まで行く
	 */
	private void relayStationToReceiver() {

	}

	public void getLug() {
	    long start=0;
	    long end=0;
	    Signal sig=new Signal();

	    if(sig.openSig("START",Receiver)=="OK"){
		sig.sendSig("isExist",Receiver);
		
		start=System.currentTimeMillis();
		while(end-start>10 || (sig.getSig().toString).equals("Exist")){
		    end=System.currentTimeMillis();
		    try{
			Thread.sleep(1000);
		    }catch (InterruptedException e){
			e.printStackTrace();
		    }
		}//while

		if(end-start>10){
		    sendSig(true,Receiver);
		String receiverName=sig.getSig().toString();
		String receiverAddress=sig.getSig().toString();

		if(receiverName.equals(minami.receiverName) && receiverAddress.equals(minami.receiverAddress)){
		    changeDeliveryResult("FINISHED");
		    sendSig(true,Receiver);
		    sendSig(minami,Receiver);
		}//ifreceiver

		else{
		    changeDeliveryResult("WRONG");
		    sendSig(false,Receiver);
		}//else

		}//ifendstart
		else  sendSig(false,Receiver);
		sig.closeSig("FINISH",Receiver);
	    }//ifopensig
	   
	}//getLug

	public void sendLug() {

	}

	public void sendDeliveryRecord() {

	}

	public void startTimer() {

	}

	public void stopTimer() {

	}

}
