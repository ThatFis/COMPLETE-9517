package frc.robot.commands.swervedrive.auto;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.Command;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

public class L4elevator extends Command {


    private final SparkMax liftMotor;
    private final double speed;
    private final long duration;
    private long startTime;

    

    public L4elevator(SparkMax liftMotor, double speed, long duration) {
        this.liftMotor = liftMotor;
        this.speed = speed;
        this.duration = duration; 
    }

    @Override
    public void initialize() {
        startTime = System.currentTimeMillis();
        liftMotor.set(speed);
    }

    @Override
    public void execute() {
        // No additional execution needed
    }

    @Override
    public boolean isFinished() {
        return System.currentTimeMillis() - startTime >= duration;
    }

    @Override
    public void end(boolean interrupted) {
        liftMotor.set(0); // Stop the motor
    }

}
