package com.hcmus.mobilappsocialnetworkingimage;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatButton;

public class NetworkChangeListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Common.isConnect(context)) { //Internet is not connected
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View layout_dialog = LayoutInflater.from(context).inflate(R.layout.check_internet_dialog, null);
            builder.setView(layout_dialog);

            Button btn = layout_dialog.findViewById(R.id.btn);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setGravity(Gravity.CENTER);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    onReceive(context, intent);
                }
            });
        }
    }
}
