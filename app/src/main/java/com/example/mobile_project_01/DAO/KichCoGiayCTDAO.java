package com.example.mobile_project_01.DAO;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mobile_project_01.model.KichCoGiayCT;
import com.example.mobile_project_01.model.ThongBao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class KichCoGiayCTDAO {
    private final String COLLECTION = "KichCoGiayChiTiet";

    private final Context context;

    public KichCoGiayCTDAO(Context context) {
        this.context = context;
    }

    public void themKichCo(KichCoGiayCT kichCoGiayCT) {
        HashMap<String, Object> hashMap = kichCoGiayCT.hashMapKichCoGiayCT();
        FirebaseFirestore.getInstance().collection(COLLECTION).document(kichCoGiayCT.getMaKichCoGiayCT())
                .set(hashMap);
    }

    public void capNhatKichCo(KichCoGiayCT kichCoGiayCT) {
        FirebaseFirestore.getInstance().collection(COLLECTION).document(kichCoGiayCT.getMaKichCoGiayCT())
                .update(kichCoGiayCT.hashMapKichCoGiayCT());
    }

    public void xoaKichCo(KichCoGiayCT kichCoGiayCT) {
        FirebaseFirestore.getInstance().collection(COLLECTION).document(kichCoGiayCT.getMaKichCoGiayCT())
                .delete();
    }
}
