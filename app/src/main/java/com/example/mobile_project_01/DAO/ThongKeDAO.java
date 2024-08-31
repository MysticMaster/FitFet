package com.example.mobile_project_01.DAO;

import android.content.Context;

import com.example.mobile_project_01.model.ThongKe;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ThongKeDAO {
    private final String COLLECTION = "ThongKe";

    public ThongKeDAO() {
    }

    public void luuThongKe(ThongKe thongKe) {
        HashMap<String, Object> hashMap = thongKe.hashMapThongKe();
        FirebaseFirestore.getInstance().collection(COLLECTION).document(thongKe.getMaThongKe())
                .set(hashMap);
    }
}
