package robotSystem;

import java.util.Date;

import lejos.hardware.lcd.LCD;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.SampleProvider;
import Signal.Luggage;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.SensorMode;
public class DeliveryRobot extends Robot {

	private static String deliveryResult;

	/**
	 * 受け取り時間を得るために中継所から受取人宅までの時間を計り、配達開始時間に足して、受け取り時間を作る
	 */
	private static long elapsedTime;   //元々Date型だったけどlong型だった
    
	/*private Signal signal;

	private Signal signal;

	private Signal signal;*/

	public static void main(String[] args) {
	    while(true){
		Thread.sleep(10000);
		waitingToRelayStation();
		if(getLug()){
		    relayStationToReceiver();
		    sendLug();
		    receiverToRelayStation();
		    sendDeliveryRecord();
		}//if
		relayStationToWaiting();
	    }//while(1)
	}//main


	private static void changeDeliveryResult(String result) {
	    deliveryResult=result;
	}

	private static void relayStationToWaiting() {
		Robot rob=new Robot();
		rob.robotExists(RIGHT,45,50);
		rob.stopOnGray(LEFT);
		//lineTrace(300,RIGHT,300);   灰色ゾーンを通り過ぎるための調整用
		rob.stopOnGray(RIGHT);
		rob.changePos("Waiting");
		rob.turn(LEFT,180);
	}//relayStationToWaiting

	private static void receiverToRelayStation() {
		Robot rob=new Robot();
		 Luggage lug=rob.getLuggage();
		    Integer ID=lug.receiverAddress;
		    Integer re;//余り
		    Integer quo;//商
		    re=ID/4;
		    quo=ID%4;
		    for(int i=0;i<re;i++){
				rob.stopOnGray(RIGHT);
				//調整用
			}
	    rob.turn(RIGHT,90);
	    for(int i=0;i<quo;i++){
			rob.stopOnGray(LEFT);
			//調整用
		}
	    rob.stopOnGray(RIGHT);
	    rob.robotExists(RIGHT,45,50);
	    rob.turn(RIGHT,90);
	    rob.stopOnGray(RIGHT);
		rob.changePos("RelayStation");
		rob.turn(LEFT,180);
	}//receiverToRelayStation

	private static void waitingToRelayStation() {
		Robot rob=new Robot();
		rob.stopOnGray(LEFT);
		//lineTrace(300,RIGHT,300);   灰色ゾーンを通り過ぎるための調整用
		  rob.robotExists(RIGHT,45,50);
		rob.stopOnGray(RIGHT);
		rob.changePos("RelayStation");
		rob.turn(LEFT,180);
	}//waitingToRelayStation

	/**
	 * 中継所から受取人宅まで行く
	 */
	private static void relayStationToReceiver() {
		Robot rob=new Robot();
		long start=0;
	    long end=0;
	    Luggage lug=rob.getLuggage();
	    Integer ID=lug.receiverAddress;
	    Integer re;//余り
	    Integer quo;//商
	    re=ID/4;
	    quo=ID%4;
	    rob.robotExists(RIGHT,45,50);
	    start=System.currentTimeMillis();
		rob.stopOnGray(LEFT);
		rob.stopOnGray(LEFT);
		
	    for(int i=0;i<re;i++){
				rob.stopOnGray(RIGHT);
				//goStraight(300,300);   灰色ゾーンを通り過ぎるための調整用
			}
	    rob.turn(LEFT,90);
	    for(int i=0;i<quo;i++){
			rob.stopOnGray(RIGHT);
			//goStraight(300,300);   灰色ゾーンを通り過ぎるための調整用
		}
	    end=System.currentTimeMillis();
	    elapsedTime=end-start;
	    rob.changePos("Receiver");
		rob.turn(LEFT,180);
	}

	public static boolean getLug() {
		Robot rob=new Robot();
	    Signal sig=new Signal();
	    if(sig.openSig("START",RelayStation)=="OK"){
		sig.sendSig("workExist",RelayStation);
		String flag=sig.getSig().toString();
		if(flag.equals("true")){
		    Luggage lug=sig.getSig();//荷物多分大丈夫
		    rob.setLuggage(lug);
		    sig.closeSig("FINISH",RelayStation);
		    return true;
		}//iftrue
		else{
		    sig.closeSig("FINISH",RelayStation);
		    return false;
		}//else
	    }//ifOK
	    return false;
	}//getLug

	public static void sendLug() {
	    long start=0;
	    long end=0;
	    Signal sig=new Signal();
        Robot rob=new Robot();
	    if(sig.openSig("START",Receiver)=="OK"){
		sig.sendSig("isExist",Receiver);
		
		start=System.currentTimeMillis();
		while(end-start>10 || (sig.getSig().toString()).equals("Exist")){
		    end=System.currentTimeMillis();
		    try{
			Thread.sleep(1000);
		    }catch (InterruptedException e){
			e.printStackTrace();
		    }
		}//while

		if(end-start>10){
		    sig.sendSig(true,Receiver);
		String receiverName=sig.getSig().toString();
		String receiverAddress=sig.getSig().toString();
        Luggage lug=rob.getLuggage();
		if(receiverName.equals(lug.receiverName) && receiverAddress.equals(lug.receiverAddress)){
		    changeDeliveryResult("FINISHED");
		    sig.sendSig(true,Receiver);
		    sig.sendSig(lug,Receiver);
		}//ifreceiver

		else{
		    changeDeliveryResult("WRONG");
		    sig.sendSig(false,Receiver);
		}//else

		}//ifendstart
		else  sig.sendSig(false,Receiver);
		sig.closeSig("FINISH",Receiver);
	    }//ifopensig
	   
	}//sendLug

	public static void sendDeliveryRecord() {
		Robot rob=new Robot();
	    Signal sig=new Signal();
	    Luggage lug=rob.getLuggage();
	    if(sig.openSig("START",RelayStation)=="OK"){
		sig.sendSig(deliveryResult,RelayStation);
		if(deliveryResult.equals("FINISHED")){
		    sig.sendSig(elapsedTime,RelayStation);
		    sig.sendSig(lug.luggageID,RelayStation);
		}//ifFINISHED
		else{
		    sig.sendSig(lug,RelayStation);
		}
		sig.closeSig("FINISH",RelayStation);
	    }//ifopen	
	}//sendDeliveryRecord

	
}