// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

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
    public static final int CANID_upperMotor = 5;
    public static final int CANID_lowerMotor = 6;

    public static final int PID_IDX = 0;
    public static final double GAINS_VELOCITY_F = 0.047;
    public static final double GAINS_VELOCITY_P = 0.25;
    public static final double GAINS_VELOCITY_I = 0;
    public static final double GAINS_VELOCITY_D = 0;

    public static final double SHOOTER_COOLDOWN_TIME = 2.0;   // time in seconds to wait before shooter is turned off

    public static final double UPPER_RPM_DEFAULT = 1000;
    public static final double LOWER_RPM_DEFAULT = 1000;

    public static final double SHOOTER_RPM_STEP_CHANGE = 100;
    public static final double MAX_SHOOTER_RPM = 6000;
    public static final double MIN_SHOOTER_RPM = 0;
}

  public static class DashboardConstants {
    public static final int USBID_Camera = 0;

    public static final int targetRpmWidth = 3;
    public static final int targetRpmHeight = 3;
    public static final int rpmWidth = 6;
    public static final int rpmHeight = targetRpmHeight;

    public static final int upperTargetX = 12;
    public static final int upperTargetY = 0;
    public static final int lowerTargetX = upperTargetX;
    public static final int lowerTargetY = upperTargetY + targetRpmHeight;

    public static final int upperRpmX = upperTargetX + targetRpmWidth;
    public static final int upperRpmY = upperTargetY;
    public static final int lowerRpmX = lowerTargetX + targetRpmWidth;
    public static final int lowerRpmY = lowerTargetY;
  }
}
