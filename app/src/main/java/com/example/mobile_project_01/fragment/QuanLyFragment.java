package com.example.mobile_project_01.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mobile_project_01.QLGiayActivity;
import com.example.mobile_project_01.QLThuongHieuActivity;
import com.example.mobile_project_01.R;

import java.util.Calendar;


public class QuanLyFragment extends Fragment {

    public QuanLyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_q_l_giay, container, false);

        CardView cvQLGiay = view.findViewById(R.id.cvQLGiay);
        CardView cvQLThuongHieu = view.findViewById(R.id.cvQLThuongHieu);

        cvQLGiay.setOnClickListener(v -> {
            getActivity().startActivity(new Intent(getContext(), QLGiayActivity.class));
        });

        cvQLThuongHieu.setOnClickListener(v -> {
            getActivity().startActivity(new Intent(getContext(), QLThuongHieuActivity.class));
        });

        return view;
    }


}