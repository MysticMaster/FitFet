package com.example.mobile_project_01.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
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
import com.example.mobile_project_01.QLGiayChiTietActivity;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.fragment.BottomSheetDialogDetailGiayFragment;
import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.ThuongHieu;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class QLGiayAdapter extends RecyclerView.Adapter<QLGiayAdapter.ViewHolder> {
    private final Context context;
    private List<Giay> giayList;

    private FragmentManager fragmentManager;

    public QLGiayAdapter(Context context, List<Giay> giayList, FragmentManager fragmentManager) {
        this.context = context;
        this.giayList = giayList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_ql_giay, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Giay giay = giayList.get(position);
        if (giay != null) {
            holder.tvTenGiayQL.setText(giay.getTenGiay());

            DocumentReference reference = FirebaseFirestore.getInstance()
                    .collection("ThuongHieu").document(giay.getMaThuongHieu());
            reference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ThuongHieu thuongHieu = task.getResult().toObject(ThuongHieu.class);
                    holder.tvTenThuongHieuQL.setText(thuongHieu.getTenThuongHieu());
                }
            });

            if (giay.getAnhGiay().equals("0")) {
                holder.ivAnhGiayQL.setImageResource(R.drawable.none_image);
            } else {
                Glide.with(context).load(giay.getAnhGiay()).into(holder.ivAnhGiayQL);
            }

            holder.iBtnFunctionQLGiay.setOnClickListener(v -> {
                showFunction(v, giay);
            });
        }

    }

    @Override
    public int getItemCount() {
        return giayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAnhGiayQL;
        TextView tvTenGiayQL, tvTenThuongHieuQL;
        ImageButton iBtnFunctionQLGiay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAnhGiayQL = itemView.findViewById(R.id.ivAnhGiayQL);
            tvTenGiayQL = itemView.findViewById(R.id.tvTenGiayQL);
            tvTenThuongHieuQL = itemView.findViewById(R.id.tvTenThuongHieuQL);
            iBtnFunctionQLGiay = itemView.findViewById(R.id.iBtnFunctionQLGiay);
        }
    }

    private void showFunction(View view, Giay giay) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_function_ql_giay, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.itemDetailGiay) {
                showDetail(giay);
                return true;
            } else if (item.getItemId() == R.id.itemDSGiayCT) {
                showDSGiayCT(giay);
                return true;
            } else if (item.getItemId() == R.id.itemDSBinhLuan) {
                showDSBinhLuan(giay);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    private void showDetail(Giay giay) {
        BottomSheetDialogDetailGiayFragment diaglog = new BottomSheetDialogDetailGiayFragment(giay);
        diaglog.show(fragmentManager, diaglog.getTag());
    }

    private void showDSGiayCT(Giay giay) {
        Intent intent = new Intent(context, QLGiayChiTietActivity.class);
        intent.putExtra("giay", giay);
        context.startActivity(intent);
    }

    private void showDSBinhLuan(Giay giay) {
        Intent intent = new Intent(context, QLDanhSachBinhLuanActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("giay", giay);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void fillSearch(List<Giay> searchList) {
        giayList.clear();
        giayList.addAll(searchList);
        notifyDataSetChanged();
    }

}
