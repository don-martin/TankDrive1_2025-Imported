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
  // private Drivetrain m_drivetrain;
  // private PhotonCamera camera;

  @Override
  public void robotInit() {
    
    m_robotContainer = new RobotContainer();
    // m_drivetrain = m_robotContainer.m_drivetrain;
    // camera = m_robotContainer.m_drivetrain.camera;
    
    //Live window in TEST mode provides a GUI that can be used to 
    //display status of sensors and actuators and directly set motor inputs/actuators.
    enableLiveWindowInTest(true);
   
  }

  
  @Override
  public void teleopPeriodic() {} 

  @Override
  public void robotPeriodic() {
    // this appears to be working
    //RobotContainer.m_drivetrain.drive(0.5,0.5);
    //System.out.println("In robotPeriodic method");
    CommandScheduler.getInstance().run();
  }
}
