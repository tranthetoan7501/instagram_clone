package com.hcmus.mobilappsocialnetworkingimage.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hcmus.mobilappsocialnetworkingimage.R;

public class stories extends AppCompatActivity {
    Button back,forword;
    ProgressBar progressBar;
    ImageButton close;
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
                    System.out.println(progressStatus);
                }
                if(progressStatus==MAX){
                    Intent intent=new Intent(getApplicationContext(), stories.class);
                    startActivity(intent);
                }
            }
        });
    }
    Thread thread;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);
        getSupportActionBar().hide();
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
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status) {
                    status = false;
                }
                else{
                    status=true;
                    createThread();
                    thread.start();
                }
            }
        });

    }
}