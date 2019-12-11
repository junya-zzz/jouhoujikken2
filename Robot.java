package robotSystem;

import lejos.hardware.lcd.LCD;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.SampleProvider;
import signal.Luggage;

public class Robot {

	private static Luggage luggage;

	private String position;

	private String color;

	private static EV3UltrasonicSensor sonicSensor;
	private static EV3GyroSensor gyroSensor;
	private static EV3LargeRegulatedMotor rightMotor;
	private static EV3LargeRegulatedMotor leftMotor;
	private static EV3ColorSensor colorSensor;
	
	/**
	 * �Z���T�[������������
	 */
	protected static void initSensors() {
		LCD.clear();
		LCD.drawString("init sensors...", 0, 0);
		LCD.refresh();
		colorSensor = new EV3ColorSensor(SensorPort.S1);
		//sonicSensor = new EV3UltrasonicSensor(SensorPort.S2);
		//gyroSensor = new EV3GyroSensor(SensorPort.S4);
		rightMotor = new EV3LargeRegulatedMotor(MotorPort.B);
		leftMotor = new EV3LargeRegulatedMotor(MotorPort.A);
		//sonicSensor.disable();
		LCD.drawString("init completed", 0, 1);
		LCD.refresh();
	}
	
	/**
	 * �Z���T�[�����
	 */
	protected static void closeSensors() {
		colorSensor.close();
		//sonicSensor.close();
		//gyroSensor.close();
		rightMotor.close();
		leftMotor.close();
	}

	/**
	 * ���̏�ŉ�]����
	 * @param direction ��]���� right �܂��� left
	 * @param degree ��]����p�x
	 */
	protected static void turn(String direction, Integer degree) {
		SampleProvider angleSampleProvider = gyroSensor.getAngleMode();
		float[] angleSample = new float[gyroSensor.sampleSize()];
		int speed = 100;
		rightMotor.setSpeed(speed);
		leftMotor.setSpeed(speed);
		gyroSensor.reset();
		angleSample[0] = 0;
		if (direction.equals("right")) {
			rightMotor.backward();
			leftMotor.forward();
		} else if (direction.contentEquals("left")){
			rightMotor.forward();
			leftMotor.backward();
		} else {
			return;
		}
		while(Math.abs(angleSample[0]) < degree) {
			angleSampleProvider.fetchSample(angleSample, 0);
		}
		rightMotor.stop(true);
		leftMotor.stop(true);
	}

	protected void changePos(String position) {

	}

	protected void goStraight() {

	}

	protected void desideColor() {

	}

	protected void setPower() {

	}

	protected void desideRefLight() {

	}

	/**
	 * ���̃��[�^�[�̉�]�����w�肵����]���𒴂���܂Ń��C���g���[�X����
	 * @param roll ��]��
	 */
	protected static void lineTrace(Integer roll) {
		SensorMode redLightSampleProvider = colorSensor.getRedMode();
		float sample[] = new float[redLightSampleProvider.sampleSize()];
		float target = 0.3f;
		float black = 0.03f;
		float white = 0.80f;
		float barance;
		int maxSpeed = 300;
		float diff;
		leftMotor.resetTachoCount();
		while(leftMotor.getTachoCount() < roll){
			redLightSampleProvider.fetchSample(sample, 0);
			LCD.clear();
			LCD.drawString("tacho " + leftMotor.getTachoCount(), 0, 0);
			LCD.drawString(sample[0]+"", 0, 1);
			LCD.refresh();
			diff = sample[0] - target;
			if(diff < 0){
				barance = diff / (black - target);
				leftMotor.setSpeed(maxSpeed);
				int bPower = (int)((1-barance) * maxSpeed);
				rightMotor.setSpeed(bPower);
			}else{
				barance = diff / (white - target);
				rightMotor.setSpeed(maxSpeed);
				int aPower = (int)((1-barance) * maxSpeed);
				leftMotor.setSpeed(aPower);
			}
			rightMotor.forward();
			leftMotor.forward();
		}
		rightMotor.stop(true);
		leftMotor.stop(true);
	}
	
	public static void stopOnGray() {
		SensorMode redLightSampleProvider = colorSensor.getRedMode();
		float sample[] = new float[redLightSampleProvider.sampleSize()];
		float threshold = 0.4f;
		float gray = 0.14f;
		int speed = 360;
		rightMotor.setSpeed(speed);
		leftMotor.setSpeed(speed);
		int count = 0;
		while(count < 40){
			redLightSampleProvider.fetchSample(sample, 0);
			if (sample[0] < threshold) {
				if (sample[0] > gray) {count ++;}
				else {count = 0;}
				leftMotor.forward();
				rightMotor.stop(true);
			} else {
				leftMotor.stop(true);
				rightMotor.forward();
			}
		}
		rightMotor.stop(true);
		leftMotor.stop(true);
	}
	
	/**
	 * ���̏�ŉ�]���A�����g�Z���T�[���g���ă��{�b�g�����͈̔͂ɂ��邩���ׂ�
	 * @param direction ��]�������
	 * @param degree ��]����p�x
	 * @param distance ���ׂ鋗��
	 * @return ���{�b�g������ꍇ:true ���Ȃ��ꍇ:false
	 */
	public static boolean robotExists(String direction, int degree, float distance) {
		SampleProvider distanceProvider = sonicSensor.getDistanceMode();
		SampleProvider angleProvider = gyroSensor.getAngleMode();
		float[] distanceSample = new float[distanceProvider.sampleSize()];
		float[] angleSample = new float[angleProvider.sampleSize()];
		int speed = 100;
		rightMotor.setSpeed(speed);
		leftMotor.setSpeed(speed);
		angleSample[0] = 0;
		boolean isRobotExisting = false;
		sonicSensor.enable();
		gyroSensor.reset();
		
		if (direction.equals("right")) {
			rightMotor.backward();
			leftMotor.forward();
		} else if (direction.equals("left")){
			rightMotor.forward();
			leftMotor.backward();
		} else {
			sonicSensor.disable();
			return false;
		}
		while(Math.abs(angleSample[0]) < degree) {
			angleProvider.fetchSample(angleSample, 0);
			distanceProvider.fetchSample(distanceSample, 0);
			LCD.clear();
			LCD.drawString("gyro = " + angleSample[0], 0, 0);
			LCD.drawString("sonic = " + distanceSample[0], 1, 0);
			LCD.refresh();
			if (distanceSample[0] < distance) {
				isRobotExisting = true;
			}
		}
		
		rightMotor.stop(true);
		leftMotor.stop(true);
		sonicSensor.disable();
		return isRobotExisting;
	}
	
	protected static Luggage getLuggage() {
		return luggage;
	}
	
	protected static void setLuggage(Luggage lug) {
		luggage = lug;
		return;
	}

}
