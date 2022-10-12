package com.example.test3;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class NinSyouGamen extends AppCompatActivity {

    private static final int MY_QUEST_CODE = 10;
    private static final int PER_M_S = 7;
    private String UsernameAcount;//tên
    private String Email;//email
    private String Password;
    private String edtConfirmationPassword;
    private int verificationCode;
    private String profilePicture;
    private EditText edtVerificationCode;
    private TextView txtVerificationCode;

    private Button finishButton;
    private Button backButton;//nút quay về

    private Button button;
    private ImageButton verificationCodeChangeImageButton;
    private User user;
    AcountSakuSeiGamen acountSakuSeiGamen;
    PasswordSettei passwordSettei;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nin_syou_gamen);
        setTitle("認証");
        anhxa();
        dataInput();
        signUpInHandle();
        verificationCodeChangeHandle();
    }
    private void setTitle(String title) {
        TextView textView = new TextView(NinSyouGamen.this);
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
        Toast.makeText(NinSyouGamen.this, "戻る", Toast.LENGTH_SHORT).show();
        User user = new User(UsernameAcount,Email,Password,verificationCode,profilePicture,"","");
        Bundle bundle = new Bundle();
        bundle.putSerializable("emailAcountPasswordverificationCode",user);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK,intent);
        finish();
        return super.onSupportNavigateUp();
    }
    private void verificationCodeChangeHandle() {
        verificationCodeChangeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificationCode = random();
                txtVerificationCode.setText(verificationCode+"");
            }
        });
    }

    private void signUpInHandle() {
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(NinSyouGamen.this);
                progressDialog.setTitle("❣❣❣❣アカウント作成中...❣❣❣❣");
                progressDialog.show();
                handleSignUp();
                progressDialog.dismiss();
            }
        });
    }

    private void dataInput() {
        UsernameAcount = "";
        Email = user.getEmail();
        Password = user.getPassword();
        verificationCode = user.getVerificationCode();
        profilePicture = user.getProfilePicture();
        edtVerificationCode.setText("");

        verificationCode = random();
        txtVerificationCode.setText(verificationCode+"");
    }

    private void anhxa(){
        user = (User) getIntent().getExtras().get("acountEmailPassword");
        UsernameAcount = "";
        Email = "";
        Password = "";
        verificationCode =0;
        profilePicture = "";

        backButton= findViewById(R.id.backButton);
        edtVerificationCode = findViewById(R.id.edtVerificationCode);
        txtVerificationCode = findViewById(R.id.txtVerificationCode);

        finishButton = findViewById(R.id.finishButton);
        verificationCodeChangeImageButton = findViewById(R.id.verificationCodeChangeImageButton);
    }

    private int random(){
        return (int) Math.floor(((Math.random()*899999)+100000));
    }

    private void handleSignUp(){
        if (edtVerificationCode.getText().toString().isEmpty()||Email.toString().isEmpty()||
                Password.toString().isEmpty()){
            Toast.makeText(this, "入力エラー", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!edtVerificationCode.getText().toString().trim().equals(txtVerificationCode.getText().toString().trim())){
            Toast.makeText(this, "認証コード入力エラー", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(Email.toString(),Password.toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase
                            .getInstance()
                            .getReference("user/"+FirebaseAuth.getInstance()
                                    .getCurrentUser()
                                    .getUid())
                            .setValue(new User(
                                    "",
                                    Email.toString(),
                                    Password.toString(),
                                    verificationCode,"","","")
                            );
                    luuFileVerificationCodeVaoMay(Email,verificationCode);
                    startActivity(new Intent(NinSyouGamen.this,FragmentNavigationBar.class));
                    Toast.makeText(NinSyouGamen.this,"アカウント作成成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(NinSyouGamen.this,"登録するメールは既に存在しています",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void luuFileVerificationCodeVaoMay(String email, int verificationCode) {
//        String data = "メールの名前："+email+"\n"+"認証コード:"+verificationCode+"\n";
        if (ContextCompat.checkSelfPermission(NinSyouGamen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/","WICHAT");
            if (!file.exists()){
                file.mkdir();
            }
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/WICHAT/","認証コード.txt");
            if (!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            viet(email,verificationCode);
        }else{
            askPermission();
        }

    }

    private void viet(String Email,int verificationCode) {

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                +"/WICHAT/");
        try {
            File gpxfile = new File(file, "認証コード.txt");
            StringBuilder text = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(gpxfile));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
            String data = "メールの名前："+Email+"\n"+"認証コード:"+verificationCode+"\n";
            if (text.indexOf(Email)!=(-1)){
                Toast.makeText(this, "du lieu da ton tai", Toast.LENGTH_SHORT).show();
            }else{
                FileWriter fileWriter = new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath()+"/WICHAT/"+"認証コード.txt");
                fileWriter.append(text+data);
                fileWriter.flush();
                fileWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    private void vietTxtFile(String string,String string2,String string3) {
//        try {
//
//            String past = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+string3+"/";
//
//            File root = new File(past);
//            File gpxfile = new File(root, string2+".txt");
//            FileWriter fileWriter = new FileWriter(gpxfile);
//            fileWriter.append(string);
//            fileWriter.flush();
//            fileWriter.close();
//            Toast.makeText(getBaseContext(), "File saved successfully!",
//                    Toast.LENGTH_SHORT).show();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

//    private void taoFileTxt(String txtFileCodeName,String wiChatFolderName,String data) {
//        try {
//            String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+wiChatFolderName;
////            File root = new File(rootPath);
//            File root = new File(rootPath,txtFileCodeName);
//            if (!root.exists()) {
//                root.mkdirs();
//                File f = new File(rootPath + string +".txt");
//                if (f.exists()) {
//                    Toast.makeText(NinSyouGamen.this, "tao file txt khong thanh cong", Toast.LENGTH_SHORT).show();
//                }else {
//                    f.createNewFile();
//                    FileOutputStream out = new FileOutputStream(f);
//                    out.flush();
//                    out.close();
//                }
//            }
//            FileWriter fileWriter = new FileWriter(gpxfile);
//            fileWriter.append(string);
//            fileWriter.flush();
//            fileWriter.close();
//            else {
//                File f = new File(rootPath + txtFileCodeName +".txt");
//                if (f.exists()) {
//                    Toast.makeText(NinSyouGamen.this, "tao file txt khong thanh cong", Toast.LENGTH_SHORT).show();
//                }else {
//                    f.createNewFile();
//                    FileOutputStream out = new FileOutputStream(f);
//
//                    out.flush();
//                    out.close();
//                }
//            }
//            vietTxtFile();

//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private void taoFolder(String data,String txtFileCodeName,String wiChatFolderName) {
//        if (ContextCompat.checkSelfPermission(NinSyouGamen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
//            createDirectory(wiChatFolderName,txtFileCodeName,data);
//        }else{
//            askPermission();
//        }
//    }
    private void askPermission() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PER_M_S);
        Log.d(TAG, "askPermission: askPermission");
    }
//    private void createDirectory(String wiChatFolderName, String txtFileCodeName, String data) {
//        String part = Environment.getExternalStorageDirectory()+"/"+wiChatFolderName;
//        File file = new File(Environment.getExternalStorageDirectory(),wiChatFolderName);
//
//        if (!file.exists()){
//            file.mkdir();
//            Toast.makeText(this, "tao thanh cong", Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "createDirectory: tao thu muc thanh cong");
//        }
//        file = new File(Environment.getExternalStorageDirectory(),wiChatFolderName);
//        taoFileTxt(txtFileCodeName,wiChatFolderName,data);
//
//
//    }
//
//    private void backActivity() {
//
//        Toast.makeText(NinSyouGamen.this, "戻る", Toast.LENGTH_SHORT).show();
//        User user = new User(UsernameAcount,Email,Password,verificationCode,profilePicture,"","");
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("emailAcountPasswordverificationCode",user);
//        Intent intent = new Intent();
//        intent.putExtras(bundle);
//        setResult(Activity.RESULT_OK,intent);
//        finish();
//    }
}