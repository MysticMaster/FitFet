package com.example.mobile_project_01.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mobile_project_01.fragment.KHChoLayHangDHFragment;
import com.example.mobile_project_01.fragment.KHChoXacNhanDHFragment;
import com.example.mobile_project_01.fragment.KHDaGiaoDHFragment;
import com.example.mobile_project_01.fragment.KHDaHuyDHFragment;
import com.example.mobile_project_01.fragment.KHDaNhanDHFragment;
import com.example.mobile_project_01.fragment.KHDangGiaoDHFragment;

public class ViewPager2KHDonHangAdapter extends FragmentStateAdapter {

    private String maKhachHang;

    public ViewPager2KHDonHangAdapter(@NonNull FragmentActivity fragmentActivity, String maKhachHang) {
        super(fragmentActivity);
        this.maKhachHang = maKhachHang;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                KHChoXacNhanDHFragment khChoXacNhanDHFragment = new KHChoXacNhanDHFragment(maKhachHang);
                return khChoXacNhanDHFragment;
            case 1:
                KHChoLayHangDHFragment khChoLayHangDHFragment = new KHChoLayHangDHFragment(maKhachHang);
                return khChoLayHangDHFragment;
            case 2:
                KHDangGiaoDHFragment khDangGiaoDHFragment = new KHDangGiaoDHFragment(maKhachHang);
                return khDangGiaoDHFragment;
            case 3:
                KHDaGiaoDHFragment khDaGiaoDHFragment = new KHDaGiaoDHFragment(maKhachHang);
                return khDaGiaoDHFragment;
            case 4:
                KHDaNhanDHFragment khDaNhanDHFragment = new KHDaNhanDHFragment(maKhachHang);
                return khDaNhanDHFragment;
            case 5:
                KHDaHuyDHFragment khDaHuyDHFragment = new KHDaHuyDHFragment(maKhachHang);
                return khDaHuyDHFragment;
        }
        return new KHChoXacNhanDHFragment(maKhachHang);
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
