package com.example.mobile_project_01.DAO;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mobile_project_01.model.Giay;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class GiayDAO {
    private final String COLLECTION = "Giay";

    private final Context context;

    public GiayDAO(Context context) {
        this.context = context;
    }

    public void themGiay(Giay giay) {
        HashMap<String, Object> hashMap = giay.hashMapGiay();
        FirebaseFirestore.getInstance().collection(COLLECTION).document(giay.getMaGiay())
                .set(hashMap)
                .addOnCompleteListener(task -> {
                    Toast.makeText(context, "Thêm giày thành công", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(context, "Thêm giày thất bại", Toast.LENGTH_SHORT).show();
                });
    }

    public void capNhatGiay(Giay giay) {
        FirebaseFirestore.getInstance().collection(COLLECTION).document(giay.getMaGiay())
                .update(giay.hashMapGiay());
    }
}
