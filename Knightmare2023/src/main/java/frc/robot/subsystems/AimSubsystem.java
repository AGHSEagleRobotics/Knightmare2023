// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.fasterxml.jackson.databind.deser.ValueInstantiator.Gettable;
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
  private boolean m_manualActive;   // defines whether manual aim is active

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
      // also disable manual aim, if it's active
      m_manualActive = false;
    }
  }

  /** Set the target Aim Angle */
  private void setTargetAngle(double target) {
    m_angleSetpoint = target;
    Preferences.setDouble(ShooterConstants.PREF_KEY_ANGLE, target);
    DataLogManager.log(String.format("Target angle set to %.2f", target));
  }

  /** Get the target Aim Angle */
  public double getTargetAngle() {
    return m_angleSetpoint;
  }

  /** Increase the shooter setpoint */
  public void increaseAngle() {
    setTargetAngle(getTargetAngle() + ShooterConstants.SETPOINT_INCREMENT);
    m_manualActive = true;
    m_autoEnabled = false;
  }
  
  /** Decrease the shooter setpoint */
  public void decreaseAngle() {
    setTargetAngle(getTargetAngle() - ShooterConstants.SETPOINT_INCREMENT);
    m_manualActive = true;
    m_autoEnabled = false;
  }

  /** The current shooter pitch angle */
  public double getAngle() {
    return m_ahrs.getPitch();   // ToDo: verify that this is the correct axis/orientation
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    if (m_autoEnabled || m_manualActive) {
      double pitchError = getAngle() - getTargetAngle();

      if (pitchError > ShooterConstants.AIM_ANGLE_TOLERANCE) {
        // lower the shooter
        m_actuator.set(ShooterConstants.AIM_DOWN);
      }
      else if (pitchError < -(ShooterConstants.AIM_ANGLE_TOLERANCE)) {
        // raise the shooter
        m_actuator.set(ShooterConstants.AIM_UP);
      }
      else {
        // hold position
        m_actuator.set(ShooterConstants.AIM_STOP);
        // we're at the setpoint - disable manual mode
        m_manualActive = false;
      }
    } else {
      // hold position
      m_actuator.set(ShooterConstants.AIM_STOP);
    }

    if (DriverStation.isDisabled()) {
      // disable aim
      m_autoEnabled = false;
      m_manualActive = false;
    }
  }
}
