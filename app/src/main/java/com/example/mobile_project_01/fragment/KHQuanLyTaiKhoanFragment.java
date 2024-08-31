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
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.model.KhachHang;
import com.google.firebase.auth.FirebaseAuth;


public class KHQuanLyTaiKhoanFragment extends Fragment {

    private KhachHang khachHang;

    public KHQuanLyTaiKhoanFragment() {
        // Required empty public constructor
    }

    public KHQuanLyTaiKhoanFragment(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_k_h_quan_ly_tai_khoan, container, false);

        CardView cvAnhKH = view.findViewById(R.id.cvAnhKH);
        ImageView ivAnhKH = view.findViewById(R.id.ivAnhKH);

        if (khachHang != null) {
            if (khachHang.getAnhKhachHang().equals("0")) {
                ivAnhKH.setImageResource(R.drawable.none_image);
            } else {
                Glide.with(this).load(khachHang.getAnhKhachHang()).into(ivAnhKH);
            }
        }

        cvAnhKH.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), DetailAnhActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("khachHang", khachHang);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        });

        CardView cvHoTen = view.findViewById(R.id.cvHoTen);
        TextView tvHoTenKH = view.findViewById(R.id.tvHoTenKH);
        tvHoTenKH.setText(khachHang.getHoKhachHang() + " " + khachHang.getTenKhachHang());
        cvHoTen.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DetailUpdateThongTinActivity.class);
            Bundle sendBundle = new Bundle();
            sendBundle.putSerializable("khachHang", khachHang);
            sendBundle.putInt("loaiUpdate", 0);
            intent.putExtras(sendBundle);
            getActivity().startActivity(intent);
        });

        CardView cvThemThongTin = view.findViewById(R.id.cvThemThongTin);
        cvThemThongTin.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ExtraFunctionActivity.class);
            Bundle sendBundle = new Bundle();
            sendBundle.putSerializable("khachHang", khachHang);
            sendBundle.putInt("loaiExtra", 0);
            intent.putExtras(sendBundle);
            getActivity().startActivity(intent);
        });

        CardView cvBaoMat = view.findViewById(R.id.cvBaoMat);
        cvBaoMat.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ExtraFunctionActivity.class);
            Bundle sendBundle = new Bundle();
            sendBundle.putSerializable("khachHang", khachHang);
            sendBundle.putInt("loaiExtra", 1);
            intent.putExtras(sendBundle);
            getActivity().startActivity(intent);
        });

        CardView cvHoTro = view.findViewById(R.id.cvHoTro);
        cvHoTro.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ExtraFunctionActivity.class);
            Bundle sendBundle = new Bundle();
            sendBundle.putSerializable("khachHang", khachHang);
            sendBundle.putInt("loaiExtra", 2);
            intent.putExtras(sendBundle);
            getActivity().startActivity(intent);
        });

        CardView cvDangXuatKH = view.findViewById(R.id.cvDangXuatKH);
        cvDangXuatKH.setOnClickListener(v -> {
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