package com.example.mydict;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mydict.adapter.WordAdapter;
import com.example.mydict.model.Word;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class oldWordActivity extends AppCompatActivity {
    ListView lvOldWord;
    WordAdapter adapter;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        getWordsFromFirebase();
        addEvents();
    }


    //Bắt sự kiện click vào các item listview
    private void addEvents() {
        lvOldWord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Word word = adapter.getItem(position);
                String key = word.getWordId();
                Intent intent=new Intent(oldWordActivity.this, detailActivity.class);
                intent.putExtra("KEY",key);
    //            intent.putExtra("detail","oldWord");
                startActivity(intent);
            }
        });

    }

    //Load data dưới dang custom view -- chưa thuộc
    private void getWordsFromFirebase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("words");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot dss : dataSnapshot.getChildren()) {

                    //convert ra đối tượng Contact:
                    Word word = dss.getValue(Word.class);
                    String key = dss.getKey();
                    count++;
                    word.setWordId(key);
                    String temp;
                    int divide = 0;
                    try {
                        temp = word.getStatus();
                        divide = Integer.parseInt(temp);
                    }
                    catch (Exception ex){
                        Log.e("LOI GI NE == ", " " + ex);
                        Log.e("NO LA DANG NAY == ", " " + word.getStatus());
                    }

                    if( divide == 1){
                        adapter.add(word);
                    }
                }
                count++;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addControls() {
        lvOldWord = findViewById(R.id.lvWord);
        adapter = new WordAdapter(this, R.layout.item);
        lvOldWord.setAdapter(adapter);
    }

    //Tạo menu với item thêm từ
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_old,menu);
        return super.onCreateOptionsMenu(menu);
    }
    // Bắt sự kiện click vào menu item thêm từ
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //mở màn hình thêm ở đây
        if(item.getItemId()==R.id.mnOldAdd)
        {
            Intent intent=new Intent(this, addWordActivity.class);
            intent.putExtra("wordid", count);
      //      intent.putExtra("main","oldWord");
            startActivity(intent);
        }
        //mở màn hình all word
        if(item.getItemId()==R.id.mnNewTab)
        {
            Intent intent = new Intent(this, newWordActivity.class);
            startActivity(intent);
        }

        //mở màn hình chua thuoc word
        if(item.getItemId()==R.id.mnMain)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        //mở màn hình chua thuoc word
        if(item.getItemId()==R.id.mnBackOld)
        {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
