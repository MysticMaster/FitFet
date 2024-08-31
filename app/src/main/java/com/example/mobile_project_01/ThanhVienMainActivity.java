package com.example.mobile_project_01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.mobile_project_01.DAO.DonHangDAO;
import com.example.mobile_project_01.DAO.ThongKeDAO;
import com.example.mobile_project_01.fragment.KHQuanLyTaiKhoanFragment;
import com.example.mobile_project_01.fragment.LichSuBanHangFragment;
import com.example.mobile_project_01.fragment.QLDonHangFragment;
import com.example.mobile_project_01.fragment.QuanLyFragment;
import com.example.mobile_project_01.fragment.TVQuanLyTaiKhoanFragment;
import com.example.mobile_project_01.fragment.ThongKeFragment;
import com.example.mobile_project_01.function.AutoStringID;
import com.example.mobile_project_01.model.DonHang;
import com.example.mobile_project_01.model.DonHangChiTiet;
import com.example.mobile_project_01.model.ThanhVien;
import com.example.mobile_project_01.model.ThongKe;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ThanhVienMainActivity extends AppCompatActivity {

    private ThanhVien thanhVien;
    private Boolean change = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_vien_main);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            thanhVien = (ThanhVien) bundle.getSerializable("thanhVien");
            change = bundle.getBoolean("change");
        }

        BottomNavigationView bnvTVMain = findViewById(R.id.bnvTVMain);
        bnvTVMain.setItemIconTintList(null);

        if (savedInstanceState == null) {
            setSupportFragmentManager(new QuanLyFragment());
        }

        if (change == true) {
            setSupportFragmentManager(new TVQuanLyTaiKhoanFragment(thanhVien));
            MenuItem menuItem = bnvTVMain.getMenu().findItem(R.id.taiKhoanTV);
            menuItem.setChecked(true);
        }


        bnvTVMain.setOnNavigationItemSelectedListener((item -> {
            if (item.getItemId() == R.id.danhSachSP) {
                setSupportFragmentManager(new QuanLyFragment());
                return true;
            } else if (item.getItemId() == R.id.donHang) {
                setSupportFragmentManager(new QLDonHangFragment(thanhVien.getMaThanhVien()));
                return true;
            } else if (item.getItemId() == R.id.thongKe) {
                setSupportFragmentManager(new ThongKeFragment());
                return true;
            } else if (item.getItemId() == R.id.taiKhoanTV) {
                setSupportFragmentManager(new TVQuanLyTaiKhoanFragment(thanhVien));
                return true;
            }else if (item.getItemId() == R.id.lichSu) {
                setSupportFragmentManager(new LichSuBanHangFragment());
                return true;
            }
            return false;
        }));

        boDemNguocThoiGian();
    }

    private void setSupportFragmentManager(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flTVMain, fragment).commit();
    }

    private void boDemNguocThoiGian() {
        LocalTime timeNow = LocalTime.now();
        LocalDate dateNow = LocalDate.now();

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        String gioHoanThanh = timeNow.format(timeFormatter);
        String ngayHoanThanh = dateNow.format(dateFormatter);

        FirebaseFirestore.getInstance().collection("DonHang")
                .whereEqualTo("trangThaiDonHang", 3)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DonHang> list = new ArrayList<>();
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            list.add(snapshot.toObject(DonHang.class));
                        }
                        for (DonHang donHang : list) {
                            LocalDate specifiedDate = LocalDate.parse(donHang.getNgayGiaoKH(), dateFormatter);

                            long soNgayDemNguoc = ChronoUnit.DAYS.between(dateNow, specifiedDate);
                            if (soNgayDemNguoc > 3) {
                                donHang.setTrangThaiDonHang(4);
                                donHang.setGioHoanThanh(gioHoanThanh);
                                donHang.setNgayHoanThanh(ngayHoanThanh);
                                DonHangDAO donHangDAO = new DonHangDAO(ThanhVienMainActivity.this);
                                donHangDAO.capNhatDonHang(donHang);

                                ThongKeDAO thongKeDAO = new ThongKeDAO();
                                FirebaseFirestore.getInstance().collection("DonHangChiTiet")
                                        .whereEqualTo("maDonHang", donHang.getMaDonHang())
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                List<DonHangChiTiet> readList = new ArrayList<>();
                                                for (QueryDocumentSnapshot snapshot : task1.getResult()) {
                                                    readList.add(snapshot.toObject(DonHangChiTiet.class));
                                                }
                                                for (DonHangChiTiet donHangChiTiet : readList) {
                                                    String maThongKe = AutoStringID.createStringID("TK-");
                                                    ThongKe thongKe = new ThongKe(maThongKe, donHangChiTiet.getMaGiayChiTiet(),
                                                            ngayHoanThanh, donHangChiTiet.getTongTien(), donHangChiTiet.getSoLuongMua());
                                                    thongKeDAO.luuThongKe(thongKe);
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có chắc muốn thoát ứng dụng?")
                .setPositiveButton("Có", (dialog, which) -> finish())
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show();
    }

}