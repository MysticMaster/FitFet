package com.example.mobile_project_01.model;

import java.io.Serializable;
import java.util.HashMap;

public class PhanHoi implements Serializable {
    private String maPhanHoi, maBinhLuan, noiDung;

    public PhanHoi() {
    }

    public PhanHoi(String maPhanHoi, String maBinhLuan, String noiDung) {
        this.maPhanHoi = maPhanHoi;
        this.maBinhLuan = maBinhLuan;
        this.noiDung = noiDung;
    }

    public String getMaPhanHoi() {
        return maPhanHoi;
    }

    public void setMaPhanHoi(String maPhanHoi) {
        this.maPhanHoi = maPhanHoi;
    }

    public String getMaBinhLuan() {
        return maBinhLuan;
    }

    public void setMaBinhLuan(String maBinhLuan) {
        this.maBinhLuan = maBinhLuan;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public HashMap<String, Object> hashMapPhanHoi() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("maPhanHoi", this.maPhanHoi);
        data.put("maBinhLuan", this.maBinhLuan);
        data.put("noiDung", this.noiDung);

        return data;
    }
}
