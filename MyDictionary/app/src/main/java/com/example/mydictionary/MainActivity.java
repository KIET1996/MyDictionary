package com.example.mydictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    ListView lvWord;
    ArrayAdapter<String> adapter;
    String TAG="FIREBASE";
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);

        lvWord=findViewById(R.id.lvWord);
        lvWord.setAdapter(adapter);
        //lấy đối tượng FirebaseDatabase
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //Kết nối tới node có tên là words (node này do ta định nghĩa trong CSDL Firebase)
        DatabaseReference myRef = database.getReference("words");

        //truy suất và lắng nghe sự thay đổi dữ liệu
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                //vòng lặp để lấy dữ liệu khi có sự thay đổi trên Firebase
                for (DataSnapshot data: dataSnapshot.getChildren())
                {
                    //lấy key của dữ liệu
                    String key=data.getKey();
                    count ++;
                    //lấy giá trị của key (nội dung)
                    String value=data.getValue().toString();
                    adapter.add(key+"\n"+value);
                }
                count ++;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

        lvWord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data=adapter.getItem(position);
                String key=data.split("\n")[0];
                Intent intent=new Intent(MainActivity.this,updateWordActivity.class);
                intent.putExtra("KEY",key);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Bắt sự kiện khi người dùng chọn Thêm từ để chuyển trang
        if (item.getItemId()==R.id.mnAddWord){
            //Goi sự kiện thêm
            Intent intent=new Intent(MainActivity.this,addWordActivity.class);
            intent.putExtra("id", count);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}
