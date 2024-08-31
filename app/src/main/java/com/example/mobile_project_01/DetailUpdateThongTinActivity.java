package com.example.mobile_project_01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobile_project_01.DAO.KhachHangDAO;
import com.example.mobile_project_01.DAO.ThanhVienDAO;
import com.example.mobile_project_01.fragment.BottomSheetDialogForgotPassword;
import com.example.mobile_project_01.model.KhachHang;
import com.example.mobile_project_01.model.ThanhVien;
import com.google.firebase.auth.FirebaseAuth;

public class DetailUpdateThongTinActivity extends AppCompatActivity {

    private KhachHang khachHang;
    private ThanhVien thanhVien;
    private int loaiUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_update_thong_tin);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            khachHang = (KhachHang) bundle.getSerializable("khachHang");
            thanhVien = (ThanhVien) bundle.getSerializable("thanhVien");
            loaiUpdate = bundle.getInt("loaiUpdate");
        }

        ImageButton iBtnThoatHTThongTin = findViewById(R.id.iBtnThoatHTThongTin);
        TextView tvLoaiUpdateThongTin = findViewById(R.id.tvLoaiUpdateThongTin);
        TextView tvSaveUpdateThongTin = findViewById(R.id.tvSaveUpdateThongTin);

        LinearLayout llDetailHoTen = findViewById(R.id.llDetailHoTen);
        EditText edtDetailHo = findViewById(R.id.edtDetailHo);
        EditText edtDetailTen = findViewById(R.id.edtDetailTen);

        LinearLayout llDetailSDTKH = findViewById(R.id.llDetailSDTKH);
        EditText edtDetailSDTKH = findViewById(R.id.edtDetailSDTKH);

        LinearLayout llDetailDiaChiKH = findViewById(R.id.llDetailDiaChiKH);
        EditText edtDetailDiaChiKH = findViewById(R.id.edtDetailDiaChiKH);

        LinearLayout llDetailMatKhau = findViewById(R.id.llDetailMatKhau);
        EditText edtDetailMKCu = findViewById(R.id.edtDetailMKCu);
        EditText edtDetailMKMoi = findViewById(R.id.edtDetailMKMoi);
        EditText edtDetailMKXacNhan = findViewById(R.id.edtDetailMKXacNhan);
        TextView tvUpdateQuenMKCu = findViewById(R.id.tvUpdateQuenMKCu);

        if (loaiUpdate == 0) {
            llDetailHoTen.setVisibility(View.VISIBLE);
            tvLoaiUpdateThongTin.setText("Chỉnh sửa họ và tên");
        } else if (loaiUpdate == 1) {
            llDetailSDTKH.setVisibility(View.VISIBLE);
            tvLoaiUpdateThongTin.setText("Chỉnh sửa số điện thoại");
        } else if (loaiUpdate == 2) {
            llDetailDiaChiKH.setVisibility(View.VISIBLE);
            tvLoaiUpdateThongTin.setText("Chỉnh sửa địa chỉ");
        } else if (loaiUpdate == 3) {
            llDetailMatKhau.setVisibility(View.VISIBLE);
            tvLoaiUpdateThongTin.setText("Đổi mật khẩu");
        }

        if (khachHang != null) {
            edtDetailHo.setText(khachHang.getHoKhachHang());
            edtDetailTen.setText(khachHang.getTenKhachHang());

            if (!khachHang.getSoDienThoai().equals("0")) {
                edtDetailSDTKH.setText(khachHang.getSoDienThoai());
            }

            if (!khachHang.getDiaChi().equals("0")) {
                edtDetailDiaChiKH.setText(khachHang.getDiaChi());
            }

            listenTextChange(edtDetailHo, tvSaveUpdateThongTin);
            listenTextChange(edtDetailTen, tvSaveUpdateThongTin);
            listenTextChange(edtDetailSDTKH, tvSaveUpdateThongTin);
            listenTextChange(edtDetailDiaChiKH, tvSaveUpdateThongTin);
            listenTextChange(edtDetailMKMoi, tvSaveUpdateThongTin);

            if (loaiUpdate == 0) {
                tvSaveUpdateThongTin.setOnClickListener(v -> {
                    if (edtDetailHo.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Trống họ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (edtDetailTen.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Trống tên", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    khachHang.setHoKhachHang(edtDetailHo.getText().toString());
                    khachHang.setTenKhachHang(edtDetailTen.getText().toString());
                    new KhachHangDAO(this).capNhatKhachHang(khachHang);
                    startKHMain(tvSaveUpdateThongTin, iBtnThoatHTThongTin, khachHang);
                });
            } else if (loaiUpdate == 1) {
                tvSaveUpdateThongTin.setOnClickListener(v -> {
                    if (!edtDetailSDTKH.getText().toString().isEmpty()) {
                        khachHang.setSoDienThoai(edtDetailSDTKH.getText().toString());
                        new KhachHangDAO(this).capNhatKhachHang(khachHang);
                        startKHMain(tvSaveUpdateThongTin, iBtnThoatHTThongTin, khachHang);
                    }
                });
            } else if (loaiUpdate == 2) {
                tvSaveUpdateThongTin.setOnClickListener(v -> {
                    if (!edtDetailDiaChiKH.getText().toString().isEmpty()) {
                        khachHang.setDiaChi(edtDetailDiaChiKH.getText().toString());
                        new KhachHangDAO(this).capNhatKhachHang(khachHang);
                        startKHMain(tvSaveUpdateThongTin, iBtnThoatHTThongTin, khachHang);
                    }
                });
            } else if (loaiUpdate == 3) {
                tvSaveUpdateThongTin.setOnClickListener(v -> {
                    if (edtDetailMKCu.getText().toString().trim().isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập mật khẩu cũ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (edtDetailMKMoi.getText().toString().trim().isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (edtDetailMKMoi.getText().toString().trim().length() < 8) {
                        Toast.makeText(this, "Mật khẩu mới phải hơn 8 ký tự", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (!edtDetailMKMoi.getText().toString().trim().matches(".*[a-zA-Z].*") ||
                            !edtDetailMKMoi.getText().toString().trim().matches(".*\\d.*")) {
                        Toast.makeText(this, "Mật khẩu phải bao gồm chữ và số", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (edtDetailMKXacNhan.getText().toString().trim().isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập mật khẩu xác nhận", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (!edtDetailMKXacNhan.getText().toString().trim().equals(edtDetailMKMoi.getText().toString().trim())) {
                        Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    FirebaseAuth auth = FirebaseAuth.getInstance();

                    String mkCu = edtDetailMKCu.getText().toString().trim();
                    auth.signInWithEmailAndPassword(auth.getCurrentUser().getEmail(), mkCu)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    String mkMoi = edtDetailMKMoi.getText().toString().trim();
                                    auth.getCurrentUser().updatePassword(mkMoi)
                                            .addOnCompleteListener(updateTask -> {
                                                if (updateTask.isSuccessful()) {
                                                    edtDetailMKCu.setText(null);
                                                    edtDetailMKMoi.setText(null);
                                                    edtDetailMKXacNhan.setText(null);
                                                    startKHMain(tvSaveUpdateThongTin, iBtnThoatHTThongTin, khachHang);
                                                } else {
                                                    Toast.makeText(this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                } else {
                                    Toast.makeText(this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                                }
                            });

                });
            }
        } else if (thanhVien != null) {
            edtDetailHo.setText(thanhVien.getHoThanhVien());
            edtDetailTen.setText(thanhVien.getTenThanhVien());

            listenTextChange(edtDetailHo, tvSaveUpdateThongTin);
            listenTextChange(edtDetailTen, tvSaveUpdateThongTin);
            listenTextChange(edtDetailMKMoi, tvSaveUpdateThongTin);

            if (loaiUpdate == 0) {
                tvSaveUpdateThongTin.setOnClickListener(v -> {
                    if (edtDetailHo.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Trống họ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (edtDetailTen.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Trống tên", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    thanhVien.setHoThanhVien(edtDetailHo.getText().toString());
                    thanhVien.setTenThanhVien(edtDetailTen.getText().toString());
                    new ThanhVienDAO(this).capNhatThanhVien(thanhVien);
                    startTVMain(tvSaveUpdateThongTin, iBtnThoatHTThongTin, thanhVien);
                });
            } else if (loaiUpdate == 3) {

                tvSaveUpdateThongTin.setOnClickListener(v -> {
                    if (edtDetailMKCu.getText().toString().trim().isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập mật khẩu cũ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (edtDetailMKMoi.getText().toString().trim().isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (edtDetailMKMoi.getText().toString().trim().length() < 8) {
                        Toast.makeText(this, "Mật khẩu mới phải hơn 8 ký tự", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (!edtDetailMKMoi.getText().toString().trim().matches(".*[a-zA-Z].*") ||
                            !edtDetailMKMoi.getText().toString().trim().matches(".*\\d.*")) {
                        Toast.makeText(this, "Mật khẩu phải bao gồm chữ và số", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (edtDetailMKXacNhan.getText().toString().trim().isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập mật khẩu xác nhận", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (!edtDetailMKXacNhan.getText().toString().trim().equals(edtDetailMKMoi.getText().toString().trim())) {
                        Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    FirebaseAuth auth = FirebaseAuth.getInstance();

                    String mkCu = edtDetailMKCu.getText().toString().trim();
                    auth.signInWithEmailAndPassword(auth.getCurrentUser().getEmail(), mkCu)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    String mkMoi = edtDetailMKMoi.getText().toString().trim();
                                    auth.getCurrentUser().updatePassword(mkMoi)
                                            .addOnCompleteListener(updateTask -> {
                                                if (updateTask.isSuccessful()) {
                                                    edtDetailMKCu.setText(null);
                                                    edtDetailMKMoi.setText(null);
                                                    edtDetailMKXacNhan.setText(null);
                                                    startTVMain(tvSaveUpdateThongTin, iBtnThoatHTThongTin, thanhVien);
                                                } else {
                                                    Toast.makeText(this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                } else {
                                    Toast.makeText(this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                                }
                            });
                });
            }
        }

        tvUpdateQuenMKCu.setOnClickListener(v -> {
            BottomSheetDialogForgotPassword forgotPassword = new BottomSheetDialogForgotPassword();
            forgotPassword.show(getSupportFragmentManager(), forgotPassword.getTag());
        });

        iBtnThoatHTThongTin.setOnClickListener(v -> {
            finish();
        });

    }

    private void startKHMain(TextView textView, ImageButton imageButton, KhachHang khachHang) {
        textView.setEnabled(false);
        textView.setTextColor(0x66000000);

        imageButton.setOnClickListener(v1 -> {
            Intent intent = new Intent(this, KhachHangMainActivity.class);
            Bundle sendBundle = new Bundle();
            sendBundle.putSerializable("khachHang", khachHang);
            sendBundle.putBoolean("change", true);
            intent.putExtras(sendBundle);
            startActivity(intent);
            finish();
        });
    }

    private void startTVMain(TextView textView, ImageButton imageButton, ThanhVien thanhVien) {
        textView.setEnabled(false);
        textView.setTextColor(0x66000000);

        imageButton.setOnClickListener(v1 -> {
            Intent intent = new Intent(this, ThanhVienMainActivity.class);
            Bundle sendBundle = new Bundle();
            sendBundle.putSerializable("thanhVien", thanhVien);
            sendBundle.putBoolean("change", true);
            intent.putExtras(sendBundle);
            startActivity(intent);
            finish();
        });
    }

    private void listenTextChange(EditText editText, TextView textView) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textView.setEnabled(true);
                textView.setTextColor(Color.BLACK);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
