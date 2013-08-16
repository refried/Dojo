package edu.gatech.cc.pfunk.blobifyme;

import java.io.InputStream;

import org.opencv.core.Mat;

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
    private static final int CHOOSE_FILE_RESULT_CODE = 200;

    private ImageView pictureView;
    private Button    takePictureBtn;

    private Uri fileUri;

    private Uri blobUri;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blobify_me_app);
        this.pictureView = (ImageView) findViewById(R.id.picture_view);
        this.pictureView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                toggleImageUri();
            }
            
        });
        this.takePictureBtn = (Button) findViewById(R.id.btn_take_picture);
        this.takePictureBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // doLoadPicture() //uncomment to load picture from gallary
                doTakePicture();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        // init Open CV
        // NOTE: this is an async operation...we'll be fine for now, but
        // be aware if you use this in another app.
        OpenCVUtil.initOpenCV(this);
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
    
    protected void doLoadPicture() {
        // create Intent to load a picture and return control the calling application
        Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
        // set the type of content to include all images
        intent2.setType("image/*");
        // start the get content Intent
        startActivityForResult(intent2, CHOOSE_FILE_RESULT_CODE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = null;
        this.imageUri = null;
        //only process if the image capture was successful
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode != RESULT_CANCELED) {
            uri = this.fileUri;
        } else if (requestCode == CHOOSE_FILE_RESULT_CODE && resultCode != RESULT_CANCELED) {
            uri = data.getData();
        }

        if (uri != null) {
            //this sets one of the image uris we can toggle through
            extractBlobIntoFile();
            toggleImageUri();

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Extract the blob into a file that can be displayed in the image view.
     */
    private void extractBlobIntoFile() {
        try {
            InputStream is = getContentResolver().openInputStream(this.fileUri);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            Mat masked = BlobDetection.extractColorBlob(bitmap);
            
            this.blobUri = MediaFileUtil.getOutputMediaFileUri(MediaFileUtil.MEDIA_TYPE_IMAGE);
            OpenCVUtil.matToFile(this, masked, this.blobUri);

        } catch (Exception e) {
            Log.wtf(TAG, e);
        }
    }

    /**
     * Toggle the image view between the camera image and the blob image
     * 
     */
    private void toggleImageUri() {
        if (this.imageUri == null || this.imageUri.equals(this.blobUri)) {
            this.imageUri = this.fileUri;
        } else {
            this.imageUri = this.blobUri;
        }
        pictureView.setImageURI(this.imageUri);
    }
}
