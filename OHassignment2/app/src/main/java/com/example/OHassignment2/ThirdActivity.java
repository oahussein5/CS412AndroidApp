package com.example.OHassignment2;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;

public class ThirdActivity extends AppCompatActivity {

    private ImageView ivCaptured;
    private File photoFile;
    private Uri photoUri;

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && photoFile != null && photoFile.exists()) {
                    ivCaptured.setImageURI(null);      // force a reload of the same file
                    ivCaptured.setImageURI(photoUri);
                } else {
                    Toast.makeText(this, "No image captured", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        ivCaptured = findViewById(R.id.ivCaptured);
        Button btnCapture = findViewById(R.id.btnCaptureImage);

        File dir = new File(getCacheDir(), "images");
        dir.mkdirs();
        photoFile = new File(dir, "captured_image.jpg");
        photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);

        btnCapture.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            try {
                cameraLauncher.launch(cameraIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
