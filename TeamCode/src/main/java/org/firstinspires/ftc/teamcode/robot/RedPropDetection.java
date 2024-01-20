package org.firstinspires.ftc.teamcode.robot;


import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.apriltags.RedPropThreshold;
import org.firstinspires.ftc.vision.VisionPortal;
import org.opencv.core.Size;

@Autonomous(name = "Vision Test")
public class RedPropDetection extends LinearOpMode {
    private RedPropThreshold redPropThreshold;
    private VisionPortal portal;

    @Override
    public void runOpMode() throws InterruptedException {
        //redPropThreshold = new RedPropThreshold();
        portal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setCameraResolution(new Size(640, 480))
                .setCamera(BuiltinCameraDirection.BACK)
                .addProcessor(redPropThreshold)
                .build();
        waitForStart();
        telemetry.addData("Prop Position", redPropThreshold.getPropPosition());
        telemetry.update();             // Will output prop possition on Driver Station Console
    }
}
