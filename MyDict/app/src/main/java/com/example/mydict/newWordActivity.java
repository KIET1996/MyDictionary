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

public class newWordActivity extends AppCompatActivity {
    ListView lvNewWord;
    WordAdapter adapter;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        getWordsFromFirebase();
        addEvents();;
    }

    //Bắt sự kiện click vào các item listview
    private void addEvents() {
        lvNewWord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Word word = adapter.getItem(position);
                String key = word.getWordId();
                Intent intent=new Intent(newWordActivity.this, updateWordActivity.class);
                intent.putExtra("KEY",key);
                startActivity(intent);
            }
        });

    }

    //Load data dưới dang custom view
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

                    if( divide == 0){
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
        lvNewWord = findViewById(R.id.lvWord);
        adapter = new WordAdapter(this, R.layout.item);
        lvNewWord.setAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_new,menu);
        return super.onCreateOptionsMenu(menu);
    }
    // Bắt sự kiện click vào menu item thêm từ
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.mnNewAdd)
        {
            //mở màn hình thêm ở đây
            Intent intent=new Intent(this, addWordActivity.class);
            intent.putExtra("wordid", count);
            startActivity(intent);
        }
        if(item.getItemId()==R.id.mnOldTab)
        {
            Intent intent = new Intent(this, oldWordActivity.class);
            startActivity(intent);
        }
        if(item.getItemId()==R.id.mnBackNew)
        {
            //mở màn hình thêm ở đây
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
