package com.example.mobile_project_01.DAO;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.ThanhVien;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ThanhVienDAO {
    private final String COLLECTION = "ThanhVien";
    private final Context context;

    public ThanhVienDAO(Context context) {
        this.context = context;
    }

    public void themThanhVien(ThanhVien thanhVien) {
        HashMap<String, Object> hashMap = thanhVien.hashMapTV();
        FirebaseFirestore.getInstance().collection(COLLECTION).document(thanhVien.getMaThanhVien())
                .set(hashMap)
                .addOnCompleteListener(task -> {
                    Toast.makeText(context, "Thêm thành viên thành công", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(context, "Thêm thành viên thất bại", Toast.LENGTH_SHORT).show();
                });
    }

    public void capNhatThanhVien(ThanhVien thanhVien) {
        FirebaseFirestore.getInstance().collection(COLLECTION).document(thanhVien.getMaThanhVien())
                .update(thanhVien.hashMapTV()).addOnSuccessListener(command -> {
                    Toast.makeText(context, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(context, "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT).show();
                });
    }
}
