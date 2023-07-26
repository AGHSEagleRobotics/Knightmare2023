// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Relay;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }

  public static class DriveTrainConstants {
    public static final int CANID_frontRight = 1;
    public static final int CANID_backRight = 2;
    public static final int CANID_backLeft = 3;
    public static final int CANID_frontLeft = 4;
  }

  public static class ShooterConstants {
    public static final int CANID_leftMotor = 5;
    public static final int CANID_rightMotor = 6;
    public static final int CANID_feederMotor = 7;
    public static final int RELAY_signalLight = 1;

    public static final int PID_IDX = 0;
    public static final double GAINS_VELOCITY_F = 0.047;
    public static final double GAINS_VELOCITY_P = 0.25;
    public static final double GAINS_VELOCITY_I = 0;
    public static final double GAINS_VELOCITY_D = 0;

    public static final double SHOOTER_COOLDOWN_TIME = 2.0;   // time in seconds to wait before shooter is turned off

    public static final double LEFT_RPM_DEFAULT = 1000;
    public static final double RIGHT_RPM_DEFAULT = 1000;

    public static final double SHOOTER_TRIGGER_THRESHOLD = 0.5;

    public static final double SHOOTER_RPM_STEP_CHANGE = 100;
    public static final double MAX_SHOOTER_RPM = 6000;
    public static final double MIN_SHOOTER_RPM = 0;

    public static final double SHOOTER_RPM_TOLERANCE = 100;

    // Shoot command
    public static final double SHOOT_FWD_TIME = 0.25;
    public static final double SHOOT_REV_TIME = 0.25;
    
    public static final double FEEDER_FWD_SPEED = 0.5;
    public static final double FEEDER_REV_SPEED = -0.25;

    public static final Relay.Value SIGNAL_ON = Relay.Value.kForward;
    public static final Relay.Value SIGNAL_OFF = Relay.Value.kOff;

    // Aim Subsystem
    public static final int RELAY_aimActuator = 0;

    public static final String PREF_KEY_ANGLE = "Knightmare2023/AimAngle";
    public static final double DEFAULT_ANGLE = 10.0;
    public static final double AIM_ANGLE_TOLERANCE = 0.15;  // degrees; the shooter is considered on-target within this tolerance

    public static final double SETPOINT_INCREMENT = 0.10;   // degrees - amount to change the setpoint for manual adjustment
    public static final int INCREMENT_DELAY = 25;           // loop count; increment/decrement once, then wait this long to increment continuously

    public static final Relay.Value AIM_UP = Relay.Value.kReverse;
    public static final Relay.Value AIM_DOWN = Relay.Value.kForward;
    public static final Relay.Value AIM_STOP = Relay.Value.kOff;
  }

  public static class DashboardConstants {
    public static final int USBID_Camera = 0;

    public static final int targetRpmWidth = 3;
    public static final int targetRpmHeight = 3;
    public static final int rpmWidth = 6;
    public static final int rpmHeight = targetRpmHeight;
    public static final int rpmOkWidth = 2;
    public static final int rpmOkHeight = targetRpmHeight;

    public static final int leftTargetX = 12;
    public static final int leftTargetY = 0;
    public static final int rightTargetX = leftTargetX;
    public static final int rightTargetY = leftTargetY + targetRpmHeight;

    public static final int leftRpmX = leftTargetX + targetRpmWidth;
    public static final int leftRpmY = leftTargetY;
    public static final int rightRpmX = rightTargetX + targetRpmWidth;
    public static final int rightRpmY = rightTargetY;

    public static final int leftRpmOkX = leftTargetX + targetRpmWidth + rpmWidth;
    public static final int leftRpmOkY = leftTargetY;
    public static final int rightRpmOkX = rightTargetX + targetRpmWidth + rpmWidth;
    public static final int rightRpmOkY = rightTargetY;

    public static final int angleWidth = 3;
    public static final int angleHeight = 2;
    public static final int autoAimWidth = 2;
    public static final int autoAimHeight = angleHeight;

    public static final int targetAngleX = leftTargetX;
    public static final int targetAngleY = rightTargetY + targetRpmHeight;
    public static final int shooterAngleX = targetAngleX + angleWidth;
    public static final int shooterAngleY = targetAngleY;
    public static final int autoAimX = shooterAngleX + angleWidth;
    public static final int autoAimY = targetAngleY;

    public static final int pitchAngleX = leftTargetX;
    public static final int pitchAngleY = 8;
    public static final int pitchAngleWidth = 3;
    public static final int pitchAngleHeight = 2;
  }
}
