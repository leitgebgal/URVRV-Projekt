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

                            original = createScoreMarker(original);

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

    public Mat createScoreMarker(Mat mat){
        Imgproc.rectangle(
                mat,                    //Matrix obj of the image
                new Point(130, 50),        //p1
                new Point(800, 680),       //p2
                new Scalar(102,207,219),     //Scalar object for color
                -1
        );
        Imgproc.rectangle(
                mat,                    //Matrix obj of the image
                new Point(132, 52),        //p1
                new Point(798, 678),       //p2
                new Scalar(0, 0, 0),     //Scalar object for color
                10
        );

        return mat;
    }

}