package org.firstinspires.ftc.teamcode.subsystem;

import android.util.Log;
import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.wpilibcontroller.ProfiledPIDController;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.trajectory.TrapezoidProfile;
import org.firstinspires.ftc.teamcode.util.Junction;

import java.util.function.DoubleSupplier;

@Config
public class LiftSubsystem extends SubsystemBase {
    private final MotorEx left, right;

    private Junction currentGoal;

    // tune
    public static int none = 0;
    public static int ground = 150;
    public static int low = 840; // 837
    public static int medium = 1200;
    public static int high = 1600;

    public static double kP = 0.01;
    public static double kI = 0;
    public static double kD = 0.0003;
    public static double kG = 0.1;
    public static double maxVelocity = 2000;
    public static double maxAcceleration = 2000;
    private final ProfiledPIDController controller = new ProfiledPIDController(kP, kI, kD,
            new TrapezoidProfile.Constraints(maxVelocity, maxAcceleration));

    public static double power = 1;
    public static int threshold = 20;

    public static double slowFactor = 1.5;

    private final DoubleSupplier doubleSupplier;

    private int currentTarget;

    private double output;

    public LiftSubsystem(MotorEx left, MotorEx right, DoubleSupplier doubleSupplier) {
        this.left = left;
        this.right = right;
        this.doubleSupplier = doubleSupplier;
    }

    public void update(){
        left.set(power);
        right.set(power);
    }

    public void setJunction(Junction junction){
        currentGoal = junction;
        switch (junction) {
            case NONE:
                currentTarget = none;
                controller.setGoal(none);
                break;
            case GROUND:
                currentTarget = ground;
                controller.setGoal(ground);
                break;
            case LOW:
                currentTarget = low;
                controller.setGoal(low);
                break;
            case MEDIUM:
                currentTarget = medium;
                controller.setGoal(medium);
                break;
            case HIGH:
                currentTarget = high;
                controller.setGoal(high);
                break;
        }
    }

    public boolean atTarget(){
        switch(currentGoal){
            case NONE:
                return left.getCurrentPosition()<none+ threshold && left.getCurrentPosition()>none- threshold;
            case GROUND:
                return left.getCurrentPosition()<ground+ threshold && left.getCurrentPosition()>ground- threshold;
            case LOW:
                return left.getCurrentPosition()<low+ threshold && left.getCurrentPosition()>low- threshold;
            case MEDIUM:
                return left.getCurrentPosition()<medium+ threshold && left.getCurrentPosition()>medium- threshold;
            case HIGH:
                return left.getCurrentPosition()<high+ threshold && left.getCurrentPosition()>high- threshold;
        }
        return false;
    }

    public Junction getCurrentJunction() {
        return currentGoal;
    }

    public double getOutput() {
        return output;
    }

    public int getCurrentTarget() {
        return currentTarget;
    }

    @Override
    public void periodic() {
        if(doubleSupplier.getAsDouble() != 0) {
            left.set(doubleSupplier.getAsDouble() / slowFactor);
            right.set(doubleSupplier.getAsDouble() / slowFactor);
            controller.setGoal(right.getCurrentPosition());
        } else {
            output = controller.calculate(right.getCurrentPosition()) + kG;
            left.set(output);
            right.set(output);
        }
    }
}
