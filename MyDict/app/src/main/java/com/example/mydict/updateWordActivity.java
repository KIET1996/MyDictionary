package com.example.mydict;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class updateWordActivity extends AppCompatActivity {
    EditText  txtWord, txtKind, txtMean, txtExample;
    String wordID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_word);
        addControls();
        getWorDetail();
    }

    //Lấy chi tiết từ và show lên màn hình
    private void getWorDetail() {
        Intent intent=getIntent();
        wordID = intent.getStringExtra("KEY");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Kết nối tới node có tên la words (node này do ta định nghĩa trong CSDL Firebase)
        DatabaseReference myRef = database.getReference("words");

        //truy suất và lắng nghe sự thay đổi dữ liệu
        //chỉ truy suất node được chọn trên ListView myRef.child(key)
        //addListenerForSingleValueEvent để lấy dữ liệu đơn
        myRef.child(wordID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    //nó trả về 1 DataSnapShot, mà giá trị đơn nên gọi getValue trả về 1 HashMap
                    HashMap<String,Object> hashMap= (HashMap<String, Object>) dataSnapshot.getValue();
                    //HashMap này sẽ có kích  thước bằng số Node con bên trong node truy vấn
                    //mỗi phần tử có key là wordid được định nghĩa trong cấu trúc Json của Firebase
                    txtWord.setText(hashMap.get("word").toString());
                    txtKind.setText(hashMap.get("kind").toString());
                    txtMean.setText(hashMap.get("mean").toString());
                    txtExample.setText(hashMap.get("example").toString());
                }
                catch (Exception ex)
                {
                    Log.e("LOI_JSON",ex.toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("LOI_CHITIET", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    //Update từ lên csdl
    public void updateWord(View view) {
        String wd=txtWord.getText().toString();
        String kind=txtKind.getText().toString();
        String mean=txtMean.getText().toString();
        String example=txtExample.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Kết nối tới node có tên là words (node này do ta định nghĩa trong CSDL Firebase)
        DatabaseReference myRef = database.getReference("words");
        myRef.child(wordID).child("word").setValue(wd);
        myRef.child(wordID).child("kind").setValue(kind);
        myRef.child(wordID).child("mean").setValue(mean);
        myRef.child(wordID).child("example").setValue(example);
        finish();
    }

    //Xoa từ khỏi csdl
    public void deleteWord(View view) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Kết nối tới node có tên là words (node này do ta định nghĩa trong CSDL Firebase)
        DatabaseReference myRef = database.getReference("words");
        myRef.child(wordID).removeValue();
        finish();
    }
    //Lấy id các edit text
    private void addControls() {
        txtWord=findViewById(R.id.edtWord);
        txtKind=findViewById(R.id.edtKind);
        txtMean=findViewById(R.id.edtMean);
        txtExample=findViewById(R.id.edtExample);
    }
}