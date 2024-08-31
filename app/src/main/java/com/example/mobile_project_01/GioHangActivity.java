package com.example.mobile_project_01;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mobile_project_01.adapter.GioHangAdapter;
import com.example.mobile_project_01.function.AutoStringID;
import com.example.mobile_project_01.model.DonHang;
import com.example.mobile_project_01.model.DonHangChiTiet;
import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.GioHang;
import com.example.mobile_project_01.model.KhachHang;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GioHangActivity extends AppCompatActivity {

    private KhachHang khachHang;
    private List<GioHang> gioHangList;

    private GioHangAdapter adapter;

    private LinearLayout llKhongCoGiGioHang;
    private RecyclerView rvDanhSachGioHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            khachHang = (KhachHang) bundle.getSerializable("khachHang");
        }

        ImageButton iBtnThoatGioHang = findViewById(R.id.iBtnThoatGioHang);
        rvDanhSachGioHang = findViewById(R.id.rvDanhSachGioHang);
        llKhongCoGiGioHang = findViewById(R.id.llKhongCoGiGioHang);
        TextView tvMuaHang = findViewById(R.id.tvMuaHang);
        ProgressBar pbThanhToan = findViewById(R.id.pbThanhToan);
        TextView tvTongThanhToanGioHang = findViewById(R.id.tvTongThanhToanGioHang);

        gioHangList = new ArrayList<>();
        adapter = new GioHangAdapter(this, gioHangList, tvMuaHang, tvTongThanhToanGioHang,
                getSupportFragmentManager());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvDanhSachGioHang.setLayoutManager(manager);
        rvDanhSachGioHang.setAdapter(adapter);

        List<GioHang> getGioHang = adapter.getSelectedItems();

        tvMuaHang.setEnabled(false);
        tvMuaHang.setAlpha(0.3f);

        tvMuaHang.setOnClickListener(v -> {
            tvMuaHang.setVisibility(View.INVISIBLE);
            pbThanhToan.setVisibility(View.VISIBLE);

            List<DonHangChiTiet> donHangChiTietList = new ArrayList<>();

            for (GioHang gioHang : getGioHang) {
                String maDonHangCT = AutoStringID.createStringID("DHCT-");
                String maDonHang = "0";

                DonHangChiTiet donHangChiTiet = new DonHangChiTiet(maDonHangCT,
                        maDonHang, gioHang.getMaGiayChiTiet(),
                        gioHang.getKichCoMua(), gioHang.getSoLuongMua(), gioHang.getTongTien());
                donHangChiTietList.add(donHangChiTiet);
            }

            Intent intent = new Intent(this, ThanhToanActivity.class);
            Bundle send = new Bundle();
            send.putSerializable("donHangChiTietList", (Serializable) donHangChiTietList);
            send.putSerializable("getGioHang", (Serializable) getGioHang);
            send.putSerializable("khachHang", khachHang);
            intent.putExtras(send);
            startActivity(intent);
            finish();
        });


        iBtnThoatGioHang.setOnClickListener(v -> {
            finish();
        });

        layDanhSachGioHang();
    }

    private void layDanhSachGioHang() {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("GioHang");

        reference.whereEqualTo("maKhachHang", khachHang.getMaKhachHang())
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
                                    GioHang addGioHang = addSnapshot.toObject(GioHang.class);
                                    gioHangList.add(addGioHang);
                                    adapter.notifyDataSetChanged();
                                    break;
                                case MODIFIED:
                                    DocumentSnapshot modiSnapshot = change.getDocument();
                                    GioHang modiGioHang = modiSnapshot.toObject(GioHang.class);
                                    for (int i = 0; i < gioHangList.size(); i++) {
                                        GioHang gioHangIndex = gioHangList.get(i);
                                        if (gioHangIndex.getMaGioHang().equals(modiGioHang.getMaGioHang())) {
                                            gioHangList.set(i, modiGioHang);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                                case REMOVED:
                                    String removeID = change.getDocument().getId();
                                    for (int i = 0; i < gioHangList.size(); i++) {
                                        if (gioHangList.get(i).getMaGioHang().equals(removeID)) {
                                            gioHangList.remove(i);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                            }
                        }
                        if (gioHangList.size() == 0) {
                            llKhongCoGiGioHang.setVisibility(View.VISIBLE);
                            rvDanhSachGioHang.setVisibility(View.GONE);
                        } else {
                            llKhongCoGiGioHang.setVisibility(View.GONE);
                            rvDanhSachGioHang.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

}