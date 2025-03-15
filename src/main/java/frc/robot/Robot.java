package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import edu.wpi.first.wpilibj.Servo;
//import edu.wpi.first.cameraserver.CameraServer;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.DigitalInput;

import edu.wpi.first.wpilibj.XboxController;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to each mode, as
 * described in the TimedRobot documentation. If you change the name of this class or the package after creating this
 * project, you must also update the build.gradle file in the project.
 */
public class Robot extends TimedRobot {
  private static Robot instance;
  private Command m_autonomousCommand;
  private RobotContainer m_robotContainer;
private XboxController Driver;
private XboxController Operater;
private Timer disabledTimer;

//Callouts//

//Motors that outtake the Coral//
private SparkMax CoralMotorL;
private SparkMax CoralMotorR;

private SparkMax BallMotorP;
private TalonFX BallMotorI;
//Human source Intake//
private SparkMax CSource;

//Lift the Motor//
private SparkMax liftMotor;



private final DoubleSolenoid Endsolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 7, 6);
private final DoubleSolenoid clawsolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0,1);
//private final DoubleSolenoid Ballsolenoid = new DoubleSolenoid(PneumaticsModuleType.CTRE, 5, 10);
//private final DoubleSolenoid Armsolenoid = new DoubleSolenoid(PneumaticsModuleType.CTRE, 11, 4);

 private boolean BallOut = false;
// private boolean Process   or = false;
// private boolean ground = false;
 private boolean End = false;

