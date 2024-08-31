package com.example.mobile_project_01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mobile_project_01.DAO.KhachHangDAO;
import com.example.mobile_project_01.function.AutoStringID;
import com.example.mobile_project_01.model.KhachHang;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextInputLayout tilHoSignUp = findViewById(R.id.tilHoSignUp);
        TextInputEditText tiEdtHoSignUp = findViewById(R.id.tiEdtHoSignUp);

        TextInputLayout tilTenSignUp = findViewById(R.id.tilTenSignUp);
        TextInputEditText tiEdtTenSignUp = findViewById(R.id.tiEdtTenSignUp);

        TextInputLayout tilEmailSignUp = findViewById(R.id.tilEmailSignUp);
        TextInputEditText tiEdtEmailSignUp = findViewById(R.id.tiEdtEmailSignUp);

        TextInputLayout tilPassSignUp = findViewById(R.id.tilPassSignUp);
        TextInputEditText tiEdtPassSignUp = findViewById(R.id.tiEdtPassSignUp);
        ImageButton iBtnAnPassSignUp = findViewById(R.id.iBtnAnPassSignUp);
        ImageButton iBtnHienPassSignUp = findViewById(R.id.iBtnHienPassSignUp);

        TextInputLayout tilPassCon = findViewById(R.id.tilPassCon);
        TextInputEditText tiEdtPassCon = findViewById(R.id.tiEdtPassCon);
        ImageButton iBtnAnPassCon = findViewById(R.id.iBtnAnPassCon);
        ImageButton iBtnHienPassCon = findViewById(R.id.iBtnHienPassCon);

        ProgressBar pbSignUp = findViewById(R.id.pbSignUp);
        CardView cvSignUpSubmit = findViewById(R.id.cvSignUpSubmit);
        LinearLayout llExitSignUp = findViewById(R.id.llExitSignUp);

        tiEdtHoSignUp.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                tilHoSignUp.setHelperText(null);
            }
            return false;
        });

        tiEdtTenSignUp.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                tilTenSignUp.setHelperText(null);
            }
            return false;
        });

        tiEdtEmailSignUp.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                tilEmailSignUp.setHelperText(null);
            }
            return false;
        });

        tiEdtPassSignUp.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                tilPassSignUp.setHelperText(null);
            }
            return false;
        });

        tiEdtPassCon.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                tilPassCon.setHelperText(null);
            }
            return false;
        });

        iBtnAnPassSignUp.setOnClickListener(v -> {
            iBtnHienPassSignUp.setVisibility(View.VISIBLE);
            iBtnAnPassSignUp.setVisibility(View.GONE);
            tiEdtPassSignUp.setTransformationMethod(PasswordTransformationMethod.getInstance());
        });

        iBtnHienPassSignUp.setOnClickListener(v -> {
            iBtnHienPassSignUp.setVisibility(View.GONE);
            iBtnAnPassSignUp.setVisibility(View.VISIBLE);
            tiEdtPassSignUp.setTransformationMethod(null);
        });

        iBtnAnPassCon.setOnClickListener(v -> {
            iBtnHienPassCon.setVisibility(View.VISIBLE);
            iBtnAnPassCon.setVisibility(View.GONE);
            tiEdtPassCon.setTransformationMethod(PasswordTransformationMethod.getInstance());
        });

        iBtnHienPassCon.setOnClickListener(v -> {
            iBtnHienPassCon.setVisibility(View.GONE);
            iBtnAnPassCon.setVisibility(View.VISIBLE);
            tiEdtPassCon.setTransformationMethod(null);
        });

        cvSignUpSubmit.setOnClickListener(v -> {
            Boolean checkHo = false;
            Boolean checkTen = false;
            Boolean checkEmail = false;
            Boolean checkPass = false;
            Boolean checkPassCon = false;

            if (tiEdtHoSignUp.getText().toString().isEmpty()) {
                tilHoSignUp.setHelperText("Trống họ");
                checkHo = true;
            } else if (tiEdtHoSignUp.getText().toString().length() < 2) {
                tilHoSignUp.setHelperText("Họ phải trên 2 ký tự");
                checkHo = true;
            } else if (tiEdtHoSignUp.getText().toString().matches(".*\\d.*")) {
                tilHoSignUp.setHelperText("Họ không được chứa số");
                checkHo = true;
            }

            if (tiEdtTenSignUp.getText().toString().isEmpty()) {
                tilTenSignUp.setHelperText("Trống tên");
                checkTen = true;
            } else if (tiEdtTenSignUp.getText().toString().length() < 2) {
                tilTenSignUp.setHelperText("Tên phải trên 2 ký tự");
                checkTen = true;
            } else if (tiEdtTenSignUp.getText().toString().matches(".*\\d.*")) {
                tilTenSignUp.setHelperText("Tên không được chứa số");
                checkTen = true;
            }

            if (tiEdtEmailSignUp.getText().toString().trim().isEmpty()) {
                tilEmailSignUp.setHelperText("Trống email");
                checkEmail = true;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(tiEdtEmailSignUp.getText().toString().trim()).matches()) {
                tilEmailSignUp.setHelperText("Email không hợp lệ");
                checkEmail = true;
            } else if (tiEdtEmailSignUp.getText().toString().trim().length() > 50) {
                tilEmailSignUp.setHelperText("Email quá dài");
                checkEmail = true;
            }

            if (tiEdtPassSignUp.getText().toString().trim().isEmpty()) {
                tilPassSignUp.setHelperText("Trống mật khẩu");
                checkPass = true;
            } else if (tiEdtPassSignUp.getText().toString().trim().length() < 8) {
                tilPassSignUp.setHelperText("Mật khẩu phải hơn 8 ký tự");
                checkPass = true;
            } else if (!tiEdtPassSignUp.getText().toString().trim().matches(".*[a-zA-Z].*") ||
                    !tiEdtPassSignUp.getText().toString().trim().matches(".*\\d.*")) {
                tilPassSignUp.setHelperText("Mật khẩu phải bao gồm chữ và số");
                checkPass = true;
            }

            if (tiEdtPassCon.getText().toString().trim().isEmpty()) {
                tilPassCon.setHelperText("Trống mật khẩu xác nhận");
                checkPassCon = true;
            } else if (!tiEdtPassCon.getText().toString().trim().equals(tiEdtPassSignUp.getText().toString().trim())) {
                tilPassCon.setHelperText("Mậtt khẩu xác nhận không khớp");
                checkPassCon = true;
            }

            if (checkHo == true || checkTen == true || checkEmail == true
                    || checkPass == true || checkPassCon == true) {
                return;
            }

            pbSignUp.setVisibility(View.VISIBLE);
            cvSignUpSubmit.setVisibility(View.INVISIBLE);

            String ho = tiEdtHoSignUp.getText().toString();
            String ten = tiEdtTenSignUp.getText().toString();
            String email = tiEdtEmailSignUp.getText().toString().trim();
            String matKhau = tiEdtPassSignUp.getText().toString().trim();

            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(email, matKhau)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser userID = task.getResult().getUser();
                            String maTaiKhoan = userID.getUid();

                            KhachHang khachHang = new KhachHang(AutoStringID.createStringID("KH-"),
                                    ho, ten, email, "0", "0",
                                    "0", maTaiKhoan, 0);
                            new KhachHangDAO(SignUpActivity.this).themKhachHang(khachHang);
                            finish();
                        } else {
                            Toast.makeText(this, "Email đã được sử dụng", Toast.LENGTH_SHORT).show();
                            pbSignUp.setVisibility(View.GONE);
                            cvSignUpSubmit.setVisibility(View.VISIBLE);
                        }
                    });
        });

        llExitSignUp.setOnClickListener(v -> {
            finish();
        });
    }
}