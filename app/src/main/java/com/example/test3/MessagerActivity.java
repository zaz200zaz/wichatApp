package com.example.test3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class MessagerActivity extends AppCompatActivity {
    private Bitmap circularBitmap;
    private String urlImageMessage;
    private boolean ImageOrMessage;
    private String UserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private Uri imagePath;
    private static final int MY_REQUEST_CODE = 1;
    private String curentPhotoPath;//写真の住所
    private int i =1;
    private CardView cardView;
    private ImageView ButtonSendCamera;
    private ImageView imgSendMessageView;

    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private EditText edtMessageInput;
    private TextView txtChattingWith;
    private ProgressBar progressBar;

    private ImageView imgToolbar,imgSend;

    private ImageView cameraImageView1;

    private MessageAdapter messageAdapter;
    private ImageMessageAdapter imageMessageAdapter;
    private ArrayList<Message> message;
    private ArrayList<String> chatRoomName;

    private Message message2;

    String usernameOfTheRoommate,emailOfRoommate,chatRoomId;
    private FirebaseUser userDatabase =FirebaseAuth.getInstance().getCurrentUser();
    private ImageButton imgSendCamera;
    private String anhNguoiNhan;private String tenNguoigui;
    //max alex => phòng phải có tên là alexmax

    //id of the chat room for max and alex

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messager);
        setTitle("メッセージ画面");
        anhXa();
        layDuLieuDuocGuiTuChatFragment();
        bamVaoDauCongThiHienThiCameraBar();
        caiDatTenCuaNguoiDangChatVoiMinh();
        //đặt trình nghe onclickListen
        guiTinNhan();
        setUpChatRoom();
        chupAnhGui();
    }
    public void setTitle(String title) {
        TextView textView = new TextView(MessagerActivity.this);
        textView.setText(title);
        textView.setTextSize(20);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(android.R.color.background_light));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(textView);
    }
    private void caiDatTenCuaNguoiDangChatVoiMinh() {
        String name = getIntent().getStringExtra("username_of_roomate");
        if (name.isEmpty()){
            txtChattingWith.setText(getIntent().getStringExtra("email_of_roommate"));
        }else{
            txtChattingWith.setText(getIntent().getStringExtra("username_of_roomate"));
        }

    }
    private void bamVaoDauCongThiHienThiCameraBar() {
        ButtonSendCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraBar();
            }
        });
    }
    private void layDuLieuDuocGuiTuChatFragment() {
        usernameOfTheRoommate =catEmail(getIntent().getStringExtra("email_of_roommate"));
        anhNguoiNhan = getIntent().getStringExtra("img_of_roommate");
//        usernameOfTheRoommate = getIntent().getStringExtra("username_of_roomate");
        emailOfRoommate = getIntent().getStringExtra("email_of_roommate");
        tenNguoigui = getIntent().getStringExtra("username_of_roomate");
    }
    private void guiTinNhan() {
        imgSend.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                //tạo bảng messages với tên phòng chát
                addMassage();
            }
        });
        messageAdapter = new MessageAdapter(message,getIntent().getStringExtra("my_image2"),getIntent().getStringExtra("img_of_roommate"),MessagerActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);
        Glide.with(MessagerActivity.this).load(getIntent().getStringExtra("img_of_roommate")).placeholder(R.drawable.account_img).error(R.drawable.account_img).into(imgToolbar);
    }
    private void chupAnhGui() {
        cameraImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileName = "photo";
                File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    File file = File.createTempFile(fileName,".jpg",storageDirectory);
                    curentPhotoPath = file.getAbsolutePath();
                    Uri uri = FileProvider.getUriForFile(MessagerActivity.this, "com.example.test3.fileprovider", file);
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
    private String catEmail(String username_of_roomate) {
        String [] list = username_of_roomate.split("\\@",2);
        return list[0];
    }
    private void anhXa() {
        chatRoomName = new ArrayList<>();
        urlImageMessage = "";
        ImageOrMessage = true;
        imgSendMessageView= findViewById(R.id.imgSendMessageView);
        cameraImageView1 = findViewById(R.id.imageView1);
        ButtonSendCamera = findViewById(R.id.imgSendCamera);
        cardView = findViewById(R.id.cardView);
        recyclerView = findViewById(R.id.recyclerviewMessages);
        imgSend = findViewById(R.id.imgSendMessage);
        edtMessageInput = findViewById(R.id.edtText);
        txtChattingWith = findViewById(R.id.txtChattingwWith);
        progressBar = findViewById(R.id.progressMessager);
        imgToolbar = findViewById(R.id.img_toolbar);

        //trò chuyện với thanh người dùng
        txtChattingWith.setText(usernameOfTheRoommate);
        message = new ArrayList<>();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String Time() {

        DateTimeFormatter dtf =  DateTimeFormatter.ofPattern("HH:mm:ss,yyyy/MM/dd");
        DateTimeFormatter formatter = DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(Locale.getDefault(Locale.Category.FORMAT));

        ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.systemDefault());

        String formattedDate = dateTime.format(dtf);
        String string = formattedDate;
        String[] parts = string.split(",", 2);
        String part1 = parts[0]; // time00：00：00
        String part2 = parts[1]; // 日月年2022/08/23
        String Time[] = part1.split(":");
        return Time[0]+":"+Time[1];
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String Year() {

        DateTimeFormatter dtf =  DateTimeFormatter.ofPattern("HH:mm:ss,yyyy/MM/dd");

        DateTimeFormatter formatter = DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(Locale.getDefault(Locale.Category.FORMAT));

        ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.systemDefault());

        String formattedDate = dateTime.format(dtf);
        String string = formattedDate;
        String[] parts = string.split(",", 2);
        String part1 = parts[0]; // time00：00：00
        String part2 = parts[1]; // 日月年2022/08/23
        String Time[] = part1.split(":");
        return part2;
    }
    //lấy tên người dùng
    private void setUpChatRoom(){
        //FirebaseDatabase.getInstance().getReference lấy dữ liệu từ firebase theo đường dẫn
        FirebaseDatabase.getInstance().getReference("user/"+ FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String myUsername = catEmail(snapshot.getValue(User.class).getEmail());//tranthanhtuan199981@gmail.com  ->tranthanhtuan199981
                if (usernameOfTheRoommate.compareTo(myUsername)>0){//max compe alex
                    chatRoomId = myUsername + usernameOfTheRoommate;
                }else if (usernameOfTheRoommate.compareTo(myUsername)==00){//alex alex
                    chatRoomId = myUsername + usernameOfTheRoommate;
                }else{
                    chatRoomId = usernameOfTheRoommate+myUsername; //alex max
                }
                attachMessageListener(chatRoomId);
                layTenNguoiNhan(chatRoomId,getIntent().getStringExtra("email_of_roommate"),snapshot.getValue(User.class).getProfilePicture());//dia chi phong chat-nguoi chat-anh
                if (chatRoomName==null){
                    chatRoomName.add(chatRoomId);
                }else{
                    for (String kiemTra:chatRoomName
                    ) {
                        if (kiemTra.equals(chatRoomId)) {
                            break;
                        }else{
                            chatRoomName.add(chatRoomId);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void attachMessageListener(String chatRoomId){//ai chát với
        FirebaseDatabase.getInstance().getReference("messages/"+chatRoomId).addValueEventListener(new ValueEventListener() {//tạo phòng chát với chatroomID
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //dữ liêu sẽ thay đổi mỗi khi nhận được tin nhắn mới
                message.clear(); //xóa sạch mảng tin nhắn
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    message.add(dataSnapshot.getValue(Message.class));//thêm tin nhắn vào trong mảng mesage
                }
                //sau khi nhận được thông báo
                messageAdapter.notifyDataSetChanged();//thông báo Đã thay đổi tập dữ liệu
                //cho dù có nhiều tin nhắn đến bao nhiêu thì khi xem sẽ là tin nhắn cuối cùng
                recyclerView.scrollToPosition(message.size()-1);//tin nhắn sẽ được hiển thị từ dưới lên
                recyclerView.setVisibility(View.VISIBLE);//hiển thị recylerView
                progressBar.setVisibility(View.GONE);//che progressBar đi
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void layTenNguoiNhan(String chatRoomId,String TenNguoiNhan,String profire){
        if (tenNguoigui.isEmpty()){
            FirebaseDatabase.getInstance().getReference("roomNametesr/"+chatRoomId).setValue(new FineData(chatRoomId,TenNguoiNhan,"",profire));
        }else{
            FirebaseDatabase.getInstance().getReference("roomNametesr/"+chatRoomId).setValue(new FineData(chatRoomId,tenNguoigui,"",profire));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addMassage(){
        DatabaseReference diaChiPhongChat = FirebaseDatabase.getInstance().getReference("messages/"+chatRoomId).push();
        diaChiPhongChat.setValue(new Message(userDatabase.getEmail(),emailOfRoommate,edtMessageInput.getText().toString(),anhNguoiNhan,Time(),Year()));

        imgSendMessageView.setVisibility(View.GONE);
        edtMessageInput.setText("");
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addImgMassage(String s){
        DatabaseReference diaChiPhongChat = FirebaseDatabase.getInstance().getReference("messages/"+chatRoomId).push();
        diaChiPhongChat.setValue(new Message(userDatabase.getEmail(),emailOfRoommate,"写真",s,Time(),Year()));
        imgSendMessageView.setVisibility(View.GONE);
        edtMessageInput.setText("");
    }

    private void cameraBar(){
        if (i>2){
            i=0;
        }else{
            i++;
        }
        if (i%2==0){
            cardView.setVisibility(View.VISIBLE);
        }else  cardView.setVisibility(View.GONE);
    }
    private void BotronAnh(){
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.kirito);
        Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 100);
        ImageView circularImageView = (ImageView) findViewById(R.id.imageView);
        circularImageView.setImageBitmap(circularBitmap);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==MY_REQUEST_CODE&&resultCode==RESULT_OK){
            upLoadImage();
            String sfdsafs = urlImageMessage;
            Bitmap bitmap = BitmapFactory.decodeFile(curentPhotoPath);
            circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 100);
            imgSendMessageView.setImageBitmap(circularBitmap);
            imgSendMessageView.setVisibility(View.VISIBLE);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void upLoadImage(){
        ProgressDialog progressDialog = new ProgressDialog(this);
        //dat tieu de cho tien trinh do
        progressDialog.setTitle("送信中...");
        //hien thi doan hoi thoai do cuoi cung
        progressDialog.show();
        FirebaseStorage.getInstance().getReference("images/"+ UUID.randomUUID().toString()).putFile(imagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                //neu tac vu thanh cong
                if (task.isSuccessful()){
                    //lưu ảnh đã tải xuống vào image của user và chỉ hiển thị được khi quá trình dowload Url kết thúc
                    //lên phải cài 1 trình nghe hoàn thất
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @SuppressLint("NewApi")
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()){//khi nhiệm vụ thành công thì sẽ update
                                //updateProfilePicture(task.getResult().toString());
                                urlImageMessage =task.getResult().toString();
                                updateProfilePicture(urlImageMessage);
                            }
                        }
                    });
                    //sau do xuat ra thong báo có nội dung là đã upLoad ảnh thành công
                    Toast.makeText(MessagerActivity.this, "写真が送信された", Toast.LENGTH_SHORT).show();
                }else{
                    //thông báo đã xảy ra lỗi
                    Toast.makeText(MessagerActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
                progressDialog.setMessage("送信("+(int) progress + "%)");
            }
        });
    }
    //chuyển url chuỗi
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateProfilePicture(String ulr){
        addImgMassage(ulr);
    }
}