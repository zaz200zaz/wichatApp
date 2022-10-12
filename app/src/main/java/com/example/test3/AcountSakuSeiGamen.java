package com.example.test3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AcountSakuSeiGamen extends AppCompatActivity {
    private static final int MY_QUEST_CODE = 10;
    private Button backButton;//nút quay về
    private Button nextButton;//nút đi tiếp
    private EditText edtAcount;//tên
    private EditText edtConfirmationAcount;//xác nhận lại tên
    private EditText edtEmail;//email
    private EditText edtConfirmationEmail;//xác nhận email
    private String password;
    private int verificationCode;
    private String profilePicture;
    private String tenHienThi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acount_saku_sei_gamen);
        setTitle("アカウント作成");//タイトル設定
        anhXa();//ID反射
        diTiep();//次へのボタン処理
    }

    private void setTitle(String title) {
        TextView textView = new TextView(AcountSakuSeiGamen.this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.blank_menu,menu);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void diTiep() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });
    }

    private void quayTroLai() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AcountSakuSeiGamen.this,MiTouRoku.class));
                finish();
            }
        });
    }
    private void anhXa(){
        edtEmail= findViewById(R.id.edtEmail);
        edtConfirmationEmail= findViewById(R.id.edtConfirmationEmail);
        password = "";
        verificationCode = 0;
        profilePicture = "";
        tenHienThi = "";
        backButton= findViewById(R.id.backButton);
        nextButton= findViewById(R.id.nextButton);
    }
    private void next(){
        if (edtConfirmationEmail.getText().toString().isEmpty()|| edtEmail.getText().toString().isEmpty()){
            Toast.makeText(AcountSakuSeiGamen.this, "値がない所発見", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!edtEmail.getText().toString().equals(edtConfirmationEmail.getText().toString())){
            Toast.makeText(AcountSakuSeiGamen.this, "メール入力エラー", Toast.LENGTH_LONG).show();
            return;
        }
        if (edtEmail.getText().toString().equals(edtConfirmationEmail.getText().toString())){
            String email = edtEmail.getText().toString().trim();
            User user = new User(tenHienThi,email,password,verificationCode,profilePicture,"","");
            Bundle bundle = new Bundle();
            bundle.putSerializable("acountEmail",  user);
            Intent intent = new Intent(AcountSakuSeiGamen.this,PasswordSettei.class);
            intent.putExtras(bundle);
            startActivityForResult(intent,MY_QUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (MY_QUEST_CODE == requestCode && resultCode == Activity.RESULT_OK) {
            User user = (User)data.getExtras().get("acountEmailPassword");
            edtEmail.setText(user.getEmail());
            edtConfirmationEmail.setText(user.getEmail());
            password = user.getPassword();
            verificationCode = user.getVerificationCode();
            profilePicture = user.getProfilePicture();
        }
    }
}