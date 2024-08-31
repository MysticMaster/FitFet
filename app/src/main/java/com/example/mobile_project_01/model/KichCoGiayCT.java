package com.example.mobile_project_01.model;

import java.io.Serializable;
import java.util.HashMap;

public class KichCoGiayCT implements Serializable {
    private String maKichCoGiayCT, maGiayCT;
    private int kichCo, soLuong;

    public KichCoGiayCT() {
    }

    public KichCoGiayCT(String maKichCoGiayCT, String maGiayCT, int kichCo, int soLuong) {
        this.maKichCoGiayCT = maKichCoGiayCT;
        this.maGiayCT = maGiayCT;
        this.kichCo = kichCo;
        this.soLuong = soLuong;
    }

    public String getMaKichCoGiayCT() {
        return maKichCoGiayCT;
    }

    public void setMaKichCoGiayCT(String maKichCoGiayCT) {
        this.maKichCoGiayCT = maKichCoGiayCT;
    }

    public String getMaGiayCT() {
        return maGiayCT;
    }

    public void setMaGiayCT(String maGiayCT) {
        this.maGiayCT = maGiayCT;
    }

    public int getKichCo() {
        return kichCo;
    }

    public void setKichCo(int kichCo) {
        this.kichCo = kichCo;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public HashMap<String, Object> hashMapKichCoGiayCT() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("maKichCoGiayCT", this.maKichCoGiayCT);
        data.put("maGiayCT", this.maGiayCT);
        data.put("kichCo", this.kichCo);
        data.put("soLuong", this.soLuong);

        return data;
    }
}
