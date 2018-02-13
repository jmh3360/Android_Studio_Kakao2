package com.bitcamp.app.kakao.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

/**
 * Created by 1027 on 2018-02-13.
 */

public class Phone {
    private Context context;
    private Activity activity;

    public Phone(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
         }
         public void dial(String phoneNumber){
             Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phoneNumber));
             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             context.startActivity(intent);
         }
         public void call(String phoneNumber){
             Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phoneNumber));
             if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                     != PackageManager.PERMISSION_GRANTED){
                 ActivityCompat.requestPermissions(activity,new String[]{},2);
                 return;
             }
                 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             context.startActivity(intent);
         }
}
