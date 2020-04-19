package com.example.mydict;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mydict.adapter.WordAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addWordActivity extends AppCompatActivity {
    EditText txtWord, txtKind, txtMean, txtExample;
    String count;
    WordAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        addControls();
    }
    // Lấy id các text view
    private void addControls() {
        txtWord = findViewById(R.id.addWord);
        txtKind = findViewById(R.id.addKind);
        txtMean = findViewById(R.id.addMean);
        txtExample = findViewById(R.id.addExample);
    }

    //Thêm từ mới vào csdl
    public void addNewWord(View view) {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            //Kết nối tới node có tên là Wors (node này do ta định nghĩa trong CSDL Firebase)
            DatabaseReference myRef = database.getReference("words");
            Intent intent = getIntent();
            int count = intent.getIntExtra("wordid",0);
            String wordId="word"+ count;
            String wd=txtWord.getText().toString();
            String kind = txtKind.getText().toString();
            String mean = txtMean.getText().toString();
            String example = txtExample.getText().toString();
            myRef.child(wordId).child("word").setValue(wd);
            myRef.child(wordId).child("kind").setValue(kind);
            myRef.child(wordId).child("mean").setValue(mean);
            myRef.child(wordId).child("example").setValue(example);

            finish();
        }
        catch (Exception ex)
        {
            Toast.makeText(this,"Error:"+ex.toString(),Toast.LENGTH_LONG).show();
        }
    }



}