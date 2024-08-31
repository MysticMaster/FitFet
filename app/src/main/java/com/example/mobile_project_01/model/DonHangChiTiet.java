package com.example.mobile_project_01.model;

import java.io.Serializable;
import java.util.HashMap;

public class DonHangChiTiet implements Serializable {
    private String maDonHangChiTiet, maDonHang, maGiayChiTiet;
    private int kichCoMua, soLuongMua, tongTien;

    public DonHangChiTiet() {
    }

    public DonHangChiTiet(String maDonHangChiTiet, String maDonHang, String maGiayChiTiet, int kichCoMua, int soLuongMua, int tongTien) {
        this.maDonHangChiTiet = maDonHangChiTiet;
        this.maDonHang = maDonHang;
        this.maGiayChiTiet = maGiayChiTiet;
        this.kichCoMua = kichCoMua;
        this.soLuongMua = soLuongMua;
        this.tongTien = tongTien;
    }

    public String getMaDonHangChiTiet() {
        return maDonHangChiTiet;
    }

    public void setMaDonHangChiTiet(String maDonHangChiTiet) {
        this.maDonHangChiTiet = maDonHangChiTiet;
    }

    public String getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(String maDonHang) {
        this.maDonHang = maDonHang;
    }

    public String getMaGiayChiTiet() {
        return maGiayChiTiet;
    }

    public void setMaGiayChiTiet(String maGiayChiTiet) {
        this.maGiayChiTiet = maGiayChiTiet;
    }

    public int getKichCoMua() {
        return kichCoMua;
    }

    public void setKichCoMua(int kichCoMua) {
        this.kichCoMua = kichCoMua;
    }

    public int getSoLuongMua() {
        return soLuongMua;
    }

    public void setSoLuongMua(int soLuongMua) {
        this.soLuongMua = soLuongMua;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }

    public HashMap<String, Object> hashMapDHCT() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("maDonHangChiTiet", this.maDonHangChiTiet);
        data.put("maDonHang", this.maDonHang);
        data.put("maGiayChiTiet", this.maGiayChiTiet);
        data.put("kichCoMua", this.kichCoMua);
        data.put("soLuongMua", this.soLuongMua);
        data.put("tongTien", this.tongTien);

        return data;
    }

}
