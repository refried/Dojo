package edu.gatech.cc.pfunk.blobifyme;

import java.io.FileNotFoundException;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;

/**
 * Blob detection code, using Open CV to mask and process the image.
 * 
 * @author Jesse Rosalia
 *
 */
public class BlobDetection {

    // values discovered to work with green Android folder through trial and error
    
    private static final Scalar greenLower = new Scalar(60, 50, 50);
            
    private static final Scalar greenUpper = new Scalar(80, 255, 255);


    /**
     * 
     * @return
     * @throws FileNotFoundException
     */
    public static Mat getBinary(Bitmap srcImg, Scalar lowHsv, Scalar highHsv) {
        //convert the bitmap to a matrix
        Mat mRgba = new Mat();
        Utils.bitmapToMat(srcImg, mRgba);
        
        //and convert the rgba to HSV
        Mat HSV = new Mat();
        Imgproc.cvtColor(mRgba, HSV, Imgproc.COLOR_RGB2HSV, 3);

        //and apply the mask
        Mat masked = new Mat();
        Core.inRange(HSV, lowHsv, highHsv, masked);

        //and return the resulting matrix
        return masked;
    }
    
    public static Mat extractColorBlob(Bitmap srcImg) {
        Mat bin = getBinary(srcImg, greenLower, greenUpper);
        
        //TODO 
        
        return bin;
    }
}
