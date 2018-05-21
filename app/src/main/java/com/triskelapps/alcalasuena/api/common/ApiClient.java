package com.triskelapps.alcalasuena.api.common;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.triskelapps.alcalasuena.DebugHelper;
import com.triskelapps.alcalasuena.util.DateUtils;

import java.lang.reflect.Modifier;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    private static final String TAG = "ApiClient";

    // Tutorial Retrofit 2.0
    // http://inthecheesefactory.com/blog/retrofit-2.0/en

    public static final String BASE_URL_PRODUCTION = "https://alcalasuena.es";
    public static final String BASE_URL_DEBUG = "https://alcalasuena.es";

    public static final String BASE_URL =
            (DebugHelper.SWITCH_PROD_ENVIRONMENT ? BASE_URL_PRODUCTION : BASE_URL_DEBUG);

    public static final String BASE_URL_API = BASE_URL + "/api/v1/";


    private static Retrofit sharedInstance;

//    private static JsonDeserializer<Date> jsonDateDeserializer = new JsonDeserializer<Date>() {
//        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//
//            try {
//                return News.datetimeNewsFormatApiGet.parse(((JsonObject) json).get("initDate").getAsString());
//            } catch (ParseException e) {
//                throw new JsonParseException(e);
//            }
//
//        }
//    };

    public static Retrofit getInstance() {
        if (sharedInstance == null) {

            Gson gson = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.VOLATILE, Modifier.TRANSIENT, Modifier.STATIC)
//                    .registerTypeAdapter(Area.class, null)
                    .setPrettyPrinting()
//                    .registerTypeAdapter(Date.class, jsonDateDeserializer)
                    .setDateFormat(DateUtils.formatDateApi.toPattern())
                    .create();


            sharedInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL_API)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(getOkHttpClient())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

        }

        return sharedInstance;
    }


    private static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        client.addInterceptor(loggingInterceptor);

        return client.build();
    }

}
