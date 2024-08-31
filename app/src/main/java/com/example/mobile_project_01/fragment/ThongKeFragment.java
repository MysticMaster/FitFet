package com.example.mobile_project_01.fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobile_project_01.R;
import com.example.mobile_project_01.adapter.QLGiayChiTietAdapter;
import com.example.mobile_project_01.function.FormatCurrency;
import com.example.mobile_project_01.model.DonHang;
import com.example.mobile_project_01.model.DonHangChiTiet;
import com.example.mobile_project_01.model.GiayChiTiet;
import com.example.mobile_project_01.model.ThongKe;
import com.example.mobile_project_01.model.ThuongHieu;
import com.example.mobile_project_01.model.Top;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class ThongKeFragment extends Fragment {
    public ThongKeFragment() {
        // Required empty public constructor
    }

    private BarChart barChartThongKe;
    private LineChart lineChartThongKe;

    private List<ThongKe> thongKeList;
    private TextView tvSoDon, tvDoanhSo, tvDoanhThu, tvSoLuongTop1, tvPhanTram;
    private QLGiayChiTietAdapter adapter;
    private List<GiayChiTiet> giayChiTietList;

    private EditText edtTuNgay, edtDenNgay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_thong_ke, container, false);
        barChartThongKe = view.findViewById(R.id.barChartThongKe);
        lineChartThongKe = view.findViewById(R.id.lineChartThongKe);
        thongKeList = new ArrayList<>();

        LinearLayout llListLoc = view.findViewById(R.id.llListLoc);
        edtTuNgay = view.findViewById(R.id.edtTuNgay);
        edtDenNgay = view.findViewById(R.id.edtDenNgay);
        ImageButton iBtnMoLocTK = view.findViewById(R.id.iBtnMoLocTK);
        LinearLayout llFunctionLoc = view.findViewById(R.id.llFunctionLoc);
        ImageButton iBtnLocTK = view.findViewById(R.id.iBtnLocTK);
        ImageButton iBtnCancelLocTK = view.findViewById(R.id.iBtnCancelLocTK);

        //-------------------------------------------------------------------------------------------

        RecyclerView rvSanPhamTop1 = view.findViewById(R.id.rvSanPhamTop1);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        giayChiTietList = new ArrayList<>();
        adapter = new QLGiayChiTietAdapter(getContext(), giayChiTietList, getChildFragmentManager());
        rvSanPhamTop1.setLayoutManager(manager);
        rvSanPhamTop1.setAdapter(adapter);

        //-------------------------------------------------------------------------------------------

        tvSoDon = view.findViewById(R.id.tvSoDon);
        tvDoanhSo = view.findViewById(R.id.tvDoanhSo);
        tvDoanhThu = view.findViewById(R.id.tvDoanhThu);
        tvSoLuongTop1 = view.findViewById(R.id.tvSoLuongTop1);
        tvPhanTram = view.findViewById(R.id.tvPhanTram);

        //-------------------------------------------------------------------------------------------

        TextView tvLoaiThongKe = view.findViewById(R.id.tvLoaiThongKe);
        ImageButton iBtnChangeThongKe = view.findViewById(R.id.iBtnChangeThongKe);
        iBtnChangeThongKe.setOnClickListener(v -> {
            if (barChartThongKe.getVisibility() == View.VISIBLE) {
                tvLoaiThongKe.setText("Biểu đồ doanh thu");
                barChartThongKe.setVisibility(View.INVISIBLE);
                lineChartThongKe.setVisibility(View.VISIBLE);
            } else if (lineChartThongKe.getVisibility() == View.VISIBLE) {
                tvLoaiThongKe.setText("Biểu đồ doanh số");
                barChartThongKe.setVisibility(View.VISIBLE);
                lineChartThongKe.setVisibility(View.INVISIBLE);
            }
        });

        //-------------------------------------------------------------------------------------------
        iBtnMoLocTK.setOnClickListener(v -> {
            iBtnMoLocTK.setVisibility(View.GONE);
            llFunctionLoc.setVisibility(View.VISIBLE);
            llListLoc.setVisibility(View.VISIBLE);
        });

        edtTuNgay.setOnClickListener(v -> {
            showDatePickerDialog(edtTuNgay);
        });

        edtDenNgay.setOnClickListener(v -> {
            showDatePickerDialog(edtDenNgay);
        });

        iBtnLocTK.setOnClickListener(v -> {
            thongKeList.clear();

            locThongKe();
        });

        iBtnCancelLocTK.setOnClickListener(v -> {
            edtTuNgay.setText("");
            edtDenNgay.setText("");
            thongKeList.clear();

            tongThongKe();

            iBtnMoLocTK.setVisibility(View.VISIBLE);
            llFunctionLoc.setVisibility(View.GONE);
            llListLoc.setVisibility(View.GONE);
        });

        //-------------------------------------------------------------------------------------------

        tongThongKe();
        return view;
    }

    private void setDataChart() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<Entry> lineEntries = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            int doanhSo = 0;
            int doanhThu = 0;
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            for (ThongKe thongKe : thongKeList) {
                String date = thongKe.getNgay();
                LocalDate localDate = LocalDate.parse(date, dateFormatter);
                int month = localDate.getMonthValue();
                if (month == (i + 1)) {
                    doanhSo += thongKe.getSoLuongDaBan();
                    doanhThu += thongKe.getDoanhThu();
                }
            }
            barEntries.add(new BarEntry((float) i, (float) doanhSo));
            lineEntries.add(new Entry((float) i, (float) doanhThu));

            BarDataSet barDataSet = new BarDataSet(barEntries, "Doanh số");
            LineDataSet lineDataSet = new LineDataSet(lineEntries, "Doanh thu");

            int gray = Color.parseColor("#C0C0C0");
            barDataSet.setColor(gray);
            lineDataSet.setColor(Color.RED);

            BarData barData = new BarData(barDataSet);
            LineData lineData = new LineData(lineDataSet);

            barChartThongKe.setData(barData);
            barChartThongKe.getDescription().setEnabled(false);

            barChartThongKe.getLegend().setEnabled(true);
            barChartThongKe.setDrawGridBackground(false);
            barChartThongKe.getXAxis().setDrawGridLines(false);

            XAxis barChartThongKeXAxis = barChartThongKe.getXAxis();
            barChartThongKeXAxis.setTextSize(7f);
            ArrayList<String> barRow = new ArrayList<>(Arrays.asList(
                    "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
            ));

            barChartThongKeXAxis.setValueFormatter(new IndexAxisValueFormatter(barRow));
            barChartThongKeXAxis.setGranularity(0f);
            barChartThongKeXAxis.setLabelCount(barRow.size());
            barChartThongKeXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            // Tùy chỉnh đơn vị cho trục Y bên trái
            YAxis barLeftAxis = barChartThongKe.getAxisLeft();
            barLeftAxis.setTextSize(7f);
            barLeftAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    return String.format(Locale.getDefault(), "%.0f SP", value);
                }
            });
            barLeftAxis.setDrawGridLines(true);

            barChartThongKe.getAxisRight().setDrawLabels(false);

            //-------------------------------------------------------------------------------

            lineChartThongKe.setData(lineData);
            lineChartThongKe.getDescription().setEnabled(false);
            lineChartThongKe.getLegend().setEnabled(true);
            lineChartThongKe.setDrawGridBackground(false);
            lineChartThongKe.getXAxis().setDrawGridLines(false);

            XAxis lineChartThongKeXAxis = lineChartThongKe.getXAxis();
            lineChartThongKeXAxis.setTextSize(7f);
            ArrayList<String> lineRow = new ArrayList<>(Arrays.asList(
                    "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
            ));

            lineChartThongKeXAxis.setValueFormatter(new IndexAxisValueFormatter(lineRow));
            lineChartThongKeXAxis.setGranularity(1f);
            lineChartThongKeXAxis.setLabelCount(lineRow.size());
            lineChartThongKeXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            // Tùy chỉnh đơn vị cho trục Y bên phải
            YAxis rightAxis = lineChartThongKe.getAxisRight();
            rightAxis.setTextSize(7f);
            rightAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    int doanhThu = (int) value;
                    return convertToShortScale(doanhThu) + "₫";
                }
            });

            lineChartThongKe.getAxisLeft().setDrawAxisLine(false);
            lineChartThongKe.getAxisLeft().setDrawLabels(false);

            barChartThongKe.invalidate();
            lineChartThongKe.invalidate();
        }
    }

    private String convertToShortScale(int value) {
        if (value < 1000) {
            return String.valueOf(value);
        } else if (value < 1000000) {
            return String.format(Locale.getDefault(), "%.1fM", value / 1000000.0);
        } else {
            return String.format(Locale.getDefault(), "%.1fM", value / 1000000.0);
        }
    }

    private void tongThongKe() {
        thongKeList.clear();

        FirebaseFirestore.getInstance().collection("ThongKe")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }

                    if (value != null) {
                        for (DocumentChange change : value.getDocumentChanges()) {
                            switch (change.getType()) {
                                case ADDED:
                                    DocumentSnapshot addSnapshot = change.getDocument();
                                    thongKeList.add(addSnapshot.toObject(ThongKe.class));
                                    break;
                            }
                        }
                        setDataChart();
                        setAllData();
                    }
                });
    }

    private void locThongKe() {
        thongKeList.clear();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        if (edtTuNgay.getText().toString().isEmpty() || edtDenNgay.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ ngày bắt đầu và kết thúc", Toast.LENGTH_SHORT).show();
        } else {
            LocalDate start = LocalDate.parse(edtTuNgay.getText().toString(), dateFormatter);
            LocalDate end = LocalDate.parse(edtDenNgay.getText().toString(), dateFormatter);

            if (start.isAfter(end)) {
                Toast.makeText(getContext(), "Khoảng ngày không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseFirestore.getInstance().collection("ThongKe")
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            return;
                        }

                        if (value != null) {
                            for (DocumentChange change : value.getDocumentChanges()) {
                                switch (change.getType()) {
                                    case ADDED:
                                        DocumentSnapshot addSnapshot = change.getDocument();
                                        ThongKe thongKe = addSnapshot.toObject(ThongKe.class);
                                        LocalDate ngay = LocalDate.parse(thongKe.getNgay(), dateFormatter);

                                        if (ngay.isAfter(start.minusDays(1))
                                                && ngay.isBefore(end.plusDays(1))) {
                                            thongKeList.add(thongKe);
                                        }
                                        break;
                                }
                                setDataChart();
                                setAllData();
                            }
                        }
                    });
        }
    }


    private void setAllData() {
        giayChiTietList.clear();

        if (!thongKeList.isEmpty()) {

            FirebaseFirestore.getInstance().collection("GiayChiTiet")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<GiayChiTiet> readList = new ArrayList<>();
                            List<Top> topList = new ArrayList<>();
                            for (DocumentSnapshot snapshot : task.getResult()) {
                                readList.add(snapshot.toObject(GiayChiTiet.class));
                            }

                            int doanhSo = 0;
                            int doanhThu = 0;
                            for (ThongKe thongKe : thongKeList) {
                                doanhSo += thongKe.getSoLuongDaBan();
                                doanhThu += thongKe.getDoanhThu();
                            }

                            tvSoDon.setText(String.valueOf(thongKeList.size()));
                            tvDoanhSo.setText(String.valueOf(doanhSo));
                            tvDoanhThu.setText(FormatCurrency.formatCurrency(doanhThu) + "₫");

                            for (GiayChiTiet giayChiTiet : readList) {
                                int count = 0;
                                for (ThongKe thongKe : thongKeList) {
                                    if (thongKe.getMaGiayChiTiet().equals(giayChiTiet.getMaGiayChiTiet())) {
                                        count += thongKe.getSoLuongDaBan();
                                    }
                                }
                                topList.add(new Top(giayChiTiet.getMaGiayChiTiet(), count));
                            }

                            if (!topList.isEmpty()) {
                                Collections.sort(topList, (o1, o2) -> o2.getCount() - o1.getCount());

                                String maGiayCTTop1 = topList.get(0).getMaGiayCT();

                                DocumentReference reference = FirebaseFirestore.getInstance()
                                        .collection("GiayChiTiet").document(maGiayCTTop1);
                                reference.get().addOnCompleteListener(task1 -> {
                                    GiayChiTiet giayChiTiet = task1.getResult().toObject(GiayChiTiet.class);
//                                    giayChiTietList.clear();
                                    giayChiTietList.add(giayChiTiet);
                                    adapter.notifyDataSetChanged();
                                });

                                int top1DoanhSo = 0;
                                int top1DoanhThu = 0;
                                for (ThongKe thongKe : thongKeList) {
                                    if (thongKe.getMaGiayChiTiet().equals(maGiayCTTop1)) {
                                        top1DoanhSo += thongKe.getSoLuongDaBan();
                                        top1DoanhThu += thongKe.getDoanhThu();
                                    }
                                }

                                tvSoLuongTop1.setText("Đã bán " + top1DoanhSo + " chiếc");
                                double tiLe = (double) top1DoanhThu / doanhThu * 100;
                                int tiLeDoanhThu = (int) tiLe;
                                tvPhanTram.setText("Chiếm " + tiLeDoanhThu + "% trên tổng doanh thu");
                            }

                        }
                    });
        } else {
            adapter.notifyDataSetChanged();
            tvSoLuongTop1.setText("0");
            tvPhanTram.setText("0%");
            tvSoDon.setText("0");
            tvDoanhSo.setText("0");
            tvDoanhThu.setText("0₫");
        }
    }

    private void showDatePickerDialog(EditText editText) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        editText.setText(LocalDate.of(year, month + 1, dayOfMonth).format(dateFormatter));
                    }
                },
                year, month, dayOfMonth);

        datePickerDialog.show();
    }
}
