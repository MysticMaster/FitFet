package com.example.mobile_project_01.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.model.ThuongHieu;

import java.util.List;

public class ThuongHieuAdapter extends RecyclerView.Adapter<ThuongHieuAdapter.ViewHolder> {

    private final Context context;
    private List<ThuongHieu> thuongHieuList;

    private onItemClick click;

    public ThuongHieuAdapter(Context context, List<ThuongHieu> thuongHieuList) {
        this.context = context;
        this.thuongHieuList = thuongHieuList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_thuong_hieu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThuongHieu thuongHieu = thuongHieuList.get(position);

        if (thuongHieu != null) {

            if (thuongHieu.getLogoThuongHieu().equals("0")) {
                holder.ivAnhThuongHieu.setImageResource(R.drawable.none_image);
            } else {
                Glide.with(context).load(thuongHieu.getLogoThuongHieu()).into(holder.ivAnhThuongHieu);
            }

            holder.tvHTTenThuongHieu.setText(thuongHieu.getTenThuongHieu());

            holder.itemView.setOnClickListener(v -> {
                if (click != null) {
                    click.onItemClick(thuongHieu);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return thuongHieuList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAnhThuongHieu;
        TextView tvHTTenThuongHieu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAnhThuongHieu = itemView.findViewById(R.id.ivAnhThuongHieu);
            tvHTTenThuongHieu = itemView.findViewById(R.id.tvHTTenThuongHieu);

        }
    }

    public interface onItemClick {
        void onItemClick(ThuongHieu thuongHieu);
    }

    public void setOnItemClickListener(onItemClick listener) {
        click = listener;
    }
}
