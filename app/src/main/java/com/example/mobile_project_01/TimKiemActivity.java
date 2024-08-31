package com.example.mobile_project_01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.mobile_project_01.adapter.ThuongHieuAdapter;
import com.example.mobile_project_01.adapter.TimKiemGiayAdapter;
import com.example.mobile_project_01.function.SpacesItemDecoration;
import com.example.mobile_project_01.function.UtilityClass;
import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.KhachHang;
import com.example.mobile_project_01.model.ThuongHieu;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class TimKiemActivity extends AppCompatActivity {

    private List<Giay> giayList;
    private RecyclerView rvDSGiayTimKiem;
    private LinearLayout llKhongCoSPTimKiem;
    private List<Giay> saveList;
    private List<Giay> searchList;
    private TimKiemGiayAdapter timKiemGiayAdapter;
    private List<ThuongHieu> thuongHieuList;
    private ThuongHieuAdapter thuongHieuAdapter;
    private KhachHang khachHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_kiem);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            khachHang = (KhachHang) bundle.getSerializable("khachHang");
        }

        giayList = new ArrayList<>();
        llKhongCoSPTimKiem = findViewById(R.id.llKhongCoSPTimKiem);

        saveList = new ArrayList<>();
        searchList = new ArrayList<>();

        timKiemGiayAdapter = new TimKiemGiayAdapter(this, giayList, khachHang);

        thuongHieuList = new ArrayList<>();
        thuongHieuAdapter = new ThuongHieuAdapter(this, thuongHieuList);

        ImageButton iBtnThoatTimKiem = findViewById(R.id.iBtnThoatTimKiem);
        EditText edtTimKiem = findViewById(R.id.edtTimKiem);
        ImageButton iBtnLocTimKiem = findViewById(R.id.iBtnLocTimKiem);

        edtTimKiem.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                edtTimKiem.setCursorVisible(true);
            }
        });

        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    exitSeacrh();
                } else {
                    fillQuery(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rvDSGiayTimKiem = findViewById(R.id.rvDSGiayTimKiem);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rvDSGiayTimKiem.setLayoutManager(manager);
        rvDSGiayTimKiem.setAdapter(timKiemGiayAdapter);

        RecyclerView rvLocDSThuongHieu = findViewById(R.id.rvLocDSThuongHieu);
        LinearLayoutManager thuongHieuManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvLocDSThuongHieu.setLayoutManager(thuongHieuManager);
        rvLocDSThuongHieu.setAdapter(thuongHieuAdapter);

        thuongHieuAdapter.setOnItemClickListener(thuongHieu -> {
            layDanhSachGiayTheoThuongHieu(thuongHieu.getMaThuongHieu());
        });

        iBtnLocTimKiem.setOnClickListener(v -> {
            if (rvLocDSThuongHieu.getVisibility() == View.GONE) {
                rvLocDSThuongHieu.setVisibility(View.VISIBLE);
                iBtnLocTimKiem.setImageResource(R.drawable.ic_clear_x_black);
            } else if (rvLocDSThuongHieu.getVisibility() == View.VISIBLE) {
                rvLocDSThuongHieu.setVisibility(View.GONE);
                iBtnLocTimKiem.setImageResource(R.drawable.ic_filter);
                exitSeacrh();
            }
        });

        iBtnThoatTimKiem.setOnClickListener(v -> {
            finish();
        });

        layDanhSachAllGiay();

        layDanhSachThuongHieu();
    }

    private void layDanhSachAllGiay() {
        giayList.clear();
        saveList.clear();
        FirebaseFirestore.getInstance().collection("Giay")
                .whereEqualTo("trangThaiGiay", 0)
                .get()
                .addOnCompleteListener(task -> {
                    for (DocumentSnapshot snapshot : task.getResult()) {
                        Giay giay = snapshot.toObject(Giay.class);
                        giayList.add(giay);
                        saveList.add(giay);
                    }
                    timKiemGiayAdapter.notifyDataSetChanged();
                });
    }

    private void layDanhSachGiayTheoThuongHieu(String maThuongHieu) {
        giayList.clear();
        FirebaseFirestore.getInstance().collection("Giay")
                .whereEqualTo("maThuongHieu", maThuongHieu)
                .whereEqualTo("trangThaiGiay", 0)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot snapshot : task.getResult()) {
                            Giay giay = snapshot.toObject(Giay.class);
                            giayList.add(giay);
                        }
                        timKiemGiayAdapter.notifyDataSetChanged();

                        if (giayList.size() == 0) {
                            rvDSGiayTimKiem.setVisibility(View.GONE);
                            llKhongCoSPTimKiem.setVisibility(View.VISIBLE);
                        } else {
                            rvDSGiayTimKiem.setVisibility(View.VISIBLE);
                            llKhongCoSPTimKiem.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void layDanhSachThuongHieu() {
        FirebaseFirestore.getInstance().collection("ThuongHieu")
                .whereEqualTo("trangThaiThuongHieu", 0)
                .get()
                .addOnCompleteListener(task -> {
                    for (DocumentSnapshot snapshot : task.getResult()) {
                        ThuongHieu thuongHieu = snapshot.toObject(ThuongHieu.class);
                        thuongHieuList.add(thuongHieu);
                    }
                    thuongHieuAdapter.notifyDataSetChanged();

                    if (giayList.size() == 0) {
                        rvDSGiayTimKiem.setVisibility(View.GONE);
                        llKhongCoSPTimKiem.setVisibility(View.VISIBLE);
                    } else {
                        rvDSGiayTimKiem.setVisibility(View.VISIBLE);
                        llKhongCoSPTimKiem.setVisibility(View.GONE);
                    }
                });
    }

    private void fillQuery(String query) {
        searchList.clear();
        for (Giay item : giayList) {
            if (containsIgnoreCaseAndAccent(item.getTenGiay(), query)) {
                searchList.add(item);
            }
        }
        if (searchList.size() == 0) {
            rvDSGiayTimKiem.setVisibility(View.GONE);
            llKhongCoSPTimKiem.setVisibility(View.VISIBLE);
        } else {
            timKiemGiayAdapter.fillSearch(searchList);
            rvDSGiayTimKiem.setVisibility(View.VISIBLE);
            llKhongCoSPTimKiem.setVisibility(View.GONE);
        }

    }

    private void exitSeacrh() {
        giayList.clear();
        giayList.addAll(saveList);
        timKiemGiayAdapter.notifyDataSetChanged();
        rvDSGiayTimKiem.setVisibility(View.VISIBLE);
        llKhongCoSPTimKiem.setVisibility(View.GONE);
    }

    private boolean containsIgnoreCaseAndAccent(String original, String query) {
        String normalizedOriginal = Normalizer.normalize(original, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();

        String normalizedQuery = Normalizer.normalize(query, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();

        return normalizedOriginal.contains(normalizedQuery);
    }
}