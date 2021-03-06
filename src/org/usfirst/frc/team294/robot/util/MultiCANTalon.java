package org.usfirst.frc.team294.robot.util;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.SpeedController;

public class MultiCANTalon implements SpeedController {

	private CANTalon[] controllers;
	
	public MultiCANTalon(int[] motors) {
		controllers = new CANTalon[motors.length];
		for (int i=0; i < motors.length; i++)
			controllers[i] = new CANTalon(motors[i]);
		for (int i=1; i < motors.length; i++) {
			controllers[i].changeControlMode(ControlMode.Follower);
		}
	}
	
	public void SetInverted(int motor, boolean isInverted) {
		assert(motor != 0);
		controllers[motor].reverseOutput(isInverted);
	}

	@Override
	public void pidWrite(double output) {
		set(output);
	}

	@Override
	public double get() {
		return controllers[0].get();
	}

	@Override
	public void set(double speed, byte syncGroup) {
		controllers[0].set(speed);
		for (int i=1; i < controllers.length; i++) {
			controllers[i].set(controllers[0].getDeviceID());
		}
	}

	@Override
	public void set(double speed) {
		controllers[0].set(speed);
		for (int i=1; i < controllers.length; i++) {
			controllers[i].set(controllers[0].getDeviceID());
		}
	}

	@Override
	public void disable() {
		controllers[0].disable();
	}
}
