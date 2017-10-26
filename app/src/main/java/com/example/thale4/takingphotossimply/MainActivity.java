package com.example.thale4.takingphotossimply;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;
    private static final int REQUEST_THUMBNAIL_IMAGE = 1;
    private static final int REQUEST_SAVE_IMAGE = 2;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.imageView);
    }

    public void onThumbnailClick(View view)
    {
        dispatchTakeThumbnailIntent();
    }

    private void dispatchTakeThumbnailIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_THUMBNAIL_IMAGE);
        }
    }

    public void onSaveClick(View view)
    {
        dispatchTakeAndSavePictureIntent();
    }

    private void dispatchTakeAndSavePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.thale4.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_SAVE_IMAGE);
            }
        }
    }

    // Get thumbnail for image and display to ImageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_THUMBNAIL_IMAGE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }
        else if (requestCode == REQUEST_SAVE_IMAGE)
        {
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            mImageView.setImageBitmap(bitmap);
            Toast.makeText(getApplicationContext(), "Image saved to device!", Toast.LENGTH_LONG).show();
        }
    }

    // Creates image file name with a simply time stamp
    protected File createImageFile() throws IOException {
        File pictureDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_.jpg";
        File newfile = new File(pictureDirectory, imageFileName);

        mCurrentPhotoPath = newfile.getAbsolutePath();
        Log.d("test", mCurrentPhotoPath);

        return newfile;
    }


}