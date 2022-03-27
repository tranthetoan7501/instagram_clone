package com.hcmus.mobilappsocialnetworkingimage.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.utils.firebaseMethods;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class nextActivity extends AppCompatActivity {
    private ImageView imageShare;
    private EditText descriptionText;
    private ImageView backButton;
    private TextView shareButton;
    private firebaseMethods firebaseMethods;

    private Intent intent;
    private String imgUrl;

    //
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

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
            Bitmap bitmap = ((BitmapDrawable) imageShare.getDrawable()).getBitmap();
            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            firebaseMethods.uploadNewPhoto(imgUrl, descriptionText.getText().toString(), myDateObj.format(myFormatObj), descriptionText.getText().toString());

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

        if(intent.hasExtra(getString(R.string.selected_image))){
            imgUrl = intent.getStringExtra(getString(R.string.selected_image));
            imageShare.setImageURI(Uri.parse(imgUrl));
        }
    }
}
