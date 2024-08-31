package com.example.mobile_project_01.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_project_01.R;
import com.example.mobile_project_01.function.AutoStringID;
import com.example.mobile_project_01.model.GiayChiTiet;
import com.example.mobile_project_01.model.KichCoGiayCT;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KichCoPickUpAdapter extends RecyclerView.Adapter<KichCoPickUpAdapter.ViewHolder> {

    private final Context context;

    private GiayChiTiet giayChiTiet;
    private int integerList[];
    private List<KichCoGiayCT> dataList;

    private List<KichCoGiayCT> selectedItems = new ArrayList<>();

    private CardView cardView;

    private ImageButton imageButton;

    public KichCoPickUpAdapter(Context context, int[] integerList, CardView cardView) {
        this.context = context;
        this.integerList = integerList;
        this.cardView = cardView;
    }

    public KichCoPickUpAdapter(Context context, int[] integerList, List<KichCoGiayCT> dataList, GiayChiTiet giayChiTiet, ImageButton imageButton) {
        this.context = context;
        this.integerList = integerList;
        this.dataList = dataList;
        this.giayChiTiet = giayChiTiet;
        this.imageButton = imageButton;
    }

    public List<KichCoGiayCT> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<KichCoGiayCT> selectedItems) {
        this.selectedItems = selectedItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_lua_chon_kich_co, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int index = integerList[position];

        holder.tvKichCoPick.setText(String.valueOf(index));

        if (dataList != null) {
            for (KichCoGiayCT kichCoGiayCT : dataList) {
                if (kichCoGiayCT.getKichCo() == index) {
                    holder.cbKichCoPick.setChecked(true);
                    selectedItems.add(kichCoGiayCT);
                }
            }
        }

        holder.cbKichCoPick.setOnClickListener(v -> {
            holder.cbKichCoPick.setEnabled(false);
            if (!holder.cbKichCoPick.isChecked()) {
                KichCoGiayCT kichCoIndex = null;
                for (KichCoGiayCT kichCoGiayCT : selectedItems) {
                    if (kichCoGiayCT.getKichCo() == index) {
                        kichCoIndex = kichCoGiayCT;
                        break;
                    }
                }
                if (kichCoIndex != null) {
                    if (kichCoIndex.getSoLuong() != 0) {
                        Toast.makeText(context, "Cỡ " + kichCoIndex.getKichCo() + " còn "
                                + kichCoIndex.getSoLuong() + " chiếc, không thể loại bỏ", Toast.LENGTH_SHORT).show();
                        holder.cbKichCoPick.setChecked(true);
                    } else {
                        selectedItems.remove(kichCoIndex);
                    }
                }
            } else if (holder.cbKichCoPick.isChecked()) {
                if (giayChiTiet != null) {
                    selectedItems.add(new KichCoGiayCT(AutoStringID.createStringID("KCSPCT-"),
                            giayChiTiet.getMaGiayChiTiet(), index, 0));
                } else {
                    selectedItems.add(new KichCoGiayCT(AutoStringID.createStringID("KCSPCT-"),
                            "0", index, 0));
                }
            }

            if (cardView != null) {
                if (selectedItems.isEmpty()) {
                    cardView.setEnabled(false);
                } else if (!selectedItems.isEmpty()) {
                    cardView.setEnabled(true);
                }
            }

            if (imageButton != null) {
                if (selectedItems.isEmpty()) {
                    imageButton.setEnabled(false);
                } else if (!selectedItems.isEmpty()) {
                    imageButton.setEnabled(true);
                }
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    holder.cbKichCoPick.setEnabled(true);
                }
            }, 2000);

        });
    }

    @Override
    public int getItemCount() {
        return integerList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbKichCoPick;
        TextView tvKichCoPick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cbKichCoPick = itemView.findViewById(R.id.cbKichCoPick);
            tvKichCoPick = itemView.findViewById(R.id.tvKichCoPick);
        }
    }
}
