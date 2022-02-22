package com.hcmus.mobilappsocialnetworkingimage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    EditText username;
    EditText password;
    Button loginButton;
    Button register;
    FirebaseAuth mAuth;
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
        register = findViewById(R.id.register);
        register.setOnClickListener(view ->{
            startActivity(new Intent(this,register.class));
        });
    }

    void authentication(){
        String emailC = username.getText().toString()+"@gmail.com";
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
                        Toast.makeText(login.this,"Successful",Toast.LENGTH_SHORT).show();
                        login.super.onBackPressed();
                    }
                    else {
                        Toast.makeText(login.this,"Authentication error: " + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

}