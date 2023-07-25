// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;

public class ShooterSubsystem extends SubsystemBase {

  private final WPI_TalonFX m_leftMotor;
  private final WPI_TalonFX m_rightMotor;
  private final CANSparkMax m_feederMotor;
  private final Relay m_signalLight;

  private Timer m_shooterCooldownTimer;   //< Allow the shooter to run for a bit after disabling

  private boolean m_shooterEnabled = false;
  private double m_leftTargetRPM = ShooterConstants.LEFT_RPM_DEFAULT;
  private double m_rightTargetRPM = ShooterConstants.RIGHT_RPM_DEFAULT;

  // Math variables needed to convert RPM to ticks per second/ticks per
  private final int SENSOR_CYCLES_PER_SECOND = 10;   // sensor velocity period is 100 ms
  private final int SEC_PER_MIN = 60;
  private final int COUNTS_PER_REV = 2048;

  public enum Position { left, right, both }
  public enum FeederDirection { forward, reverse, stop }

  /** Creates a new ShooterSubsystem. */
  public ShooterSubsystem(WPI_TalonFX leftMotor, WPI_TalonFX rightMotor, CANSparkMax feederMotor, Relay signalLight)  {
    m_leftMotor = leftMotor;
    m_rightMotor = rightMotor;
    m_feederMotor = feederMotor;
    m_signalLight = signalLight;

    // Configure left motor
    m_leftMotor.configFactoryDefault();
    m_leftMotor.setNeutralMode(NeutralMode.Coast);
    m_leftMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    m_leftMotor.setSensorPhase(false);
    m_leftMotor.setInverted(false);

    m_leftMotor.config_kF(ShooterConstants.PID_IDX, ShooterConstants.GAINS_VELOCITY_F);
    m_leftMotor.config_kP(ShooterConstants.PID_IDX, ShooterConstants.GAINS_VELOCITY_P);
    m_leftMotor.config_kI(ShooterConstants.PID_IDX, ShooterConstants.GAINS_VELOCITY_I);
    m_leftMotor.config_kD(ShooterConstants.PID_IDX, ShooterConstants.GAINS_VELOCITY_D);

    // Configure right motor
    m_rightMotor.configFactoryDefault();
    m_rightMotor.setNeutralMode(NeutralMode.Coast);
    m_rightMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    m_rightMotor.setSensorPhase(true);
    m_rightMotor.setInverted(true);

    m_rightMotor.config_kF(ShooterConstants.PID_IDX, ShooterConstants.GAINS_VELOCITY_F);
    m_rightMotor.config_kP(ShooterConstants.PID_IDX, ShooterConstants.GAINS_VELOCITY_P);
    m_rightMotor.config_kI(ShooterConstants.PID_IDX, ShooterConstants.GAINS_VELOCITY_I);
    m_rightMotor.config_kD(ShooterConstants.PID_IDX, ShooterConstants.GAINS_VELOCITY_D);

    // Configure feeder motor
    m_feederMotor.setIdleMode(IdleMode.kBrake);
    m_feederMotor.setInverted(true);

    m_shooterCooldownTimer = new Timer();
  }

  /** Enable or disable the shooter.
   *  When disabling, the shooter will run for a short time before stopping.
   *  If you want the shooter to stop right away, use stopShooter() instead.
   */
  public void setShooterEnabled (boolean enable) { 
    if (enable) {
      m_shooterEnabled = true;
      DataLogManager.log("enabling shooter");
      m_shooterCooldownTimer.stop();    // stop the cooldown timer
      m_shooterCooldownTimer.reset();
    } else { // if false
      m_shooterCooldownTimer.restart(); // start (or restart) the cooldown timer
    }
  }

  /** Stop the shooter without delay */
  public void stopShooter() {
    m_shooterEnabled = false;
    DataLogManager.log("stopping shooter");
    m_shooterCooldownTimer.stop();    // stop the cooldown timer
    m_shooterCooldownTimer.reset();
  }
  
  public boolean isShooterEnabled () { 
    return m_shooterEnabled;
  }
  
  public void setLeftTargetRPM (double rpm) {
    m_leftTargetRPM = rpm;
  }
  public void setRightTargetRPM (double rpm) {
    m_rightTargetRPM = rpm;
  }

  public double getLeftTargetRPM () {
    return m_leftTargetRPM;
  }
  public double getRightTargetRPM () {
    return m_rightTargetRPM;
  }

