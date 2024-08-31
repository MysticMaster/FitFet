package com.example.mobile_project_01.DAO;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mobile_project_01.model.DonHang;
import com.example.mobile_project_01.model.DonHangChiTiet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class DonHangChiTietDAO {

    private final String COLLECTION = "DonHangChiTiet";

    private final Context context;

    public DonHangChiTietDAO(Context context) {
        this.context = context;
    }

    public void taoDonHangCT(DonHangChiTiet donHangChiTiet) {
        HashMap<String, Object> hashMap = donHangChiTiet.hashMapDHCT();
        FirebaseFirestore.getInstance().collection(COLLECTION).document(donHangChiTiet.getMaDonHangChiTiet())
                .set(hashMap);
    }

    public void capNhatDonHangCT(DonHangChiTiet donHangChiTiet) {
        FirebaseFirestore.getInstance().collection(COLLECTION).document(donHangChiTiet.getMaDonHangChiTiet())
                .update(donHangChiTiet.hashMapDHCT());
    }

    public void xoaDonHangCT(DonHangChiTiet donHangChiTiet) {
        FirebaseFirestore.getInstance().collection(COLLECTION).document(donHangChiTiet.getMaDonHangChiTiet())
                .delete();
    }

}
