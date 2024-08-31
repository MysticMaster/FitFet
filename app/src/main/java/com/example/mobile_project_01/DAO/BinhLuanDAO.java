package com.example.mobile_project_01.DAO;

import android.content.Context;
import android.widget.Toast;

import com.example.mobile_project_01.model.BinhLuan;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class BinhLuanDAO {

    private final String COLLECTION = "BinhLuan";

    private final Context context;

    public BinhLuanDAO(Context context) {
        this.context = context;
    }

    public void taoBinhLuan(BinhLuan binhLuan) {
        HashMap<String, Object> hashMap = binhLuan.hashMapBinhLuan();
        FirebaseFirestore.getInstance().collection(COLLECTION).document(binhLuan.getMaBinhLuan())
                .set(hashMap);
    }

    public void capNhatBinhLuan(BinhLuan binhLuan) {
        FirebaseFirestore.getInstance().collection(COLLECTION).document(binhLuan.getMaBinhLuan())
                .update(binhLuan.hashMapBinhLuan());
    }

    public void xoaBinhLuan(BinhLuan binhLuan) {
        FirebaseFirestore.getInstance().collection(COLLECTION).document(binhLuan.getMaBinhLuan())
                .delete();
    }

}
