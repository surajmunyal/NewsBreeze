package com.greedygame.newsbreeze.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static androidx.core.content.ContextCompat.getSystemService;

public class CommonUtils {
    public static String getDateFromDateTime(String fromFormatDate, String toDate, String dateTime) {
        String strDate = null;
        try {
            SimpleDateFormat dt = new SimpleDateFormat(fromFormatDate, Locale.ENGLISH);
            Date parsedDate = dt.parse(dateTime);
            DateFormat df = new SimpleDateFormat(toDate,Locale.ENGLISH);
            strDate = df.format(parsedDate);
        } catch (ParseException e) {
            Log.e("TAG", e.toString());
        }
        return strDate;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            NetworkCapabilities cap = cm.getNetworkCapabilities(cm.getActiveNetwork());
            if (cap == null) return false;
            return cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = cm.getAllNetworks();
            for (Network n: networks) {
                NetworkInfo nInfo = cm.getNetworkInfo(n);
                if (nInfo != null && nInfo.isConnected()) return true;
            }
        } else {
            NetworkInfo[] networks = cm.getAllNetworkInfo();
            for (NetworkInfo nInfo: networks) {
                if (nInfo != null && nInfo.isConnected()) return true;
            }
        }

        return false;
    }

}
