package com.example.mobile_project_01.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mobile_project_01.QLDanhSachDonHangActivity;
import com.example.mobile_project_01.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


public class QLDonHangFragment extends Fragment {

    private String maThanhVien;

    public QLDonHangFragment() {
        // Required empty public constructor
    }

    public QLDonHangFragment(String maThanhVien) {
        this.maThanhVien = maThanhVien;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_q_l_don_hang, container, false);

        Intent intent = new Intent(getContext(), QLDanhSachDonHangActivity.class);
        Bundle bundle = new Bundle();

        CardView cvQLDSChoXacNhan = view.findViewById(R.id.cvQLDSChoXacNhan);
        TextView tvSLDHChoXacNhan = view.findViewById(R.id.tvSLDHChoXacNhan);

        hienThiSoLuong(0, tvSLDHChoXacNhan);

        cvQLDSChoXacNhan.setOnClickListener((v -> {
            bundle.putString("maThanhVien", maThanhVien);
            bundle.putInt("trangThaiDonHang", 0);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        }));

        // ------------------------------------------------------------------------
        CardView cvQLDSChoGiao = view.findViewById(R.id.cvQLDSChoGiao);
        TextView tvSLDHChoGiao = view.findViewById(R.id.tvSLDHChoGiao);

        hienThiSoLuong(1, tvSLDHChoGiao);

        cvQLDSChoGiao.setOnClickListener((v -> {
            bundle.putString("maThanhVien", maThanhVien);
            bundle.putInt("trangThaiDonHang", 1);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        }));

        // ------------------------------------------------------------------------
        CardView cvQLDSDangGiao = view.findViewById(R.id.cvQLDSDangGiao);
        TextView tvSLDHDangGiao = view.findViewById(R.id.tvSLDHDangGiao);

        hienThiSoLuong(2, tvSLDHDangGiao);

        cvQLDSDangGiao.setOnClickListener((v -> {
            bundle.putString("maThanhVien", maThanhVien);
            bundle.putInt("trangThaiDonHang", 2);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        }));

        // ------------------------------------------------------------------------
        CardView cvQLDSDaGiao = view.findViewById(R.id.cvQLDSDaGiao);
        TextView tvSLDHDaGiao = view.findViewById(R.id.tvSLDHDaGiao);

        hienThiSoLuong(3, tvSLDHDaGiao);

        cvQLDSDaGiao.setOnClickListener((v -> {
            bundle.putString("maThanhVien", maThanhVien);
            bundle.putInt("trangThaiDonHang", 3);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        }));

        // ------------------------------------------------------------------------
        CardView cvQLDSDaHoanThanh = view.findViewById(R.id.cvQLDSDaHoanThanh);
        TextView tvSLDHDaHoanThanh = view.findViewById(R.id.tvSLDHDaHoanThanh);

        hienThiSoLuong(4, tvSLDHDaHoanThanh);

        cvQLDSDaHoanThanh.setOnClickListener((v -> {
            bundle.putString("maThanhVien", maThanhVien);
            bundle.putInt("trangThaiDonHang", 4);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        }));

        // ------------------------------------------------------------------------
        CardView cvQLDSKhongHopLe = view.findViewById(R.id.cvQLDSKhongHopLe);
        TextView tvSLDHKhongHopLe = view.findViewById(R.id.tvSLDHKhongHopLe);

        hienThiSoLuong(6, tvSLDHKhongHopLe);

        cvQLDSKhongHopLe.setOnClickListener((v -> {
            bundle.putString("maThanhVien", maThanhVien);
            bundle.putInt("trangThaiDonHang", 6);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        }));

        // ---------------------------------------------------------------------------
        CardView cvQLDSDonHangBiTuChoi = view.findViewById(R.id.cvQLDSDonHangBiTuChoi);
        TextView tvSLDHBiTuChoi = view.findViewById(R.id.tvSLDHBiTuChoi);

        hienThiSoLuong(7,tvSLDHBiTuChoi);

        cvQLDSDonHangBiTuChoi.setOnClickListener(v -> {
            bundle.putString("maThanhVien", maThanhVien);
            bundle.putInt("trangThaiDonHang", 7);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        });

        return view;
    }

    private void hienThiSoLuong(int status, TextView textView) {
        FirebaseFirestore.getInstance()
                .collection("DonHang")
                .whereEqualTo("trangThaiDonHang", status)
                .addSnapshotListener((snapshot, e) -> {
                    if (snapshot != null) {
                        int count = snapshot.size();
                        textView.setText(String.valueOf(count));
                    }
                });
    }
}