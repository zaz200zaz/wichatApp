package com.example.test3;

import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.app.ActivityCompat;
        import androidx.core.content.ContextCompat;

        import android.Manifest;
        import android.app.Activity;
        import android.content.Context;
        import android.content.pm.PackageManager;
        import android.os.Bundle;
        import android.os.Environment;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.BufferedReader;
        import java.io.BufferedWriter;
        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.FileReader;
        import java.io.FileWriter;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.OutputStreamWriter;

public class CreateFolder extends AppCompatActivity {
    EditText editText; EditText txtName; EditText noidungtxt;
    String string;
    Button taoFolder;Button taotxtFile;Button viettxtFile;Button doctxtFile;Button xoa;
    private static final int PER_M_S = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_folder);
        anhXa();
//        taoFolder("tuan");
//        taoFileTxt("test1","tuan");
//        vietTxtFile("1234","test1","tuan");
        taoFolder();
        taoFileTxt();
        vietTxtFile();
        deletetxtFile();
        readFile();
    }
//    private void taoFolder(String tuan) {
//        if (ContextCompat.checkSelfPermission(CreateFolder.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
//            createDirectory(tuan);
//        }else{
//            askPermission();
//        }
//    }
    private void readFile() {
        doctxtFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String past = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+editText.getText().toString().trim()+"/";
                File root = new File(past);
                File gpxfile = new File(root, txtName.getText().toString().trim()+".txt");
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
                    //You'll need to add proper error handling here
                }
                TextView textView = findViewById(R.id.textView);
                textView.setText(text.toString());
            }
        });
    }

    private void deletetxtFile() {
        xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String past = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+editText.getText().toString().trim()+"/";
                File root = new File(past);
                File gpxfile = new File(root, txtName.getText().toString().trim()+".txt");
                gpxfile.delete();
                Toast.makeText(getBaseContext(), "File deleted successfully!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void vietTxtFile() {
        viettxtFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String past = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+editText.getText().toString().trim()+"/";
                    File root = new File(past);
                    File gpxfile = new File(root, txtName.getText().toString().trim()+".txt");
                    FileWriter fileWriter = new FileWriter(gpxfile);
                    fileWriter.append(noidungtxt.getText().toString().trim());
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
        });
    }
//    private void vietTxtFile(String string,String string2,String string3) {
//                try {
//
//                    String past = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+string3+"/";
//
//                    File root = new File(past);
//                    File gpxfile = new File(root, string2+".txt");
//                    FileWriter fileWriter = new FileWriter(gpxfile);
//                    fileWriter.append(string);
//                    fileWriter.flush();
//                    fileWriter.close();
//                    Toast.makeText(getBaseContext(), "File saved successfully!",
//                            Toast.LENGTH_SHORT).show();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//    }
    private void taoFileTxt() {
        taotxtFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String rootPath = Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + "/"+editText.getText().toString().trim()+"/";
                    File root = new File(rootPath);
                    if (!root.exists()) {
                        root.mkdirs();
                    }
                    File f = new File(rootPath + txtName.getText().toString().trim() +".txt");
                    if (f.exists()) {
                        f.delete();
                        Toast.makeText(CreateFolder.this, "tao file txt khong thanh cong", Toast.LENGTH_SHORT).show();
                    }
                    f.createNewFile();
                    Toast.makeText(CreateFolder.this, "tao file txt thanh cong", Toast.LENGTH_SHORT).show();

                    FileOutputStream out = new FileOutputStream(f);

                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
//    private void taoFileTxt(String string,String string2) {
//                try {
//                    String rootPath = Environment.getExternalStorageDirectory()
//                            .getAbsolutePath() + "/"+string2+"/";
//                    File root = new File(rootPath);
//                    if (!root.exists()) {
//                        root.mkdirs();
//                    }
//                    File f = new File(rootPath + string +".txt");
//                    if (f.exists()) {
//                        f.delete();
//                        Toast.makeText(CreateFolder.this, "tao file txt khong thanh cong", Toast.LENGTH_SHORT).show();
//                    }
//                    f.createNewFile();
//                    Toast.makeText(CreateFolder.this, "tao file txt thanh cong", Toast.LENGTH_SHORT).show();
//
//                    FileOutputStream out = new FileOutputStream(f);
//
//                    out.flush();
//                    out.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//    }
    private void taoFolder() {
        taoFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                string = editText.getText().toString().trim();
                if (ContextCompat.checkSelfPermission(CreateFolder.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
                    createDirectory(string);
                }else{
                    askPermission();
                }
            }
        });
    }

    private void anhXa() {
        editText = findViewById(R.id.editTextTextPersonName);
        txtName = findViewById(R.id.editTextTextPersonName3);
        noidungtxt = findViewById(R.id.editTextTextPersonName2);
        taoFolder = findViewById(R.id.button);
        taotxtFile = findViewById(R.id.button2);
        viettxtFile = findViewById(R.id.button3);
        doctxtFile = findViewById(R.id.button4);
        xoa = findViewById(R.id.button5);
    }
//    private String readFromFile(String name) throws IOException {
//        String result= "";
//        InputStream inputStream = openFileInput(name);//"読み込むファイル名.txt"
//        if (inputStream != null) {
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//            String tempString = "";
//            StringBuilder stringBuilder = new StringBuilder();
//
//            while ((tempString = bufferedReader.readLine()) != null) {
//                stringBuilder.append(tempString);
//            }
//            inputStream.close();
//            result = stringBuilder.toString();
//        }
//        return result;
//    }
    private void askPermission() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PER_M_S);
    }

    private void createDirectory(String string) {

        File file = new File(Environment.getExternalStorageDirectory(),string);

        if (!file.exists()){
            file.mkdir();
            Toast.makeText(this, "tao thanh cong", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "tao file ko thanh cong", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==PER_M_S){
            if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                createDirectory(string);
            }else {
                Toast.makeText(this, "Permisson Denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}