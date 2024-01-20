package org.firstinspires.ftc.teamcode.opencv;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.opencv.Camera;
import org.firstinspires.ftc.teamcode.opencv.samplePipeline1;

@TeleOp(name = "Sample_TeleOP", group = "robot")
public class Test_teleop extends LinearOpMode{
    Camera camera = new Camera(hardwareMap);

    @Override
    public void runOpMOde() throws InterruptedException {
        camera.switchToFirstPipeline();
        telemetry.addLine("Status: Initalized");
        waitForStart();

        while (opModeIsActive()) {
            // OpMode receives the information from the pipeline class
            // to the camera module class
            telemetry.addLine(camera.getPipeline1Output());
            telemetry.update();
        }
    }
}
