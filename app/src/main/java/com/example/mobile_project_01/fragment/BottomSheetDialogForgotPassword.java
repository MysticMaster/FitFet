package com.example.mobile_project_01.fragment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mobile_project_01.R;
import com.example.mobile_project_01.WelcomeActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.List;


public class BottomSheetDialogForgotPassword extends BottomSheetDialogFragment {

    public BottomSheetDialogForgotPassword() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog_forgot_password, container, false);

        ImageButton iBtnCloseForgot = view.findViewById(R.id.iBtnCloseForgot);
        TextInputLayout tilEmailForgot = view.findViewById(R.id.tilEmailForgot);
        TextInputEditText tiEdtEmailForgot = view.findViewById(R.id.tiEdtEmailForgot);
        ProgressBar pbForgot = view.findViewById(R.id.pbForgot);
        CardView cvForgotSubmit = view.findViewById(R.id.cvForgotSubmit);

        tiEdtEmailForgot.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                tilEmailForgot.setHelperText(null);
            }
            return false;
        });

        cvForgotSubmit.setOnClickListener(v -> {
            Boolean check = false;

            if (tiEdtEmailForgot.getText().toString().trim().isEmpty()) {
                tilEmailForgot.setHelperText("Trống tên");
                check = true;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(tiEdtEmailForgot.getText().toString().trim()).matches()) {
                tilEmailForgot.setHelperText("Email không hợp lệ");
                check = true;
            }

            if (check == true) {
                return;
            }

            cvForgotSubmit.setVisibility(View.INVISIBLE);
            pbForgot.setVisibility(View.VISIBLE);

            String email = tiEdtEmailForgot.getText().toString().trim();

            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getContext(), "Hệ thống đã gửi một liên kết đến email của bạn" +
                            " truy cập đường dẫn để lấy lại mật khẩu", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            });


        });

        iBtnCloseForgot.setOnClickListener(v -> {
            dismiss();
        });
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setContentView(R.layout.fragment_bottom_sheet_dialog_forgot_password);

        FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.bottom_sheet_dialog_background);

        return dialog;
    }

}