package org.firstinspires.ftc.teamcode.robot;



import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.robotcore.external.hardware.camera.*;
import org.firstinspires.ftc.robotcore.external.hardware.camera.*;
import org.firstinspires.ftc.teamcode.apriltags.*;
import org.firstinspires.ftc.vision.*;
import org.opencv.core.*;

@Autonomous(name = "Vision Test")
public class CameraTest {
    private RedPropThreshold redPropThreshold;
    private VisionPortal portal;

    @Override
    public void runOpMode() throws InterruptedException {
        redPropThreshold = new RedPropThreshold();
        portal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setCameraResolution(new Size(640, 480))
                .setCamera(BuiltinCameraDirection.BACK)
                .addProcessor(redPropThreshold)
                .build();
        waitForStart();
    }
}
