package com.example.test3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RenrakusakiFragmentGamen3 extends AppCompatActivity {
    private ArrayList<User> users;  //mảng danh sách người dùng

    private DatabaseReference mDatabase;

// ...

    private DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance().getReference("user");
    private ImageView imageViewProfile;
    private EditText editTextName;
    private Button buttonHenSyuu;
    private String tenTruocKhiThayDoi;private String tenSauKhiThayDoi;

    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renrakusaki_fragment_gamen3);
        getSupportActionBar().setTitle("連絡先");
        anhXa();
        caiDatLaiTen();

        Glide.with(RenrakusakiFragmentGamen3.this).load(getIntent().getStringExtra("img_of_roommate")).placeholder(R.drawable.account_img).error(R.drawable.account_img).into(imageViewProfile);
        String name = getIntent().getStringExtra("username_of_roomate");
        String email = getIntent().getStringExtra("email_of_roommate");
        if (name.isEmpty()){
            editTextName.setText(email);
        }else{
            editTextName.setText(name);
        }


        tenTruocKhiThayDoi = editTextName.getText().toString().trim();
    }


    private void anhXa() {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        users = new ArrayList<>();
        editTextName = findViewById(R.id.editTextTextPersonName);
        buttonHenSyuu = findViewById(R.id.button);
        imageViewProfile = findViewById(R.id.img_toolbar);
    }

    private void caiDatLaiTen() {
        buttonHenSyuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;//1-2
                if (count==3||count<1){//1-2
                    count=1;editTextName.setEnabled(true);
                    resetName(editTextName.getText().toString().trim());
                }

                if (count%2==0){
//                       editTextName.setEnabled(true);
                    tenSauKhiThayDoi = editTextName.getText().toString().trim();
                    if (tenSauKhiThayDoi!=tenTruocKhiThayDoi){
                        resetName(editTextName.getText().toString().trim());
                    }
                    editTextName.setEnabled(true);

                } else{
                    editTextName.setEnabled(false);
                    resetName(editTextName.getText().toString().trim());
                }



            }
        });
        findViewById(R.id.MainActivity2Id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextName.setEnabled(false);
                count=1;
            }
        });
    }

    private void resetName(String name) {
        List<String> stringList = new ArrayList<>();
        stringList.clear();
        users.clear();//xóa sạch người dùng

        databaseReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String userGmail = dataSnapshot.getValue(User.class).getEmail();//email cua moi nguoi
                    String ult = dataSnapshot.getKey();//ma UID cua email
                    String sds = getIntent().getStringExtra("email_of_roommate");//ten nguoi dc an
                    if (userGmail.equals(sds)){

                        updateName(name,ult);
                        return;
                    }
                    Log.d("TAG", "onDataChange: "+ult);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RenrakusakiFragmentGamen3.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateName(String name , String UserUids){
        //ở đây chúng ta sẽ cài đặt đường dẫn tại đây
//        FirebaseDatabase.getInstance().getReference("user/" + UserUids+"/usernameAcount").setValue(name);
        mDatabase.child("user").child(UserUids).child("usernameAcount").setValue(name);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}