package com.example.mobile_project_01.fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mobile_project_01.R;
import com.example.mobile_project_01.adapter.QLGiayAdapter;
import com.example.mobile_project_01.adapter.QLGiayChiTietAdapter;
import com.example.mobile_project_01.adapter.QLThuongHieuAdapter;
import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.GiayChiTiet;
import com.example.mobile_project_01.model.ThuongHieu;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class NgungKinhDoanhFragment extends Fragment {

    private String maGiay;
    private int status;

    public NgungKinhDoanhFragment() {
        // Required empty public constructor
    }

    public NgungKinhDoanhFragment(int status) {
        this.status = status;
    }

    public NgungKinhDoanhFragment(String maGiay, int status) {
        this.maGiay = maGiay;
        this.status = status;
    }

    private LinearLayout llDanhSachNKD;
    private LinearLayout llKhongCoGiNgungKinhDoanh;
    private List<Giay> giayList;
    private List<Giay> saveList;
    private List<Giay> searchList;
    private QLGiayAdapter qlGiayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ngung_kinh_doanh, container, false);

        RecyclerView rvQLDSNgungKinhDoanh = view.findViewById(R.id.rvQLDSNgungKinhDoanh);
        llDanhSachNKD = view.findViewById(R.id.llDanhSachNKD);
        llKhongCoGiNgungKinhDoanh = view.findViewById(R.id.llKhongCoGiNgungKinhDoanh);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvQLDSNgungKinhDoanh.setLayoutManager(manager);

        List<ThuongHieu> thuongHieuList = new ArrayList<>();
        QLThuongHieuAdapter qlThuongHieuAdapter = new QLThuongHieuAdapter(getContext(), thuongHieuList, getChildFragmentManager());
//-------------------------------------------------------------------------------------------------------
        LinearLayout llQLTimKiemGiayNKD = view.findViewById(R.id.llQLTimKiemGiayNKD);

        giayList = new ArrayList<>();
        saveList = new ArrayList<>();
        searchList = new ArrayList<>();

        qlGiayAdapter = new QLGiayAdapter(getContext(), giayList, getChildFragmentManager());

        EditText edtTimKiemQLGiayNKD = view.findViewById(R.id.edtTimKiemQLGiayNKD);

        edtTimKiemQLGiayNKD.addTextChangedListener(new TextWatcher() {
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

        edtTimKiemQLGiayNKD.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                edtTimKiemQLGiayNKD.setCursorVisible(true);
            }
        });
