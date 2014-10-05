
package org.usfirst.frc.team294.robot.subsystems;

import org.usfirst.frc.team294.robot.Robot;
import org.usfirst.frc.team294.robot.RobotMap;
import org.usfirst.frc.team294.robot.commands.DriveWithJoysticks;
import org.usfirst.frc.team294.robot.util.LinearVictor884;
import org.usfirst.frc.team294.robot.util.RateLimitFilter;
import org.usfirst.frc.team294.robot.util.TripleSpeedController;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Drivetrain extends Subsystem {

	// Left Drive
	SpeedController leftMotor1 = new LinearVictor884(RobotMap.kPWM_leftMotor1);
	SpeedController leftMotor2 = new LinearVictor884(RobotMap.kPWM_leftMotor2);
	SpeedController leftMotor3 = new LinearVictor884(RobotMap.kPWM_leftMotor3);
	SpeedController leftMotor = new TripleSpeedController(leftMotor1, leftMotor2, leftMotor3, (byte) 0);
	
	// Right Drive
	SpeedController rightMotor1 = new LinearVictor884(RobotMap.kPWM_rightMotor1);
	SpeedController rightMotor2 = new LinearVictor884(RobotMap.kPWM_rightMotor2);
	SpeedController rightMotor3 = new LinearVictor884(RobotMap.kPWM_rightMotor3);
	SpeedController rightMotor = new TripleSpeedController(rightMotor1, rightMotor2, rightMotor3, (byte) 0);

	RobotDrive drive = new RobotDrive(leftMotor, rightMotor);

	Encoder rightDriveEncoder = new Encoder(RobotMap.kDIN_rightDriveEncoderA, RobotMap.kDIN_rightDriveEncoderB);
	Encoder leftDriveEncoder = new Encoder(RobotMap.kDIN_leftDriveEncoderA, RobotMap.kDIN_leftDriveEncoderB);

	RateLimitFilter leftFilter = new RateLimitFilter(6.0);
	RateLimitFilter rightFilter = new RateLimitFilter(6.0);
	
	Timer lowBatteryTimer = new Timer();
	Timer lowBatteryScaleTimer = new Timer();
    double lowBatteryScale = 1.0;

	public Drivetrain() {
		lowBatteryTimer.start();
		lowBatteryScaleTimer.start();
	}

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new DriveWithJoysticks());
	}
	
	public void stop() {
		drive.tankDrive(0, 0);
		lowBatteryScale = 1.0;
	}
	
	public void resetEncoders() {
		leftDriveEncoder.reset();
		rightDriveEncoder.reset();
	}
	
	public void autoDrive(double speed) {
		double leftValue = leftDriveEncoder.get();
		double rightValue = rightDriveEncoder.get();
		
		double leftSpeed = speed;
		double rightSpeed = speed;
		
		if (leftValue > rightValue) {
			leftSpeed -= (leftValue-rightValue)/100.0;
			if (speed >= 0 && leftSpeed < 0)
				leftSpeed = 0.0;
		} else if (leftValue < rightValue) {
			rightSpeed -= (rightValue-leftValue)/100.0;
			if (speed >= 0 && rightSpeed < 0)
				rightSpeed = 0.0;
		}
		
		//logging.info("lenc: %s renc: %s lout: %s rout: %s", leftValue, rightValue, leftSpeed, rightSpeed)
		tankDrive(leftSpeed, rightSpeed);
	}
	
	public void tankDrive(double lPower, double rPower) {
		double l = leftFilter.update(lPower);
		double r = rightFilter.update(rPower);
		// monitor battery voltage; if less than 6V for X time, reduce
		// drivetrain by Y factor for a period of time
		if (Robot.pdp.getVoltage() > 6.5)
			lowBatteryTimer.reset();
		if (lowBatteryTimer.get() > 0.1) {
			lowBatteryScaleTimer.reset();
			lowBatteryScale = 0.25;
		}
		if (lowBatteryScaleTimer.get() > 0.5)
			lowBatteryScale = 1.0;
		//logging.info("l: %s r: %s lbs: %s", l, r, lowBatteryScale)
		drive.tankDrive(l*lowBatteryScale, r*lowBatteryScale);
	}
}

