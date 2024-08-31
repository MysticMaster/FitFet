package com.example.mobile_project_01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobile_project_01.DAO.DonHangChiTietDAO;
import com.example.mobile_project_01.DAO.DonHangDAO;
import com.example.mobile_project_01.DAO.GiayChiTietDAO;
import com.example.mobile_project_01.DAO.KichCoGiayCTDAO;
import com.example.mobile_project_01.DAO.ThongBaoDAO;
import com.example.mobile_project_01.adapter.QLDonHangCTAdapter;
import com.example.mobile_project_01.function.AutoStringID;
import com.example.mobile_project_01.function.FormatCurrency;
import com.example.mobile_project_01.model.DonHang;
import com.example.mobile_project_01.model.DonHangChiTiet;
import com.example.mobile_project_01.model.GiayChiTiet;
import com.example.mobile_project_01.model.KichCoGiayCT;
import com.example.mobile_project_01.model.ThongBao;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QLHienThiDonHangActivity extends AppCompatActivity {

    private DonHang donHang;

    private String maThanhVien;

    private List<DonHangChiTiet> donHangChiTietList;
    private QLDonHangCTAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlhien_thi_don_hang);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            donHang = (DonHang) bundle.getSerializable("donHang");
            maThanhVien = bundle.getString("maThanhVien");
        }

        donHangChiTietList = new ArrayList<>();
        adapter = new QLDonHangCTAdapter(this, donHangChiTietList, getSupportFragmentManager());

        ImageButton iBtnThoatHTDH = findViewById(R.id.iBtnThoatHTDH);
        TextView tvTrangThaiDH = findViewById(R.id.tvTrangThaiDH);
        TextView tvQLThongTinNhanHang = findViewById(R.id.tvQLThongTinNhanHang);
        TextView tvQLGhiChuDonHang = findViewById(R.id.tvQLGhiChuDonHang);
        RecyclerView rvQLDonHangCTHienThi = findViewById(R.id.rvQLDonHangCTHienThi);
        TextView tvQLMaDonHangHienThi = findViewById(R.id.tvQLMaDonHangHienThi);
        TextView tvQLTongThanhToanHienThi = findViewById(R.id.tvQLTongThanhToanHienThi);
        TextView tvQLThoiGianDatHang = findViewById(R.id.tvQLThoiGianDatHang);
        TextView tvQLThoiGianDuyetDon = findViewById(R.id.tvQLThoiGianDuyetDon);
        TextView tvQLThoiGianGiaoVC = findViewById(R.id.tvQLThoiGianGiaoVC);
        TextView tvQLThoiGianGiaoKH = findViewById(R.id.tvQLThoiGianGiaoKH);
        TextView tvQLThoiGianHoanThanh = findViewById(R.id.tvQLThoiGianHoanThanh);
        FrameLayout flFunctionQLCapNhatDH = findViewById(R.id.flFunctionQLCapNhatDH);
        ProgressBar pbQLCapNhatDH = findViewById(R.id.pbQLCapNhatDH);
        TextView tvQLCapNhatDH = findViewById(R.id.tvQLCapNhatDH);
        TextView tvQLExtraFunctionDH = findViewById(R.id.tvQLExtraFunctionDH);

        tvQLThongTinNhanHang.setText(donHang.getHoTenNguoiNhan() + "\n" + donHang.getSdtNguoiNhan()
                + "\n" + donHang.getDiaChi());
        tvQLGhiChuDonHang.setText(donHang.getGhiChu());

        tvQLMaDonHangHienThi.setText("Mã đơn hàng: " + donHang.getMaDonHang());
        tvQLTongThanhToanHienThi.setText("₫" + FormatCurrency.formatCurrency(donHang.getTongThanhToan()));
        tvQLThoiGianDatHang.setText("Thời gian đặt hàng: " + donHang.getGioTao() + " " + donHang.getNgayTao());
        tvQLThoiGianDuyetDon.setText("Thời gian duyệt đơn: " + donHang.getGioDuyet() + " " + donHang.getNgayDuyet());
        tvQLThoiGianGiaoVC.setText("Thời gian chuyển hàng: " + donHang.getGioGiaoVC() + " " + donHang.getNgayGiaoVC());
        tvQLThoiGianGiaoKH.setText("Thời gian đến nơi: " + donHang.getGioGiaoKH() + " " + donHang.getNgayGiaoKH());
        tvQLThoiGianHoanThanh.setText("Thời gian hoàn thành: " + donHang.getGioHoanThanh() + " " + donHang.getNgayHoanThanh());

        if (donHang.getTrangThaiDonHang() == 0) {
            tvTrangThaiDH.setText("Chờ xác nhận");

            tvQLThoiGianDuyetDon.setVisibility(View.GONE);
            tvQLThoiGianGiaoVC.setVisibility(View.GONE);
            tvQLThoiGianGiaoKH.setVisibility(View.GONE);
            tvQLThoiGianHoanThanh.setVisibility(View.GONE);

            tvQLCapNhatDH.setText("Xác Nhận");
            tvQLCapNhatDH.setOnClickListener(v -> {
                openAlertDialog(1, pbQLCapNhatDH, tvQLCapNhatDH);
            });

            tvQLExtraFunctionDH.setText("Hủy đơn hàng");
            tvQLExtraFunctionDH.setVisibility(View.VISIBLE);
            tvQLExtraFunctionDH.setOnClickListener(v -> {
                DonHangDAO donHangDAO = new DonHangDAO(QLHienThiDonHangActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(QLHienThiDonHangActivity.this);
                builder.setTitle("Thông báo");
                builder.setMessage("Xác nhận đơn hàng này là SPAM");

                builder.setPositiveButton("Xác nhận", ((dialog, which) -> {
                    donHang.setGhiChu("Đơn hàng bị hủy do SPAM");
                    donHang.setTrangThaiDonHang(6);
                    donHangDAO.capNhatDonHang(donHang);
                    finish();
                }));

                builder.setNegativeButton("Hủy", ((dialog, which) -> {

                }));

                AlertDialog dialog = builder.create();
                dialog.show();
            });

        } else if (donHang.getTrangThaiDonHang() == 1) {
            tvTrangThaiDH.setText("Chờ lấy hàng");

            tvQLThoiGianGiaoVC.setVisibility(View.GONE);
            tvQLThoiGianGiaoKH.setVisibility(View.GONE);
            tvQLThoiGianHoanThanh.setVisibility(View.GONE);

            tvQLCapNhatDH.setText("Giao Vận Chuyển");
            tvQLCapNhatDH.setOnClickListener(v -> {
                openAlertDialog(2, pbQLCapNhatDH, tvQLCapNhatDH);
            });
        } else if (donHang.getTrangThaiDonHang() == 2) {
            tvTrangThaiDH.setText("Đang giao");

            tvQLThoiGianGiaoKH.setVisibility(View.GONE);
            tvQLThoiGianHoanThanh.setVisibility(View.GONE);

            tvQLCapNhatDH.setText("Giao Khách Hàng");
            tvQLCapNhatDH.setOnClickListener(v -> {
                openAlertDialog(3, pbQLCapNhatDH, tvQLCapNhatDH);
            });

            tvQLExtraFunctionDH.setText("Lấy hàng về");
            tvQLExtraFunctionDH.setVisibility(View.VISIBLE);
            tvQLExtraFunctionDH.setOnClickListener(v -> {
                DonHangDAO donHangDAO = new DonHangDAO(QLHienThiDonHangActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(QLHienThiDonHangActivity.this);
                builder.setTitle("Thông báo");
                builder.setMessage("Xác nhận đơn hàng bị từ chối nhận");

                builder.setPositiveButton("Xác nhận", ((dialog, which) -> {
                    FirebaseFirestore.getInstance().collection("DonHangChiTiet")
                            .whereEqualTo("maDonHang", donHang.getMaDonHang())
                            .get().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    donHang.setGhiChu("Khách hàng không nhận hàng");
                                    donHang.setTrangThaiDonHang(7);
                                    donHangDAO.capNhatDonHang(donHang);

                                    List<DonHangChiTiet> list = new ArrayList<>();
                                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                        list.add(snapshot.toObject(DonHangChiTiet.class));
                                    }

                                    KichCoGiayCTDAO kichCoGiayCTDAO = new KichCoGiayCTDAO(this);
                                    for (DonHangChiTiet donHangChiTiet : list) {

                                        CollectionReference reference = FirebaseFirestore.getInstance().collection("KichCoGiayChiTiet");
                                        reference.whereEqualTo("maGiayCT", donHangChiTiet.getMaGiayChiTiet())
                                                .whereEqualTo("kichCo", donHangChiTiet.getKichCoMua())
                                                .get()
                                                .addOnCompleteListener(task1 -> {
                                                    KichCoGiayCT kichCoGiayCT = task1.getResult()
                                                            .getDocuments().get(0).toObject(KichCoGiayCT.class);
                                                    kichCoGiayCT.setSoLuong(kichCoGiayCT.getSoLuong() + donHangChiTiet.getSoLuongMua());
                                                    kichCoGiayCTDAO.capNhatKichCo(kichCoGiayCT);
                                                });
                                    }

                                    finish();
                                }
                            });
                }));

                builder.setNegativeButton("Hủy", ((dialog, which) -> {

                }));

                AlertDialog dialog = builder.create();
                dialog.show();
            });
        } else if (donHang.getTrangThaiDonHang() == 3) {
            tvTrangThaiDH.setText("Đã giao");

            tvQLThoiGianHoanThanh.setVisibility(View.GONE);
            flFunctionQLCapNhatDH.setVisibility(View.GONE);
        } else if (donHang.getTrangThaiDonHang() == 4) {
            tvTrangThaiDH.setText("Đã hoàn thành");
            flFunctionQLCapNhatDH.setVisibility(View.GONE);
        } else if (donHang.getTrangThaiDonHang() == 6) {
            tvTrangThaiDH.setText("Không hợp lệ");

            tvQLThoiGianDuyetDon.setVisibility(View.GONE);
            tvQLThoiGianGiaoVC.setVisibility(View.GONE);
            tvQLThoiGianGiaoKH.setVisibility(View.GONE);
            tvQLThoiGianHoanThanh.setVisibility(View.GONE);

            tvQLExtraFunctionDH.setText("Xóa bỏ");
            tvQLExtraFunctionDH.setVisibility(View.VISIBLE);
            tvQLExtraFunctionDH.setOnClickListener(v -> {

                DonHangDAO donHangDAO = new DonHangDAO(QLHienThiDonHangActivity.this);
                DonHangChiTietDAO donHangChiTietDAO = new DonHangChiTietDAO(QLHienThiDonHangActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(QLHienThiDonHangActivity.this);
                builder.setTitle("Thông báo");
                builder.setMessage("Xác nhận xóa đơn hàng SPAM");

                builder.setPositiveButton("Xác nhận", ((dialog, which) -> {
                    FirebaseFirestore.getInstance().collection("DonHangChiTiet")
                            .whereEqualTo("maDonHang", donHang.getMaDonHang())
                            .get().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                        donHangChiTietDAO.xoaDonHangCT(snapshot.toObject(DonHangChiTiet.class));
                                    }
                                    donHangDAO.xoaDonHang(donHang);

                                    finish();
                                }
                            });
                }));

                builder.setNegativeButton("Hủy", ((dialog, which) -> {

                }));

                AlertDialog dialog = builder.create();
                dialog.show();
            });

            flFunctionQLCapNhatDH.setVisibility(View.GONE);
        } else if (donHang.getTrangThaiDonHang() == 7) {
            tvTrangThaiDH.setText("Bị từ chối");

            tvQLThoiGianGiaoKH.setVisibility(View.GONE);
            tvQLThoiGianHoanThanh.setVisibility(View.GONE);

            flFunctionQLCapNhatDH.setVisibility(View.GONE);
        }

        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvQLDonHangCTHienThi.setLayoutManager(manager);
        rvQLDonHangCTHienThi.setAdapter(adapter);

        layDanhSachDonHangCT(donHang.getMaDonHang());

        iBtnThoatHTDH.setOnClickListener(v -> {
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
        DonHangDAO donHangDAO = new DonHangDAO(QLHienThiDonHangActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(QLHienThiDonHangActivity.this);
        builder.setTitle("Thông báo");
        String message = null;
        if (status == 1) {
            message = "Xác nhận duyệt đơn hàng này?";
        } else if (status == 2) {
            message = "Hàng hóa đã đúng yêu cầu đơn hàng?";
        } else if (status == 3) {
            message = "Xác nhận lại hành động?";
        }
        builder.setMessage(message);

        LocalTime localTime = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String gio = localTime.format(timeFormatter);

        LocalDate localDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String ngay = localDate.format(dateFormatter);


        builder.setPositiveButton("Xác nhận", ((dialog, which) -> {
            if (status == 1) {
                progressBar.setVisibility(View.VISIBLE);
                textView.setVisibility(View.INVISIBLE);

                donHang.setTrangThaiDonHang(status);
                donHang.setMaThanhVien(maThanhVien);
                donHang.setGioDuyet(gio);
                donHang.setNgayDuyet(ngay);
                donHangDAO.capNhatDonHang(donHang);

                String maTB = AutoStringID.createStringID("TB-");
                String maDonHang = donHang.getMaDonHang();
                String noiDung = "Đơn hàng " + donHang.getMaDonHang() + " đã được xác nhận";
                ThongBao thongBao = new ThongBao(maTB, donHang.getMaKhachHang(), donHang.getMaDonHang(), noiDung,
                        gio, ngay, 1, 0);

                new ThongBaoDAO(this).taoThongBao(thongBao);
                finish();
            } else if (status == 2) {
                progressBar.setVisibility(View.VISIBLE);
                textView.setVisibility(View.INVISIBLE);

                FirebaseFirestore.getInstance().collection("DonHangChiTiet")
                        .whereEqualTo("maDonHang", donHang.getMaDonHang())
                        .get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                donHang.setTrangThaiDonHang(status);
                                donHang.setGioGiaoVC(gio);
                                donHang.setNgayGiaoVC(ngay);
                                donHangDAO.capNhatDonHang(donHang);

                                List<DonHangChiTiet> list = new ArrayList<>();
                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    list.add(snapshot.toObject(DonHangChiTiet.class));
                                }

                                KichCoGiayCTDAO kichCoGiayCTDAO = new KichCoGiayCTDAO(this);
                                for (DonHangChiTiet donHangChiTiet : list) {

                                    CollectionReference reference = FirebaseFirestore.getInstance().collection("KichCoGiayChiTiet");
                                    reference.whereEqualTo("maGiayCT", donHangChiTiet.getMaGiayChiTiet())
                                            .whereEqualTo("kichCo", donHangChiTiet.getKichCoMua())
                                            .get()
                                            .addOnCompleteListener(task1 -> {
                                                KichCoGiayCT kichCoGiayCT = task1.getResult()
                                                        .getDocuments().get(0).toObject(KichCoGiayCT.class);
                                                kichCoGiayCT.setSoLuong(kichCoGiayCT.getSoLuong() - donHangChiTiet.getSoLuongMua());
                                                kichCoGiayCTDAO.capNhatKichCo(kichCoGiayCT);
                                            });
                                }

                                finish();
                            } else {
                                Toast.makeText(this, "Mạng yếu, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                textView.setVisibility(View.VISIBLE);
                            }
                        });
            } else if (status == 3) {
                progressBar.setVisibility(View.VISIBLE);
                textView.setVisibility(View.INVISIBLE);

                donHang.setTrangThaiDonHang(status);
                donHang.setGioGiaoKH(gio);
                donHang.setNgayGiaoKH(ngay);
                donHangDAO.capNhatDonHang(donHang);

                String maTB = AutoStringID.createStringID("TB-");
                String noiDung = "Đơn hàng " + donHang.getMaDonHang() + " đã được giao đến nơi";
                ThongBao thongBao = new ThongBao(maTB, donHang.getMaKhachHang(), donHang.getMaDonHang(), noiDung,
                        gio, ngay, 3, 0);

                new ThongBaoDAO(this).taoThongBao(thongBao);
                finish();
            }
        }));

        builder.setNegativeButton("Hủy", ((dialog, which) -> {

        }));

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}