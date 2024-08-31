package com.example.mobile_project_01.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;

public class ThongKe implements Serializable {
    private String maThongKe, maGiayChiTiet, ngay;
    private int doanhThu, soLuongDaBan;
    private LocalDate localDate;

    public ThongKe() {
    }

    public ThongKe(String maThongKe, String maGiayChiTiet, String ngay, int doanhThu, int soLuongDaBan) {
        this.maThongKe = maThongKe;
        this.maGiayChiTiet = maGiayChiTiet;
        this.ngay = ngay;
        this.doanhThu = doanhThu;
        this.soLuongDaBan = soLuongDaBan;
    }

    public String getMaThongKe() {
        return maThongKe;
    }

    public void setMaThongKe(String maThongKe) {
        this.maThongKe = maThongKe;
    }

    public String getMaGiayChiTiet() {
        return maGiayChiTiet;
    }

    public void setMaGiayChiTiet(String maGiayChiTiet) {
        this.maGiayChiTiet = maGiayChiTiet;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public int getDoanhThu() {
        return doanhThu;
    }

    public void setDoanhThu(int doanhThu) {
        this.doanhThu = doanhThu;
    }

    public int getSoLuongDaBan() {
        return soLuongDaBan;
    }

    public void setSoLuongDaBan(int soLuongDaBan) {
        this.soLuongDaBan = soLuongDaBan;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public HashMap<String, Object> hashMapThongKe() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("maThongKe", this.maThongKe);
        data.put("maGiayChiTiet", this.maGiayChiTiet);
        data.put("ngay", this.ngay);
        data.put("doanhThu", this.doanhThu);
        data.put("soLuongDaBan", this.soLuongDaBan);

        return data;
    }
}
