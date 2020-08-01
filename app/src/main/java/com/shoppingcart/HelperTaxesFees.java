package com.shoppingcart;

import java.util.Calendar;

public final class HelperTaxesFees
{
    private static Calendar calendar;

    public static double getTaxes()
    {
        return 0.13;
    }
    public static double getFees()
    {
        calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        if(day == Calendar.THURSDAY || day == Calendar.FRIDAY )
            return 3.0;

        else if(day == Calendar.SATURDAY || day == Calendar.SUNDAY )
            return 5.0;

        else
            return 0.0;
    }

    public static String getFeesString(double fees)
    {
        if (fees==0)
            return "FREE";
        return "$ " + String.format("%.2f", fees) ;
    }

    public static String getTaxesString(double taxes)
    {
        return  String.format("%.0f", taxes*100 ) + "%";
    }

}

