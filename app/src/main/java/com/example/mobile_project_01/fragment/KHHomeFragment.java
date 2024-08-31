package com.example.mobile_project_01.fragment;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobile_project_01.GioHangActivity;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.TimKiemActivity;
import com.example.mobile_project_01.adapter.KHGiayAdapter;
import com.example.mobile_project_01.adapter.Top10Adapter;
import com.example.mobile_project_01.adapter.ViewPager2SlideShowAdapter;
import com.example.mobile_project_01.function.FormatCurrency;
import com.example.mobile_project_01.function.SpacesItemDecoration;
import com.example.mobile_project_01.function.UtilityClass;
import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.GiayChiTiet;
import com.example.mobile_project_01.model.KhachHang;
import com.example.mobile_project_01.model.ThongKe;
import com.example.mobile_project_01.model.Top;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class KHHomeFragment extends Fragment {
    private RecyclerView rvDanhSachGiay;
    private List<Giay> giayList;
    private KHGiayAdapter adapter;
    private RecyclerView rvDanhSachTop;
    private List<GiayChiTiet> top10List;
    private Top10Adapter top10Adapter;
    private KhachHang khachHang;

    public KHHomeFragment() {
    }

    public KHHomeFragment(KhachHang khachHang) {
        // Required empty public constructor
        this.khachHang = khachHang;
    }

    private Handler handler;
    private int currentPosition = 0;
    private List<String> itemList;
    private TextView tvGoiY;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kh_home, container, false);

        ViewPager2 vp2SlideShow = view.findViewById(R.id.vp2SlideShow);
        int[] arrImages = {R.drawable.slide_1, R.drawable.slide_2, R.drawable.slide_3};
        ViewPager2SlideShowAdapter viewPager2SlideShowAdapter = new ViewPager2SlideShowAdapter(getContext(), arrImages);
        vp2SlideShow.setAdapter(viewPager2SlideShowAdapter);

        vp2SlideShow.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        vp2SlideShow.setOffscreenPageLimit(1);
        vp2SlideShow.setCurrentItem(0);
        vp2SlideShow.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                vp2SlideShow.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        vp2SlideShow.setCurrentItem((position + 1) % arrImages.length, true);
                    }
                }, 3000);
            }
        });


        rvDanhSachGiay = view.findViewById(R.id.rvDanhSachGiay);
        giayList = new ArrayList<>();
        adapter = new KHGiayAdapter(getContext(), giayList, khachHang);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        rvDanhSachGiay.setLayoutManager(manager);
        rvDanhSachGiay.setAdapter(adapter);

        rvDanhSachTop = view.findViewById(R.id.rvDanhSachTop);
        top10List = new ArrayList<>();
        top10Adapter = new Top10Adapter(getContext(), top10List, khachHang);
        LinearLayoutManager top10Manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvDanhSachTop.setLayoutManager(top10Manager);
        rvDanhSachTop.setAdapter(top10Adapter);

        ImageView iBtnGioHangHome = view.findViewById(R.id.iBtnGioHangHome);
        iBtnGioHangHome.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), GioHangActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("khachHang", khachHang);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        });

        CardView cvTienHanhTimKiem = view.findViewById(R.id.cvTienHanhTimKiem);
        cvTienHanhTimKiem.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TimKiemActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("khachHang", khachHang);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        });

        layDanhSachGiay();

        tvGoiY = view.findViewById(R.id.tvGoiY);
        itemList = new ArrayList<>();
        handler = new Handler();

        FirebaseFirestore.getInstance().collection("ThuongHieu")
                .whereEqualTo("trangThaiThuongHieu", 0)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot snapshot : task.getResult()) {
                            itemList.add(snapshot.getString("tenThuongHieu"));
                        }
                        goiYTuDong();
                    }
                });

        top10();

        return view;
    }

    private void goiYTuDong() {
        if (itemList.isEmpty()) {
            return;
        }

        tvGoiY.setText(itemList.get(currentPosition));
        currentPosition++;

        if (currentPosition >= itemList.size()) {
            currentPosition = 0;
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goiYTuDong();
            }
        }, 5000);
    }


    private void layDanhSachGiay() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.item_loading_dialog, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00FFFFFF")));

        CollectionReference reference = FirebaseFirestore.getInstance().collection("Giay");

        reference.whereEqualTo("trangThaiGiay", 0)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e(TAG, "Đã có lỗi: ", error);
                        return;
                    }
                    if (value != null) {
                        dialog.show();
                        for (DocumentChange change : value.getDocumentChanges()) {
                            switch (change.getType()) {
                                case ADDED:
                                    DocumentSnapshot addSnapshot = change.getDocument();
                                    Giay addGiay = addSnapshot.toObject(Giay.class);
                                    giayList.add(addGiay);
                                    adapter.notifyDataSetChanged();
                                    break;
                                case MODIFIED:
                                    DocumentSnapshot modiSnapshot = change.getDocument();
                                    Giay modiGiay = modiSnapshot.toObject(Giay.class);
                                    for (int i = 0; i < giayList.size(); i++) {
                                        Giay giayIndex = giayList.get(i);
                                        if (giayIndex.getMaGiay().equals(modiGiay.getMaGiay())) {
                                            giayList.set(i, modiGiay);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                                case REMOVED:
                                    String removeID = change.getDocument().getId();
                                    for (int i = 0; i < giayList.size(); i++) {
                                        if (giayList.get(i).getMaGiay().equals(removeID)) {
                                            giayList.remove(i);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                            }
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 500);
                    }
                });
    }

    private void top10() {
        FirebaseFirestore.getInstance().collection("ThongKe")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }
                    List<ThongKe> thongKeList = new ArrayList<>();
                    if (value != null) {
                        for (DocumentChange change : value.getDocumentChanges()) {
                            switch (change.getType()) {
                                case ADDED:
                                    DocumentSnapshot addSnapshot = change.getDocument();
                                    thongKeList.add(addSnapshot.toObject(ThongKe.class));
                                    break;
                            }
                        }
                        layDanhSachTop10(thongKeList);
                    }
                });
    }

    private void layDanhSachTop10(List<ThongKe> thongKeList) {


        FirebaseFirestore.getInstance().collection("GiayChiTiet")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<GiayChiTiet> readList = new ArrayList<>();
                        List<Top> topList = new ArrayList<>();
                        for (DocumentSnapshot snapshot : task.getResult()) {
                            readList.add(snapshot.toObject(GiayChiTiet.class));
                        }

                        for (GiayChiTiet giayChiTiet : readList) {
                            int count = 0;
                            for (ThongKe thongKe : thongKeList) {
                                if (thongKe.getMaGiayChiTiet().equals(giayChiTiet.getMaGiayChiTiet())) {
                                    count += thongKe.getSoLuongDaBan();
                                }
                            }
                            topList.add(new Top(giayChiTiet.getMaGiayChiTiet(), count));
                        }

                        if (!topList.isEmpty()) {
                            Collections.sort(topList, (o1, o2) -> o2.getCount() - o1.getCount());

                            for (int i = 0; i < 10; i++) {
                                top10List.clear();
                                FirebaseFirestore.getInstance().collection("GiayChiTiet")
                                        .whereEqualTo("maGiayChiTiet", topList.get(i).getMaGiayCT())
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                for (DocumentSnapshot snapshot : task1.getResult()) {
                                                    top10List.add(snapshot.toObject(GiayChiTiet.class));
                                                }
                                                top10Adapter.notifyDataSetChanged();
                                            }
                                        });
                            }

                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

}