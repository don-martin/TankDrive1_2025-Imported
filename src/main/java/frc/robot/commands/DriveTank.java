package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.subsystem.Drivetrain;

public class DriveTank extends Command {
    private Drivetrain m_drivetrain;

    public DriveTank(Drivetrain drivetrain) {
        m_drivetrain = drivetrain;
        addRequirements(drivetrain);

    }

    @Override
    public void execute() {
        m_drivetrain.drive(RobotContainer.m_driverController.getLeftY(), RobotContainer.m_driverController.getRightY());
        
        // To get continuous updates to the SmartDashboard of objects that don't implement Sendable, the PUTs need to be in a code location where there are called repeatedly.
        SmartDashboard.putNumber("Left Y",RobotContainer.m_driverController.getLeftY());
        SmartDashboard.putNumber("Right Y", RobotContainer.m_driverController.getRightY());
    }

    @Override
    public boolean isFinished() {
        return false;
    }
    
    @Override
    public void end(boolean bool) {
        m_drivetrain.drive(0,0);
    }
}
