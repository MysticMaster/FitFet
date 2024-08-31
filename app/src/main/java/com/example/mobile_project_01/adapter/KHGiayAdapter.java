package com.example.mobile_project_01.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobile_project_01.KHHienThiGiayActivity;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.function.FormatCurrency;
import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.GiayChiTiet;
import com.example.mobile_project_01.model.KhachHang;
import com.example.mobile_project_01.model.ThuongHieu;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Locale;

public class KHGiayAdapter extends RecyclerView.Adapter<KHGiayAdapter.ViewHolder> {

    private final Context context;
    private List<Giay> giayList;
    private KhachHang khachHang;

    public KHGiayAdapter(Context context, List<Giay> giayList, KhachHang khachHang) {
        this.context = context;
        this.giayList = giayList;
        this.khachHang = khachHang;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_giay, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Giay giay = giayList.get(position);

        if (giay != null) {

            holder.tvTenGiay.setText(giay.getTenGiay());

            DocumentReference reference = FirebaseFirestore.getInstance()
                    .collection("ThuongHieu").document(giay.getMaThuongHieu());
            reference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ThuongHieu thuongHieu = task.getResult().toObject(ThuongHieu.class);
                    holder.tvTenThuongHieu.setText(thuongHieu.getTenThuongHieu());
                }
            });

            if (giay.getAnhGiay().equals("0")) {
                holder.ivAnhGiay.setImageResource(R.drawable.none_image);
            } else {
                Glide.with(context).load(giay.getAnhGiay()).into(holder.ivAnhGiay);
            }

            FirebaseFirestore.getInstance().collection("GiayChiTiet")
                    .whereEqualTo("maGiay", giay.getMaGiay())
                    .orderBy("gia")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot snapshot = task.getResult();
                            if (snapshot != null && !snapshot.isEmpty()) {
                                GiayChiTiet minGiay = snapshot.getDocuments().get(0).toObject(GiayChiTiet.class);
                                GiayChiTiet maxGiay = snapshot.getDocuments().get(snapshot.size() - 1).toObject(GiayChiTiet.class);

                                String min = FormatCurrency.formatCurrency(minGiay.getGia()) + "₫";
                                String max = FormatCurrency.formatCurrency(maxGiay.getGia()) + "₫";

                                holder.tvKhoangGia.setText(min + " - " + max);
                            }
                        } else {
                            Log.e("Lỗi ", "Lỗi: " + task.getException());
                        }
                    });

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, KHHienThiGiayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("giay", giay);
                bundle.putSerializable("khachHang", khachHang);
                intent.putExtras(bundle);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return giayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAnhGiay;
        TextView tvTenGiay, tvTenThuongHieu, tvKhoangGia;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAnhGiay = itemView.findViewById(R.id.ivAnhGiay);
            tvTenGiay = itemView.findViewById(R.id.tvTenGiay);
            tvTenThuongHieu = itemView.findViewById(R.id.tvTenThuongHieu);
            tvKhoangGia = itemView.findViewById(R.id.tvKhoangGia);
        }
    }

}
