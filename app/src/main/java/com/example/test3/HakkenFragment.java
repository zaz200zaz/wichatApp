package com.example.test3;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HakkenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HakkenFragment extends Fragment {
//    private ArrayList<Hakken> hakkenArrayList;
//    hakkenAdapter.OnUserClickListener onUserClickListener;
TextView textView;
    View view;
    int requetCode = 45;
//    private hakkenAdapter hakkenAdapter;
//    private RecyclerView recyclerView;//hiển thị danh sách bạn bè

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HakkenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HakkenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HakkenFragment newInstance(String param1, String param2) {
        HakkenFragment fragment = new HakkenFragment();
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
        view = inflater.inflate(R.layout.fragment_hakken, container, false);
        anhXa(view);
        caiDatTitleFragment();
        view.findViewById(R.id.QrScanId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });
        return view;
    }

    private void google(String url) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);

    }

    private void scanCode() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(HakkenFragment.this);
        integrator.setPrompt("scan");
        integrator.setBeepEnabled(true);
        integrator.setCaptureActivity(Capture.class);
        integrator.setOrientationLocked(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    private void anhXa(View view) {
        textView = view.findViewById(R.id.textView5);
    }

    private List<Hakken> getList() {
        List<Hakken> hakkens = new ArrayList<>();
        hakkens.add(new Hakken(R.drawable.ic_baseline_moment_24,"モーメンツ"));
        hakkens.add(new Hakken(R.drawable.ic_baseline_qr_code_scanner_24,"QRコードのスキャン"));
        hakkens.add(new Hakken(R.drawable.ic_baseline_share_24,"シェア"));
        hakkens.add(new Hakken(R.drawable.ic_baseline_groups_3_24,"近くにいる人"));

        return hakkens;
    }

    private void caiDatTitleFragment() {
        setTitle("発見");
        setHasOptionsMenu(true);
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            IntentResult intent = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
            if (intent!=null){
                if (intent.getContents()!=null){
                    textView.setText(intent.getContents());
//                    textView.setVisibility(View.VISIBLE);
                    if (!textView.getText().toString().isEmpty()){
                        if (textView.getText().toString().indexOf("https://")!=-1||textView.getText().toString().indexOf("http://")!=-1){
                            google(textView.getText().toString());
                        }else{
                            Toast.makeText(getContext(), "ウェブサイトのURIの式ではない！読み込めませんでした"+"\n"+"QRコードの値は："+textView.getText().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else {
                    Toast.makeText(getContext(), "結果なし", Toast.LENGTH_SHORT).show();
                }
            }else{
                super.onActivityResult(requestCode,resultCode,data);
            }
    }
}