//---------------------------------------------------------------------------------------------------------
        List<GiayChiTiet> giayChiTietList = new ArrayList<>();
        QLGiayChiTietAdapter qlGiayChiTietAdapter = new QLGiayChiTietAdapter(getContext(), giayChiTietList, getChildFragmentManager());

        if (status == 0) {
            rvQLDSNgungKinhDoanh.setAdapter(qlThuongHieuAdapter);
            layDSThuongHieuNgungKinhDoanh(qlThuongHieuAdapter, thuongHieuList);
        } else if (status == 1) {
            llQLTimKiemGiayNKD.setVisibility(View.VISIBLE);

            rvQLDSNgungKinhDoanh.setAdapter(qlGiayAdapter);
            layDSGiayNgungKinhDoanh();
        } else if (status == 2) {
            rvQLDSNgungKinhDoanh.setAdapter(qlGiayChiTietAdapter);
            layDSGiayCTNgungKinhDoanh(maGiay, qlGiayChiTietAdapter, giayChiTietList);
        }

        return view;
    }

    private void layDSThuongHieuNgungKinhDoanh(QLThuongHieuAdapter adapter, List<ThuongHieu> thuongHieuList) {
        FirebaseFirestore.getInstance().collection("ThuongHieu")
                .whereEqualTo("trangThaiThuongHieu", 1)
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
                                    ThuongHieu addThuongHieu = addSnapshot.toObject(ThuongHieu.class);
                                    thuongHieuList.add(addThuongHieu);
                                    adapter.notifyDataSetChanged();
                                    break;
                                case MODIFIED:
                                    DocumentSnapshot modiSnapshot = change.getDocument();
                                    ThuongHieu modiThuongHieu = modiSnapshot.toObject(ThuongHieu.class);
                                    for (int i = 0; i < thuongHieuList.size(); i++) {
                                        ThuongHieu thuongHieuIndex = thuongHieuList.get(i);
                                        if (thuongHieuIndex.getMaThuongHieu().equals(modiThuongHieu.getMaThuongHieu())) {
                                            thuongHieuList.set(i, modiThuongHieu);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                                case REMOVED:
                                    String removeID = change.getDocument().getId();
                                    for (int i = 0; i < thuongHieuList.size(); i++) {
                                        if (thuongHieuList.get(i).getMaThuongHieu().equals(removeID)) {
                                            thuongHieuList.remove(i);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                            }
                        }
                        if (thuongHieuList.size() == 0) {
                            llKhongCoGiNgungKinhDoanh.setVisibility(View.VISIBLE);
                            llDanhSachNKD.setVisibility(View.GONE);
                        } else {
                            llKhongCoGiNgungKinhDoanh.setVisibility(View.GONE);
                            llDanhSachNKD.setVisibility(View.VISIBLE);
                        }
                    }
                }));
    }

    private void layDSGiayNgungKinhDoanh() {
        giayList.clear();
        saveList.clear();

        FirebaseFirestore.getInstance().collection("Giay")
                .whereEqualTo("trangThaiGiay", 1)
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
                                    Giay addGiay = addSnapshot.toObject(Giay.class);
                                    giayList.add(addGiay);
                                    saveList.add(addGiay);
                                    qlGiayAdapter.notifyDataSetChanged();
                                    break;
                                case MODIFIED:
                                    DocumentSnapshot modiSnapshot = change.getDocument();
                                    Giay modiGiay = modiSnapshot.toObject(Giay.class);
                                    for (int i = 0; i < giayList.size(); i++) {
                                        Giay giayIndex = giayList.get(i);
                                        if (giayIndex.getMaGiay().equals(modiGiay.getMaGiay())) {
                                            giayList.set(i, modiGiay);
                                            saveList.add(i, modiGiay);
                                            qlGiayAdapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                                case REMOVED:
                                    String removeID = change.getDocument().getId();
                                    for (int i = 0; i < giayList.size(); i++) {
                                        if (giayList.get(i).getMaGiay().equals(removeID)) {
                                            giayList.remove(i);
                                            saveList.remove(i);
                                            qlGiayAdapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                            }
                        }
                        if (giayList.size() == 0) {
                            llKhongCoGiNgungKinhDoanh.setVisibility(View.VISIBLE);
                            llDanhSachNKD.setVisibility(View.GONE);
                        } else {
                            llKhongCoGiNgungKinhDoanh.setVisibility(View.GONE);
                            llDanhSachNKD.setVisibility(View.VISIBLE);
                        }
                    }
                }));
    }

    private void layDSGiayCTNgungKinhDoanh(String maGiay, QLGiayChiTietAdapter adapter, List<GiayChiTiet> giayChiTietList) {
        FirebaseFirestore.getInstance().collection("GiayChiTiet")
                .whereEqualTo("maGiay", maGiay)
                .whereEqualTo("trangThaiGiayChiTiet", 1)
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
                                    GiayChiTiet addGiayChiTiet = addSnapshot.toObject(GiayChiTiet.class);
                                    giayChiTietList.add(addGiayChiTiet);
                                    adapter.notifyDataSetChanged();
                                    break;
                                case MODIFIED:
                                    DocumentSnapshot modiSnapshot = change.getDocument();
                                    GiayChiTiet modiGiayChiTiet = modiSnapshot.toObject(GiayChiTiet.class);
                                    for (int i = 0; i < giayChiTietList.size(); i++) {
                                        GiayChiTiet giayChiTietIndex = giayChiTietList.get(i);
                                        if (giayChiTietIndex.getMaGiayChiTiet().equals(modiGiayChiTiet.getMaGiayChiTiet())) {
                                            giayChiTietList.set(i, modiGiayChiTiet);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                                case REMOVED:
                                    String removeID = change.getDocument().getId();
                                    for (int i = 0; i < giayChiTietList.size(); i++) {
                                        if (giayChiTietList.get(i).getMaGiayChiTiet().equals(removeID)) {
                                            giayChiTietList.remove(i);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                            }
                        }
                        if (giayChiTietList.size() == 0) {
                            llKhongCoGiNgungKinhDoanh.setVisibility(View.VISIBLE);
                            llDanhSachNKD.setVisibility(View.GONE);
                        } else {
                            llKhongCoGiNgungKinhDoanh.setVisibility(View.GONE);
                            llDanhSachNKD.setVisibility(View.VISIBLE);
                        }
                    }
                }));
    }


    private void fillQuery(String query) {
        searchList.clear();
        for (Giay item : giayList) {
            if (containsIgnoreCaseAndAccent(item.getTenGiay(), query)) {
                searchList.add(item);
            }
        }
        qlGiayAdapter.fillSearch(searchList);
    }

    private void exitSeacrh() {
        giayList.clear();
        giayList.addAll(saveList);
        qlGiayAdapter.notifyDataSetChanged();
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