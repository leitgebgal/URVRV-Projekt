package com.example.projekt;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projekt.databinding.FragmentFirstBinding;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private Uri cam_uri;

    ActivityResultLauncher<Intent> startCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        //binding.imageHolder.setImageURI(cam_uri);

                        try {
                            Bitmap bitmap = Bitmap.createBitmap(MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), cam_uri));
                            binding.imageView2.setImageBitmap(bitmap);

                            Mat original = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1);
                            Utils.bitmapToMat(bitmap, original);

                            original = createScoreMarker(original, 222);

                            Bitmap or = Bitmap.createBitmap(original.cols(), original.rows(), Bitmap.Config.ARGB_8888);
                            Utils.matToBitmap(original, or);
                            binding.imageView2.setImageBitmap(or);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        OpenCVLoader.initDebug();
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
                cam_uri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cam_uri);

                startCamera.launch(cameraIntent);

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public Mat createScoreMarker(Mat mat, int score){
        // MIN SCORE = 0, MAX SCORE = 255
        // BLACK = 1, WHITE = 0

        if(score > 255) {
            score = 255;
        }
        else if(score < 0){
            score = 0;
        }

        //CONVERT SCORE TO BINARY STRING
        String conversion = Integer.toBinaryString(score);
        String result = String.format("%8s", conversion).replaceAll(" ", "0");

        // RECTANGLES
        // fill rectangle
        Imgproc.rectangle(
                mat,                    //Matrix obj of the image
                new Point(100, 100),        //p1
                new Point(2600, 2600),       //p2
                new Scalar(102,207,219),    //Scalar object for color
                -1
        );

        // border rectangle
        Imgproc.rectangle(
                mat,                    //Matrix obj of the image
                new Point(100, 100),        //p1
                new Point(2600, 2600),       //p2
                new Scalar(0, 0, 0),     //Scalar object for color
                10
        );

        // CIRCLES
        // red circle
        Imgproc.circle(
                mat,                    //Matrix obj of the image
                new Point(550, 550),
                300,
                new Scalar(255,0,0),
                -1
        );
        Imgproc.circle(
                mat,                    //Matrix obj of the image
                new Point(550, 550),
                300,
                new Scalar(0,0,0),
                10
        );

        // other circles
        //1
        if(result.charAt(7) == '1'){
            Imgproc.circle(
                    mat,                    //Matrix obj of the image
                    new Point(1350, 550),
                    300,
                    new Scalar(0,0,0),
                    -1
            );
        } else {
            Imgproc.circle(
                    mat,                    //Matrix obj of the image
                    new Point(1350, 550),
                    300,
                    new Scalar(255,255,255),
                    -1
            );
        }
        Imgproc.circle(
                mat,                    //Matrix obj of the image
                new Point(1350, 550),
                300,
                new Scalar(0,0,0),
                10
        );
        //2
        if(result.charAt(6) == '1'){
            Imgproc.circle(
                    mat,                    //Matrix obj of the image
                    new Point(2150, 550),
                    300,
                    new Scalar(0,0,0),
                    -1
            );
        } else {
            Imgproc.circle(
                    mat,                    //Matrix obj of the image
                    new Point(2150, 550),
                    300,
                    new Scalar(255,255,255),
                    -1
            );
        }

        Imgproc.circle(
                mat,                    //Matrix obj of the image
                new Point(2150, 550),
                300,
                new Scalar(0,0,0),
                10
        );
        //3
        if(result.charAt(5) == '1'){
            Imgproc.circle(
                    mat,                    //Matrix obj of the image
                    new Point(550, 1350),
                    300,
                    new Scalar(0,0,0),
                    -1
            );
        } else {
            Imgproc.circle(
                    mat,                    //Matrix obj of the image
                    new Point(550, 1350),
                    300,
                    new Scalar(255,255,255),
                    -1
            );
        }
        Imgproc.circle(
                mat,                    //Matrix obj of the image
                new Point(550, 1350),
                300,
                new Scalar(0,0,0),
                10
        );
        //4
        if(result.charAt(4) == '1'){
            Imgproc.circle(
                    mat,                    //Matrix obj of the image
                    new Point(1350, 1350),
                    300,
                    new Scalar(0,0,0),
                    -1
            );
        } else {
            Imgproc.circle(
                    mat,                    //Matrix obj of the image
                    new Point(1350, 1350),
                    300,
                    new Scalar(255,255,255),
                    -1
            );
        }

        Imgproc.circle(
                mat,                    //Matrix obj of the image
                new Point(1350, 1350),
                300,
                new Scalar(0,0,0),
                10
        );
        //5
        if(result.charAt(3) == '1'){
            Imgproc.circle(
                    mat,                    //Matrix obj of the image
                    new Point(2150, 1350),
                    300,
                    new Scalar(0,0,0),
                    -1
            );
        } else {
            Imgproc.circle(
                    mat,                    //Matrix obj of the image
                    new Point(2150, 1350),
                    300,
                    new Scalar(255,255,255),
                    -1
            );
        }
        Imgproc.circle(
                mat,                    //Matrix obj of the image
                new Point(2150, 1350),
                300,
                new Scalar(0,0,0),
                10
        );
        //6
        if(result.charAt(2) == '1'){
            Imgproc.circle(
                    mat,                    //Matrix obj of the image
                    new Point(550, 2150),
                    300,
                    new Scalar(0,0,0),
                    -1
            );
        } else {
            Imgproc.circle(
                    mat,                    //Matrix obj of the image
                    new Point(550, 2150),
                    300,
                    new Scalar(255,255,255),
                    -1
            );
        }

        Imgproc.circle(
                mat,                    //Matrix obj of the image
                new Point(550, 2150),
                300,
                new Scalar(0,0,0),
                10
        );
        //7
        if(result.charAt(1) == '1'){
            Imgproc.circle(
                    mat,                    //Matrix obj of the image
                    new Point(1350, 2150),
                    300,
                    new Scalar(0,0,0),
                    -1
            );
        } else {
            Imgproc.circle(
                    mat,                    //Matrix obj of the image
                    new Point(1350, 2150),
                    300,
                    new Scalar(255,255,255),
                    -1
            );
        }

        Imgproc.circle(
                mat,                    //Matrix obj of the image
                new Point(1350, 2150),
                300,
                new Scalar(0,0,0),
                10
        );
        //8
        if(result.charAt(0) == '1'){
            Imgproc.circle(
                    mat,                    //Matrix obj of the image
                    new Point(2150, 2150),
                    300,
                    new Scalar(0,0,0),
                    -1
            );
        } else {
            Imgproc.circle(
                    mat,                    //Matrix obj of the image
                    new Point(2150, 2150),
                    300,
                    new Scalar(255,255,255),
                    -1
            );
        }

        Imgproc.circle(
                mat,                    //Matrix obj of the image
                new Point(2150, 2150),
                300,
                new Scalar(0,0,0),
                10
        );
        return mat;
    }

}