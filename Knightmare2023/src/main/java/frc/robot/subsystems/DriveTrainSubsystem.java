// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DriveTrainSubsystem extends SubsystemBase {
  private final WPI_VictorSPX frontLeft = new WPI_VictorSPX(Constants.frontLeft);
  private final WPI_TalonSRX backLeft = new WPI_TalonSRX(Constants.backLeft);
  private final WPI_TalonSRX frontRight = new WPI_TalonSRX(Constants.frontRight);
  private final WPI_VictorSPX backRight = new WPI_VictorSPX(Constants.backRight);
 
  private final WPI_VictorSPX m_frontLeft;
  private final WPI_TalonSRX m_backLeft;
  private final WPI_TalonSRX m_frontRight;
  private final WPI_VictorSPX m_backRight;

  
  
  private final DifferentialDrive m_differentialDrive;

  /** Creates a new DriveTrainSubsystem. */
  public DriveTrainSubsystem(WPI_VictorSPX frontLeft, WPI_TalonSRX backLeft, WPI_TalonSRX frontRight,   WPI_VictorSPX backRight) {
   
    m_frontLeft = frontLeft;
    m_backLeft = backLeft;
    m_frontRight = frontRight;
    m_backRight = backRight;

    m_frontRight.setInverted(true);
    m_backRight.setInverted(true);

    m_frontRight.setNeutralMode(NeutralMode.Brake);
    m_frontLeft.setNeutralMode(NeutralMode.Brake);
    m_backLeft.setNeutralMode(NeutralMode.Brake);
    m_backRight.setNeutralMode(NeutralMode.Brake);

    m_backRight.follow(m_frontRight);
    m_backLeft.follow(m_frontLeft);

    m_differentialDrive = new DifferentialDrive(m_frontLeft, m_frontRight);
  }

  public void drive(double speed, double turn) {
    m_differentialDrive.arcadeDrive(speed, turn);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
