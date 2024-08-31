package com.example.mobile_project_01;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.mobile_project_01.adapter.QLTinNhanAdapter;
import com.example.mobile_project_01.adapter.TinNhanCTAdapter;
import com.example.mobile_project_01.model.ThanhVien;
import com.example.mobile_project_01.model.TinNhan;
import com.example.mobile_project_01.model.TinNhanCT;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class QLDanhSachTinNhanKHActivity extends AppCompatActivity {

    private List<TinNhan> tinNhanList;
    private QLTinNhanAdapter adapter;
    private ThanhVien thanhVien;

    private LinearLayout llKhongCoTinNhan;

    private RecyclerView rvDSTinNhanKH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qldanh_sach_tin_nhan_khactivity);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            thanhVien = (ThanhVien) bundle.getSerializable("thanhVien");
        }

        ImageButton iBtnThoatQLDSTinNhanKH = findViewById(R.id.iBtnThoatQLDSTinNhanKH);
        rvDSTinNhanKH = findViewById(R.id.rvDSTinNhanKH);
        llKhongCoTinNhan = findViewById(R.id.llKhongCoTinNhan);

        tinNhanList = new ArrayList<>();
        adapter = new QLTinNhanAdapter(this, tinNhanList, thanhVien);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvDSTinNhanKH.setLayoutManager(manager);
        rvDSTinNhanKH.setAdapter(adapter);

        layDanhSachTinNhan();

        iBtnThoatQLDSTinNhanKH.setOnClickListener(v -> {
            finish();
        });
    }

    private void layDanhSachTinNhan() {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("TinNhan");

        reference.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e(TAG, "Đã có lỗi: ", error);
                return;
            }
            if (value != null) {
                for (DocumentChange change : value.getDocumentChanges()) {
                    switch (change.getType()) {
                        case ADDED:
                            DocumentSnapshot addSnapshot = change.getDocument();
                            TinNhan addTinNhan = addSnapshot.toObject(TinNhan.class);
                            tinNhanList.add(addTinNhan);
                            adapter.notifyDataSetChanged();
                            break;
                        case REMOVED:
                            String removeID = change.getDocument().getId();
                            for (int i = 0; i < tinNhanList.size(); i++) {
                                if (tinNhanList.get(i).getMaTinNhan().equals(removeID)) {
                                    tinNhanList.remove(i);
                                    adapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                            break;
                    }
                }
                if (tinNhanList.size() == 0) {
                    llKhongCoTinNhan.setVisibility(View.VISIBLE);
                    rvDSTinNhanKH.setVisibility(View.GONE);
                } else {
                    llKhongCoTinNhan.setVisibility(View.GONE);
                    rvDSTinNhanKH.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}