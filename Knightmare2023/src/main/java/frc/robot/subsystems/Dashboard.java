// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.Map;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSource.ConnectionStrategy;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DashboardConstants;

public class Dashboard extends SubsystemBase {
  private final ShooterSubsystem m_shooterSubsystem;
  private final AimSubsystem m_aimSubsystem;
  private final AHRS m_ahrs;

  private final ShuffleboardTab m_shuffleboardTab;
  private final String SHUFFLEBOARD_TAB_NAME = "PlayBall";

  private final UsbCamera m_cameraView;
  private final int CAMERA_RES_WIDTH = 320;
  private final int CAMERA_RES_HEIGHT = 200;
  private final int CAMERA_FPS = 30;

  private final ComplexWidget m_CameraComplexWidget;

  private final GenericEntry m_leftTargetRPM;
  private final GenericEntry m_rightTargetRPM;
  private final GenericEntry m_leftRPM;
  private final GenericEntry m_rightRPM;
  private final GenericEntry m_leftRpmOk;
  private final GenericEntry m_rightRpmOk;

  private final GenericEntry m_targetAngle;
  private final GenericEntry m_shooterAngle;
  private final GenericEntry m_autoAim;
  
  private final GenericEntry m_rollAngle;
  private final GenericEntry m_pitchAngle;
  private final GenericEntry m_yawAngle;
  private final GenericEntry m_isMoving;
  
  /** Creates a new Dashboard. */
  public Dashboard(ShooterSubsystem shooterSubsystem, AimSubsystem aimSubsystem, AHRS ahrs) {
    m_shooterSubsystem = shooterSubsystem;
    m_aimSubsystem = aimSubsystem;
    m_ahrs = ahrs;

    m_shuffleboardTab = Shuffleboard.getTab(SHUFFLEBOARD_TAB_NAME);
    Shuffleboard.selectTab(SHUFFLEBOARD_TAB_NAME);

    m_cameraView = CameraServer.startAutomaticCapture(DashboardConstants.USBID_Camera);
    m_cameraView.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    m_cameraView.setFPS(CAMERA_FPS);
    m_cameraView.setResolution(CAMERA_RES_WIDTH, CAMERA_RES_HEIGHT);

    m_CameraComplexWidget = m_shuffleboardTab.add("Camera View", m_cameraView)
      .withWidget(BuiltInWidgets.kCameraStream)
      .withSize(12, 12)
      .withPosition(0, 0);
      
    m_leftTargetRPM = m_shuffleboardTab.add("LeftSetpoint", 0)
      .withSize(DashboardConstants.targetRpmWidth, DashboardConstants.targetRpmHeight)
      .withPosition(DashboardConstants.leftTargetX, DashboardConstants.leftTargetY)
      .getEntry();
    m_leftRPM = m_shuffleboardTab.add("LeftRPM", 0)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("Min", 0.0,
                             "Max", 6000.0))
      .withSize(DashboardConstants.rpmWidth, DashboardConstants.rpmHeight)
      .withPosition(DashboardConstants.leftRpmX, DashboardConstants.leftRpmY)
      .getEntry();
    m_leftRpmOk = m_shuffleboardTab.add("LeftOk", false)
      .withWidget(BuiltInWidgets.kBooleanBox)
      .withSize(DashboardConstants.rpmOkWidth, DashboardConstants.rpmOkHeight)
      .withPosition(DashboardConstants.leftRpmOkX, DashboardConstants.leftRpmOkY)
      .getEntry();
      
