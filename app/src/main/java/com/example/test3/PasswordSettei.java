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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordSettei extends AppCompatActivity {
    private static final int MY_QUEST_CODE = 10;
    private Button nextButton;
    private String UsernameAcount;//tên
    private String Email;//email
    private EditText edtPassword;
    private EditText edtConfirmationPassword;
    private int verificationCode;
    private String profilePicture;
    private User user;
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_settei);
        setTitle("パスワード設定");//タイトル設定
        anhxa();//ID反射
        addData();//必要なデータ取得
        next();//次へ押されたらの処理関数
    }

    public static boolean isValid(final String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private void next() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity();
            }
        });
    }

    private void setTitle(String title) {
        TextView textView = new TextView(PasswordSettei.this);
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
        Toast.makeText(PasswordSettei.this, "戻る", Toast.LENGTH_SHORT).show();
        String S_password = edtPassword.getText().toString().trim();
        User user = new User(UsernameAcount,Email,S_password,verificationCode,profilePicture,"","");
        Bundle bundle = new Bundle();
        bundle.putSerializable("acountEmailPassword",user);
        Intent intent = new Intent(PasswordSettei.this,NinSyouGamen.class);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK,intent);
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.blank_menu,menu);
        return true;
    }

    private void addData() {
        UsernameAcount = user.getUsernameAcount();
        Email = user.getEmail();
        edtPassword.setText(user.getPassword());
        edtConfirmationPassword.setText(user.getPassword());
        verificationCode = user.getVerificationCode();
        profilePicture = user.getProfilePicture();
    }

    private void anhxa(){
        user = (User) getIntent().getExtras().get("acountEmail");
        UsernameAcount = "";
        Email = "";
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmationPassword=findViewById(R.id.edtConfirmationPassword);
        verificationCode = 0;
        profilePicture = "";

        nextButton = findViewById(R.id.nextButton);
    }

    private void nextActivity(){
        String s1 = edtPassword.getText().toString().trim();
        String s2 = edtConfirmationPassword.getText().toString().trim();

        if (s1.isEmpty()|| s2.isEmpty()){
            Toast.makeText(PasswordSettei.this, "値がない所発見", Toast.LENGTH_SHORT).show();
            return;
        }
        if (s1.equals(s2)){
            if ( isValid(s1) && isValid(s2)){
                TextView txtError = findViewById(R.id.txtErrorId);
                txtError.setVisibility(View.GONE);
                Toast.makeText(PasswordSettei.this, "パスワード入力成功", Toast.LENGTH_SHORT).show();
                String S_password = s1;
                User user = new User(UsernameAcount,Email,S_password,verificationCode,profilePicture,"","");
                Bundle bundle = new Bundle();
                bundle.putSerializable("acountEmailPassword",  user);
                Intent intent = new Intent(PasswordSettei.this,NinSyouGamen.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,MY_QUEST_CODE);
            }else{
                TextView txtError = findViewById(R.id.txtErrorId);
                txtError.setText("1,パスワードには、少なくとも 1 つの数字 [0-9] が含まれている必要があります。\n" +
                        "2,パスワードには、小文字のラテン文字 [a-z] が少なくとも 1 つ含まれている必要があります。\n" +
                        "3,パスワードには、少なくとも 1 つの大文字のラテン文字 [A-Z] が含まれている必要があります。\n" +
                        "4,パスワードには、.! などの特殊文字が少なくとも 1 つ含まれている必要があります。 @ # & ( )\n" +
                        "5,パスワードの長さは、8 文字以上、最大 20 文字である必要があります。");
                txtError.setVisibility(View.VISIBLE);
                return;
            }
        }else{
            Toast.makeText(PasswordSettei.this, "パスワード同じじゃない", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (MY_QUEST_CODE==requestCode && resultCode == Activity.RESULT_OK){

        }
    }
}