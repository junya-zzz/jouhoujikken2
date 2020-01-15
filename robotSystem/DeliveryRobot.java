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
import lejos.utility.Delay;
import recordSystem.Luggage;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.SensorMode;
import signal.*;
import recordSystem.*;
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
		try{
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
		}catch(InterruptedException e){
			System.out.println("error.");
		}
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
		    Integer ID = lug.getRequestInformation().getReceiverAddress();
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
	    Integer ID=lug.getRequestInformation().getReceiverAddress();
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
		//Robot rob=new Robot();
	    EV3Signal sig=new EV3Signal();
	    try{
	    	if(sig.openSig(Port.RELAY)){
	    		sig.sendSig("Is the luggageList empty?");
	    		Object flag=sig.getSig();
	    		if(!flag.equals("true")){
	    		    //Luggage lug=(Luggage)sig.getSig();//荷物多分大丈夫
	    			Luggage lug = (Luggage)flag;
	    		    setLuggage(lug);
	    		    LCD.clear();
					LCD.drawString("received.", 0, 2);
					Delay.msDelay(5000);
					LCD.refresh();
	    		    sig.closeSig();
	    		    return true;
	    		}//iftrue
	    		else{
	    			//sig.getSig();
	    			LCD.clear();
					LCD.drawString("Couldn't receive.", 0, 2);
					Delay.msDelay(5000);
					LCD.refresh();
	    		    sig.closeSig();
	    		    return false;
	    		}//else
	    	    }//ifOK
	    }catch(Exception e){
	    	System.out.println("error.");
	    }
	    return false;
	}//getLug

	public static void sendLug() {
	    long start=0;
	    long end=0;
	    EV3Signal sig=new EV3Signal();
        //Robot rob=new Robot();
        try{
        	if(sig.openSig(Port.RECEIVE)){
        		sig.sendSig("isExist");
        		start=System.currentTimeMillis();
        		while(end-start<10 && !(sig.getSig().toString()).equals("Exist")){
        		    end=System.currentTimeMillis();
        		    try{
        			Thread.sleep(1000);
        		    }catch (InterruptedException e){
        			e.printStackTrace();
        		    }
        		}//while

        		if(end-start<10){
        		    sig.sendSig("true");
        		String receiverName=sig.getSig().toString();
        		String receiverAddress=sig.getSig().toString();
                Luggage lug=getLuggage();
        		if(receiverName.equals(lug.getRequestInformation().getReceiverName()) && receiverAddress.equals(String.valueOf(lug.getRequestInformation().getReceiverAddress()))){
        		    changeDeliveryResult("FINISHED");
        		    LCD.clear();
    				LCD.drawString("FINISHED", 0, 2);
    				Delay.msDelay(5000);
    				LCD.refresh();
        		    sig.sendSig("true");
        		    sig.sendSig(lug);
        		}//ifreceiver

        		else{
        		    changeDeliveryResult("WRONG");
        		    sig.sendSig("false");
        		}//else

        		}//ifendstart
        		else  sig.sendSig("false");
        		sig.closeSig();
        	    }//ifopensig
        }catch(Exception e){
        	System.out.println("error.");
        }   
	}//sendLug

	public static void sendDeliveryRecord() {
		//Robot rob=new Robot();
	    EV3Signal sig=new EV3Signal();
	    Luggage lug=getLuggage();
	    try{
	    	if(sig.openSig(Port.RELAY)){
	    		//changeDeliveryResult("wrongAddress");
	    		//elapsedTime = 1200;
	    		sig.sendSig(deliveryResult);
	    		if(deliveryResult.equals("finished")){
	    		    sig.sendSig(elapsedTime);
	    		    sig.sendSig(lug.getLuggageID());
	    		}//ifFINISHED
	    		else{
	    			sig.sendSig(lug);
	    		}
	    		sig.closeSig();
	    	    }//ifopen
	    }catch(Exception e){
	    	System.out.println("error.");
	    }	
	}//sendDeliveryRecord

	
}