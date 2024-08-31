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
import com.example.mobile_project_01.CSKHActivity;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.model.KhachHang;
import com.example.mobile_project_01.model.ThanhVien;
import com.example.mobile_project_01.model.TinNhan;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class QLTinNhanAdapter extends RecyclerView.Adapter<QLTinNhanAdapter.ViewHolder> {

    private final Context context;
    private List<TinNhan> tinNhanList;

    private ThanhVien thanhVien;

    public QLTinNhanAdapter(Context context, List<TinNhan> tinNhanList, ThanhVien thanhVien) {
        this.context = context;
        this.tinNhanList = tinNhanList;
        this.thanhVien = thanhVien;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_ql_tin_nhan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TinNhan tinNhan = tinNhanList.get(position);

        if (tinNhan != null) {

            DocumentReference reference = FirebaseFirestore.getInstance()
                    .collection("KhachHang").document(tinNhan.getMaKhachHang());
            reference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    KhachHang khachHang = task.getResult().toObject(KhachHang.class);
                    if (khachHang.getAnhKhachHang().equals("0")) {
                        holder.ivAnhKHTinNhan.setImageResource(R.drawable.none_image);
                    } else {
                        Glide.with(context).load(khachHang.getAnhKhachHang()).into(holder.ivAnhKHTinNhan);
                    }

                    holder.tvTenKHTinNhan.setText(khachHang.getHoKhachHang() + " " + khachHang.getTenKhachHang());
                }
            });

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, CSKHActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("maNguoiGui", thanhVien.getMaThanhVien());
                bundle.putString("maTinNhan", tinNhan.getMaTinNhan());
                bundle.putString("maKH",tinNhan.getMaKhachHang());
                intent.putExtras(bundle);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return tinNhanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAnhKHTinNhan;
        TextView tvTenKHTinNhan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAnhKHTinNhan = itemView.findViewById(R.id.ivAnhKHTinNhan);
            tvTenKHTinNhan = itemView.findViewById(R.id.tvTenKHTinNhan);
        }
    }
}
