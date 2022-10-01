package com.example.admin.context;

import com.example.admin.model.User;
import com.example.admin.model.Wash;

import java.util.List;

import okhttp3.OkHttpClient;

/**
 * Class used to store Global Context to be shared across Activities.
 */
public class GlobalContext
{
    public static User currentOnlineUser;
    public static List<Wash> washes;
    // Hashable map of productId to Product
//    public static Map<String, Product> products;

    public static OkHttpClient httpClient;

    public static final String userKey = "user";
    public static String BIJAY_1 = "";
}
