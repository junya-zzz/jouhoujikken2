package robotSystem;
import recordSystem.Luggage;
import signalSystem.EV3Signal;

import java.io.IOException;
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
	    	 try {
		Thread.sleep(10000);
	    	 } catch(InterruptedException e){
	    		            e.printStackTrace();
	    		       }      

		waitingToRelayStation();
		try{
		if(getLug()){
			
		    relayStationToReceiver();
		    sendLug();
		    receiverToRelayStation();
		    sendDeliveryRecord();
		}//if
		}catch(IOException e){
			e.printStackTrace();
		}
		relayStationToWaiting();
	    }//while(1)
	}//main


	private static void changeDeliveryResult(String result) {
	    deliveryResult=result;
	}

	private static void relayStationToWaiting() {
		robotExists(RIGHT,45,50);
		stopOnGray(LEFT);
		//lineTrace(300,RIGHT,300);   灰色ゾーンを通り過ぎるための調整用
		stopOnGray(RIGHT);
		changePos("Waiting");
		turn(LEFT,180);
	}//relayStationToWaiting

	private static void receiverToRelayStation() {
		
		    Integer ID=getLuggage().getRequestInformation().getReceiverAddress();
		    Integer re;//余り
		    Integer quo;//商
		    re=ID/4;
		    quo=ID%4;
		    for(int i=0;i<re;i++){
				stopOnGray(RIGHT);
				//調整用
			}
	    turn(RIGHT,90);
	    for(int i=0;i<quo;i++){
			stopOnGray(LEFT);
			//調整用
		}
	    stopOnGray(RIGHT);
	    robotExists(RIGHT,45,50);
	    turn(RIGHT,90);
	    stopOnGray(RIGHT);
		changePos("RelayStation");
		turn(LEFT,180);
	}//receiverToRelayStation

	private static void waitingToRelayStation() {
		stopOnGray(LEFT);
		//lineTrace(300,RIGHT,300);   灰色ゾーンを通り過ぎるための調整用
		  robotExists(RIGHT,45,50);
		stopOnGray(RIGHT);
		changePos("RelayStation");
		turn(LEFT,180);
	}//waitingToRelayStation

	/**
	 * 中継所から受取人宅まで行く
	 */
	private static void relayStationToReceiver() {
		long start=0;
	    long end=0;
	    Integer ID=getLuggage().getRequestInformation().getReceiverAddress();
	    Integer re;//余り
	    Integer quo;//商
	    re=ID/4;
	    quo=ID%4;
	   robotExists(RIGHT,45,50);
	    start=System.currentTimeMillis();
		stopOnGray(LEFT);
		stopOnGray(LEFT);
		
	    for(int i=0;i<re;i++){
				stopOnGray(RIGHT);
				//goStraight(300,300);   灰色ゾーンを通り過ぎるための調整用
			}
	    turn(LEFT,90);
	    for(int i=0;i<quo;i++){
			stopOnGray(RIGHT);
			//goStraight(300,300);   灰色ゾーンを通り過ぎるための調整用
		}
	    end=System.currentTimeMillis();
	    elapsedTime=end-start;
	    changePos("Receiver");
		turn(LEFT,180);
	}

	public static boolean getLug() throws IOException{
	    EV3Signal sig=new EV3Signal();
	    if(sig.openSig("RelayStation")){   	
			sig.sendSig("workExist");
		String flag=sig.getSig().toString();
		if(flag.equals("true")){
		    setLuggage((Luggage) sig.getSig());//荷物多分大丈夫
		    sig.closeSig();
		    return true;
		}//iftrue
		else{
		    sig.closeSig();
		    return false;
		}//else
	    }//ifOK
	    return false;
	}//getLug

	public static void sendLug() throws IOException{
	    long start=0;
	    long end=0;
	    EV3Signal sig=new EV3Signal();
	    if(sig.openSig("Receiver")){
		sig.sendSig("isExist");
		
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
		    sig.sendSig(true);
		String receiverName=sig.getSig().toString();
		String receiverAddress=sig.getSig().toString();
		if(receiverName.equals(getLuggage().getRequestInformation().getReceiverName()) && receiverAddress.equals(getLuggage().getRequestInformation().getReceiverAddress().toString())){
		    changeDeliveryResult("FINISHED");
		    sig.sendSig(true);
		    sig.sendSig(getLuggage());
		}//ifreceiver

		else{
		    changeDeliveryResult("WRONG");
		    sig.sendSig(false);
		}//else

		}//ifendstart
		else  sig.sendSig(false);
		sig.closeSig();
	    }//ifopensig
	   
	}//sendLug

	public static void sendDeliveryRecord() throws IOException{
	    EV3Signal sig=new EV3Signal();
	    if(sig.openSig("RelayStation")){
		sig.sendSig(deliveryResult);
		if(deliveryResult.equals("FINISHED")){
		    sig.sendSig(elapsedTime);
		    sig.sendSig(getLuggage().getLuggageID());
		}//ifFINISHED
		else{
		    sig.sendSig(getLuggage());
		}
		sig.closeSig();
	    }//ifopen	
	}//sendDeliveryRecord

	
}