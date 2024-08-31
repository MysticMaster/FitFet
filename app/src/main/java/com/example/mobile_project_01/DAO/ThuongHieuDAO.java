package com.example.mobile_project_01.DAO;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.ThuongHieu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ThuongHieuDAO {
    private final String COLLECTION = "ThuongHieu";

    private final Context context;

    public ThuongHieuDAO(Context context) {
        this.context = context;
    }


    public void themThuongHieu(ThuongHieu thuongHieu) {
        HashMap<String, Object> hashMap = thuongHieu.hashMapThuongHieu();
        FirebaseFirestore.getInstance().collection(COLLECTION).document(thuongHieu.getMaThuongHieu())
                .set(hashMap)
                .addOnCompleteListener(task -> {
                    Toast.makeText(context, "Thêm thương hiệu thành công", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(context, "Thêm thương hiệu thất bại", Toast.LENGTH_SHORT).show();
                });
    }

    public void capNhatThuongHieu(ThuongHieu thuongHieu) {
        FirebaseFirestore.getInstance().collection(COLLECTION).document(thuongHieu.getMaThuongHieu())
                .update(thuongHieu.hashMapThuongHieu()).addOnSuccessListener(command -> {
                    Toast.makeText(context, "Cập nhật thương hiệu thành công", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(context, "Cập nhật thương hiệu thất bại", Toast.LENGTH_SHORT).show();
                });
    }
}
