package com.example.mydict;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class updateWordActivity extends AppCompatActivity {
    EditText  txtWord, txtKind, txtMean, txtExample;
    CheckBox cbStatus;
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
                    if (Integer.parseInt(hashMap.get("status").toString()) == 1){
                        cbStatus.setChecked(true);
                    }
                    else cbStatus.setChecked(false);
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
        String status="0";
        if(cbStatus.isChecked()){
            status ="1";
        }
        if(wd.isEmpty() || mean.isEmpty()){
            Toast toast =  Toast.makeText(this,"Không được để từ, từ loại và nghĩa của từ trống!",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Kết nối tới node có tên là words (node này do ta định nghĩa trong CSDL Firebase)
        DatabaseReference myRef = database.getReference("words");
        myRef.child(wordID).child("word").setValue(wd);
        myRef.child(wordID).child("kind").setValue(kind);
        myRef.child(wordID).child("mean").setValue(mean);
        myRef.child(wordID).child("example").setValue(example);
        myRef.child(wordID).child("status").setValue(status);
        finish();
        Toast toast =  Toast.makeText(this,"Chỉnh sửa từ thành công!",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    //Xoa từ khỏi csdl
    public void deleteWord(View view) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Kết nối tới node có tên là words (node này do ta định nghĩa trong CSDL Firebase)
        DatabaseReference myRef = database.getReference("words");
        myRef.child(wordID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast toast;
                toast = Toast.makeText(getApplicationContext(), "Xoá thành công!",   Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast toast;
                        toast = Toast.makeText(getApplicationContext(), "Xoá không thành công!",   Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                });

        finish();
    }
    //Lấy id các edit text
    private void addControls() {
        txtWord=findViewById(R.id.edtWord);
        txtKind=findViewById(R.id.edtKind);
        txtMean=findViewById(R.id.edtMean);
        txtExample=findViewById(R.id.edtExample);
        cbStatus=findViewById(R.id.cbStatus);
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
        if(item.getItemId()==R.id.mnBack)
        {
            //mở màn hình thêm ở đây
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}