package org.firstinspires.ftc.teamcode.example.opmode;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.example.lift.SetJunction;
import org.firstinspires.ftc.teamcode.example.util.Junction;

import java.util.Arrays;
import java.util.List;

// import statically so that Gamepad.Button does not have to be repeated
import static com.arcrobotics.ftclib.gamepad.GamepadKeys.Button.*;

@TeleOp
public class MainOpMode extends BaseOpMode {

    @Override
    public void initialize() {
        super.initialize();

        gb2(LEFT_BUMPER).toggleWhenPressed(claw.runGrabCommand(), claw.runReleaseCommand());
        gb2(A).whenPressed(lift.goTo(Junction.NONE));
        gb2(X).whenPressed(lift.goTo(Junction.LOW));
        gb2(B).whenPressed(lift.goTo(Junction.MEDIUM));
        gb2(Y).whenPressed(lift.goTo(Junction.HIGH));


        register(drive, lift, claw);
        drive.setDefaultCommand(drive.robotCentric(gamepadEx1::getLeftX,
                gamepadEx1::getLeftY, gamepadEx1::getRightX));
    }
}