  public double getLeftRPM () {
    double rawSensorData = m_leftMotor.getSelectedSensorVelocity();
    double motorRPM = rawSensorData * SENSOR_CYCLES_PER_SECOND * SEC_PER_MIN / COUNTS_PER_REV;
// if (m_shooterEnabled) return m_leftTargetRPM;  // DEBUG
// else return 0;  // DEBUG
    return motorRPM;
  }
  public double getRightRPM () {
    double rawSensorData = m_rightMotor.getSelectedSensorVelocity();
    double motorRPM = rawSensorData * SENSOR_CYCLES_PER_SECOND * SEC_PER_MIN / COUNTS_PER_REV;
// if (m_shooterEnabled) return m_rightTargetRPM;  // DEBUG
// else return 0;  // DEBUG
    return motorRPM;
  }

  public boolean isLeftRpmOk() {
    double error = getLeftRPM() - m_leftTargetRPM;
    return (Math.abs(error) <= ShooterConstants.SHOOTER_RPM_TOLERANCE);
  }
  public boolean isRightRpmOk() {
    double error = getRightRPM() - m_rightTargetRPM;
    return (Math.abs(error) <= ShooterConstants.SHOOTER_RPM_TOLERANCE);
  }

  /** Increase shooter speed(s) by one step
   *  @param  position  Increase speed of left, right or both wheels
   */
  public void shooterRpmStepIncrease(Position position) {
    if ((position == Position.left) || (position == Position.both)) {
      m_leftTargetRPM += ShooterConstants.SHOOTER_RPM_STEP_CHANGE;
      m_leftTargetRPM = MathUtil.clamp(m_leftTargetRPM, ShooterConstants.MIN_SHOOTER_RPM, ShooterConstants.MAX_SHOOTER_RPM);
    }
    if ((position == Position.right) || (position == Position.both)) {
      m_rightTargetRPM += ShooterConstants.SHOOTER_RPM_STEP_CHANGE;
      m_rightTargetRPM = MathUtil.clamp(m_rightTargetRPM, ShooterConstants.MIN_SHOOTER_RPM, ShooterConstants.MAX_SHOOTER_RPM);
    }
  }

  /** Decrease shooter speed(s) by one step
   *  @param  position  Decrease speed of left, right or both wheels
   */
  public void shooterRpmStepDecrease(Position position) {
    if ((position == Position.left) || (position == Position.both)) {
      m_leftTargetRPM -= ShooterConstants.SHOOTER_RPM_STEP_CHANGE;
      m_leftTargetRPM = MathUtil.clamp(m_leftTargetRPM, ShooterConstants.MIN_SHOOTER_RPM, ShooterConstants.MAX_SHOOTER_RPM);
    }
    if ((position == Position.right) || (position == Position.both)) {
      m_rightTargetRPM -= ShooterConstants.SHOOTER_RPM_STEP_CHANGE;
      m_rightTargetRPM = MathUtil.clamp(m_rightTargetRPM, ShooterConstants.MIN_SHOOTER_RPM, ShooterConstants.MAX_SHOOTER_RPM);
    }
  }

  public void setFeederMotor(FeederDirection direction) {
    switch (direction) {
      case forward:
        m_feederMotor.set(ShooterConstants.FEEDER_FWD_SPEED);
        break;
        
      case reverse:
        m_feederMotor.set(ShooterConstants.FEEDER_REV_SPEED);
        break;

      case stop:
      default:
        m_feederMotor.set(0.0);
        break;
    }
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    
    if (m_shooterCooldownTimer.hasElapsed(ShooterConstants.SHOOTER_COOLDOWN_TIME))  {
      // don't turn off shooter until some time has elapsed
      m_shooterEnabled = false;
      DataLogManager.log("disabling shooter");
      m_shooterCooldownTimer.stop();
      m_shooterCooldownTimer.reset();
    }

    if (DriverStation.isDisabled()) {
      // This prevents the shooter from starting when the robot is enabled.
      m_shooterEnabled = false;
    }

    if (m_shooterEnabled) {
      double leftSpeed = m_leftTargetRPM * COUNTS_PER_REV / SENSOR_CYCLES_PER_SECOND / SEC_PER_MIN;
      m_leftMotor.set(ControlMode.Velocity, leftSpeed);
      double rightSpeed = m_rightTargetRPM * COUNTS_PER_REV / SENSOR_CYCLES_PER_SECOND / SEC_PER_MIN;
      m_rightMotor.set(ControlMode.Velocity, rightSpeed);
      m_signalLight.set(ShooterConstants.SIGNAL_ON);
    } else {
      m_leftMotor.set(0);
      m_rightMotor.set(0);
      m_signalLight.set(ShooterConstants.SIGNAL_OFF);
    }

  }
}
