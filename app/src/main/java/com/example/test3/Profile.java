package com.example.test3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Profile extends AppCompatActivity {
    private TextView flag;
    private Button btlUpload;
    private ImageView imgProfile;
    private Uri imagePath;
    private TextView textView_profile_name;
    private String UserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String myEmail,myUsername;
    private Button camera;
    private Button album;
    private String curentPhotoPath;
    private int MY_REQUEST_CODE=2;

    private Bitmap circularBitmap;
//    private Bitmap circularBitmap;
    private String urlImageMessage;
//    private Uri imagePath;
//    private static final int MY_REQUEST_CODE = 1;
//    private String curentPhotoPath;//写真の住所
//    ImageView imageView3;
    private static String img;
    private DatabaseReference reference;
    private ValueEventListener valListener;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("個人情報");
        anhXa();
        upImage();
        khiAnVaoAnhThiDiVaoThuVien();
        getAcount();
        chupAnhGui();
        caiDatAnh();
    }

    private void caiDatAnh() {
        reference = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        valListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if (user.getProfilePicture().equals("default")) {
                    ImageView imageView = findViewById(R.id.profile_img);
                    imageView.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getApplicationContext()).load(user.getProfilePicture()).into(imgProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };reference.addValueEventListener(valListener);
    }

    private void chupAnhGui() {
        findViewById(R.id.cameraBtId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileName = "photo";
                File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    File file = File.createTempFile(fileName,".jpg",storageDirectory);
                    curentPhotoPath = file.getAbsolutePath();
                    Uri uri = FileProvider.getUriForFile(Profile.this, "com.example.test3.fileprovider", file);
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

    private void getAcount(){
        FirebaseDatabase.getInstance().getReference("user/"+ FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myEmail = snapshot.getValue(User.class).getEmail();//lấy tên người dùng trong database
                myUsername = snapshot.getValue(User.class).getUsernameAcount();
                img = snapshot.getValue(User.class).getProfilePicture();
                if (myUsername.isEmpty()){
                    textView_profile_name.setText(myEmail);
                }else {
                    textView_profile_name.setText(myUsername);
                }
                return;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setTitle(String title) {
        TextView textView = new TextView(Profile.this);
        textView.setText(title);
        textView.setTextSize(20);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(android.R.color.background_light));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(textView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void khiAnVaoAnhThiDiVaoThuVien() {
        findViewById(R.id.albumBtId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cho 1 hanh dong moi
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                //cai dat loai cho hanh dong cu the o day la hinh anh image
                photoIntent.setType("image/*");
                //cai dat lenh mo file image cua android
                startActivityForResult(photoIntent,1);
//                flag.setText("dsad");
            }
        });
    }

    private void upImage() {
        btlUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag.getText().toString().isEmpty()){
                    Toast.makeText(Profile.this, "写真を選んでください！", Toast.LENGTH_SHORT).show();
                }else{
                    upLoadImage();
                }
            }
        });
    }

    private void anhXa() {

        flag = findViewById(R.id.flag);
        flag.setText("");
        btlUpload = findViewById(R.id.btnUploadImage);
        imgProfile = findViewById(R.id.profile_img);
        textView_profile_name = findViewById(R.id.txtUserEmail);

        camera = findViewById(R.id.cameraBtId);
        album = findViewById(R.id.albumBtId);

        user = new User();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==MY_REQUEST_CODE&&resultCode==RESULT_OK && data != null){
            flag.setText("dsad");
            Bitmap bitmap = BitmapFactory.decodeFile(curentPhotoPath);
            circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 100);
            imgProfile.setImageBitmap(circularBitmap);
            imgProfile.setVisibility(View.VISIBLE);
        }else{
            flag.setText("");
        }
        if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            flag.setText("dsad");
            imagePath = data.getData();
            getImageInImageView();
        }else{
            flag.setText("");
        }

    }

    private void getImageInImageView() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgProfile.setImageBitmap(bitmap);
    }
    //tao boi canh hop thoai tien do
    private void upLoadImage(){
        //this se la lop nay
        ProgressDialog progressDialog = new ProgressDialog(this);
        //dat tieu de cho tien trinh do❣
        progressDialog.setTitle("❣❣❣❣写真アップロード中...❣❣❣❣");
        //hien thi doan hoi thoai do cuoi cung
        progressDialog.show();
        //hinh anh nay o day va chung toi goi tep
        //va ten tep se la duong dan cua chung toi
        FirebaseStorage.getInstance()
                .getReference("images/"+ UUID.randomUUID().toString())
                .putFile(imagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                //neu tac vu thanh cong
                if (task.isSuccessful()){
                    //lưu ảnh đã tải xuống vào image của user và chỉ hiển thị được khi quá trình dowload Url kết thúc
                    //lên phải cài 1 trình nghe hoàn thất
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()){//khi nhiệm vụ thành công thì sẽ update
                                updateProfilePicture(task.getResult().toString());
                            }
                        }
                    });
                    //sau do xuat ra thong báo có nội dung là đã upLoad ảnh thành công
                    Toast.makeText(Profile.this, "写真アップロード成功", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    //thông báo đã xảy ra lỗi
                    Toast.makeText(Profile.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                //cho dù task có thành công hay không thì tiến trình của hộp thoại phải được loại bỏ
                progressDialog.dismiss();
            }
            //đặt trình nghe tiến trinh xem và theo dõi được bao nhiêu ảnh tải lên firebase
            //và khi nó thay đổi nó sẽ cập nhật tiến trình cho người xem
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                //biến kép nghĩa là tiến bộ
                double progress = 100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount();
                //thông báo đại loại, chuyển đổi thành số nguyên và thêm phần trăm khi upLoad ảnh len firebase
                progressDialog.setMessage("アップロード"+(int) progress + "%");
            }
        });
    }
    //chuyển url chuỗi
    private void updateProfilePicture(String ulr){
        //ở đây chúng ta sẽ cài đặt đường dẫn tại đây
        FirebaseDatabase.getInstance().getReference("user/" + UserUid+"/profilePicture").setValue(ulr);
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