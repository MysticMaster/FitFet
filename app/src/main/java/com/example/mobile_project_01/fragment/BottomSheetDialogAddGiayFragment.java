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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mobile_project_01.DAO.GiayDAO;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.function.AutoStringID;
import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.GiayChiTiet;
import com.example.mobile_project_01.model.ThuongHieu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class BottomSheetDialogAddGiayFragment extends BottomSheetDialogFragment {

    public BottomSheetDialogAddGiayFragment() {
        // Required empty public constructor
    }

    private Uri uri;
    private String maThuongHieu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog_add_giay, container, false);

        ImageView ivAddAnhGiay = view.findViewById(R.id.ivAddAnhGiay);
        EditText edtAddTenGiay = view.findViewById(R.id.edtAddTenGiay);
        Spinner spiAddThuongHieu = view.findViewById(R.id.spiAddThuongHieu);
        setDataSpinnerThuongHieu(spiAddThuongHieu);
        ProgressBar pbAddGiay = view.findViewById(R.id.pbAddGiay);
        CardView cvAddGiay = view.findViewById(R.id.cvAddGiay);
        CardView cvCloseAddGiay = view.findViewById(R.id.cvCloseAddGiay);

        StorageReference reference = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = reference.child("khoHinhGiay/" + AutoStringID.createStringID("ANHSP-"));
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
                                ivAddAnhGiay.setImageBitmap(bitmap);
                            } catch (IOException ex) {
                                Log.e(TAG, "Lỗi hình: " + ex);
                            }
                        }
                    }
                }
        );

        ivAddAnhGiay.setOnClickListener((v -> {
            launcher.launch("image/*");
        }));

        ViewTreeObserver.OnGlobalLayoutListener visibilityChange = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (edtAddTenGiay.getText().toString().isEmpty() || maThuongHieu == null) {
                    cvAddGiay.setEnabled(false);
                } else {
                    cvAddGiay.setEnabled(true);
                }
            }
        };

        view.getViewTreeObserver().addOnGlobalLayoutListener(visibilityChange);

        cvAddGiay.setOnClickListener((v -> {
            cvAddGiay.setVisibility(View.INVISIBLE);
            pbAddGiay.setVisibility(View.VISIBLE);

            String maGiay = AutoStringID.createStringID("SP-");
            String tenGiay = edtAddTenGiay.getText().toString().trim();
            GiayDAO dao = new GiayDAO(getContext());

            if (uri == null) {
                Giay giay = new Giay(maGiay, tenGiay, "0", maThuongHieu, 0);
                dao.themGiay(giay);
                dismiss();
            } else {
                UploadTask uploadTask = imageRef.putFile(uri);

                uploadTask.addOnSuccessListener((taskSnapshot -> {
                    Task<Uri> uriTask = imageRef.getDownloadUrl();
                    uriTask.addOnSuccessListener((uri1 -> {
                        String anhGiay = uri1.toString();
                        Giay giay = new Giay(maGiay, tenGiay, anhGiay, maThuongHieu, 0);
                        dao.themGiay(giay);
                        dismiss();
                    })).addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Lỗi không xác định", Toast.LENGTH_SHORT).show();
                        cvAddGiay.setVisibility(View.INVISIBLE);
                        pbAddGiay.setVisibility(View.VISIBLE);
                    });
                })).addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Mạng yếu! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    cvAddGiay.setVisibility(View.INVISIBLE);
                    pbAddGiay.setVisibility(View.VISIBLE);
                });
            }
        }));

        cvCloseAddGiay.setOnClickListener((v -> {
            dismiss();
        }));

        return view;
    }

    private void setDataSpinnerThuongHieu(Spinner spinnerThuongHieu) {
        List<ThuongHieu> thuongHieuList = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("ThuongHieu")
                .whereEqualTo("trangThaiThuongHieu", 0)
                .get().addOnCompleteListener((task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot snapshot : task.getResult()) {
                            ThuongHieu thuongHieu = snapshot.toObject(ThuongHieu.class);
                            thuongHieuList.add(thuongHieu);
                        }

                        List<HashMap<String, Object>> hashMapList = new ArrayList<>();
                        for (ThuongHieu thuongHieu : thuongHieuList) {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("maThuongHieu", thuongHieu.getMaThuongHieu());
                            hashMap.put("tenThuongHieu", thuongHieu.getTenThuongHieu());
                            hashMapList.add(hashMap);
                        }
                        SimpleAdapter simpleAdapter = new SimpleAdapter(
                                getContext(),
                                hashMapList,
                                android.R.layout.simple_list_item_1,
                                new String[]{"tenThuongHieu"},
                                new int[]{android.R.id.text1}
                        );
                        spinnerThuongHieu.setAdapter(simpleAdapter);

                        spinnerThuongHieu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                HashMap<String, Object> hashMap = hashMapList.get(position);
                                maThuongHieu = (String) hashMap.get("maThuongHieu");
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                }));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setContentView(R.layout.fragment_bottom_sheet_dialog_add_giay);

        FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.bottom_sheet_dialog_background);

        return dialog;
    }

}