    m_rightTargetRPM = m_shuffleboardTab.add("RightSetpoint", 0)
      .withSize(DashboardConstants.targetRpmWidth, DashboardConstants.targetRpmHeight)
      .withPosition(DashboardConstants.rightTargetX, DashboardConstants.rightTargetY)
      .getEntry();
    m_rightRPM = m_shuffleboardTab.add("RightRPM", 0)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("Min", 0.0,
                             "Max", 6000.0))
      .withSize(DashboardConstants.rpmWidth, DashboardConstants.rpmHeight)
      .withPosition(DashboardConstants.rightRpmX, DashboardConstants.rightRpmY)
      .getEntry();
    m_rightRpmOk = m_shuffleboardTab.add("RightOk", false)
      .withWidget(BuiltInWidgets.kBooleanBox)
      .withSize(DashboardConstants.rpmOkWidth, DashboardConstants.rpmOkHeight)
      .withPosition(DashboardConstants.rightRpmOkX, DashboardConstants.rightRpmOkY)
      .getEntry();

    m_targetAngle = m_shuffleboardTab.add("TargetAngle", 0)
      .withSize(DashboardConstants.angleWidth, DashboardConstants.angleHeight)
      .withPosition(DashboardConstants.targetAngleX, DashboardConstants.targetAngleY)
      .getEntry();

    m_shooterAngle = m_shuffleboardTab.add("ShooterAngle", 0)
      .withSize(DashboardConstants.angleWidth, DashboardConstants.angleHeight)
      .withPosition(DashboardConstants.shooterAngleX, DashboardConstants.shooterAngleY)
      .getEntry();

    m_autoAim = m_shuffleboardTab.add("AutoAim", false)
      .withWidget(BuiltInWidgets.kBooleanBox)
      .withSize(DashboardConstants.autoAimWidth, DashboardConstants.autoAimHeight)
      .withPosition(DashboardConstants.autoAimX, DashboardConstants.autoAimY)
      .getEntry();

    m_rollAngle = m_shuffleboardTab.add("Roll", 0)
      .withWidget(BuiltInWidgets.kTextView)
      .withSize(2, 2)
      .withPosition(12, 9)
      .getEntry();
    m_pitchAngle = m_shuffleboardTab.add("Pitch", 0)
      .withWidget(BuiltInWidgets.kTextView)
      .withSize(2, 2)
      .withPosition(14, 9)
      .getEntry();
    m_yawAngle = m_shuffleboardTab.add("Yaw", 0)
      .withWidget(BuiltInWidgets.kTextView)
      .withSize(2, 2)
      .withPosition(16, 9)
      .getEntry();
    m_isMoving = m_shuffleboardTab.add("Moving", false)
      .withWidget(BuiltInWidgets.kBooleanBox)
      .withSize(2, 1)
      .withPosition(18, 9)
      .getEntry();
  } // end constructor


  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    // RPM
    m_leftTargetRPM.setInteger((int)m_shooterSubsystem.getLeftTargetRPM());
    m_rightTargetRPM.setInteger((int)m_shooterSubsystem.getRightTargetRPM());
    m_leftRPM.setInteger((int)m_shooterSubsystem.getLeftRPM());
    m_rightRPM.setInteger((int)m_shooterSubsystem.getRightRPM());
    m_leftRpmOk.setBoolean(m_shooterSubsystem.isLeftRpmOk());
    m_rightRpmOk.setBoolean(m_shooterSubsystem.isRightRpmOk());

    // Aim
    m_targetAngle.setDouble(Math.round(m_aimSubsystem.getTargetAngle() * 100.0) / 100.0);
    m_shooterAngle.setDouble(Math.round(m_aimSubsystem.getAngle() * 100.0) / 100.0);
    m_autoAim.setBoolean(m_aimSubsystem.isAutoEnabled());

    // ToDo: Temporary
    m_rollAngle.setDouble(Math.round(m_ahrs.getRoll() * 100.0) / 100.0);
    m_pitchAngle.setDouble(Math.round(m_ahrs.getPitch() * 100.0) / 100.0);
    m_yawAngle.setDouble(Math.round(m_ahrs.getYaw() * 100.0) / 100.0);
    m_isMoving.setBoolean(m_ahrs.isMoving());
  }
}
