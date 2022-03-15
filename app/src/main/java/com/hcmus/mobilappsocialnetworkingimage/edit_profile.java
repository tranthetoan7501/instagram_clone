package com.hcmus.mobilappsocialnetworkingimage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class edit_profile extends AppCompatActivity {
    FirebaseAuth mAuth;
    ImageButton back, ok;
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    EditText username,about;
    Bundle bundle;
    UserInfor userInfor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        back=(ImageButton) findViewById(R.id.edit_back);
        ok=(ImageButton) findViewById(R.id.edit_ok);
        username=findViewById(R.id.edit_username);
        about=findViewById(R.id.edit_about);

        bundle=getIntent().getExtras();
        userInfor= (UserInfor) getIntent().getSerializableExtra("userInfor");
        System.out.println(userInfor);
        username.setText(userInfor.getUsername());
        about.setText(userInfor.getAbout());
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
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app/");
                DatabaseReference myRef = database.getReference("account").child(bundle.getString("uid"));
                myRef.setValue(new UserInfor(username.getText().toString()
                        ,userInfor.getEmail()
                        ,about.getText().toString()
                        ,userInfor.getAvatar()));
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