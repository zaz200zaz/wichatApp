package com.example.test3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AcountNinSyou extends AppCompatActivity {
    private static final int MY_QUEST_CODE = 10;
    private EditText edtAcount_ninSyo;
    private EditText edtConfirmationPassword_ninSyo;
    private ArrayList<User> users;
    private boolean aBoolean = false;
    private UserAdapter userAdapter;//bộ điều hợp người dùng
    private UserAdapter.OnUserClickListener onUserClickListener;//khi mà ấn vào từng người dùng
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acount_nin_syou);
        setTitle("アカウント認証");//タイトル設定
        anhXa();//ID反射
        neuCoduLieuThiNhapEmailVao();//データ受け取り関数
        diTiep();//次へ
    }

    public void setTitle(String title) {
        TextView textView = new TextView(AcountNinSyou.this);
        textView.setText(title);
        textView.setTextSize(20);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(android.R.color.background_light));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(textView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(AcountNinSyou.this,MiTouRoku.class);
        intent.putExtra("tk",edtAcount_ninSyo.getText().toString().trim());
        startActivity(intent);
        finish();
        return super.onSupportNavigateUp();
    }

    private void diTiep() {
        findViewById(R.id.nextButton_ninSyo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUsers();
            }
        });
    }

    private void neuCoduLieuThiNhapEmailVao() {
        String s = getIntent().getStringExtra("tk");
        if (!s.isEmpty()){
            edtAcount_ninSyo.setText(s);
        }
    }

    private void anhXa(){
        edtAcount_ninSyo = findViewById(R.id.edtAcount_ninSyo);
        edtConfirmationPassword_ninSyo = findViewById(R.id.edtConfirmationPassword_ninSyo);
        users = new ArrayList<>();
    }

    private  void getUsers(){
        users.clear();//xóa sạch người dùng
        FirebaseDatabase.getInstance().getReference("user")//lấy dữ liệu từ user
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            users.add(dataSnapshot.getValue(User.class));
                        }
                        String email = edtAcount_ninSyo.getText().toString().trim();
                        String code = edtConfirmationPassword_ninSyo.getText().toString().trim();
                        if (code.isEmpty()||email.isEmpty()){
                            Toast.makeText(AcountNinSyou.this, "1(#`Д´)ﾉｺﾞﾙｧｧｧｧｧ!!", Toast.LENGTH_SHORT).show();
                        }
                        for (User user: users){
                            if (!code.isEmpty()&&!email.isEmpty()&&user.getEmail().equals(email)&&user.getVerificationCode()==Integer.parseInt(edtConfirmationPassword_ninSyo.getText().toString().trim())) {
                                handleLogin(user.getEmail(),user.getPassword(),user);
                                FirebaseAuth.getInstance().signInWithEmailAndPassword(user.getEmail(),user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            Intent intent = new Intent(AcountNinSyou.this,PasswordChange2.class);
                                            intent.putExtra("mk",user.getPassword())
                                                    .putExtra("tk",user.getEmail());
                                            startActivity(intent);
                                            finish();
                                            Toast.makeText(AcountNinSyou.this,"認証コード正しい！",Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(AcountNinSyou.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                                            Log.d("TAG", "onComplete: "+task.getException().getLocalizedMessage());
                                        }
                                    }
                                });
                                return;
                            }

                        }
                    }

                    private void handleLogin(String tk, String mk, User user) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AcountNinSyou.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}