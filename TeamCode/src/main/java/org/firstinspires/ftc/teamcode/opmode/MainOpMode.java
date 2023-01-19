package org.firstinspires.ftc.teamcode.opmode;

import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.command.claw.Grab;
import org.firstinspires.ftc.teamcode.command.claw.Release;
import org.firstinspires.ftc.teamcode.command.drive.DriveRobotCentric;
import org.firstinspires.ftc.teamcode.command.drive.DriveSlowMode;
import org.firstinspires.ftc.teamcode.command.lift.SetJunction;
import org.firstinspires.ftc.teamcode.command.lift.UpdateLift;
import org.firstinspires.ftc.teamcode.util.Junction;

@TeleOp
public class MainOpMode extends BaseOpMode {
    private DriveRobotCentric robotCentricDrive;
    private DriveSlowMode slowMode;
    private UpdateLift updateLift;


    @Override
    public void initialize() {
        super.initialize();


        // drive
        robotCentricDrive = new DriveRobotCentric(drive, gamepadEx1::getLeftX,
                gamepadEx1::getLeftY, gamepadEx1::getRightX);

        slowMode = new DriveSlowMode(drive, gamepadEx1::getLeftX, gamepadEx1::getLeftY, gamepadEx1::getRightX);

//        updateLift = new UpdateLift(lift);

        gb1(GamepadKeys.Button.LEFT_BUMPER)
                .whileHeld(slowMode);

        gb2(GamepadKeys.Button.LEFT_BUMPER)
                .toggleWhenPressed(new Grab(claw), new Release(claw));
//
//        gb2(GamepadKeys.Button.LEFT_BUMPER)
//                .toggleWhenPressed(claw.runGrabCommand(), claw.runReleaseCommand());


        gb2(GamepadKeys.Button.A)
                .whenPressed(new SetJunction(lift, Junction.NONE));
        gb2(GamepadKeys.Button.X)
                .whenPressed(new SetJunction(lift, Junction.LOW));
        gb2(GamepadKeys.Button.B)
                .whenPressed(new SetJunction(lift, Junction.MEDIUM));
        gb2(GamepadKeys.Button.Y)
                .whenPressed(new SetJunction(lift, Junction.HIGH));




        register(drive, lift, claw);
        drive.setDefaultCommand(robotCentricDrive);
//        lift.setDefaultCommand(updateLift);

    }
}
