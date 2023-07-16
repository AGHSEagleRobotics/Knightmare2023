// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.AimSubsystem;

public class UpdateAngleCommand extends CommandBase {
  public enum Direction { increase, decrease };

  private final Direction m_direction;
  private final AimSubsystem m_AimSubsystem;

  private int runCount = 0;
  

  /** Creates a new IncreaseAngleCommand. */
  public UpdateAngleCommand(Direction direction, AimSubsystem aimSubsystem) {
    m_AimSubsystem = aimSubsystem;
    m_direction = direction;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_AimSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    runCount = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    runCount++;

    if (m_AimSubsystem.isAutoEnabled()) {
      // Increment/decrement once when the command starts,
      // then continuously after the specified delay
      if ((runCount == 1) || (runCount > ShooterConstants.INCREMENT_DELAY)) {
        if (m_direction == Direction.increase) {
          m_AimSubsystem.increaseTargetAngle();
        } else {
          m_AimSubsystem.decreaseTargetAngle();
        }
      }
    } else {
      // manual mode
      if (m_direction == Direction.increase) {
        m_AimSubsystem.setAimMotor(ShooterConstants.AIM_UP);
      } else {
        m_AimSubsystem.setAimMotor(ShooterConstants.AIM_DOWN);
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if (m_AimSubsystem.isAutoEnabled()) {
      // store the target angle in persistent memory
      m_AimSubsystem.saveTargetAngle();
    } else {
      // manual mode
      m_AimSubsystem.setAimMotor(ShooterConstants.AIM_STOP);
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
