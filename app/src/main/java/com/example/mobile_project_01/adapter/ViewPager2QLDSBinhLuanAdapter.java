package com.example.mobile_project_01.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mobile_project_01.fragment.BinhLuanTichCucFragment;
import com.example.mobile_project_01.fragment.BinhLuanTieuCucFragment;

public class ViewPager2QLDSBinhLuanAdapter extends FragmentStateAdapter {

    private String maGiay;

    public ViewPager2QLDSBinhLuanAdapter(@NonNull FragmentActivity fragmentActivity, String maGiay) {
        super(fragmentActivity);
        this.maGiay = maGiay;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                BinhLuanTichCucFragment tichCucFragment = new BinhLuanTichCucFragment(maGiay);
                return tichCucFragment;
            case 1:
                BinhLuanTieuCucFragment tieuCucFragment = new BinhLuanTieuCucFragment(maGiay);
                return tieuCucFragment;
        }
        return new BinhLuanTichCucFragment(maGiay);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
