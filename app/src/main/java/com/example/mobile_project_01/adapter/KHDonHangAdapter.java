package com.example.mobile_project_01.adapter;

import android.app.Activity;
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

import com.example.mobile_project_01.DAO.DonHangDAO;
import com.example.mobile_project_01.KHHienThiDonHangActivity;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.function.FormatCurrency;
import com.example.mobile_project_01.model.DonHang;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class KHDonHangAdapter extends RecyclerView.Adapter<KHDonHangAdapter.ViewHolder> {

    private final Context context;
    private List<DonHang> donHangList;

    public KHDonHangAdapter(Context context, List<DonHang> donHangList) {
        this.context = context;
        this.donHangList = donHangList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_don_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DonHang donHang = donHangList.get(position);

        if (donHang != null) {

            holder.tvKHMaDonHang.setText("Mã đơn hàng: " + donHang.getMaDonHang());
            if (donHang.getTrangThaiDonHang() == 0) {
                holder.tvKHLoaiNgay.setText("Ngày đặt hàng: " + donHang.getNgayTao());
            } else if (donHang.getTrangThaiDonHang() == 1) {
                holder.tvKHLoaiNgay.setText("Ngày duyệt đơn: " + donHang.getNgayDuyet());
            } else if (donHang.getTrangThaiDonHang() == 2) {
                holder.tvKHLoaiNgay.setText("Ngày gửi: " + donHang.getNgayGiaoVC());
            } else if (donHang.getTrangThaiDonHang() == 3) {
                holder.tvKHLoaiNgay.setText("Ngày đến: " + donHang.getNgayGiaoKH());
            } else if (donHang.getTrangThaiDonHang() == 4) {
                holder.tvKHLoaiNgay.setText("Ngày nhận: " + donHang.getNgayHoanThanh());
            } else if (donHang.getTrangThaiDonHang() == 5) {
                holder.tvKHLoaiNgay.setText("Ngày hủy: " + donHang.getNgayHuyDon());
            }

            holder.tvKHTongThanhToan.setText("Tổng thanh toán: " + FormatCurrency.formatCurrency(donHang.getTongThanhToan()) + "₫");

            FirebaseFirestore.getInstance().collection("DonHangChiTiet")
                    .whereEqualTo("maDonHang", donHang.getMaDonHang()).get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        int count = queryDocumentSnapshots.size();
                        holder.tvKHSoSanPham.setText("Số phân loại đặt: " + count);
                    });

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, KHHienThiDonHangActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("donHang", donHang);
                intent.putExtras(bundle);
                context.startActivity(intent);
            });

            if (donHang.getTrangThaiDonHang() == 5) {
                holder.itemView.setOnLongClickListener(v -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Xác nhận xóa đơn hàng này");

                    builder.setPositiveButton("Xác nhận", ((dialog, which) -> {
                        new DonHangDAO(context).xoaDonHang(donHang);
                    }));

                    builder.setNegativeButton("Hủy", ((dialog, which) -> {

                    }));

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return false;
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return donHangList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvKHMaDonHang, tvKHLoaiNgay, tvKHSoSanPham, tvKHTongThanhToan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKHMaDonHang = itemView.findViewById(R.id.tvKHMaDonHang);
            tvKHLoaiNgay = itemView.findViewById(R.id.tvKHLoaiNgay);
            tvKHSoSanPham = itemView.findViewById(R.id.tvKHSoSanPham);
            tvKHTongThanhToan = itemView.findViewById(R.id.tvKHTongThanhToan);
        }
    }
}
