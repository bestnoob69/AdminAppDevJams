package com.example.admin.utils;

/**
 * Custom Runnable Class with feature to allow for extraction of statusCode and jsonResponse
 * outside of the Runnable entity by the caller itself.
 */
public abstract class MyRunnable implements Runnable {
    protected int statusCode = 0;
    protected String jsonResponse = null;

    public void preRun(final int statusCode,
                       final String jsonResponse) {
        this.statusCode = statusCode;
        this.jsonResponse = jsonResponse;
    }

    @Override
    public abstract void run();
}
