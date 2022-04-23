package com.hcmus.mobilappsocialnetworkingimage.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.model.userAccountSettingsModel;
import com.hcmus.mobilappsocialnetworkingimage.photoEditor.EditImageActivity;
import com.hcmus.mobilappsocialnetworkingimage.utils.firebaseMethods;
import com.hcmus.mobilappsocialnetworkingimage.utils.networkChangeListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Vector;

public class editProfileActivity extends Activity {
    FirebaseAuth mAuth;
    public static ImageView avatar;
    Button changePassword;
    ImageButton back, ok, camera;
    com.hcmus.mobilappsocialnetworkingimage.utils.networkChangeListener networkChangeListener=new networkChangeListener();
    EditText username,about;
    Bundle bundle;
    userAccountSettingsModel userAccountSettingsModel;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app/");
    public Bitmap imageProfile;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public static String photoFileName = "photo.jpg";
    static File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        back = findViewById(R.id.edit_back);
        ok = findViewById(R.id.edit_ok);
        camera = findViewById(R.id.camera);
        avatar = findViewById(R.id.avatar);
        username=findViewById(R.id.edit_username);
        about=findViewById(R.id.edit_about);
        changePassword=findViewById(R.id.change_password);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("type","change password");
//                bundle.putString("email", mAuth.getCurrentUser().getEmail());
                Intent intent1 = new Intent(getApplication(), navigationActivity.class);
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });

        bundle=getIntent().getExtras();
        userAccountSettingsModel= (userAccountSettingsModel) getIntent().getSerializableExtra("userAccountSettings");
        Picasso.get().load(userAccountSettingsModel.getProfile_photo()).into(avatar);
        username.setText(userAccountSettingsModel.getUsername());
        about.setText(userAccountSettingsModel.getDescription());
        camera.setOnClickListener(view -> selectImage());

        back.setOnClickListener(view -> editProfileActivity.this.finish());

        ok.setOnClickListener(view -> {
            if(username.getText().toString().equals(userAccountSettingsModel.getUsername())
                && about.getText().toString().equals(userAccountSettingsModel.getDescription())&& imageProfile == null){
                editProfileActivity.this.finish();
            } else {
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
        mAuth=FirebaseAuth.getInstance();
        DatabaseReference myRef=database.getReference("user_account_settings").child(mAuth.getUid());
        myRef.child("username").setValue(username.getText().toString());
        myRef.child("description").setValue(about.getText().toString());
        myRef=database.getReference("users").child(mAuth.getUid());
        myRef.child("username").setValue(username.getText().toString());
        if(imageProfile!=null){
            firebaseMethods.firebaseUploadBitmap(imageProfile,"editProfile");
        }
    }

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }
        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    public void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    photoFile = getPhotoFileUri(photoFileName);
                    Uri fileProvider = FileProvider.getUriForFile(editProfileActivity.this, "com.hcmus.mobilappsocialnetworkingimage.provider", photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        // Start the image capture intent to take photo
                        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                    }
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                 // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                imageProfile=takenImage;
                sendToEdit(imageProfile);

            }
            else if (requestCode == 2) {
                Uri selectedImage = data.getData();

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

            }
            else if(requestCode == SECOND_ACTIVITY_REQUEST_CODE){
                String path = data.getStringExtra("imagePath");
                imageProfile = BitmapFactory.decodeFile(path);
                imageProfile=Bitmap.createScaledBitmap(imageProfile,200,200,false);
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
        bundle.putByteArray("ImagePath", bytesArrayBmp);
        intent.putExtras(bundle);
        startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
    }
}