public Robot() {
  instance = this;
  // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
  // autonomous chooser on the dashboard.
  
  //Controller type//  
  Driver = new XboxController(Constants.OperatorConstants.kDriverControllerPort);
  Operater = new XboxController(Constants.OperatorConstants.kOperatorControllerPort);

  CoralMotorL = new SparkMax(Constants.OperatorConstants.CoralMotorL, MotorType.kBrushless);
  CoralMotorR = new SparkMax(Constants.OperatorConstants.CoralMotorR, MotorType.kBrushless);
  BallMotorI= new TalonFX(Constants.OperatorConstants.BallMotorI);
  BallMotorP= new SparkMax(Constants.OperatorConstants.BallMotorP, MotorType.kBrushless);
  CSource= new SparkMax(Constants.OperatorConstants.CSource, MotorType.kBrushless);
 

}

  public static Robot getInstance()
  {
    return instance;
  }

  /**
   * This function is run when the robot is first started up and should be used for any initialization code.
   */
  @Override
  public void robotInit()
  {
    // CameraServer.startAutomaticCapture();
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
    liftMotor = m_robotContainer.getLiftMotor();

    // Create a timer to disable motor brake a few seconds after disable.  This will let the robot stop
    // immediately when disabled, but then also let it be pushed more 
    disabledTimer = new Timer();
   // Endsolenoid.set(Value.kReverse);
   // Armsolenoid.set(Value.kReverse);
    if (isSimulation())
    {
      DriverStation.silenceJoystickConnectionWarning(true);
    }
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics that you want ran
   * during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic()
  {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit()
  {
    m_robotContainer.setMotorBrake(true);
    disabledTimer.reset();
    disabledTimer.start();
  }

  @Override
  public void disabledPeriodic()
  {
    if (disabledTimer.hasElapsed(Constants.DrivebaseConstants.WHEEL_LOCK_TIME))
    {
      m_robotContainer.setMotorBrake(true);
      disabledTimer.stop();
      disabledTimer.reset();
    }
  }

  /**
   * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit()
  {
    m_robotContainer.setMotorBrake(true);
    String selectedAuto = m_robotContainer.getSelectedAuto();
    m_autonomousCommand = m_robotContainer.getAutonomousCommand(selectedAuto);
    // schedule the autonomous command (example)
    if (m_autonomousCommand != null)
    {
      m_autonomousCommand.schedule();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic()
  {
  }

  @Override
  public void teleopInit()
  {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null)
    {
      m_autonomousCommand.cancel();
    } else
    {
      CommandScheduler.getInstance().cancelAll();
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic(){
  
   
      //elevator subsystem
      // // Set the motor speed based on trigger values
      if (Operater.getPOV() == 0) {
        // Move motor forward
        liftMotor.set(-.9); // Scale speed down to 50%
    } else if (Operater.getPOV() == 180) {
        // Move motor backward
        liftMotor.set(.97); // Scale speed down to 50%
    } else  {
        // Stop motor
        liftMotor.set(0);
   }


   // Motors that push the coral to the reef

    // Control the motors based on bumper inputs
    if (Operater.getAButtonPressed()) {
   //      // Spin motors forward
        CoralMotorL.set(.2); // 80% speed forward
        CoralMotorR.set(-.2); // 80% speed forward

     } else if (Operater.getYButtonPressed()) {
        // Spin motors backward
        CoralMotorL.set(-.2); // 80% speed forward
        CoralMotorR.set(.2); // 80% speed forward
     } else if (Operater.getBButtonPressed()) {
      // Spin motors backward
      CoralMotorL.set(.5); // 80% speed forward
      CoralMotorR.set(-.5); // 80% speed forward
   }else if (Operater.getBButtonReleased() || Operater.getAButtonReleased() || Operater.getYButtonReleased()) {
        // Stop motors
      CoralMotorL.set(0);
      CoralMotorR.set(0);

    }
    
    if (Operater.getXButtonPressed()) {
     //      // Spin motors forward
          CSource.set(-.8); // 80% speed forward
       }  else if (Operater.getXButtonReleased()) { 
          // Stop motors
          CSource.set(0);
       }
       
     //   if (toplimitSwitch.get() == false){
     //     BallMotorI.set(-.4);
     //     if (!inTimerRunning) {
     //       timer.reset();
     //       timer.start();
     //       inTimerRunning = true;
     //   }
     // }else  if (toplimitSwitch.get() == true ) {
     //   if (inTimerRunning && timer.get() < 3.0) {
     //     BallMotorI.set(-.4);
     //   }else{
     //     BallMotorI.set(.0);
     //     timer.stop();
     //     inTimerRunning = false;
   
     //   }
     //}

       if (Operater.getRightBumperPressed()) {
         //      // Spin motors forward
              BallMotorP.set(.5); // 80% speed forward
   
           } else if (Operater.getLeftBumperPressed()) {
              // Spin motors backward
              BallMotorP.set(-.5); // 80% speed forward
           } else if (Operater.getRightBumperReleased() || Operater.getLeftBumperReleased()) {
              // Stop motors
            BallMotorP.set(0);

     }

     if (Operater.getRightTriggerAxis() > .1) {
       //      // Spin motors forward
            BallMotorI.set(.5); // 80% speed forward
         } else if (Operater.getLeftTriggerAxis() > .1) {
            // Spin motors backward
            BallMotorI.set(-.5); // 80% speed forward
         } else if (Operater.getRightTriggerAxis() < .1 && Operater.getLeftTriggerAxis() < .1) {
            // Stop motors
          BallMotorI.set(0);

   }  

     

   if(Driver.getRightBumperButtonPressed()){

    End = !End;
}  
if (End){
  
  Endsolenoid.set(Value.kForward);
 
} else {
  Endsolenoid.set(Value.kReverse);
}
  

  
  if(Driver.getYButtonPressed()){

    BallOut = !BallOut;
}  
if (BallOut){
  
  clawsolenoid.set(Value.kForward);
 
} else {
  clawsolenoid.set(Value.kReverse);
}




    // if(Operater.getPOV() == 0)
    // { Armsolenoid.set(Value.kForward);
    // } else if (Operater.getPOV() == 180)
    // {Armsolenoid.set(Value.kReverse);
    // }
  }

  

  @Override
  public void testInit()
  {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic()
  {
  }

  /**
   * This function is called once when the robot is first started up.
   */
  @Override
  public void simulationInit()
  {
  }

  /**
   * This function is called periodically whilst in simulation.
   */
  @Override
  public void simulationPeriodic()
  {
  }
  }
