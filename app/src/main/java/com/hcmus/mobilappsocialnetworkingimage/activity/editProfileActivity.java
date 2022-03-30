package com.hcmus.mobilappsocialnetworkingimage.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Constants;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.model.userModel;
import com.hcmus.mobilappsocialnetworkingimage.photoEditor.EditImageActivity;
import com.hcmus.mobilappsocialnetworkingimage.utils.networkChangeListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import com.hcmus.mobilappsocialnetworkingimage.model.userAccountSettingsModel;

import io.grpc.Context;

public class editProfileActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    public static ImageView avatar;
    ImageButton back, ok, camera;
    com.hcmus.mobilappsocialnetworkingimage.utils.networkChangeListener networkChangeListener=new networkChangeListener();
    EditText username,about;
    Bundle bundle;
    userAccountSettingsModel userAccountSettingsModel;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app/");
    Bitmap imageProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        back=(ImageButton) findViewById(R.id.edit_back);
        ok=(ImageButton) findViewById(R.id.edit_ok);
        camera=(ImageButton) findViewById(R.id.camera);
        avatar=(ImageView) findViewById(R.id.avatar);
        username=findViewById(R.id.edit_username);
        about=findViewById(R.id.edit_about);

        bundle=getIntent().getExtras();
        userAccountSettingsModel= (userAccountSettingsModel) getIntent().getSerializableExtra("userAccountSettings");


        Picasso.get().load(userAccountSettingsModel.getProfile_photo()).into(avatar);
        username.setText(userAccountSettingsModel.getUsername());
        about.setText(userAccountSettingsModel.getDescription());
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage();
            }
        });

        back.setOnClickListener(view -> editProfileActivity.this.finish());

        ok.setOnClickListener(view -> {
            if(username.getText().toString().equals(userAccountSettingsModel.getUsername())
                && about.getText().toString().equals(userAccountSettingsModel.getDescription())&& imageProfile==null){
                editProfileActivity.this.finish();
            }
            else {
                DatabaseReference myRef = database.getReference("user");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Vector<String> userCheck = new Vector<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            userCheck.add(snapshot.child("username").getValue().toString());
                        }
                        if (userCheck.contains(username.getText().toString())) {
                            username.setError("Username already exist!");
                            username.requestFocus();
                        } else {
                            setdata();
                            editProfileActivity.this.finish();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    
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

    private void setdata(){
        System.out.println(about.getText().toString());
        mAuth=FirebaseAuth.getInstance();
        DatabaseReference myRef=database.getReference("user_account_settings").child(mAuth.getUid());
        myRef.child("username").setValue(username.getText().toString());
        myRef.child("description").setValue(about.getText().toString());
        myRef=database.getReference("users").child(mAuth.getUid());
        myRef.child("username").setValue(username.getText().toString());
        if(imageProfile!=null){
            firebaseUploadBitmap(imageProfile);
        }

    }
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);

                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void firebaseUploadBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();
        StorageReference imageStorage = FirebaseStorage.getInstance("gs://social-media-f92fc.appspot.com").getReference();
        StorageReference imageRef = imageStorage.child("profile_photos/"+FirebaseAuth.getInstance().getUid()+"/"+"imagePath");
        Task<Uri> urlTask = imageRef.putBytes(data).continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return imageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                DatabaseReference myRef=database.getReference("user_account_settings").child(mAuth.getUid());
                myRef.child("profile_photo").setValue(downloadUri.toString());
            } else {
                Toast.makeText(editProfileActivity.this,"Change profile photo failed",Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageProfile=imageBitmap;
                sendToEdit(imageProfile);
                imageBitmap=Bitmap.createScaledBitmap(imageBitmap,200,200,true);
                avatar.setImageBitmap(imageBitmap);
            }
            else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                // h=1;
                //imgui = selectedImage;
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of image from gallery......******************.........", picturePath + "");
                imageProfile=thumbnail;
                sendToEdit(imageProfile);
                imageProfile=Bitmap.createScaledBitmap(thumbnail,200,200,true);
                avatar.setImageBitmap(imageProfile);
            }
            else if(requestCode==0){
                imageProfile=EditImageActivity.byteToBitmap(data.getByteArrayExtra("imagePath"));
                imageProfile=Bitmap.createScaledBitmap(imageProfile,200,200,true);
                avatar.setImageBitmap(imageProfile);
            }
        }
    }
    public void sendToEdit(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] bytesArrayBmp = baos.toByteArray();
        Intent intent=new Intent(getApplicationContext(), EditImageActivity.class);
        Bundle bundle=new Bundle();
        bundle.putByteArray("ImagePath",bytesArrayBmp);
        intent.putExtras(bundle);
        startActivityForResult(intent,SECOND_ACTIVITY_REQUEST_CODE);
    }
}
