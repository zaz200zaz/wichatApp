package com.example.test3;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JiBunGamen5 extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private DatabaseReference mDatabase;
    private String[] type;
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView autoCompleteTextView;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private Button postCodeButton;

    private EditText nameHoDem;
    private EditText nameTen;
    private EditText nameCachDocHoDem;
    private EditText nameCachDocTen;
    private EditText country;
    private EditText postCode;
    private EditText area;
    private EditText areaChirun;
    private EditText homeNameAndNumber;
    private EditText phoneNumber;
    private Button getDataButtonId;
    private Button birthDayId;
    private EditText banChiId;
    //    private String postCode;
    private final String url="http://zipcloud.ibsnet.co.jp/api/search?zipcode=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ji_bun_gamen5);
        setTitle("個人情報");

        anhXa();
        initDatePicker();
        setSeiBetsu();
        postButtonClick();
        themThongTin();
        neuDaCoSanDuLieu();
    }
    //データ受け取り関数
    private void neuDaCoSanDuLieu() {
        nameHoDem.setText((String) getIntent().getExtras().get("txtNameId1"));
        nameTen.setText((String) getIntent().getExtras().get("txtNameId2"));
        nameCachDocHoDem.setText((String) getIntent().getExtras().get("txtHuRiGaNaNameId1"));
        nameCachDocTen.setText((String) getIntent().getExtras().get("txtHuRiGaNaNameId2"));
        country.setText((String) getIntent().getExtras().get("txtKokusekiId1"));
        postCode.setText((String) getIntent().getExtras().get("txtYuuBinBanGoId1"));
        area.setText((String) getIntent().getExtras().get("address"));
        areaChirun.setText((String) getIntent().getExtras().get("address2"));
        banChiId.setText((String) getIntent().getExtras().get("address3"));
        homeNameAndNumber.setText((String) getIntent().getExtras().get("address4"));
        phoneNumber.setText((String) getIntent().getExtras().get("txtDenWaBangGoId1"));
        birthDayId.setText((String) getIntent().getExtras().get("txtTanjyoBiId1"));
        autoCompleteTextView.setText((String) getIntent().getExtras().get("txtseibetsuId1"));
    }
    //個人情報を登録関数
    private void themThongTin() {
        getDataButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
                progressDialog.setTitle("ちょっとお待ちください。");
                progressDialog.show();
                String nameHoDem1 = nameHoDem.getText().toString();
                String nameTen1 = nameTen.getText().toString();
                String nameCachDocHoDem1 = nameCachDocHoDem.getText().toString();
                String nameCachDocTen1 = nameCachDocTen.getText().toString();
                String autoCompleteTextView1 = autoCompleteTextView.getText().toString();
                String birthDayId1 = birthDayId.getText().toString();
                String country1 = country.getText().toString();
                String postCode1 = postCode.getText().toString();
                String area1 = area.getText().toString();
                String areaChirun1 = areaChirun.getText().toString();
                String banChiId1 = banChiId.getText().toString();
                String homeNameAndNumber1 = homeNameAndNumber.getText().toString();
                String phoneNumber1 = phoneNumber.getText().toString();

                if (nameHoDem1.isEmpty()|| nameTen1.isEmpty()|| nameCachDocHoDem1.isEmpty()||
                        nameCachDocTen1.isEmpty()|| autoCompleteTextView1.isEmpty()|| birthDayId1.isEmpty()||
                        country1.isEmpty()|| postCode1.isEmpty()|| area1.isEmpty()|| areaChirun1.isEmpty()||
                        banChiId1.isEmpty()|| homeNameAndNumber1.isEmpty()|| phoneNumber1.isEmpty())
                {

                    Toast.makeText(JiBunGamen5.this, "未入力の所が有ります。", Toast.LENGTH_SHORT).show();

                }else{

                    themTheTrongFirebaseDatabase(nameHoDem1,nameTen1,nameCachDocHoDem1,
                            nameCachDocTen1,autoCompleteTextView1,birthDayId1,
                            country1,postCode1, area1,
                            areaChirun1,banChiId1,homeNameAndNumber1,
                            phoneNumber1);
                    startActivity(new Intent(JiBunGamen5.this,JiBunGamen4.class));
                    finish();
                }
                progressDialog.dismiss();
            }
        });
    }
    //データ最新
    private void themTheTrongFirebaseDatabase(String nameHoDem,String nameTen,String nameCachDocHoDem, String nameCachDocTen, String autoCompleteTextView,String birthDayId, String country, String postCode, String area, String areaChirun,String banChiId,String homeNameAndNumber, String phoneNumber) {
        String ten = nameHoDem;
        String ten2 = nameTen;
        String cachDocTen = nameCachDocHoDem;
        String cachDocTen2 = nameCachDocTen;
        String gioiTinh = autoCompleteTextView;
        String ngayThangNamSinh = birthDayId;
        String quocGia = country;
        String soBuuDien = postCodeType(postCode);
        String sdt = phoneNumberType(phoneNumber);
        String pahtId =  FirebaseAuth.getInstance().getUid();
        FirebaseDatabase.getInstance()
                .getReference("user/"+ pahtId+"/個人情報")
                .setValue(new PersonaInformation(ten,ten2,cachDocTen,cachDocTen2,gioiTinh,ngayThangNamSinh,quocGia,soBuuDien,area,areaChirun,banChiId,homeNameAndNumber,sdt));
    }
    //ポストコード処理関数
    private String postCodeType(String postCode) {
        String dau3 = ""; String dau7 = "";boolean flag = true;
        char[] sad = postCode.toCharArray();
        for (int i=0;i<sad.length;i++){
            if (sad[i] == '-'){
                flag = true;
                break;
            }else{
                flag = false;
            }
        }
        if (flag){
            return postCode;
        }
        for (int i=0;i<sad.length;i++){
            if (i<3){
                dau3+=sad[i];
            }else{
                dau7+=sad[i];
            }
        }
        return  dau3+"-"+dau7;
    }
    //電話番号処理関数
    private String phoneNumberType(String phoneNumber) {
        String dau3 = ""; String dau7 = "";String dau11 = "";boolean flag = true;
        char[] sad = phoneNumber.toCharArray();
        for (int i=0;i<sad.length;i++){
            if (sad[i] == '-'){
                flag = true;
                break;
            }else{
                flag = false;
            }
        }
        if (flag){
            return phoneNumber;
        }else{
            for (int i=0;i<sad.length;i++){
                if (i<3){
                    dau3+=sad[i];
                }
                if (i>=3&&i<7){
                    dau7+=sad[i];
                }if (i>=7){
                    dau11+=sad[i];
                }
            }
            return  dau3+"-"+dau7+"-"+dau11;
        }
    }
    //ポストコードで住所検索関数
    private void postButtonClick() {
        postCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!postCode.getText().toString().equals("")){
                    String postCode2 = postCode.getText().toString().trim();
                    String postCode3 = postCode2.replaceAll("[-_*+.,;:`\\[\\]\\{\\}]","");
                    try{
                        //okhttpを利用するカスタム関数（下記）

                        httpRequest(url+postCode3);
                    }catch(Exception e){
                        Log.e("Hoge",e.getMessage());
                    }
                }else{
                    Toast.makeText(JiBunGamen5.this, "郵便番号を入力してください！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //APIを使ってポストコードから住所を取得
    private void httpRequest(String url) throws IOException {
        //OkHttpClinet生成
        OkHttpClient client = new OkHttpClient();
        //request生成
        Request request = new Request.Builder()
                .url(url)
                .build();
        //非同期リクエスト
        client.newCall(request).enqueue(new Callback() {
            //エラーのとき
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("Hoge",e.getMessage());
                Toast.makeText(JiBunGamen5.this, "エラー："+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            //正常のとき
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //response取り出し
                final String jsonStr = response.body().string();
                Log.d("Hoge","jsonStr=" + jsonStr);
                //JSON処理
                try{
                    //jsonパース
                    JSONObject json = new JSONObject(jsonStr);
                    JSONArray asdas = json.getJSONArray("results");
                    String nameKen = "",nameKen2 = "",nameKen3 = "";
                    for (int i = 0 ; i<asdas.length();i++){
                        nameKen = asdas.getJSONObject(i).getString("address1");
                        nameKen2 = asdas.getJSONObject(i).getString("address2");
                        nameKen3 = asdas.getJSONObject(i).getString("address3");
                    }
                    final String status = json.getString("results");
                    String dsa="[A-Za-z0-9\\[\\]\\{\\}\\,]";
                    //親スレッドUI更新
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    String finalNameKen = nameKen;
                    String finalNameKen2 = nameKen2;
                    String finalNameKen3 = nameKen3;
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            area.setText(finalNameKen);
                            areaChirun.setText(finalNameKen2+finalNameKen3);
                        }
                    });
                }catch(Exception e){
                    Log.e("Hoge",e.getMessage());
                }
            }
        });
    }
    //性別関数
    private void setSeiBetsu() {
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(JiBunGamen5.this, autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //誕生日処理関数
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int mont, int day) {
                mont = mont+1;
                String date = makeDateString(year,mont,day);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;
        datePickerDialog = new DatePickerDialog(this,style,onDateSetListener,year,month,day);

    }
    private String makeDateString(int year, int mont, int day) {
        return year+"年"+mont+"月"+day+"日";
    }
    //ID反射
    private void anhXa() {
        banChiId = findViewById(R.id.banChiId);
        birthDayId = findViewById(R.id.birthDayId);
        getDataButtonId = findViewById(R.id.getDataButtonId);
        type = new String[]{"男","女","以外"};
        adapter = new ArrayAdapter<>(this,R.layout.drop_dow_item,type);
        autoCompleteTextView = findViewById(R.id.filled_exposed);
        dateButton = findViewById(R.id.birthDayId);
        nameHoDem = findViewById(R.id.nameHoDem);
        nameTen = findViewById(R.id.nameTen);
        nameCachDocHoDem = findViewById(R.id.nameCachDocHoDem);
        nameCachDocTen = findViewById(R.id.nameCachDocTen);
        country = findViewById(R.id.countryId);
        postCode = findViewById(R.id.psotCodeId);
        area = findViewById(R.id.areaId);
        areaChirun = findViewById(R.id.areaChirenId);
        homeNameAndNumber = findViewById(R.id.homeNameAndNunberId);
        phoneNumber = findViewById(R.id.phoneNumberId);
        postCodeButton = findViewById(R.id.postCodeButtonId);
    }
    //タイトル設定
    public void setTitle(String title) {
        TextView textView = new TextView(JiBunGamen5.this);
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    //誕生日のダイアログ表示
    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}