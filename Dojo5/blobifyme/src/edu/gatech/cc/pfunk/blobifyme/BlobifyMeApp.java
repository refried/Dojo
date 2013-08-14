package edu.gatech.cc.pfunk.blobifyme;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class BlobifyMeApp extends Activity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    private ImageView pictureView;
    private Button    takePictureBtn;

    private Uri fileUri;

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
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
