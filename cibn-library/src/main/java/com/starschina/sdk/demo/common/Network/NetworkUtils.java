package com.starschina.sdk.demo.common.Network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2016/10/14.
 */
public class NetworkUtils {

    private Retrofit.Builder mRetrofitBuild;

    private static NetworkUtils mInstance;

    private NetworkUtils() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        //httpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

        mRetrofitBuild = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient.build());
    }

    public static NetworkUtils getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkUtils();
        }

        return mInstance;
    }

    public <T> T getService(final Class<T> service, String url, boolean useGson) {
        if (useGson) {
            return mRetrofitBuild.addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(url).build().create(service);
        }else {
            return mRetrofitBuild.addConverterFactory(ScalarsConverterFactory.create())
                    .baseUrl(url).build().create(service);
        }
    }
}
