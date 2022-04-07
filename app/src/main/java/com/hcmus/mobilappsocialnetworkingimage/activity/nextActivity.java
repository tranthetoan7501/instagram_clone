package com.hcmus.mobilappsocialnetworkingimage.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.utils.firebaseMethods;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class nextActivity extends AppCompatActivity {
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 2;
    private ImageView imageShare;
    private EditText descriptionText;
    private ImageView backButton;
    private TextView shareButton;
    private firebaseMethods firebaseMethods;
    private Bitmap bitmap;
    private Intent intent;
    private ArrayList<String> imgUrls = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        init();

        backButton.setOnClickListener(v -> {
            finish();
        });

        setImage();

        shareButton.setOnClickListener(v -> {
            imageShare.setDrawingCacheEnabled(true);
            imageShare.buildDrawingCache();
            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            if (intent.hasExtra(getString(R.string.selected_image))){
                firebaseMethods.uploadNewPhoto(imgUrls, null, descriptionText.getText().toString(), myDateObj.format(myFormatObj), descriptionText.getText().toString());

            } else {
                firebaseMethods.uploadNewPhoto(null, bitmap, descriptionText.getText().toString(), myDateObj.format(myFormatObj), descriptionText.getText().toString());
            }
            finish();
            Intent intent = new Intent(nextActivity.this, mainActivity.class);
            startActivity(intent);
        });
    }

    void init() {
        imageShare = findViewById(R.id.imageShare);
        descriptionText = findViewById(R.id.descriptionText);
        backButton = findViewById(R.id.back_button);
        shareButton = findViewById(R.id.tvShare);
        firebaseMethods = new firebaseMethods(this);
    }

    void setImage(){

        intent = getIntent();

        if (intent.hasExtra(getString(R.string.selected_image))){
            imgUrls = intent.getStringArrayListExtra(getString(R.string.selected_image));
            Log.d("nextActivity", "setImage: " + imgUrls.get(0));
            Uri uri = Uri.parse(imgUrls.get(0));
            imageShare.setImageURI(uri);
        }
        else {
            bitmap = intent.getParcelableExtra("imageBitmap");
            imageShare.setImageBitmap(bitmap);
        }
    }
}
