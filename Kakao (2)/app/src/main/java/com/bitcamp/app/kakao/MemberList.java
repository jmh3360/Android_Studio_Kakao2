package com.bitcamp.app.kakao;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.bitcamp.app.kakao.Intro.*;
import java.util.ArrayList;

import static com.bitcamp.app.kakao.Intro.MEMBER_1;

public class MemberList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_list);
        final Context context = MemberList.this;
        final ListView listView = findViewById(R.id.listView);
        listView.setAdapter(new MemberItem(
                context,
                (ArrayList<Intro.Member>) new Intro.ListService(){
            @Override
            public ArrayList<?> execute() {
                return new MemberItemList(context).list();
            }
        }.execute()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Member member = (Member) listView.getItemAtPosition(pos);
                Toast.makeText(context,member.name+"의 상세로 이동",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, MemberDetail.class);
                intent.putExtra(MEMBER_1,member.userid);
                startActivity(intent);
            }
        });
    }
    private abstract class ListQuery extends Intro.QueryFactory{
        SQLiteOpenHelper helper;
        public ListQuery(Context context) {
            super(context);
            helper = new Intro.SQLiteHelper(context);
        }
        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class MemberItemList extends ListQuery{

        public MemberItemList(Context context) {
            super(context);
        }
        public ArrayList<Intro.Member> list(){
            ArrayList<Intro.Member> members = new ArrayList();
            String sql = String.format(
                    "SELECT %s, %s, %s, %s FROM %s ",
                    MEMBER_1, Intro.MEMBER_3,
                    Intro.MEMBER_5, Intro.MEMBER_6,
                    Intro.TABLE_MEMBER);
            Cursor cursor = this.getDatabase().rawQuery(sql,null);
            Member member = null;
            if(cursor != null){
                while (cursor.moveToNext()){
                    member = new Intro.Member();
                    member.userid = cursor.getString(cursor.getColumnIndex(MEMBER_1));
                    member.name = cursor.getString(cursor.getColumnIndex(Intro.MEMBER_3));
                    member.phoneNumber = cursor.getString(cursor.getColumnIndex(Intro.MEMBER_5));
                    member.profilePhoto = cursor.getString(cursor.getColumnIndex(Intro.MEMBER_6));
                    members.add(member);
                }
            }
            Log.d("멤버 수:",String.valueOf(members.size()));
            return members;
        }
    }
    private class MemberItem extends BaseAdapter{
        ArrayList<Intro.Member> list;
        LayoutInflater inflater;
        public MemberItem(Context context,ArrayList<Intro.Member> list) {
            this.list = list;
            this.inflater = LayoutInflater.from(context);
        }
        private int[] photo = {
                R.drawable.profile_1,
                R.drawable.profile_2,
                R.drawable.profile_3,
                R.drawable.profile_4,
                R.drawable.profile_5,
                R.drawable.profile_6,
                R.drawable.profile_8
        };

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View v, ViewGroup g) {
            ViewHolder holder;
            if(v==null){
                v = inflater.inflate(R.layout.member_item,null);
                holder = new ViewHolder();
                holder.profilePhoto = v.findViewById(R.id.profile_photo);
                holder.name = v.findViewById(R.id.name);
                holder.phoneNumber = v.findViewById(R.id.phone_number);
                v.setTag(holder);
            }else{
                holder = (ViewHolder) v.getTag();
            }
            holder.profilePhoto.setImageResource(photo[i]);
            holder.name.setText(list.get(i).name);
            holder.phoneNumber.setText(list.get(i).phoneNumber);
            return v;
        }
    }
    static class ViewHolder{
        ImageView profilePhoto;
        TextView name;
        TextView phoneNumber;
    }
}
