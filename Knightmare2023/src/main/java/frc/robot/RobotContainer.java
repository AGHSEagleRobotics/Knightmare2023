// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.DriveTrainConstants;
import frc.robot.Constants.OperatorConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.commands.DriveCommand;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...

  private final DriveTrainSubsystem m_driveTrainSubsystem = new DriveTrainSubsystem(
    new WPI_VictorSPX(DriveTrainConstants.CANID_frontLeft),
    new WPI_TalonSRX(DriveTrainConstants.CANID_backLeft),
    new WPI_VictorSPX(DriveTrainConstants.CANID_frontRight),
    new WPI_TalonSRX(DriveTrainConstants.CANID_backRight)
  );

  private final ShooterSubsystem m_shooterSubsystem = new ShooterSubsystem(
    new WPI_TalonFX(ShooterConstants.CANID_upperMotor),
    new WPI_TalonFX(ShooterConstants.CANID_lowerMotor));

  public final Dashboard m_dashboard = new Dashboard(m_shooterSubsystem);

  // Driver controllers
  private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    DriveCommand m_driveCommand = new DriveCommand(m_driveTrainSubsystem, () -> m_driverController.getLeftY(), () -> m_driverController.getRightX());
    m_driveTrainSubsystem.setDefaultCommand(m_driveCommand);
    // Configure the trigger bindings
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    m_driverController.a().onTrue(new InstantCommand(() -> m_shooterSubsystem.setShooterEnabled(true)));
    m_driverController.b().onTrue(new InstantCommand(() -> m_shooterSubsystem.setShooterEnabled(false)));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return null;
  }

  /** This function is called periodically by Robot.robotPeriodic. */
  public void periodic() {
    m_dashboard.periodic();
  }
  
}
