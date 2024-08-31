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

import java.util.ArrayList;
import java.util.List;


public class KHDaHuyDHFragment extends Fragment {

    private String maKhachHang;

    public KHDaHuyDHFragment() {
        // Required empty public constructor
    }

    public KHDaHuyDHFragment(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_k_h_da_huy_d_h, container, false);

        List<DonHang> donHangList = new ArrayList<>();
        KHDonHangAdapter adapter = new KHDonHangAdapter(getContext(), donHangList);

        RecyclerView rvDanhSachDonHangDaHuy = view.findViewById(R.id.rvDanhSachDonHangDaHuy);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvDanhSachDonHangDaHuy.setLayoutManager(manager);
        rvDanhSachDonHangDaHuy.setAdapter(adapter);

        LinearLayout llKhongCoGiKHDaHuy = view.findViewById(R.id.llKhongCoGiKHDaHuy);

        CollectionReference reference = FirebaseFirestore.getInstance().collection("DonHang");

        reference.whereEqualTo("maKhachHang", maKhachHang)
                .whereEqualTo("trangThaiDonHang", 5)
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
                                    donHangList.add(donHang);
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
                            llKhongCoGiKHDaHuy.setVisibility(View.VISIBLE);
                            rvDanhSachDonHangDaHuy.setVisibility(View.GONE);
                        } else {
                            llKhongCoGiKHDaHuy.setVisibility(View.GONE);
                            rvDanhSachDonHangDaHuy.setVisibility(View.VISIBLE);
                        }
                    }
                });

        return view;
    }
}