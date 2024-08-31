package com.example.mobile_project_01;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mobile_project_01.DAO.BinhLuanDAO;
import com.example.mobile_project_01.adapter.BinhLuanAdapter;
import com.example.mobile_project_01.adapter.KHGiayCTAdapter;
import com.example.mobile_project_01.fragment.BottomSheetDialogPickUpFragment;
import com.example.mobile_project_01.function.AutoStringID;
import com.example.mobile_project_01.model.BinhLuan;
import com.example.mobile_project_01.model.DonHang;
import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.GiayChiTiet;
import com.example.mobile_project_01.model.KhachHang;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KHHienThiGiayActivity extends AppCompatActivity {

    private List<GiayChiTiet> giayChiTietList;
    private KHGiayCTAdapter adapter;

    private List<BinhLuan> binhLuanList;
    private BinhLuanAdapter binhLuanAdapter;

    private Giay giay;
    private KhachHang khachHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khhien_thi_giay);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            giay = (Giay) bundle.getSerializable("giay");
            khachHang = (KhachHang) bundle.getSerializable("khachHang");
        }

        ImageView ivAnhGiayHienThi = findViewById(R.id.ivAnhGiayHienThi);
        TextView tvTenGiayHienThi = findViewById(R.id.tvTenGiayHienThi);
        TextView tvTenGiayCTHienThi = findViewById(R.id.tvTenGiayCTHienThi);

        if (giay.getAnhGiay().equals("0")) {
            ivAnhGiayHienThi.setImageResource(R.drawable.none_image);
        } else {
            Glide.with(this).load(giay.getAnhGiay()).into(ivAnhGiayHienThi);
        }
        tvTenGiayHienThi.setText(giay.getTenGiay());

        ImageButton iBtnThoatHienThi = findViewById(R.id.iBtnThoatHienThi);
        ImageButton iBtnGioHangHT = findViewById(R.id.iBtnGioHangHT);

        RecyclerView rvGiayChiTiet = findViewById(R.id.rvGiayChiTiet);
        giayChiTietList = new ArrayList<>();
        adapter = new KHGiayCTAdapter(this, giayChiTietList);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvGiayChiTiet.setLayoutManager(manager);
        rvGiayChiTiet.setAdapter(adapter);

        TextView tvBinhLuan = findViewById(R.id.tvBinhLuan);
        RelativeLayout rlBinhLuan = findViewById(R.id.rlBinhLuan);
        ImageView ivAvtBinhLuan = findViewById(R.id.ivAvtBinhLuan);
        EditText edtNoiDungBinhLuan = findViewById(R.id.edtNoiDungBinhLuan);
        ImageButton iBtnGuiBinhLuan = findViewById(R.id.iBtnGuiBinhLuan);
        RecyclerView rvDanhSachBinhLuan = findViewById(R.id.rvDanhSachBinhLuan);

        if (khachHang.getAnhKhachHang().equals("0")) {
            ivAvtBinhLuan.setImageResource(R.drawable.none_image);
        } else {
            Glide.with(this).load(khachHang.getAnhKhachHang()).into(ivAvtBinhLuan);
        }

        binhLuanList = new ArrayList<>();
        binhLuanAdapter = new BinhLuanAdapter(this, binhLuanList, khachHang.getMaKhachHang());

        LinearLayoutManager blManager = new LinearLayoutManager(this);
        rvDanhSachBinhLuan.setLayoutManager(blManager);
        rvDanhSachBinhLuan.setAdapter(binhLuanAdapter);

        LocalTime localTime = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String gioTao = localTime.format(timeFormatter);

        LocalDate localDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String ngayTao = localDate.format(dateFormatter);

        adapter.setOnItemClickListener(giayChiTiet -> {
            tvTenGiayCTHienThi.setText(giayChiTiet.getTenGiayChiTiet());
            if (giayChiTiet.getAnhGiayChiTiet().equals("0")) {
                ivAnhGiayHienThi.setImageResource(R.drawable.none_image);
            } else {
                Glide.with(KHHienThiGiayActivity.this).load(giayChiTiet.getAnhGiayChiTiet()).into(ivAnhGiayHienThi);
            }

        });

        tvBinhLuan.setOnClickListener(v -> {
            if (rlBinhLuan.getVisibility() == View.GONE) {
                tvBinhLuan.setText("Bình luận <<");
                rlBinhLuan.setVisibility(View.VISIBLE);
            } else if (rlBinhLuan.getVisibility() == View.VISIBLE) {
                tvBinhLuan.setText("Bình luận >>");
                rlBinhLuan.setVisibility(View.GONE);
            }
        });

        iBtnGuiBinhLuan.setOnClickListener(v -> {
            if (!edtNoiDungBinhLuan.getText().toString().trim().isEmpty()) {
                BinhLuan binhLuan = new BinhLuan(AutoStringID.createStringID("BL-"),
                        giay.getMaGiay(),
                        khachHang.getMaKhachHang(), edtNoiDungBinhLuan.getText().toString(),
                        gioTao, ngayTao, 0);
                new BinhLuanDAO(this).taoBinhLuan(binhLuan);

                edtNoiDungBinhLuan.setText(null);
            }
        });

        TextView tvThemVaoGio = findViewById(R.id.tvThemVaoGio);
        TextView tvMuaNgay = findViewById(R.id.tvMuaNgay);

        iBtnGioHangHT.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, GioHangActivity.class);
            Bundle send = new Bundle();
            send.putSerializable("khachHang", khachHang);
            intent1.putExtras(send);
            startActivity(intent1);
        });

        iBtnThoatHienThi.setOnClickListener(v -> {
            finish();
        });

        tvMuaNgay.setOnClickListener(v -> {
            BottomSheetDialogPickUpFragment dialog = new BottomSheetDialogPickUpFragment(1, giay, khachHang);
            dialog.show(getSupportFragmentManager(), dialog.getTag());
        });

        tvThemVaoGio.setOnClickListener(v -> {
            BottomSheetDialogPickUpFragment dialog = new BottomSheetDialogPickUpFragment(0, giay, khachHang);
            dialog.show(getSupportFragmentManager(), dialog.getTag());
        });

        layDanhSachGiayCT(giay.getMaGiay());
        layDanhSachBinhLuan(giay.getMaGiay());
    }

    private void layDanhSachGiayCT(String maGiay) {
        FirebaseFirestore.getInstance().collection("GiayChiTiet")
                .whereEqualTo("maGiay", maGiay)
                .whereEqualTo("trangThaiGiayChiTiet", 0)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            GiayChiTiet giayChiTiet = snapshot.toObject(GiayChiTiet.class);
                            giayChiTietList.add(giayChiTiet);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void layDanhSachBinhLuan(String maGiay) {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("BinhLuan");

        reference.whereEqualTo("maGiay", maGiay)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e(TAG, "Đã có lỗi: ", error);
                        return;
                    }

                    if (value != null) {
                        for (DocumentChange change : value.getDocumentChanges()) {
                            switch (change.getType()) {
                                case ADDED:
                                    DocumentSnapshot addSnapshot = change.getDocument();
                                    BinhLuan binhLuan = addSnapshot.toObject(BinhLuan.class);
                                    binhLuanList.add(binhLuan);

                                    Collections.sort(binhLuanList, (o1, o2) -> {
                                        return o1.getTrangThaiBinhLuan() - o2.getTrangThaiBinhLuan();
                                    });
                                    binhLuanAdapter.notifyDataSetChanged();
                                    break;
                                case MODIFIED:
                                    DocumentSnapshot modiSnapshot = change.getDocument();
                                    BinhLuan modiBinhLuan = modiSnapshot.toObject(BinhLuan.class);
                                    for (int i = 0; i < binhLuanList.size(); i++) {
                                        BinhLuan binhLuanIndex = binhLuanList.get(i);
                                        if (binhLuanIndex.getMaBinhLuan().equals(modiBinhLuan.getMaBinhLuan())) {
                                            binhLuanList.set(i, modiBinhLuan);
                                            binhLuanAdapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                                case REMOVED:
                                    String removeID = change.getDocument().getId();
                                    for (int i = 0; i < binhLuanList.size(); i++) {
                                        if (binhLuanList.get(i).getMaBinhLuan().equals(removeID)) {
                                            binhLuanList.remove(i);
                                            binhLuanAdapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                });
    }

}