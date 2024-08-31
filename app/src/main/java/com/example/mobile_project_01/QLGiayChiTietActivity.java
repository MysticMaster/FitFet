package com.example.mobile_project_01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mobile_project_01.adapter.ViewPager2QLDSDanhMucAdapter;
import com.example.mobile_project_01.fragment.BottomSheetDialogAddGiayCTFragment;
import com.example.mobile_project_01.model.Giay;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class QLGiayChiTietActivity extends AppCompatActivity {

    private Giay giay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_giay_chi_tiet);

        Intent intent = getIntent();

        TextView tvTenGiayHienThiQL = findViewById(R.id.tvTenGiayHienThiQL);

        if (intent != null) {
            giay = (Giay) intent.getSerializableExtra("giay");
            tvTenGiayHienThiQL.setText(giay.getTenGiay());
        }

        TabLayout tlGiayCT = findViewById(R.id.tlGiayCT);
        ViewPager2 vp2DSGiayCT = findViewById(R.id.vp2DSGiayCT);

        ViewPager2QLDSDanhMucAdapter viewPager2QLDSDanhMucAdapter = new ViewPager2QLDSDanhMucAdapter(this, giay.getMaGiay(), 2);
        vp2DSGiayCT.setAdapter(viewPager2QLDSDanhMucAdapter);

        new TabLayoutMediator(tlGiayCT, vp2DSGiayCT, ((tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Kinh doanh");
                    break;
                case 1:
                    tab.setText("Ngừng kinh doanh");
                    break;

            }
        })).attach();

        ImageButton iBtnThoatHienThiQL = findViewById(R.id.iBtnThoatHienThiQL);
        iBtnThoatHienThiQL.setOnClickListener(v -> {
            finish();
        });

        ImageButton iBtnThemGiayCT = findViewById(R.id.iBtnThemGiayCT);
        iBtnThemGiayCT.setOnClickListener(v -> {
            BottomSheetDialogAddGiayCTFragment addGiayCTFragment = new BottomSheetDialogAddGiayCTFragment(giay.getMaGiay());
            addGiayCTFragment.show(getSupportFragmentManager(), addGiayCTFragment.getTag());
        });

    }
}