package com.example.mobile_project_01.DAO;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mobile_project_01.model.GiayChiTiet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class GiayChiTietDAO {
    private final String COLLECTION = "GiayChiTiet";

    private final Context context;

    public GiayChiTietDAO(Context context) {
        this.context = context;
    }


    public void themGiayChiTiet(GiayChiTiet giayChiTiet) {
        HashMap<String, Object> hashMap = giayChiTiet.hashMapGiayCT();
        FirebaseFirestore.getInstance().collection(COLLECTION).document(giayChiTiet.getMaGiayChiTiet())
                .set(hashMap)
                .addOnCompleteListener(task -> {
                    Toast.makeText(context, "Thêm phân loại thành công", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(context, "Thêm phân loại tiết thất bại", Toast.LENGTH_SHORT).show();
                });
    }

    public void capNhatGiayChiTiet(GiayChiTiet giayChiTiet) {
        FirebaseFirestore.getInstance().collection(COLLECTION).document(giayChiTiet.getMaGiayChiTiet())
                .update(giayChiTiet.hashMapGiayCT());
    }
}
