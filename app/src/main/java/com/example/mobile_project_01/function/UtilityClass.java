package com.example.mobile_project_01.function;

import android.content.Context;
import android.util.DisplayMetrics;

public class UtilityClass {

    public static int calculateSpacing(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        int numColumns = 2;

        int spacingDp = 5;

        int spacingPx = Math.round(spacingDp * context.getResources().getDisplayMetrics().density);

        int totalSpacing = spacingPx * (numColumns - 14);
        int itemWidth = (int) ((dpWidth - totalSpacing) / numColumns);

        return (int) ((dpWidth - itemWidth) / (numColumns * 2));
    }

}
