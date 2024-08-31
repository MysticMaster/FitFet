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
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mobile_project_01.DAO.GiayChiTietDAO;
import com.example.mobile_project_01.DAO.GiayDAO;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.function.AutoStringID;
import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.GiayChiTiet;
import com.example.mobile_project_01.model.ThuongHieu;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BottomSheetDialogDetailGiayFragment extends BottomSheetDialogFragment {

    private Giay giay;

    public BottomSheetDialogDetailGiayFragment() {
        // Required empty public constructor
    }

    public BottomSheetDialogDetailGiayFragment(Giay giay) {
        this.giay = giay;
    }

    private FrameLayout flEditTenGiay, flEditThuongHieu;

    private ImageView ivDetailAnhGiay;

    private ImageButton iBtnEditDetailAnhGiay;
    private TextView tvDetailTenGiay, tvDetailThuongHieu;

    private EditText edtDetailTenGiay;

    private Spinner spiEditThuongHieu;

    private CardView cvOpenEditGiay, cvSaveEditGiay;
    private ProgressBar pbSaveEditGiay;

    private String maThuongHieu;

    private Uri uri;
    private StorageReference reference;
    private StorageReference imageRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog_detail_giay, container, false);

        reference = FirebaseStorage.getInstance().getReference();
        imageRef = reference.child("khoHinhGiay/" + AutoStringID.createStringID("ANHSP-"));

        ivDetailAnhGiay = view.findViewById(R.id.ivDetailAnhGiay);
        iBtnEditDetailAnhGiay = view.findViewById(R.id.iBtnEditDetailAnhGiay);

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
                                ivDetailAnhGiay.setImageBitmap(bitmap);
                            } catch (IOException ex) {
                                Log.e(TAG, "Lỗi hình: " + ex);
                            }
                        }
                    }
                }
        );

        iBtnEditDetailAnhGiay.setOnClickListener((v -> {
            launcher.launch("image/*");
        }));

        cvOpenEditGiay = view.findViewById(R.id.cvOpenEditGiay);
        cvSaveEditGiay = view.findViewById(R.id.cvSaveEditGiay);
        pbSaveEditGiay = view.findViewById(R.id.pbSaveEditGiay);

        TextView tvTrangThaiGiay = view.findViewById(R.id.tvTrangThaiGiay);
        CardView cvStatusGiayCT = view.findViewById(R.id.cvStatusGiayCT);
        TextView tvStatusGiay = view.findViewById(R.id.tvStatusGiay);
        if (giay.getTrangThaiGiay() == 0) {
            tvTrangThaiGiay.setText("Đang bán");

            tvStatusGiay.setText("Ngừng bán");
            cvStatusGiayCT.setOnClickListener(v -> {
                openDiaLogDoiTrangThai(1);
            });
        } else if (giay.getTrangThaiGiay() == 1) {
            tvTrangThaiGiay.setText("Ngừng bán");

            tvStatusGiay.setText("Tiếp tục bán");
            cvStatusGiayCT.setOnClickListener(v -> {
                openDiaLogDoiTrangThai(0);
            });
        }

        tvDetailTenGiay = view.findViewById(R.id.tvDetailTenGiay);
        edtDetailTenGiay = view.findViewById(R.id.edtDetailTenGiay);
        flEditTenGiay = view.findViewById(R.id.flEditTenGiay);
        ImageButton iBtnEditDetailTenGiay = view.findViewById(R.id.iBtnEditDetailTenGiay);
        ImageButton iBtnDoneDetailTenGiay = view.findViewById(R.id.iBtnDoneDetailTenGiay);

        iBtnEditDetailTenGiay.setOnClickListener((v -> {
            iBtnEditDetailTenGiay.setVisibility(View.GONE);
            iBtnDoneDetailTenGiay.setVisibility(View.VISIBLE);
            tvDetailTenGiay.setVisibility(View.GONE);
            edtDetailTenGiay.setVisibility(View.VISIBLE);
        }));

        iBtnDoneDetailTenGiay.setOnClickListener((v -> {
            if (edtDetailTenGiay.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Trống tên giày", Toast.LENGTH_SHORT).show();
            } else {
                tvDetailTenGiay.setText(edtDetailTenGiay.getText().toString().trim());
                iBtnEditDetailTenGiay.setVisibility(View.VISIBLE);
                iBtnDoneDetailTenGiay.setVisibility(View.GONE);
                tvDetailTenGiay.setVisibility(View.VISIBLE);
                edtDetailTenGiay.setVisibility(View.GONE);
            }
        }));

        tvDetailThuongHieu = view.findViewById(R.id.tvDetailThuongHieu);
        spiEditThuongHieu = view.findViewById(R.id.spiEditThuongHieu);

        flEditThuongHieu = view.findViewById(R.id.flEditThuongHieu);
        ImageButton iBtnEditDetailThuongHieu = view.findViewById(R.id.iBtnEditDetailThuongHieu);
        ImageButton iBtnDoneDetailThuongHieu = view.findViewById(R.id.iBtnDoneDetailThuongHieu);

        iBtnEditDetailThuongHieu.setOnClickListener((v -> {
            iBtnEditDetailThuongHieu.setVisibility(View.GONE);
            iBtnDoneDetailThuongHieu.setVisibility(View.VISIBLE);
            tvDetailThuongHieu.setVisibility(View.GONE);
            spiEditThuongHieu.setVisibility(View.VISIBLE);
        }));

        iBtnDoneDetailThuongHieu.setOnClickListener((v -> {
            FirebaseFirestore.getInstance().collection("ThuongHieu").document(maThuongHieu)
                    .get().addOnCompleteListener(task -> {
                        ThuongHieu thuongHieu = task.getResult().toObject(ThuongHieu.class);
                        tvDetailThuongHieu.setText(thuongHieu.getTenThuongHieu());
                    });

            iBtnEditDetailThuongHieu.setVisibility(View.VISIBLE);
            iBtnDoneDetailThuongHieu.setVisibility(View.GONE);
            tvDetailThuongHieu.setVisibility(View.VISIBLE);
            spiEditThuongHieu.setVisibility(View.GONE);
        }));

        cvOpenEditGiay.setOnClickListener((v -> {
            cvOpenEditGiay.setVisibility(View.GONE);
            cvSaveEditGiay.setVisibility(View.VISIBLE);

            iBtnEditDetailAnhGiay.setVisibility(View.VISIBLE);
            flEditTenGiay.setVisibility(View.VISIBLE);
            flEditThuongHieu.setVisibility(View.VISIBLE);
        }));

        ViewTreeObserver.OnGlobalLayoutListener visibilityChange = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (tvDetailTenGiay.getVisibility() == View.GONE || tvDetailThuongHieu.getVisibility() == View.GONE) {
                    cvSaveEditGiay.setEnabled(false);
                } else {
                    cvSaveEditGiay.setEnabled(true);
                }
            }
        };

        view.getViewTreeObserver().addOnGlobalLayoutListener(visibilityChange);

        CardView cvCloseDetailGiay = view.findViewById(R.id.cvCloseDetailGiay);
        cvCloseDetailGiay.setOnClickListener((v -> {
            dismiss();
        }));

        setData(giay);

        return view;
    }

    private void setData(Giay giay) {
        if (giay.getAnhGiay().equals("0")) {
            ivDetailAnhGiay.setImageResource(R.drawable.none_image);
        } else {
            Glide.with(getContext()).load(giay.getAnhGiay()).into(ivDetailAnhGiay);
        }
        tvDetailTenGiay.setText(giay.getTenGiay());
        edtDetailTenGiay.setText(giay.getTenGiay());

        FirebaseFirestore.getInstance().collection("ThuongHieu").document(giay.getMaThuongHieu())
                .get().addOnCompleteListener(task1 -> {
                    ThuongHieu thuongHieu = task1.getResult().toObject(ThuongHieu.class);
                    tvDetailThuongHieu.setText(thuongHieu.getTenThuongHieu());
                    setDataSpinnerThuongHieu(spiEditThuongHieu, thuongHieu.getMaThuongHieu());
                });

        cvSaveEditGiay.setOnClickListener((v -> {
            cvSaveEditGiay.setVisibility(View.GONE);
            pbSaveEditGiay.setVisibility(View.VISIBLE);

            giay.setTenGiay(tvDetailTenGiay.getText().toString().trim());
            if (maThuongHieu != null) {
                giay.setMaThuongHieu(maThuongHieu);
            }

            GiayDAO dao = new GiayDAO(getContext());

            if (uri != null) {
                UploadTask uploadTask = imageRef.putFile(uri);

                uploadTask.addOnSuccessListener((taskSnapshot -> {
                    Task<Uri> uriTask = imageRef.getDownloadUrl();

                    uriTask.addOnSuccessListener((uri1 -> {
                        if (!giay.getAnhGiay().equals("0")) {
                            FirebaseStorage.getInstance().getReferenceFromUrl(giay.getAnhGiay())
                                    .delete();
                        }
                        String anhGiay = uri1.toString();
                        giay.setAnhGiay(anhGiay);
                        dao.capNhatGiay(giay);
                        Toast.makeText(getContext(), "Cập nhật giày thành công", Toast.LENGTH_SHORT).show();

                        iBtnEditDetailAnhGiay.setVisibility(View.GONE);
                        flEditTenGiay.setVisibility(View.GONE);
                        flEditThuongHieu.setVisibility(View.GONE);

                        cvSaveEditGiay.setVisibility(View.VISIBLE);
                        pbSaveEditGiay.setVisibility(View.GONE);
                    })).addOnFailureListener(e -> {
                        cvSaveEditGiay.setVisibility(View.VISIBLE);
                        pbSaveEditGiay.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Lỗi không xác định", Toast.LENGTH_SHORT).show();
                    });
                })).addOnFailureListener(e -> {
                    cvSaveEditGiay.setVisibility(View.VISIBLE);
                    pbSaveEditGiay.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Mạng yếu! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                });
            } else {
                dao.capNhatGiay(giay);
                Toast.makeText(getContext(), "Cập nhật giày thành công", Toast.LENGTH_SHORT).show();

                iBtnEditDetailAnhGiay.setVisibility(View.GONE);
                flEditTenGiay.setVisibility(View.GONE);
                flEditThuongHieu.setVisibility(View.GONE);

                cvOpenEditGiay.setVisibility(View.VISIBLE);
                pbSaveEditGiay.setVisibility(View.GONE);
            }
        }));

    }

    private void setDataSpinnerThuongHieu(Spinner spinnerThuongHieu, String maTH) {
        List<ThuongHieu> thuongHieuList = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("ThuongHieu")
                .whereEqualTo("trangThaiThuongHieu", 0)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot snapshot : task.getResult()) {
                            ThuongHieu thuongHieu = snapshot.toObject(ThuongHieu.class);
                            thuongHieuList.add(thuongHieu);
                        }

                        for (ThuongHieu thuongHieu : thuongHieuList) {
                            if (thuongHieu.getMaThuongHieu().equals(maTH)) {
                                thuongHieuList.remove(thuongHieu);
                                thuongHieuList.add(0, thuongHieu);
                                break;
                            }
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
                });
    }

    private void openDiaLogDoiTrangThai(int status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Cảnh báo");
        builder.setMessage("Đây là thay đổi có ảnh hưởng lớn đến hệ thống, cần cân nhắc kỹ trước khi xác nhận");

        builder.setPositiveButton("Xác nhận", ((dialog, which) -> {
            giay.setTrangThaiGiay(status);
            new GiayDAO(getContext()).capNhatGiay(giay);
            Toast.makeText(getContext(), "Cập nhật giày thành công", Toast.LENGTH_SHORT).show();

            GiayChiTietDAO dao = new GiayChiTietDAO(getContext());
            FirebaseFirestore.getInstance().collection("GiayChiTiet")
                    .whereEqualTo("maGiay", giay.getMaGiay())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<GiayChiTiet> readList = new ArrayList<>();
                            for (DocumentSnapshot snapshot : task.getResult()) {
                                readList.add(snapshot.toObject(GiayChiTiet.class));
                            }

                            for (GiayChiTiet giayChiTiet : readList) {
                                giayChiTiet.setTrangThaiGiayChiTiet(status);
                                dao.capNhatGiayChiTiet(giayChiTiet);
                            }
                        }
                    });

            dismiss();
        }));

        builder.setNegativeButton("Hủy", ((dialog, which) -> {

        }));

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setContentView(R.layout.fragment_bottom_sheet_dialog_detail_giay);

        FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.bottom_sheet_dialog_background);

        return dialog;
    }

}