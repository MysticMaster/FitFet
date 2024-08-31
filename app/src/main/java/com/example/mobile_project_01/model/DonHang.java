package com.example.mobile_project_01.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;

public class DonHang implements Serializable {
    private String maDonHang, maThanhVien, maKhachHang, gioTao, ngayTao, gioDuyet, ngayDuyet;
    private String gioGiaoVC, ngayGiaoVC, gioGiaoKH, ngayGiaoKH, gioHoanThanh, ngayHoanThanh, gioHuyDon, ngayHuyDon;
    private String diaChi, hoTenNguoiNhan, sdtNguoiNhan, ghiChu;
    private int tongThanhToan, trangThaiDonHang;

    private LocalDateTime localDateTime;

    public DonHang() {
    }

    public DonHang(String maDonHang, String maThanhVien, String maKhachHang, String gioTao, String ngayTao, String gioDuyet, String ngayDuyet, String gioGiaoVC, String ngayGiaoVC, String gioGiaoKH, String ngayGiaoKH, String gioHoanThanh, String ngayHoanThanh, String gioHuyDon, String ngayHuyDon, String diaChi, String hoTenNguoiNhan, String sdtNguoiNhan, String ghiChu, int tongThanhToan, int trangThaiDonHang) {
        this.maDonHang = maDonHang;
        this.maThanhVien = maThanhVien;
        this.maKhachHang = maKhachHang;
        this.gioTao = gioTao;
        this.ngayTao = ngayTao;
        this.gioDuyet = gioDuyet;
        this.ngayDuyet = ngayDuyet;
        this.gioGiaoVC = gioGiaoVC;
        this.ngayGiaoVC = ngayGiaoVC;
        this.gioGiaoKH = gioGiaoKH;
        this.ngayGiaoKH = ngayGiaoKH;
        this.gioHoanThanh = gioHoanThanh;
        this.ngayHoanThanh = ngayHoanThanh;
        this.gioHuyDon = gioHuyDon;
        this.ngayHuyDon = ngayHuyDon;
        this.diaChi = diaChi;
        this.hoTenNguoiNhan = hoTenNguoiNhan;
        this.sdtNguoiNhan = sdtNguoiNhan;
        this.ghiChu = ghiChu;
        this.tongThanhToan = tongThanhToan;
        this.trangThaiDonHang = trangThaiDonHang;
    }

    public String getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(String maDonHang) {
        this.maDonHang = maDonHang;
    }

    public String getMaThanhVien() {
        return maThanhVien;
    }

    public void setMaThanhVien(String maThanhVien) {
        this.maThanhVien = maThanhVien;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getGioTao() {
        return gioTao;
    }

    public void setGioTao(String gioTao) {
        this.gioTao = gioTao;
    }

    public String getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }

    public String getGioDuyet() {
        return gioDuyet;
    }

    public void setGioDuyet(String gioDuyet) {
        this.gioDuyet = gioDuyet;
    }

    public String getNgayDuyet() {
        return ngayDuyet;
    }

    public void setNgayDuyet(String ngayDuyet) {
        this.ngayDuyet = ngayDuyet;
    }

    public String getGioGiaoVC() {
        return gioGiaoVC;
    }

    public void setGioGiaoVC(String gioGiaoVC) {
        this.gioGiaoVC = gioGiaoVC;
    }

    public String getNgayGiaoVC() {
        return ngayGiaoVC;
    }

    public void setNgayGiaoVC(String ngayGiaoVC) {
        this.ngayGiaoVC = ngayGiaoVC;
    }

    public String getGioGiaoKH() {
        return gioGiaoKH;
    }

    public void setGioGiaoKH(String gioGiaoKH) {
        this.gioGiaoKH = gioGiaoKH;
    }

    public String getNgayGiaoKH() {
        return ngayGiaoKH;
    }

    public void setNgayGiaoKH(String ngayGiaoKH) {
        this.ngayGiaoKH = ngayGiaoKH;
    }

    public String getGioHoanThanh() {
        return gioHoanThanh;
    }

    public void setGioHoanThanh(String gioHoanThanh) {
        this.gioHoanThanh = gioHoanThanh;
    }

    public String getNgayHoanThanh() {
        return ngayHoanThanh;
    }

    public void setNgayHoanThanh(String ngayHoanThanh) {
        this.ngayHoanThanh = ngayHoanThanh;
    }

    public String getGioHuyDon() {
        return gioHuyDon;
    }

    public void setGioHuyDon(String gioHuyDon) {
        this.gioHuyDon = gioHuyDon;
    }

    public String getNgayHuyDon() {
        return ngayHuyDon;
    }

    public void setNgayHuyDon(String ngayHuyDon) {
        this.ngayHuyDon = ngayHuyDon;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getHoTenNguoiNhan() {
        return hoTenNguoiNhan;
    }

    public void setHoTenNguoiNhan(String hoTenNguoiNhan) {
        this.hoTenNguoiNhan = hoTenNguoiNhan;
    }

    public String getSdtNguoiNhan() {
        return sdtNguoiNhan;
    }

    public void setSdtNguoiNhan(String sdtNguoiNhan) {
        this.sdtNguoiNhan = sdtNguoiNhan;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public int getTongThanhToan() {
        return tongThanhToan;
    }

    public void setTongThanhToan(int tongThanhToan) {
        this.tongThanhToan = tongThanhToan;
    }

    public int getTrangThaiDonHang() {
        return trangThaiDonHang;
    }

    public void setTrangThaiDonHang(int trangThaiDonHang) {
        this.trangThaiDonHang = trangThaiDonHang;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public HashMap<String, Object> hashMapDH() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("maDonHang", this.maDonHang);
        data.put("maThanhVien", this.maThanhVien);
        data.put("maKhachHang", this.maKhachHang);
        data.put("gioTao", this.gioTao);
        data.put("ngayTao", this.ngayTao);
        data.put("gioDuyet", this.gioDuyet);
        data.put("ngayDuyet", this.ngayDuyet);
        data.put("gioGiaoVC", this.gioGiaoVC);
        data.put("ngayGiaoVC", this.ngayGiaoVC);
        data.put("gioGiaoKH", this.gioGiaoKH);
        data.put("ngayGiaoKH", this.ngayGiaoKH);
        data.put("gioHoanThanh", this.gioHoanThanh);
        data.put("ngayHoanThanh", this.ngayHoanThanh);
        data.put("gioHuyDon", this.gioHuyDon);
        data.put("ngayHuyDon", this.ngayHuyDon);
        data.put("diaChi", this.diaChi);
        data.put("hoTenNguoiNhan", this.hoTenNguoiNhan);
        data.put("sdtNguoiNhan", this.sdtNguoiNhan);
        data.put("ghiChu", this.ghiChu);
        data.put("tongThanhToan", this.tongThanhToan);
        data.put("trangThaiDonHang", this.trangThaiDonHang);

        return data;
    }
}
