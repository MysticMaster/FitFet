package com.example.mobile_project_01.DAO;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mobile_project_01.model.DonHangChiTiet;
import com.example.mobile_project_01.model.GioHang;
import com.example.mobile_project_01.model.KichCoGiayCT;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class GioHangDAO {

    private final String COLLECTION = "GioHang";

    private final Context context;

    public GioHangDAO(Context context) {
        this.context = context;
    }

    public void themGiohang(GioHang gioHang) {
        HashMap<String, Object> hashMap = gioHang.hashMapGioHang();
        FirebaseFirestore.getInstance().collection(COLLECTION).document(gioHang.getMaGioHang())
                .set(hashMap);
    }

    public void capNhatGioHang(GioHang gioHang) {
        FirebaseFirestore.getInstance().collection(COLLECTION).document(gioHang.getMaGioHang())
                .update(gioHang.hashMapGioHang());
    }

    public void xoaGioHang(GioHang gioHang) {
        FirebaseFirestore.getInstance().collection(COLLECTION).document(gioHang.getMaGioHang())
                .delete();
    }

}
