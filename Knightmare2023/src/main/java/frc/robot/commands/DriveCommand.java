// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrainSubsystem;

public class DriveCommand extends CommandBase {

  private final DriveTrainSubsystem m_driveTrainSubsystem;

  private final Supplier<Double> m_leftStickYAxis;
  private final Supplier<Double> m_rightStickXAxis;

  /** Creates a new DriveCommand. */
  public DriveCommand(DriveTrainSubsystem driveTrainSubsystem, Supplier<Double> leftY, Supplier<Double> rightX) {
    m_driveTrainSubsystem = driveTrainSubsystem;

    m_leftStickYAxis = leftY;
    m_rightStickXAxis = rightX;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_driveTrainSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_driveTrainSubsystem.drive(m_leftStickYAxis.get(), m_rightStickXAxis.get());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
