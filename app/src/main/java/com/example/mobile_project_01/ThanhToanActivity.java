package com.example.mobile_project_01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobile_project_01.DAO.DonHangChiTietDAO;
import com.example.mobile_project_01.DAO.DonHangDAO;
import com.example.mobile_project_01.DAO.GioHangDAO;
import com.example.mobile_project_01.adapter.KHDonHangCTAdapter;
import com.example.mobile_project_01.function.AutoStringID;
import com.example.mobile_project_01.function.FormatCurrency;
import com.example.mobile_project_01.model.DonHang;
import com.example.mobile_project_01.model.DonHangChiTiet;
import com.example.mobile_project_01.model.GioHang;
import com.example.mobile_project_01.model.KhachHang;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ThanhToanActivity extends AppCompatActivity {

    private int count = 0;
    private KhachHang khachHang;
    private List<DonHangChiTiet> donHangChiTietList;
    private List<GioHang> getGioHang;
    private Boolean checkCancel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            donHangChiTietList = (List<DonHangChiTiet>) bundle.getSerializable("donHangChiTietList");
            getGioHang = (List<GioHang>) bundle.getSerializable("getGioHang");
            khachHang = (KhachHang) bundle.getSerializable("khachHang");
        }

        ImageButton iBtnThoatDatHang = findViewById(R.id.iBtnThoatDatHang);
        EditText edtHoTenNguoiNhan = findViewById(R.id.edtHoTenNguoiNhan);
        edtHoTenNguoiNhan.setText(khachHang.getHoKhachHang() + " " + khachHang.getTenKhachHang());
        EditText edtSdtNguoiNhan = findViewById(R.id.edtSdtNguoiNhan);
        if (!khachHang.getSoDienThoai().equals("0")) {
            edtSdtNguoiNhan.setText(khachHang.getSoDienThoai());
        }
        EditText edtDiaChiNguoiNhan = findViewById(R.id.edtDiaChiNguoiNhan);
        if (!khachHang.getDiaChi().equals("0")) {
            edtDiaChiNguoiNhan.setText(khachHang.getDiaChi());
        }
        EditText edtGhiChu = findViewById(R.id.edtGhiChu);
        TextView tvThanhToan = findViewById(R.id.tvThanhToan);
        ProgressBar pbThanhToan = findViewById(R.id.pbThanhToan);
        TextView tvTongThanhToan = findViewById(R.id.tvTongThanhToan);

        RecyclerView rvDanhSachDonHangCT = findViewById(R.id.rvDanhSachDonHangCT);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        KHDonHangCTAdapter adapter = new KHDonHangCTAdapter(this, donHangChiTietList);
        rvDanhSachDonHangCT.setLayoutManager(manager);
        rvDanhSachDonHangCT.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        ViewTreeObserver.OnGlobalLayoutListener visibilityChange = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (edtHoTenNguoiNhan.getText().toString().isEmpty() ||
                        edtSdtNguoiNhan.getText().toString().trim().isEmpty() ||
                        !isValidPhoneNumber(edtSdtNguoiNhan.getText().toString().trim()) ||
                        edtDiaChiNguoiNhan.getText().toString().isEmpty()) {
                    tvThanhToan.setAlpha(0.3f);
                    tvThanhToan.setEnabled(false);
                } else {
                    tvThanhToan.setAlpha(1f);
                    tvThanhToan.setEnabled(true);
                }
            }
        };

        View rootView = getWindow().getDecorView().getRootView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(visibilityChange);

        for (DonHangChiTiet donHangChiTiet : donHangChiTietList) {
            count += donHangChiTiet.getTongTien();
        }
        tvTongThanhToan.setText("₫" + FormatCurrency.formatCurrency(count));

        tvThanhToan.setOnClickListener(v -> {
            tvThanhToan.setVisibility(View.INVISIBLE);
            pbThanhToan.setVisibility(View.VISIBLE);

            String maDonHang = AutoStringID.createStringID("DH-");
            String maThanhVien = "0";

            LocalTime localTime = LocalTime.now();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String gioTao = localTime.format(timeFormatter);

            LocalDate localDate = LocalDate.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String ngayTao = localDate.format(dateFormatter);

            String gioDuyet = "0";
            String ngayDuyet = "0";
            String gioGiaoVC = "0";
            String ngayGiaoVC = "0";
            String gioGiaoKH = "0";
            String ngayGiaoKH = "0";
            String gioHoanThanh = "0";
            String ngayHoanThanh = "0";
            String gioHuyDon = "0";
            String ngayHuyDon = "0";
            String diaChi = edtDiaChiNguoiNhan.getText().toString();
            String hoTenNguoiNhan = edtHoTenNguoiNhan.getText().toString();
            String sdtNguoiNhan = edtSdtNguoiNhan.getText().toString().trim();
            String ghiChu = "Trống";
            if (!edtGhiChu.getText().toString().isEmpty()) {
                ghiChu = edtGhiChu.getText().toString();
            }

            int tongThanhToan = count;
            int trangThaiDonHang = 0;

            DonHang donHang = new DonHang(maDonHang, maThanhVien, khachHang.getMaKhachHang(),
                    gioTao, ngayTao, gioDuyet, ngayDuyet, gioGiaoVC, ngayGiaoVC,
                    gioGiaoKH, ngayGiaoKH, gioHoanThanh, ngayHoanThanh, gioHuyDon,
                    ngayHuyDon, diaChi, hoTenNguoiNhan, sdtNguoiNhan, ghiChu,
                    tongThanhToan, trangThaiDonHang);
            DonHangDAO donHangDAO = new DonHangDAO(ThanhToanActivity.this);
            donHangDAO.taoDonHang(donHang);

            DonHangChiTietDAO donHangChiTietDAO = new DonHangChiTietDAO(ThanhToanActivity.this);
            for (DonHangChiTiet donHangChiTiet : donHangChiTietList) {
                donHangChiTiet.setMaDonHang(maDonHang);
                donHangChiTietDAO.taoDonHangCT(donHangChiTiet);
            }

            if (getGioHang != null) {
                for (GioHang gioHang : getGioHang) {
                    new GioHangDAO(this).xoaGioHang(gioHang);
                }
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(ThanhToanActivity.this);
            builder.setTitle("Thông báo");
            builder.setMessage("Đặt hàng thành công");
            builder.setPositiveButton("Đóng", ((dialog, which) -> {
                checkCancel = true;
                finish();
            }));

            AlertDialog dialog = builder.create();

            dialog.setOnDismissListener(dialog1 -> {
                checkCancel = true;
                finish();
            });

            dialog.show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!checkCancel) {
                        dialog.dismiss();
                        finish();
                    }
                }
            }, 3000);
        });

        TextView tvDieuKhoan = findViewById(R.id.tvDieuKhoan);
        SpannableString spannableString = new SpannableString("Nhấn 'Đặt hàng' đồng nghĩa với việc bạn đồng ý tuân theo Điều khoản của FitFeet.");

        spannableString.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                spannableString.toString().indexOf("Đặt hàng"),
                spannableString.toString().indexOf("Đặt hàng") + "Đặt hàng".length(),
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                // Xử lý khi chữ "Điều khoản" được click
                showDialogDieuKhoan();
            }
        };

        spannableString.setSpan(clickableSpan,
                spannableString.toString().indexOf("Điều khoản"),
                spannableString.toString().indexOf("Điều khoản") + "Điều khoản".length(),
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new StyleSpan(Typeface.ITALIC),
                spannableString.toString().indexOf("Điều khoản"),
                spannableString.toString().indexOf("Điều khoản") + "Điều khoản".length(),
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvDieuKhoan.setText(spannableString);
        tvDieuKhoan.setMovementMethod(LinkMovementMethod.getInstance());


        iBtnThoatDatHang.setOnClickListener(v -> {
            finish();
        });
    }

    private void showDialogDieuKhoan() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.item_dialog_dieu_khoan, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        dialog.setOnDismissListener(dialog1 -> {
            dialog.dismiss();
        });

        TextView tvCloseDieuKhoanDialog = view.findViewById(R.id.tvCloseDieuKhoanDialog);
        tvCloseDieuKhoanDialog.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return Patterns.PHONE.matcher(phoneNumber).matches();
    }
}