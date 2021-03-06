package org.usfirst.frc.team294.robot.util;

import edu.wpi.first.wpilibj.SpeedController;

public class TripleSpeedController implements SpeedController {

	private SpeedController[] controllers;
	private byte mySyncGroup;
	private int[] inverted;
	
	public TripleSpeedController(SpeedController c1, SpeedController c2, SpeedController c3, byte syncGroup) {
		controllers = new SpeedController[3];
		controllers[0] = c1;
		controllers[1] = c2;
		controllers[2] = c3;
		mySyncGroup = syncGroup;
		inverted = new int[3];
		inverted[0] = inverted[1] = inverted[2] = 1;
	}
	
	public void SetInverted(int motor, boolean isInverted) {
		inverted[motor] = isInverted ? -1 : 1;
	}

	@Override
	public void pidWrite(double output) {
		set(output);
	}

	@Override
	public double get() {
		return controllers[0].get() * inverted[0];
	}

	@Override
	public void set(double speed, byte syncGroup) {
		for (int i=0; i<3; ++i)
			controllers[i].set(speed * inverted[i], mySyncGroup);
	}

	@Override
	public void set(double speed) {
		for (int i=0; i<3; ++i)
			controllers[i].set(speed * inverted[i], mySyncGroup);
	}

	@Override
	public void disable() {
		for (int i=0; i<3; ++i)
			controllers[i].disable();
	}
}
