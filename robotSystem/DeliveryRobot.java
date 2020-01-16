package robotSystem;
import recordSystem.*;
import relaySystem.RelayStation;
import signal.*;
import java.util.Date;
import java.io.IOException;
import java.util.Date;

import javax.swing.text.DefaultEditorKit.BeepAction;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.SampleProvider;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;
public class DeliveryRobot extends Robot {

	/**
	 * �ו��̔z�B���ʂ�ێ�����ϐ�
	 */
	private static LuggageCondition deliveryResult;

	/**
	 * �󂯎�莞��
	 */
	private static long elapsedTime;   //���XDate�^����������long�^������
	//�󂯎�莞�Ԃ𓾂邽�߂ɒ��p��������l��܂ł̎��Ԃ��v��A�z�B�J�n���Ԃɑ����āA�󂯎�莞�Ԃ����


	private final static int ID=10;


	public static void main(String[] args) throws IOException{
		//initSensors();

		//while(true){   
			//try {
				//Delay.msDelay(1000);
				//waitingToRelayStation();
				if(getLug()){
					//relayStationToReceiver();
					sendLug();
					//receiverToRelayStation();
					sendDeliveryRecord();
				}//if
				//relayStationToWaiting();
			//} catch(IOException e){
				//e.printStackTrace();
			//}      
		//}//while(1) 

	}//main


	private static void changeDeliveryResult(LuggageCondition result) {
		deliveryResult=result;
	}

	/**
	 * ���p������ҋ@���܂ōs��
	 */
	private static void relayStationToWaiting() throws IOException{
		robotExists(RIGHT,45,50);
		turn(LEFT,45);
		lineTrace(1300,LEFT,200); 
		turn(LEFT,40);
		lineTrace(2100,RIGHT,200); 
		stopOnGray(LEFT);  
		changePos("Waiting");
		turn(LEFT,180);
	}//relayStationToWaiting

	/**
	 * ���l��璆�p���܂ōs��
	 */
	private static void receiverToRelayStation(){

		//Integer ID=getLuggage().getRequestInformation().getReceiverAddress();
		Integer quo;//��
		Integer re;//�]��
		quo=ID/4;
		re=ID%4;
		//���l��]�[��
		if(quo==0){
			for(int i=0;i<re;i++){
				lineTrace(500,RIGHT,150); 
				stopOnGray(RIGHT);		
			}//for
			turn(RIGHT,90);
		}//if

		else if(quo!=0 && re!=2 &&re!=3){

			if(re==1){

				lineTrace(300,RIGHT,150);
				stopOnGray(RIGHT);	
				turn(RIGHT,30);
			}//if re1

			for(int i=0;i<quo;i++){
				lineTrace(700,LEFT,200); 
				stopOnGray(LEFT);	
			}//for

		}//elseif

		else{
			if(re==2){	 
				lineTrace(300,LEFT,150);
				stopOnGray(LEFT);
				turn(LEFT,30);
			}//if

			for(int i=0;i<quo;i++){
				lineTrace(700,LEFT,200); 
				stopOnGray(LEFT);	
			}//for	

			for(int i=0;i<4;i++){
				lineTrace(700,RIGHT,200); 
				stopOnGray(RIGHT);	
			}//for

			turn(RIGHT,10);

		}//else
		//���l��]�[���ȍ~	 
		lineTrace(3300,RIGHT,200); 
		stopOnGray(RIGHT);
		turn(RIGHT,40);
		lineTrace(750,RIGHT,150);
		turn(RIGHT,40);
		lineTrace(700,RIGHT,150);
		stopOnGray(RIGHT);



		changePos("RelayStation");
		turn(LEFT,180);
	}//receiverToRelayStation

	/**
	 * �ҋ@�����璆�p���ɍs��
	 */
	private static void waitingToRelayStation() throws IOException{
		lineTrace(2300,LEFT,300); 
		robotExists(RIGHT,45,50);
		turn(LEFT,45);
		turn(RIGHT,45);
		lineTrace(900,RIGHT,200);
		stopOnGray(LEFT);  

		changePos("RelayStation");
		turn(LEFT,180);
	}//waitingToRelayStation

