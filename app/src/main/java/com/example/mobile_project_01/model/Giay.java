package com.example.mobile_project_01.model;

import java.io.Serializable;
import java.util.HashMap;

public class Giay implements Serializable {
    private String maGiay, tenGiay, anhGiay, maThuongHieu;
    private int trangThaiGiay;

    public Giay() {
    }

    public Giay(String maGiay, String tenGiay, String anhGiay, String maThuongHieu, int trangThaiGiay) {
        this.maGiay = maGiay;
        this.tenGiay = tenGiay;
        this.anhGiay = anhGiay;
        this.maThuongHieu = maThuongHieu;
        this.trangThaiGiay = trangThaiGiay;
    }

    public String getMaGiay() {
        return maGiay;
    }

    public void setMaGiay(String maGiay) {
        this.maGiay = maGiay;
    }

    public String getTenGiay() {
        return tenGiay;
    }

    public void setTenGiay(String tenGiay) {
        this.tenGiay = tenGiay;
    }

    public String getAnhGiay() {
        return anhGiay;
    }

    public void setAnhGiay(String anhGiay) {
        this.anhGiay = anhGiay;
    }

    public String getMaThuongHieu() {
        return maThuongHieu;
    }

    public void setMaThuongHieu(String maThuongHieu) {
        this.maThuongHieu = maThuongHieu;
    }

    public int getTrangThaiGiay() {
        return trangThaiGiay;
    }

    public void setTrangThaiGiay(int trangThaiGiay) {
        this.trangThaiGiay = trangThaiGiay;
    }

    public HashMap<String, Object> hashMapGiay() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("maGiay", this.maGiay);
        data.put("tenGiay", this.tenGiay);
        data.put("anhGiay", this.anhGiay);
        data.put("maThuongHieu", this.maThuongHieu);
        data.put("trangThaiGiay", this.trangThaiGiay);

        return data;
    }
}
