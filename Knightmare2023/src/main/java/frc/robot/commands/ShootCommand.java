// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.ShooterSubsystem.FeederDirection;

public class ShootCommand extends CommandBase {
  private final ShooterSubsystem m_shooterSubsystem;

  private final Timer m_timer = new Timer();

  private final double SHOOT_FWD_TIME_END = ShooterConstants.SHOOT_FWD_TIME;
  private final double SHOOT_TIME_END = SHOOT_FWD_TIME_END + ShooterConstants.SHOOT_REV_TIME;

  /** Creates a new ShootCommand. */
  public ShootCommand(ShooterSubsystem shooterSubsystem) {
    m_shooterSubsystem = shooterSubsystem;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_shooterSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (m_shooterSubsystem.isShooterEnabled() == false) {
      // shooter is not enabled - don't try to shoot
      DataLogManager.log("Can't shoot - shooter not enabled");
      cancel();
    } else {
      m_timer.restart();
      DataLogManager.log("Shoot!");
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_timer.get() < SHOOT_FWD_TIME_END) {
      m_shooterSubsystem.setFeederMotor(FeederDirection.forward);
    } else {
      m_shooterSubsystem.setFeederMotor(FeederDirection.reverse);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_shooterSubsystem.setFeederMotor(FeederDirection.stop);
    m_shooterSubsystem.setShooterEnabled(false);
    m_timer.reset();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (m_timer.get() >= SHOOT_TIME_END);
  }
}
