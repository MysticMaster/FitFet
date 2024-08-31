package com.example.mobile_project_01.model;

import java.io.Serializable;
import java.util.HashMap;

public class GioHang implements Serializable {

    private String maGioHang, maKhachHang, maGiayChiTiet;
    private int kichCoMua, soLuongMua, tongTien;

    public GioHang() {
    }

    public GioHang(String maGioHang, String maKhachHang, String maGiayChiTiet, int kichCoMua, int soLuongMua, int tongTien) {
        this.maGioHang = maGioHang;
        this.maKhachHang = maKhachHang;
        this.maGiayChiTiet = maGiayChiTiet;
        this.kichCoMua = kichCoMua;
        this.soLuongMua = soLuongMua;
        this.tongTien = tongTien;
    }

    public String getMaGioHang() {
        return maGioHang;
    }

    public void setMaGioHang(String maGioHang) {
        this.maGioHang = maGioHang;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
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

    public HashMap<String, Object> hashMapGioHang() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("maGioHang", this.maGioHang);
        data.put("maKhachHang", this.maKhachHang);
        data.put("maGiayChiTiet", this.maGiayChiTiet);
        data.put("kichCoMua", this.kichCoMua);
        data.put("soLuongMua", this.soLuongMua);
        data.put("tongTien", this.tongTien);

        return data;
    }

}
