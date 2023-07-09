// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.Map;

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
  private final ShooterSubsystem m_ShooterSubsystem;

  private final ShuffleboardTab m_shuffleboardTab;
  private final String SHUFFLEBOARD_TAB_NAME = "PlayBall";

  private final UsbCamera m_cameraView;
  private final int CAMERA_RES_WIDTH = 320;
  private final int CAMERA_RES_HEIGHT = 200;
  private final int CAMERA_FPS = 30;

  private final ComplexWidget m_CameraComplexWidget;

  private final GenericEntry m_upperTargetRPM;
  private final GenericEntry m_lowerTargetRPM;
  private final GenericEntry m_upperRPM;
  private final GenericEntry m_lowerRPM;
  private final GenericEntry m_upperRpmOk;
  private final GenericEntry m_lowerRpmOk;

  /** Creates a new Dashboard. */
  public Dashboard(ShooterSubsystem shooterSubsystem) {
    m_ShooterSubsystem = shooterSubsystem;

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
      
    m_upperTargetRPM = m_shuffleboardTab.add("UpperTarget", 0)
      .withSize(DashboardConstants.targetRpmWidth, DashboardConstants.targetRpmHeight)
      .withPosition(DashboardConstants.upperTargetX, DashboardConstants.upperTargetY)
      .getEntry();
    m_upperRPM = m_shuffleboardTab.add("UpperRPM", 0)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("Min", 0.0,
                             "Max", 6000.0))
      .withSize(DashboardConstants.rpmWidth, DashboardConstants.rpmHeight)
      .withPosition(DashboardConstants.upperRpmX, DashboardConstants.upperRpmY)
      .getEntry();
    m_upperRpmOk = m_shuffleboardTab.add("UpperOk", false)
      .withWidget(BuiltInWidgets.kBooleanBox)
      .withSize(DashboardConstants.rpmOkWidth, DashboardConstants.rpmOkHeight)
      .withPosition(DashboardConstants.upperRpmOkX, DashboardConstants.upperRpmOkY)
      .getEntry();
      
    m_lowerTargetRPM = m_shuffleboardTab.add("LowerTarget", 0)
      .withSize(DashboardConstants.targetRpmWidth, DashboardConstants.targetRpmHeight)
      .withPosition(DashboardConstants.lowerTargetX, DashboardConstants.lowerTargetY)
      .getEntry();
    m_lowerRPM = m_shuffleboardTab.add("LowerRPM", 0)
      .withWidget(BuiltInWidgets.kNumberBar)
      .withProperties(Map.of("Min", 0.0,
                             "Max", 6000.0))
      .withSize(DashboardConstants.rpmWidth, DashboardConstants.rpmHeight)
      .withPosition(DashboardConstants.lowerRpmX, DashboardConstants.lowerRpmY)
      .getEntry();
    m_lowerRpmOk = m_shuffleboardTab.add("LowerOk", false)
      .withWidget(BuiltInWidgets.kBooleanBox)
      .withSize(DashboardConstants.rpmOkWidth, DashboardConstants.rpmOkHeight)
      .withPosition(DashboardConstants.lowerRpmOkX, DashboardConstants.lowerRpmOkY)
      .getEntry();

    // m_pitch = m_shuffleboardTab.add("Pitch", 0 )
    // .withWidget(BuiltInWidgets.kTextView)
    // .withSize(2, 2)
    // .withPosition(19, 4)
    // .getEntry();
  } // end constructor


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    m_upperTargetRPM.setInteger((int)m_ShooterSubsystem.getUpperTargetRPM());
    m_lowerTargetRPM.setInteger((int)m_ShooterSubsystem.getLowerTargetRPM());
    m_upperRPM.setInteger((int)m_ShooterSubsystem.getUpperRPM());
    m_lowerRPM.setInteger((int)m_ShooterSubsystem.getLowerRPM());
    m_upperRpmOk.setBoolean(m_ShooterSubsystem.isUpperRpmOk());
    m_lowerRpmOk.setBoolean(m_ShooterSubsystem.isLowerRpmOk());
  }
}
