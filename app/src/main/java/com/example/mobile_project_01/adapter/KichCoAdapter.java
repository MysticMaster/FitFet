package com.example.mobile_project_01.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_project_01.R;
import com.example.mobile_project_01.model.KichCoGiayCT;

import java.util.List;

public class KichCoAdapter extends RecyclerView.Adapter<KichCoAdapter.ViewHolder> {

    private List<KichCoGiayCT> list;
    private final Context context;

    private onItemClick click;

    public int selectedPosition = RecyclerView.NO_POSITION;

    public int status = -1;

    public int index = -1;

    public KichCoAdapter(List<KichCoGiayCT> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_kich_co, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KichCoGiayCT kichCo = list.get(position);

        if (kichCo != null) {

            holder.tvKichCo.setText(String.valueOf(kichCo.getKichCo()));

            if (selectedPosition == position) {
                holder.itemView.setBackgroundColor(Color.RED);
            } else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }

            if (status == 0) {
                holder.itemView.setEnabled(false);
            }

            if (index != -1) {
                if (kichCo.getKichCo() == index) {
                    holder.itemView.setBackgroundColor(Color.RED);
                }
            }

            holder.itemView.setOnClickListener(v -> {
                if (selectedPosition != position) {
                    notifyItemChanged(selectedPosition);
                    selectedPosition = position;
                    notifyItemChanged(selectedPosition);
                }
                if (click != null) {
                    click.onItemClick(list.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvKichCo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKichCo = itemView.findViewById(R.id.tvKichCo);
        }
    }

    public interface onItemClick {
        void onItemClick(KichCoGiayCT kichCoGiayCT);
    }

    public void setOnItemClickListener(onItemClick listener) {
        click = listener;
    }
}
