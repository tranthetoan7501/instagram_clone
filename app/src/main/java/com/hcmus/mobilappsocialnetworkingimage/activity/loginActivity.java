package com.hcmus.mobilappsocialnetworkingimage.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.utils.networkChangeListener;

public class loginActivity extends AppCompatActivity {
    private EditText mEmail;
    private EditText mPassword;
    private Button loginBtn;
    private Button registerBtn;
    private Button forgotPwdBtn;

    // Firebase
    private FirebaseAuth mAuth;
    private networkChangeListener networkChangeListener = new networkChangeListener();

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        mainActivity.fa.finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail = findViewById(R.id.emailEditText);
        mPassword = findViewById(R.id.password);
        forgotPwdBtn =findViewById(R.id.forget_password);
        loginBtn = findViewById(R.id.login_button);
        registerBtn = findViewById(R.id.register);

        mAuth = FirebaseAuth.getInstance();
        init();
    }

    private void init() {
        EditText[] editTexts ={ mEmail, mPassword};
        loginBtn.setOnClickListener(view ->{
            loginUser();
        });
        registerBtn.setOnClickListener(view ->{
            startActivity(new Intent(this, registerActivity.class));
        });
        forgotPwdBtn.setOnClickListener(view -> {
            startActivity(new Intent(this, forgotPasswordActivity.class));
        });

        for(EditText editText: editTexts){
            editText.setOnKeyListener((v, keyCode, event) -> {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    loginUser();
                    return true;
                }
                return false;
            });
        }
    }

    private void loginUser(){
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            mEmail.setError("Email or username cannot be empty");
            mEmail.requestFocus();
        }
        else if (TextUtils.isEmpty(password)){
            mPassword.setError("Password cannot be empty");
            mPassword.requestFocus();
        }
        else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    if (mAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(loginActivity.this, mainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(loginActivity.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(loginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}


