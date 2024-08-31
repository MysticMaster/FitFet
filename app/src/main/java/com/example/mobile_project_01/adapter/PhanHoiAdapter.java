package com.example.mobile_project_01.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_project_01.DAO.BinhLuanDAO;
import com.example.mobile_project_01.DAO.PhanHoiDAO;
import com.example.mobile_project_01.R;
import com.example.mobile_project_01.model.BinhLuan;
import com.example.mobile_project_01.model.PhanHoi;
import com.example.mobile_project_01.model.ThanhVien;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class PhanHoiAdapter extends RecyclerView.Adapter<PhanHoiAdapter.ViewHolder> {

    private final Context context;
    private List<PhanHoi> phanHoiList;

    private Boolean loai;

    public PhanHoiAdapter(Context context, List<PhanHoi> phanHoiList, Boolean loai) {
        this.context = context;
        this.phanHoiList = phanHoiList;
        this.loai = loai;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_phan_hoi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PhanHoi phanHoi = phanHoiList.get(position);

        if (phanHoi != null) {

            holder.tvNoiDungPhanHoi.setText(phanHoi.getNoiDung());
            holder.edtEditPhanHoi.setText(phanHoi.getNoiDung());

            if (loai) {
                holder.iBtnFunctionPhanHoi.setVisibility(View.VISIBLE);
            }

            holder.iBtnFunctionPhanHoi.setOnClickListener(v -> {
                showFunction(v, holder.edtEditPhanHoi, holder.tvNoiDungPhanHoi, holder.tvHuyEditPhanHoi,
                        holder.tvEditPhanHoi, phanHoi, holder.rlEditBinhLuan);
            });
        }
    }

    @Override
    public int getItemCount() {
        return phanHoiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNoiDungPhanHoi, tvHuyEditPhanHoi, tvEditPhanHoi;
        ImageButton iBtnFunctionPhanHoi;
        RelativeLayout rlEditBinhLuan;
        EditText edtEditPhanHoi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNoiDungPhanHoi = itemView.findViewById(R.id.tvNoiDungPhanHoi);
            tvHuyEditPhanHoi = itemView.findViewById(R.id.tvHuyEditPhanHoi);
            tvEditPhanHoi = itemView.findViewById(R.id.tvEditPhanHoi);
            iBtnFunctionPhanHoi = itemView.findViewById(R.id.iBtnFunctionPhanHoi);
            rlEditBinhLuan = itemView.findViewById(R.id.rlEditBinhLuan);
            edtEditPhanHoi = itemView.findViewById(R.id.edtEditPhanHoi);
        }
    }

    private void showFunction(View view, EditText editText, TextView textView, TextView tvHuy, TextView tvSave, PhanHoi phanHoi, RelativeLayout relativeLayout) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_extra_function, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.itemEdit) {
                relativeLayout.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                editPhanHoi(editText, textView, tvHuy, tvSave, phanHoi, relativeLayout);
                return true;
            } else if (item.getItemId() == R.id.itemDelete) {
                xoaPhanHoi(phanHoi);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    private void editPhanHoi(EditText editText, TextView textView, TextView tvHuy, TextView tvSave, PhanHoi phanHoi, RelativeLayout relativeLayout) {
        tvHuy.setOnClickListener(v -> {
            relativeLayout.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        });


        tvSave.setOnClickListener(v -> {
            if (!editText.getText().toString().isEmpty()) {
                phanHoi.setNoiDung(editText.getText().toString());
                new PhanHoiDAO(context).capNhatPhanHoi(phanHoi);

                textView.setText(editText.getText().toString());

                relativeLayout.setVisibility(View.GONE);
                editText.setText(null);
                textView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void xoaPhanHoi(PhanHoi phanHoi) {
        new PhanHoiDAO(context).xoaPhanHoi(phanHoi);
    }

}
