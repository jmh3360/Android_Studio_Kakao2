package com.bitcamp.app.kakao;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.bitcamp.app.kakao.Intro.*;

import static com.bitcamp.app.kakao.Intro.MEMBER_1;
import static com.bitcamp.app.kakao.Intro.MEMBER_3;
import static com.bitcamp.app.kakao.Intro.MEMBER_4;
import static com.bitcamp.app.kakao.Intro.MEMBER_5;
import static com.bitcamp.app.kakao.Intro.MEMBER_6;
import static com.bitcamp.app.kakao.Intro.MEMBER_7;
import static com.bitcamp.app.kakao.Intro.TABLE_MEMBER;

public class MemberUpdate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_update);
        final Context context = MemberUpdate.this;
        final String user = getIntent().getStringExtra(Intro.TABLE_MEMBER);
        final String[] arr = user.split(",");
        final TextView name = findViewById(R.id.name);
        name.setText(arr[2]+"님의 프로필 수정");
        final TextView phoneNumber = findViewById(R.id.phone_number);
        phoneNumber.setText(arr[4]);
        findViewById(R.id.list_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        final ImageView profilePhoto = findViewById(R.id.profile_photo);
        final EditText changeProfilePhoto = findViewById(R.id.change_profile_photo);
        final EditText changeName= findViewById(R.id.change_name);
        final EditText changePhoneNumber = findViewById(R.id.change_phone_number);
        final EditText changeAddress = findViewById(R.id.change_address);
        final EditText changeEmail = findViewById(R.id.change_email);
        BitmapFactory.Options options= new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap orgImage = BitmapFactory.decodeResource(getResources(),
                this.getResources()
                        .getIdentifier(
                                this.getPackageName()+":drawable/"
                                        +arr[5],
                                null, null),
                options
        );
        Bitmap resize = orgImage.createScaledBitmap(orgImage, 100, 100, true);
        profilePhoto.setImageBitmap(resize);
        changeProfilePhoto.setHint(arr[5]);
        changeName.setHint(arr[2]);
        changePhoneNumber.setHint(arr[4]);
        changeAddress.setHint(arr[6]);
        changeEmail.setHint(arr[3]);
        findViewById(R.id.confirm_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("변경하려는 값들 : ",String.valueOf(changeProfilePhoto.getText()));
                Log.d("변경하려는 값들 : ",String.valueOf(changeName.getText()));
                Log.d("변경하려는 값들 : ",String.valueOf(changePhoneNumber.getText()));
                Log.d("변경하려는 값들 : ",String.valueOf(changeAddress.getText()));
                Log.d("변경하려는 값들 : ",String.valueOf(changeEmail.getText()));
                final Member changeMember = new Member();
                changeMember.userid = arr[0];
                changeMember.profilePhoto =
                        (String.valueOf(changeProfilePhoto.getText()).equals("")) ?
                                arr[5] : String.valueOf(changeProfilePhoto.getText());
                changeMember.name =
                        (String.valueOf(changeName.getText()).equals("")) ?
                                arr[2] : String.valueOf(changeName.getText());
                changeMember.phoneNumber =
                        (String.valueOf(changePhoneNumber.getText()).equals("")) ?
                                arr[4] : String.valueOf(changePhoneNumber.getText());
                changeMember.address =
                        (String.valueOf(changeAddress.getText()).equals("")) ?
                                arr[6] : String.valueOf(changeAddress.getText());
                changeMember.email =
                        (String.valueOf(changeEmail.getText()).equals("")) ?
                                arr[3] : String.valueOf(changeEmail.getText());
                final MemberItemUpdate itemUpdate = new MemberItemUpdate(context);
                new DMLService() {
                    @Override
                    public void execute() {
                        itemUpdate.update(changeMember);
                    }
                }.execute();
                Intent intent = new Intent(context,MemberDetail.class);
                intent.putExtra(MEMBER_1,arr[0]);
                startActivity(intent);
            }
        });
        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeProfilePhoto.setHint(arr[5]);
                changeName.setHint(arr[2]);
                changePhoneNumber.setHint(arr[4]);
                changeAddress.setHint(arr[6]);
                changeEmail.setHint(arr[3]);
            }
        });
    }
    private abstract class UpdateQuery extends QueryFactory{
        SQLiteOpenHelper helper;
        public UpdateQuery(Context context) {
            super(context);
            helper = new SQLiteHelper(context);
        }
        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }
    private class MemberItemUpdate extends UpdateQuery{
        public MemberItemUpdate(Context context) {
            super(context);
        }
        public void update(Member member){
            String sql = String.format(
                    "UPDATE %s SET %s = '%s',"+
                            " %s = '%s',"+
                            " %s = '%s',"+
                            " %s = '%s',"+
                            " %s = '%s'"+
                            " WHERE %s LIKE '%s'" ,
                            TABLE_MEMBER,
                    MEMBER_6,member.profilePhoto,
                    MEMBER_3,member.name,
                    MEMBER_5,member.phoneNumber,
                    MEMBER_7,member.address,
                    MEMBER_4,member.email,
                    MEMBER_1,member.userid);
            this.getDatabase().execSQL(sql);
        }
    }
}
