package edu.gatech.cc.pfunk.blobifyme;

import java.io.FileNotFoundException;
import java.io.OutputStream;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

/**
 * Utilities for working with OpenCV.
 * 
 * @author Jesse Rosalia
 *
 */
public class OpenCVUtil {
    
    private static final String TAG = OpenCVUtil.class.getSimpleName();
    protected static boolean initd = false;

    public static void initOpenCV(Context context) {
        BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(context) {
            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    case LoaderCallbackInterface.SUCCESS:
                        initd = true;
                        Log.i(TAG, "OpenCV loaded successfully");
                        break;
                    default:
                        super.onManagerConnected(status);
                        break;
                }
            }
        };

        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, context, mLoaderCallback);
    }
    
    public static boolean isInitd() {
        return initd;
    }

    public static void matToFile(Context context, Mat src, Uri outUri) throws FileNotFoundException {
        Bitmap dest = Bitmap.createBitmap(src.width(), src.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(src, dest);

        OutputStream out = context.getContentResolver().openOutputStream(outUri);
        dest.compress(Bitmap.CompressFormat.JPEG, 90, out);
    }

}
