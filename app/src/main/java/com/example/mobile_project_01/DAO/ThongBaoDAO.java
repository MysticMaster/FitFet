package com.example.mobile_project_01.DAO;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.ThanhVien;
import com.example.mobile_project_01.model.ThongBao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ThongBaoDAO {

    private final String COLLECTION = "ThongBao";

    private final Context context;

    public ThongBaoDAO(Context context) {
        this.context = context;
    }

    public void taoThongBao(ThongBao thongBao) {
        HashMap<String, Object> hashMap = thongBao.hashMapTB();
        FirebaseFirestore.getInstance().collection(COLLECTION).document(thongBao.getMaThongBao())
                .set(hashMap);
    }

    public void capNhatThongBao(ThongBao thongBao) {
        FirebaseFirestore.getInstance().collection(COLLECTION).document(thongBao.getMaThongBao())
                .update(thongBao.hashMapTB());
    }

    public void xoaThongBao(ThongBao thongBao) {
        FirebaseFirestore.getInstance().collection(COLLECTION).document(thongBao.getMaThongBao())
                .delete();
    }
}
