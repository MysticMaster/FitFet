package com.example.mobile_project_01.DAO;

import android.content.Context;
import android.widget.Toast;

import com.example.mobile_project_01.model.PhanHoi;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class PhanHoiDAO {

    private final String COLLECTION = "PhanHoi";

    private final Context context;

    public PhanHoiDAO(Context context) {
        this.context = context;
    }

    public void taoPhanHoi(PhanHoi phanHoi) {
        HashMap<String, Object> hashMap = phanHoi.hashMapPhanHoi();
        FirebaseFirestore.getInstance().collection(COLLECTION).document(phanHoi.getMaPhanHoi())
                .set(hashMap);
    }

    public void capNhatPhanHoi(PhanHoi phanHoi) {
        FirebaseFirestore.getInstance().collection(COLLECTION).document(phanHoi.getMaPhanHoi())
                .update(phanHoi.hashMapPhanHoi());
    }

    public void xoaPhanHoi(PhanHoi phanHoi) {
        FirebaseFirestore.getInstance().collection(COLLECTION).document(phanHoi.getMaPhanHoi())
                .delete();
    }

}
