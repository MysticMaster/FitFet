package com.example.mobile_project_01.fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mobile_project_01.R;
import com.example.mobile_project_01.adapter.KHDonHangAdapter;
import com.example.mobile_project_01.model.DonHang;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class KHDaGiaoDHFragment extends Fragment {

    private String maKhachHang;

    public KHDaGiaoDHFragment() {
        // Required empty public constructor
    }

    public KHDaGiaoDHFragment(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_k_h_da_giao_d_h, container, false);

        List<DonHang> donHangList = new ArrayList<>();
        KHDonHangAdapter adapter = new KHDonHangAdapter(getContext(), donHangList);

        RecyclerView rvDanhSachDonHangDaGiao = view.findViewById(R.id.rvDanhSachDonHangDaGiao);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvDanhSachDonHangDaGiao.setLayoutManager(manager);
        rvDanhSachDonHangDaGiao.setAdapter(adapter);

        LinearLayout llKhongCoGiKHDaGiao = view.findViewById(R.id.llKhongCoGiKHDaGiao);

        CollectionReference reference = FirebaseFirestore.getInstance().collection("DonHang");

        reference.whereEqualTo("maKhachHang", maKhachHang)
                .whereEqualTo("trangThaiDonHang", 3)
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
                                    DonHang donHang = addSnapshot.toObject(DonHang.class);
                                    List<DonHang> readList = new ArrayList<>();
                                    readList.add(donHang);
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
                            llKhongCoGiKHDaGiao.setVisibility(View.VISIBLE);
                            rvDanhSachDonHangDaGiao.setVisibility(View.GONE);
                        } else {
                            llKhongCoGiKHDaGiao.setVisibility(View.GONE);
                            rvDanhSachDonHangDaGiao.setVisibility(View.VISIBLE);
                        }
                    }
                });

        return view;
    }

    private void collectionsSort(List<DonHang> list, List<DonHang> donHangList) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        for (DonHang donHang : list) {
            String date = donHang.getNgayGiaoKH();
            String time = donHang.getGioGiaoKH();

            LocalDate localDate = LocalDate.parse(date, dateFormatter);
            LocalTime localTime = LocalTime.parse(time, timeFormatter);

            LocalDateTime dateTime = LocalDateTime.of(localDate, localTime);
            donHang.setLocalDateTime(dateTime);
            donHangList.add(donHang);
        }
        Collections.sort(donHangList, ((o1, o2) -> o2.getLocalDateTime().compareTo(o1.getLocalDateTime())));
    }
}