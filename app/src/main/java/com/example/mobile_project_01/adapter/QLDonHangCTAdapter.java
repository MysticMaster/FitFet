package com.example.mobile_project_01.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.fragment.BottomSheetDialogDetailGiayCTFragment;
import com.example.mobile_project_01.function.FormatCurrency;
import com.example.mobile_project_01.model.DonHangChiTiet;
import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.GiayChiTiet;
import com.example.mobile_project_01.model.KichCoGiayCT;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QLDonHangCTAdapter extends RecyclerView.Adapter<QLDonHangCTAdapter.ViewHolder> {

    private final Context context;
    private List<DonHangChiTiet> donHangChiTietList;
    private FragmentManager fragmentManager;

    public QLDonHangCTAdapter(Context context, List<DonHangChiTiet> donHangChiTietList, FragmentManager fragmentManager) {
        this.context = context;
        this.donHangChiTietList = donHangChiTietList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_don_hang_chi_tiet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DonHangChiTiet donHangChiTiet = donHangChiTietList.get(position);

        if (donHangChiTiet != null) {

            DocumentReference giayCTReference = FirebaseFirestore.getInstance()
                    .collection("GiayChiTiet")
                    .document(donHangChiTiet.getMaGiayChiTiet());
            giayCTReference.get().addOnCompleteListener(task -> {
                GiayChiTiet giayChiTiet = task.getResult().toObject(GiayChiTiet.class);
                if (giayChiTiet.getAnhGiayChiTiet().equals("0")) {
                    holder.ivAnhGiayCTDonHang.setImageResource(R.drawable.none_image);
                } else {
                    Glide.with(context).load(giayChiTiet.getAnhGiayChiTiet()).into(holder.ivAnhGiayCTDonHang);
                }

                FirebaseFirestore.getInstance().collection("Giay")
                        .document(giayChiTiet.getMaGiay()).get()
                        .addOnCompleteListener(task1 -> {
                            Giay giay = task1.getResult().toObject(Giay.class);
                            holder.tvTenGiayDonHang.setText(giay.getTenGiay());
                        });
                holder.tvPhanLoaiGiay.setText("Phân loại: " + giayChiTiet.getTenGiayChiTiet()
                        + " /Màu: " + giayChiTiet.getMauSac() + ", Cỡ " + donHangChiTiet.getKichCoMua());

                holder.tvDonGia.setText("₫" + FormatCurrency.formatCurrency(giayChiTiet.getGia()));
                holder.tvSoLuongMua.setText("x" + donHangChiTiet.getSoLuongMua());


                holder.itemView.setOnClickListener(v -> {
                    FirebaseFirestore.getInstance().collection("KichCoGiayChiTiet")
                            .whereEqualTo("maGiayCT", giayChiTiet.getMaGiayChiTiet())
                            .get()
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    List<KichCoGiayCT> readList = new ArrayList<>();

                                    for (QueryDocumentSnapshot snapshot : task1.getResult()) {
                                        readList.add(snapshot.toObject(KichCoGiayCT.class));
                                    }

                                    BottomSheetDialogDetailGiayCTFragment dialog =
                                            new BottomSheetDialogDetailGiayCTFragment(giayChiTiet, 1);
                                    dialog.show(fragmentManager, dialog.getTag());
                                }
                            });
                });
            });
        }
    }

    @Override
    public int getItemCount() {
        return donHangChiTietList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAnhGiayCTDonHang;
        TextView tvTenGiayDonHang, tvPhanLoaiGiay, tvDonGia, tvSoLuongMua;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAnhGiayCTDonHang = itemView.findViewById(R.id.ivAnhGiayCTDonHang);
            tvTenGiayDonHang = itemView.findViewById(R.id.tvTenGiayDonHang);
            tvPhanLoaiGiay = itemView.findViewById(R.id.tvPhanLoaiGiay);
            tvDonGia = itemView.findViewById(R.id.tvDonGia);
            tvSoLuongMua = itemView.findViewById(R.id.tvSoLuongMua);
        }
    }
}
