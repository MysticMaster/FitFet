package com.example.mobile_project_01.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;

public class ThongBao implements Serializable {
    private String maThongBao, maKhachHang, maDonHang, noiDung;
    private String gioTaoTB, ngayTaoTB;

    private int loaiTB, trangThaiTB;

    private LocalDateTime localDateTime;

    public ThongBao() {
    }

    public ThongBao(String maThongBao, String maKhachHang, String maDonHang, String noiDung, String gioTaoTB, String ngayTaoTB, int loaiTB, int trangThaiTB) {
        this.maThongBao = maThongBao;
        this.maKhachHang = maKhachHang;
        this.maDonHang = maDonHang;
        this.noiDung = noiDung;
        this.gioTaoTB = gioTaoTB;
        this.ngayTaoTB = ngayTaoTB;
        this.loaiTB = loaiTB;
        this.trangThaiTB = trangThaiTB;
    }

    public ThongBao(String maThongBao, String maKhachHang, String noiDung, String gioTaoTB, String ngayTaoTB, int trangThaiTB) {
        this.maThongBao = maThongBao;
        this.maKhachHang = maKhachHang;
        this.noiDung = noiDung;
        this.gioTaoTB = gioTaoTB;
        this.ngayTaoTB = ngayTaoTB;
        this.trangThaiTB = trangThaiTB;
    }

    public String getMaThongBao() {
        return maThongBao;
    }

    public void setMaThongBao(String maThongBao) {
        this.maThongBao = maThongBao;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(String maDonHang) {
        this.maDonHang = maDonHang;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getGioTaoTB() {
        return gioTaoTB;
    }

    public void setGioTaoTB(String gioTaoTB) {
        this.gioTaoTB = gioTaoTB;
    }

    public String getNgayTaoTB() {
        return ngayTaoTB;
    }

    public void setNgayTaoTB(String ngayTaoTB) {
        this.ngayTaoTB = ngayTaoTB;
    }

    public int getLoaiTB() {
        return loaiTB;
    }

    public void setLoaiTB(int loaiTB) {
        this.loaiTB = loaiTB;
    }

    public int getTrangThaiTB() {
        return trangThaiTB;
    }

    public void setTrangThaiTB(int trangThaiTB) {
        this.trangThaiTB = trangThaiTB;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public HashMap<String, Object> hashMapTB() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("maThongBao", this.maThongBao);
        data.put("maKhachHang", this.maKhachHang);
        data.put("maDonHang", this.maDonHang);
        data.put("noiDung", this.noiDung);
        data.put("gioTaoTB", this.gioTaoTB);
        data.put("ngayTaoTB", this.ngayTaoTB);
        data.put("loaiTB", this.loaiTB);
        data.put("trangThaiTB", this.trangThaiTB);

        return data;
    }
}
