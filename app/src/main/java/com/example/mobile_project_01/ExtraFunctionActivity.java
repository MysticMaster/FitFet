package com.example.mobile_project_01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mobile_project_01.DAO.TinNhanDAO;
import com.example.mobile_project_01.function.AutoStringID;
import com.example.mobile_project_01.model.KhachHang;
import com.example.mobile_project_01.model.ThanhVien;
import com.example.mobile_project_01.model.TinNhan;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ExtraFunctionActivity extends AppCompatActivity {

    private KhachHang khachHang;
    private ThanhVien thanhVien;
    private int loaiExtra = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_function);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            khachHang = (KhachHang) bundle.getSerializable("khachHang");
            thanhVien = (ThanhVien) bundle.getSerializable("thanhVien");
            loaiExtra = bundle.getInt("loaiExtra");
        }

        ImageButton iBtnExtraFunction = findViewById(R.id.iBtnExtraFunction);
        TextView tvLoaiExtra = findViewById(R.id.tvLoaiExtra);

        LinearLayout llExtraThemThongTinKH = findViewById(R.id.llExtraThemThongTinKH);
        CardView cvSDTKH = findViewById(R.id.cvSDTKH);
        CardView cvDiaChiKH = findViewById(R.id.cvDiaChiKH);

        LinearLayout llExtraBaoMat = findViewById(R.id.llExtraBaoMat);
        CardView cvMatKhau = findViewById(R.id.cvMatKhau);

        LinearLayout llExtraHoTroKH = findViewById(R.id.llExtraHoTroKH);
        CardView cvThongTinLienHeTV = findViewById(R.id.cvThongTinLienHeTV);
        CardView cvTroChuyenTV = findViewById(R.id.cvTroChuyenTV);

        if (loaiExtra == 0) {
            llExtraThemThongTinKH.setVisibility(View.VISIBLE);
            tvLoaiExtra.setText("Thêm thông tin");
        } else if (loaiExtra == 1) {
            llExtraBaoMat.setVisibility(View.VISIBLE);
            tvLoaiExtra.setText("Bảo mật");
        } else if (loaiExtra == 2) {
            llExtraHoTroKH.setVisibility(View.VISIBLE);
            tvLoaiExtra.setText("Cần hỗ trợ");
        }

        Intent intent = new Intent(this, DetailUpdateThongTinActivity.class);
        Bundle sendBundle = new Bundle();

        if (khachHang != null) {
            sendBundle.putSerializable("khachHang", khachHang);

            cvSDTKH.setOnClickListener(v -> {
                sendBundle.putInt("loaiUpdate", 1);
                intent.putExtras(sendBundle);
                startActivity(intent);
            });

            cvDiaChiKH.setOnClickListener(v -> {
                sendBundle.putInt("loaiUpdate", 2);
                intent.putExtras(sendBundle);
                startActivity(intent);
            });

            cvMatKhau.setOnClickListener(v -> {
                sendBundle.putInt("loaiUpdate", 3);
                intent.putExtras(sendBundle);
                startActivity(intent);
            });

            cvThongTinLienHeTV.setOnClickListener(v -> {
                startActivity(new Intent(this, CSKHActivity.class));
            });

            cvTroChuyenTV.setOnClickListener(v -> {
                CollectionReference collectionRef = FirebaseFirestore.getInstance().collection("TinNhan");
                collectionRef.whereEqualTo("maKhachHang", khachHang.getMaKhachHang())
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                QuerySnapshot snapshot = task.getResult();
                                if (snapshot.isEmpty()) {
                                    String maTinNhan = AutoStringID.createStringID("TN-");
                                    TinNhan tinNhan = new TinNhan(maTinNhan, khachHang.getMaKhachHang(), 0);
                                    new TinNhanDAO(this).taoTinNhan(tinNhan);

                                    Intent newIntent = new Intent(this, CSKHActivity.class);
                                    Bundle newBundle = new Bundle();
                                    newBundle.putString("maNguoiGui", khachHang.getMaKhachHang());
                                    newBundle.putString("maTinNhan", tinNhan.getMaTinNhan());
                                    newIntent.putExtras(newBundle);
                                    startActivity(newIntent);
                                } else {
                                    TinNhan tinNhan = snapshot.getDocuments().get(0).toObject(TinNhan.class);

                                    Intent newIntent = new Intent(this, CSKHActivity.class);
                                    Bundle newBundle = new Bundle();
                                    newBundle.putString("maNguoiGui", khachHang.getMaKhachHang());
                                    newBundle.putString("maTinNhan", tinNhan.getMaTinNhan());
                                    newIntent.putExtras(newBundle);
                                    startActivity(newIntent);
                                }
                            }
                        });
            });
        } else if (thanhVien != null) {
            sendBundle.putSerializable("thanhVien", thanhVien);

            cvMatKhau.setOnClickListener(v -> {
                sendBundle.putInt("loaiUpdate", 3);
                intent.putExtras(sendBundle);
                startActivity(intent);
            });
        }


        iBtnExtraFunction.setOnClickListener(v -> {
            finish();
        });
    }
}