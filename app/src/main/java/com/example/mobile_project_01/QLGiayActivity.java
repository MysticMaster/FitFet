package com.example.mobile_project_01;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mobile_project_01.adapter.QLGiayAdapter;
import com.example.mobile_project_01.adapter.ViewPager2QLDSDanhMucAdapter;
import com.example.mobile_project_01.fragment.BottomSheetDialogAddGiayFragment;
import com.example.mobile_project_01.model.Giay;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class QLGiayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlgiay);

        TabLayout tlGiay = findViewById(R.id.tlGiay);
        ViewPager2 vp2DSGiay = findViewById(R.id.vp2DSGiay);

        ViewPager2QLDSDanhMucAdapter viewPager2QLDSDanhMucAdapter = new ViewPager2QLDSDanhMucAdapter(this, 1);
        vp2DSGiay.setAdapter(viewPager2QLDSDanhMucAdapter);

        new TabLayoutMediator(tlGiay, vp2DSGiay, ((tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Kinh doanh");
                    break;
                case 1:
                    tab.setText("Ngừng kinh doanh");
                    break;

            }
        })).attach();

        ImageButton iBtnThoatQLGiay = findViewById(R.id.iBtnThoatQLGiay);
        ImageButton iBtnThemGiay = findViewById(R.id.iBtnThemGiay);

        iBtnThoatQLGiay.setOnClickListener(v -> {
            finish();
        });

        iBtnThemGiay.setOnClickListener(v -> {
            BottomSheetDialogAddGiayFragment diaglog = new BottomSheetDialogAddGiayFragment();
            diaglog.show(getSupportFragmentManager(), diaglog.getTag());
        });

    }
}