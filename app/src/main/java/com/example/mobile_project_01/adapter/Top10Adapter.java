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
import com.example.mobile_project_01.model.GiayChiTiet;
import com.example.mobile_project_01.model.KhachHang;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Top10Adapter extends RecyclerView.Adapter<Top10Adapter.ViewHolder> {

    private final Context context;
    private List<GiayChiTiet> giayChiTietList;

    private KhachHang khachHang;

    public Top10Adapter(Context context, List<GiayChiTiet> giayChiTietList, KhachHang khachHang) {
        this.context = context;
        this.giayChiTietList = giayChiTietList;
        this.khachHang = khachHang;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_top_10, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GiayChiTiet giayChiTiet = giayChiTietList.get(position);
        if (giayChiTiet != null) {
            if (giayChiTiet.getAnhGiayChiTiet().equals("0")) {
                holder.ivAnhGiayTop1.setImageResource(R.drawable.none_image);
            } else {
                Glide.with(context).load(giayChiTiet.getAnhGiayChiTiet()).into(holder.ivAnhGiayTop1);
            }
            holder.tvTenGiayTop1.setText(giayChiTiet.getTenGiayChiTiet());

            FirebaseFirestore.getInstance().collection("Giay")
                    .whereEqualTo("maGiay", giayChiTiet.getMaGiay())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Giay giay = task.getResult().getDocuments().get(0).toObject(Giay.class);

                            holder.itemView.setOnClickListener(v -> {
                                Intent intent = new Intent(context, KHHienThiGiayActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("giay", giay);
                                bundle.putSerializable("khachHang", khachHang);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            });
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return giayChiTietList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAnhGiayTop1;
        TextView tvTenGiayTop1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAnhGiayTop1 = itemView.findViewById(R.id.ivAnhGiayTop1);
            tvTenGiayTop1 = itemView.findViewById(R.id.tvTenGiayTop1);
        }
    }
}
