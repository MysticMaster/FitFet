package com.example.mobile_project_01.function;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatCurrency {
    public static String formatCurrency(int i) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

        numberFormat.setMaximumFractionDigits(0);

        return numberFormat.format(i);
    }
}
