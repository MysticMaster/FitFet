package com.example.mobile_project_01.fragment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mobile_project_01.DAO.GiayChiTietDAO;
import com.example.mobile_project_01.DAO.KichCoGiayCTDAO;
import com.example.mobile_project_01.QLDetailGiayChiTietActivity;
import com.example.mobile_project_01.QLHienThiDonHangActivity;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.adapter.KichCoAdapter;
import com.example.mobile_project_01.adapter.KichCoPickUpAdapter;
import com.example.mobile_project_01.function.AutoStringID;
import com.example.mobile_project_01.function.FormatCurrency;
import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.GiayChiTiet;
import com.example.mobile_project_01.model.KichCoGiayCT;
import com.example.mobile_project_01.model.ThuongHieu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;


public class BottomSheetDialogDetailGiayCTFragment extends BottomSheetDialogFragment {

    private GiayChiTiet giayChiTiet;
    private int trangThai;

    private int kichCo;

    public BottomSheetDialogDetailGiayCTFragment() {
        // Required empty public constructor
    }

    public BottomSheetDialogDetailGiayCTFragment(GiayChiTiet giayChiTiet, int trangThai) {
        this.giayChiTiet = giayChiTiet;
        this.trangThai = trangThai;
    }

    private KichCoAdapter kichCoAdapter;
    private List<KichCoGiayCT> kichCoGiayCTList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog_detail_giay_c_t, container, false);

        RelativeLayout rlFunctionDetailGiayCT = view.findViewById(R.id.rlFunctionDetailGiayCT);
        Button btnCloseDetailGiayCTNgoai = view.findViewById(R.id.btnCloseDetailGiayCTNgoai);

        LinearLayout llFunctionDetailGiayCT = view.findViewById(R.id.llFunctionDetailGiayCT);

        TextView tvTrangThaiGiayCT = view.findViewById(R.id.tvTrangThaiGiayCT);
        CardView cvStatusGiayCT = view.findViewById(R.id.cvStatusGiayCT);
        TextView tvStatusGiayCT = view.findViewById(R.id.tvStatusGiayCT);
        if (giayChiTiet.getTrangThaiGiayChiTiet() == 0) {
            tvTrangThaiGiayCT.setText("Đang bán");

            tvStatusGiayCT.setText("Ngừng bán");
            cvStatusGiayCT.setOnClickListener(v -> {
                openDiaLogDoiTrangThai(1);
            });
        } else if (giayChiTiet.getTrangThaiGiayChiTiet() == 1) {
            tvTrangThaiGiayCT.setText("Ngừng bán");

            tvStatusGiayCT.setText("Tiếp tục bán");
            cvStatusGiayCT.setOnClickListener(v -> {
                openDiaLogDoiTrangThai(0);
            });
        }

        // Ảnh--------------------------------------------------------------------------------------------
        ImageView ivDetailAnhGiayCT = view.findViewById(R.id.ivDetailAnhGiayCT);

        if (giayChiTiet.getAnhGiayChiTiet().equals("0")) {
            ivDetailAnhGiayCT.setImageResource(R.drawable.none_image);
        } else {
            Glide.with(getContext()).load(giayChiTiet.getAnhGiayChiTiet()).into(ivDetailAnhGiayCT);
        }

        if (trangThai == 1) {
            llFunctionDetailGiayCT.setVisibility(View.GONE);
            btnCloseDetailGiayCTNgoai.setVisibility(View.VISIBLE);
        }

        TextView tvDetailTenGiayCT = view.findViewById(R.id.tvDetailTenGiayCT);
        tvDetailTenGiayCT.setText(giayChiTiet.getTenGiayChiTiet());
        TextView tvDetailMauGiayCT = view.findViewById(R.id.tvDetailMauGiayCT);
        tvDetailMauGiayCT.setText(giayChiTiet.getMauSac());
        TextView tvDetailGiaGiayCT = view.findViewById(R.id.tvDetailGiaGiayCT);
        tvDetailGiaGiayCT.setText(FormatCurrency.formatCurrency(giayChiTiet.getGia()) + "₫");


        RecyclerView rvDetailKichCo = view.findViewById(R.id.rvDetailKichCo);
        kichCoGiayCTList = new ArrayList<>();
        kichCoAdapter = new KichCoAdapter(kichCoGiayCTList, getContext());
        kichCoAdapter.index = kichCo;

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvDetailKichCo.setLayoutManager(manager);
        rvDetailKichCo.setAdapter(kichCoAdapter);

        kichCoAdapter.notifyDataSetChanged();


        // Số lượng------------------------------------------------------------------------------------
        RelativeLayout rlDetailSLGiayCT = view.findViewById(R.id.rlDetailSLGiayCT);
        TextView tvDetailSLGiayCT = view.findViewById(R.id.tvDetailSLGiayCT);

        kichCoAdapter.setOnItemClickListener(kichCoGiayCT -> {
            rlDetailSLGiayCT.setVisibility(View.VISIBLE);

            tvDetailSLGiayCT.setText(String.valueOf(kichCoGiayCT.getSoLuong()));

        });

