package com.example.test3;



import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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


import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JibunFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JibunFragment extends Fragment {

    private User user;
    private ValueEventListener valListener;
    private DatabaseReference reference;
    private static String user_nickName;
    private static String user_userName;
    private static String email;
    private static String user_email;

    private ArrayList<User> users;  //mảng danh sách người dùng
    private ProgressBar progressBar;//hình tròn đợi sử lý
    private UserAdapter.OnUserClickListener onUserClickListener;//khi mà ấn vào từng người dùng

    private TextView edtNickNameId;private TextView user_acount;
    private ImageView cameraProfile;

    private Button edit_button;
    int count = 1;
    private EditText edtUserNameId;
    private DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance().getReference("user");
    private String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    private DatabaseReference mDatabase;
    private String tenTruocKhiThayDoi;
    private String tenTruocKhiThayDoi2;
    private String tenSauKhiThayDoi;
    private String tenSauKhiThayDoi2;
    private String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Fragment applicationContext;

    public JibunFragment() {
    }

    public static JibunFragment newInstance(String param1, String param2) {
        JibunFragment fragment = new JibunFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final  View view = inflater.inflate(R.layout.fragment_jibun, container, false);
        setTitle("自分");
        setHasOptionsMenu(true);
        anhXa(view);
        tenTruocKhiThayDoi = edtUserNameId.getText().toString().trim();
        tenTruocKhiThayDoi2 = edtNickNameId.getText().toString().trim();
        LogOut(view);
        thayDoiMatKhau(view);
        jiBunGamen4Intern(view);
        layThongTinAnhTen();
        xoaAction(view);
        khiAnVaoAnhNguoiDung(view);
        caiDatLaiTen(view);
        return view;
    }
    //nickNameとユーザー名をアップデート
    private void caiDatLaiTen(View view) {
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;//1-2
                if (count==3||count<1){//1-2
                    count=1;
                    edtUserNameId.setEnabled(true);
                    updateUser_nickName(edtUserNameId.getText().toString().trim(),Uid);

                    edtNickNameId.setEnabled(true);
                    resetName(edtNickNameId.getText().toString().trim());
                }

                if (count%2==0){
                    tenSauKhiThayDoi = edtUserNameId.getText().toString().trim();
                    if (tenSauKhiThayDoi!=tenTruocKhiThayDoi){
                        resetName(edtUserNameId.getText().toString().trim());
                    }
                    edtUserNameId.setEnabled(true);

                    tenSauKhiThayDoi2 = edtUserNameId.getText().toString().trim();

                    if (tenSauKhiThayDoi2!=tenTruocKhiThayDoi2){
                        updateUser_nickName(edtNickNameId.getText().toString().trim(),Uid);
                    }
                    edtNickNameId.setEnabled(true);

                } else{
                    edtUserNameId.setEnabled(false);
                    resetName(edtUserNameId.getText().toString().trim());

                    edtNickNameId.setEnabled(false);
                    updateUser_nickName(edtNickNameId.getText().toString().trim(),Uid);
                }
            }
        });
        view.findViewById(R.id.tableGamenId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtUserNameId.setEnabled(false);edtNickNameId.setEnabled(false);
                count=1;
            }
        });
    }
    private void resetName(String name) {
        List<String> stringList = new ArrayList<>();
        stringList.clear();
        users.clear();//xóa sạch người dùng
        databaseReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String userGmail = dataSnapshot.getValue(User.class).getEmail();//email cua moi nguoi
                    String ult = Uid;//ma UID cua email
                    String sds = userEmail;//ten nguoi dc an
                    if (userGmail.equals(sds)){
                        updateName(name,ult);
                        return;
                    }
                    Log.d("TAG", "onDataChange: "+ult);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateName(String name , String UserUids){
        if (!name.isEmpty()){
            mDatabase.child("user").child(UserUids).child("userName").setValue(name);
        }
    }
    private void updateUser_nickName(String name , String UserUids){
        if (!name.isEmpty()){
            mDatabase.child("user").child(UserUids).child("nickName").setValue(name);
        }
    }
    //ユーザーの写真を設定関数
    private void khiAnVaoAnhNguoiDung(View view) {
        view.findViewById(R.id.cameraProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Profile.class));
            }
        });
    }
    //ユーザーの情報を取り込む関数(user_nickName、user_userName)
    private void layThongTinAnhTen() {

         String EmailForUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
         user_email = EmailForUser;
         String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("user").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    String image = task.getResult().getValue(User.class).getProfilePicture();
                    user_nickName = task.getResult().getValue(User.class).getNickName();
                    user_userName = task.getResult().getValue(User.class).getUserName();
                    caiDatAnhTenCuaNguoiDung2();
                }
            }
        });
    }
    //FragmentのViewが破棄と写真表示の設定
    private void xoaAction(View view){
         reference = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
         valListener =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 user = dataSnapshot.getValue(User.class);
                if (user.getProfilePicture().equals("default")) {
                    ImageView imageView =  view.findViewById(R.id.cameraProfile);
                    imageView.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getContext()).load(user.getProfilePicture()).into(cameraProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };reference.addValueEventListener(valListener);

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        reference.removeEventListener(valListener);
    }
    //パスワード変更関数
    private void thayDoiMatKhau(View view) {
        view.findViewById(R.id.tblPasswordChangeId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("ちょっとお待ちください。");
                progressDialog.show();
                startActivity(new Intent(getContext(),PaswordsChange.class));
                progressDialog.dismiss();
            }
        });
    }
    //情報登録、閲覧処理関数
    private void jiBunGamen4Intern(View view) {
        view.findViewById(R.id.kojinJyohoId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("個人情報を読み込み中...");
                progressDialog.show();
                startActivity(new Intent(getContext(),JiBunGamen4.class));
                progressDialog.dismiss();
            }
        });
    }
    //ニックネームがなかったらデフォルトです
    private void anMail(String string) {
        if (string.isEmpty()){
            edtNickNameId.setVisibility(View.GONE);
            user_acount.setText("デフォルト");
            user_acount.setVisibility(View.VISIBLE);
        }else{
            user_acount.setVisibility(View.VISIBLE);
            user_acount.setText(string);
            edtNickNameId.setVisibility(View.VISIBLE);
        }
    }
    //画面のタイトルを設定
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
    //自分のニックネーム、ユーザー名、登録されたメールを表示する
    private void caiDatAnhTenCuaNguoiDung2(){
        anMail(user_nickName);
        user_acount.setText(user_email);//登録されたメール
        edtNickNameId.setText(user_nickName);//ニックネーム
        edtUserNameId.setText(user_userName);//ユーザー名
        progressBar.setVisibility(View.GONE);//
    }
    //ログアウト関数
    private void LogOut(View view) {
        view.findViewById(R.id.tblLogOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("❣❣❣❣ログアウト中...❣❣❣❣");
                progressDialog.show();
                FirebaseAuth.getInstance().signOut();
                progressDialog.dismiss();
                Intent intent = new Intent(getContext(),MiTouRoku.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
    //IDの反射
    private void anhXa(View view) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = new User();
        progressBar = view.findViewById(R.id.progressBar);//hình tròn quay ở giữa
//        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);//trượt nhanh làm mới bố trí
        edtNickNameId = view.findViewById(R.id.edtNickNameId);
        user_acount = view.findViewById(R.id.txtEmail);
        cameraProfile = view.findViewById(R.id.cameraProfile);
        users = new ArrayList<>();
        edit_button = view.findViewById(R.id.henShyuId);
        edtUserNameId = view.findViewById(R.id.edtUserNameId);

    }
}