package com.example.mobile_project_01.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_project_01.R;
import com.example.mobile_project_01.model.GiayChiTiet;
import com.example.mobile_project_01.model.ThongKe;
import com.example.mobile_project_01.model.ThuongHieu;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class LichSuBanAdapter extends RecyclerView.Adapter<LichSuBanAdapter.ViewHolder> {

    private final Context context;
    private List<ThongKe> thongKeList;

    public LichSuBanAdapter(Context context, List<ThongKe> thongKeList) {
        this.context = context;
        this.thongKeList = thongKeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_lich_su_ban_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThongKe thongKe = thongKeList.get(position);

        if (thongKe != null) {
            holder.tvNgayBan.setText("Ngày bán: " + thongKe.getNgay());

            DocumentReference reference = FirebaseFirestore.getInstance().collection("GiayChiTiet")
                    .document(thongKe.getMaGiayChiTiet());
            reference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    GiayChiTiet giayChiTiet = task.getResult().toObject(GiayChiTiet.class);
                    holder.tvTenGiayCTLS.setText("Phân loại: " + giayChiTiet.getTenGiayChiTiet());
                }
            });

            holder.tvSoLuongBan.setText("Đã bán: " + thongKe.getSoLuongDaBan() + " chiếc");
            holder.tvThuVe.setText("Thu về: " + thongKe.getDoanhThu() + "₫");
        }
    }

    @Override
    public int getItemCount() {
        return thongKeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNgayBan, tvTenGiayCTLS, tvSoLuongBan, tvThuVe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNgayBan = itemView.findViewById(R.id.tvNgayBan);
            tvTenGiayCTLS = itemView.findViewById(R.id.tvTenGiayCTLS);
            tvSoLuongBan = itemView.findViewById(R.id.tvSoLuongBan);
            tvThuVe = itemView.findViewById(R.id.tvThuVe);
        }
    }
}
