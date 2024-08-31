package com.example.mobile_project_01;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mobile_project_01.DAO.ThongBaoDAO;
import com.example.mobile_project_01.fragment.KHDonHangFragment;
import com.example.mobile_project_01.fragment.KHHomeFragment;
import com.example.mobile_project_01.fragment.KHQuanLyTaiKhoanFragment;
import com.example.mobile_project_01.fragment.ThongBaoFragment;
import com.example.mobile_project_01.model.DonHang;
import com.example.mobile_project_01.model.KhachHang;
import com.example.mobile_project_01.model.KichCoGiayCT;
import com.example.mobile_project_01.model.ThongBao;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KhachHangMainActivity extends AppCompatActivity {
    private KhachHang khachHang;
    private Boolean change = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khach_hang_main);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            khachHang = (KhachHang) bundle.getSerializable("khachHang");
            change = bundle.getBoolean("change");
        }

        BottomNavigationView bnvMain = findViewById(R.id.bnvMain);
        bnvMain.setItemIconTintList(null);

        if (savedInstanceState == null) {
            setSupportFragmentManager(new KHHomeFragment(khachHang));
        }

        if (change == true) {
            setSupportFragmentManager(new KHQuanLyTaiKhoanFragment(khachHang));
            MenuItem menuItem = bnvMain.getMenu().findItem(R.id.idTaiKhoan);
            menuItem.setChecked(true);
        }


        bnvMain.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.idHome) {
                setSupportFragmentManager(new KHHomeFragment(khachHang));
                return true;
            } else if (item.getItemId() == R.id.idDonHang) {
                setSupportFragmentManager(new KHDonHangFragment(khachHang.getMaKhachHang()));
                return true;
            } else if (item.getItemId() == R.id.idThongBao) {
                setSupportFragmentManager(new ThongBaoFragment(khachHang.getMaKhachHang()));
                return true;
            } else if (item.getItemId() == R.id.idTaiKhoan) {
                setSupportFragmentManager(new KHQuanLyTaiKhoanFragment(khachHang));
                return true;
            }
            return false;
        });
    }

    private void setSupportFragmentManager(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flMain, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có chắc muốn thoát ứng dụng?")
                .setPositiveButton("Có", (dialog, which) -> finish())
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show();
    }
}