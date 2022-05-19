package com.phani.facedetection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.phani.facedetection.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private final int IMAGE_CAPTURE = 951;
    private InputImage curImage;
    public static final int REQUEST_PERMISSION_CAMERA = 123;
    private Bitmap currentImageTaken;
    private FaceDetector curDetector;
    private ActivityMainBinding mBinding;
    private boolean isPictureToBeTaken = true;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        FaceDetectorOptions highAccuracyOpts = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .setMinFaceSize(0.15f)
                .enableTracking()
                .build();
        curDetector = FaceDetection.getClient(highAccuracyOpts);

        manageVisibilities(isPictureToBeTaken);

        mBinding.takePictureBtn.setOnClickListener(view -> {
            if ((ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, IMAGE_CAPTURE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                        REQUEST_PERMISSION_CAMERA);
            }
        });

        mBinding.removePictureBtn.setOnClickListener(view -> {
            mBinding.pictureTakenIV.setImageResource(R.mipmap.ic_launcher_round);
            manageVisibilities(true);
        });
        mBinding.faceCountBtn.setOnClickListener(view -> detectNumberOfFacesInImage(currentImageTaken));
        mBinding.detectFacesBtn.setOnClickListener(view -> detectFacesInImage(currentImageTaken));
    }

    private void detectNumberOfFacesInImage(Bitmap bitmap) {
        curImage = InputImage.fromBitmap(bitmap, 0);
        curDetector.process(curImage).addOnSuccessListener(faces -> {
            int numberOfFaces = 0;

            for (Face ignored : faces) {
                numberOfFaces++;
            }

            Bundle curBundle = new Bundle();
            curBundle.putInt("NUMBER_OF_FACES_DETECTED", numberOfFaces);
            DialogFragment resultDialog = new OutputDialog();
            resultDialog.setArguments(curBundle);
            resultDialog.setCancelable(false);
            resultDialog.show(getSupportFragmentManager(), "OUTPUT_SHOW");
            Toast.makeText(this, "Faces Detected", Toast.LENGTH_SHORT).show();
        });
    }

    private void detectFacesInImage(Bitmap bitmap) {
        curImage = InputImage.fromBitmap(bitmap, 0);
        mBinding.pictureTakenIV.setImageBitmap(bitmap);
        try {
            Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(mutableBitmap);

            FaceDetectorOptions highAccuracyOpts =
                    new FaceDetectorOptions.Builder()
                            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                            .build();

            FaceDetector detector = FaceDetection.getClient(highAccuracyOpts);

            detector.process(curImage).addOnSuccessListener(faces -> {
                for (Face face : faces) {
                    Rect bounds = face.getBoundingBox();

                    Paint p = new Paint();
                    p.setColor(Color.GREEN);
                    p.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(bounds, p);
                    mBinding.pictureTakenIV.setImageBitmap(mutableBitmap);
                }
            }).addOnFailureListener(e -> Log.e("Failure", e.getMessage()));
            Toast.makeText(this, "Faces Detected", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CAPTURE && resultCode == RESULT_OK) {
            assert data != null;
            Bundle extras = data.getExtras();
            currentImageTaken = (Bitmap) extras.get("data");
            mBinding.pictureTakenIV.setImageBitmap(currentImageTaken);
            isPictureToBeTaken = false;
            manageVisibilities(false);
        }
    }

    // set buttons
    private void manageVisibilities(boolean option) {
        if (option) {
            mBinding.takePictureBtn.setVisibility(View.VISIBLE);
            mBinding.removePictureBtn.setVisibility(View.INVISIBLE);
            mBinding.faceCountBtn.setVisibility(View.INVISIBLE);
            mBinding.detectFacesBtn.setVisibility(View.INVISIBLE);
        } else {
            mBinding.takePictureBtn.setVisibility(View.INVISIBLE);
            mBinding.removePictureBtn.setVisibility(View.VISIBLE);
            mBinding.faceCountBtn.setVisibility(View.VISIBLE);
            mBinding.detectFacesBtn.setVisibility(View.VISIBLE);
        }
    }

    // double tap to exit
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(() ->
                        doubleBackToExitPressedOnce = false,
                2000);
    }
}