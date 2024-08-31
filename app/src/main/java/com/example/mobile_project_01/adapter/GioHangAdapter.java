package com.example.mobile_project_01.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobile_project_01.DAO.GioHangDAO;
import com.example.mobile_project_01.QLHienThiDonHangActivity;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.fragment.BottomSheetDialogPickUpFragment;
import com.example.mobile_project_01.function.FormatCurrency;
import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.GiayChiTiet;
import com.example.mobile_project_01.model.GioHang;
import com.example.mobile_project_01.model.KichCoGiayCT;
import com.example.mobile_project_01.widget.QuantityNumberPicker;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.ViewHolder> {

    private final Context context;
    private List<GioHang> gioHangList;

    private List<GioHang> selectedItems = new ArrayList<>();

    private FragmentManager fragmentManager;

    private TextView submit, tongTien;

    public GioHangAdapter(Context context, List<GioHang> gioHangList, TextView submit, TextView tongTien, FragmentManager fragmentManager) {
        this.context = context;
        this.gioHangList = gioHangList;
        this.submit = submit;
        this.tongTien = tongTien;
        this.fragmentManager = fragmentManager;
    }

    public List<GioHang> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<GioHang> selectedItems) {
        this.selectedItems = selectedItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_gio_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        if (gioHang != null) {

            DocumentReference giayCTReference = FirebaseFirestore.getInstance()
                    .collection("GiayChiTiet")
                    .document(gioHang.getMaGiayChiTiet());
            giayCTReference.get().addOnCompleteListener(task -> {
                GiayChiTiet giayChiTiet = task.getResult().toObject(GiayChiTiet.class);
                if (giayChiTiet.getAnhGiayChiTiet().equals("0")) {
                    holder.ivAnhGiayCTGioHang.setImageResource(R.drawable.none_image);
                } else {
                    Glide.with(context).load(giayChiTiet.getAnhGiayChiTiet()).into(holder.ivAnhGiayCTGioHang);
                }

                FirebaseFirestore.getInstance().collection("Giay")
                        .document(giayChiTiet.getMaGiay())
                        .get()
                        .addOnCompleteListener(task1 -> {
                            Giay giay = task1.getResult().toObject(Giay.class);
                            holder.tvTenGiayGioHang.setText(giay.getTenGiay());

                            holder.iBtnFunctionGioHang.setOnClickListener(v -> {
                                showFunction(v, gioHang, giay);
                            });
                        });

                holder.tvPhanLoaiGiayGioHang.setText("Phân loại: " + giayChiTiet.getTenGiayChiTiet()
                        + " /Màu: " + giayChiTiet.getMauSac() + ", Cỡ " + gioHang.getKichCoMua());

                holder.tvDonGiaGiayCTGioHang.setText("₫" + FormatCurrency.formatCurrency(giayChiTiet.getGia()));
                holder.qnpSoLuongGioHang.setQuantity(gioHang.getSoLuongMua());

                CollectionReference reference = FirebaseFirestore.getInstance().collection("KichCoGiayChiTiet");
                reference.whereEqualTo("maGiayCT", gioHang.getMaGiayChiTiet())
                        .whereEqualTo("kichCo", gioHang.getKichCoMua())
                        .get()
                        .addOnCompleteListener(task1 -> {
                            KichCoGiayCT kichCoGiayCT = task1.getResult()
                                    .getDocuments().get(0).toObject(KichCoGiayCT.class);
                            holder.tvSLGiayCTGioHang.setText("Còn lại: " + kichCoGiayCT.getSoLuong());
                            holder.qnpSoLuongGioHang.setMaxQuantity(kichCoGiayCT.getSoLuong());

                            holder.qnpSoLuongGioHang.setOnQuantityChangeListener(newQuantity -> {
                                gioHang.setSoLuongMua(newQuantity);
                                gioHang.setTongTien(gioHang.getSoLuongMua() * giayChiTiet.getGia());
                                new GioHangDAO(context).capNhatGioHang(gioHang);
                            });
                        });

                holder.cbGioHangPick.setOnClickListener(v -> {
                    holder.cbGioHangPick.setEnabled(false);

                    if (!holder.cbGioHangPick.isChecked()) {
                        selectedItems.remove(gioHang);
                        holder.qnpSoLuongGioHang.setVisibility(View.VISIBLE);
                        holder.iBtnFunctionGioHang.setVisibility(View.VISIBLE);
                    } else if (holder.cbGioHangPick.isChecked()) {
                        selectedItems.add(gioHang);
                        holder.qnpSoLuongGioHang.setVisibility(View.INVISIBLE);
                        holder.iBtnFunctionGioHang.setVisibility(View.INVISIBLE);
                    }

                    if (submit != null) {
                        if (selectedItems.isEmpty()) {
                            submit.setAlpha(0.4f);
                            submit.setEnabled(false);
                        } else {
                            submit.setAlpha(1f);
                            submit.setEnabled(true);
                        }
                    }

                    if (tongTien != null) {
                        if (selectedItems.isEmpty()) {
                            tongTien.setText("₫0");
                        } else {
                            int count = 0;
                            for (int i = 0; i < selectedItems.size(); i++) {
                                count += selectedItems.get(i).getTongTien();
                            }
                            tongTien.setText("₫" + FormatCurrency.formatCurrency(count));
                        }
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.cbGioHangPick.setEnabled(true);
                        }
                    }, 1500);

                });

            });
        }
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox cbGioHangPick;

        ImageView ivAnhGiayCTGioHang;
        TextView tvTenGiayGioHang, tvPhanLoaiGiayGioHang, tvDonGiaGiayCTGioHang, tvSLGiayCTGioHang;

        ImageButton iBtnFunctionGioHang;
        QuantityNumberPicker qnpSoLuongGioHang;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cbGioHangPick = itemView.findViewById(R.id.cbGioHangPick);
            ivAnhGiayCTGioHang = itemView.findViewById(R.id.ivAnhGiayCTGioHang);
            tvTenGiayGioHang = itemView.findViewById(R.id.tvTenGiayGioHang);
            tvPhanLoaiGiayGioHang = itemView.findViewById(R.id.tvPhanLoaiGiayGioHang);
            tvDonGiaGiayCTGioHang = itemView.findViewById(R.id.tvDonGiaGiayCTGioHang);
            tvSLGiayCTGioHang = itemView.findViewById(R.id.tvSLGiayCTGioHang);
            iBtnFunctionGioHang = itemView.findViewById(R.id.iBtnFunctionGioHang);
            qnpSoLuongGioHang = itemView.findViewById(R.id.qnpSoLuongGioHang);
        }
    }

    private void showFunction(View view, GioHang gioHang, Giay giay) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_function_gio_hang, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.itemDoiSanPham) {
                BottomSheetDialogPickUpFragment dialog = new BottomSheetDialogPickUpFragment(0, giay, gioHang);
                dialog.show(fragmentManager, dialog.getTag());
                return true;
            } else if (item.getItemId() == R.id.itemXoaGioHang) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông báo");
                builder.setMessage("Xác nhận xóa sản phẩm khỏi giỏ hàng");

                builder.setPositiveButton("Xác nhận", ((dialog, which) -> {
                    new GioHangDAO(context).xoaGioHang(gioHang);
                    notifyDataSetChanged();
                }));

                builder.setNegativeButton("Hủy", ((dialog, which) -> {

                }));

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
            return false;
        });

        popupMenu.show();
    }
}
