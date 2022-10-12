package com.example.test3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JiBunGamen4 extends AppCompatActivity {
    private TextView txtNameId;
    private TextView txtHuRiGaNaNameId;
    private TextView txtseibetsuId;
    private TextView txtTanjyoBiId;
    private TextView txtKokusekiId;
    private TextView txtYuuBinBanGoId;
    private TextView txtJyuSyoId;
    private TextView txtDenWaBangGoId;
    private DatabaseReference mDatabase;
    private TableLayout tableLayout;
    private TextView flagTextView;

    private String txtNameId1="";
    private String txtNameId2="";
    private String txtHuRiGaNaNameId1="";private String txtHuRiGaNaNameId2="";
    private String txtseibetsuId1="";
    private String txtTanjyoBiId1="";
    private String txtKokusekiId1="";

    private String txtYuuBinBanGoId1="";
    private String txtDenWaBangGoId1="";

    private String address="";
    private String address2="";
    private String address3="";
    private String address4="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ji_bun_gamen4);
        setTitle("個人情報");
        anhXa();
        kiemTraXemCoThongTinHayKhong();
    }
    //データをサーバーから取り込んで表示関数
    private void ganDuLieu() {
        {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("user/"+FirebaseAuth.getInstance().getUid()).child("個人情報").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                            String name ="";
                            name = String.valueOf(task.getResult().getValue(PersonaInformation.class).getFullName()+" "+
                                    task.getResult().getValue(PersonaInformation.class).getFullName2());
                            String sfsadf = name;
                            txtNameId.setText(name);
                            txtNameId1 = String.valueOf(task.getResult().getValue(PersonaInformation.class).getFullName());
                        txtNameId2 = String.valueOf(task.getResult().getValue(PersonaInformation.class).getFullName2());

                            txtHuRiGaNaNameId.setText(String.valueOf(task.getResult().getValue(PersonaInformation.class).getHowToReadName()+" "+
                                    task.getResult().getValue(PersonaInformation.class).getHowToReadName2()));
                            txtHuRiGaNaNameId1 = String.valueOf(task.getResult().getValue(PersonaInformation.class).getHowToReadName());
                        txtHuRiGaNaNameId2 = String.valueOf(task.getResult().getValue(PersonaInformation.class).getHowToReadName2());

                            txtseibetsuId.setText(String.valueOf(task.getResult().getValue(PersonaInformation.class).getSex()));
                        txtseibetsuId1 = String.valueOf(task.getResult().getValue(PersonaInformation.class).getSex());

                            txtTanjyoBiId.setText(String.valueOf(task.getResult().getValue(PersonaInformation.class).getBirthDay()));
                        txtTanjyoBiId1 = String.valueOf(task.getResult().getValue(PersonaInformation.class).getBirthDay());

                            txtKokusekiId.setText(String.valueOf(task.getResult().getValue(PersonaInformation.class).getCountry()));
                        txtKokusekiId1 = String.valueOf(task.getResult().getValue(PersonaInformation.class).getCountry());

                            txtYuuBinBanGoId.setText(String.valueOf(task.getResult().getValue(PersonaInformation.class).getPostCode()));
                        txtYuuBinBanGoId1 = String.valueOf(task.getResult().getValue(PersonaInformation.class).getPostCode());

                            address = task.getResult().getValue(PersonaInformation.class).getAddress();
                            address2 = task.getResult().getValue(PersonaInformation.class).getAddress2();
                            address3 = task.getResult().getValue(PersonaInformation.class).getAddress3();
                            address4 = task.getResult().getValue(PersonaInformation.class).getAddress4();
                            String diaChi=String.valueOf(task.getResult().getValue(PersonaInformation.class).getAddress()+
                                    task.getResult().getValue(PersonaInformation.class).getAddress2()+
                                    task.getResult().getValue(PersonaInformation.class).getAddress3()+
                                    task.getResult().getValue(PersonaInformation.class).getAddress4());
                            txtJyuSyoId.setText(diaChi);

                            txtDenWaBangGoId.setText(String.valueOf(task.getResult().getValue(PersonaInformation.class).getPhoneNumber()));
                        txtDenWaBangGoId1 = String.valueOf(task.getResult().getValue(PersonaInformation.class).getPhoneNumber());

                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                            Log.d("name:", name);


                    }
                }
            });
        }
    }
    //IDを反射
    private void anhXa() {
        address = "";address2 = "";address3 = "";address4 = "";
        flagTextView = findViewById(R.id.flagTextView);
        tableLayout = findViewById(R.id.tableId);
        txtNameId = findViewById(R.id.txtNameId);
        txtHuRiGaNaNameId = findViewById(R.id.txtHuRiGaNaNameId);
        txtseibetsuId = findViewById(R.id.txtseibetsuId);
        txtTanjyoBiId = findViewById(R.id.txtTanjyoBiId);
        txtKokusekiId = findViewById(R.id.txtKokusekiId);
        txtYuuBinBanGoId = findViewById(R.id.txtYuuBinBanGoId);
        txtJyuSyoId = findViewById(R.id.txtJyuSyoId);
        txtDenWaBangGoId = findViewById(R.id.txtDenWaBangGoId);
    }
    //登録情報があるかどうかを判定する関数
    private void kiemTraXemCoThongTinHayKhong() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String s1 = FirebaseAuth.getInstance().getUid();
                String s2 = "user/"+s1+"/個人情報";
                if (snapshot.hasChild(s2)) {
                    // run some code
                    ganDuLieu();
                    flagTextView.setVisibility(View.GONE);
                    tableLayout.setVisibility(View.VISIBLE);
                }else{
                    flagTextView.setVisibility(View.VISIBLE);
                    tableLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hensyu_menu,menu);
        return true;
    }
    //情報登録ページへ移動
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.henSyuId:
                jyoHoToRoKuHandle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //データを渡す関数
    private void jyoHoToRoKuHandle() {
        Intent intent = new Intent(JiBunGamen4.this,JiBunGamen5.class);
        intent.putExtra("txtNameId1",txtNameId1);
        intent.putExtra("txtNameId2",txtNameId2);
        intent.putExtra("txtHuRiGaNaNameId1",txtHuRiGaNaNameId1);
        intent.putExtra("txtHuRiGaNaNameId2",txtHuRiGaNaNameId2);
        intent.putExtra("txtseibetsuId1",txtseibetsuId1);
        intent.putExtra("txtTanjyoBiId1",txtTanjyoBiId1);
        intent.putExtra("txtKokusekiId1",txtKokusekiId1);
        intent.putExtra("txtYuuBinBanGoId1",txtYuuBinBanGoId1);
        intent.putExtra("address",address);
        intent.putExtra("address2",address2);
        intent.putExtra("address3",address3);
        intent.putExtra("address4",address4);
        intent.putExtra("txtDenWaBangGoId1",txtDenWaBangGoId1);

        startActivity(intent);
    }
    //タイトル設定
    public void setTitle(String title) {
        TextView textView = new TextView(JiBunGamen4.this);
        textView.setText(title);
        textView.setTextSize(20);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(android.R.color.background_light));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(textView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    //バックボタンを押されたらactivity終了
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}