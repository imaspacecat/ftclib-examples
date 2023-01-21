package org.firstinspires.ftc.teamcode.example.subsystem;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.FunctionalCommand;
import com.arcrobotics.ftclib.command.StartEndCommand;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import org.firstinspires.ftc.teamcode.example.util.Junction;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.DoubleSupplier;
import java.util.HashMap;

@Config
public class LiftSubsystem extends SubsystemBase {
    private final MotorEx left, right;

    private Junction currentGoal;

    EnumMap<Junction, Integer> encoderTicks = new EnumMap<>(Junction.class);

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

        // tune
        encoderTicks.put(Junction.NONE, 0);
        encoderTicks.put(Junction.GROUND, 150);
        encoderTicks.put(Junction.LOW, 840);
        encoderTicks.put(Junction.MEDIUM, 1200);
        encoderTicks.put(Junction.HIGH, 1600);
    }

    public void setJunction(Junction junction){
        currentGoal = junction;

        currentTarget = encoderTicks.get(junction);
        controller.setSetPoint(currentTarget);
    }

    public boolean atTarget(){
        if (currentGoal != null) {
            return left.getCurrentPosition() < currentTarget + threshold && left.getCurrentPosition() > currentTarget - threshold;
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
