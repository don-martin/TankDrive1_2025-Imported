// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;

import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystem.Drivetrain;

/**
 * This is a demo program showing the use of the DifferentialDrive class. Runs the motors with tank
 * steering and an Xbox controller.
 * 
 * Git Hub remotre repository is https://github.com/don-martin/TankDrive1/tree/main
 * 
 */
public class Robot extends TimedRobot {

  private RobotContainer m_robotContainer;
  private Drivetrain m_drivetrain;
  private PhotonCamera camera;
  private Alert target2Found = new Alert("Target 2 Found.",AlertType.kWarning);
  private Alert target3Found = new Alert("Target 3 Found.", AlertType.kWarning);

 
  @Override
  public void robotInit() {
    
    m_robotContainer = new RobotContainer();
    m_drivetrain = m_robotContainer.m_drivetrain;
    camera = m_robotContainer.m_drivetrain.camera;
    
    //Live window in TEST mode provides a GUI that can be used to 
    //display status of sensors and actuators and directly set motor inputs/actuators.
    enableLiveWindowInTest(true);
   
  }

  
  @Override
  public void teleopPeriodic() {

    boolean targetVisible = false;
    double targetYaw = 0.0;
    double targetRange = 0.0;
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
                    if (target.getFiducialId() > 0) {
                        // Found Tag 7, record its information
                        targetYaw = target.getYaw();
                        System.out.println("Yaw: " + targetYaw);
                        // if yaw is positive, turn to the right
                        // if yaw is negative, turn to the left.
                        // set this up so that it pivots in a circle until
                        // a target is identified, then stops and hones in on the 
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
            }
        }

  } 

  @Override
  public void robotPeriodic() {
    // this appears to be working
    //RobotContainer.m_drivetrain.drive(0.5,0.5);
    //System.out.println("In robotPeriodic method");
    CommandScheduler.getInstance().run();
  }
}
