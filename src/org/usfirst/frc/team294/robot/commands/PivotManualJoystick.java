package org.usfirst.frc.team294.robot.commands;

import org.usfirst.frc.team294.robot.Robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class PivotManualJoystick extends Command {
	private GenericHID m_stick;
	
	public PivotManualJoystick(GenericHID stick) {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.pivot);
		m_stick = stick;
	}
	
	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.pivot.stop();
	}
	
	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
    	Robot.pivot.setManual(m_stick.getY());
	}
	
	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}
	
	// Called once after isFinished returns true
	protected void end() {
		Robot.pivot.stop();
	}
	
	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
