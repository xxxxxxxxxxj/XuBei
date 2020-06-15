package com.haohao.xubei.data.network;

import android.app.Application;
import android.util.Log;

import com.alibaba.sdk.android.httpdns.HttpDns;
import com.alibaba.sdk.android.httpdns.HttpDnsService;
import com.haohao.xubei.AppConfig;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Dns;

/**
 * 使用阿里云httpdns解析
 * date：2018/11/5 14:51
 * author：xiongj
 **/
public class OkHttpDns implements Dns {
    private static final Dns SYSTEM = Dns.SYSTEM;
    private HttpDnsService httpdns;//httpdns 解析服务
    private static OkHttpDns instance = null;

    private OkHttpDns(Application context) {
        this.httpdns = HttpDns.getService(context, AppConfig.ACCOUNT_ID);
    }

    public static OkHttpDns getInstance(Application context) {
        if (instance == null) {
            instance = new OkHttpDns(context);
        }
        return instance;
    }

    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        //通过异步解析接口获取ip
        String ip = httpdns.getIpByHostAsync(hostname);
        if (ip != null) {
            //如果ip不为null，直接使用该ip进行网络请求
            List<InetAddress> inetAddresses = Arrays.asList(InetAddress.getAllByName(ip));
            if (AppConfig.DEBUG) {
                Log.e("OkHttpDns", "inetAddresses:" + inetAddresses);
            }
            return inetAddresses;
        }
        //如果返回null，走系统DNS服务解析域名
        return SYSTEM.lookup(hostname);
    }
}
