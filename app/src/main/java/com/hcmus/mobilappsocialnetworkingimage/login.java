package com.hcmus.mobilappsocialnetworkingimage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class login extends AppCompatActivity {
    EditText username;
    EditText password;
    Button loginButton;
    Button register;
    FirebaseAuth mAuth;
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    Context context;

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

    @Override
    public void onBackPressed() {
        this.finish();
        MainActivity.fa.finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();

        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(view ->{
            authentication();
        });

        //Event when input enter to edit texts
        EditText[] editTexts={username,password};
        for(int i=0;i<editTexts.length;i++) {
            editTexts[i].setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // If the event is a key-down event on the "enter" button
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        // Perform action on key press
                        authentication();
                        return true;
                    }
                    return false;
                }
            });
        }
        register = findViewById(R.id.register);
        register.setOnClickListener(view ->{
            startActivity(new Intent(this,register.class));
        });
    }



    void authentication(){
        String emailC = username.getText().toString();
        String passwordC = password.getText().toString();

        if(TextUtils.isEmpty(emailC)){
            username.setError("Email cannot be empty");
            username.requestFocus();
        }
        else
        if(TextUtils.isEmpty(passwordC)){
            password.setError("Password cannot be empty");
            password.requestFocus();
        }
        else{
            mAuth.signInWithEmailAndPassword(emailC,passwordC).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        if(mAuth.getCurrentUser().isEmailVerified()) {
                            Toast.makeText(login.this, "Successful", Toast.LENGTH_SHORT).show();
                            login.this.finish();

                        }
                        else{
                            Toast.makeText(login.this, "Account is not exist", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(login.this,"Authentication error: " + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

}