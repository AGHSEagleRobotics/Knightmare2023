// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;

public class AimSubsystem extends SubsystemBase {
  private final AHRS m_ahrs;
  private final Relay m_actuator;   //<  linear actuator

  private double m_angleSetpoint;   // target angle (inclination) for the shooter
  private boolean m_autoEnabled;    // defines whether auto-aim is enabled

  /** Creates a new AimSubsystem. */
  public AimSubsystem(Relay actuator, AHRS ahrs) {
    m_actuator = actuator;
    m_ahrs = ahrs;

    double targetAngle = Preferences.getDouble(ShooterConstants.PREF_KEY_ANGLE, ShooterConstants.DEFAULT_ANGLE);
    setTargetAngle(targetAngle);
  }

  /** Enable or disable Auto Aim.
   *  Auto Aim will continuously adjust the shooter angle to the setpoint when enabled.
   */
  public void setAutoEnabled (boolean enable) {
    m_autoEnabled = enable;

    if (enable == false) {
      setAimMotor(ShooterConstants.AIM_STOP);
    }
  }

  /** Get Auto Aim mode */
  public boolean isAutoEnabled() {
    return m_autoEnabled;
  }

  /** Set the target Aim Angle */
  private void setTargetAngle(double target) {
    m_angleSetpoint = target;
  }
  
  /** Get the target Aim Angle */
  public double getTargetAngle() {
    return m_angleSetpoint;
  }
  
  /** Save the target Aim Angle */
  public void saveTargetAngle() {
    Preferences.setDouble(ShooterConstants.PREF_KEY_ANGLE, m_angleSetpoint);
    DataLogManager.log(String.format("Target angle saved: %.2f", m_angleSetpoint));
  }

  /** Increase the shooter setpoint */
  public void increaseTargetAngle() {
    setTargetAngle(getTargetAngle() + ShooterConstants.SETPOINT_INCREMENT);
  }
  
  /** Decrease the shooter setpoint */
  public void decreaseTargetAngle() {
    setTargetAngle(getTargetAngle() - ShooterConstants.SETPOINT_INCREMENT);
  }

  /** The current shooter pitch angle */
  public double getAngle() {
    return (-1.0 * m_ahrs.getPitch());
  }

  /** Set the aim linear actuator */
  public void setAimMotor(Relay.Value value) {
    m_actuator.set(value);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    if (m_autoEnabled) {
      double pitchError = getAngle() - getTargetAngle();

      if (pitchError > ShooterConstants.AIM_ANGLE_TOLERANCE) {
        // lower the shooter
        setAimMotor(ShooterConstants.AIM_DOWN);
      }
      else if (pitchError < -(ShooterConstants.AIM_ANGLE_TOLERANCE)) {
        // raise the shooter
        setAimMotor(ShooterConstants.AIM_UP);
      }
      else {
        // hold position
        setAimMotor(ShooterConstants.AIM_STOP);
      }
    }
    
    if (DriverStation.isDisabled()) {
      // disable aim
      m_autoEnabled = false;
      setAimMotor(ShooterConstants.AIM_STOP);
    }
  }
}
