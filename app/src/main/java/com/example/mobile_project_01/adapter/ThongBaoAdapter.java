package com.example.mobile_project_01.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_project_01.DAO.ThongBaoDAO;
import com.example.mobile_project_01.KHHienThiDonHangActivity;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.model.DonHang;
import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.ThongBao;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.ViewHolder> {

    private final Context context;
    private List<ThongBao> thongBaoList;

    public ThongBaoAdapter(Context context, List<ThongBao> thongBaoList) {
        this.context = context;
        this.thongBaoList = thongBaoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_thong_bao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThongBao thongBao = thongBaoList.get(position);

        if (thongBao != null) {

            holder.tvNgayTaoTB.setText(thongBao.getNgayTaoTB());
            String loaiTB = null;
            if (thongBao.getLoaiTB() == 1) {
                loaiTB = "Đơn hàng của bạn đã được xác nhận";
            } else if (thongBao.getLoaiTB() == 3) {
                loaiTB = "Đơn hàng của bạn đã giao đến";
            }
            holder.tvLoaiTB.setText(loaiTB);

            holder.tvNoiDungTB.setText(thongBao.getNoiDung());

            FirebaseFirestore.getInstance().collection("DonHang")
                    .whereEqualTo("maDonHang", thongBao.getMaDonHang())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DonHang donHang = task.getResult().getDocuments().get(0).toObject(DonHang.class);
                            if (donHang.getTrangThaiDonHang() == 2 || donHang.getTrangThaiDonHang() == 3) {
                                holder.itemView.setOnClickListener(v -> {
                                    Intent intent = new Intent(context, KHHienThiDonHangActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("donHang", donHang);
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                });
                            }
                        }
                    });

            holder.itemView.setOnLongClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông báo");
                builder.setMessage("Xác nhận xóa thông báo");

                builder.setPositiveButton("Xác nhận", ((dialog, which) -> {
                    new ThongBaoDAO(context).xoaThongBao(thongBao);
                }));

                builder.setNegativeButton("Hủy", ((dialog, which) -> {

                }));

                AlertDialog dialog = builder.create();
                dialog.show();

                return false;
            });
        }
    }

    @Override
    public int getItemCount() {
        return thongBaoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNgayTaoTB, tvLoaiTB, tvNoiDungTB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNgayTaoTB = itemView.findViewById(R.id.tvNgayTaoTB);
            tvLoaiTB = itemView.findViewById(R.id.tvLoaiTB);
            tvNoiDungTB = itemView.findViewById(R.id.tvNoiDungTB);
        }
    }
}
