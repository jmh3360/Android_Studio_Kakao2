package com.bitcamp.app.kakao;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitcamp.app.kakao.util.Email;
import com.bitcamp.app.kakao.util.Phone;

import static com.bitcamp.app.kakao.Intro.*;

public class MemberDetail extends AppCompatActivity {
    Phone phone;
    Email email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_detail);
        final Context context = MemberDetail.this;
        Intent intent = this.getIntent();
        final String userid = intent.getStringExtra(Intro.MEMBER_1);
        final MemberItem item = new MemberItem(context);
        email = new Email(context,this);



        phone = new Phone(context,this);
        findViewById(R.id.album_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,Album.class));
            }
        });
        findViewById(R.id.call_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        final Member member = (Member) new DetailService() {
            @Override
            public Object execute() {
                return item.detail(userid);
            }
        }.execute();
        final TextView name = findViewById(R.id.name);
        name.setText(member.name+"의 프로필");
        final TextView phoneNumber = findViewById(R.id.phone_number);
        phoneNumber.setText(member.phoneNumber);
        final ImageView profilePhoto = findViewById(R.id.profile_photo);
        BitmapFactory.Options options= new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap orgImage = BitmapFactory.decodeResource(getResources(),
                this.getResources()
                        .getIdentifier(
                                this.getPackageName()+":drawable/"
                                        +member.profilePhoto,
                                null, null),
                options
        );
        Bitmap resize = orgImage.createScaledBitmap(orgImage, 100, 100, true);
        profilePhoto.setImageBitmap(resize);
        findViewById(R.id.update_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        findViewById(R.id.email_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.sendEmail(member.email);
            }
        });
        findViewById(R.id.update_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MemberUpdate.class);
                    intent.putExtra(TABLE_MEMBER,member.toString());
                startActivity(intent);
            }
        });
    }
    private abstract class DetailQuery extends Intro.QueryFactory{
        SQLiteOpenHelper helper;
        public DetailQuery(Context context) {
            super(context);
            helper = new Intro.SQLiteHelper(context);
        }
        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class MemberItem extends DetailQuery{
        public MemberItem(Context context) {super(context);}
        public Member detail(String userid){
            Member member = null;
            String sql = String.format(
                    "SELECT * FROM %s WHERE " +
                            "%s LIKE '%s'",
                    TABLE_MEMBER,MEMBER_1,userid
                     );
            Cursor cursor = this.getDatabase().rawQuery(sql,null);
            if(cursor != null){
                member = new Member();
                cursor.moveToNext();
                member.userid = cursor.getString(cursor.getColumnIndex(MEMBER_1));
                member.password = cursor.getString(cursor.getColumnIndex(MEMBER_2));
                member.name = cursor.getString(cursor.getColumnIndex(MEMBER_3));
                member.email = cursor.getString(cursor.getColumnIndex(MEMBER_4));
                member.phoneNumber = cursor.getString(cursor.getColumnIndex(MEMBER_5));
                member.profilePhoto = cursor.getString(cursor.getColumnIndex(MEMBER_6));
                member.address = cursor.getString(cursor.getColumnIndex(MEMBER_7));
            }
            Log.d("검색한 회원의 이름",member.name);
            Log.d("검색한 회원의 프로필 이미지",member.profilePhoto);
            return member;
        }
    }
}
