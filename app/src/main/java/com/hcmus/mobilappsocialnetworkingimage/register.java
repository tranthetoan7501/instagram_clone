package com.hcmus.mobilappsocialnetworkingimage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    EditText username;
    EditText password;
    EditText confirm_password;
    Button registerButton;
    ImageButton previous;
    Button already_have;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);
        registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(view ->{
            createAccount();
        });
        previous = findViewById(R.id.previous);
        already_have = findViewById(R.id.already_have);
        already_have.setOnClickListener(view ->{
            super.onBackPressed();
        });
    }


    void createAccount(){
        String email = username.getText().toString()+"@gmail.com";
        String pass = password.getText().toString();
        String confirmPassword = confirm_password.getText().toString();

        if(TextUtils.isEmpty(email)){
            username.setError("Email cannot be empty");
            username.requestFocus();
        }
        else
        if(TextUtils.isEmpty(pass)){
            password.setError("Password cannot be empty");
            password.requestFocus();
        }
        else
        if(TextUtils.isEmpty(confirmPassword)){
            confirm_password.setError("Password cannot be empty");
            confirm_password.requestFocus();
        }
        else
        if(!TextUtils.equals(pass,confirmPassword)){
            confirm_password.setError("Confirmed password doesn't match");
            confirm_password.requestFocus();
        }
        else{
            mAuth.createUserWithEmailAndPassword(email,confirmPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(register.this,"Successful",Toast.LENGTH_SHORT).show();
//                        String userID = mAuth.getCurrentUser().getUid();
//                        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
//                        Map<String,Object> infor = new HashMap<>();
//                        infor.put("email",email);
//                        infor.put("name","Anonymous");
//                        infor.put("address","Updating...");
//                        infor.put("phoneNumber","Updating...");
//                        infor.put("image","https://i.ibb.co/L5knT0t/sample2.jpg");
//                        documentReference.set(infor).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Log.d("Tag","Success");
//                            }
//                        });
                        register.super.onBackPressed();
                    }
                    else{
                        Toast.makeText(register.this,"Registration error: "+ task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}