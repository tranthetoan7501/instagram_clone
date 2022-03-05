package com.hcmus.mobilappsocialnetworkingimage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

public class edit_profile extends AppCompatActivity{
    FirebaseAuth mAuth;
    ImageButton back, ok;
    User user;
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    EditText username,about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        back=(ImageButton) findViewById(R.id.edit_back);
        ok=(ImageButton) findViewById(R.id.edit_ok);
        username=findViewById(R.id.edit_username);
        about=findViewById(R.id.edit_about);

        user= (User) getIntent().getSerializableExtra("USER");

        username.setText(user.getUsername());
        about.setText(user.getAbout());

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

    @Override
    protected void onStart() {
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();

    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}