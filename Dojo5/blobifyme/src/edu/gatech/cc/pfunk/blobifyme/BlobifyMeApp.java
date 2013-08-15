package edu.gatech.cc.pfunk.blobifyme;

import java.io.InputStream;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class BlobifyMeApp extends Activity {

    private static final String TAG = BlobifyMeApp.class.getSimpleName();
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    private ImageView pictureView;
    private Button    takePictureBtn;

    private Uri fileUri;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
//                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blobify_me_app);
        this.pictureView = (ImageView) findViewById(R.id.picture_view);
        this.takePictureBtn = (Button) findViewById(R.id.btn_take_picture);
        this.takePictureBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                doTakePicture();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this, mLoaderCallback);
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.blobify_me_app, menu);
        return true;
    }

    protected void doTakePicture() {
     // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // create a file to save the image
        this.fileUri = MediaFileUtil.getOutputMediaFileUri(MediaFileUtil.MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, this.fileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //only set the pictureview if the image capture was successful
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode != RESULT_CANCELED) {
            pictureView.setImageURI(this.fileUri);
            try {
                InputStream is = getContentResolver().openInputStream(this.fileUri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                Mat mRgba = new Mat();
                Utils.bitmapToMat(bitmap, mRgba);
                Mat HSV = new Mat();
                
                Imgproc.cvtColor(mRgba, HSV, Imgproc.COLOR_RGB2HSV, 3);
                Core.inRange(HSV,  new Scalar(0, 100, 30), new Scalar(5, 255, 255), mRgba);
                System.out.println(HSV);
//                Imgproc.cvtColor(mRgba, mRgba, Imgproc.COLOR_GRAY2BGRA, 4); 
            } catch (Exception e) {
                
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
