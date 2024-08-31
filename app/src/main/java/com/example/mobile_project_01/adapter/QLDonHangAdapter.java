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
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_project_01.KHHienThiDonHangActivity;
import com.example.mobile_project_01.QLHienThiDonHangActivity;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.function.FormatCurrency;
import com.example.mobile_project_01.model.DonHang;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class QLDonHangAdapter extends RecyclerView.Adapter<QLDonHangAdapter.ViewHolder> {

    private final Context context;
    private List<DonHang> donHangList;

    private String maThanhVien;

    public QLDonHangAdapter(Context context, List<DonHang> donHangList, String maThanhVien) {
        this.context = context;
        this.donHangList = donHangList;
        this.maThanhVien = maThanhVien;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_ql_don_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DonHang donHang = donHangList.get(position);

        if (donHang != null) {

            holder.tvQLMaDonHang.setText("Mã đơn hàng: " + donHang.getMaDonHang());

            holder.tvQLTongThanhToan.setText("Tổng thanh toán: " + FormatCurrency.formatCurrency(donHang.getTongThanhToan()) + "₫");

            if (donHang.getTrangThaiDonHang() == 0 || donHang.getTrangThaiDonHang() == 6 || donHang.getTrangThaiDonHang() == 7) {
                holder.tvQLThoiGian.setText("Thời gian đặt hàng: " + donHang.getGioTao() + " " + donHang.getNgayTao());
            } else if (donHang.getTrangThaiDonHang() == 1) {
                holder.tvQLThoiGian.setText("Thời gian duyệt đơn: " + donHang.getGioDuyet() + " " + donHang.getNgayDuyet());
            } else if (donHang.getTrangThaiDonHang() == 2) {
                holder.tvQLThoiGian.setText("Thời gian giao vận chuyển: " + donHang.getGioGiaoVC() + " " + donHang.getNgayGiaoVC());
            } else if (donHang.getTrangThaiDonHang() == 3) {
                holder.tvQLThoiGian.setText("Thời gian giao khách hàng: " + donHang.getNgayGiaoKH() + " " + donHang.getNgayGiaoKH());
            } else if (donHang.getTrangThaiDonHang() == 4) {
                holder.tvQLThoiGian.setText("Thời gian hoàn thành : " + donHang.getGioHoanThanh() + " " + donHang.getNgayHoanThanh());
            }

            FirebaseFirestore.getInstance().collection("DonHangChiTiet")
                    .whereEqualTo("maDonHang", donHang.getMaDonHang())
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        int count = queryDocumentSnapshots.size();
                        holder.tvQLSoSanPham.setText("Số phân loại đặt: " + count);
                    });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, QLHienThiDonHangActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("donHang", donHang);
                    bundle.putString("maThanhVien", maThanhVien);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return donHangList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvQLMaDonHang, tvQLThoiGian, tvQLSoSanPham, tvQLTongThanhToan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQLMaDonHang = itemView.findViewById(R.id.tvQLMaDonHang);
            tvQLThoiGian = itemView.findViewById(R.id.tvQLThoiGian);
            tvQLSoSanPham = itemView.findViewById(R.id.tvQLSoSanPham);
            tvQLTongThanhToan = itemView.findViewById(R.id.tvQLTongThanhToan);
        }
    }
}
