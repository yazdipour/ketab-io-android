package io.github.yazdipour.ketabdlr.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringUtils {
    public static String farsiNumbersToEnglish(String farsiStr) {
        String[] english = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String[] farsi = new String[]{"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹"};
        for (int i = 0; i < farsi.length; i++)
            farsiStr = farsiStr.replaceAll(farsi[i], english[i]);
        return farsiStr;
    }

    public static String getRawString(Context context, int id) throws Exception {
        InputStream inputStream = context.getResources().openRawResource(id);
        StringBuilder strBuild = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) strBuild.append(line);
        }
        return strBuild.toString();
    }

    public static Boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
