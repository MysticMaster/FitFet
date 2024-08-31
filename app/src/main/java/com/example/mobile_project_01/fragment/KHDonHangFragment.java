package com.example.mobile_project_01.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobile_project_01.R;
import com.example.mobile_project_01.adapter.ViewPager2KHDonHangAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class KHDonHangFragment extends Fragment {

    private String maKhachHang;

    public KHDonHangFragment() {
        // Required empty public constructor
    }

    public KHDonHangFragment(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_k_h_don_hang, container, false);

        TabLayout tlKHDonHang = view.findViewById(R.id.tlKHDonHang);
        ViewPager2 vp2KHDonHang = view.findViewById(R.id.vp2KHDonHang);

        ViewPager2KHDonHangAdapter adapter = new ViewPager2KHDonHangAdapter(getActivity(), maKhachHang);
        vp2KHDonHang.setAdapter(adapter);

        new TabLayoutMediator(tlKHDonHang, vp2KHDonHang, ((tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Chờ xác nhận");
                    break;
                case 1:
                    tab.setText("Chuẩn bị hàng");
                    break;
                case 2:
                    tab.setText("Đang giao");
                    break;
                case 3:
                    tab.setText("Đã giao");
                    break;
                case 4:
                    tab.setText("Đã nhận");
                    break;
                case 5:
                    tab.setText("Đã hủy");
                    break;
            }
        })).attach();

        return view;
    }
}