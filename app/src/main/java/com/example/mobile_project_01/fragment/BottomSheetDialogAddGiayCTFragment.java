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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.mobile_project_01.DAO.GiayChiTietDAO;
import com.example.mobile_project_01.DAO.KichCoGiayCTDAO;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.adapter.KichCoPickUpAdapter;
import com.example.mobile_project_01.function.AutoStringID;
import com.example.mobile_project_01.model.GiayChiTiet;
import com.example.mobile_project_01.model.KichCoGiayCT;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public class BottomSheetDialogAddGiayCTFragment extends BottomSheetDialogFragment {

    private String maGiay;

    public BottomSheetDialogAddGiayCTFragment() {
        // Required empty public constructor
    }

    public BottomSheetDialogAddGiayCTFragment(String maGiay) {
        this.maGiay = maGiay;
    }

    private Uri uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog_add_giay_c_t, container, false);

        ImageView ivAddAnhGiayCT = view.findViewById(R.id.ivAddAnhGiayCT);
        EditText edtAddTenGiayCT = view.findViewById(R.id.edtAddTenGiayCT);
        EditText edtAddMauGiayCT = view.findViewById(R.id.edtAddMauGiayCT);
        EditText edtAddGiaGiayCT = view.findViewById(R.id.edtAddGiaGiayCT);
        RecyclerView rvAddKichCo = view.findViewById(R.id.rvAddKichCo);
        ProgressBar pbAddGiayCT = view.findViewById(R.id.pbAddGiayCT);
        CardView cvAddGiayCT = view.findViewById(R.id.cvAddGiayCT);
        CardView cvCloseAddGiayCT = view.findViewById(R.id.cvCloseAddGiayCT);

        int arrKichCo[] = {36, 37, 38, 39, 40, 41, 42, 43, 44, 45};
        KichCoPickUpAdapter adapter = new KichCoPickUpAdapter(getContext(), arrKichCo, cvAddGiayCT);
        List<KichCoGiayCT> listKichCo = adapter.getSelectedItems();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        rvAddKichCo.setLayoutManager(gridLayoutManager);
        rvAddKichCo.setAdapter(adapter);

        StorageReference reference = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = reference.child("khoHinhGiayCT/" + AutoStringID.createStringID("ANHSPCT-"));
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
                                ivAddAnhGiayCT.setImageBitmap(bitmap);
                            } catch (IOException ex) {
                                Log.e(TAG, "Lỗi hình: " + ex);
                            }
                        }
                    }
                }
        );

        ivAddAnhGiayCT.setOnClickListener((v -> {
            launcher.launch("image/*");
        }));

        cvAddGiayCT.setEnabled(false);
        ViewTreeObserver.OnGlobalLayoutListener visibilityChange = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (edtAddTenGiayCT.getText().toString().isEmpty() ||
                        edtAddMauGiayCT.getText().toString().isEmpty() ||
                        edtAddGiaGiayCT.getText().toString().isEmpty()) {
                    rvAddKichCo.setVisibility(View.GONE);
                } else {
                    rvAddKichCo.setVisibility(View.VISIBLE);
                }

                if (rvAddKichCo.getVisibility() == View.GONE) {
                    cvAddGiayCT.setEnabled(false);
                }
            }
        };

        view.getViewTreeObserver().addOnGlobalLayoutListener(visibilityChange);

        cvAddGiayCT.setOnClickListener((v -> {
            cvAddGiayCT.setVisibility(View.INVISIBLE);
            pbAddGiayCT.setVisibility(View.VISIBLE);

            String maGiayCT = AutoStringID.createStringID("SPCT-");
            String tenGiayCT = edtAddTenGiayCT.getText().toString();
            String mauSac = edtAddMauGiayCT.getText().toString();
            int donGia = Integer.parseInt(edtAddGiaGiayCT.getText().toString());
            GiayChiTietDAO dao = new GiayChiTietDAO(getContext());

            if (uri == null) {
                GiayChiTiet giayChiTiet = new GiayChiTiet(maGiayCT, maGiay, tenGiayCT, mauSac,
                        "0", donGia, 0);
                dao.themGiayChiTiet(giayChiTiet);

                for (KichCoGiayCT kichCoGiayCT : listKichCo) {
                    kichCoGiayCT.setMaGiayCT(giayChiTiet.getMaGiayChiTiet());
                    new KichCoGiayCTDAO(getContext()).themKichCo(kichCoGiayCT);
                }

                dismiss();
            } else {
                UploadTask uploadTask = imageRef.putFile(uri);

                uploadTask.addOnSuccessListener((taskSnapshot -> {
                    Task<Uri> uriTask = imageRef.getDownloadUrl();

                    uriTask.addOnSuccessListener((uri1 -> {
                        String anhGiayCT = uri1.toString();
                        GiayChiTiet giayChiTiet = new GiayChiTiet(maGiayCT, maGiay, tenGiayCT, mauSac,
                                anhGiayCT, donGia, 0);
                        dao.themGiayChiTiet(giayChiTiet);

                        for (KichCoGiayCT kichCoGiayCT : listKichCo) {
                            kichCoGiayCT.setMaGiayCT(giayChiTiet.getMaGiayChiTiet());
                            new KichCoGiayCTDAO(getContext()).themKichCo(kichCoGiayCT);
                        }

                        dismiss();
                    })).addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Lỗi không xác định", Toast.LENGTH_SHORT).show();
                        cvAddGiayCT.setVisibility(View.INVISIBLE);
                        pbAddGiayCT.setVisibility(View.VISIBLE);
                    });
                })).addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Mạng yếu! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    cvAddGiayCT.setVisibility(View.INVISIBLE);
                    pbAddGiayCT.setVisibility(View.VISIBLE);
                });
            }
        }));

        cvCloseAddGiayCT.setOnClickListener((v -> {
            dismiss();
        }));

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setContentView(R.layout.fragment_bottom_sheet_dialog_add_giay_c_t);

        FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.bottom_sheet_dialog_background);

        return dialog;
    }

}