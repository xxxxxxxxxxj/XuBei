package com.xubei.shop.data.network.service;

import com.xubei.shop.ui.module.base.BaseData;
import com.xubei.shop.ui.module.base.BaseDataResponse;
import com.xubei.shop.ui.module.user.model.MessageConfigBean;
import com.xubei.shop.ui.module.user.model.RedemptionCenterBean;
import com.xubei.shop.ui.module.user.model.RedemptionRecordBean;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * 活动相关接口
 * date：2018/3/26 11:29
 * author：Seraph
 **/
public interface ApiCommonService {

    String BASE_URL = "http://common-server.xubei.com/";

    String TEST_URL = "http://common-server.xubei-test.com/";

    String PRD_URL = "http://common-server.xubei-prd.com/";

    //通过手机号获取用户的兑换券
    @GET("diversion/getByMobile")
    Flowable<BaseDataResponse<List<RedemptionCenterBean>>> getByMobile(@Header("Authorization") String authorization, @Query("mobile") String mobile);

    //兑换
    @GET("diversion/exchange")
    Flowable<BaseDataResponse<BaseData>> exchange(@Header("Authorization") String authorization, @Query("mobile") String mobile, @Query("id") String id, @Query("gameId") String gameId);

    //兑换记录列表
    @GET("diversion/getExchangeByMobile")
    Flowable<BaseDataResponse<List<RedemptionRecordBean>>> getExchangeByMobile(@Header("Authorization") String authorization);

    //获取是否有可以兑换的券
    @GET("anon/diversion/receive/getByMobile")
    Flowable<BaseDataResponse<String>> getByMobileReceive(@Query("mobile") String mobile);


    //保存用户得消息配置
    @GET("anon/message/push/config/save")
    Flowable<BaseDataResponse<String>> saveMessageConfig(@Header("Authorization") String authorization, @Query("type") String type, @Query("status") String status);

    //获取用户消息状态配置
    @GET("anon/message/push/config/get")
    Flowable<BaseDataResponse<List<MessageConfigBean>>> getMessageConfig(@Header("Authorization") String authorization);


    //获取图片验证码链接地址 业务类型1.登陆2.注册3.忘记密码4.忘记支付密码5.活动
    @GET("anon/captcha/anon/captcha/getUrl")
    Flowable<BaseDataResponse<String>> getUrl(@Query("clientType") String clientType,@Query("businessId") Integer businessId);

    //发送（注册/找回密码）验证码(渠道固定使用xubei_android)           业务类型1.登陆2.注册3忘记密码4忘记支付密码5活动
    @GET("anon/sms/sendCode")
    Flowable<BaseDataResponse<String>> sendCode(@Query("mobile") String mobile, @Query("ticket") String ticket, @Query("businessNo") String businessNo,@Query("businessId") Integer businessId);

}