// Button ---------------------------------------------------------------------------------------------
        CardView cvOpenEditGiayCT = view.findViewById(R.id.cvOpenEditGiayCT);
        CardView cvCloseDetailGiayCT = view.findViewById(R.id.cvCloseDetailGiayCT);

        cvOpenEditGiayCT.setOnClickListener((v -> {
            Intent intent = new Intent(getContext(), QLDetailGiayChiTietActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("giayChiTiet", giayChiTiet);
            intent.putExtras(bundle);
            startActivity(intent);
        }));

        cvCloseDetailGiayCT.setOnClickListener((v -> {
            dismiss();
        }));

        btnCloseDetailGiayCTNgoai.setOnClickListener(v -> {
            dismiss();
        });

        layDanhSachKichCo(giayChiTiet.getMaGiayChiTiet());
        changeData(giayChiTiet, ivDetailAnhGiayCT, tvDetailTenGiayCT,
                tvDetailMauGiayCT, tvDetailGiaGiayCT);

        return view;
    }

    private void layDanhSachKichCo(String maGiayChiTiet) {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("KichCoGiayChiTiet");

        reference.whereEqualTo("maGiayCT", maGiayChiTiet)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e(ContentValues.TAG, "Đã có lỗi: ", error);
                        return;
                    }
                    if (value != null) {
                        for (DocumentChange change : value.getDocumentChanges()) {
                            switch (change.getType()) {
                                case ADDED:
                                    DocumentSnapshot addSnapshot = change.getDocument();
                                    KichCoGiayCT addKichCo = addSnapshot.toObject(KichCoGiayCT.class);
                                    kichCoGiayCTList.add(addKichCo);
                                    Collections.sort(kichCoGiayCTList, Comparator.comparingInt(KichCoGiayCT::getKichCo));
                                    kichCoAdapter.notifyDataSetChanged();
                                    break;
                                case MODIFIED:
                                    DocumentSnapshot modiSnapshot = change.getDocument();
                                    KichCoGiayCT modiKichCo = modiSnapshot.toObject(KichCoGiayCT.class);
                                    for (int i = 0; i < kichCoGiayCTList.size(); i++) {
                                        KichCoGiayCT kichCoIndex = kichCoGiayCTList.get(i);
                                        if (kichCoIndex.getMaKichCoGiayCT().equals(modiKichCo.getMaKichCoGiayCT())) {
                                            kichCoGiayCTList.set(i, modiKichCo);
                                            kichCoAdapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                                case REMOVED:
                                    String removeID = change.getDocument().getId();
                                    for (int i = 0; i < kichCoGiayCTList.size(); i++) {
                                        if (kichCoGiayCTList.get(i).getMaKichCoGiayCT().equals(removeID)) {
                                            kichCoGiayCTList.remove(i);
                                            kichCoAdapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                });
    }

    private void changeData(GiayChiTiet giayChiTiet, ImageView ivAnh, TextView tvTen, TextView tvMau, TextView tvGia) {
        FirebaseFirestore.getInstance().collection("GiayChiTiet")
                .document(giayChiTiet.getMaGiayChiTiet())
                .addSnapshotListener(((value, error) -> {
                    if (error != null) {
                        return;
                    }
                    if (value != null && value.exists()) {
                        String anhGiayCT = value.getString("anhGiayChiTiet");
                        String tenGiayCT = value.getString("tenGiayChiTiet");
                        String mauSac = value.getString("mauSac");
                        long getDonGia = value.getLong("gia");
                        int gia = (int) getDonGia;

                        if (getActivity() != null) {

                            Glide.with(getContext()).load(anhGiayCT).into(ivAnh);
                            tvTen.setText(tenGiayCT);
                            tvMau.setText(mauSac);
                            tvGia.setText(FormatCurrency.formatCurrency(gia) + "₫");
                        }
                    }
                }));
    }

    private void openDiaLogDoiTrangThai(int status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Cảnh báo");
        builder.setMessage("Đây là thay đổi có ảnh hưởng lớn đến hệ thống, cần cân nhắc kỹ trước khi xác nhận");

        builder.setPositiveButton("Xác nhận", ((dialog, which) -> {
            giayChiTiet.setTrangThaiGiayChiTiet(status);
            new GiayChiTietDAO(getContext()).capNhatGiayChiTiet(giayChiTiet);
            Toast.makeText(getContext(), "Cập nhật phân loại thành công", Toast.LENGTH_SHORT).show();
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
        dialog.setContentView(R.layout.fragment_bottom_sheet_dialog_detail_giay_c_t);

        FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.bottom_sheet_dialog_background);

        return dialog;
    }
}