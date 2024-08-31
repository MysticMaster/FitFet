package com.example.mobile_project_01.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobile_project_01.DAO.BinhLuanDAO;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.fragment.BottomSheetDialogDetailBinhLuanFragment;
import com.example.mobile_project_01.model.BinhLuan;
import com.example.mobile_project_01.model.KhachHang;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class QLBinhLuanAdapter extends RecyclerView.Adapter<QLBinhLuanAdapter.ViewHolder> {

    private final Context context;
    private List<BinhLuan> binhLuanList;

    private FragmentManager fragmentManager;

    public QLBinhLuanAdapter(Context context, List<BinhLuan> binhLuanList, FragmentManager fragmentManager) {
        this.context = context;
        this.binhLuanList = binhLuanList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_ql_binh_luan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BinhLuan binhLuan = binhLuanList.get(position);

        if (binhLuan != null) {

            DocumentReference KHReference = FirebaseFirestore.getInstance()
                    .collection("KhachHang").document(binhLuan.getMaKhachHang());
            KHReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    KhachHang khachHang = task.getResult().toObject(KhachHang.class);
                    if (khachHang.getAnhKhachHang().equals("0")) {
                        holder.ivQLAvtKHBL.setImageResource(R.drawable.none_image);
                    } else {
                        Glide.with(context).load(khachHang.getAnhKhachHang()).into(holder.ivQLAvtKHBL);
                    }
                    holder.tvQLTenKHBL.setText(khachHang.getHoKhachHang() + " " + khachHang.getTenKhachHang());
                }
            });

            holder.tvQLNDBinhLuan.setText("Nội dung: " + binhLuan.getNoiDung());

            holder.iBtnFunctionQLBL.setOnClickListener(v -> {
                showFunction(v, binhLuan);
            });
        }

    }

    @Override
    public int getItemCount() {
        return binhLuanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivQLAvtKHBL;
        TextView tvQLTenKHBL, tvQLNDBinhLuan;

        ImageButton iBtnFunctionQLBL;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivQLAvtKHBL = itemView.findViewById(R.id.ivQLAvtKHBL);
            tvQLTenKHBL = itemView.findViewById(R.id.tvQLTenKHBL);
            tvQLNDBinhLuan = itemView.findViewById(R.id.tvQLNDBinhLuan);
            iBtnFunctionQLBL = itemView.findViewById(R.id.iBtnFunctionQLBL);
        }
    }

    private void showFunction(View view, BinhLuan binhLuan) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_function_ql_binh_luan, popupMenu.getMenu());

        MenuItem itemBLTichCuc = popupMenu.getMenu().findItem(R.id.itemBLTichCuc);
        MenuItem itemBLTieuCuc = popupMenu.getMenu().findItem(R.id.itemBLTieuCuc);

        if (binhLuan.getTrangThaiBinhLuan() == 0) {
            itemBLTichCuc.setVisible(false);
            itemBLTieuCuc.setVisible(true);
        } else if (binhLuan.getTrangThaiBinhLuan() == 1) {
            itemBLTichCuc.setVisible(true);
            itemBLTieuCuc.setVisible(false);
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.itemBLTichCuc) {
                thayDoiTrangThai(0, binhLuan);
                return true;
            } else if (item.getItemId() == R.id.itemBLTieuCuc) {
                thayDoiTrangThai(1, binhLuan);
                return true;
            } else if (item.getItemId() == R.id.itemPhanHoiBL) {
                phanHoi(binhLuan);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    private void thayDoiTrangThai(int status, BinhLuan binhLuan) {
        binhLuan.setTrangThaiBinhLuan(status);
        new BinhLuanDAO(context).capNhatBinhLuan(binhLuan);
        notifyDataSetChanged();
    }

    private void phanHoi(BinhLuan binhLuan) {
        BottomSheetDialogDetailBinhLuanFragment dialog = new BottomSheetDialogDetailBinhLuanFragment(binhLuan);
        dialog.show(fragmentManager, dialog.getTag());
    }

}
