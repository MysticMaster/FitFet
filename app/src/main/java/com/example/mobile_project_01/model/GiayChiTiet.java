package com.example.mobile_project_01.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class GiayChiTiet implements Serializable {
    private String maGiayChiTiet, maGiay, tenGiayChiTiet, mauSac, anhGiayChiTiet;
    private List<KichCoGiayCT> listKichCo;
    private int gia, trangThaiGiayChiTiet;

    public GiayChiTiet() {
    }

    public GiayChiTiet(String maGiayChiTiet, String maGiay, String tenGiayChiTiet, String mauSac, String anhGiayChiTiet, int gia, int trangThaiGiayChiTiet) {
        this.maGiayChiTiet = maGiayChiTiet;
        this.maGiay = maGiay;
        this.tenGiayChiTiet = tenGiayChiTiet;
        this.mauSac = mauSac;
        this.anhGiayChiTiet = anhGiayChiTiet;
        this.gia = gia;
        this.trangThaiGiayChiTiet = trangThaiGiayChiTiet;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public String getMaGiayChiTiet() {
        return maGiayChiTiet;
    }

    public void setMaGiayChiTiet(String maGiayChiTiet) {
        this.maGiayChiTiet = maGiayChiTiet;
    }

    public String getMaGiay() {
        return maGiay;
    }

    public void setMaGiay(String maGiay) {
        this.maGiay = maGiay;
    }

    public String getTenGiayChiTiet() {
        return tenGiayChiTiet;
    }

    public void setTenGiayChiTiet(String tenGiayChiTiet) {
        this.tenGiayChiTiet = tenGiayChiTiet;
    }

    public String getMauSac() {
        return mauSac;
    }

    public void setMauSac(String mauSac) {
        this.mauSac = mauSac;
    }

    public String getAnhGiayChiTiet() {
        return anhGiayChiTiet;
    }

    public void setAnhGiayChiTiet(String anhGiayChiTiet) {
        this.anhGiayChiTiet = anhGiayChiTiet;
    }

    public List<KichCoGiayCT> getListKichCo() {
        return listKichCo;
    }

    public void setListKichCo(List<KichCoGiayCT> listKichCo) {
        this.listKichCo = listKichCo;
    }

    public int getTrangThaiGiayChiTiet() {
        return trangThaiGiayChiTiet;
    }

    public void setTrangThaiGiayChiTiet(int trangThaiGiayChiTiet) {
        this.trangThaiGiayChiTiet = trangThaiGiayChiTiet;
    }

    public HashMap<String, Object> hashMapGiayCT() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("maGiayChiTiet", this.maGiayChiTiet);
        data.put("maGiay", this.maGiay);
        data.put("tenGiayChiTiet", this.tenGiayChiTiet);
        data.put("mauSac", this.mauSac);
        data.put("anhGiayChiTiet", this.anhGiayChiTiet);
        data.put("gia", this.gia);
        data.put("trangThaiGiayChiTiet", this.trangThaiGiayChiTiet);

        return data;
    }

}
