package com.example.admin.utils;

import android.os.Looper;

import com.example.admin.context.GlobalContext;
import com.google.android.gms.common.util.concurrent.HandlerExecutor;

import java.util.concurrent.Executors;

import okhttp3.Request;
import okhttp3.Response;

public class HttpCallHelper {

    public static void executeHttpCall(final Request.Builder requestBuilder,
                                       final Runnable postFinishRunnable) {

        final Request httpPost = requestBuilder
                .header("digi1key", GlobalContext.BIJAY_1)
                .build();

        final Response[] httpResponse = new Response[1];

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                int statusCode = 0;
                String jsonResponse = null;
                int maxRetries = 3, count = 0, success = 0;

                while (success == 0 && count < maxRetries) {
                    try {
                        httpResponse[0] = GlobalContext.httpClient.newCall(httpPost).execute();
                        statusCode = httpResponse[0].code();
                        jsonResponse = httpResponse[0].body().string();
                        success = 1;
                    } catch (Exception e) {
                        e.printStackTrace();
                        jsonResponse = e.toString();
                        count++;
                    }
                }

                // Pass status and response to caller class.
                ((MyRunnable)postFinishRunnable).preRun(statusCode, jsonResponse);

                // Execute PostFinishRunnable on main thread
                final HandlerExecutor mainThreadExecutor = new HandlerExecutor(Looper.getMainLooper());
                mainThreadExecutor.execute(postFinishRunnable);
            }
        });
    }
}
