package com.example.mobile_project_01.model;

import java.io.Serializable;
import java.util.HashMap;

public class ThanhVien implements Serializable {
    private String maThanhVien, hoThanhVien, tenThanhVien, emailThanhVien, anhThanhVien;
    private String maTaiKhoan;
    private int phanQuyen, trangThaiThanhVien;

    public ThanhVien() {
    }

    public ThanhVien(String maThanhVien, String hoThanhVien, String tenThanhVien, String emailThanhVien, String anhThanhVien, String maTaiKhoan, int phanQuyen, int trangThaiThanhVien) {
        this.maThanhVien = maThanhVien;
        this.hoThanhVien = hoThanhVien;
        this.tenThanhVien = tenThanhVien;
        this.emailThanhVien = emailThanhVien;
        this.anhThanhVien = anhThanhVien;
        this.maTaiKhoan = maTaiKhoan;
        this.phanQuyen = phanQuyen;
        this.trangThaiThanhVien = trangThaiThanhVien;
    }

    public String getMaThanhVien() {
        return maThanhVien;
    }

    public void setMaThanhVien(String maThanhVien) {
        this.maThanhVien = maThanhVien;
    }

    public String getHoThanhVien() {
        return hoThanhVien;
    }

    public void setHoThanhVien(String hoThanhVien) {
        this.hoThanhVien = hoThanhVien;
    }

    public String getTenThanhVien() {
        return tenThanhVien;
    }

    public void setTenThanhVien(String tenThanhVien) {
        this.tenThanhVien = tenThanhVien;
    }

    public String getEmailThanhVien() {
        return emailThanhVien;
    }

    public void setEmailThanhVien(String emailThanhVien) {
        this.emailThanhVien = emailThanhVien;
    }

    public String getAnhThanhVien() {
        return anhThanhVien;
    }

    public void setAnhThanhVien(String anhThanhVien) {
        this.anhThanhVien = anhThanhVien;
    }

    public String getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public void setMaTaiKhoan(String maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }

    public int getPhanQuyen() {
        return phanQuyen;
    }

    public void setPhanQuyen(int phanQuyen) {
        this.phanQuyen = phanQuyen;
    }

    public int getTrangThaiThanhVien() {
        return trangThaiThanhVien;
    }

    public void setTrangThaiThanhVien(int trangThaiThanhVien) {
        this.trangThaiThanhVien = trangThaiThanhVien;
    }

    public HashMap<String, Object> hashMapTV() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("maThanhVien", this.maThanhVien);
        data.put("hoThanhVien", this.hoThanhVien);
        data.put("tenThanhVien", this.tenThanhVien);
        data.put("emailThanhVien", this.emailThanhVien);
        data.put("anhThanhVien", this.anhThanhVien);
        data.put("maTaiKhoan", this.maTaiKhoan);
        data.put("phanQuyen", this.phanQuyen);
        data.put("trangThaiThanhVien", this.trangThaiThanhVien);

        return data;
    }
}
