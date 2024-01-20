package org.firstinspires.ftc.teamcode.opencv;

import org.opencv.core.Mat;
import org.openftc.easyopencv.*;

public class samplePipeline1 extends OpenCvPipeline {
    private String output = "nothing";

    public samplePipeline1() {

    }

    // Mat is the image matrix that should be processed.
    @Override
    public Mat processFrame(Mat input) {
        output = "Sample Pipeline Is Running!";
        return input;
    }

    public String getOutput() {
        return output;
    }
}
