package com.example.mobile_project_01.model;

import java.io.Serializable;
import java.util.HashMap;

public class ThuongHieu implements Serializable {
    private String maThuongHieu, tenThuongHieu, logoThuongHieu;
    private int trangThaiThuongHieu;

    public ThuongHieu() {
    }

    public ThuongHieu(String maThuongHieu, String tenThuongHieu, String logoThuongHieu, int trangThaiThuongHieu) {
        this.maThuongHieu = maThuongHieu;
        this.tenThuongHieu = tenThuongHieu;
        this.logoThuongHieu = logoThuongHieu;
        this.trangThaiThuongHieu = trangThaiThuongHieu;
    }

    public String getMaThuongHieu() {
        return maThuongHieu;
    }

    public void setMaThuongHieu(String maThuongHieu) {
        this.maThuongHieu = maThuongHieu;
    }

    public String getTenThuongHieu() {
        return tenThuongHieu;
    }

    public void setTenThuongHieu(String tenThuongHieu) {
        this.tenThuongHieu = tenThuongHieu;
    }

    public String getLogoThuongHieu() {
        return logoThuongHieu;
    }

    public void setLogoThuongHieu(String logoThuongHieu) {
        this.logoThuongHieu = logoThuongHieu;
    }

    public int getTrangThaiThuongHieu() {
        return trangThaiThuongHieu;
    }

    public void setTrangThaiThuongHieu(int trangThaiThuongHieu) {
        this.trangThaiThuongHieu = trangThaiThuongHieu;
    }

    public HashMap<String, Object> hashMapThuongHieu() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("maThuongHieu", this.maThuongHieu);
        data.put("tenThuongHieu", this.tenThuongHieu);
        data.put("logoThuongHieu", this.logoThuongHieu);
        data.put("trangThaiThuongHieu", this.trangThaiThuongHieu);

        return data;
    }
}
