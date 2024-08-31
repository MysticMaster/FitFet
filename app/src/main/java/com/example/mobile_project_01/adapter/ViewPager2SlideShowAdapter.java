package com.example.mobile_project_01.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_project_01.R;

public class ViewPager2SlideShowAdapter extends RecyclerView.Adapter<ViewPager2SlideShowAdapter.ViewHolder> {

    private Context context;
    private int[] arrImages;

    public ViewPager2SlideShowAdapter(Context context, int[] arrImages) {
        this.context = context;
        this.arrImages = arrImages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_slide_show, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ivSlideShow.setImageResource(arrImages[position]);
    }

    @Override
    public int getItemCount() {
        return arrImages.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSlideShow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSlideShow = itemView.findViewById(R.id.ivSlideShow);
        }
    }
}
