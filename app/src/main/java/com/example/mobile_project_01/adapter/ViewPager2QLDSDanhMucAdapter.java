package com.example.mobile_project_01.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mobile_project_01.fragment.KinhDoanhFragment;
import com.example.mobile_project_01.fragment.NgungKinhDoanhFragment;

public class ViewPager2QLDSDanhMucAdapter extends FragmentStateAdapter {

    private String maGiay;
    private int status;

    public ViewPager2QLDSDanhMucAdapter(@NonNull FragmentActivity fragmentActivity, int status) {
        super(fragmentActivity);
        this.status = status;
    }

    public ViewPager2QLDSDanhMucAdapter(@NonNull FragmentActivity fragmentActivity, String maGiay, int status) {
        super(fragmentActivity);
        this.maGiay = maGiay;
        this.status = status;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                if (status == 0) {
                    KinhDoanhFragment kinhDoanhFragment = new KinhDoanhFragment(0);
                    return kinhDoanhFragment;
                } else if (status == 1) {
                    KinhDoanhFragment kinhDoanhFragment = new KinhDoanhFragment(1);
                    return kinhDoanhFragment;
                } else if (status == 2) {
                    KinhDoanhFragment kinhDoanhFragment = new KinhDoanhFragment(maGiay, 2);
                    return kinhDoanhFragment;
                }
            case 1:
                if (status == 0) {
                    NgungKinhDoanhFragment ngungKinhDoanhFragment = new NgungKinhDoanhFragment(0);
                    return ngungKinhDoanhFragment;
                } else if (status == 1) {
                    NgungKinhDoanhFragment ngungKinhDoanhFragment = new NgungKinhDoanhFragment(1);
                    return ngungKinhDoanhFragment;
                } else if (status == 2) {
                    NgungKinhDoanhFragment ngungKinhDoanhFragment = new NgungKinhDoanhFragment(maGiay, 2);
                    return ngungKinhDoanhFragment;
                }
        }

        KinhDoanhFragment kinhDoanhFragment = null;
        if (status == 0) {
            kinhDoanhFragment = new KinhDoanhFragment(0);
        } else if (status == 1) {
            kinhDoanhFragment = new KinhDoanhFragment(1);
        } else if (status == 2) {
            kinhDoanhFragment = new KinhDoanhFragment(maGiay, 2);
        }
        return kinhDoanhFragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
