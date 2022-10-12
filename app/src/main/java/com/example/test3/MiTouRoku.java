package com.example.test3;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MiTouRoku extends AppCompatActivity {
    private static final int PER_M_S = 7;
    private ArrayList<User> users2;  //mảng danh sách người dùng
    private DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance().getReference("user");
    private EditText edtEmail;
    private EditText edtPassword;
    private Button loginButton;
    private Button forgotPasswordButton;
    private Button createAcountButton;
    private int REQUET_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_tou_roku);
        setTitle("ログイン画面");
        anhXa();
        phanDoanXemDaDangNhapHayChua();
        dangNhap();
        quenMatKhau();
        taoTaiKhoan();
    }

    public void setTitle(String title) {
        TextView textView = new TextView(MiTouRoku.this);
        textView.setText(title);
        textView.setTextSize(20);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(android.R.color.background_light));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(textView);
    }

    private void taoTaiKhoan() {
        createAcountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MiTouRoku.this,AcountSakuSeiGamen.class);
                startActivity(intent);
            }
        });
    }

    private void quenMatKhau() {
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MiTouRoku.this,AcountNinSyou.class);
                intent.putExtra("tk",edtEmail.getText().toString().trim())
                                .putExtra("mk",edtPassword.getText().toString().trim());
                startActivity(intent);
            }
        });
    }

    private void dangNhap() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtEmail.getText().toString().isEmpty()||edtPassword.getText().toString().isEmpty()){
                    Toast.makeText(MiTouRoku.this, "1invalid input", Toast.LENGTH_SHORT).show();
                    return;
                }
                handleLogin();
            }
        });
    }

    private void phanDoanXemDaDangNhapHayChua() {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(MiTouRoku.this,FragmentNavigationBar.class));
            finish();
        }
    }

    private void anhXa(){
        users2 = new ArrayList<>();
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtTextPassword);
        loginButton = findViewById(R.id.loginButton);
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        createAcountButton = findViewById(R.id.createAcountButton);
    }

    private void handleLogin(){
        ProgressDialog progressDialog = new ProgressDialog(MiTouRoku.this);
        progressDialog.setTitle("❣❣❣❣ログイン中...❣❣❣❣");
        progressDialog.show();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(MiTouRoku.this,FragmentNavigationBar.class));finish();
                    Toast.makeText(MiTouRoku.this,"ログイン成功",Toast.LENGTH_SHORT).show();
                    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                    getUsers(email);
                }else{
                    Toast.makeText(MiTouRoku.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    private  void getUsers(String EmailForUser){
        users2.clear();//xóa sạch người dùng
        //lấy dữ liệu từ user
        databaseReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    users2.add(dataSnapshot.getValue(User.class));
                }
                //lấy ảnh người gửi
                for (User user: users2){
                    if (user.getEmail().equals(EmailForUser)){
                        String Email = String.valueOf(user.getEmail());
                        String verificationCode = String.valueOf(user.getVerificationCode());
                        luuFileVerificationCodeVaoMay(Email,verificationCode,"wiChat","認証コード");
                        return;
                    }
                }
            }

            private void luuFileVerificationCodeVaoMay(String email, String verificationCode, String wiChat, String code) {
                String sada = email;
                String data = readFile(wiChat,code);
                if(data.indexOf(email)==-1){
                    Log.d(TAG, "luuFileVerificationCodeVaoMay: "+data);
                    String sada2 = data+"\n"+"メールの名前："+email+"\n"+"認証コード:"+String.valueOf(verificationCode)+"\n";
                    String sada3 = wiChat;
                    String sada4 = code;
                    taoFolder(wiChat);
                    taoFileTxt(code,wiChat);
                    vietTxtFile(sada2,code,wiChat);
                }else{

                }

            }

            private void vietTxtFile(String string,String string2,String string3) {
                try {

                    String past = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+string3+"/";

                    File root = new File(past);
                    File gpxfile = new File(root, string2+".txt");
                    FileWriter fileWriter = new FileWriter(gpxfile);
                    fileWriter.append(string);
                    fileWriter.flush();
                    fileWriter.close();
                    Toast.makeText(getBaseContext(), "File saved successfully!",
                            Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            private void taoFolder(String tuan) {
                if (ContextCompat.checkSelfPermission(MiTouRoku.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {

                    createDirectory(tuan);

                }else{

                    askPermission();
                }

            }
            private void createDirectory(String string) {

                File file = new File(Environment.getExternalStorageDirectory(),string);

                if (!file.exists()){
                    file.mkdir();
                    Toast.makeText(MiTouRoku.this, "tao thanh cong", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "createDirectory: tao thu muc thanh cong");
                }else {
                    taoFileTxt("code","wiChat");
                }

            }

            private void taoFileTxt(String string,String string2) {
                try {
                    String rootPath = Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + "/"+string2+"/";
                    File root = new File(rootPath);
                    if (!root.exists()) {
                        root.mkdirs();

                        File f = new File(rootPath + string +".txt");
                        if (f.exists()) {

                            Toast.makeText(MiTouRoku.this, "tao file txt khong thanh cong", Toast.LENGTH_SHORT).show();
                        }else {
                            f.createNewFile();
                            FileOutputStream out = new FileOutputStream(f);

                            out.flush();
                            out.close();
                        }

                    }else {
                        File f = new File(rootPath + string +".txt");
                        if (f.exists()) {
//                    f.delete();

                            Toast.makeText(MiTouRoku.this, "tao file txt khong thanh cong", Toast.LENGTH_SHORT).show();
                        }else {
                            f.createNewFile();
                            FileOutputStream out = new FileOutputStream(f);

                            out.flush();
                            out.close();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            private void askPermission() {
                ActivityCompat.requestPermissions(MiTouRoku.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PER_M_S);
                Log.d(TAG, "askPermission: askPermission");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MiTouRoku.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String readFile(String tenFolder,String tenFileTxt) {
        String past = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+tenFolder+"/";
        File root = new File(past);
        File gpxfile = new File(root, tenFileTxt+".txt");
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(gpxfile));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
        }
        String dasdsa = text.toString();
        return dasdsa;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }
}