package frc.robot.commands;
 
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.swervedrive.auto.L4elevator;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

public class AutonomousCommand extends SequentialCommandGroup {
    public AutonomousCommand(SparkMax liftMotor) {
        addCommands(
            new L4elevator(liftMotor, 0.5, 6000) // Run motor at 50% speed for 6000 milliseconds (6 seconds)
        );
    }
}