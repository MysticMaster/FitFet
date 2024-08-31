package com.example.mobile_project_01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobile_project_01.DAO.DonHangDAO;
import com.example.mobile_project_01.DAO.GiayChiTietDAO;
import com.example.mobile_project_01.DAO.ThongKeDAO;
import com.example.mobile_project_01.adapter.KHDonHangCTAdapter;
import com.example.mobile_project_01.function.AutoStringID;
import com.example.mobile_project_01.function.FormatCurrency;
import com.example.mobile_project_01.model.DonHang;
import com.example.mobile_project_01.model.DonHangChiTiet;
import com.example.mobile_project_01.model.GiayChiTiet;
import com.example.mobile_project_01.model.ThongKe;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class KHHienThiDonHangActivity extends AppCompatActivity {

    private DonHang donHang;

    private List<DonHangChiTiet> donHangChiTietList;
    private KHDonHangCTAdapter adapter;

    private boolean checkCancel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khhien_thi_don_hang);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            donHang = (DonHang) bundle.getSerializable("donHang");
        }

        donHangChiTietList = new ArrayList<>();
        adapter = new KHDonHangCTAdapter(this, donHangChiTietList);

        ImageButton iBtnThoatHTDonHang = findViewById(R.id.iBtnThoatHTDonHang);
        TextView tvTrangThaiDonHang = findViewById(R.id.tvTrangThaiDonHang);
        TextView tvThongBaoTTDonHang = findViewById(R.id.tvThongBaoTTDonHang);
        TextView tvThongTinNhanHang = findViewById(R.id.tvThongTinNhanHang);
        TextView tvGhiChuDonHang = findViewById(R.id.tvGhiChuDonHang);
        RecyclerView rvDonHangCTHienThi = findViewById(R.id.rvDonHangCTHienThi);
        TextView tvMaDonHangHienThi = findViewById(R.id.tvMaDonHangHienThi);
        TextView tvThoiGianDatHang = findViewById(R.id.tvThoiGianDatHang);
        TextView tvThoiGianDuyetDon = findViewById(R.id.tvThoiGianDuyetDon);
        TextView tvThoiGianGiaoVC = findViewById(R.id.tvThoiGianGiaoVC);
        TextView tvThoiGianGiaoKH = findViewById(R.id.tvThoiGianGiaoKH);
        TextView tvThoiGianHoanThanh = findViewById(R.id.tvThoiGianHoanThanh);
        TextView tvTongThanhToanHienThi = findViewById(R.id.tvTongThanhToanHienThi);
        FrameLayout flFunctionKHCapNhatDH = findViewById(R.id.flFunctionKHCapNhatDH);
        ProgressBar pbKHCapNhatDH = findViewById(R.id.pbKHCapNhatDH);
        TextView tvKHFunctionDH = findViewById(R.id.tvKHFunctionDH);

        tvThongTinNhanHang.setText(donHang.getHoTenNguoiNhan() + "\n" + donHang.getSdtNguoiNhan()
                + "\n" + donHang.getDiaChi());
        tvGhiChuDonHang.setText(donHang.getGhiChu());

        tvMaDonHangHienThi.setText("Mã đơn hàng: " + donHang.getMaDonHang());
        tvThoiGianDatHang.setText("Thời gian đặt hàng: " + donHang.getGioTao() + " " + donHang.getNgayTao());
        tvThoiGianDuyetDon.setText("Thời gian duyệt đơn: " + donHang.getGioDuyet() + " " + donHang.getNgayDuyet());
        tvThoiGianGiaoVC.setText("Thời gian chuyển hàng: " + donHang.getGioGiaoVC() + " " + donHang.getNgayGiaoVC());
        tvThoiGianGiaoKH.setText("Thời gian đến nơi: " + donHang.getGioGiaoKH() + " " + donHang.getNgayGiaoKH());
        tvThoiGianHoanThanh.setText("Thời gian hoàn thành: " + donHang.getGioHoanThanh() + " " + donHang.getNgayHoanThanh());
        tvTongThanhToanHienThi.setText("₫" + FormatCurrency.formatCurrency(donHang.getTongThanhToan()));

        if (donHang.getTrangThaiDonHang() == 0) {
            tvTrangThaiDonHang.setText("Đơn hàng đang chờ xác nhận");
            tvThongBaoTTDonHang.setText("Bạn vui lòng đợi chúng mình duyệt đơn nha");

            tvThoiGianDuyetDon.setVisibility(View.GONE);
            tvThoiGianGiaoVC.setVisibility(View.GONE);
            tvThoiGianGiaoKH.setVisibility(View.GONE);
            tvThoiGianHoanThanh.setVisibility(View.GONE);

            tvKHFunctionDH.setText("Hủy Đơn");
            tvKHFunctionDH.setOnClickListener(v -> {
                openAlertDialog(5, pbKHCapNhatDH, tvKHFunctionDH);
            });
        } else if (donHang.getTrangThaiDonHang() == 1) {
            tvTrangThaiDonHang.setText("Đang chuẩn bị gửi hàng");
            tvThongBaoTTDonHang.setText("Chúng mình đang chuẩn bị hàng, bạn vui lòng đợi nha");

            tvThoiGianGiaoVC.setVisibility(View.GONE);
            tvThoiGianGiaoKH.setVisibility(View.GONE);
            tvThoiGianHoanThanh.setVisibility(View.GONE);
            tvKHFunctionDH.setVisibility(View.GONE);
            flFunctionKHCapNhatDH.setVisibility(View.GONE);
        } else if (donHang.getTrangThaiDonHang() == 2) {
            tvTrangThaiDonHang.setText("Đơn hàng đang được giao");
            tvThongBaoTTDonHang.setText("Đơn hàng đang trên đường đến với bạn nha");

            tvThoiGianGiaoKH.setVisibility(View.GONE);
            tvThoiGianHoanThanh.setVisibility(View.GONE);
            tvKHFunctionDH.setVisibility(View.GONE);
            flFunctionKHCapNhatDH.setVisibility(View.GONE);
        } else if (donHang.getTrangThaiDonHang() == 3) {
            tvTrangThaiDonHang.setText("Đơn hàng đã giao đến");
            tvThongBaoTTDonHang.setText("Đơn hàng đã đến nơi, cảm ơn bạn đã đợi nha");

            tvThoiGianHoanThanh.setVisibility(View.GONE);
            tvKHFunctionDH.setText("Đã Nhận Được Hàng");
            tvKHFunctionDH.setOnClickListener(v -> {
                openAlertDialog(4, pbKHCapNhatDH, tvKHFunctionDH);
            });

        } else if (donHang.getTrangThaiDonHang() == 4) {
            tvTrangThaiDonHang.setText("Đơn hàng đã hoàn thành");
            tvThongBaoTTDonHang.setText("Cảm ơn bạn đã ủng hộ FitFeet ạ");

            tvKHFunctionDH.setVisibility(View.GONE);
        } else if (donHang.getTrangThaiDonHang() == 5) {
            tvTrangThaiDonHang.setText("Chi tiết đơn hủy");
            tvThongBaoTTDonHang.setText("Đã hủy đơn vào " + donHang.getGioHuyDon() + " Ngày " + donHang.getNgayHuyDon());


            tvThoiGianDuyetDon.setVisibility(View.GONE);
            tvThoiGianGiaoVC.setVisibility(View.GONE);
            tvThoiGianGiaoKH.setVisibility(View.GONE);
            tvThoiGianHoanThanh.setVisibility(View.GONE);

            tvKHFunctionDH.setText("Mua Lại");
            tvKHFunctionDH.setOnClickListener(v -> {
                openAlertDialog(0, pbKHCapNhatDH, tvKHFunctionDH);
            });
        }

        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvDonHangCTHienThi.setLayoutManager(manager);
        rvDonHangCTHienThi.setAdapter(adapter);

        layDanhSachDonHangCT(donHang.getMaDonHang());

        iBtnThoatHTDonHang.setOnClickListener(v -> {
            finish();
        });
    }

    private void layDanhSachDonHangCT(String maDonHang) {
        FirebaseFirestore.getInstance().collection("DonHangChiTiet")
                .whereEqualTo("maDonHang", maDonHang)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            DonHangChiTiet donHangChiTiet = snapshot.toObject(DonHangChiTiet.class);
                            donHangChiTietList.add(donHangChiTiet);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void openAlertDialog(int status, ProgressBar progressBar, TextView textView) {
        DonHangDAO donHangDAO = new DonHangDAO(KHHienThiDonHangActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(KHHienThiDonHangActivity.this);
        builder.setTitle("Thông báo");
        String message = null;
        if (status == 5) {
            message = "Xác nhận hủy đơn hàng?";
        } else if (status == 4) {
            message = "Cảm ơn bạn đã mua hàng tại FitFeet";
        } else if (status == 0) {
            message = "Bạn muốn đặt mua lại sản phẩm này?";
        }
        builder.setMessage(message);

        LocalTime localTime = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String gio = localTime.format(timeFormatter);

        LocalDate localDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String ngay = localDate.format(dateFormatter);

        if (status == 5) {
            builder.setPositiveButton("Xác nhận", (((dialog, which) -> {
                progressBar.setVisibility(View.VISIBLE);
                textView.setVisibility(View.INVISIBLE);

                donHang.setTrangThaiDonHang(status);
                donHang.setGioHuyDon(gio);
                donHang.setNgayHuyDon(ngay);
                donHangDAO.capNhatDonHang(donHang);
                finish();
            })));

            builder.setNegativeButton("Hủy", ((dialog, which) -> {

            }));

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        if (status == 0) {
            builder.setPositiveButton("Xác nhận", (((dialog, which) -> {
                progressBar.setVisibility(View.VISIBLE);
                textView.setVisibility(View.INVISIBLE);

                donHang.setTrangThaiDonHang(status);
                donHang.setGioTao(gio);
                donHang.setNgayTao(ngay);
                donHangDAO.capNhatDonHang(donHang);
                finish();
            })));

            builder.setNegativeButton("Hủy", ((dialog, which) -> {

            }));

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        if (status == 4) {
            progressBar.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);

            donHang.setTrangThaiDonHang(status);
            donHang.setGioHoanThanh(gio);
            donHang.setNgayHoanThanh(ngay);
            donHangDAO.capNhatDonHang(donHang);

            ThongKeDAO thongKeDAO = new ThongKeDAO();
            FirebaseFirestore.getInstance().collection("DonHangChiTiet")
                    .whereEqualTo("maDonHang", donHang.getMaDonHang())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<DonHangChiTiet> readList = new ArrayList<>();
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                readList.add(snapshot.toObject(DonHangChiTiet.class));
                            }
                            for (DonHangChiTiet donHangChiTiet : readList) {
                                String maThongKe = AutoStringID.createStringID("TK-");
                                ThongKe thongKe = new ThongKe(maThongKe, donHangChiTiet.getMaGiayChiTiet(),
                                        ngay, donHangChiTiet.getTongTien(), donHangChiTiet.getSoLuongMua());
                                thongKeDAO.luuThongKe(thongKe);
                            }
                        }
                    });

            builder.setNegativeButton("Đóng", ((dialog, which) -> {
                checkCancel = true;
                finish();
            }));

            AlertDialog dialog = builder.create();
            dialog.show();

            dialog.setOnDismissListener(dialog1 -> {
                checkCancel = true;
                finish();
            });

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!checkCancel) {
                        dialog.dismiss();
                        finish();
                    }
                }
            }, 4000);
        }
    }

}