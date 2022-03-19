package com.hcmus.mobilappsocialnetworkingimage;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hcmus.mobilappsocialnetworkingimage.utils.firebaseMethods;

public class registerActivity extends AppCompatActivity {
    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirmPwd;
    private EditText mUserName;
    private Button registerButton;
    private Button already_have;
    private FirebaseAuth mAuth;
    private firebaseMethods firebaseMethods;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = registerActivity.this;
        firebaseMethods = new firebaseMethods(this);
        mEmail= findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mConfirmPwd = findViewById(R.id.confirm_password);
        mUserName = findViewById(R.id.username);
        EditText[] editTexts = { mEmail, mPassword, mConfirmPwd, mUserName };
        registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(view -> init());

        already_have = findViewById(R.id.already_have);
        already_have.setOnClickListener(view -> super.onBackPressed());

        for (EditText editText : editTexts) {
            editText.setOnKeyListener((v, keyCode, event) -> {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    init();
                    return true;
                }
                return false;
            });
        }
    }

    void init(){
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String confirmPwd = mConfirmPwd.getText().toString();
        String username = mUserName.getText().toString();

        if(TextUtils.isEmpty(email)) {
            mEmail.setError("Email cannot be empty");
            mEmail.requestFocus();
        } else if(TextUtils.isEmpty(password)) {
            mPassword.setError("Password cannot be empty");
            mPassword.requestFocus();
        } else if(TextUtils.isEmpty(confirmPwd)) {
            mConfirmPwd.setError("Password cannot be empty");
            mConfirmPwd.requestFocus();
        } else if(TextUtils.isEmpty(username)) {
            mUserName.setError("Username cannot be empty");
            mUserName.requestFocus();
        } else if(!TextUtils.equals(password, confirmPwd)) {
            mConfirmPwd.setError("Confirmed password doesn't match");
            mConfirmPwd.requestFocus();
        } else if(mUserName.getText().toString().indexOf('@')!=-1){
            mUserName.setError("Username cannot have character '@'");
            mUserName.requestFocus();
        }
        else {
//            if (firebaseMethods.checkIfUserExists(username, dataSnapshot){
//                Toast.makeText(registerActivity.this, "Account is not exist", Toast.LENGTH_LONG).show();
//                return;
//            }
           firebaseMethods.registerNewUser(email, password, username);
        }
    }
}

/* ------------------------------------------- Firebase ----------------------------------------------------  */