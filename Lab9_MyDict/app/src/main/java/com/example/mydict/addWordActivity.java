package com.example.mydict;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addWordActivity extends AppCompatActivity {
    EditText txtWord, txtKind, txtMean, txtExample, txtSyn;
    

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
        txtSyn  = findViewById(R.id.addSyn);
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
            String wd = txtWord.getText().toString();
            String kind = txtKind.getText().toString();
            String mean = txtMean.getText().toString();
            String syn = txtSyn.getText().toString();
            String example = txtExample.getText().toString();
            if(wd.isEmpty() || mean.isEmpty() || kind.isEmpty()){
                Toast toast =  Toast.makeText(this,"Không được để từ, từ loại và nghĩa của từ trống!",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            else{
                String status = "0";
                myRef.child(wordId).child("word").setValue(wd);
                myRef.child(wordId).child("kind").setValue(kind);
                myRef.child(wordId).child("mean").setValue(mean);
                myRef.child(wordId).child("synonym").setValue(syn);
                myRef.child(wordId).child("example").setValue(example);
                myRef.child(wordId).child("status").setValue(status);

                finish();
                Toast toast =  Toast.makeText(this,"Thêm từ mới thành công!",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this,"Error:"+ex.toString(),Toast.LENGTH_LONG).show();
        }
    }
    //nut huy
    public void buttonHuy (View view){
        finish();
    }

    //Tạo menu với item thêm từ
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_sub,menu);
        return super.onCreateOptionsMenu(menu);
    }
    // Bắt sự kiện click vào menu item thêm từ
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intt = getIntent();
        String sr = intt.getStringExtra("main");

        if(item.getItemId()==R.id.mnBack)
        {
//            if (sr.equals("main")) {
//                Intent intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
//            }
//
//            if (sr.equals("oldWord")) {
//                Intent intent = new Intent(this, oldWordActivity.class);
//                startActivity(intent);
//            }
//
//            if (sr.equals("allWord")) {
//                Intent intent = new Intent(this, newWordActivity.class);
//                startActivity(intent);
//            }
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

}