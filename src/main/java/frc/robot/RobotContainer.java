package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.DriveTank;
import frc.robot.subsystem.Drivetrain;

public class RobotContainer {
    public final Drivetrain m_drivetrain = new Drivetrain();
    public static final CommandXboxController m_driverController = new CommandXboxController(Constants.DRIVER_CONTROLLER);

    public RobotContainer () {
       //m_drivetrain.setDefaultCommand(new DriveTank(m_drivetrain));
       configureButtonBindings();
    }

    private void configureButtonBindings() {
        m_driverController.b().whileTrue(m_drivetrain.pivot()).onFalse(Commands.runOnce(() -> m_drivetrain.drive(0,0),m_drivetrain));
        m_driverController.x().onTrue(m_drivetrain.pivot180());
        
        //This will only set the initial values.  To have something continuously logged, use 
        //SmartDashboard.putData(someObjectImplementingSendable).  Sendables only need to be in an
        //init step, not in a periodic step.
        SmartDashboard.putBoolean("bButton",m_driverController.b().getAsBoolean());
        SmartDashboard.putNumber("Left Y",m_driverController.getLeftY());
        SmartDashboard.putNumber("Right Y", m_driverController.getRightY());
    }
}
