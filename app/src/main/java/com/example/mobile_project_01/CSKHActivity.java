package com.example.mobile_project_01;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobile_project_01.DAO.TinNhanCTDAO;
import com.example.mobile_project_01.adapter.TinNhanCTAdapter;
import com.example.mobile_project_01.model.TinNhanCT;
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
import java.util.UUID;

public class CSKHActivity extends AppCompatActivity {

    private List<TinNhanCT> tinNhanCTList;
    private TinNhanCTAdapter adapter;

    private String maNguoiGui;
    private String maTinNhan;

    private String maKH = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cskhactivity);


        ImageButton iBtnThoatCSKH = findViewById(R.id.iBtnThoatCSKH);
        TextView tvLoaiHoTroKH = findViewById(R.id.tvLoaiHoTroKH);

        LinearLayout llLienHeTV = findViewById(R.id.llLienHeTV);

        RelativeLayout rlTroChuyen = findViewById(R.id.rlTroChuyen);
        RecyclerView rvViewChat = findViewById(R.id.rvViewChat);
        EditText edtNoiDung = findViewById(R.id.edtNoiDung);
        ImageButton iBtnGui = findViewById(R.id.iBtnGui);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            maNguoiGui = bundle.getString("maNguoiGui");
            maTinNhan = bundle.getString("maTinNhan");
            maKH = bundle.getString("maKH");

            rlTroChuyen.setVisibility(View.VISIBLE);

            if (maKH != null) {
                FirebaseFirestore.getInstance().collection("KhachHang")
                        .document(maKH)
                        .get()
                        .addOnCompleteListener(task -> {
                            String hoTen = task.getResult().getString("hoKhachHang") + " " +
                                    task.getResult().getString("tenKhachHang");
                            tvLoaiHoTroKH.setText(hoTen);
                        });
            } else {
                tvLoaiHoTroKH.setText("Trò chuyện");
            }
        } else {
            tvLoaiHoTroKH.setText("Liên hệ");
            llLienHeTV.setVisibility(View.VISIBLE);
        }

        tinNhanCTList = new ArrayList<>();
        adapter = new TinNhanCTAdapter(this, tinNhanCTList, maNguoiGui);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvViewChat.setLayoutManager(manager);
        rvViewChat.setAdapter(adapter);

        iBtnGui.setOnClickListener(v -> {
            guiTinNhan(edtNoiDung);
        });

        layDanhSachTinNhan(rvViewChat);

        iBtnThoatCSKH.setOnClickListener(v -> {
            finish();
        });
    }

    private void guiTinNhan(EditText editText) {
        if (editText.getText().toString().trim().isEmpty()) {
            return;
        }
        String noiNung = editText.getText().toString();

        LocalTime localTime = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String gio = localTime.format(timeFormatter);

        LocalDate localDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String ngay = localDate.format(dateFormatter);

        TinNhanCT tinNhanCT = new TinNhanCT(UUID.randomUUID().toString(), maTinNhan, maNguoiGui,
                noiNung, gio, ngay);

        new TinNhanCTDAO(this).taoTinNhanCT(tinNhanCT);

        editText.setText(null);
    }

    private void layDanhSachTinNhan(RecyclerView recyclerView) {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("TinNhanCT");

        reference.whereEqualTo("maTinNhan", maTinNhan)
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
                                    TinNhanCT addTinNhanCT = addSnapshot.toObject(TinNhanCT.class);
                                    List<TinNhanCT> readList = new ArrayList<>();
                                    readList.add(addTinNhanCT);
                                    collectionsSort(readList, tinNhanCTList);
                                    recyclerView.smoothScrollToPosition(tinNhanCTList.size() - 1);
                                    adapter.notifyDataSetChanged();
                                    break;
                                case REMOVED:
                                    String removeID = change.getDocument().getId();
                                    for (int i = 0; i < tinNhanCTList.size(); i++) {
                                        if (tinNhanCTList.get(i).getMaTinNhanCT().equals(removeID)) {
                                            tinNhanCTList.remove(i);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                });
    }

    private void collectionsSort(List<TinNhanCT> list, List<TinNhanCT> tinNhanCTList) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        for (TinNhanCT tinNhanCT : list) {
            String date = tinNhanCT.getNgayTaoTN();
            String time = tinNhanCT.getGioTaoTN();

            LocalDate localDate = LocalDate.parse(date, dateFormatter);
            LocalTime localTime = LocalTime.parse(time, timeFormatter);

            LocalDateTime dateTime = LocalDateTime.of(localDate, localTime);
            tinNhanCT.setLocalDateTime(dateTime);
            tinNhanCTList.add(tinNhanCT);
        }
        Collections.sort(tinNhanCTList, ((o1, o2) -> o1.getLocalDateTime().compareTo(o2.getLocalDateTime())));
    }
}