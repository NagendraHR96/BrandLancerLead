package com.example.brandlancerlead.brandUtility;

import java.util.concurrent.TimeUnit;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerConnection {

    private final  static int Timeout = 180;

    //private static String service_base_url = "http://117.198.98.219:8090/BrandServices.svc/";
    //public static String service_base_url = "http://49.206.229.93:8089/BrandServices.svc/";
    public static String service_base_url = "http://203.192.233.36:8090/BrandServices.svc/";
    //public static String service_base_url = "http://203.192.233.36:8090//BrandServices.svc/";
    // public static String service_base_url = "http://192.168.0.210:8090//BrandServices.svc/";

    public static String fttpUrl = "203.192.233.36";


    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC);

    private static OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder()
            .readTimeout(Timeout, TimeUnit.SECONDS)
            .connectTimeout(Timeout, TimeUnit.SECONDS).addInterceptor(loggingInterceptor);





    public static <S> S createRetrofitConnection(Class<S> serverWeb) {
       Retrofit.Builder server_builder = new Retrofit.Builder()
                .baseUrl(service_base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpBuilder.build());

        Retrofit serverRetro = server_builder.build();

        if (!httpBuilder.interceptors().contains(loggingInterceptor)) {
            httpBuilder.addInterceptor(loggingInterceptor);
            server_builder.client(httpBuilder.build());
            serverRetro = server_builder.build();
        }

        return serverRetro.create(serverWeb);
    }
}
