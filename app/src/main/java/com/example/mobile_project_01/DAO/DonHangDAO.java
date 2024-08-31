package com.example.mobile_project_01.DAO;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mobile_project_01.model.DonHang;
import com.example.mobile_project_01.model.GiayChiTiet;
import com.example.mobile_project_01.model.ThongBao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class DonHangDAO {
    private final String COLLECTION = "DonHang";

    private final Context context;

    public DonHangDAO(Context context) {
        this.context = context;
    }

    public void taoDonHang(DonHang donHang) {
        HashMap<String, Object> hashMap = donHang.hashMapDH();
        FirebaseFirestore.getInstance().collection(COLLECTION).document(donHang.getMaDonHang())
                .set(hashMap);
    }

    public void capNhatDonHang(DonHang donHang) {
        FirebaseFirestore.getInstance().collection(COLLECTION).document(donHang.getMaDonHang())
                .update(donHang.hashMapDH());
    }

    public void xoaDonHang(DonHang donHang) {
        FirebaseFirestore.getInstance().collection(COLLECTION).document(donHang.getMaDonHang())
                .delete();
    }
}
