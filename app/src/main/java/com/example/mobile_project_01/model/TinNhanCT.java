package com.example.mobile_project_01.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;

public class TinNhanCT implements Serializable {
    private String maTinNhanCT, maTinNhan, maNguoiGui, noiDung;
    private String gioTaoTN, ngayTaoTN;

    private LocalDateTime localDateTime;

    public TinNhanCT() {
    }

    public TinNhanCT(String maTinNhanCT, String maTinNhan, String maNguoiGui, String noiDung, String gioTaoTN, String ngayTaoTN) {
        this.maTinNhanCT = maTinNhanCT;
        this.maTinNhan = maTinNhan;
        this.maNguoiGui = maNguoiGui;
        this.noiDung = noiDung;
        this.gioTaoTN = gioTaoTN;
        this.ngayTaoTN = ngayTaoTN;
    }

    public String getMaTinNhanCT() {
        return maTinNhanCT;
    }

    public void setMaTinNhanCT(String maTinNhanCT) {
        this.maTinNhanCT = maTinNhanCT;
    }

    public String getMaTinNhan() {
        return maTinNhan;
    }

    public void setMaTinNhan(String maTinNhan) {
        this.maTinNhan = maTinNhan;
    }

    public String getMaNguoiGui() {
        return maNguoiGui;
    }

    public void setMaNguoiGui(String maNguoiGui) {
        this.maNguoiGui = maNguoiGui;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getGioTaoTN() {
        return gioTaoTN;
    }

    public void setGioTaoTN(String gioTaoTN) {
        this.gioTaoTN = gioTaoTN;
    }

    public String getNgayTaoTN() {
        return ngayTaoTN;
    }

    public void setNgayTaoTN(String ngayTaoTN) {
        this.ngayTaoTN = ngayTaoTN;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public HashMap<String, Object> hashMapTNCT() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("maTinNhanCT", this.maTinNhanCT);
        data.put("maTinNhan", this.maTinNhan);
        data.put("maNguoiGui", this.maNguoiGui);
        data.put("noiDung", this.noiDung);
        data.put("gioTaoTN", this.gioTaoTN);
        data.put("ngayTaoTN", this.ngayTaoTN);

        return data;
    }
}
