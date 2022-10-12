package com.example.test3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RenrakuSakiGamen2 extends AppCompatActivity {
    private String timKiem;
    private RecyclerView recyclerView;//hiển thị danh sách bạn bè
    protected FineAdapter fineAdapter;//bộ điều hợp người dùng
    private ArrayList<Message> messageLayDulieu;
    private ArrayList<FineData>tenCacPhongChat;//ten cac phong chat
    private ArrayList<String>roomName;
    protected int count;
    protected String TenPhongchat;
    protected int arrLength;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference myRef2;
    protected ArrayList<FineData> fineData;
    private RecyclerView.LayoutManager linearLayoutManager;
    ArrayList<FineData> fineData324;
    private TextView edtKensaku;

    private ArrayList<User> users;  //mảng danh sách người dùng
    private DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance().getReference("user");
    private ArrayList<User> users2;  //mảng danh sách người dùng
    private RenrakuSakiAdapter userAdapter;//bộ điều hợp người dùng
    RenrakuSakiAdapter.OnUserClickListener onUserClickListener;//khi mà ấn vào từng người dùng
    String myImageUrl;
    private String EmailForUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renraku_saki_gamen2);
        setTitle("該当する友達検索画面");
        AnhXa();
        layDuLieuTimKiemNguoiDung();
        getUsers();
        khiAnVaoTungThanhVien();
        quayTroVeMainActivity();
    }

    public void setTitle(String title) {
        TextView textView = new TextView(RenrakuSakiGamen2.this);
        textView.setText(title);
        textView.setTextSize(20);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(android.R.color.background_light));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(textView);
    }

    private void quayTroVeMainActivity() {
        findViewById(R.id.img_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(RenrakuSakiGamen2.this,MainActivity.class));
                finish();
            }
        });
    }

    private void layDuLieuTimKiemNguoiDung() {
        timKiem= getIntent().getStringExtra("KhoaTimKiem").trim();
        edtKensaku.setText(timKiem);
    }

    private void AnhXa(){
        users = new ArrayList<>();users2 = new ArrayList<>();
        edtKensaku = findViewById(R.id.edtKensaku);
        fineData324 = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        fineData = new ArrayList<>();
        TenPhongchat = "";
        arrLength = 0;
        count = 0;
        database = FirebaseDatabase.getInstance();
        roomName = new ArrayList<>();
        tenCacPhongChat= new ArrayList<>();
        messageLayDulieu = new ArrayList<>();
    }
    private ArrayList<FineData> getFineMessagetest(String chatrmmm,String tenNguoiNhan,String noiDung,String image) {
        fineData324 .add(new FineData(chatrmmm,tenNguoiNhan,noiDung,image));
        return fineData324;
    }
//    private void layTenPhongChat(String hhhh){
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        myRef = database.getReference("roomNametesr");
//        tenCacPhongChat.clear();fineData.clear();
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange( @NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot dataSnapshot1hot: dataSnapshot.getChildren()){
//                    tenCacPhongChat.add(dataSnapshot1hot.getValue(FineData.class));//thêm tin nhắn vào trong mảng mesage
//                }
//                int soLuongPhong = tenCacPhongChat.size();
//                arrLength = soLuongPhong;
//                for (int i = 0; i < tenCacPhongChat.size(); i++) {
//                    messageLayDulieu.clear();
//                    String nguoiGui;
//                    nguoiGui =tenCacPhongChat.get(i).getSendPerson();
//                    myRef2 = database.getReference("messages/"+tenCacPhongChat.get(i).getChatRoomName());
//                    myRef2.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange( @NonNull DataSnapshot dataSnapshot) {
//                            for (DataSnapshot dataSnapshot1hot: dataSnapshot.getChildren()){
//                                messageLayDulieu.add(dataSnapshot1hot.getValue(Message.class));//thêm tin nhắn vào trong mảng mesage
//                            }
//                            count++;
//                            for ( int i = 0; i<messageLayDulieu.size();i++){
//                                if (messageLayDulieu.get(i).getConten().indexOf(hhhh)>-1){
//                                    fineAdapter = new FineAdapter(getFineMessagetest("",nguoiGui,messageLayDulieu.get(i).getConten()));
//                                    roomName.add("sender:"+nguoiGui+"=>"+"message:"+messageLayDulieu.get(i).getConten()+"\n");
//                                }
//                            }
//                            RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(RenrakuSakiGamen2.this,DividerItemDecoration.VERTICAL);
//                            recyclerView.addItemDecoration(itemDecoration);
//                            recyclerView.setLayoutManager(linearLayoutManager);
//                            recyclerView.setAdapter(fineAdapter);
//                            recyclerView.setVisibility(View.VISIBLE);
//                            messageLayDulieu.clear();
//                        }
//                        @Override
//                        public void onCancelled(DatabaseError error) {
//                            Log.w("121212", "Failed to read value.", error.toException());
//                        }
//                    });
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError error) {
//            }
//        });
//    }

    private void khiAnVaoTungThanhVien() {
        onUserClickListener = new RenrakuSakiAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(int positon) {//vị trí
                startActivity(new Intent(RenrakuSakiGamen2.this,RenrakusakiFragmentGamen3.class)
                                .putExtra("username_of_roomate",users.get(positon).getUsernameAcount())//tuấn
//                        .putExtra("username_of_name",users.get(positon).getEmail())//tuấn
                                .putExtra("email_of_roommate",users.get(positon).getEmail())//tranthanhtuan199981@gmail.com
                                .putExtra("img_of_roommate",users.get(positon).getProfilePicture())//ảnh
                                .putExtra("my_image",myImageUrl)//địa chỉ ảnh
//                                .putExtra("username_of_roomate2",users2.get(positon).getUsernameAcount())//tuấn
//                                .putExtra("email_of_roommate2",users2.get(positon).getEmail())//tranthanhtuan199981@gmail.com
//                                .putExtra("img_of_roommate2",users2.get(positon).getProfilePicture())//ảnh
//                                .putExtra("my_image2",myImageUrl)//địa chỉ ảnh
                );
                finish();
            }
        };
    }

    private void getUsers() {
        users.clear();//xóa sạch người dùng
        users2.clear();//xóa sạch người dùng
        //lấy dữ liệu từ user
        databaseReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String userGmail = dataSnapshot.getValue(User.class).getUsernameAcount();
                    String sds = timKiem;
                    if (userGmail.indexOf(sds)>-1 && !dataSnapshot.getValue(User.class).getEmail().trim().equals(EmailForUser.trim())){
                        users.add(dataSnapshot.getValue(User.class));
                    }
                }
                if (users.size()!=0){
                    userAdapter = new RenrakuSakiAdapter(users,RenrakuSakiGamen2.this,onUserClickListener,users.get(0).getUsernameAcount());
                    recyclerView.setLayoutManager(new LinearLayoutManager(RenrakuSakiGamen2.this));
                    recyclerView.setAdapter(userAdapter);
                    recyclerView.setVisibility(View.VISIBLE);
                }else {
                    TextView textView = findViewById(R.id.textView);
                    textView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RenrakuSakiGamen2.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}