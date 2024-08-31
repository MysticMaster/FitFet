package com.example.mobile_project_01;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobile_project_01.fragment.BottomSheetDialogForgotPassword;
import com.example.mobile_project_01.model.KhachHang;
import com.example.mobile_project_01.model.ThanhVien;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private int chonTK;

    private Boolean checkBoxSave = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bundle getBundle = getIntent().getExtras();
        if (getBundle != null) {
            checkBoxSave = getBundle.getBoolean("checkBoxSave");
        }

        TextInputLayout tilUser = findViewById(R.id.tilUser);
        TextInputEditText tiEdtUser = findViewById(R.id.tiEdtUser);
        TextInputLayout tilPass = findViewById(R.id.tilPass);
        TextInputEditText tiEdtPass = findViewById(R.id.tiEdtPass);
        ImageButton iBtnAnMK = findViewById(R.id.iBtnAnMK);
        ImageButton iBtnHienMK = findViewById(R.id.iBtnHienMK);
        CheckBox cbLuuTK = findViewById(R.id.cbLuuTK);
        Spinner spiLoaiTK = findViewById(R.id.spiLoaiTK);
        ProgressBar pbLogin = findViewById(R.id.pbLogin);
        CardView cvLogin = findViewById(R.id.cvLogin);
        TextView tvTimMatKhau = findViewById(R.id.tvTimMatKhau);
        TextView tvDangKy = findViewById(R.id.tvDangKy);

        if (checkBoxSave) {
            SharedPreferences preferences = getSharedPreferences("luuTaiKhoan", MODE_PRIVATE);
            if (preferences.getAll().size() > 0) {
                cbLuuTK.setChecked(false);
                tiEdtUser.setText("");
                tiEdtPass.setText("");

                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
            }
        }

        tiEdtUser.setOnTouchListener(((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                tilUser.setHelperText(null);
            }
            return false;
        }));

        tiEdtPass.setOnTouchListener(((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                tilPass.setHelperText(null);
            }
            return false;
        }));

        iBtnAnMK.setOnClickListener(v -> {
            iBtnHienMK.setVisibility(View.VISIBLE);
            iBtnAnMK.setVisibility(View.GONE);
            tiEdtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        });

        iBtnHienMK.setOnClickListener(v -> {
            iBtnHienMK.setVisibility(View.GONE);
            iBtnAnMK.setVisibility(View.VISIBLE);
            tiEdtPass.setTransformationMethod(null);
        });

        String[] loaiTK = {"Khách Hàng", "Thành Viên"};
        ArrayAdapter<String> loaiTKAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, loaiTK);
        loaiTKAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiLoaiTK.setAdapter(loaiTKAdapter);

        spiLoaiTK.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chonTK = position;
                if (chonTK == 1) {
                    tvDangKy.setVisibility(View.GONE);
                } else if (chonTK == 0) {
                    tvDangKy.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        layTaiKhoan(tiEdtUser, tiEdtPass, spiLoaiTK, cbLuuTK);

        cvLogin.setOnClickListener(v -> {
            Boolean checkUser = false;
            Boolean checkPass = false;

            if (tiEdtUser.getText().toString().trim().isEmpty()) {
                tilUser.setHelperText("Vui lòng nhập Email");
                checkUser = true;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(tiEdtUser.getText().toString().trim()).matches()) {
                tilUser.setHelperText("Email không hợp lệ");
                checkUser = true;
            }

            if (tiEdtPass.getText().toString().trim().isEmpty()) {
                tilPass.setHelperText("Vui lòng nhập mật khẩu");
                checkPass = true;
            }

            if (checkUser == true || checkPass == true) {
                return;
            }

            pbLogin.setVisibility(View.VISIBLE);
            cvLogin.setVisibility(View.INVISIBLE);

            String email = tiEdtUser.getText().toString().trim();
            String pass = tiEdtPass.getText().toString().trim();

            FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(LoginActivity.this, task1 -> {
                        if (task1.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String maTaiKhoan = firebaseUser.getUid();

                            if (chonTK == 0) {
                                FirebaseFirestore.getInstance().collection("KhachHang")
                                        .whereEqualTo("maTaiKhoan", maTaiKhoan)
                                        .get()
                                        .addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful()) {
                                                QuerySnapshot snapshot = task2.getResult();
                                                if (snapshot.isEmpty()) {
                                                    pbLogin.setVisibility(View.INVISIBLE);
                                                    cvLogin.setVisibility(View.VISIBLE);
                                                    Toast.makeText(LoginActivity.this, "Tài khoản không phải là khách hàng", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    KhachHang khachHang = null;
                                                    for (QueryDocumentSnapshot documentSnapshot : snapshot) {
                                                        khachHang = documentSnapshot.toObject(KhachHang.class);
                                                    }

                                                    if (khachHang.getTrangThaiKhachHang() == 0) {
                                                        Intent intent = new Intent(LoginActivity.this, KhachHangMainActivity.class);
                                                        Bundle bundle = new Bundle();
                                                        bundle.putSerializable("khachHang", khachHang);
                                                        intent.putExtras(bundle);

                                                        luuTaiKhoan(email, pass, chonTK, cbLuuTK.isChecked());
                                                        if (!cbLuuTK.isChecked()) {
                                                            tiEdtPass.setText("");
                                                        }

                                                        startActivity(intent);
                                                        finish();
                                                    } else if (khachHang.getTrangThaiKhachHang() == 1) {
                                                        pbLogin.setVisibility(View.INVISIBLE);
                                                        cvLogin.setVisibility(View.VISIBLE);
                                                        Toast.makeText(LoginActivity.this, "Tài khoản đã bị khóa", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            } else {
                                                Toast.makeText(LoginActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }

                            if (chonTK == 1) {
                                FirebaseFirestore.getInstance().collection("ThanhVien")
                                        .whereEqualTo("maTaiKhoan", maTaiKhoan)
                                        .get()
                                        .addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful()) {
                                                QuerySnapshot snapshot = task2.getResult();
                                                if (snapshot.isEmpty()) {
                                                    pbLogin.setVisibility(View.INVISIBLE);
                                                    cvLogin.setVisibility(View.VISIBLE);
                                                    Toast.makeText(LoginActivity.this, "Tài khoản không phải là thành viên, Vui lòng liên hệ ADMIN để được hỗ trợ", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    ThanhVien thanhVien = null;
                                                    for (QueryDocumentSnapshot documentSnapshot : snapshot) {
                                                        thanhVien = documentSnapshot.toObject(ThanhVien.class);
                                                    }

                                                    if (thanhVien.getTrangThaiThanhVien() == 0) {
                                                        Intent intent = new Intent(LoginActivity.this, ThanhVienMainActivity.class);
                                                        Bundle bundle = new Bundle();
                                                        bundle.putSerializable("thanhVien", thanhVien);
                                                        intent.putExtras(bundle);

                                                        luuTaiKhoan(email, pass, chonTK, cbLuuTK.isChecked());
                                                        if (!cbLuuTK.isChecked()) {
                                                            tiEdtPass.setText("");
                                                        }

                                                        startActivity(intent);
                                                        finish();
                                                    } else if (thanhVien.getTrangThaiThanhVien() == 1) {
                                                        pbLogin.setVisibility(View.INVISIBLE);
                                                        cvLogin.setVisibility(View.VISIBLE);
                                                        Toast.makeText(LoginActivity.this, "Tài khoản đã bị khóa", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            } else {
                                                Toast.makeText(LoginActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }

                        } else {
                            pbLogin.setVisibility(View.INVISIBLE);
                            cvLogin.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "Thông tin tài khoản mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                        pbLogin.setVisibility(View.INVISIBLE);
                        cvLogin.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "Lỗi kết nối internet", Toast.LENGTH_SHORT).show();
                    });

        });

        tvTimMatKhau.setOnClickListener(v -> {
            BottomSheetDialogForgotPassword dialogForgotPassword = new BottomSheetDialogForgotPassword();
            dialogForgotPassword.show(getSupportFragmentManager(), dialogForgotPassword.getTag());
        });

        tvDangKy.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });
    }

    private void luuTaiKhoan(String email, String pass, int loaiTK, boolean check) {
        SharedPreferences preferences = getSharedPreferences("luuTaiKhoan", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.putString("pass", pass);
        editor.putInt("loaiTK", loaiTK);
        editor.putBoolean("check", check);
        editor.apply();
    }

    private void layTaiKhoan(TextInputEditText tiEdtEmail, TextInputEditText tiEdtPass, Spinner spiLoaiTK, CheckBox cbLuuTK) {
        SharedPreferences preferences = getSharedPreferences("luuTaiKhoan", MODE_PRIVATE);
        String email = preferences.getString("email", "");
        String pass = preferences.getString("pass", "");
        int loaiTK = preferences.getInt("loaiTK", -1);
        boolean check = preferences.getBoolean("check", false);

        cbLuuTK.setChecked(check);
        if (cbLuuTK.isChecked()) {
            tiEdtEmail.setText(email);
            tiEdtPass.setText(pass);
            spiLoaiTK.setSelection(loaiTK);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có chắc muốn thoát ứng dụng?")
                .setPositiveButton("Có", (dialog, which) -> finish())
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show();
    }
}