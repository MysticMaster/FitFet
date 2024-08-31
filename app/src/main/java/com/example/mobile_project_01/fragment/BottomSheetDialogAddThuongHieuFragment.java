package com.example.mobile_project_01.fragment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Dialog;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mobile_project_01.DAO.ThuongHieuDAO;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.function.AutoStringID;
import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.ThuongHieu;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;


public class BottomSheetDialogAddThuongHieuFragment extends BottomSheetDialogFragment {

    private Uri uri;

    public BottomSheetDialogAddThuongHieuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog_add_thuong_hieu, container, false);

        ImageView ivAddLogoThuongHieu = view.findViewById(R.id.ivAddLogoThuongHieu);
        EditText edtAddTenThuongHieu = view.findViewById(R.id.edtAddTenThuongHieu);
        ProgressBar pbAddThuongHieu = view.findViewById(R.id.pbAddThuongHieu);
        CardView cvAddThuongHieu = view.findViewById(R.id.cvAddThuongHieu);
        CardView cvCloseAddThuongHieu = view.findViewById(R.id.cvCloseAddThuongHieu);

        StorageReference reference = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = reference.child("khoHinhThuongHieu/" + AutoStringID.createStringID("LOGO-"));
        ActivityResultLauncher<String> launcher;

        launcher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri o) {
                        if (o != null) {
                            uri = o;
                            try {
                                ContentResolver resolver = getContext().getContentResolver();
                                InputStream inputStream = resolver.openInputStream(uri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                ivAddLogoThuongHieu.setImageBitmap(bitmap);
                            } catch (IOException ex) {
                                Log.e(TAG, "Lỗi hình: " + ex);
                            }
                        }
                    }
                }
        );

        ivAddLogoThuongHieu.setOnClickListener(v -> {
            launcher.launch("image/*");
        });

        ViewTreeObserver.OnGlobalLayoutListener visibilityChange = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (edtAddTenThuongHieu.getText().toString().isEmpty()) {
                    cvAddThuongHieu.setEnabled(false);
                } else {
                    cvAddThuongHieu.setEnabled(true);
                }
            }
        };

        view.getViewTreeObserver().addOnGlobalLayoutListener(visibilityChange);

        cvAddThuongHieu.setOnClickListener(v -> {
            cvAddThuongHieu.setVisibility(View.INVISIBLE);
            pbAddThuongHieu.setVisibility(View.VISIBLE);

            String maThuongHieu = AutoStringID.createStringID("TH-");
            String tenThuongHieu = edtAddTenThuongHieu.getText().toString();
            ThuongHieuDAO thuongHieuDAO = new ThuongHieuDAO(getContext());

            if (uri == null) {
                ThuongHieu thuongHieu = new ThuongHieu(maThuongHieu, tenThuongHieu, "0", 0);
                thuongHieuDAO.themThuongHieu(thuongHieu);
                dismiss();
            } else {
                UploadTask uploadTask = imageRef.putFile(uri);

                uploadTask.addOnSuccessListener((taskSnapshot -> {
                    Task<Uri> uriTask = imageRef.getDownloadUrl();
                    uriTask.addOnSuccessListener((uri1 -> {
                        String logoThuongHieu = uri1.toString();
                        ThuongHieu thuongHieu = new ThuongHieu(maThuongHieu, tenThuongHieu, logoThuongHieu, 0);
                        thuongHieuDAO.themThuongHieu(thuongHieu);
                        dismiss();
                    })).addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Lỗi không xác định", Toast.LENGTH_SHORT).show();
                        cvAddThuongHieu.setVisibility(View.INVISIBLE);
                        pbAddThuongHieu.setVisibility(View.VISIBLE);
                    });
                })).addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Mạng yếu! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    cvAddThuongHieu.setVisibility(View.INVISIBLE);
                    pbAddThuongHieu.setVisibility(View.VISIBLE);
                });
            }
        });

        cvCloseAddThuongHieu.setOnClickListener(v -> {
            dismiss();
        });

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setContentView(R.layout.fragment_bottom_sheet_dialog_add_thuong_hieu);

        FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.bottom_sheet_dialog_background);

        return dialog;
    }

}