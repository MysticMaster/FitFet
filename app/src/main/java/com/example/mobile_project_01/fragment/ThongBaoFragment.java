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
import com.example.mobile_project_01.adapter.ThongBaoAdapter;
import com.example.mobile_project_01.model.DonHang;
import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.KhachHang;
import com.example.mobile_project_01.model.ThongBao;
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


public class ThongBaoFragment extends Fragment {

    private List<ThongBao> thongBaoList;
    private String maKhachHang;

    private ThongBaoAdapter adapter;

    private LinearLayout llKhongCoGiThongBao;

    private RecyclerView rvDanhSachThongBao;

    public ThongBaoFragment() {
        // Required empty public constructor
    }

    public ThongBaoFragment(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_thong_bao, container, false);
        rvDanhSachThongBao = view.findViewById(R.id.rvDanhSachThongBao);
        thongBaoList = new ArrayList<>();
        adapter = new ThongBaoAdapter(getContext(), thongBaoList);

        llKhongCoGiThongBao = view.findViewById(R.id.llKhongCoGiThongBao);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvDanhSachThongBao.setLayoutManager(manager);
        rvDanhSachThongBao.setAdapter(adapter);

        layDanhSachThongBao();

        return view;
    }

    private void layDanhSachThongBao() {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("ThongBao");

        reference.whereEqualTo("maKhachHang", maKhachHang)
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
                                    ThongBao addThongBao = addSnapshot.toObject(ThongBao.class);
                                    List<ThongBao> readList = new ArrayList<>();
                                    readList.add(addThongBao);
                                    collectionsSort(readList, thongBaoList);
                                    adapter.notifyDataSetChanged();
                                    break;
                                case MODIFIED:
                                    DocumentSnapshot modiSnapshot = change.getDocument();
                                    ThongBao modiThongBao = modiSnapshot.toObject(ThongBao.class);
                                    for (int i = 0; i < thongBaoList.size(); i++) {
                                        ThongBao thongBaoIndex = thongBaoList.get(i);
                                        if (thongBaoIndex.getMaThongBao().equals(modiThongBao.getMaThongBao())) {
                                            thongBaoList.set(i, modiThongBao);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                                case REMOVED:
                                    String removeID = change.getDocument().getId();
                                    for (int i = 0; i < thongBaoList.size(); i++) {
                                        if (thongBaoList.get(i).getMaThongBao().equals(removeID)) {
                                            thongBaoList.remove(i);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                            }
                        }
                        if (thongBaoList.size() == 0) {
                            llKhongCoGiThongBao.setVisibility(View.VISIBLE);
                            rvDanhSachThongBao.setVisibility(View.GONE);
                        } else {
                            llKhongCoGiThongBao.setVisibility(View.GONE);
                            rvDanhSachThongBao.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void collectionsSort(List<ThongBao> list, List<ThongBao> thongBaoList) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        for (ThongBao thongBao : list) {
            String date = thongBao.getNgayTaoTB();
            String time = thongBao.getGioTaoTB();

            LocalDate localDate = LocalDate.parse(date, dateFormatter);
            LocalTime localTime = LocalTime.parse(time, timeFormatter);

            LocalDateTime dateTime = LocalDateTime.of(localDate, localTime);
            thongBao.setLocalDateTime(dateTime);
            thongBaoList.add(thongBao);
        }
        Collections.sort(thongBaoList, ((o1, o2) -> o2.getLocalDateTime().compareTo(o1.getLocalDateTime())));
    }

}