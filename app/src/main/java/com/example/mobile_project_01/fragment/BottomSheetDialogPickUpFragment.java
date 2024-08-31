package com.example.mobile_project_01.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mobile_project_01.DAO.GioHangDAO;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.ThanhToanActivity;
import com.example.mobile_project_01.adapter.KHGiayCTAdapter;
import com.example.mobile_project_01.adapter.KichCoAdapter;
import com.example.mobile_project_01.function.AutoStringID;
import com.example.mobile_project_01.function.FormatCurrency;
import com.example.mobile_project_01.model.DonHangChiTiet;
import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.GiayChiTiet;
import com.example.mobile_project_01.model.GioHang;
import com.example.mobile_project_01.model.KhachHang;
import com.example.mobile_project_01.model.KichCoGiayCT;
import com.example.mobile_project_01.widget.QuantityNumberPicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


public class BottomSheetDialogPickUpFragment extends com.google.android.material.bottomsheet.BottomSheetDialogFragment {

    private int trangThai;
    private Giay giay;
    private ImageView ivAnhGiayDialog;
    private TextView tvTen, tvGia, tvSoLuong;
    private RecyclerView rvChonMau;
    private KHGiayCTAdapter KHGiayCTAdapter;
    private List<GiayChiTiet> giayChiTietList;

    private KhachHang khachHang;

    private String maGiayCTMua;
    private int kichCoMua;
    private int donGia;
    private int soLuongMua;

    private List<KichCoGiayCT> listKichCo;

    private GioHang gioHang;

    public BottomSheetDialogPickUpFragment() {

    }

    public BottomSheetDialogPickUpFragment(int trangThai, Giay giay, KhachHang khachHang) {
        this.trangThai = trangThai;
        this.giay = giay;
        this.khachHang = khachHang;
    }

    public BottomSheetDialogPickUpFragment(int trangThai, Giay giay, GioHang gioHang) {
        this.trangThai = trangThai;
        this.giay = giay;
        this.gioHang = gioHang;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog_pick_up, container, false);
        ivAnhGiayDialog = view.findViewById(R.id.ivAnhGiayDialog);
        tvTen = view.findViewById(R.id.tvTen);
        tvGia = view.findViewById(R.id.tvGia);
        tvSoLuong = view.findViewById(R.id.tvSoLuong);
        rvChonMau = view.findViewById(R.id.rvChonMau);
        giayChiTietList = new ArrayList<>();
        KHGiayCTAdapter = new KHGiayCTAdapter(getContext(), giayChiTietList);

        if (giay.getAnhGiay().equals("0")) {
            ivAnhGiayDialog.setImageResource(R.drawable.none_image);
        } else {
            Glide.with(this).load(giay.getAnhGiay()).into(ivAnhGiayDialog);
        }

        RecyclerView rvChonCo = view.findViewById(R.id.rvChonCo);
        listKichCo = new ArrayList<>();
        KichCoAdapter kichCoAdapter = new KichCoAdapter(listKichCo, getContext());

        LinearLayoutManager managerGiay = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvChonMau.setLayoutManager(managerGiay);
        rvChonMau.setAdapter(KHGiayCTAdapter);

        LinearLayoutManager managerKichCo = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvChonCo.setLayoutManager(managerKichCo);
        rvChonCo.setAdapter(kichCoAdapter);

        QuantityNumberPicker qnpSoLuongMua = view.findViewById(R.id.qnpSoLuongMua);

        TextView tvSubmit = view.findViewById(R.id.tvSubmit);
        tvSubmit.setEnabled(false);
        tvSubmit.setAlpha(0.3f);
        if (trangThai == 0) {
            tvSubmit.setText("Thêm vào giỏ");
        } else if (trangThai == 1) {
            tvSubmit.setText("Mua ngay");
        }

        KHGiayCTAdapter.setOnItemClickListener((giayChiTiet -> {
            tvSubmit.setEnabled(false);
            tvSubmit.setAlpha(0.3f);

            tvSoLuong.setText("0");

            qnpSoLuongMua.setVisibility(View.INVISIBLE);
            maGiayCTMua = giayChiTiet.getMaGiayChiTiet();

            tvTen.setText(giayChiTiet.getTenGiayChiTiet());
            donGia = giayChiTiet.getGia();
            tvGia.setText(FormatCurrency.formatCurrency(giayChiTiet.getGia()) + "₫");
            if (giayChiTiet.getAnhGiayChiTiet().equals("0")) {
                ivAnhGiayDialog.setImageResource(R.drawable.none_image);
            } else {
                Glide.with(getContext()).load(giayChiTiet.getAnhGiayChiTiet()).into(ivAnhGiayDialog);
            }
            listKichCo.clear();
            listKichCo.addAll(giayChiTiet.getListKichCo());
            kichCoAdapter.selectedPosition = RecyclerView.NO_POSITION;
            kichCoAdapter.notifyDataSetChanged();
        }));

