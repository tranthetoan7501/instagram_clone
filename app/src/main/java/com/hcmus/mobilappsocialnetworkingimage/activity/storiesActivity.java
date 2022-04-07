package com.hcmus.mobilappsocialnetworkingimage.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.adapter.storiesAdapter;
import com.hcmus.mobilappsocialnetworkingimage.utils.networkChangeListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class storiesActivity extends Activity {
    ImageButton close;
    Vector<String> listName=new Vector<>();
    List<List<String>> listImage;
    Vector<String> listAvt=new Vector<>();
    LinearLayout listProgressBar;
    RelativeLayout relativeLayout;
    ImageButton person, more;
    ImageView image;
    TextView username;
    String currentUser;
    LinearLayout sheetStory;
    int pos;
    int temp=0;
    boolean key=true;
    com.hcmus.mobilappsocialnetworkingimage.utils.networkChangeListener networkChangeListener=new networkChangeListener();
    Vector<customProgress> t=new Vector<>();
    Thread thread1=new Thread(new Runnable() {
        @Override
        public void run() {
            while(temp<t.size()) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.get().load(listImage.get(pos).get(temp)).into(image);
                    }
                });
                t.get(temp).createThread();
                t.get(temp).thread.start();
                while(key||t.get(temp).status){

                }
                t.get(temp).progressStatus=t.get(temp).MAX;
                key=true;
                temp++;
            }
            goStories();

        }

    });


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

    private void openDialog() {
        t.get(temp).status=false;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(currentUser.equals(listName.get(pos))) {
            dialog.setContentView(R.layout.up_item_story_fragment_4host);
            LinearLayout delete = dialog.findViewById(R.id.delete);
            delete.setOnClickListener(v -> {
                DatabaseReference databaseReference= FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        .getReference("user_stories");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            if(snapshot.getKey().equals(FirebaseAuth.getInstance().getUid())){
                                for(DataSnapshot snapshot1:snapshot.getChildren()) {
                                    if (snapshot1.getValue().equals(listImage.get(pos).get(temp))) {
                                        snapshot1.getRef().removeValue();
                                        return;
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                dialog.dismiss();
                finish();
            });
        }
        else{
            dialog.setContentView(R.layout.up_item_story_fragment_4stranger);
        }

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        more.getLocationOnScreen(new int[2]);
        wlp.gravity = Gravity.TOP | Gravity.LEFT;
        wlp.x = more.getLeft() - more.getWidth() / 2;
        wlp.y = more.getTop() + more.getHeight() / 2;
        window.setAttributes(wlp);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                t.get(temp).status = true;
                t.get(temp).createThread();
                t.get(temp).thread.start();
            }
        });
        dialog.show();
        LinearLayout report=dialog.findViewById(R.id.report);
        report.setOnClickListener(v->{
            dialog.dismiss();
            t.get(temp).status = true;
            t.get(temp).createThread();
            t.get(temp).thread.start();

        });
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);
        person=findViewById(R.id.person);
        image=findViewById(R.id.image);
        username=findViewById(R.id.username);
        listProgressBar=findViewById(R.id.list_progress);
        sheetStory=findViewById(R.id.sheetStory);
        more=findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        currentUser=getIntent().getStringExtra("currentUser");
        pos=Integer.parseInt(getIntent().getStringExtra("position"));
        listName=new Vector<>(getIntent().getStringArrayListExtra("Name"));
        listImage= (List<List<String>>) getIntent().getSerializableExtra("Image");
        listAvt=new Vector<>(getIntent().getStringArrayListExtra("Avatar"));
        Picasso.get().load(listAvt.get(pos)).into(person);

        LayoutInflater layoutInflater = getLayoutInflater();
        for(int i=0;i<listImage.get(pos).size();i++){
            View view=layoutInflater.inflate(R.layout.custom_progressbar,null,false);
            ProgressBar progressBar=view.findViewById(R.id.progress_horizontal);
            customProgress cusP=new customProgress(progressBar);
            listProgressBar.addView(view,new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1));
            t.add(cusP);
        };

        username.setText(listName.get(pos));
        thread1.start();
        relativeLayout=findViewById(R.id.item);
        close=findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                t.get(temp).status=false;
            }
        });
        relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                t.get(temp).status=false;
                return false;
            }

        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(t.get(temp).status){
                    t.get(temp).status=false;
                    key=false;
                }
                else{
                    t.get(temp).status = true;
                    t.get(temp).createThread();
                    t.get(temp).thread.start();
                }
            }

        });
    }
    public void goStories(){
        Intent intent = new Intent(getApplicationContext(), storiesActivity.class);
        listImage.remove(pos);
        listName.remove(pos);
        listAvt.remove(pos);
        if(pos<listName.size()){
            intent.putExtra("position", String.valueOf(pos));
            intent.putExtra("currentUser",currentUser);
            intent.putStringArrayListExtra("Name", new ArrayList<String>(listName));
            //intent.putStringArrayListExtra("Image", new ArrayList<String>(listImage));
            intent.putExtra("Image", (Serializable) listImage);
            intent.putStringArrayListExtra("Avatar", new ArrayList<String>(listAvt));
            startActivity(intent);
        }
        finish();
    }
    class customProgress{
        boolean status=true;
        Thread thread;
        ProgressBar progressBar;
        int k=0;
        final int MAX = 100;
        int progressStatus = 0;
        Handler handler = new Handler();
        public customProgress(ProgressBar progressBar){
            this.progressBar=progressBar;
        }
        void createThread(){

            thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    while (progressStatus < MAX && status) {
                        // Update the progress status

                        // Try to sleep the thread for 100 milliseconds
                        try {
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Update the progress bar
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(progressStatus);
                            }
                        });
                        progressStatus += 1;
                       // System.out.println(progressStatus);
                    }
                    if(progressStatus==MAX){
                        status=false;
                        key=false;
                    }
                }
            });
        }

    }

}
