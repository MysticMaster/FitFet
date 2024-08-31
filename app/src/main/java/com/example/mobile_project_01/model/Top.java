package com.example.mobile_project_01.model;

import java.io.Serializable;

public class Top implements Serializable {
    private String maGiayCT;
    private int count;

    public Top() {
    }

    public Top(String maGiayCT, int count) {
        this.maGiayCT = maGiayCT;
        this.count = count;
    }

    public String getMaGiayCT() {
        return maGiayCT;
    }

    public void setMaGiayCT(String maGiayCT) {
        this.maGiayCT = maGiayCT;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
