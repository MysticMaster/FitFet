package com.example.mobile_project_01;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mobile_project_01.adapter.QLDonHangAdapter;
import com.example.mobile_project_01.model.DonHang;
import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.ThongBao;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QLDanhSachDonHangActivity extends AppCompatActivity {

    private String maThanhVien;
    private int trangThaiDonHang;
    private List<DonHang> donHangList;

    private QLDonHangAdapter adapter;

    private LinearLayout llKhongCoGi;

    private RecyclerView rvQLDSDonHang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qldanh_sach_don_hang);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            trangThaiDonHang = bundle.getInt("trangThaiDonHang");
            maThanhVien = bundle.getString("maThanhVien");
        }

        ImageButton iBtnThoatQLDSDonHang = findViewById(R.id.iBtnThoatQLDSDonHang);
        TextView tvLoaiDSDonHang = findViewById(R.id.tvLoaiDSDonHang);
        rvQLDSDonHang = findViewById(R.id.rvQLDSDonHang);
        donHangList = new ArrayList<>();
        adapter = new QLDonHangAdapter(this, donHangList, maThanhVien);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvQLDSDonHang.setLayoutManager(manager);
        rvQLDSDonHang.setAdapter(adapter);

        llKhongCoGi = findViewById(R.id.llKhongCoGi);

        if (trangThaiDonHang == 0) {
            tvLoaiDSDonHang.setText("Chờ xác nhận");
        } else if (trangThaiDonHang == 1) {
            tvLoaiDSDonHang.setText("Chuẩn bị hàng");
        } else if (trangThaiDonHang == 2) {
            tvLoaiDSDonHang.setText("Đang giao");
        } else if (trangThaiDonHang == 3) {
            tvLoaiDSDonHang.setText("Đã giao");
        } else if (trangThaiDonHang == 4) {
            tvLoaiDSDonHang.setText("Đã hoàn thành");
        } else if (trangThaiDonHang == 6) {
            tvLoaiDSDonHang.setText("Không hợp lệ");
        } else if (trangThaiDonHang == 7) {
            tvLoaiDSDonHang.setText("Bị từ chối");
        }

        layDanhSachDonHang(trangThaiDonHang);

        iBtnThoatQLDSDonHang.setOnClickListener(v -> {
            finish();
        });
    }

    private void layDanhSachDonHang(int trangThaiDonHang) {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("DonHang");

        reference.whereEqualTo("trangThaiDonHang", trangThaiDonHang)
                .addSnapshotListener(((value, error) -> {
                    if (error != null) {
                        Log.e(TAG, "Đã có lỗi: ", error);
                        return;
                    }
                    if (value != null) {
                        for (DocumentChange change : value.getDocumentChanges()) {
                            switch (change.getType()) {
                                case ADDED:
                                    DocumentSnapshot addSnapshot = change.getDocument();
                                    DonHang addDonHang = addSnapshot.toObject(DonHang.class);
                                    List<DonHang> readList = new ArrayList<>();
                                    readList.add(addDonHang);
                                    collectionsSort(readList, donHangList);
                                    adapter.notifyDataSetChanged();
                                    break;
                                case MODIFIED:
                                    DocumentSnapshot modiSnapshot = change.getDocument();
                                    DonHang modiDonHang = modiSnapshot.toObject(DonHang.class);
                                    for (int i = 0; i < donHangList.size(); i++) {
                                        DonHang donHangIndex = donHangList.get(i);
                                        if (donHangIndex.getMaDonHang().equals(modiDonHang.getMaDonHang())) {
                                            donHangList.set(i, modiDonHang);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                                case REMOVED:
                                    String removeID = change.getDocument().getId();
                                    for (int i = 0; i < donHangList.size(); i++) {
                                        if (donHangList.get(i).getMaDonHang().equals(removeID)) {
                                            donHangList.remove(i);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                            }
                        }
                        if (donHangList.size() == 0) {
                            llKhongCoGi.setVisibility(View.VISIBLE);
                            rvQLDSDonHang.setVisibility(View.GONE);
                        } else {
                            llKhongCoGi.setVisibility(View.GONE);
                            rvQLDSDonHang.setVisibility(View.VISIBLE);
                        }
                    }
                }));
    }

    private void collectionsSort(List<DonHang> list, List<DonHang> donHangList) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        String date = null;
        String time = null;

        for (DonHang donHang : list) {

            if (donHang.getTrangThaiDonHang() == 0) {
                date = donHang.getNgayTao();
                time = donHang.getGioTao();
            } else if (donHang.getTrangThaiDonHang() == 1) {
                date = donHang.getNgayDuyet();
                time = donHang.getGioDuyet();
            } else if (donHang.getTrangThaiDonHang() == 2) {
                date = donHang.getNgayGiaoVC();
                time = donHang.getGioGiaoVC();
            } else if (donHang.getTrangThaiDonHang() == 3) {
                date = donHang.getNgayGiaoKH();
                time = donHang.getGioGiaoKH();
            } else if (donHang.getTrangThaiDonHang() == 4) {
                date = donHang.getNgayHoanThanh();
                time = donHang.getGioHoanThanh();
            }

            LocalDate localDate = LocalDate.parse(date, dateFormatter);
            LocalTime localTime = LocalTime.parse(time, timeFormatter);

            LocalDateTime dateTime = LocalDateTime.of(localDate, localTime);
            donHang.setLocalDateTime(dateTime);
            donHangList.add(donHang);
        }
        Collections.sort(donHangList, ((o1, o2) -> o2.getLocalDateTime().compareTo(o1.getLocalDateTime())));
    }

}