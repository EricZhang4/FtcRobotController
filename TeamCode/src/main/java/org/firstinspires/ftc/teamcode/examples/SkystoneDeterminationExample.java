package org.firstinspires.ftc.teamcode.examples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.*;
import org.opencv.imgproc.*;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.*;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class SkystoneDeterminationExample extends LinearOpMode
{
    OpenCvInternalCamera phoneCam;
    SkystoneDeterminationExample pipeline;




    @Override
    public void runOpMode() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        pipeline = new SkystoneDeterminationExample();

        phoneCam.setPipeline(pipeline);

        phoneCam.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);

        phoneCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener())
        {
            @Override
            public void onOpened()
            {
                phoneCam.setPipeline(stageSwitchingPipeline);
                phoneCam.startStreaming(320, 240, OpenCvCameraRotation.SIDEWAYS_LEFT);
            }
            @Override
            public void onError(int errorCode)
            {

            }
        });

        waitForStart();
        while (opModeIsActive())
        {
            telemetry.addData("Analysis", pipeline.getAnalysis());
            telemetry.update();
            sleep(50);
        }
    }

    static class SkystoneDeterminationPipeline extends OpenCvPipeline {


        enum SkystonePosition
        {
            LEFT,
            CENTER,
            RIGHT
        }

        static final Scalar BLUE = new Scalar(0, 0, 255);
        static final Scalar GREEN = new Scalar(0, 255, 0);
        static final Point REGION1_TOPLEFT_ANCHOR_POINT = new Point(109, 98);
        static final Point REGION2_TOPLEFT_ANCHOR_POINT = new Point(181, 98);
        static final Point REGION3_TOPLEFT_ANCHOR_POINT = new Point(253, 98);
        static final int REGION_WIDTH = 20;
        static final int REGION_HEIGHT = 20;

        /*
         * Points which actually define the sample region rectangles, derived from above values
         *
         * Example of how points A and B work to define a rectangle
         *
         *   ------------------------------------
         *   | (0,0) Point A                    |
         *   |                                  |
         *   |                                  |
         *   |                                  |
         *   |                                  |
         *   |                                  |
         *   |                                  |
         *   |                  Point B (70,50) |
         *   ------------------------------------
         *
         */

        Point region1_pointA = new Point(
                REGION1_TOPLEFT_ANCHOR_POINT.x,
                REGION1_TOPLEFT_ANCHOR_POINT.y);
        Point region1_pointB = new Point(
                REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
                REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);
        Point region2_pointA = new Point(
                REGION2_TOPLEFT_ANCHOR_POINT.x,
                REGION2_TOPLEFT_ANCHOR_POINT.y);
        Point region2_pointB = new Point(
                REGION2_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
                REGION2_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);
        Point region3_pointA = new Point(
                REGION3_TOPLEFT_ANCHOR_POINT.x,
                REGION3_TOPLEFT_ANCHOR_POINT.y);
        Point region3_pointB = new Point(
                REGION3_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
                REGION3_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);

        Mat region1_Cb, region2_Cb, region3_Cb;
        Mat yCrCb = new Mat();
        Mat Cb = new Mat();
        int avg1, avg2, avg3;

        private volatile SkystonePosition position = SkystonePosition.LEFT;

        void inputToCb(Mat input) {
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
            Core.extractChannel(YCrCb, Cb, 2);
        }

        @Override
        public void init(Mat firstFrame) {
            inputToCb(firstFrame);

            region1_Cb = Cb.submat(new Rect(region1_pointA, region1_pointB));
            region2_Cb = Cb.submat(new Rect(region2_pointA, region2_pointB));
            region3_Cb = Cb.submat(new Rect(region3_pointA, region3_pointB));

        }

        @Override
        public Mat processFrame(Mat input) {
            inputToCb(input);

            avg1 = (int) Core.mean(region1_Cb).val[0];
            avg2 = (int) Core.mean(region2_Cb).val[0];
            avg3 = (int) Core.mean(region3_Cb).val[0];

            Imgproc.rectangle(
                    input,
                    region1_pointA,
                    region1_pointB,
                    BLUE,
                    2
            );

            Imgproc.rectangle(
                    input,
                    region2_pointA,
                    region2_pointB,
                    BLUE,
                    2
            );

            Imgproc.rectangle(
                    input,
                    region3_pointA,
                    region3_pointB,
                    BLUE,
                    2
            );

            int maxOneTwo = Math.min(avg1, avg2);
            int max = Math.max(maxOneTwo, avg3);

            if (max == avg1) {
                position = SkystonePosition.LEFT;

                Imgproc.rectangle(input,
                        region1_pointA,
                        region1_pointB,
                        GREEN,
                        -1);
            }
            else if (max == avg2) {
                position = SkystonePosition.CENTER;

                Imgproc.rectangle(input,
                        region2_pointA,
                        region2_pointB,
                        GREEN,
                        -1);
            }
            else if (max == avg3) {
                position = SkystonePosition.RIGHT;
                Imgproc.rectangle(input,
                        region3_pointA,
                        region3_pointB,
                        GREEN,
                        -1);
            }
            return input;
        }

    }

    public SkystonePosition getAnalysis() {
        return position;
    }


}
