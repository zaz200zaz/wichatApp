package com.example.test3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.ProcessCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaswordsChange extends AppCompatActivity {
    private FirebaseUser user ;
    private EditText mkCu;
    private EditText mkMoi;
    private EditText xacNhanMkMoi;
    private Button btAnThayDoiMk;
    private ProgressBar progressBar;
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paswords_change);
        anhXa();
        setTitle("パスワード変更");
        passwordChange();
    }

    public static boolean isValid(final String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private void anhXa() {
        progressBar = findViewById(R.id.progressBar2);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mkCu = findViewById(R.id.editTextTextPersonName2);
        mkMoi = findViewById(R.id.editTextTextPersonName3);
        xacNhanMkMoi = findViewById(R.id.editTextTextPersonName4);
        btAnThayDoiMk = findViewById(R.id.button3);
    }

    private void passwordChange() {
        btAnThayDoiMk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(PaswordsChange.this);
                progressDialog.setTitle("❣❣❣❣パスワード変更中...❣❣❣❣");
                progressDialog.show();
                if (getEmail() != null && getPassword() != null ){
                    if ( newPass() != null){
                        AuthCredential credential = EmailAuthProvider.getCredential(getEmail(),getPassword());
                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    user.updatePassword(newPass()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                Toast.makeText(PaswordsChange.this, "パスワード変更成功", Toast.LENGTH_LONG).show();
                                                thayDoiMkTrongDatabase();
                                                loginOut();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }else{
                        progressDialog.dismiss();
                        TextView textView = findViewById(R.id.textView6);
                        textView.setText("1,パスワードには、少なくとも 1 つの数字 [0-9] が含まれている必要があります。\n" +
                                "2,パスワードには、小文字のラテン文字 [a-z] が少なくとも 1 つ含まれている必要があります。\n" +
                                "3,パスワードには、少なくとも 1 つの大文字のラテン文字 [A-Z] が含まれている必要があります。\n" +
                                "4,パスワードには、.! などの特殊文字が少なくとも 1 つ含まれている必要があります。 @ # & ( )\n" +
                                "5,パスワードの長さは、8 文字以上、最大 20 文字である必要があります。\n" +
                                "6,パスワードが同じじゃないと駄目です。\n" +
                                "7,パスワード欄が空いていたらエラーになります。");
                        textView.setVisibility(View.VISIBLE);
                        Toast.makeText(PaswordsChange.this, "パスワード変更失敗", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(PaswordsChange.this, "何かしらのエラーを出てしまいました❣", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void thayDoiMkTrongDatabase() {
         String UserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
         DatabaseReference mDatabase;
         mDatabase = FirebaseDatabase.getInstance().getReference();
         mDatabase.child("user").child(UserUid).child("password").setValue(mkMoi.getText().toString().trim());
    }

    private void loginOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(PaswordsChange.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private String newPass() {
        String mkmoi1 = mkMoi.getText().toString().trim();
        String xacNhanMkMoi1 = xacNhanMkMoi.getText().toString().trim();
        if (!mkmoi1.isEmpty()&&!xacNhanMkMoi1.isEmpty()){
            if (mkmoi1.equals(xacNhanMkMoi1)){
                if (isValid(mkmoi1)){
                    return mkmoi1;
                }
            }
        }
        return null;
    }

    private String getPassword() {
        String password = mkMoi.getText().toString().trim();
        String password2 = mkCu.getText().toString().trim();
        String password3 = xacNhanMkMoi.getText().toString().trim();
        if (!password.isEmpty()&&!password2.isEmpty()&&!password3.isEmpty()){
            return password2;
        }
        Toast.makeText(this, "パスワード欄を入力してください❣", Toast.LENGTH_SHORT).show();
        return null;

    }

    private String getEmail() {
        if (user != null){
            return user.getEmail();
        }
        Toast.makeText(this, "メールが同じではありません", Toast.LENGTH_SHORT).show();
        return null;
    }

    public void setTitle(String title) {
        TextView textView = new TextView(PaswordsChange.this);
        textView.setText(title);
        textView.setTextSize(20);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(android.R.color.background_light));
        PaswordsChange.this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        PaswordsChange.this.getSupportActionBar().setCustomView(textView);
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

}