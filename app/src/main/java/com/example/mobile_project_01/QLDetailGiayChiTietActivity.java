package com.example.mobile_project_01;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mobile_project_01.DAO.GiayChiTietDAO;
import com.example.mobile_project_01.DAO.KichCoGiayCTDAO;
import com.example.mobile_project_01.adapter.KichCoAdapter;
import com.example.mobile_project_01.adapter.KichCoPickUpAdapter;
import com.example.mobile_project_01.function.AutoStringID;
import com.example.mobile_project_01.function.FormatCurrency;
import com.example.mobile_project_01.model.Giay;
import com.example.mobile_project_01.model.GiayChiTiet;
import com.example.mobile_project_01.model.KichCoGiayCT;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QLDetailGiayChiTietActivity extends AppCompatActivity {

    private GiayChiTiet giayChiTiet;

    private Uri uri;

    private KichCoAdapter kichCoAdapter;
    private KichCoPickUpAdapter pickUpAdapter;
    private List<KichCoGiayCT> kichCoGiayCTList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qldetail_giay_chi_tiet);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            giayChiTiet = (GiayChiTiet) bundle.getSerializable("giayChiTiet");
        }

        ImageButton iBtnThoatEditGiayCT = findViewById(R.id.iBtnThoatEditGiayCT);
        iBtnThoatEditGiayCT.setOnClickListener(v -> {
            finish();
        });

        //-------------------------------------------------------------------------------------------
        GiayChiTietDAO giayChiTietDAO = new GiayChiTietDAO(this);

        ImageView ivEditAnhGiayCT = findViewById(R.id.ivEditAnhGiayCT);
        ImageButton iBtnChangeAnhGiayCT = findViewById(R.id.iBtnChangeAnhGiayCT);
        ImageButton iBtnDoneEditAnhGiayCT = findViewById(R.id.iBtnDoneEditAnhGiayCT);

        if (giayChiTiet.getAnhGiayChiTiet().equals("0")) {
            ivEditAnhGiayCT.setImageResource(R.drawable.none_image);
        } else {
            Glide.with(this).load(giayChiTiet.getAnhGiayChiTiet()).into(ivEditAnhGiayCT);
        }


        StorageReference reference = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = reference.child("khoHinhGiayCT/" + AutoStringID.createStringID("ANHSPCT-"));

        ActivityResultLauncher<String> launcher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri o) {
                        if (o != null) {
                            uri = o;
                            try {
                                ContentResolver resolver = getContentResolver();
                                InputStream inputStream = resolver.openInputStream(uri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                ivEditAnhGiayCT.setImageBitmap(bitmap);
                            } catch (IOException ex) {
                                Log.e(TAG, "Lỗi hình: " + ex);
                            }
                        }
                    }
                }
        );

        iBtnChangeAnhGiayCT.setOnClickListener((v -> {
            launcher.launch("image/*");
        }));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.item_loading_dialog, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00FFFFFF")));

        iBtnDoneEditAnhGiayCT.setOnClickListener(v -> {
            if (uri != null) {
                dialog.show();
                UploadTask uploadTask = imageRef.putFile(uri);

                uploadTask.addOnSuccessListener((taskSnapshot -> {
                    Task<Uri> uriTask = imageRef.getDownloadUrl();

                    uriTask.addOnSuccessListener((uri1 -> {

                        if (!giayChiTiet.getAnhGiayChiTiet().equals("0")) {
                            FirebaseStorage.getInstance().getReferenceFromUrl(giayChiTiet.getAnhGiayChiTiet())
                                    .delete();
                        }
                        String anhGiayCT = uri1.toString();
                        giayChiTiet.setAnhGiayChiTiet(anhGiayCT);
                        giayChiTietDAO.capNhatGiayChiTiet(giayChiTiet);
                        Toast.makeText(this, "Cập nhật phân loại thành công", Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 500);
                    }));
                }));
            }
        });

        //------------------------------------------------------------------------------------------
        TextView tvEditTenGiayCT = findViewById(R.id.tvEditTenGiayCT);
        EditText edtEditTenGiayCT = findViewById(R.id.edtEditTenGiayCT);
        ImageButton iBtnOpenEditTenGiayCT = findViewById(R.id.iBtnOpenEditTenGiayCT);
        LinearLayout llEditTenGiayCT = findViewById(R.id.llEditTenGiayCT);
        ImageButton iBtnDoneEditTenGiayCT = findViewById(R.id.iBtnDoneEditTenGiayCT);
        ImageButton iBtnCancelEditTenGiayCT = findViewById(R.id.iBtnCancelEditTenGiayCT);

        tvEditTenGiayCT.setText(giayChiTiet.getTenGiayChiTiet());
        edtEditTenGiayCT.setText(giayChiTiet.getTenGiayChiTiet());

        iBtnOpenEditTenGiayCT.setOnClickListener(v -> {
            iBtnOpenEditTenGiayCT.setVisibility(View.GONE);
            llEditTenGiayCT.setVisibility(View.VISIBLE);
            tvEditTenGiayCT.setVisibility(View.GONE);
            edtEditTenGiayCT.setVisibility(View.VISIBLE);
        });

        iBtnDoneEditTenGiayCT.setOnClickListener(v -> {
            if (!edtEditTenGiayCT.getText().toString().isEmpty()) {
                giayChiTiet.setTenGiayChiTiet(edtEditTenGiayCT.getText().toString());
                giayChiTietDAO.capNhatGiayChiTiet(giayChiTiet);
                Toast.makeText(this, "Cập nhật phân loại thành công", Toast.LENGTH_SHORT).show();
                tvEditTenGiayCT.setText(giayChiTiet.getTenGiayChiTiet());

                iBtnOpenEditTenGiayCT.setVisibility(View.VISIBLE);
                llEditTenGiayCT.setVisibility(View.GONE);
                tvEditTenGiayCT.setVisibility(View.VISIBLE);
                edtEditTenGiayCT.setVisibility(View.GONE);
            }
        });

        iBtnCancelEditTenGiayCT.setOnClickListener(v -> {
            iBtnOpenEditTenGiayCT.setVisibility(View.VISIBLE);
            llEditTenGiayCT.setVisibility(View.GONE);
            tvEditTenGiayCT.setVisibility(View.VISIBLE);
            edtEditTenGiayCT.setVisibility(View.GONE);
        });

        //--------------------------------------------------------------------------------------------
        TextView tvEditMauGiayCT = findViewById(R.id.tvEditMauGiayCT);
        EditText edtEditMauGiayCT = findViewById(R.id.edtEditMauGiayCT);
        ImageButton iBtnOpenEditMauGiayCT = findViewById(R.id.iBtnOpenEditMauGiayCT);
        LinearLayout llEditMauGiayCT = findViewById(R.id.llEditMauGiayCT);
        ImageButton iBtnDoneEditMauGiayCT = findViewById(R.id.iBtnDoneEditMauGiayCT);
        ImageButton iBtnCancelEditMauGiayCT = findViewById(R.id.iBtnCancelEditMauGiayCT);

        tvEditMauGiayCT.setText(giayChiTiet.getMauSac());
        edtEditMauGiayCT.setText(giayChiTiet.getMauSac());

        iBtnOpenEditMauGiayCT.setOnClickListener(v -> {
            iBtnOpenEditMauGiayCT.setVisibility(View.GONE);
            llEditMauGiayCT.setVisibility(View.VISIBLE);
            tvEditMauGiayCT.setVisibility(View.GONE);
            edtEditMauGiayCT.setVisibility(View.VISIBLE);
        });

        iBtnDoneEditMauGiayCT.setOnClickListener(v -> {
            if (!edtEditMauGiayCT.getText().toString().isEmpty()) {
                giayChiTiet.setMauSac(edtEditMauGiayCT.getText().toString());
                giayChiTietDAO.capNhatGiayChiTiet(giayChiTiet);
                Toast.makeText(this, "Cập nhật phân loại thành công", Toast.LENGTH_SHORT).show();
                tvEditMauGiayCT.setText(giayChiTiet.getMauSac());

                iBtnOpenEditMauGiayCT.setVisibility(View.VISIBLE);
                llEditMauGiayCT.setVisibility(View.GONE);
                tvEditMauGiayCT.setVisibility(View.VISIBLE);
                edtEditMauGiayCT.setVisibility(View.GONE);
            }
        });

        iBtnCancelEditMauGiayCT.setOnClickListener(v -> {
            iBtnOpenEditMauGiayCT.setVisibility(View.VISIBLE);
            llEditMauGiayCT.setVisibility(View.GONE);
            tvEditMauGiayCT.setVisibility(View.VISIBLE);
            edtEditMauGiayCT.setVisibility(View.GONE);
        });

        //------------------------------------------------------------------------------------------

        TextView tvEditGiaGiayCT = findViewById(R.id.tvEditGiaGiayCT);
        EditText edtEditGiaGiayCT = findViewById(R.id.edtEditGiaGiayCT);
        ImageButton iBtnOpenEditEditGiaGiayCT = findViewById(R.id.iBtnOpenEditEditGiaGiayCT);
        LinearLayout llEditGiaGiayCT = findViewById(R.id.llEditGiaGiayCT);
        ImageButton iBtnDoneEditGiaGiayCT = findViewById(R.id.iBtnDoneEditGiaGiayCT);
        ImageButton iBtnCancelEditGiaGiayCT = findViewById(R.id.iBtnCancelEditGiaGiayCT);

        tvEditGiaGiayCT.setText(FormatCurrency.formatCurrency(giayChiTiet.getGia()) + "₫");
        edtEditGiaGiayCT.setText(String.valueOf(giayChiTiet.getGia()));

        iBtnOpenEditEditGiaGiayCT.setOnClickListener(v -> {
            iBtnOpenEditEditGiaGiayCT.setVisibility(View.GONE);
            llEditGiaGiayCT.setVisibility(View.VISIBLE);
            tvEditGiaGiayCT.setVisibility(View.GONE);
            edtEditGiaGiayCT.setVisibility(View.VISIBLE);
        });

        iBtnDoneEditGiaGiayCT.setOnClickListener(v -> {
            if (!edtEditGiaGiayCT.getText().toString().isEmpty()) {
                giayChiTiet.setGia(Integer.parseInt(edtEditGiaGiayCT.getText().toString()));
                giayChiTietDAO.capNhatGiayChiTiet(giayChiTiet);
                Toast.makeText(this, "Cập nhật phân loại thành công", Toast.LENGTH_SHORT).show();
                tvEditGiaGiayCT.setText(FormatCurrency.formatCurrency(giayChiTiet.getGia()) + "₫");

                iBtnOpenEditEditGiaGiayCT.setVisibility(View.VISIBLE);
                llEditGiaGiayCT.setVisibility(View.GONE);
                tvEditGiaGiayCT.setVisibility(View.VISIBLE);
                edtEditGiaGiayCT.setVisibility(View.GONE);
            }
        });

        iBtnCancelEditGiaGiayCT.setOnClickListener(v -> {
            iBtnOpenEditEditGiaGiayCT.setVisibility(View.VISIBLE);
            llEditGiaGiayCT.setVisibility(View.GONE);
            tvEditGiaGiayCT.setVisibility(View.VISIBLE);
            edtEditGiaGiayCT.setVisibility(View.GONE);
        });
        //-------------------------------------------------------------------------------------------
        KichCoGiayCTDAO kichCoGiayCTDAO = new KichCoGiayCTDAO(this);

        RecyclerView rvEditKichCo = findViewById(R.id.rvEditKichCo);
        RecyclerView rvUpdateKichCo = findViewById(R.id.rvUpdateKichCo);
        ImageButton iBtnOpenEditKichCoGiayCT = findViewById(R.id.iBtnOpenEditKichCoGiayCT);
        LinearLayout llEditKichCoGiayCT = findViewById(R.id.llEditKichCoGiayCT);
        ImageButton iBtnDoneEditKichCoGiayCT = findViewById(R.id.iBtnDoneEditKichCoGiayCT);
        ImageButton iBtnCancelEditKichCoGiayCT = findViewById(R.id.iBtnCancelEditKichCoGiayCT);

        kichCoGiayCTList = new ArrayList<>();
        kichCoAdapter = new KichCoAdapter(kichCoGiayCTList, this);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvEditKichCo.setLayoutManager(manager);
        rvEditKichCo.setAdapter(kichCoAdapter);

        int arrCo[] = {36, 37, 38, 39, 40, 41, 42, 43, 44, 45};
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        pickUpAdapter = new KichCoPickUpAdapter(this, arrCo, kichCoGiayCTList, giayChiTiet, iBtnDoneEditKichCoGiayCT);
        rvUpdateKichCo.setLayoutManager(gridLayoutManager);
        rvUpdateKichCo.setAdapter(pickUpAdapter);

        iBtnOpenEditKichCoGiayCT.setOnClickListener(v -> {
            iBtnOpenEditKichCoGiayCT.setVisibility(View.GONE);
            llEditKichCoGiayCT.setVisibility(View.VISIBLE);
            rvEditKichCo.setVisibility(View.GONE);
            rvUpdateKichCo.setVisibility(View.VISIBLE);
        });

        List<KichCoGiayCT> listCoUpdate = pickUpAdapter.getSelectedItems();

        iBtnDoneEditKichCoGiayCT.setOnClickListener(v -> {
            FirebaseFirestore.getInstance().collection("KichCoGiayChiTiet")
                    .whereEqualTo("maGiayCT", giayChiTiet.getMaGiayChiTiet())
                    .get()
                    .addOnCompleteListener(task -> {
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            snapshot.getReference().delete();
                        }

                        for (KichCoGiayCT kichCoGiayCT : listCoUpdate) {
                            kichCoGiayCTDAO.themKichCo(kichCoGiayCT);
                        }
                        Toast.makeText(this, "Đã cập nhật danh sách kích cỡ", Toast.LENGTH_SHORT).show();

                        iBtnOpenEditKichCoGiayCT.setVisibility(View.VISIBLE);
                        llEditKichCoGiayCT.setVisibility(View.GONE);
                        rvEditKichCo.setVisibility(View.VISIBLE);
                        rvUpdateKichCo.setVisibility(View.GONE);
                    });
        });

        iBtnCancelEditKichCoGiayCT.setOnClickListener(v -> {
            iBtnOpenEditKichCoGiayCT.setVisibility(View.VISIBLE);
            llEditKichCoGiayCT.setVisibility(View.GONE);
            rvEditKichCo.setVisibility(View.VISIBLE);
            rvUpdateKichCo.setVisibility(View.GONE);
        });
        //-----------------------------------------------------------------------------------------------
        RelativeLayout rlEditSLGiayCT = findViewById(R.id.rlEditSLGiayCT);
        TextView tvEditSLGiayCT = findViewById(R.id.tvEditSLGiayCT);
        ImageButton iBtnOpenEditSLGiayCT = findViewById(R.id.iBtnOpenEditSLGiayCT);

        kichCoAdapter.setOnItemClickListener(kichCoGiayCT -> {
            rlEditSLGiayCT.setVisibility(View.VISIBLE);

            tvEditSLGiayCT.setText(String.valueOf(kichCoGiayCT.getSoLuong()));
            iBtnOpenEditSLGiayCT.setOnClickListener(v -> {
                openDialogUpdateSoLuong(giayChiTiet, kichCoGiayCT, tvEditSLGiayCT);
            });
        });

        layDanhSachKichCo(giayChiTiet.getMaGiayChiTiet());
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

    private void openDialogUpdateSoLuong(GiayChiTiet giayChiTiet, KichCoGiayCT kichCoGiayCT, TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.item_dialog_update_so_luong_giay_ct, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        ImageView ivDialogAnhGiayCT = view.findViewById(R.id.ivDialogAnhGiayCT);
        TextView tvDialogTenGiay = view.findViewById(R.id.tvDialogTenGiay);
        TextView tvDialogTenGiayGT = view.findViewById(R.id.tvDialogTenGiayGT);
        TextView tvDialogMauGiayGT = view.findViewById(R.id.tvDialogMauGiayGT);
        TextView tvDialogCoGiayGT = view.findViewById(R.id.tvDialogCoGiayGT);
        EditText edtDialogSoLuongGiayCT = view.findViewById(R.id.edtDialogSoLuongGiayCT);
        CardView cvUpdateDialogSoLuong = view.findViewById(R.id.cvUpdateDialogSoLuong);
        CardView cvDongDialogSoLuong = view.findViewById(R.id.cvDongDialogSoLuong);

        if (giayChiTiet.getAnhGiayChiTiet().equals("0")) {
            ivDialogAnhGiayCT.setImageResource(R.drawable.none_image);
        } else {
            Glide.with(this).load(giayChiTiet.getAnhGiayChiTiet()).into(ivDialogAnhGiayCT);
        }

        FirebaseFirestore.getInstance()
                .collection("Giay").document(giayChiTiet.getMaGiay())
                .get().addOnCompleteListener(task -> {
                    Giay giay = task.getResult().toObject(Giay.class);
                    tvDialogTenGiay.setText("Giày: " + giay.getTenGiay());
                });


        tvDialogTenGiayGT.setText("Loại: " + giayChiTiet.getTenGiayChiTiet());
        tvDialogMauGiayGT.setText("Màu: " + giayChiTiet.getMauSac());
        tvDialogCoGiayGT.setText("Cỡ: " + kichCoGiayCT.getKichCo());

        edtDialogSoLuongGiayCT.setText(String.valueOf(kichCoGiayCT.getSoLuong()));

        cvUpdateDialogSoLuong.setOnClickListener(v -> {
            if (!edtDialogSoLuongGiayCT.getText().toString().trim().isEmpty()) {
                kichCoGiayCT.setSoLuong(Integer.parseInt(edtDialogSoLuongGiayCT.getText().toString().trim()));
                new KichCoGiayCTDAO(this).capNhatKichCo(kichCoGiayCT);
                textView.setText(String.valueOf(kichCoGiayCT.getSoLuong()));
                Toast.makeText(this, "Số lượng của cỡ " + kichCoGiayCT.getKichCo() +
                        " đã được thay đổi thành " + kichCoGiayCT.getSoLuong(), Toast.LENGTH_SHORT).show();
                pickUpAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        cvDongDialogSoLuong.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

}