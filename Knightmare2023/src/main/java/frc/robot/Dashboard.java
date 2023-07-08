package frc.robot;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSource.ConnectionStrategy;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.Constants.DashboardConstants;

public class Dashboard {
    private final ShuffleboardTab m_shuffleboardTab;
    private final String SHUFFLEBOARD_TAB_NAME = "PlayBall";

    private final UsbCamera m_cameraView;
    private final int CAMERA_RES_WIDTH = 320;
    private final int CAMERA_RES_HEIGHT = 200;
    private final int CAMERA_FPS = 30;

    private final ComplexWidget m_CameraComplexWidget;
    
    public Dashboard() {
        m_shuffleboardTab =  Shuffleboard.getTab(SHUFFLEBOARD_TAB_NAME);
        Shuffleboard.selectTab(SHUFFLEBOARD_TAB_NAME);

        m_cameraView = CameraServer.startAutomaticCapture(DashboardConstants.USBID_Camera);
        m_cameraView.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
        m_cameraView.setFPS(CAMERA_FPS);
        m_cameraView.setResolution(CAMERA_RES_WIDTH, CAMERA_RES_HEIGHT);

        m_CameraComplexWidget = m_shuffleboardTab.add("Camera View", m_cameraView)
            .withWidget(BuiltInWidgets.kCameraStream)
            .withSize(19, 11)
            .withPosition(0, 0);

        // m_pitch = m_shuffleboardTab.add("Pitch", 0 )
        //     .withWidget(BuiltInWidgets.kTextView)
        //     .withSize(2, 2)
        //     .withPosition(19, 4)
        //     .getEntry();
    } //end constructor

}
 