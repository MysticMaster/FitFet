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
import com.example.mobile_project_01.adapter.QLBinhLuanAdapter;
import com.example.mobile_project_01.model.BinhLuan;
import com.example.mobile_project_01.model.DonHang;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class BinhLuanTichCucFragment extends Fragment {

    private String maGiay;

    public BinhLuanTichCucFragment() {
        // Required empty public constructor
    }

    public BinhLuanTichCucFragment(String maGiay) {
        this.maGiay = maGiay;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_binh_luan_tich_cuc, container, false);

        List<BinhLuan> binhLuanList = new ArrayList<>();
        QLBinhLuanAdapter adapter = new QLBinhLuanAdapter(getContext(), binhLuanList, getChildFragmentManager());

        RecyclerView rvQLDSBinhLuanTichCuc = view.findViewById(R.id.rvQLDSBinhLuanTichCuc);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvQLDSBinhLuanTichCuc.setLayoutManager(manager);
        rvQLDSBinhLuanTichCuc.setAdapter(adapter);

        LinearLayout llKhongCoBLTichCuc = view.findViewById(R.id.llKhongCoBLTichCuc);

        CollectionReference reference = FirebaseFirestore.getInstance().collection("BinhLuan");

        reference.whereEqualTo("maGiay", maGiay)
                .whereEqualTo("trangThaiBinhLuan", 0)
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
                                    BinhLuan binhLuan = addSnapshot.toObject(BinhLuan.class);
                                    binhLuanList.add(binhLuan);
                                    adapter.notifyDataSetChanged();
                                    break;
                                case MODIFIED:
                                    DocumentSnapshot modiSnapshot = change.getDocument();
                                    BinhLuan modiBinhLuan = modiSnapshot.toObject(BinhLuan.class);
                                    for (int i = 0; i < binhLuanList.size(); i++) {
                                        BinhLuan binhLuanIndex = binhLuanList.get(i);
                                        if (binhLuanIndex.getMaBinhLuan().equals(modiBinhLuan.getMaBinhLuan())) {
                                            binhLuanList.set(i, modiBinhLuan);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                                case REMOVED:
                                    String removeID = change.getDocument().getId();
                                    for (int i = 0; i < binhLuanList.size(); i++) {
                                        if (binhLuanList.get(i).getMaBinhLuan().equals(removeID)) {
                                            binhLuanList.remove(i);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                            }
                        }
                        if (binhLuanList.size() == 0) {
                            llKhongCoBLTichCuc.setVisibility(View.VISIBLE);
                            rvQLDSBinhLuanTichCuc.setVisibility(View.GONE);
                        } else {
                            llKhongCoBLTichCuc.setVisibility(View.GONE);
                            rvQLDSBinhLuanTichCuc.setVisibility(View.VISIBLE);
                        }
                    }
                }));

        return view;
    }
}