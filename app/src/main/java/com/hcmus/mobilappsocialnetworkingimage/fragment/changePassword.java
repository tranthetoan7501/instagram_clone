package com.hcmus.mobilappsocialnetworkingimage.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hcmus.mobilappsocialnetworkingimage.R;


public class changePassword extends Fragment  implements View.OnClickListener{
    ImageButton previous, confirm;
    EditText currentPassword, newPassword, confirmPassword;
    String email;
    Bundle bundle = new Bundle();
    private FirebaseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_change_password, container, false);
        bundle=this.getArguments();
        email=bundle.getString("email");
        previous = view.findViewById(R.id.previous);
        previous.setOnClickListener(this);
        confirm=view.findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        currentPassword=view.findViewById(R.id.currentPassword);
        newPassword=view.findViewById(R.id.newPassword);
        confirmPassword=view.findViewById(R.id.confirmPassword);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.previous:
                getActivity().finish();
                break;
            case R.id.confirm:
                System.out.println(email);
                String curP= currentPassword.getText().toString();
                String newP = newPassword.getText().toString();
                String conP= confirmPassword.getText().toString();
                if(TextUtils.isEmpty(curP)){
                    currentPassword.setError("Cannot be empty");
                    currentPassword.requestFocus();
                }
                else
                if(TextUtils.isEmpty(newP)){
                    newPassword.setError("Cannot be empty");
                    newPassword.requestFocus();
                }
                else
                if(TextUtils.isEmpty(conP)){
                    confirmPassword.setError("Cannot be empty");
                    confirmPassword.requestFocus();
                }
                else
                if(!TextUtils.equals(newP, conP)){
                    confirmPassword.setError("Must be like new password");
                    confirmPassword.requestFocus();
                }
                else
                if(newP.length()<6){
                    newPassword.setError("At least 6 characters");
                    newPassword.requestFocus();
                }
                else
                {
                    user= FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential= EmailAuthProvider.getCredential(email,curP);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                user.updatePassword(newP).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(getContext(),"Changed password successfully", Toast.LENGTH_LONG).show();
                                            getActivity().finish();
                                        }else {
                                            Toast.makeText(getContext(),"Failed", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }else {
                                Toast.makeText(getContext(),"Authentication failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                break;
        }
    }
}