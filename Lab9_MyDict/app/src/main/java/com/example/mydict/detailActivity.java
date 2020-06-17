package com.example.mydict;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class detailActivity extends AppCompatActivity {
    TextView txtWord, txtKind, txtMean, txtExample, txtSyn, txtStatus;
    CheckBox cbStatus;
    String wordID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
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
                    txtSyn.setText(hashMap.get("synonym").toString());
                    txtExample.setText(hashMap.get("example").toString());

                    if (Integer.parseInt(hashMap.get("status").toString()) == 1){
                        //  cbStatus.setChecked(true);
                        txtStatus.setText("Đã thuộc");
                    }
                    else //cbStatus.setChecked(false);
                        txtStatus.setText("Chưa thuộc");
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

    //chuyen sang man hinh cap nhat
    public void updateScreen(View view) {
        Intent intent=getIntent();
        wordID = intent.getStringExtra("KEY");

        String ls = intent.getStringExtra("detail");
        Intent itt=new Intent(detailActivity.this, updateWordActivity.class);
        itt.putExtra("KEY", wordID);
        itt.putExtra("detail", ls);
        startActivity(itt);


    }

    //Xoa từ khỏi csdl
    public void deleteWord(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo!");
        builder.setMessage("Bạn thật sự muốn xóa từ này?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                //Kết nối tới node có tên là words (node này do ta định nghĩa trong CSDL Firebase)
                DatabaseReference myRef = database.getReference("words");
                myRef.child(wordID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast toast;
                        toast = Toast.makeText(getApplicationContext(), "Xóa từ thành công!",   Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {

                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast toast;
                                toast = Toast.makeText(getApplicationContext(), "Xóa từ thành công!",   Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        });

                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Hủy hoạt động xóa!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }
    //Lấy id các edit text
    private void addControls() {
        txtWord=findViewById(R.id.detailWord);
        txtKind=findViewById(R.id.detailKind);
        txtMean=findViewById(R.id.detailMean);
        txtExample=findViewById(R.id.detailExample);
        txtSyn=findViewById(R.id.detailSyn);
        txtStatus=findViewById(R.id.detailStatus);
    }

    //Tạo menu với item thêm từ
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_sub,menu);
        return super.onCreateOptionsMenu(menu);
    }
    // Bắt sự kiện click vào menu item trở về
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intt = getIntent();
        String sr = intt.getStringExtra("detail");

        if(item.getItemId()==R.id.mnBack)
        {
            //mở màn hình thêm ở đây
            if (sr.equals("main")) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }

            if (sr.equals("oldWord")) {
                Intent intent = new Intent(this, oldWordActivity.class);
                startActivity(intent);
            }

            if (sr.equals("allWord")) {
                Intent intent = new Intent(this, newWordActivity.class);
                startActivity(intent);
            }
//            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
