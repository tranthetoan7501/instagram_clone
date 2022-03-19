package com.hcmus.mobilappsocialnetworkingimage.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hcmus.mobilappsocialnetworkingimage.R;

public class nextActivity extends AppCompatActivity {
    private ImageView imageShare;
    private EditText descriptionText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        imageShare = findViewById(R.id.imageShare); // get image
        descriptionText = findViewById(R.id.descriptionText); // get description

    }
}
