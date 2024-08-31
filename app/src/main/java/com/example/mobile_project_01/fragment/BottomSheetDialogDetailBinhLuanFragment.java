package com.example.mobile_project_01.fragment;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mobile_project_01.DAO.PhanHoiDAO;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.adapter.PhanHoiAdapter;
import com.example.mobile_project_01.model.BinhLuan;
import com.example.mobile_project_01.model.KhachHang;
import com.example.mobile_project_01.model.PhanHoi;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class BottomSheetDialogDetailBinhLuanFragment extends BottomSheetDialogFragment {

    private BinhLuan binhLuan;

    private List<PhanHoi> phanHoiList;
    private PhanHoiAdapter adapter;

    public BottomSheetDialogDetailBinhLuanFragment() {
        // Required empty public constructor
    }

    public BottomSheetDialogDetailBinhLuanFragment(BinhLuan binhLuan) {
        this.binhLuan = binhLuan;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog_detail_binh_luan, container, false);

        ImageView ivDetailQLAvtKHBL = view.findViewById(R.id.ivDetailQLAvtKHBL);
        TextView tvDetailQLTenKHBL = view.findViewById(R.id.tvDetailQLTenKHBL);
        TextView tvDetailQLNDBinhLuan = view.findViewById(R.id.tvDetailQLNDBinhLuan);
        ImageButton iBtnCLoseDetailBinhLuan = view.findViewById(R.id.iBtnCLoseDetailBinhLuan);

        DocumentReference KHReference = FirebaseFirestore.getInstance()
                .collection("KhachHang").document(binhLuan.getMaKhachHang());
        KHReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                KhachHang khachHang = task.getResult().toObject(KhachHang.class);
                if (khachHang.getAnhKhachHang().equals("0")) {
                    ivDetailQLAvtKHBL.setImageResource(R.drawable.none_image);
                } else {
                    Glide.with(getContext()).load(khachHang.getAnhKhachHang()).into(ivDetailQLAvtKHBL);
                }
                tvDetailQLTenKHBL.setText(khachHang.getHoKhachHang() + " " + khachHang.getTenKhachHang());
            }
        });

        tvDetailQLNDBinhLuan.setText(binhLuan.getNoiDung());

        EditText edtNoiDungPhanHoi = view.findViewById(R.id.edtNoiDungPhanHoi);
        ImageButton iBtnGuiBinhLuan = view.findViewById(R.id.iBtnGuiBinhLuan);

        iBtnGuiBinhLuan.setOnClickListener(v -> {
            if (!edtNoiDungPhanHoi.getText().toString().isEmpty()) {
                PhanHoi phanHoi = new PhanHoi(UUID.randomUUID().toString(), binhLuan.getMaBinhLuan(),
                        edtNoiDungPhanHoi.getText().toString());
                new PhanHoiDAO(getContext()).taoPhanHoi(phanHoi);
                edtNoiDungPhanHoi.setText(null);
            }
        });

        RecyclerView rvDanhSachPhanHoi = view.findViewById(R.id.rvDanhSachPhanHoi);
        phanHoiList = new ArrayList<>();
        adapter = new PhanHoiAdapter(getContext(), phanHoiList, true);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvDanhSachPhanHoi.setLayoutManager(manager);
        rvDanhSachPhanHoi.setAdapter(adapter);

        iBtnCLoseDetailBinhLuan.setOnClickListener(v -> {
            dismiss();
        });

        layDanhSachPhanHoi(binhLuan.getMaBinhLuan());
        return view;
    }

    private void layDanhSachPhanHoi(String maBinhLuan) {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("PhanHoi");

        reference.whereEqualTo("maBinhLuan", maBinhLuan)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e(TAG, "Đã có lỗi: ", error);
                        return;
                    }

                    if (value != null) {
                        for (DocumentChange change : value.getDocumentChanges()) {
                            switch (change.getType()) {
                                case ADDED:
                                    DocumentSnapshot addSnapshot = change.getDocument();
                                    PhanHoi phanHoi = addSnapshot.toObject(PhanHoi.class);
                                    phanHoiList.add(phanHoi);
                                    adapter.notifyDataSetChanged();
                                    break;
                                case MODIFIED:
                                    DocumentSnapshot modiSnapshot = change.getDocument();
                                    PhanHoi modiPhanHoi = modiSnapshot.toObject(PhanHoi.class);
                                    for (int i = 0; i < phanHoiList.size(); i++) {
                                        PhanHoi phanHoiIndex = phanHoiList.get(i);
                                        if (phanHoiIndex.getMaPhanHoi().equals(modiPhanHoi.getMaPhanHoi())) {
                                            phanHoiList.set(i, modiPhanHoi);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                                case REMOVED:
                                    String removeID = change.getDocument().getId();
                                    for (int i = 0; i < phanHoiList.size(); i++) {
                                        if (phanHoiList.get(i).getMaPhanHoi().equals(removeID)) {
                                            phanHoiList.remove(i);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setContentView(R.layout.fragment_bottom_sheet_dialog_detail_binh_luan);

        FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.bottom_sheet_dialog_background);

        return dialog;
    }

}