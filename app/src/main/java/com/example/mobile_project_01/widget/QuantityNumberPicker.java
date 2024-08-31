package com.example.mobile_project_01.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobile_project_01.R;

public class QuantityNumberPicker extends LinearLayout {
    private EditText edtQuantity;

    private OnQuantityChangeListener onQuantityChangeListener;

    private int quantity = 0;
    private int maxQuantity;


    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        edtQuantity.setText(String.valueOf(quantity));
    }

    public QuantityNumberPicker(Context context) {
        super(context);
    }

    public QuantityNumberPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.quantity_number_picker, this, true);

        ImageButton iBtnAddition = view.findViewById(R.id.iBtnAddition);
        ImageButton iBtnSubtraction = view.findViewById(R.id.iBtnSubtraction);
        edtQuantity = view.findViewById(R.id.edtQuantity);

        iBtnAddition.setOnClickListener(v -> {
            iBtnAddition.setEnabled(false);
            iBtnAddition.setAlpha(0.5f);
            tang();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    iBtnAddition.setEnabled(true);
                    iBtnAddition.setAlpha(1f);
                }
            }, 800);
        });

        iBtnSubtraction.setOnClickListener(v -> {
            iBtnSubtraction.setEnabled(false);
            iBtnSubtraction.setAlpha(0.5f);
            giam();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    iBtnSubtraction.setEnabled(true);
                    iBtnSubtraction.setAlpha(1f);
                }
            }, 1500);
        });

    }

    private void tang() {
        if (quantity < maxQuantity) {
            quantity++;
            edtQuantity.setText(String.valueOf(quantity));
            if (onQuantityChangeListener != null) {
                onQuantityChangeListener.onQuantityChanged(quantity);
            }
        }
    }

    private void giam() {
        if (quantity > 0) {
            quantity--;
            edtQuantity.setText(String.valueOf(quantity));
            if (onQuantityChangeListener != null) {
                onQuantityChangeListener.onQuantityChanged(quantity);
            }
        }
    }

    public interface OnQuantityChangeListener {
        void onQuantityChanged(int newQuantity);
    }

    public void setOnQuantityChangeListener(OnQuantityChangeListener listener) {
        this.onQuantityChangeListener = listener;
    }

}
