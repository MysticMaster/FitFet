package com.example.mobile_project_01.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mobile_project_01.DetailAnhActivity;
import com.example.mobile_project_01.DetailUpdateThongTinActivity;
import com.example.mobile_project_01.ExtraFunctionActivity;
import com.example.mobile_project_01.LoginActivity;
import com.example.mobile_project_01.QLDanhSachTinNhanKHActivity;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.model.KhachHang;
import com.example.mobile_project_01.model.ThanhVien;
import com.google.firebase.auth.FirebaseAuth;


public class TVQuanLyTaiKhoanFragment extends Fragment {
    private ThanhVien thanhVien;

    public TVQuanLyTaiKhoanFragment() {
        // Required empty public constructor
    }

    public TVQuanLyTaiKhoanFragment(ThanhVien thanhVien) {
        this.thanhVien = thanhVien;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_t_v_quan_ly_tai_khoan, container, false);

        CardView cvAnhTV = view.findViewById(R.id.cvAnhTV);
        ImageView ivAnhTV = view.findViewById(R.id.ivAnhTV);

        if (thanhVien != null) {
            if (thanhVien.getAnhThanhVien().equals("0")) {
                ivAnhTV.setImageResource(R.drawable.none_image);
            } else {
                Glide.with(this).load(thanhVien.getAnhThanhVien()).into(ivAnhTV);
            }
        }

        cvAnhTV.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), DetailAnhActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("thanhVien", thanhVien);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        });

        CardView cvHoTenTV = view.findViewById(R.id.cvHoTenTV);
        TextView tvHoTenTV = view.findViewById(R.id.tvHoTenTV);
        tvHoTenTV.setText(thanhVien.getHoThanhVien() + " " + thanhVien.getTenThanhVien());
        cvHoTenTV.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DetailUpdateThongTinActivity.class);
            Bundle sendBundle = new Bundle();
            sendBundle.putSerializable("thanhVien", thanhVien);
            sendBundle.putInt("loaiUpdate", 0);
            intent.putExtras(sendBundle);
            getActivity().startActivity(intent);
        });

        CardView cvBaoMatTV = view.findViewById(R.id.cvBaoMatTV);
        cvBaoMatTV.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ExtraFunctionActivity.class);
            Bundle sendBundle = new Bundle();
            sendBundle.putSerializable("thanhVien", thanhVien);
            sendBundle.putInt("loaiExtra", 1);
            intent.putExtras(sendBundle);
            getActivity().startActivity(intent);
        });

        CardView cvTinNhanKH = view.findViewById(R.id.cvTinNhanKH);
        cvTinNhanKH.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), QLDanhSachTinNhanKHActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("thanhVien", thanhVien);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        CardView cvDangXuatTV = view.findViewById(R.id.cvDangXuatTV);
        cvDangXuatTV.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Bạn chắc chắn muốn đăng xuất khỏi tài khoản?");

            builder.setPositiveButton("Đăng xuất", ((dialog, which) -> {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("checkBoxSave", true);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
                getActivity().finish();
            }));

            builder.setNegativeButton("Không", ((dialog, which) -> {

            }));

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        return view;
    }
}