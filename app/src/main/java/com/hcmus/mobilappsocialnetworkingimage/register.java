package com.hcmus.mobilappsocialnetworkingimage;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    EditText email;
    EditText password;
    EditText confirm_password;
    EditText username;
    Button registerButton;
    Button already_have;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        email= findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);
        username=findViewById(R.id.username);

        EditText[] editTexts={email,password,confirm_password,username};

        registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(view ->{
            createAccount();
        });
        already_have = findViewById(R.id.already_have);
        already_have.setOnClickListener(view ->{
            super.onBackPressed();
        });
        //Event when input enter to edit texts
        for(int i=0;i<editTexts.length;i++) {
            editTexts[i].setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // If the event is a key-down event on the "enter" button
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        // Perform action on key press
                        createAccount();
                        return true;
                    }
                    return false;
                }
            });
        }
    }


    void createAccount(){
        String _email = email.getText().toString();
        String pass = password.getText().toString();
        String confirmPassword = confirm_password.getText().toString();
        String _username=username.getText().toString();

        if(TextUtils.isEmpty(_email)){
            email.setError("Email cannot be empty");
            email.requestFocus();
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
        if(TextUtils.isEmpty(_username)){
            username.setError("Username cannot be empty");
            username.requestFocus();
        }
        else
        if(!TextUtils.equals(pass,confirmPassword)){
            confirm_password.setError("Confirmed password doesn't match");
            confirm_password.requestFocus();
        }
        else{
            mAuth.createUserWithEmailAndPassword(_email,pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            addData();
                                            Toast.makeText(register.this, "Successfully, Please your email for verification", Toast.LENGTH_LONG).show();
                                            register.this.finish();
                                        }
                                        else {
                                            Toast.makeText(register.this, task.getException().getMessage()  , Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            }
                            else{
                                Toast.makeText(register.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
    //Put data to database
    public void addData(){
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("username", username.getText().toString());
        user.put("email", email.getText().toString());
        user.put("about","");
        user.put("avatar","https://thelifetank.com/wp-content/uploads/2018/08/avatar-default-icon.png");
//        db.collection("account")
//                .document(mAuth.getUid())
//                .set(user);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("account").child(mAuth.getUid());
        myRef.setValue(user);
    }
}