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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobile_project_01.DAO.BinhLuanDAO;
import com.example.mobile_project_01.DAO.PhanHoiDAO;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.model.BinhLuan;
import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.GiayChiTiet;
import com.example.mobile_project_01.model.KhachHang;
import com.example.mobile_project_01.model.PhanHoi;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class BinhLuanAdapter extends RecyclerView.Adapter<BinhLuanAdapter.ViewHolder> {

    private final Context context;
    private List<BinhLuan> binhLuanList;

    private PhanHoiAdapter adapter;
    private List<PhanHoi> phanHoiList;

    private Boolean checkClick = false;

    private String maKhachHang;

    public BinhLuanAdapter(Context context, List<BinhLuan> binhLuanList, String maKhachHang) {
        this.context = context;
        this.binhLuanList = binhLuanList;
        this.maKhachHang = maKhachHang;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_binh_luan, parent, false);
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
                        holder.ivAvatarKH.setImageResource(R.drawable.none_image);
                    } else {
                        Glide.with(context).load(khachHang.getAnhKhachHang()).into(holder.ivAvatarKH);
                    }
                    holder.tvTenKhachHang.setText(khachHang.getHoKhachHang() + " " + khachHang.getTenKhachHang());
                }
            });

            DocumentReference GiayReference = FirebaseFirestore.getInstance()
                    .collection("Giay").document(binhLuan.getMaGiay());
            GiayReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Giay giay = task.getResult().toObject(Giay.class);
                    holder.tvTenGiayBinhLuan.setText(giay.getTenGiay());
                }
            });

            holder.tvNoiDungBinhLuan.setText(binhLuan.getNoiDung());
            holder.edtEditBinhLuan.setText(binhLuan.getNoiDung());
            holder.tvThoiGianBinhLuan.setText(binhLuan.getGioBinhLuan() + " " + binhLuan.getNgayBinhLuan());

            phanHoiList = new ArrayList<>();
            adapter = new PhanHoiAdapter(context, phanHoiList, false);

            LinearLayoutManager manager = new LinearLayoutManager(context);
            holder.rvPhanHoi.setLayoutManager(manager);
            holder.rvPhanHoi.setAdapter(adapter);

            holder.tvHienThiPhanHoi.setOnClickListener(v -> {
                if (checkClick == false) {
                    checkClick = true;
                } else {
                    checkClick = false;
                }

                if (checkClick == true) {
                    holder.tvHienThiPhanHoi.setText("Phản hồi của FitFeet ▲");
                    holder.rvPhanHoi.setVisibility(View.VISIBLE);
                } else {
                    holder.tvHienThiPhanHoi.setText("Phản hồi của FitFeet ▼");
                    holder.rvPhanHoi.setVisibility(View.GONE);
                }
            });

            if (binhLuan.getMaKhachHang().equals(maKhachHang)) {
                holder.iBtnFunctionBinhLuan.setVisibility(View.VISIBLE);
            }

            holder.iBtnFunctionBinhLuan.setOnClickListener(v -> {
                showFunction(v, holder.edtEditBinhLuan, holder.tvNoiDungBinhLuan, holder.tvHuyEditBinhLuan,
                        holder.iBtnEditBinhLuan, binhLuan, holder.rlEditBinhLuan);
            });

            layDanhSachPhanHoi(binhLuan.getMaBinhLuan());
        }
    }

    @Override
    public int getItemCount() {
        return binhLuanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAvatarKH;

        ImageButton iBtnFunctionBinhLuan, iBtnEditBinhLuan;

        TextView tvTenKhachHang, tvTenGiayBinhLuan, tvNoiDungBinhLuan,
                tvThoiGianBinhLuan, tvHienThiPhanHoi, tvHuyEditBinhLuan;

        RelativeLayout rlEditBinhLuan;
        EditText edtEditBinhLuan;
        RecyclerView rvPhanHoi;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatarKH = itemView.findViewById(R.id.ivAvatarKH);
            tvTenKhachHang = itemView.findViewById(R.id.tvTenKhachHang);
            tvTenGiayBinhLuan = itemView.findViewById(R.id.tvTenGiayBinhLuan);
            tvNoiDungBinhLuan = itemView.findViewById(R.id.tvNoiDungBinhLuan);
            tvThoiGianBinhLuan = itemView.findViewById(R.id.tvThoiGianBinhLuan);
            tvHienThiPhanHoi = itemView.findViewById(R.id.tvHienThiPhanHoi);
            tvHuyEditBinhLuan = itemView.findViewById(R.id.tvHuyEditBinhLuan);
            rlEditBinhLuan = itemView.findViewById(R.id.rlEditBinhLuan);
            edtEditBinhLuan = itemView.findViewById(R.id.edtEditBinhLuan);
            rvPhanHoi = itemView.findViewById(R.id.rvPhanHoi);
            iBtnFunctionBinhLuan = itemView.findViewById(R.id.iBtnFunctionBinhLuan);
            iBtnEditBinhLuan = itemView.findViewById(R.id.iBtnEditBinhLuan);
        }
    }

    private void layDanhSachPhanHoi(String maBinhLuan) {
        FirebaseFirestore.getInstance().collection("PhanHoi")
                .whereEqualTo("maBinhLuan", maBinhLuan)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            PhanHoi phanHoi = snapshot.toObject(PhanHoi.class);
                            phanHoiList.add(phanHoi);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void showFunction(View view, EditText editText, TextView textView, TextView tvClick, ImageButton imageButton, BinhLuan binhLuan, RelativeLayout relativeLayout) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_extra_function, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.itemEdit) {
                relativeLayout.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                editBinhLuan(editText, textView, tvClick, imageButton, binhLuan, relativeLayout);
                return true;
            } else if (item.getItemId() == R.id.itemDelete) {
                xoaBinhLuan(binhLuan);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    private void editBinhLuan(EditText editText, TextView textView, TextView tvClick, ImageButton imageButton, BinhLuan binhLuan, RelativeLayout relativeLayout) {
        tvClick.setOnClickListener(v -> {
            relativeLayout.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        });

        imageButton.setOnClickListener(v -> {
            if (!editText.getText().toString().isEmpty()) {
                binhLuan.setNoiDung(editText.getText().toString());
                new BinhLuanDAO(context).capNhatBinhLuan(binhLuan);

                textView.setText(editText.getText().toString());

                relativeLayout.setVisibility(View.GONE);
                editText.setText(null);
                textView.setVisibility(View.VISIBLE);
            }
        });

    }

    private void xoaBinhLuan(BinhLuan binhLuan) {
        new BinhLuanDAO(context).xoaBinhLuan(binhLuan);

        PhanHoiDAO phanHoiDAO = new PhanHoiDAO(context);
        FirebaseFirestore.getInstance().collection("PhanHoi")
                .whereEqualTo("maBinhLuan", binhLuan.getMaBinhLuan())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                PhanHoi phanHoi = snapshot.toObject(PhanHoi.class);
                                phanHoiDAO.xoaPhanHoi(phanHoi);
                            }
                            notifyDataSetChanged();
                        } else {
                            notifyDataSetChanged();
                        }
                    }
                });
    }
}
