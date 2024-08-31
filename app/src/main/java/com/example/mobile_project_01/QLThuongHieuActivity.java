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

import com.example.mobile_project_01.adapter.QLThuongHieuAdapter;
import com.example.mobile_project_01.adapter.ViewPager2QLDSDanhMucAdapter;
import com.example.mobile_project_01.fragment.BottomSheetDialogAddThuongHieuFragment;
import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.ThuongHieu;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class QLThuongHieuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlthuong_hieu);

        TabLayout tlThuongHieu = findViewById(R.id.tlThuongHieu);
        ViewPager2 vp2DSThuongHieu = findViewById(R.id.vp2DSThuongHieu);

        ViewPager2QLDSDanhMucAdapter viewPager2QLDSDanhMucAdapter = new ViewPager2QLDSDanhMucAdapter(this, 0);
        vp2DSThuongHieu.setAdapter(viewPager2QLDSDanhMucAdapter);

        new TabLayoutMediator(tlThuongHieu, vp2DSThuongHieu, ((tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Kinh doanh");
                    break;
                case 1:
                    tab.setText("Ngừng kinh doanh");
                    break;

            }
        })).attach();

        ImageButton iBtnThoatQLThuongHieu = findViewById(R.id.iBtnThoatQLThuongHieu);
        ImageButton iBtnThemThuongHieu = findViewById(R.id.iBtnThemThuongHieu);

        iBtnThemThuongHieu.setOnClickListener(v -> {
            BottomSheetDialogAddThuongHieuFragment dialog = new BottomSheetDialogAddThuongHieuFragment();
            dialog.show(getSupportFragmentManager(), dialog.getTag());
        });

        iBtnThoatQLThuongHieu.setOnClickListener(v -> {
            finish();
        });

    }

}