package com.bitcamp.app.kakao;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class Memberadd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_add);
                final Context context = Memberadd.this;
                final EditText name = findViewById(R.id.name);
                final EditText phoneNum = findViewById(R.id.phone_number);
                final EditText email = findViewById(R.id.change_email);
                final EditText address = findViewById(R.id.change_address);

                findViewById(R.id.list_btn).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(context,MemberList.class));
                    }
                });

                findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                final Intro.Member member = new Intro.Member();
                member.name = String.valueOf(name.getText());
                member.phoneNumber = String.valueOf(phoneNum.getText());
                member.email = String.valueOf(email.getText());
                member.address = String.valueOf(address.getText());

                final MemberItemInsert insert = new MemberItemInsert(context);
                new Intro.DMLService(){

                    @Override
                    public void execute() {
                        insert.Insert(member);
                    }
                }.execute();
                startActivity(new Intent(context,MemberList.class));
            }
        });

        final ImageView profilePhoto = findViewById(R.id.profile_photo);
        BitmapFactory.Options options= new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap orgImage = BitmapFactory.decodeResource(getResources(),
                this.getResources()
                        .getIdentifier(
                                this.getPackageName()+":drawable/"
                                        +"profile_0",
                                null, null),
                options
        );
        Bitmap resize = orgImage.createScaledBitmap(orgImage, 600, 600, true);
        profilePhoto.setImageBitmap(resize);

    }
    private abstract class InsertQuery extends Intro.QueryFactory{
        Intro.SQLiteHelper helper;
        public InsertQuery(Context context) {
            super(context);
            helper = new Intro.SQLiteHelper(context);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }
    private class MemberItemInsert extends InsertQuery{

        public MemberItemInsert(Context context) { super(context);  }
        public void Insert(Intro.Member member){
            this.getDatabase().execSQL(String.format(" INSERT INTO %s (%s,%s,%s,%s,%s) " +
                    "VALUES('%s','%s','%s','%s','%s')",
                    Intro.TABLE_MEMBER,Intro.MEMBER_6,Intro.MEMBER_3,Intro.MEMBER_5,Intro.MEMBER_7,Intro.MEMBER_7,
                    "default_profile",member.name,member.phoneNumber,member.address,member.email));
        }
    }
}
