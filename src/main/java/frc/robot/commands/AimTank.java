package frc.robot.commands;

import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystem.Drivetrain;

public class AimTank extends Command {

//Not using this class

     private Drivetrain m_drivetrain;
     private PhotonTrackedTarget target;

    public AimTank(Drivetrain drivetrain, PhotonTrackedTarget target) {
        m_drivetrain = drivetrain;
        this.target = target;
        addRequirements(drivetrain);
    }

    @Override
    public void execute() {
        m_drivetrain.drive(0,target.getYaw());
        
    }

    @Override
    public boolean isFinished() {
        return true;
    }
    
    @Override
    public void end(boolean bool) {
        m_drivetrain.drive(0,0);
    }
}


