package com.example.mobile_project_01.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.fragment.BottomSheetDialogDetailGiayCTFragment;
import com.example.mobile_project_01.model.GiayChiTiet;
import com.example.mobile_project_01.model.KichCoGiayCT;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class KHGiayCTAdapter extends RecyclerView.Adapter<KHGiayCTAdapter.ViewHolder> {

    private final Context context;
    private List<GiayChiTiet> giayChiTietList;

    private onItemClick click;

    public int selectedPosition = RecyclerView.NO_POSITION;

    public KHGiayCTAdapter(Context context, List<GiayChiTiet> giayChiTietList) {
        this.context = context;
        this.giayChiTietList = giayChiTietList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_giay_chi_tiet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GiayChiTiet giayChiTiet = giayChiTietList.get(position);

        if (giayChiTiet != null) {

            if (giayChiTiet.getAnhGiayChiTiet().equals("0")) {
                holder.ivAnhGiayChiTiet.setImageResource(R.drawable.none_image);
            } else {
                Glide.with(context).load(giayChiTiet.getAnhGiayChiTiet()).into(holder.ivAnhGiayChiTiet);
            }

            if (selectedPosition == position) {
                holder.itemView.setBackgroundColor(Color.RED);
            } else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }

            holder.itemView.setOnClickListener(v -> {
                if (selectedPosition != position) {
                    notifyItemChanged(selectedPosition);
                    selectedPosition = position;
                    notifyItemChanged(selectedPosition);
                }
                if (click != null) {
                    GiayChiTiet giayCT = giayChiTietList.get(position);

                    FirebaseFirestore.getInstance().collection("KichCoGiayChiTiet")
                            .whereEqualTo("maGiayCT", giayCT.getMaGiayChiTiet())
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    List<KichCoGiayCT> readList = new ArrayList<>();

                                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                        readList.add(snapshot.toObject(KichCoGiayCT.class));
                                    }
                                    Collections.sort(readList, Comparator.comparingInt(KichCoGiayCT::getKichCo));
                                    giayCT.setListKichCo(readList);

                                    click.onItemClick(giayCT);
                                }
                            });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return giayChiTietList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAnhGiayChiTiet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAnhGiayChiTiet = itemView.findViewById(R.id.ivAnhGiayChiTiet);
        }
    }

    public interface onItemClick {
        void onItemClick(GiayChiTiet giayChiTiet);
    }

    public void setOnItemClickListener(onItemClick listener) {
        click = listener;
    }
}
