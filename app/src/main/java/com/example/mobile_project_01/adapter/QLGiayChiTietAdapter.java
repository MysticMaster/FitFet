package com.example.mobile_project_01.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobile_project_01.QLDanhSachBinhLuanActivity;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.fragment.BottomSheetDialogDetailGiayCTFragment;
import com.example.mobile_project_01.function.FormatCurrency;
import com.example.mobile_project_01.model.BinhLuan;
import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.GiayChiTiet;
import com.example.mobile_project_01.model.KichCoGiayCT;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QLGiayChiTietAdapter extends RecyclerView.Adapter<QLGiayChiTietAdapter.ViewHolder> {
    private final Context context;
    private List<GiayChiTiet> giayChiTietList;
    private FragmentManager fragmentManager;

    public QLGiayChiTietAdapter(Context context, List<GiayChiTiet> giayChiTietList, FragmentManager manager) {
        this.context = context;
        this.giayChiTietList = giayChiTietList;
        this.fragmentManager = manager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_ql_giay_chi_tiet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GiayChiTiet giayChiTiet = giayChiTietList.get(position);

        if (giayChiTiet != null) {

            if (giayChiTiet.getAnhGiayChiTiet().equals("0")) {
                holder.ivAnhGiayChiTietQL.setImageResource(R.drawable.none_image);
            } else {
                Glide.with(context).load(giayChiTiet.getAnhGiayChiTiet()).into(holder.ivAnhGiayChiTietQL);
            }

            holder.tvTenGiayChiTietQL.setText(giayChiTiet.getTenGiayChiTiet());
            holder.tvMauSacGiayChiTietQL.setText(giayChiTiet.getMauSac());
            holder.tvGiaGiayChiTietQL.setText(FormatCurrency.formatCurrency(giayChiTiet.getGia()) + "₫");

            holder.iBtnFunctionQLGiayCT.setOnClickListener(v -> {
                showFunction(v, giayChiTiet);
            });
        }
    }

    @Override
    public int getItemCount() {
        return giayChiTietList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAnhGiayChiTietQL;
        TextView tvTenGiayChiTietQL, tvMauSacGiayChiTietQL, tvGiaGiayChiTietQL;

        ImageButton iBtnFunctionQLGiayCT;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAnhGiayChiTietQL = itemView.findViewById(R.id.ivAnhGiayChiTietQL);
            tvTenGiayChiTietQL = itemView.findViewById(R.id.tvTenGiayChiTietQL);
            tvMauSacGiayChiTietQL = itemView.findViewById(R.id.tvMauSacGiayChiTietQL);
            tvGiaGiayChiTietQL = itemView.findViewById(R.id.tvGiaGiayChiTietQL);
            iBtnFunctionQLGiayCT = itemView.findViewById(R.id.iBtnFunctionQLGiayCT);
        }
    }

    private void showFunction(View view, GiayChiTiet giayChiTiet) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_function_ql_giay_ct, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.itemDetailGiayCT) {
                showDetail(giayChiTiet);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    private void showDetail(GiayChiTiet giayChiTiet) {
        FirebaseFirestore.getInstance().collection("KichCoGiayChiTiet")
                .whereEqualTo("maGiayCT", giayChiTiet.getMaGiayChiTiet())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<KichCoGiayCT> readList = new ArrayList<>();

                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            readList.add(snapshot.toObject(KichCoGiayCT.class));
                        }

                        Collections.sort(readList, Comparator.comparingInt(KichCoGiayCT::getKichCo));

                        giayChiTiet.setListKichCo(readList);
                        BottomSheetDialogDetailGiayCTFragment dialog = new BottomSheetDialogDetailGiayCTFragment(giayChiTiet, 0);
                        dialog.show(fragmentManager, dialog.getTag());
                    }
                });
    }


}
