package com.hcmus.mobilappsocialnetworkingimage.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.hcmus.mobilappsocialnetworkingimage.activity.loginActivity;
import com.hcmus.mobilappsocialnetworkingimage.model.userModel;

public class firebaseMethods {

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;

    private Context mContext;

    public firebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mContext = context;

        if (mAuthListener != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    public boolean checkIfUserExists(String username, DataSnapshot dataSnapshot) {
        userModel user = new userModel();
        for (DataSnapshot ds : dataSnapshot.child(userID).getChildren()) {
            user.setUsername(ds.getValue(userModel.class).getUsername());
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void registerNewUser(String email, String password, String username) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(mContext, "Please verify your email", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, loginActivity.class);
                        mContext.startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "Failed to send email", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(mContext, "Register account failed, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
