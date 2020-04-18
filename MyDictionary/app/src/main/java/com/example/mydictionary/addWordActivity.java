package com.example.mydictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addWordActivity extends AppCompatActivity {

    EditText txtWord, txtKind, txtMean, txtExample;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_word);
        addControls();
    }

    private void addControls() {
        txtWord=findViewById(R.id.txtNewWord);
        txtKind=findViewById(R.id.txtKind);
        txtMean=findViewById(R.id.txtMean);
        txtExample=findViewById(R.id.txtExample);
    }

    public void addNewWord(View view) {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            //Kết nối tới node có tên là contacts (node này do ta định nghĩa trong CSDL Firebase)
            DatabaseReference myRef = database.getReference("words");
            Intent intent = getIntent();
            int count = intent.getIntExtra("id",0);
            String wordId="word"+count;
            String newWord = txtWord.getText().toString();
            String kind = txtKind.getText().toString();
            String mean = txtMean.getText().toString();
            String example = txtExample.getText().toString();
            myRef.child(wordId).child("word").setValue(newWord);
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
