package com.haohao.xubei.data.network;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.utlis.FileUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 构建请求api
 * 缓存文件夹使用应用专属缓存目录,参加{@link FileUtils#getCacheDirectory(Context, String)}方法
 * date：2017/4/6 20:01
 * author：Seraph
 **/
public class ApiBuild {

    private Application application;

    @Inject
    ApiBuild(Application application) {
        this.application = application;
    }

    private OkHttpClient.Builder builder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        try {
            //添加统一的请求头信息
            builder.addInterceptor(chain -> {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("X-Request-Source", DeviceUtils.getManufacturer() + " " + DeviceUtils.getModel() + " Android " + DeviceUtils.getSDKVersionName())
                        .addHeader("X-System-Version", "App v" + AppUtils.getAppVersionName())
                        .addHeader("X-Network-Type", NetworkUtils.getNetworkType().name())
                        .build();
                return chain.proceed(newRequest);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        //log输出
        if (AppConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
        //使用阿里dns服务器
        builder.dns(OkHttpDns.getInstance(application));
        builder.connectTimeout(AppConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        return builder;
    }


    /**
     * 构建ApiInterface
     */
    public <T> T buildApiInterface(String apiBaseUrl, Class<T> service) {
        OkHttpClient.Builder builder = builder();
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(apiBaseUrl)
                .client(builder.build()).build()
                .create(service);
    }


}
