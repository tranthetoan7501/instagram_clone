package com.hcmus.mobilappsocialnetworkingimage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

public class forgotPassword extends Activity implements View.OnClickListener {
    ImageButton previous, confirm;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        previous=findViewById(R.id.previous_inFP);
        confirm=findViewById(R.id.confirm_inFP);
        email=findViewById(R.id.email_inFP);

        previous.setOnClickListener(this);
        confirm.setOnClickListener(this);
        email.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.previous_inFP:
                this.finish();
                break;
            case R.id.confirm_inFP:
                if(TextUtils.isEmpty(email.getText().toString())){
                    email.setError("Enter your registered email or username");
                    email.requestFocus();
                }
                else
                if(email.getText().toString().indexOf('@')!=-1) {
                    sendEmailResetPass(email.getText().toString());
                }
                else{
                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app/");
                    DatabaseReference myRef = database.getReference("account");
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Vector<String> userCheck=new Vector<>();
                            Vector<String> emailCheck=new Vector<>();
                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                userCheck.add(snapshot.child("username").getValue().toString());
                                emailCheck.add(snapshot.child("email").getValue().toString());
                            }
                            if(userCheck.contains(email.getText().toString())){
                                sendEmailResetPass(emailCheck.elementAt(userCheck.indexOf(email.getText().toString())));
                                return;
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                this.finish();
        }
    }
    private void sendEmailResetPass(String emailRegitered){
        FirebaseAuth.getInstance().sendPasswordResetEmail(emailRegitered)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(forgotPassword.this,"We have sent you instructions to reset your password. Please check your email",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(forgotPassword.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}