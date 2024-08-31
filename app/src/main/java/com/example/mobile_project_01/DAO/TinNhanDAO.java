package com.example.mobile_project_01.DAO;

import android.content.Context;
import android.widget.Toast;

import com.example.mobile_project_01.model.TinNhan;
import com.example.mobile_project_01.model.TinNhanCT;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class TinNhanDAO {
    private final String COLLECTION = "TinNhan";

    private final Context context;

    public TinNhanDAO(Context context) {
        this.context = context;
    }

    public void taoTinNhan(TinNhan tinNhan) {
        HashMap<String, Object> hashMap = tinNhan.hashMapTN();
        FirebaseFirestore.getInstance().collection(COLLECTION).document(tinNhan.getMaTinNhan())
                .set(hashMap);
    }

}
