package io.github.yazdipour.ketabdlr.services;

import android.annotation.SuppressLint;
import android.content.Context;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

public class Api {
    private Context context;
    public static final String BASE_URL = "https://ketab.io";

    public Api(Context context) {
        this.context = context;
    }

    public void request(String url,
                        FutureCallback<Response<String>> callback) {
        Ion.with(context)
                .load(url)
                .asString()
                .withResponse()
                .setCallback(callback);
    }

    public void request(String url,
                        String headerKey,
                        String headerValue,
                        FutureCallback<Response<String>> callback) {
        Ion.with(context)
                .load(url)
                .setHeader(headerKey, headerValue)
                .asString()
                .withResponse()
                .setCallback(callback);
    }

    public void request(String url,
                        String cookie,
                        FutureCallback<Response<String>> callback) {
        request(url, "Cookie", cookie, callback);
    }

    @SuppressLint("DefaultLocale")
    public void getSearch(String q, int page, FutureCallback<Response<String>> callback) {
        request(String.format("%s/search?q=%s&page=%d", BASE_URL, q, page), callback);
    }
}