        kichCoAdapter.setOnItemClickListener(kichCoGiayCT -> {
            tvSubmit.setEnabled(false);
            tvSubmit.setAlpha(0.3f);

            tvSoLuong.setText(String.valueOf(kichCoGiayCT.getSoLuong()));

            qnpSoLuongMua.setVisibility(View.VISIBLE);
            qnpSoLuongMua.setMaxQuantity(kichCoGiayCT.getSoLuong());
            qnpSoLuongMua.setQuantity(0);
            kichCoMua = kichCoGiayCT.getKichCo();
        });

        qnpSoLuongMua.setOnQuantityChangeListener(newQuantity -> {
            if (qnpSoLuongMua.getQuantity() == 0) {
                tvSubmit.setEnabled(false);
                tvSubmit.setAlpha(0.3f);
            } else {
                tvSubmit.setEnabled(true);
                tvSubmit.setAlpha(1f);
            }
            soLuongMua = newQuantity;
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflater = getLayoutInflater();
        View postView = layoutInflater.inflate(R.layout.item_loading_dialog, null);
        builder.setView(postView);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00FFFFFF")));

        tvSubmit.setOnClickListener((v -> {
            if (trangThai == 0) {
                if (gioHang == null) {
                    dialog.show();
                    String maGioHang = AutoStringID.createStringID("GH-");
                    int tongTien = donGia * soLuongMua;

                    GioHang gioHangNew = new GioHang(maGioHang, khachHang.getMaKhachHang(), maGiayCTMua,
                            kichCoMua, soLuongMua, tongTien);
                    new GioHangDAO(getContext()).themGiohang(gioHangNew);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            dismiss();
                            Toast.makeText(getContext(), "Đã thêm vào giỏ", Toast.LENGTH_SHORT).show();
                        }
                    }, 500);

                } else {
                    dialog.show();
                    int tongTien = donGia * soLuongMua;

                    gioHang.setMaGiayChiTiet(maGiayCTMua);
                    gioHang.setKichCoMua(kichCoMua);
                    gioHang.setSoLuongMua(soLuongMua);
                    gioHang.setTongTien(tongTien);

                    new GioHangDAO(getContext()).capNhatGioHang(gioHang);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            dismiss();
                            Toast.makeText(getContext(), "Đã cập nhật", Toast.LENGTH_SHORT).show();
                        }
                    }, 500);
                }
            } else if (trangThai == 1) {
                String maDonHangCT = AutoStringID.createStringID("DHCT-");
                String maDonHang = "0";
                int tongTien = donGia * soLuongMua;

                DonHangChiTiet donHangChiTiet = new DonHangChiTiet(maDonHangCT,
                        maDonHang, maGiayCTMua, kichCoMua, soLuongMua, tongTien);

                List<DonHangChiTiet> donHangChiTietList = new ArrayList<>();
                donHangChiTietList.add(donHangChiTiet);

                Intent intent = new Intent(getContext(), ThanhToanActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("donHangChiTietList", (Serializable) donHangChiTietList);
                bundle.putSerializable("khachHang", khachHang);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
                dismiss();
            }
        }));

        ImageButton iBtnClosePickUp = view.findViewById(R.id.iBtnClosePickUp);
        iBtnClosePickUp.setOnClickListener((v -> {
            dismiss();
        }));

        layDanhSachGiayCT(giay.getMaGiay());

        return view;
    }

    private void layDanhSachGiayCT(String maGiay) {
        FirebaseFirestore.getInstance().collection("GiayChiTiet")
                .whereEqualTo("maGiay", maGiay)
                .whereEqualTo("trangThaiGiayChiTiet", 0)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            GiayChiTiet giayChiTiet = snapshot.toObject(GiayChiTiet.class);
                            giayChiTietList.add(giayChiTiet);
                        }
                        KHGiayCTAdapter.notifyDataSetChanged();
                    }
                });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setContentView(R.layout.fragment_bottom_sheet_dialog_pick_up);

        FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.bottom_sheet_dialog_background);

        return dialog;
    }

}