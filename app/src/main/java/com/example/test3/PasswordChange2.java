package com.example.test3;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordChange2 extends AppCompatActivity {
    private EditText passwords;
    private EditText passwords2;
    private Button btn;
    private FirebaseUser user ;
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change2);
        anhXa();//ID反射
        thayDoiMatKhau();//パスワード変更関数
    }
    public static boolean isValid(final String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private void thayDoiMatKhau() {
        handleLogin(getIntent().getStringExtra("tk"),getIntent().getStringExtra("mk"));

    }

    private void thayDoiMkTrongDatabase(String s) {
        String UserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("user").child(UserUid).child("password").setValue(s);
    }

    private void anhXa() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        passwords = findViewById(R.id.editTextTextPersonName3);
        passwords2 = findViewById(R.id.editTextTextPersonName4);
        btn = findViewById(R.id.button3);
    }

    private void handleLogin(String tk,String mk) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(tk,mk).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user!=null){
                        passwords.setVisibility(View.VISIBLE);
                        passwords2.setVisibility(View.VISIBLE);
                        btn.setVisibility(View.VISIBLE);
                        Log.d("TAG", "thayDoiMatKhau: true");
                        doiMatKhau();
                    }else {
                        Log.d("TAG", "thayDoiMatKhau: false");
                    }
                    Log.d(TAG, "onComplete: dang nhap thanh cong");
                    Toast.makeText(PasswordChange2.this,"パスワード変更成功 ",Toast.LENGTH_SHORT).show();
                }else{
                    Log.d(TAG, "onComplete: "+task.getException().getLocalizedMessage());
                    Toast.makeText(PasswordChange2.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void doiMatKhau() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(PasswordChange2.this);
                progressDialog.setTitle("❣❣❣❣パスワード再設定中...❣❣❣❣");
                progressDialog.show();
                user = FirebaseAuth.getInstance().getCurrentUser();
                String mk = getIntent().getStringExtra("mk");
                String tk = getIntent().getStringExtra("tk");
                Log.d("TAG", "onClick: "+mk);
                Log.d("TAG", "onClick: "+tk);
                if (tk != null && !mk.isEmpty() && newPass() != null){
                    AuthCredential credential = EmailAuthProvider.getCredential(tk,mk);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                user.updatePassword(newPass()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            TextView txtError = findViewById(R.id.txtErrorId);
                                            txtError.setVisibility(View.GONE);
                                            Toast.makeText(PasswordChange2.this, "パスワード変更成功", Toast.LENGTH_LONG).show();
                                            thayDoiMkTrongDatabase(newPass());
                                            Log.d("TAG", "onClick: "+newPass());
                                            startActivity(new Intent(PasswordChange2.this,FragmentNavigationBar.class));
                                            finish();
                                        } else {
                                            Toast.makeText(PasswordChange2.this, "パスワード変更失敗", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }else{
                    TextView txtError = findViewById(R.id.txtErrorId);
                    txtError.setText("1,パスワードには、少なくとも 1 つの数字 [0-9] が含まれている必要があります。\n" +
                            "2,パスワードには、小文字のラテン文字 [a-z] が少なくとも 1 つ含まれている必要があります。\n" +
                            "3,パスワードには、少なくとも 1 つの大文字のラテン文字 [A-Z] が含まれている必要があります。\n" +
                            "4,パスワードには、.! などの特殊文字が少なくとも 1 つ含まれている必要があります。 @ # & ( )\n" +
                            "5,パスワードの長さは、8 文字以上、最大 20 文字である必要があります。");
                    txtError.setVisibility(View.VISIBLE);
                    Toast.makeText(PasswordChange2.this, "エラー❣", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    private String newPass() {
        String mkmoi1 = passwords.getText().toString().trim();
        String xacNhanMkMoi1 = passwords2.getText().toString().trim();
        if (!mkmoi1.isEmpty()&&!xacNhanMkMoi1.isEmpty()){
            if (mkmoi1.equals(xacNhanMkMoi1)){
                if (isValid(mkmoi1)){
                    return mkmoi1;
                }
            }
        }
        return null;
    }

}