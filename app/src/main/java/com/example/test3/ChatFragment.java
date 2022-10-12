package com.example.test3;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.ui.text.font.Typeface;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    private RecyclerView recyclerView;//hiển thị danh sách bạn bè
    private ArrayList<Message> messages;  //mảng danh sách người dùng
    private ArrayList<User> users;  //mảng danh sách người dùng
    private ArrayList<User> users2;  //mảng danh sách người dùng
    private ProgressBar progressBar;//hình tròn đợi sử lý
    private UserAdapter userAdapter;//bộ điều hợp người dùng
    private UserAdapter.OnUserClickListener onUserClickListener;//khi mà ấn vào từng người dùng
    private String myUsername;
    private String usernameOfTheRoommate,emailOfRoommate,chatRoomId;

    private SwipeRefreshLayout swipeRefreshLayout;

    private String myImageUrl;

    private TextView txtChatGamen;

    private String EmailForUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    //Tham khảo cơ sở dữ liệu tại user
    private DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance().getReference("user");

    private ImageButton imageButtonMessage;
    private ImageButton imageButtonRenrakuSaki;
    private ImageButton imageButtonHakken;
    private ImageButton imageButtonJibun;
    private EditText edtKensaku;
    private View view;
//    RecyclerView recyclerView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        setTitle("トーク");//タイトル設定
        setHasOptionsMenu(true);//オプションメニューを設定
        anhXa(view);//ID反射//
        getAcount();//アカウントメール取得
        lammoiLayout();//Layout最新
        chuyenDataSangMessage();//データ渡す関数
        getUsers();//ユーザーリスト
        timKiemMessage(view);//メッセージ検索
        return view;
    }

    public void setTitle(String title) {
        TextView textView = new TextView(getActivity());
        textView.setText(title);
        textView.setTextSize(20);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(android.R.color.background_light));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(textView);
    }

//    private ActionBar getSupportActionBar() {
//        return null;
//    }

    private void timKiemMessage(View view) {
        view.findViewById(R.id.img_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtKensaku.getText().toString().trim().isEmpty()){
                    Intent intent = new Intent(getContext(),ChatGamen_3.class);
                    intent.putExtra("KhoaTimKiem",edtKensaku.getText().toString().trim());
                    edtKensaku.setText("");
                    startActivity(intent);
                }else{
                    Toast.makeText(getContext(), "検索のところに入力してください！", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void chuyenDataSangMessage() {
        onUserClickListener = new UserAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(int positon) {//vị trí
                startActivity(new Intent(getContext(),MessagerActivity.class)
                        .putExtra("username_of_roomate",users.get(positon).getUsernameAcount())//tuấn
                        .putExtra("email_of_roommate",users.get(positon).getEmail())//tranthanhtuan199981@gmail.com
                        .putExtra("img_of_roommate",users.get(positon).getProfilePicture())//ảnh
                        .putExtra("my_image",myImageUrl)//địa chỉ ảnh

                        .putExtra("username_of_roomate2",users2.get(positon).getUsernameAcount())//tuấn
                        .putExtra("email_of_roommate2",users2.get(positon).getEmail())//tranthanhtuan199981@gmail.com
                        .putExtra("img_of_roommate2",users2.get(positon).getProfilePicture())//ảnh
                        .putExtra("my_image2",myImageUrl)//địa chỉ ảnh
                );
            }
        };
    }

    private void lammoiLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsers();//lấy mảng  người dùng
                swipeRefreshLayout.setRefreshing(false);//kéo làm mới tắt
            }
        });
    }

    private  void getUsers(){
        users.clear();//xóa sạch người dùng
        users2.clear();//xóa sạch người dùng
        //lấy dữ liệu từ user
        databaseReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String userGmail = dataSnapshot.getValue(User.class).getEmail();
                    String sds = EmailForUser;
                    if (!userGmail.equals(EmailForUser)){
                        users.add(dataSnapshot.getValue(User.class));
                    }
                    users2.add(dataSnapshot.getValue(User.class));
                }
                userAdapter = new UserAdapter(users,getContext(),onUserClickListener,catEmail(myUsername));
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(userAdapter);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                //lấy ảnh người gửi
                for (User user: users2){
                    if (user.getEmail().equals(EmailForUser)){
                        myImageUrl = user.getProfilePicture();
                        String Email = String.valueOf(user.getEmail());
                        String verificationCode = String.valueOf(user.getVerificationCode());
//                        luuFileVerificationCodeVaoMay(Email,verificationCode,"wiChat","認証コード");
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String catEmail(String myUsername) {
        String [] list = myUsername.split("\\@",2);
        return list[0];
    }

    private void anhXa(View view) {
        edtKensaku = view.findViewById(R.id.edtKensaku);
        progressBar = view.findViewById(R.id.progressBar);//hình tròn quay ở giữa
        users = new ArrayList<>();//mảng người dùng
        users2 = new ArrayList<>();//mảng người dùng
        recyclerView = view.findViewById(R.id.recyclerview);//dang xem trình tái chế
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);//trượt nhanh làm mới bố trí
    }

    private void getAcount(){
        FirebaseDatabase.getInstance().getReference("user/"+ FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myUsername = snapshot.getValue(User.class).getEmail();//lấy tên người dùng trong database
                return;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}