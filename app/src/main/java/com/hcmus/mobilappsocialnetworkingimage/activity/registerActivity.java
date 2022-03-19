package com.hcmus.mobilappsocialnetworkingimage.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.utils.firebaseMethods;

public class registerActivity extends AppCompatActivity {
    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirmPwd;
    private EditText mUserName;
    private Button registerButton;
    private Button already_have;

    private firebaseMethods firebaseMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Context mContext = registerActivity.this;
        firebaseMethods = new firebaseMethods(mContext);
        initWidgets();
        init();
    }

    private void initWidgets() {
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mConfirmPwd = findViewById(R.id.confirm_password);
        mUserName = findViewById(R.id.username);
        registerButton = findViewById(R.id.register);
        already_have = findViewById(R.id.already_have);
    }
    private void init(){
        already_have.setOnClickListener(view -> super.onBackPressed());
        registerButton.setOnClickListener(view -> {
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
                firebaseMethods.registerWithEmail(email, password, username);
            }
        });
    }
}



