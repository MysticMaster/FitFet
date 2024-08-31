package com.example.mobile_project_01.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_project_01.DAO.TinNhanCTDAO;
import com.example.mobile_project_01.QLHienThiDonHangActivity;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.model.TinNhanCT;

import java.util.List;

public class TinNhanCTAdapter extends RecyclerView.Adapter<TinNhanCTAdapter.ViewHolder> {

    private final Context context;

    private List<TinNhanCT> tinNhanCTList;

    private String maNguoiGui;

    public TinNhanCTAdapter(Context context, List<TinNhanCT> tinNhanCTList, String maNguoiGui) {
        this.context = context;
        this.tinNhanCTList = tinNhanCTList;
        this.maNguoiGui = maNguoiGui;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;

        if (viewType == 0) {
            view = inflater.inflate(R.layout.item_rv_tin_nhan_phai, parent, false);
        } else {
            view = inflater.inflate(R.layout.item_rv_tin_nhan_trai, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TinNhanCT tinNhanCT = tinNhanCTList.get(position);

        if (tinNhanCT != null) {

            holder.tvNDTinNhan.setText(tinNhanCT.getNoiDung());
            holder.tvThoiGianTNCT.setText(tinNhanCT.getGioTaoTN() + " " + tinNhanCT.getNgayTaoTN());

            holder.itemView.setOnClickListener(v -> {
                if (holder.tvThoiGianTNCT.getVisibility() == View.GONE) {
                    holder.tvThoiGianTNCT.setVisibility(View.VISIBLE);
                } else {
                    holder.tvThoiGianTNCT.setVisibility(View.GONE);
                }
            });

            if (tinNhanCT.getMaNguoiGui().equals(maNguoiGui)) {
                holder.itemView.setOnLongClickListener(v -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Gỡ tin nhắn");

                    builder.setPositiveButton("Gỡ", ((dialog, which) -> {
                        new TinNhanCTDAO(context).xoaTinNhanCT(tinNhanCT);
                    }));

                    builder.setNegativeButton("Không", ((dialog, which) -> {

                    }));

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return false;
                });
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return tinNhanCTList.get(position).getMaNguoiGui().equals(maNguoiGui) ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        return tinNhanCTList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNDTinNhan, tvThoiGianTNCT;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNDTinNhan = itemView.findViewById(R.id.tvNDTinNhan);
            tvThoiGianTNCT = itemView.findViewById(R.id.tvThoiGianTNCT);
        }
    }
}
