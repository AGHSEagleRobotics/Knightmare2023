// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;

public class ShooterSubsystem extends SubsystemBase {

  private final WPI_TalonFX m_upperMotor;
  private final WPI_TalonFX m_lowerMotor;

  private Timer m_shooterCooldownTimer;   //< Allow the shooter to run for a bit after disabling

  private boolean m_shooterEnabled = false;
  private double m_upperTargetRPM = 1000;   // TEMPORARY
  private double m_lowerTargetRPM = 1200;   // TEMPORARY

  // Math variables needed to convert RPM to ticks per second/ticks per
  private final int SENSOR_CYCLES_PER_SECOND = 10;   // sensor velocity period is 100 ms
  private final int SEC_PER_MIN = 60;
  private final int COUNTS_PER_REV = 2048;

  /** Creates a new ShooterSubsystem. */
  public ShooterSubsystem(WPI_TalonFX upperMotor, WPI_TalonFX lowerMotor)  {
    m_upperMotor = upperMotor;
    m_lowerMotor = lowerMotor;

    // Configure upper motor
    m_upperMotor.configFactoryDefault();
    m_upperMotor.setNeutralMode(NeutralMode.Coast);
    m_upperMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    m_upperMotor.setSensorPhase(false);
    m_upperMotor.setInverted(false);

    m_upperMotor.config_kF(ShooterConstants.PID_IDX, ShooterConstants.GAINS_VELOCITY_F);
    m_upperMotor.config_kP(ShooterConstants.PID_IDX, ShooterConstants.GAINS_VELOCITY_P);
    m_upperMotor.config_kI(ShooterConstants.PID_IDX, ShooterConstants.GAINS_VELOCITY_I);
    m_upperMotor.config_kD(ShooterConstants.PID_IDX, ShooterConstants.GAINS_VELOCITY_D);

    // Configure lower motor
    m_lowerMotor.configFactoryDefault();
    m_lowerMotor.setNeutralMode(NeutralMode.Coast);
    m_lowerMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    m_lowerMotor.setSensorPhase(true);
    m_lowerMotor.setInverted(true);

    m_lowerMotor.config_kF(ShooterConstants.PID_IDX, ShooterConstants.GAINS_VELOCITY_F);
    m_lowerMotor.config_kP(ShooterConstants.PID_IDX, ShooterConstants.GAINS_VELOCITY_P);
    m_lowerMotor.config_kI(ShooterConstants.PID_IDX, ShooterConstants.GAINS_VELOCITY_I);
    m_lowerMotor.config_kD(ShooterConstants.PID_IDX, ShooterConstants.GAINS_VELOCITY_D);

    m_shooterCooldownTimer = new Timer();
  }

  public void setShooterEnabled (boolean enable) { 
    if (enable) {
      m_shooterEnabled = true;
      DataLogManager.log("enabling shooter");
    } else { // if false
      m_shooterCooldownTimer.start(); // start cooldown timer
    }
  }
  
  public void setUpperTargetRPM (double rpm) {
    m_upperTargetRPM = rpm;
  }
  public void setLowerTargetRPM (double rpm) {
    m_lowerTargetRPM = rpm;
  }

  public double getUpperTargetRPM () {
    return m_upperTargetRPM;
  }
  public double getLowerTargetRPM () {
    return m_lowerTargetRPM;
  }

  public double getUpperRPM () {
    double rawSensorData = m_upperMotor.getSelectedSensorVelocity();
    double motorRPM = rawSensorData * SENSOR_CYCLES_PER_SECOND * SEC_PER_MIN / COUNTS_PER_REV;
// if (m_shooterEnabled) return 2500.0;  // DEBUG
// else return 0;  // DEBUG
    return motorRPM;
  }
  public double getLowerRPM () {
    double rawSensorData = m_lowerMotor.getSelectedSensorVelocity();
    double motorRPM = rawSensorData * SENSOR_CYCLES_PER_SECOND * SEC_PER_MIN / COUNTS_PER_REV;
// if (m_shooterEnabled) return 3000.0;  // DEBUG
// else return 0;  // DEBUG
    return motorRPM;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    
    if (m_shooterCooldownTimer.hasElapsed(ShooterConstants.SHOOTER_COOLDOWN_TIME))  {
      // don't turn off shooter until some time has elapsed
      m_shooterEnabled = false;
      m_shooterCooldownTimer.stop();
      m_shooterCooldownTimer.reset();
    }

    if (DriverStation.isDisabled()) {
      // This prevents the shooter from starting when the robot is enabled.
      m_shooterEnabled = false;
    }

    if (m_shooterEnabled) {
      double upperSpeed = m_upperTargetRPM * COUNTS_PER_REV / SENSOR_CYCLES_PER_SECOND / SEC_PER_MIN;
      m_upperMotor.set(ControlMode.Velocity, upperSpeed);      
      double lowerSpeed = m_lowerTargetRPM * COUNTS_PER_REV / SENSOR_CYCLES_PER_SECOND / SEC_PER_MIN;
      m_lowerMotor.set(ControlMode.Velocity, lowerSpeed);      
    } else {
      m_upperMotor.set(0);
      m_lowerMotor.set(0);
    }

  }
}
