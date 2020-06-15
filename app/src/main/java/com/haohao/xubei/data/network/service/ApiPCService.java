package com.haohao.xubei.data.network.service;

import com.haohao.xubei.ui.module.base.BaseDataResponse;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * pc对应的接口联动
 * date：2018/3/26 11:29
 * author：Seraph
 *
 **/
public interface ApiPCService {

    String BASE_URL = "http://pc-client.xubei.com/";

    String TEST_URL = "http://pc-client.test.xubei.com/";

    //pc上号器登录接口
    @POST("mobileLogin")
    @FormUrlEncoded
    Flowable<BaseDataResponse<String>> mobileLogin(@Field("auth_str") String auth_str, @Field("order_no") String order_no, @Field("end_time") String end_time);


}
