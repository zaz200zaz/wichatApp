package com.example.test3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class testCameOpne extends AppCompatActivity {
    private Bitmap circularBitmap;
    private String urlImageMessage;
    private Uri imagePath;
    private static final int MY_REQUEST_CODE = 1;
    private String curentPhotoPath;//写真の住所
    ImageView imageView3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_came_opne);
        imageView3 = findViewById(R.id.imageView3);
        chupAnhGui();
    }

    private void chupAnhGui() {
        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileName = "photo";
                File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    File file = File.createTempFile(fileName,".jpg",storageDirectory);
                    curentPhotoPath = file.getAbsolutePath();
                    Uri uri = FileProvider.getUriForFile(testCameOpne.this, "com.example.test3.fileprovider", file);
                    imagePath = uri;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                    startActivityForResult(intent,MY_REQUEST_CODE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==MY_REQUEST_CODE&&resultCode==RESULT_OK){
            upLoadImage();
            String sfdsafs = urlImageMessage;
            Bitmap bitmap = BitmapFactory.decodeFile(curentPhotoPath);
            circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 100);
            imageView3.setImageBitmap(circularBitmap);
            imageView3.setVisibility(View.VISIBLE);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void upLoadImage() {

    }
}