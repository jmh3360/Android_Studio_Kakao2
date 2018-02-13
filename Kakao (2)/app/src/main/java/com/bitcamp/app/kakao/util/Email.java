package com.bitcamp.app.kakao.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

/**
 * Created by 1027 on 2018-02-13.
 */

public class Email {
    private Context context;
    private Activity activity;

    public Email(Context context, Activity activity){
        this.activity = activity;
        this.context = context;
    }
    public void sendEmail(String eamil){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"+eamil));
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL,eamil);
        intent.putExtra(Intent.EXTRA_SUBJECT,"hello guys!!");
        intent.putExtra(Intent.EXTRA_TEXT,"잘지내냐 연락주라");
        context.startActivity(intent.createChooser(intent,"example"));


    }
}
