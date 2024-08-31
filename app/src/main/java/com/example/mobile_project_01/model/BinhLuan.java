package com.example.mobile_project_01.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;

public class BinhLuan implements Serializable {
    private String maBinhLuan, maGiay, maKhachHang, noiDung;
    private String gioBinhLuan, ngayBinhLuan;
    private LocalDateTime localDateTime;
    private int trangThaiBinhLuan;

    public BinhLuan() {
    }

    public BinhLuan(String maBinhLuan, String maGiay, String maKhachHang, String noiDung, String gioBinhLuan, String ngayBinhLuan, int trangThaiBinhLuan) {
        this.maBinhLuan = maBinhLuan;
        this.maGiay = maGiay;
        this.maKhachHang = maKhachHang;
        this.noiDung = noiDung;
        this.gioBinhLuan = gioBinhLuan;
        this.ngayBinhLuan = ngayBinhLuan;
        this.trangThaiBinhLuan = trangThaiBinhLuan;
    }

    public String getMaBinhLuan() {
        return maBinhLuan;
    }

    public void setMaBinhLuan(String maBinhLuan) {
        this.maBinhLuan = maBinhLuan;
    }

    public String getMaGiay() {
        return maGiay;
    }

    public void setMaGiay(String maGiay) {
        this.maGiay = maGiay;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getGioBinhLuan() {
        return gioBinhLuan;
    }

    public void setGioBinhLuan(String gioBinhLuan) {
        this.gioBinhLuan = gioBinhLuan;
    }

    public String getNgayBinhLuan() {
        return ngayBinhLuan;
    }

    public void setNgayBinhLuan(String ngayBinhLuan) {
        this.ngayBinhLuan = ngayBinhLuan;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public int getTrangThaiBinhLuan() {
        return trangThaiBinhLuan;
    }

    public void setTrangThaiBinhLuan(int trangThaiBinhLuan) {
        this.trangThaiBinhLuan = trangThaiBinhLuan;
    }

    public HashMap<String, Object> hashMapBinhLuan() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("maBinhLuan", this.maBinhLuan);
        data.put("maGiay", this.maGiay);
        data.put("maKhachHang", this.maKhachHang);
        data.put("noiDung", this.noiDung);
        data.put("gioBinhLuan", this.gioBinhLuan);
        data.put("ngayBinhLuan", this.ngayBinhLuan);
        data.put("trangThaiBinhLuan", this.trangThaiBinhLuan);

        return data;
    }
}
