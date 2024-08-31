package com.example.mobile_project_01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mobile_project_01.adapter.ViewPager2QLDSBinhLuanAdapter;
import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.GiayChiTiet;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class QLDanhSachBinhLuanActivity extends AppCompatActivity {

    private Giay giay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qldanh_sach_binh_luan);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            giay = (Giay) bundle.getSerializable("giay");
        }

        ImageButton iBtnThoatQLDSBinhLuan = findViewById(R.id.iBtnThoatQLDSBinhLuan);
        TextView tvTenGiayBL = findViewById(R.id.tvTenGiayBL);
        TabLayout tlBinhLuan = findViewById(R.id.tlBinhLuan);
        ViewPager2 vp2DSBinhLuan = findViewById(R.id.vp2DSBinhLuan);

        tvTenGiayBL.setText(giay.getTenGiay());

        ViewPager2QLDSBinhLuanAdapter viewPager2QLDSBinhLuanAdapter = new ViewPager2QLDSBinhLuanAdapter(this, giay.getMaGiay());
        vp2DSBinhLuan.setAdapter(viewPager2QLDSBinhLuanAdapter);

        new TabLayoutMediator(tlBinhLuan, vp2DSBinhLuan, ((tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Tích cực");
                    break;
                case 1:
                    tab.setText("Tiêu cực");
                    break;

            }
        })).attach();

        iBtnThoatQLDSBinhLuan.setOnClickListener(v -> {
            finish();
        });
    }
}