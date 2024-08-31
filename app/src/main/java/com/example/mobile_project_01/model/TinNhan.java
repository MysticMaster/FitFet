package com.example.mobile_project_01.model;

import java.io.Serializable;
import java.util.HashMap;

public class TinNhan implements Serializable {
    private String maTinNhan, maKhachHang;
    private int trangThai;

    public TinNhan() {
    }

    public TinNhan(String maTinNhan, String maKhachHang, int trangThai) {
        this.maTinNhan = maTinNhan;
        this.maKhachHang = maKhachHang;
        this.trangThai = trangThai;
    }

    public String getMaTinNhan() {
        return maTinNhan;
    }

    public void setMaTinNhan(String maTinNhan) {
        this.maTinNhan = maTinNhan;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public HashMap<String, Object> hashMapTN() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("maTinNhan", this.maTinNhan);
        data.put("maKhachHang", this.maKhachHang);
        data.put("trangThai", this.trangThai);

        return data;
    }
}
