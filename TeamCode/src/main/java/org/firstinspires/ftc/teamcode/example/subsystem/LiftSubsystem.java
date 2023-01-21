package org.firstinspires.ftc.teamcode.example.subsystem;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.StartEndCommand;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import org.firstinspires.ftc.teamcode.example.util.Junction;

@Config
public class LiftSubsystem extends SubsystemBase {
    private final MotorEx left, right;

    private int currentGoal;

    public static double kP = 1;
    public static double kI = 0;
    public static double kD = 0;
    private final PIDController controller = new PIDController(kP, kI, kD);

    public static int threshold = 20;

    private double output;

    public LiftSubsystem(MotorEx left, MotorEx right) {
        this.left = left;
        this.right = right;
    }

    public void setJunction(Junction junction){
        currentGoal = junction.getTick();
        controller.setSetPoint(currentGoal);
    }

    public boolean atTarget(){
            return left.getCurrentPosition() < currentGoal + threshold &&
                    left.getCurrentPosition() > currentGoal - threshold;
    }

    public int getCurrentGoal() {
        return currentGoal;
    }

    public double getOutput() {
        return output;
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