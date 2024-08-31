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
import com.example.mobile_project_01.QLGiayActivity;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.fragment.BottomSheetDialogDetailGiayFragment;
import com.example.mobile_project_01.fragment.BottomSheetDialogDetailThuongHieuFragment;
import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.ThuongHieu;

import java.util.List;

public class QLThuongHieuAdapter extends RecyclerView.Adapter<QLThuongHieuAdapter.ViewHolder> {

    private final Context context;
    private List<ThuongHieu> thuongHieuList;

    private FragmentManager fragmentManager;

    public QLThuongHieuAdapter(Context context, List<ThuongHieu> thuongHieuList, FragmentManager fragmentManager) {
        this.context = context;
        this.thuongHieuList = thuongHieuList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_ql_thuong_hieu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThuongHieu thuongHieu = thuongHieuList.get(position);

        if (thuongHieu != null) {

            holder.tvTenThuongHieuQL.setText(thuongHieu.getTenThuongHieu());

            if (thuongHieu.getLogoThuongHieu().equals("0")) {
                holder.ivAnhThuongHieuQL.setImageResource(R.drawable.none_image);
            } else {
                Glide.with(context).load(thuongHieu.getLogoThuongHieu()).into(holder.ivAnhThuongHieuQL);
            }

            holder.iBtnFunctionQLThuongHieu.setOnClickListener(v -> {
                showFunction(v, thuongHieu);
            });
        }
    }

    @Override
    public int getItemCount() {
        return thuongHieuList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAnhThuongHieuQL;
        TextView tvTenThuongHieuQL;
        ImageButton iBtnFunctionQLThuongHieu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAnhThuongHieuQL = itemView.findViewById(R.id.ivAnhThuongHieuQL);
            tvTenThuongHieuQL = itemView.findViewById(R.id.tvTenThuongHieuQL);
            iBtnFunctionQLThuongHieu = itemView.findViewById(R.id.iBtnFunctionQLThuongHieu);
        }
    }

    private void showFunction(View view, ThuongHieu thuongHieu) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_function_ql_thuong_hieu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.itemDetailThuongHieu) {
                showDetail(thuongHieu);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    private void showDetail(ThuongHieu thuongHieu) {
        BottomSheetDialogDetailThuongHieuFragment dialog = new BottomSheetDialogDetailThuongHieuFragment(thuongHieu);
        dialog.show(fragmentManager, dialog.getTag());
    }
}
