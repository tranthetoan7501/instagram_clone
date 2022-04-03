package com.hcmus.mobilappsocialnetworkingimage.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.adapter.storiesAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Vector;

public class storiesActivity extends Activity {
    Button back,forword;
    ProgressBar progressBar;
    ImageButton close;
    Vector<String> listName=new Vector<>();
    Vector<String> listImage=new Vector<>();
    Vector<String> listAvt=new Vector<>();
    int k=0;
    final int MAX = 300;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    RelativeLayout relativeLayout;
    boolean status=true;
    void createThread(){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < MAX && status) {
                    // Update the progress status

                    // Try to sleep the thread for 100 milliseconds
                    try {
                        Thread.sleep(20);
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
                }
                if(progressStatus==MAX) {
                    if (listName.size() == 1) {
                        finish();
                    } else {
                        goStories();
                    }
                }
            }
        });
    }
    Thread thread;
    Bitmap imagePath;
    ImageButton person;
    ImageView image;
    TextView username;
    int pos;


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);
        person=findViewById(R.id.person);
        image=findViewById(R.id.image);
        username=findViewById(R.id.username);

        pos=Integer.parseInt(getIntent().getStringExtra("position"));

        listName=new Vector<>(getIntent().getStringArrayListExtra("Name"));
        listImage=new Vector<>(getIntent().getStringArrayListExtra("Image"));
        listAvt=new Vector<>(getIntent().getStringArrayListExtra("Avatar"));

        Picasso.get().load(listImage.get(pos)).into(image);
        Picasso.get().load(listAvt.get(pos)).into(person);

        username.setText(listName.get(pos));

        relativeLayout=findViewById(R.id.item);
        progressBar=findViewById(R.id.progress_horizontal);
        progressBar.setIndeterminate(false);
        progressBar.setMax(MAX);
        createThread();
        thread .start();
        close=findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                status=false;
            }
        });
        relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                status=false;
                return false;
            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status) {
                    status = false;
                    if (listName.size() == 1) {
                        finish();
                    }
                    else {
                        goStories();
                    }
                } else {
                    status = true;
                    createThread();
                    thread.start();
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
            intent.putStringArrayListExtra("Name", new ArrayList<String>(listName));
            intent.putStringArrayListExtra("Image", new ArrayList<String>(listImage));
            intent.putStringArrayListExtra("Avatar", new ArrayList<String>(listAvt));
            startActivity(intent);
        }
        finish();
    }
}