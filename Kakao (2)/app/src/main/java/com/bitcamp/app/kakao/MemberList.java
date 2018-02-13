package com.bitcamp.app.kakao;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
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

import java.lang.reflect.Member;
import java.util.ArrayList;

import static com.bitcamp.app.kakao.Intro.MEMBER_1;

public class MemberList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_list);
        final Context context = MemberList.this;
        final ListView listView = findViewById(R.id.listView);
        findViewById(R.id.setting_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,Memberadd.class));
            }
        });
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
                Intro.Member member = (Intro.Member) listView.getItemAtPosition(pos);
                Toast.makeText(context,member.name+"의 상세로 이동",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, MemberDetail.class);
                intent.putExtra(MEMBER_1,member.userid);
                startActivity(intent);
            }

        });

            //콜백영역에다가는 클래스를 선언하면 안된다. 그 이유는 이벤트가 발생해야만 생성이 되기 때문에
        //코드를 읽어 들이는 순간 내용이 이미 만들어져 있어야한다.
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
                final Intro.Member member = (Intro.Member) listView.getItemAtPosition(pos);
                new AlertDialog.Builder(context).setTitle("DELETE").setMessage("삭제할게요").setPositiveButton
                        (android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context,"삭제 합니다.",Toast.LENGTH_LONG).show();
                            final MemberItemDelete delete = new MemberItemDelete(context);
                        new Intro.DMLService() {
                            @Override
                            public void execute() {
                                delete.delete(member.userid);
                                }
                        }.execute();
                        startActivity(new Intent(context,MemberList.class));
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context,"삭제 취소합니다.",Toast.LENGTH_LONG).show();
                    }
                })
                .show();

                return true;
            }
        });


        findViewById(R.id.logout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, Index.class));
            }
        });
    }
    private abstract class DeleteQuery extends Intro.QueryFactory{
        //인트로에서 한번 사용하고 있기 때문에 SQLiteOnHelper를 사용하지않아도 된다.
        //멤버 변수를 사용하는 이유 필드는 메모리 내의 영역이고 area는 cpu내의 영역이기 때문에
        //나중에 터질수가 있따. 그래서 멤변에서는 할당을하면 안된다.
        //정원이가 대답한 내장된 그 의미 물어볼 것
        Intro.SQLiteHelper helper;
        public DeleteQuery(Context context) {
            super(context);
            helper = new Intro.SQLiteHelper(context);
        }
        @Override
        public SQLiteDatabase getDatabase() {return helper.getReadableDatabase();}
    }
    //위의 abstract는 추상클래스라 객체를 만들수 없어 아래와 같이 다시 생성해준다.
    private class MemberItemDelete extends DeleteQuery{
        public MemberItemDelete(Context context) {
            super(context);
        }
        //삭제 영역은 void로 변함
        public void delete(String userid){
                //111번 라인 이해 안감....
           this.getDatabase().execSQL(String.format(" DELETE FROM %s WHERE %s LIKE '%s'",
                   Intro.TABLE_MEMBER,Intro.MEMBER_1,userid));
        }
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
            Intro.Member member = null;
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

