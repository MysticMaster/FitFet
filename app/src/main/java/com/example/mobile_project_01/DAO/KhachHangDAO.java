package com.example.mobile_project_01.DAO;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mobile_project_01.model.KhachHang;
import com.example.mobile_project_01.model.ThanhVien;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class KhachHangDAO {
    private final String COLLECTION = "KhachHang";
    private final Context context;

    public KhachHangDAO(Context context) {
        this.context = context;
    }


    public void themKhachHang(KhachHang khachHang) {
        HashMap<String, Object> hashMap = khachHang.hashMapKH();
        FirebaseFirestore.getInstance().collection(COLLECTION).document(khachHang.getMaKhachHang())
                .set(hashMap)
                .addOnCompleteListener(task -> {
                    Toast.makeText(context, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(context, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                });
    }

    public void capNhatKhachHang(KhachHang khachHang) {
        FirebaseFirestore.getInstance().collection(COLLECTION).document(khachHang.getMaKhachHang())
                .update(khachHang.hashMapKH()).addOnSuccessListener(command -> {
                    Toast.makeText(context, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(context, "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT).show();
                });
    }
}
