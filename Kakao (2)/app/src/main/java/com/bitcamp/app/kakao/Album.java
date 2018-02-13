package com.bitcamp.app.kakao;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class Album extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.album);
        final Context context = Album.this;
        GridView gridView = findViewById(R.id.album);
        gridView.setAdapter(new PhotoItem(context,photos()));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(context,"선택한사진"+i,Toast.LENGTH_LONG).show();
            }
        });
    }



    public String[] photos(){
        int count = 9;
        String[] arr = new String[count];
        for (int i =0;i<arr.length;i++){
            arr[i] = "pic"+(i+1);
        }
        return arr;
    }
    private class PhotoItem extends BaseAdapter {
        private Context context;
        private String[] photos;

        public PhotoItem(Context context,String[] photos) {
            this.context = context;
            this.photos = photos;
        }

        @Override
        public int getCount() {
            return photos.length;
        }

        @Override
        public Object getItem(int i) {
            return photos[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View v, ViewGroup g) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View view;

            if (v == null) {
                view = new View(context);
                view = inflater.inflate(R.layout.photo, null);
                ImageView imageView = view.findViewById(R.id.imageView);
                String photo = photos[i];
                switch (photo){
                    case"pic1" : imageView.setImageResource(R.drawable.pic1);break;
                    case"pic2" : imageView.setImageResource(R.drawable.pic2);break;
                    case"pic3" : imageView.setImageResource(R.drawable.pic3);break;
                    case"pic4" : imageView.setImageResource(R.drawable.pic4);break;
                    case"pic5" : imageView.setImageResource(R.drawable.pic5);break;
                    case"pic6" : imageView.setImageResource(R.drawable.pic6);break;
                    case"pic7" : imageView.setImageResource(R.drawable.pic7);break;
                    case"pic8" : imageView.setImageResource(R.drawable.pic8);break;
                    case"pic9" : imageView.setImageResource(R.drawable.pic9);break;
                }
            } else {
                view = v;
            }
            return view;
        }
    }
}


