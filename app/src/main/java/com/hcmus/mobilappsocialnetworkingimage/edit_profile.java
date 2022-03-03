package com.hcmus.mobilappsocialnetworkingimage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class edit_profile extends AppCompatActivity {
    ImageButton back, ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        back=(ImageButton) findViewById(R.id.edit_back);
        ok=(ImageButton) findViewById(R.id.edit_ok);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_profile.this.finish();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_profile.this.finish();
            }
        });

    }
}