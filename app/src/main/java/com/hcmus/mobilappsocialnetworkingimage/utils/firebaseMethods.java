package com.hcmus.mobilappsocialnetworkingimage.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.activity.editProfileActivity;
import com.hcmus.mobilappsocialnetworkingimage.activity.loginActivity;
import com.hcmus.mobilappsocialnetworkingimage.activity.postStory;
import com.hcmus.mobilappsocialnetworkingimage.model.postModel;
import com.hcmus.mobilappsocialnetworkingimage.model.userAccountSettingsModel;
import com.hcmus.mobilappsocialnetworkingimage.model.userModel;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class firebaseMethods {

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;
    private StorageReference mStorageReference;

    private Context mContext;

    public firebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app");
        myRef = mFirebaseDatabase.getReference();
        mContext = context;
        mStorageReference = FirebaseStorage.getInstance().getReference();
        if (mAuthListener != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    public void registerWithEmail(String email, String password, String username) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userID = mAuth.getCurrentUser().getUid();

                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        addNewUser(email, username, "", "");
                        Toast.makeText(mContext, "Verification email sent", Toast.LENGTH_SHORT).show();
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

    public void addNewUser(String email, String username, String description, String website) {

        userModel user = new userModel(userID, "0", email, username.replace(" ", "."));

        myRef.child(mContext.getString(R.string.dbname_users)).child(userID).setValue(user);

        userAccountSettingsModel settingsModel = new userAccountSettingsModel(
                description,
                username,
                0,
                0,
                0,
                "https://picsum.photos/200",
                username.replace(" ", "."),
                website
        );

        myRef.child(mContext.getString(R.string.dbname_user_account_settings)).child(userID).setValue(settingsModel);
    }

    public static void firebaseUploadBitmap(Bitmap bitmap, String key) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();
        StorageReference imageStorage = FirebaseStorage.getInstance("gs://social-media-f92fc.appspot.com").getReference();

        if(key.equals("editProfile")) {
            StorageReference imageRef = imageStorage.child("profile_photos/" + FirebaseAuth.getInstance().getUid() + "/" + "imagePath");
            Task<Uri> urlTask = imageRef.putBytes(data).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return imageRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    DatabaseReference myRef = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app")
                            .getReference("user_account_settings")
                            .child(FirebaseAuth.getInstance().getUid());
                    myRef.child("profile_photo").setValue(downloadUri.toString());
                }
            });
        }
        else if(key.equals("postStory")){

            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            LocalDateTime myDateObj = LocalDateTime.now();
            StorageReference imageRef = imageStorage.child("story_photos/"+ FirebaseAuth.getInstance().getUid()+"/"+myDateObj.format(myFormatObj));
            Task<Uri> urlTask = imageRef.putBytes(data).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return imageRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    DatabaseReference myRef=FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app")
                            .getReference("user_stories")
                            .child(FirebaseAuth.getInstance().getUid());
                    myRef.child(myDateObj.format(myFormatObj)).setValue(downloadUri.toString());

                }
            });
        }
    }
    public void uploadNewPhoto(ArrayList<String> imgUrls, Bitmap bm, String caption, String date_created, String tags) {
        List<String> imgUrlListFirebase = new ArrayList<>();
        String timestamp;
        AtomicReference<String> user_id = new AtomicReference<>(FirebaseAuth.getInstance().getUid());

        if (bm == null) {
            for (int i = 0; i < imgUrls.size(); i++) {
                timestamp = String.valueOf(System.currentTimeMillis());

                StorageReference storageReference = mStorageReference
                        .child("photos/users/" + user_id + "/" + timestamp);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap bitmap = null;
                bitmap = BitmapFactory.decodeFile(imgUrls.get(i));

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = storageReference.putBytes(data);
                uploadTask.addOnFailureListener(exception -> {
                    Toast.makeText(mContext, "Failed to upload photo", Toast.LENGTH_SHORT).show();
                }).addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    imgUrlListFirebase.add(uri.toString());
                    if (imgUrlListFirebase.size() == imgUrls.size()) {
                        String post_id = myRef.child("user_photos").child(user_id.get()).push().getKey();
                        postModel post = new postModel(caption, date_created, imgUrlListFirebase, tags, post_id, user_id.get());
                        myRef.child("user_photos").child(user_id.get()).child(post_id).setValue(post);
                    }
            }));
        }
        } else {
            timestamp = String.valueOf(System.currentTimeMillis());
            StorageReference storageReference = mStorageReference
                    .child("photos/users/" + user_id + "/" + timestamp);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = storageReference.putBytes(data);
            uploadTask.addOnFailureListener(exception -> {
                Toast.makeText(mContext, "Failed to upload photo", Toast.LENGTH_SHORT).show();
            }).addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                imgUrlListFirebase.add(uri.toString());
                String post_id = myRef.child("user_photos").child(user_id.get()).push().getKey();
                postModel post = new postModel(caption, date_created, imgUrlListFirebase, tags, post_id, user_id.get());
                myRef.child("user_photos").child(user_id.get()).child(post_id).setValue(post);
            }));
        }
        DatabaseReference userRef = mFirebaseDatabase.getReference("user_account_settings");
        userRef.child(user_id.get()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userAccountSettingsModel userAccountSettingsModel = dataSnapshot.getValue(userAccountSettingsModel.class);
                userAccountSettingsModel.setPosts(userAccountSettingsModel.getPosts() + 1);
                userRef.child(user_id.get()).setValue(userAccountSettingsModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
