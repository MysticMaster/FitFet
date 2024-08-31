package com.example.mobile_project_01.DAO;

import android.content.Context;
import android.widget.Toast;

import com.example.mobile_project_01.model.TinNhanCT;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class TinNhanCTDAO {

    private final String COLLECTION = "TinNhanCT";

    private final Context context;

    public TinNhanCTDAO(Context context) {
        this.context = context;
    }

    public void taoTinNhanCT(TinNhanCT tinNhanCT) {
        HashMap<String, Object> hashMap = tinNhanCT.hashMapTNCT();
        FirebaseFirestore.getInstance().collection(COLLECTION).document(tinNhanCT.getMaTinNhanCT())
                .set(hashMap);
    }

    public void xoaTinNhanCT(TinNhanCT tinNhanCT) {
        FirebaseFirestore.getInstance().collection(COLLECTION).document(tinNhanCT.getMaTinNhanCT())
                .delete();
    }

}
