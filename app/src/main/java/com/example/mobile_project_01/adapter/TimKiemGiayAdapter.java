package com.example.mobile_project_01.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobile_project_01.KHHienThiGiayActivity;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.KhachHang;
import com.example.mobile_project_01.model.ThuongHieu;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TimKiemGiayAdapter extends RecyclerView.Adapter<TimKiemGiayAdapter.ViewHolder> {

    private final Context context;
    private List<Giay> giayList;
    private KhachHang khachHang;

    public TimKiemGiayAdapter(Context context, List<Giay> giayList, KhachHang khachHang) {
        this.context = context;
        this.giayList = giayList;
        this.khachHang = khachHang;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_tim_kiem_giay, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Giay giay = giayList.get(position);

        if (giay != null) {

            holder.tvTenGiayTimKiem.setText(giay.getTenGiay());

            DocumentReference reference = FirebaseFirestore.getInstance()
                    .collection("ThuongHieu").document(giay.getMaThuongHieu());
            reference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ThuongHieu thuongHieu = task.getResult().toObject(ThuongHieu.class);
                    holder.tvTenTHTimKiem.setText(thuongHieu.getTenThuongHieu());
                }
            });

            if (giay.getAnhGiay().equals("0")) {
                holder.ivAnhGiayTimKiem.setImageResource(R.drawable.none_image);
            } else {
                Glide.with(context).load(giay.getAnhGiay()).into(holder.ivAnhGiayTimKiem);
            }

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

        ImageView ivAnhGiayTimKiem;
        TextView tvTenGiayTimKiem, tvTenTHTimKiem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAnhGiayTimKiem = itemView.findViewById(R.id.ivAnhGiayTimKiem);
            tvTenGiayTimKiem = itemView.findViewById(R.id.tvTenGiayTimKiem);
            tvTenTHTimKiem = itemView.findViewById(R.id.tvTenTHTimKiem);
        }
    }

    public void fillSearch(List<Giay> searchList) {
        giayList.clear();
        giayList.addAll(searchList);
        notifyDataSetChanged();
    }

}
