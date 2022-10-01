package com.example.admin.api;

import static com.example.admin.utils.Constants.BASE_URL;

import com.example.admin.utils.HttpCallHelper;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class WashService {
    private final static String WASH_SUBMIT_PATH = "/test/wash/submit";
    private final static String WASH_PICKUP_PATH = "/test/wash/pickup";
    private final static String LIST_WASHES_FOR_USER = "/test/wash/listbyuser";

    public WashService() {
    }

    public void submitWash(final String userId,
                          final String token,
                          final Long units,
                          final Runnable postFinishRunnable) {
        String jsonBody = String.format("{ \"userId\": \"%s\", \"token\": \"%s\", \"units\": \"%d\" }",
                userId, token, units);

        try {
            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"), jsonBody);

            Request.Builder httpPost = new Request.Builder()
                    .url(BASE_URL + WASH_SUBMIT_PATH)
                    .post(body);
            HttpCallHelper.executeHttpCall(httpPost, postFinishRunnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pickupWash(final String userId,
                           final Runnable postFinishRunnable) {
        String jsonBody = String.format("{ \"userId\": \"%s\" }",
                userId);

        try {
            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"), jsonBody);

            Request.Builder httpPost = new Request.Builder()
                    .url(BASE_URL + WASH_PICKUP_PATH)
                    .post(body);
            HttpCallHelper.executeHttpCall(httpPost, postFinishRunnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listWashesForUser(final String userId,
                           final Runnable postFinishRunnable) {
        String jsonBody = String.format("{ \"userId\": \"%s\" }",
                userId);

        try {
            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"), jsonBody);

            Request.Builder httpPost = new Request.Builder()
                    .url(BASE_URL + LIST_WASHES_FOR_USER)
                    .post(body);
            HttpCallHelper.executeHttpCall(httpPost, postFinishRunnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
