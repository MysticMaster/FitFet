package com.example.mobile_project_01.model;

import java.io.Serializable;
import java.util.HashMap;

public class KhachHang implements Serializable {
    private String maKhachHang, hoKhachHang, tenKhachHang, emailKhachHang, soDienThoai, diaChi, anhKhachHang;
    private String maTaiKhoan;
    private int trangThaiKhachHang;

    public KhachHang() {
    }

    public KhachHang(String maKhachHang, String hoKhachHang, String tenKhachHang, String emailKhachHang, String soDienThoai, String diaChi, String anhKhachHang, String maTaiKhoan, int trangThaiKhachHang) {
        this.maKhachHang = maKhachHang;
        this.hoKhachHang = hoKhachHang;
        this.tenKhachHang = tenKhachHang;
        this.emailKhachHang = emailKhachHang;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.anhKhachHang = anhKhachHang;
        this.maTaiKhoan = maTaiKhoan;
        this.trangThaiKhachHang = trangThaiKhachHang;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getHoKhachHang() {
        return hoKhachHang;
    }

    public void setHoKhachHang(String hoKhachHang) {
        this.hoKhachHang = hoKhachHang;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getEmailKhachHang() {
        return emailKhachHang;
    }

    public void setEmailKhachHang(String emailKhachHang) {
        this.emailKhachHang = emailKhachHang;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getAnhKhachHang() {
        return anhKhachHang;
    }

    public void setAnhKhachHang(String anhKhachHang) {
        this.anhKhachHang = anhKhachHang;
    }

    public String getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public void setMaTaiKhoan(String maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }

    public int getTrangThaiKhachHang() {
        return trangThaiKhachHang;
    }

    public void setTrangThaiKhachHang(int trangThaiKhachHang) {
        this.trangThaiKhachHang = trangThaiKhachHang;
    }

    public HashMap<String, Object> hashMapKH() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("maKhachHang", this.maKhachHang);
        data.put("hoKhachHang", this.hoKhachHang);
        data.put("tenKhachHang", this.tenKhachHang);
        data.put("emailKhachHang", this.emailKhachHang);
        data.put("soDienThoai", this.soDienThoai);
        data.put("diaChi", this.diaChi);
        data.put("anhKhachHang", this.anhKhachHang);
        data.put("maTaiKhoan", this.maTaiKhoan);
        data.put("trangThaiKhachHang", this.trangThaiKhachHang);

        return data;
    }
}
