package com.bitcamp.app.kakao;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.lang.reflect.Member;

public class Index extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        final Context context = Index.this;
        final EditText userid = findViewById(R.id.userid);
        final EditText password = findViewById(R.id.pass);
        final MemberExist exist = new MemberExist(context);
        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Intro.LoginService() {
                    @Override
                    public void execute() {
                        if(exist.execute(String.valueOf(userid.getText()),String.valueOf(password.getText()))){
                            Toast.makeText(context,"로그인성공",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(context,MemberList.class));
                        }else{
                            Toast.makeText(context,"로그인실패",Toast.LENGTH_LONG).show();
                            userid.setText("");
                            password.setText("");
                        }
                    }
                }.execute();
            }
        });
    }
    private class LoginQuery extends Intro.QueryFactory{
        SQLiteOpenHelper helper;
        public LoginQuery(Context context) {
            super(context);
            helper = new Intro.SQLiteHelper(context);
        }


        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class  MemberExist extends LoginQuery{

        public MemberExist(Context context) {
            super(context);
        }
        public  boolean execute(String userid, String password){
            return super.getDatabase().rawQuery(
                    String.format("SELECT * FROM %s WHERE " +
                                    " %s = '%s' AND "+
                                    " %s = '%s'",
                            Intro.TABLE_MEMBER,
                            Intro.MEMBER_1,
                            userid,
                            Intro.MEMBER_2,
                            password
                    ),   null)
                    .moveToNext();
        }
    }
}