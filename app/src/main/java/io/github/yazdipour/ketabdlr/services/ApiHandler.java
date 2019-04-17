package io.github.yazdipour.ketabdlr.services;

import android.content.Context;

public class ApiHandler {
    private static Api api;
    public static Api getApi(Context context) {
        if (api == null) api = new Api(context);
        return api;
    }
}
