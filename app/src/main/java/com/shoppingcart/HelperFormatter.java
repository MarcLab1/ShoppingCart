package com.shoppingcart;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

public final class HelperFormatter {
    private static NumberFormat df = new DecimalFormat("#0.00");

    public static double getTotal(double val)
    {
        return Double.valueOf(df.format(val));
    }

    public static String getTotalString(double val)
    {
        return "$ " + String.format("%.2f", Double.valueOf(df.format(val)));
    }

    public static String getFormattedDateEEEMMMdyyyhmma(long date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d, yyyy h:mm a");
        return sdf.format(date);
    }
    public static String getSaveString(double val)
    {
        return  "SAVE $ " + String.format("%.2f", Double.valueOf(df.format(val)));
    }



}
