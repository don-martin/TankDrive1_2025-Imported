package frc.robot.subsystem;

import org.photonvision.PhotonCamera;

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
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;

public class Drivetrain extends SubsystemBase {
    
    TalonFX leftFront;     
    TalonFX leftRear;      
	TalonFX rightFront;
	TalonFX rightRear;
    public PhotonCamera camera;

    //Alert functionality available in WPILIB 2025 projects.
    Alert speedAlert = new Alert("You're driving too fast.",AlertType.kWarning);

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
        return Commands.run(() -> drive(.5,-.5),this);
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
        //System.out.println("In drivetrain periodic method.");
        //This gets called at the same cadence as the robot periodic method
        //drive(0.8,0.8);
    }
}
