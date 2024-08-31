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
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mobile_project_01.DAO.GiayChiTietDAO;
import com.example.mobile_project_01.DAO.GiayDAO;
import com.example.mobile_project_01.DAO.ThuongHieuDAO;
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
import java.util.List;

public class BottomSheetDialogDetailThuongHieuFragment extends BottomSheetDialogFragment {

    private ThuongHieu thuongHieu;

    public BottomSheetDialogDetailThuongHieuFragment() {
        // Required empty public constructor
    }

    public BottomSheetDialogDetailThuongHieuFragment(ThuongHieu thuongHieu) {
        this.thuongHieu = thuongHieu;
    }

    private ImageView ivDetailAnhThuongHieu;

    private ImageButton iBtnEditDetailAnhThuongHieu;

    private TextView tvDetailTenThuongHieu;

    private EditText edtDetailTenThuongHieu;

    private FrameLayout flEditTenThuongHieu;

    private CardView cvOpenEditThuongHieu, cvSaveEditThuongHieu;
    private ProgressBar pbSaveEditThuongHieu;

    private Uri uri;
    private StorageReference reference;
    private StorageReference imageRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog_detail_thuong_hieu, container, false);

        reference = FirebaseStorage.getInstance().getReference();
        imageRef = reference.child("khoHinhThuongHieu/" + AutoStringID.createStringID("LOGO-"));

        ivDetailAnhThuongHieu = view.findViewById(R.id.ivDetailAnhThuongHieu);
        iBtnEditDetailAnhThuongHieu = view.findViewById(R.id.iBtnEditDetailAnhThuongHieu);

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
                                ivDetailAnhThuongHieu.setImageBitmap(bitmap);
                            } catch (IOException ex) {
                                Log.e(TAG, "Lỗi hình: " + ex);
                            }
                        }
                    }
                }
        );

        iBtnEditDetailAnhThuongHieu.setOnClickListener((v -> {
            launcher.launch("image/*");
        }));

        TextView tvTrangThaiThuongHieu = view.findViewById(R.id.tvTrangThaiThuongHieu);
        CardView cvStatusThuongHieu = view.findViewById(R.id.cvStatusThuongHieu);
        TextView tvStatusThuongHieu = view.findViewById(R.id.tvStatusThuongHieu);
        if (thuongHieu.getTrangThaiThuongHieu() == 0) {
            tvTrangThaiThuongHieu.setText("Đang phân phối");

            tvStatusThuongHieu.setText("Ngừng phân phối");
            cvStatusThuongHieu.setOnClickListener(v -> {
                openDiaLogDoiTrangThai(1);
            });
        } else if (thuongHieu.getTrangThaiThuongHieu() == 1) {
            tvTrangThaiThuongHieu.setText("Ngừng phân phối");

            tvStatusThuongHieu.setText("Mở phân phối");
            cvStatusThuongHieu.setOnClickListener(v -> {
                openDiaLogDoiTrangThai(0);
            });
        }

        cvOpenEditThuongHieu = view.findViewById(R.id.cvOpenEditThuongHieu);
        cvSaveEditThuongHieu = view.findViewById(R.id.cvSaveEditThuongHieu);
        pbSaveEditThuongHieu = view.findViewById(R.id.pbSaveEditThuongHieu);

        tvDetailTenThuongHieu = view.findViewById(R.id.tvDetailTenThuongHieu);
        edtDetailTenThuongHieu = view.findViewById(R.id.edtDetailTenThuongHieu);
        flEditTenThuongHieu = view.findViewById(R.id.flEditTenThuongHieu);
        ImageButton iBtnEditDetailTenThuongHieu = view.findViewById(R.id.iBtnEditDetailTenThuongHieu);
        ImageButton iBtnDoneDetailTenThuongHieu = view.findViewById(R.id.iBtnDoneDetailTenThuongHieu);

        iBtnEditDetailTenThuongHieu.setOnClickListener(v -> {
            iBtnEditDetailTenThuongHieu.setVisibility(View.GONE);
            iBtnDoneDetailTenThuongHieu.setVisibility(View.VISIBLE);
            tvDetailTenThuongHieu.setVisibility(View.GONE);
            edtDetailTenThuongHieu.setVisibility(View.VISIBLE);
        });

        iBtnDoneDetailTenThuongHieu.setOnClickListener(v -> {
            if (edtDetailTenThuongHieu.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Trống tên thương hiệu", Toast.LENGTH_SHORT).show();
            } else {
                tvDetailTenThuongHieu.setText(edtDetailTenThuongHieu.getText().toString());
                iBtnEditDetailTenThuongHieu.setVisibility(View.VISIBLE);
                iBtnDoneDetailTenThuongHieu.setVisibility(View.GONE);
                tvDetailTenThuongHieu.setVisibility(View.VISIBLE);
                edtDetailTenThuongHieu.setVisibility(View.GONE);
            }
        });

        cvOpenEditThuongHieu.setOnClickListener((v -> {
            cvOpenEditThuongHieu.setVisibility(View.GONE);
            cvSaveEditThuongHieu.setVisibility(View.VISIBLE);

            iBtnEditDetailAnhThuongHieu.setVisibility(View.VISIBLE);
            flEditTenThuongHieu.setVisibility(View.VISIBLE);
        }));

        ViewTreeObserver.OnGlobalLayoutListener visibilityChange = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (tvDetailTenThuongHieu.getVisibility() == View.GONE) {
                    cvSaveEditThuongHieu.setEnabled(false);
                } else {
                    cvSaveEditThuongHieu.setEnabled(true);
                }
            }
        };

        view.getViewTreeObserver().addOnGlobalLayoutListener(visibilityChange);

        CardView cvCloseDetailThuongHieu = view.findViewById(R.id.cvCloseDetailThuongHieu);
        cvCloseDetailThuongHieu.setOnClickListener(v -> {
            dismiss();
        });

        setData(thuongHieu);

        return view;
    }

    private void setData(ThuongHieu thuongHieu) {
        if (thuongHieu.getLogoThuongHieu().equals("0")) {
            ivDetailAnhThuongHieu.setImageResource(R.drawable.none_image);
        } else {
            Glide.with(getContext()).load(thuongHieu.getLogoThuongHieu()).into(ivDetailAnhThuongHieu);
        }
        tvDetailTenThuongHieu.setText(thuongHieu.getTenThuongHieu());
        edtDetailTenThuongHieu.setText(thuongHieu.getTenThuongHieu());


        cvSaveEditThuongHieu.setOnClickListener((v -> {
            cvSaveEditThuongHieu.setVisibility(View.GONE);
            pbSaveEditThuongHieu.setVisibility(View.VISIBLE);

            thuongHieu.setTenThuongHieu(tvDetailTenThuongHieu.getText().toString());

            ThuongHieuDAO dao = new ThuongHieuDAO(getContext());

            if (uri != null) {
                UploadTask uploadTask = imageRef.putFile(uri);

                uploadTask.addOnSuccessListener((taskSnapshot -> {
                    Task<Uri> uriTask = imageRef.getDownloadUrl();

                    uriTask.addOnSuccessListener((uri1 -> {
                        if (!thuongHieu.getLogoThuongHieu().equals("0")) {
                            FirebaseStorage.getInstance().getReferenceFromUrl(thuongHieu.getLogoThuongHieu())
                                    .delete();
                        }
                        String logo = uri1.toString();
                        thuongHieu.setLogoThuongHieu(logo);
                        dao.capNhatThuongHieu(thuongHieu);

                        iBtnEditDetailAnhThuongHieu.setVisibility(View.GONE);
                        flEditTenThuongHieu.setVisibility(View.GONE);

                        cvSaveEditThuongHieu.setVisibility(View.VISIBLE);
                        pbSaveEditThuongHieu.setVisibility(View.GONE);
                    })).addOnFailureListener(e -> {
                        cvSaveEditThuongHieu.setVisibility(View.VISIBLE);
                        pbSaveEditThuongHieu.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Lỗi không xác định", Toast.LENGTH_SHORT).show();
                    });
                })).addOnFailureListener(e -> {
                    cvSaveEditThuongHieu.setVisibility(View.VISIBLE);
                    pbSaveEditThuongHieu.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Mạng yếu! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                });
            } else {
                dao.capNhatThuongHieu(thuongHieu);

                iBtnEditDetailAnhThuongHieu.setVisibility(View.GONE);
                flEditTenThuongHieu.setVisibility(View.GONE);

                cvSaveEditThuongHieu.setVisibility(View.VISIBLE);
                pbSaveEditThuongHieu.setVisibility(View.GONE);
            }
        }));

    }

    private void openDiaLogDoiTrangThai(int status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Cảnh báo");
        builder.setMessage("Đây là thay đổi có ảnh hưởng lớn đến hệ thống, cần cân nhắc kỹ trước khi xác nhận");

        builder.setPositiveButton("Xác nhận", ((dialog, which) -> {
            thuongHieu.setTrangThaiThuongHieu(status);
            new ThuongHieuDAO(getContext()).capNhatThuongHieu(thuongHieu);

            GiayDAO giayDAO = new GiayDAO(getContext());
            GiayChiTietDAO giayChiTietDAO = new GiayChiTietDAO(getContext());
            FirebaseFirestore.getInstance().collection("Giay")
                    .whereEqualTo("maThuongHieu", thuongHieu.getMaThuongHieu())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<Giay> readListGiay = new ArrayList<>();
                            for (DocumentSnapshot snapshot : task.getResult()) {
                                readListGiay.add(snapshot.toObject(Giay.class));
                            }

                            for (Giay giay : readListGiay) {
                                giay.setTrangThaiGiay(status);
                                giayDAO.capNhatGiay(giay);

                                FirebaseFirestore.getInstance().collection("GiayChiTiet")
                                        .whereEqualTo("maGiay", giay.getMaGiay())
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                List<GiayChiTiet> readList = new ArrayList<>();
                                                for (DocumentSnapshot snapshot : task1.getResult()) {
                                                    readList.add(snapshot.toObject(GiayChiTiet.class));
                                                }

                                                for (GiayChiTiet giayChiTiet : readList) {
                                                    giayChiTiet.setTrangThaiGiayChiTiet(status);
                                                    giayChiTietDAO.capNhatGiayChiTiet(giayChiTiet);
                                                }
                                            }
                                        });
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
        dialog.setContentView(R.layout.fragment_bottom_sheet_dialog_detail_thuong_hieu);

        FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.bottom_sheet_dialog_background);

        return dialog;
    }

}