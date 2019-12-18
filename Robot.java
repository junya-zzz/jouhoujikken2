package robotSystem;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.SampleProvider;
import lejos.utility.Stopwatch;
import signal.Luggage;

public class Robot {

	private static Luggage luggage;

	private String position;

	private static EV3UltrasonicSensor sonicSensor;
	private static EV3GyroSensor gyroSensor;
	private static EV3LargeRegulatedMotor rightMotor;
	private static EV3LargeRegulatedMotor leftMotor;
	private static EV3ColorSensor colorSensor;
	
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	
	/**
	 * センサーを初期化する
	 */
	protected static void initSensors() {
		LCD.clear();
		LCD.drawString("init sensors...", 0, 0);
		LCD.refresh();
		colorSensor = new EV3ColorSensor(SensorPort.S1);
		sonicSensor = new EV3UltrasonicSensor(SensorPort.S2);
		gyroSensor = new EV3GyroSensor(SensorPort.S4);
		rightMotor = new EV3LargeRegulatedMotor(MotorPort.B);
		leftMotor = new EV3LargeRegulatedMotor(MotorPort.A);
		sonicSensor.disable();
		LCD.drawString("init completed", 0, 1);
		LCD.refresh();
	}
	
	/**
	 * センサーを閉じる
	 */
	protected static void closeSensors() {
		colorSensor.close();
		sonicSensor.close();
		gyroSensor.close();
		rightMotor.close();
		leftMotor.close();
	}

	/**
	 * その場で回転する
	 * @param direction 回転方向 Robot.RIGHT または Robot.LEFT
	 * @param degree 回転する角度
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
	 * 左のモーターの回転数が指定した回転数を超えるまでライントレースする
	 * @param roll 回転数
	 * @param side ラインの右側と左側どちらを走るか指定する Robot.RIGHT または Robot.LEFT
	 * @param speed 300くらいがちょうどいい
	 */
	protected static void lineTrace(Integer roll, int side, int speed) {
		SensorMode redLightSampleProvider = colorSensor.getRedMode();
		float sample[] = new float[redLightSampleProvider.sampleSize()];
		float target = 0.3f;
		float black = 0.03f;
		float white = 0.80f;
		float barance;
		int power;
		float diff;
		leftMotor.resetTachoCount();
		
		if (side == RIGHT) {
			while(leftMotor.getTachoCount() < roll){
				redLightSampleProvider.fetchSample(sample, 0);
				diff = sample[0] - target;
					if(diff < 0){
						barance = diff / (black - target);
						leftMotor.setSpeed(speed);
						power = (int)((1-barance) * speed);
						rightMotor.setSpeed(power);
					}else{
						barance = diff / (white - target);
						rightMotor.setSpeed(speed);
						power = (int)((1-barance) * speed);
						leftMotor.setSpeed(power);
					}
					rightMotor.forward();
					leftMotor.forward();
					LCD.clear();
					LCD.drawInt(leftMotor.getTachoCount(), 0, 0);
					LCD.refresh();
			}
		}else {
			while(leftMotor.getTachoCount() < roll){
				redLightSampleProvider.fetchSample(sample, 0);
				diff = sample[0] - target;
				if(diff < 0){
					barance = diff / (black - target);
					rightMotor.setSpeed(speed);
					power = (int)((1-barance) * speed);
					leftMotor.setSpeed(power);
				}else{
					barance = diff / (white - target);
					leftMotor.setSpeed(speed);
					power = (int)((1-barance) * speed);
					rightMotor.setSpeed(power);
				}
				leftMotor.forward();
				rightMotor.forward();
				LCD.clear();
				LCD.drawInt(leftMotor.getTachoCount(), 0, 0);
				LCD.refresh();
			}
		}
		rightMotor.stop(true);
		leftMotor.stop(true);
	}

	/**
	 * 灰色を検知するまでライントレースする
	 * @param side Robot.LEFT または Robot.RIGHT
	 */
	public static void stopOnGray(int side) {
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.reset();
		SensorMode redLightSampleProvider = colorSensor.getRedMode();
		float sample[] = new float[redLightSampleProvider.sampleSize()];
		float threshold = 0.4f;
		float gray = 0.08f;
		int speed = 250;
		rightMotor.setSpeed(speed);
		leftMotor.setSpeed(speed);
		int count = 0;
		boolean isOnLine=false;
		float lowestValue = 1.0f;
		if (side == RIGHT) {
			while(Button.ENTER.isUp() && count < 1){
				redLightSampleProvider.fetchSample(sample, 0);
				if (sample[0] < threshold) {
					if (sample[0] < lowestValue) {
						lowestValue = sample[0];
					}
					leftMotor.forward();
					rightMotor.stop(true);
					isOnLine = true;
				} else {
					leftMotor.stop(true);
					rightMotor.forward();
					if (isOnLine) {
						if (lowestValue > gray && stopwatch.elapsed() > 1000){
							count++;
						} else {
							count = 0;
						}
						lowestValue = 1.0f;
						isOnLine = false;
					}
				}
				LCD.clear();
				LCD.drawString(count+"", 0, 0);
				LCD.refresh();
			}
		} else if (side == LEFT){
			while(Button.ENTER.isUp() && count < 1){
				redLightSampleProvider.fetchSample(sample, 0);
				if (sample[0] < threshold) {
					if (sample[0] < lowestValue) {
						lowestValue = sample[0];
					}
					rightMotor.forward();
					leftMotor.stop(true);
					isOnLine = true;
				} else {
					rightMotor.stop(true);
					leftMotor.forward();
					if (isOnLine) {
						if (lowestValue > gray && stopwatch.elapsed() > 1000){
							count++;
						} else {
							count = 0;
						}
						lowestValue = 1.0f;
						isOnLine = false;
					}
				}
				LCD.clear();
				LCD.drawString(count+"", 0, 0);
				LCD.refresh();
			}
		}
		rightMotor.stop(true);
		leftMotor.stop(true);
	}
	
	/**
	 * その場で回転し、超音波センサーを使ってロボットがその範囲にいるか調べる
	 * @param direction 回転する方向 Robot.RIGHT または Robot.LEFT
	 * @param degree 回転する角度
	 * @param distance 調べる距離
	 * @return ロボットがいる場合:true いない場合:false
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
	
	/**
	 * positionを変更する
	 * @param position
	 */
	protected void changePos(String position) {
		this.position = position;
	}

	/**
	 * 直進する
	 * @param speed 速度
	 * @param roll モーターの目標回転数
	 */
	protected void goStraight(int speed, int roll) {
		leftMotor.setSpeed(speed);
		rightMotor.setSpeed(speed);
		leftMotor.forward();
		rightMotor.forward();
		leftMotor.resetTachoCount();
		while (leftMotor.getTachoCount() < roll);
		leftMotor.stop(true);
		rightMotor.stop(true);
	}

	protected static Luggage getLuggage() {
		return luggage;
	}
	
	protected static void setLuggage(Luggage lug) {
		luggage = lug;
		return;
	}
}
