package com.example.mobile_project_01.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mobile_project_01.R;
import com.example.mobile_project_01.adapter.LichSuBanAdapter;
import com.example.mobile_project_01.model.DonHang;
import com.example.mobile_project_01.model.ThongKe;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class LichSuBanHangFragment extends Fragment {

    public LichSuBanHangFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lich_su_ban_hang, container, false);

        LinearLayout llKhongCoLichSu = view.findViewById(R.id.llKhongCoLichSu);
        RecyclerView rvQLDSLichSu = view.findViewById(R.id.rvQLDSLichSu);
        List<ThongKe> thongKeList = new ArrayList<>();
        LichSuBanAdapter adapter = new LichSuBanAdapter(getContext(), thongKeList);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvQLDSLichSu.setLayoutManager(manager);
        rvQLDSLichSu.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection("ThongKe")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<ThongKe> readList = new ArrayList<>();
                        for (DocumentSnapshot snapshot : task.getResult()) {
                            readList.add(snapshot.toObject(ThongKe.class));
                        }
                        collectionsSort(readList, thongKeList);
                        adapter.notifyDataSetChanged();
                        if (thongKeList.size() == 0) {
                            llKhongCoLichSu.setVisibility(View.VISIBLE);
                            rvQLDSLichSu.setVisibility(View.GONE);
                        } else {
                            llKhongCoLichSu.setVisibility(View.GONE);
                            rvQLDSLichSu.setVisibility(View.VISIBLE);
                        }
                    }
                });

        return view;
    }

    private void collectionsSort(List<ThongKe> list, List<ThongKe> thongKeList) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        for (ThongKe thongKe : list) {
            String date = thongKe.getNgay();

            LocalDate localDate = LocalDate.parse(date, dateFormatter);

            thongKe.setLocalDate(localDate);
            thongKeList.add(thongKe);
        }
        Collections.sort(thongKeList, ((o1, o2) -> o2.getLocalDate().compareTo(o1.getLocalDate())));
    }

}