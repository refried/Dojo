package edu.gatech.cc.pfunk.blobifyme;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.opencv.core.Mat;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class BlobifyMeApp extends Activity {

    private Uri uri;
    private ImageView imageView;
    private Uri blobUri;
    private Uri currentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            uri = savedInstanceState.getParcelable("imageUri");
            blobUri = savedInstanceState.getParcelable("blobUri");
            currentUri = savedInstanceState.getParcelable("currentUri");
        }
            
        setContentView(R.layout.activity_blobify_me_app);
        
        Button button = (Button) findViewById(R.id.button1);
        imageView = (ImageView) findViewById(R.id.imageView1);
        button.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        
        imageView.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                toggleImage();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        
        outState.putParcelable("imageUri", uri);
        outState.putParcelable("blobUri", blobUri);
        outState.putParcelable("currentUri",currentUri);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.blobify_me_app, menu);
        return true;
    }

    public void takePicture() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        uri = MediaFileUtil.getOutputMediaFileUri(MediaFileUtil.MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        startActivityForResult(intent, 1);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            imageView.setImageURI(uri);
            currentUri = uri;
        }
        try {
            InputStream imageInput = getContentResolver().openInputStream(uri);
            Bitmap imageMap = BitmapFactory.decodeStream(imageInput);
            Mat imageMat = BlobDetection.extractColorBlob(imageMap);
            blobUri = MediaFileUtil.getOutputMediaFileUri(MediaFileUtil.MEDIA_TYPE_IMAGE);
            OpenCVUtil.matToFile(this, imageMat, blobUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        OpenCVUtil.initOpenCV(this);
        if (currentUri != null){
            imageView.setImageURI(currentUri);
        }
    }
    
    public void toggleImage(){
        if(currentUri.equals(uri)){
            currentUri = blobUri;
        }else{
            currentUri = uri;
        }
        
        imageView.setImageURI(currentUri);
    }
}

