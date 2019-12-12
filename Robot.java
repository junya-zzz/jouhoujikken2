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
	
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	
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
	 * @param direction ��]���� Robot.RIGHT �܂��� Robot.LEFT
	 * @param degree ��]����p�x
	 */
	protected static void turn(int direction, Integer degree) {
		SampleProvider angleSampleProvider = gyroSensor.getAngleMode();
		float[] angleSample = new float[gyroSensor.sampleSize()];
		int speed = 100;
		rightMotor.setSpeed(speed);
		leftMotor.setSpeed(speed);
		gyroSensor.reset();
		angleSample[0] = 0;
		if (direction == RIGHT) {
			rightMotor.backward();
			leftMotor.forward();
		} else if (direction == LEFT){
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

	/**
	 * ���̃��[�^�[�̉�]�����w�肵����]���𒴂���܂Ń��C���g���[�X����
	 * @param roll ��]��
	 * @param side ���C���̉E���ƍ����ǂ���𑖂邩�w�肷�� Robot.RIGHT �܂��� Robot.LEFT
	 */
	protected static void lineTrace(Integer roll, int side) {
		SensorMode redLightSampleProvider = colorSensor.getRedMode();
		float sample[] = new float[redLightSampleProvider.sampleSize()];
		float target = 0.3f;
		float black = 0.03f;
		float white = 0.80f;
		float barance;
		int maxSpeed = 300;
		int power;
		float diff;
		leftMotor.resetTachoCount();
		
		// �����𑖂肽���ꍇ�͉E�̃��[�^�[�ƍ��̃��[�^�[�����ւ���
		if (side == LEFT) {
			EV3LargeRegulatedMotor swapMotor = leftMotor;
			leftMotor = rightMotor;
			rightMotor = swapMotor;
		}
		
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
				power = (int)((1-barance) * maxSpeed);
				rightMotor.setSpeed(power);
			}else{
				barance = diff / (white - target);
				rightMotor.setSpeed(maxSpeed);
				power = (int)((1-barance) * maxSpeed);
				leftMotor.setSpeed(power);
			}
			rightMotor.forward();
			leftMotor.forward();
		}
		rightMotor.stop(true);
		leftMotor.stop(true);
		
		// ����ւ������[�^�[�����Ƃɖ߂�
		if (side == LEFT) {
			EV3LargeRegulatedMotor swapMotor = leftMotor;
			leftMotor = rightMotor;
			rightMotor = swapMotor;
		}
	}

	public static void stopOnGray() {
		SensorMode redLightSampleProvider = colorSensor.getRedMode();
		float sample[] = new float[redLightSampleProvider.sampleSize()];
		float threshold = 0.4f;
		float gray = 0.14f;
		int speed = 160;
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
	 * @param direction ��]������� Robot.RIGHT �܂��� Robot.LEFT
	 * @param degree ��]����p�x
	 * @param distance ���ׂ鋗��
	 * @return ���{�b�g������ꍇ:true ���Ȃ��ꍇ:false
	 */
	public static boolean robotExists(int direction, int degree, float distance) {
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
		
		if (direction == RIGHT) {
			rightMotor.backward();
			leftMotor.forward();
		} else if (direction == LEFT){
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

	
	
	protected static Luggage getLuggage() {
		return luggage;
	}
	
	protected static void setLuggage(Luggage lug) {
		luggage = lug;
		return;
	}

}
