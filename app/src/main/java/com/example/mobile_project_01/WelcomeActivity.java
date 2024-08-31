package com.example.mobile_project_01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.mobile_project_01.DAO.ThongBaoDAO;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isConnectedToInternet()) {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Kết nối internet không khả dụng. Vui lòng kiểm tra Wi-Fi hoặc dữ liệu di động của bạn");

                    builder.setPositiveButton("Xác nhận", ((dialog, which) -> {
                        finish();
                    }));

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        }, 3000);

    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(WelcomeActivity.this.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }
}