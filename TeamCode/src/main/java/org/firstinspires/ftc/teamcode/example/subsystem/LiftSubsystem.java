package org.firstinspires.ftc.teamcode.example.subsystem;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.FunctionalCommand;
import com.arcrobotics.ftclib.command.StartEndCommand;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import org.firstinspires.ftc.teamcode.example.util.Junction;

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

    public static double kP = 1;
    public static double kI = 0;
    public static double kD = 0;
    private final PIDController controller = new PIDController(kP, kI, kD);

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

    public void setJunction(Junction junction){
        currentGoal = junction;
        switch (junction) {
            case NONE:
                currentTarget = none;
                controller.setSetPoint(none);
                break;
            case GROUND:
                currentTarget = ground;
                controller.setSetPoint(ground);
                break;
            case LOW:
                currentTarget = low;
                controller.setSetPoint(low);
                break;
            case MEDIUM:
                currentTarget = medium;
                controller.setSetPoint(medium);
                break;
            case HIGH:
                currentTarget = high;
                controller.setSetPoint(high);
                break;
        }
    }

    public boolean atTarget(){
        switch(currentGoal){
            case NONE:
                return left.getCurrentPosition()<none + threshold && left.getCurrentPosition()>none - threshold;
            case GROUND:
                return left.getCurrentPosition()<ground + threshold && left.getCurrentPosition()>ground - threshold;
            case LOW:
                return left.getCurrentPosition()<low + threshold && left.getCurrentPosition()>low - threshold;
            case MEDIUM:
                return left.getCurrentPosition()<medium + threshold && left.getCurrentPosition()>medium - threshold;
            case HIGH:
                return left.getCurrentPosition()<high + threshold && left.getCurrentPosition()>high - threshold;
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

    public Command goTo(Junction junction) {
        return new StartEndCommand(
                () -> setJunction(junction),
                this::atTarget,
                this
        );
    }

    @Override
    public void periodic() {
            output = controller.calculate(right.getCurrentPosition());
            left.set(output);
            right.set(output);
    }
}
