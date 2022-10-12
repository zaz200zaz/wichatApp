package com.example.test3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayDeque;
import java.util.Deque;

public class FragmentNavigationBar extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    private boolean flag = true;
//    BottomNavigationView bottomNavigationView;
    Deque<Integer> integerDeque;
//    boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_navigation_bar);
        anhxa();
        manHinhKhiKhoiDong();
        khiAnVaoCacManHinhKhac();
    }

    private void khiAnVaoCacManHinhKhac() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();//lay id
                if (integerDeque.contains(id)){//check condition
                    if (id == R.id.chatId){
                        if (integerDeque.size()!=1){
                            if (flag){
                                integerDeque.addFirst(R.id.chatId);
                                flag = false;
                            }
                        }
                    }
                    integerDeque.remove(id);
                }
                integerDeque.push(id);
                loadFragmen(getFragment(item.getItemId()));
                return true;
            }
        });
    }

    private Fragment getFragment(int itemId) {
        switch (itemId){
            case R.id.chatId:
//                bottomNavigationView.setBackgroundResource(R.color.coloAccent);
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                return new ChatFragment();
            case R.id.renrakusakiId:
//                bottomNavigationView.setBackgroundResource(R.color.teal_200);
                bottomNavigationView.getMenu().getItem(1).setChecked(true);
                return new RenrakusakiFragment();
            case R.id.hakkenId:
//                bottomNavigationView.setBackgroundResource(android.R.color.system_accent1_200);
                bottomNavigationView.getMenu().getItem(2).setChecked(true);
                return new HakkenFragment();
            case R.id.jibunId:
//                bottomNavigationView.setBackgroundResource(android.R.color.holo_purple);
                bottomNavigationView.getMenu().getItem(3).setChecked(true);
                return new JibunFragment();
        }
        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        return new ChatFragment();
    }

    private void manHinhKhiKhoiDong() {
        loadFragmen(new ChatFragment());
//        bottomNavigationView.setBackgroundResource(R.color.coloAccent);
        bottomNavigationView.setSelectedItemId(R.id.chatId);
    }
    private void loadFragmen(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameLayoutId,fragment,fragment.getClass()
                        .getSimpleName())
                .commit();
    }
    private void setFragme(Fragment fragme) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.FrameLayoutId,fragme);
        fragmentTransaction.commit();
    }

    private void anhxa() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        integerDeque = new ArrayDeque<>(4);
        integerDeque.push(R.id.chatId);
//        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        frameLayout = findViewById(R.id.FrameLayoutId);


    }

}