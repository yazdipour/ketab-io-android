package io.github.yazdipour.ketabdlr.services;

import android.annotation.SuppressLint;
import android.content.Context;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class Api {
    private Context context;
    public static final String BASE_URL = "https://ketab.io";

    public Api(Context context) {
        this.context = context;
    }

    public void request(String url, FutureCallback<String> callback) {
        Ion.with(context)
                .load(url)
                .asString()
                .setCallback(callback);
    }

    @SuppressLint("DefaultLocale")
    public void getSearch(String q, int page, FutureCallback<String> callback) {
        request(String.format("%s/search?q=%s&page=%d", BASE_URL, q, page), callback);
    }
}