	/**
	 * ���p��������l��܂ōs��
	 */
	private static void relayStationToReceiver() throws IOException{
		long start=0;
		long end=0;
		//Integer ID=getLuggage().getRequestInformation().getReceiverAddress();
		Integer quo;//��
		Integer re;//�]��
		quo=ID/4;
		re=ID%4;
		robotExists(RIGHT,45,50);
		turn(LEFT,45);
		//���l��]�[���ȑO
		start=System.currentTimeMillis();
		lineTrace(1400,LEFT,200); 
		stopOnGray(LEFT);  
		turn(LEFT,90);
		lineTrace(3000,LEFT,300); 
		stopOnGray(RIGHT); 
		//���l��]�[��
		if(quo==0){
			turn(LEFT,90);
			for(int i=0;i<re;i++){
				lineTrace(700,LEFT,200); 
				stopOnGray(LEFT);		
			}
		}//if
		else if(quo!=0 && re!=2 && re!=3){
			for(int i=0;i<quo;i++){
				lineTrace(700,RIGHT,200); 
				stopOnGray(RIGHT);	
			}//for
			if(re==1){
				turn(LEFT,40);
				lineTrace(500,LEFT,150);
				stopOnGray(LEFT);	
			}//if re1

		}//elseif

		else{
			turn(LEFT,90);
			for(int i=0;i<4;i++){
				lineTrace(700,LEFT,150); 
				stopOnGray(LEFT);	
			}//for

			turn(RIGHT,40);
			for(int i=0;i<quo;i++){
				lineTrace(700,RIGHT,200); 
				stopOnGray(RIGHT);	
			}//for	
			if(re==2){
				turn(RIGHT,40);
				lineTrace(500,RIGHT,150);
				stopOnGray(RIGHT);	
			}//if re2
		}//else

		end=System.currentTimeMillis();
		elapsedTime=end-start;
		changePos("Receiver");
		turn(LEFT,180);   

		LCD.drawString(String.valueOf(elapsedTime), 0, 2);//
	}

	/**
	 * ���p������ו����󂯎��
	 */
	public static boolean getLug(){
		EV3Signal sig=new EV3Signal();
		try{
			if(sig.openSig(Port.RELAY)){
				sig.sendSig(RelayStation.SEND_LUG_TO_DELIVERY);
				sig.sendSig("Is the luggageList empty?");
				Object flag=sig.getSig();
				if(!flag.equals("true")){
					//Luggage lug=(Luggage)sig.getSig();//�ו��������v
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

	/**
	 * ���l��ɉו��𑗂�
	 */
	public static void sendLug(){
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
					String receiverName=(String)sig.getSig();
					int receiverAddress=(int)sig.getSig();
					Luggage lug=getLuggage();
					if(receiverName.equals(lug.getRequestInformation().getReceiverName()) && receiverAddress == lug.getRequestInformation().getReceiverAddress()){
						changeDeliveryResult(LuggageCondition.finished);
						LCD.clear();
						LCD.drawString("FINISHED", 0, 2);
						Delay.msDelay(5000);
						LCD.refresh();
						sig.sendSig("true");
						sig.sendSig(lug);
					}//ifreceiver

					else{
						changeDeliveryResult(LuggageCondition.wrongAddress);
						sig.sendSig("false");
					}//else

				}//ifendstart
				else  sig.sendSig("false");
				sig.closeSig();
			} else {
				changeDeliveryResult(LuggageCondition.receive_absence);
			}
		}catch(Exception e){
			System.out.println("error.");
		}   
	}//sendLug

	/**
	 * ���p���ɔz�B���ʂ�񍐂���
	 * @throws IOException 
	 */
	public static void sendDeliveryRecord() throws IOException{
		EV3Signal sig=new EV3Signal();
		Luggage lug=getLuggage();
		//try{
			if(sig.openSig(Port.RELAY)){
				sig.sendSig(RelayStation.REPORT_DELIVERY_RESULT);
				//changeDeliveryResult("wrongAddress");
				elapsedTime = 1200;
				sig.sendSig(deliveryResult);
				if(deliveryResult == LuggageCondition.finished){
					sig.sendSig(elapsedTime);
					sig.sendSig(lug.getLuggageID());
				}//ifFINISHED
				else{
					sig.sendSig(lug);
				}
				sig.closeSig();
			}//ifopen
		//}catch(Exception e){
			System.out.println("error.");
		//}	


	}//sendDeliveryRecord


}