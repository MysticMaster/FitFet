package com.example.mobile_project_01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mobile_project_01.DAO.KhachHangDAO;
import com.example.mobile_project_01.DAO.ThanhVienDAO;
import com.example.mobile_project_01.fragment.BottomSheetDialogChangeAvatarFragment;
import com.example.mobile_project_01.function.AutoStringID;
import com.example.mobile_project_01.model.KhachHang;
import com.example.mobile_project_01.model.ThanhVien;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DetailAnhActivity extends AppCompatActivity {

    private KhachHang khachHang;
    private ThanhVien thanhVien;

    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_anh_activity);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            khachHang = (KhachHang) bundle.getSerializable("khachHang");
            thanhVien = (ThanhVien) bundle.getSerializable("thanhVien");
        }


        ImageView ivDetailAnh = findViewById(R.id.ivDetailAnh);
        ImageButton iBtnThoatHTAnh = findViewById(R.id.iBtnThoatHTAnh);
        ImageButton iBtnDoiAnh = findViewById(R.id.iBtnDoiAnh);
        ProgressBar pbQLCapNhatAnh = findViewById(R.id.pbQLCapNhatAnh);
        TextView tvLuuAnh = findViewById(R.id.tvLuuAnh);

        if (khachHang != null) {
            if (khachHang.getAnhKhachHang().equals("0")) {
                ivDetailAnh.setImageResource(R.drawable.none_image);
            } else {
                Glide.with(this).load(khachHang.getAnhKhachHang()).into(ivDetailAnh);
            }

            BottomSheetDialogChangeAvatarFragment dialog =
                    new BottomSheetDialogChangeAvatarFragment(ivDetailAnh, tvLuuAnh);
            iBtnDoiAnh.setOnClickListener(v -> {
                dialog.show(getSupportFragmentManager(), dialog.getTag());

            });

            StorageReference reference = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = reference.child("khoHinhKhachHang/" + AutoStringID.createStringID("ANHKH-"));

            tvLuuAnh.setOnClickListener(v1 -> {
                tvLuuAnh.setVisibility(View.INVISIBLE);
                pbQLCapNhatAnh.setVisibility(View.VISIBLE);

                uri = dialog.getUri();
                UploadTask uploadTask = imageRef.putFile(uri);

                uploadTask.addOnSuccessListener((taskSnapshot -> {
                    Task<Uri> uriTask = imageRef.getDownloadUrl();

                    uriTask.addOnSuccessListener((uri1 -> {
                        if (!khachHang.getAnhKhachHang().equals("0")) {
                            FirebaseStorage.getInstance().getReferenceFromUrl(khachHang.getAnhKhachHang())
                                    .delete();
                        }
                        String anhKH = uri1.toString();
                        khachHang.setAnhKhachHang(anhKH);
                        new KhachHangDAO(this).capNhatKhachHang(khachHang);
                        pbQLCapNhatAnh.setVisibility(View.GONE);

                        iBtnThoatHTAnh.setOnClickListener(v -> {
                            Intent intent = new Intent(this, KhachHangMainActivity.class);
                            Bundle sendBundle = new Bundle();
                            sendBundle.putSerializable("khachHang", khachHang);
                            sendBundle.putBoolean("change", true);
                            intent.putExtras(sendBundle);
                            startActivity(intent);
                            finish();
                        });
                    })).addOnFailureListener(e -> {
                        tvLuuAnh.setVisibility(View.VISIBLE);
                        pbQLCapNhatAnh.setVisibility(View.GONE);
                        Toast.makeText(this, "Lỗi kết nối internet", Toast.LENGTH_SHORT).show();
                    });
                })).addOnFailureListener(e -> {
                    tvLuuAnh.setVisibility(View.VISIBLE);
                    pbQLCapNhatAnh.setVisibility(View.GONE);
                    Toast.makeText(this, "Lỗi kết nối internet", Toast.LENGTH_SHORT).show();
                });
            });
        } else if (thanhVien != null) {
            if (thanhVien.getAnhThanhVien().equals("0")) {
                ivDetailAnh.setImageResource(R.drawable.none_image);
            } else {
                Glide.with(this).load(thanhVien.getAnhThanhVien()).into(ivDetailAnh);
            }

            BottomSheetDialogChangeAvatarFragment dialog =
                    new BottomSheetDialogChangeAvatarFragment(ivDetailAnh, tvLuuAnh);
            iBtnDoiAnh.setOnClickListener(v -> {
                dialog.show(getSupportFragmentManager(), dialog.getTag());

            });

            StorageReference reference = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = reference.child("khoHinhThanhVien/" + AutoStringID.createStringID("ANHTV-"));

            tvLuuAnh.setOnClickListener(v1 -> {
                tvLuuAnh.setVisibility(View.INVISIBLE);
                pbQLCapNhatAnh.setVisibility(View.VISIBLE);

                uri = dialog.getUri();
                UploadTask uploadTask = imageRef.putFile(uri);

                uploadTask.addOnSuccessListener((taskSnapshot -> {
                    Task<Uri> uriTask = imageRef.getDownloadUrl();

                    uriTask.addOnSuccessListener((uri1 -> {
                        if (!thanhVien.getAnhThanhVien().equals("0")) {
                            FirebaseStorage.getInstance().getReferenceFromUrl(thanhVien.getAnhThanhVien())
                                    .delete();
                        }
                        String anhTV = uri1.toString();
                        thanhVien.setAnhThanhVien(anhTV);
                        new ThanhVienDAO(this).capNhatThanhVien(thanhVien);
                        pbQLCapNhatAnh.setVisibility(View.GONE);

                        iBtnThoatHTAnh.setOnClickListener(v -> {
                            Intent intent = new Intent(this, ThanhVienMainActivity.class);
                            Bundle sendBundle = new Bundle();
                            sendBundle.putSerializable("thanhVien", thanhVien);
                            sendBundle.putBoolean("change", true);
                            intent.putExtras(sendBundle);
                            startActivity(intent);
                            finish();
                        });
                    })).addOnFailureListener(e -> {
                        tvLuuAnh.setVisibility(View.VISIBLE);
                        pbQLCapNhatAnh.setVisibility(View.GONE);
                        Toast.makeText(this, "Lỗi kết nối internet", Toast.LENGTH_SHORT).show();
                    });
                })).addOnFailureListener(e -> {
                    tvLuuAnh.setVisibility(View.VISIBLE);
                    pbQLCapNhatAnh.setVisibility(View.GONE);
                    Toast.makeText(this, "Lỗi kết nối internet", Toast.LENGTH_SHORT).show();
                });
            });
        }

        iBtnThoatHTAnh.setOnClickListener(v -> {
            finish();
        });

    }
}