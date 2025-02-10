package frc.robot.subsystem;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.commands.AimTank;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;

public class Drivetrain extends SubsystemBase {
    
    TalonFX leftFront;     
    TalonFX leftRear;      
	TalonFX rightFront;
	TalonFX rightRear;
    PhotonCamera camera;

    //Alert functionality available in WPILIB 2025 projects.
    Alert speedAlert = new Alert("You're driving too fast.",AlertType.kWarning);
    Alert target2Found = new Alert("Target 2 Found.",AlertType.kWarning);
    Alert target3Found = new Alert("Target 3 Found.", AlertType.kWarning);
    Alert cameraNotConnected = new Alert("Camera is not connected.",AlertType.kWarning);
      
    public Drivetrain() {
        camera = new PhotonCamera("camera1"); // cameraName should match what is set on the camera.

        rightFront = new TalonFX(Constants.DRIVETRAIN_RIGHT_FRONT_PORT,"rio"); //Ports are the device id on the phoenix tuner
        leftFront = new TalonFX(Constants.DRIVETRAIN_LEFT_FRONT_PORT,"rio");    
        rightRear = new TalonFX(Constants.DRIVETRAIN_RIGHT_REAR_PORT,"rio");
        leftRear = new TalonFX(Constants.DRIVETRAIN_LEFT_REAR_PORT,"rio");

        // set motors up to coast instead of brake when current stops
        rightFront.setNeutralMode(NeutralModeValue.Coast);
        leftFront.setNeutralMode(NeutralModeValue.Coast);
        rightRear.setNeutralMode(NeutralModeValue.Coast);
        leftRear.setNeutralMode(NeutralModeValue.Coast);

        leftRear.setControl(new Follower(leftFront.getDeviceID(), false));
        rightRear.setControl(new Follower(rightFront.getDeviceID(), false));

        // Reset motors to original factory configuration - probably not necessary
        //rightMotor.getConfigurator().apply(new TalonFXConfiguration());
        //leftMotor.getConfigurator().apply(new TalonFXConfiguration());

        // Configure the current of the motors
        var currentConfiguration = new CurrentLimitsConfigs();
        currentConfiguration.StatorCurrentLimit = 80;
        currentConfiguration.StatorCurrentLimitEnable = true;

        var motorConfig = new MotorOutputConfigs();
        motorConfig.Inverted = InvertedValue.Clockwise_Positive;

        // rightMotor.getConfigurator().refresh(currentConfiguration);
        rightFront.getConfigurator().apply(currentConfiguration);
        //leftMotor.getConfigurator().refresh(currentConfiguration);
        rightFront.getConfigurator().apply(currentConfiguration);
        leftFront.getConfigurator().apply(motorConfig);

        
        //If we uncomment the following two lines, we get only the two actuators below in the live window.  
        //If we leave the two lines below commented out, we get 4 motors in the live window.
        //Not sure if this related to the rear motors being followers?
        //SendableRegistry.addChild(this, leftFront);
        //SendableRegistry.addChild(this, rightFront);

    }

    public Command pivot() {
        SmartDashboard.putBoolean("bButton",RobotContainer.m_driverController.b().getAsBoolean());
        return Commands.runOnce(() -> drive(.5,-.5),this);
    }

    public Command pivot180() {
        return Commands.run(() -> drive(-.5,.5),this).withTimeout(1).andThen(Commands.runOnce(() -> drive(0.0,0.0),this));
    }

    public void drive(double leftSpeed, double rightSpeed) {
        //debug example written to the console
        //System.out.println("leftSpeed: " + leftSpeed + "\nrightSpeed: " + rightSpeed);

        //Available in 2025
        speedAlert.set(leftSpeed + rightSpeed > 1.8);

        rightFront.set(rightSpeed);
        leftFront.set(leftSpeed);
        
    }

    @Override
    public void periodic() {
        boolean targetVisible = false;
        double targetYaw = 0.0;
        double targetRange = 0.0;
        cameraNotConnected.set(!camera.isConnected());
        var results = camera.getAllUnreadResults();
        if (!results.isEmpty()) {
            // Camera processed a new frame since last
            // Get the last one in the list.
            var result = results.get(results.size() - 1);
            if (result.hasTargets()) {
                // At least one AprilTag was seen by the camera
                for (var target : result.getTargets()) {
                    target2Found.set(target.getFiducialId() == 2);
                    target3Found.set(target.getFiducialId() == 3);
                    if (target.getFiducialId() == 2) {
                        // Found Tag 7, record its information
                        targetYaw = target.getYaw();
                        System.out.println("Yaw: " + targetYaw);
                        //Command aim = new AimTank(this, target);
                        drive((target.getYaw()/100)*(-1),target.getYaw()/100);
                        // if yaw is positive, turn to the right
                        // if yaw is negative, turn to the left.
                        // set this up so that it pivo      // a target is identified, then stops and hones in on the 
                        // target.
                        // read about the PID controller - there is a link in the 
                        //photon code examples.
                        /*targetRange =
                                PhotonUtils.calculateDistanceToTargetMeters(
                                        0.5, // Measured with a tape measure, or in CAD.
                                        1.435, // From 2024 game manual for ID 7
                                        Units.degreesToRadians(-30.0), // Measured with a protractor, or in CAD.
                                        Units.degreesToRadians(target.getPitch()));

                        targetVisible = true;*/
                    }
                }
            } else {
                drive(0,0);
            }
        }
    }
}
