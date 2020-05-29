package com.xubei.shop.data.network.service;

import com.xubei.shop.ui.module.account.model.AccSCBean;
import com.xubei.shop.ui.module.account.model.GameBean;
import com.xubei.shop.ui.module.account.model.GoodsStickBean;
import com.xubei.shop.ui.module.account.model.MianFeiBean;
import com.xubei.shop.ui.module.account.model.OutGoodsBean;
import com.xubei.shop.ui.module.account.model.OutGoodsDetailBean;
import com.xubei.shop.ui.module.account.model.OutGoodsDetailValuesBean;
import com.xubei.shop.ui.module.account.model.UserAccFreeBean;
import com.xubei.shop.ui.module.base.BaseData;
import com.xubei.shop.ui.module.base.BaseDataResponse;
import com.xubei.shop.ui.module.login.model.UserBean;
import com.xubei.shop.ui.module.order.model.OrderPayBean;
import com.xubei.shop.ui.module.order.model.OutOrderBean;
import com.xubei.shop.ui.module.order.model.SelectOrderPayBean;
import com.xubei.shop.ui.module.rights.model.LeaseeArbBean;
import com.xubei.shop.ui.module.rights.model.RightReasonBean;
import com.xubei.shop.ui.module.user.model.AcctManageBean;
import com.xubei.shop.ui.module.user.model.FreezeDetailBean;
import com.xubei.shop.ui.module.user.model.FundDetailBean;
import com.xubei.shop.ui.module.user.model.RechargeBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * 新的用户中心的重构完成的接口
 * date：2017/2/16 11:57
 * author：Seraph
 **/
public interface ApiUserNewService {

    String BASE_URL = "https://user-server.xubei.com/";

    String TEST_URL = "http://user-server.xubei-test.com/";

    String PRD_URL = "http://user-server.xubei-prd.com/";

    //用户基本信息
    @GET("userInfo/index")
    Flowable<BaseDataResponse<UserBean>> userInfoIndex(@Header("Authorization") String authorization);

    //更新头像
    @GET("userInfo/updateHeadPortrait")
    Flowable<BaseDataResponse<String>> updateHeadPortrait(@Header("Authorization") String authorization, @Query("imgUrl") String imgUrl);

    //保存用户信息
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("userInfo/saveUserInfoNew")
    Flowable<BaseDataResponse<String>> saveUserInfoNew(@Header("Authorization") String cokie, @Body RequestBody requestBody);

    //账号管理
    @GET("acctManage/index")
    Flowable<BaseDataResponse<AcctManageBean>> acctManageIndex(@Header("Authorization") String authorization);

    //发布商品
    //json类型数据
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("saller/publicGoods")
    Flowable<BaseDataResponse<BaseData<Long>>> publicGoods(@Header("Authorization") String authorization, @Body RequestBody requestBody);

    //编辑商品
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("saller/editGoods")
    Flowable<BaseDataResponse<BaseData<Long>>> editGoods(@Header("Authorization") String authorization, @Body RequestBody requestBody);

    //app获取商品回填数据
    @GET("saller/appEditGoodsValue")
    Flowable<BaseDataResponse<OutGoodsDetailValuesBean>> appEditGoodsValue(@Header("Authorization") String authorization, @Query("goodsId") String goodsId, @Query("businessNo") String businessNo);

    //我的出租订单
    @GET("saller/myRentList")
    Flowable<BaseDataResponse<BaseData<OutOrderBean>>> myRentList(@Header("Authorization") String authorization, @QueryMap Map<String, Object> map);

    //出租/出售账号管理
    @GET("saller/getGoodsManage")
    Flowable<BaseDataResponse<BaseData<OutGoodsBean>>> getGoodsManage(@Header("Authorization") String authorization, @QueryMap Map<String, Object> map);

    //修改商品密码
    @GET("saller/changeGoodsPwd")
    Flowable<BaseDataResponse<String>> changeGoodsPwd(@Header("Authorization") String authorization, @Query("goodsId") String goodsId, @Query("password") String password);

    //删除商品
    @GET("saller/deleteGoods")
    Flowable<BaseDataResponse<String>> deleteGoods(@Header("Authorization") String authorization, @Query("goodsIds") String goodsIds);

    //商品上下架
    @GET("saller/upOrDownGoods")
    Flowable<BaseDataResponse<String>> upOrDownGoods(@Header("Authorization") String authorization, @Query("goodsIds") String goodsIds, @Query("type") String type);

    //商品置顶
    @GET("saller/goodsStick")
    Flowable<BaseDataResponse<GoodsStickBean>> goodsStick(@Header("Authorization") String authorization, @Query("goodsIds") String goodsIds, @Query("isPre") Boolean isPre);

    //修改商品价格
    @GET("saller/updateGoodsPrice")
    Flowable<BaseDataResponse<String>> updateGoodsPrice(@Header("Authorization") String authorization, @Query("goodsId") String goodsId, @Query("leasePrice") String leasePrice);

    //维权原因
    @GET("arbitration/orderLeaseRightReason")
    Flowable<BaseDataResponse<List<RightReasonBean>>> orderLeaseRightReason(@Header("Authorization") String authorization, @Query("orderNo") String orderNo);

    //提交维权
    @GET("arbitration/leaseeSubArb")
    Flowable<BaseDataResponse<String>> leaseeSubArb(@Header("Authorization") String authorization, @QueryMap Map<String, Object> map);

    //查看维权记录
    @GET("arbitration/toLeaseeArbList")
    Flowable<BaseDataResponse<LeaseeArbBean>> toLeaseeArbList(@Header("Authorization") String authorization, @Query("orderId") String orderId);

    //买家/卖家添加维权详情
    @GET("arbitration/addLeaseRightDetail")
    Flowable<BaseDataResponse<String>> addLeaseRightDetail(@Header("Authorization") String authorization, @Query("leaseRightNo") String leaseRightNo, @Query("isAggre") Integer isAggre, @Query("urls") String urls);

    //买家/卖家处理维权
    @GET("arbitration/handleLeaseRight")
    Flowable<BaseDataResponse<String>> handleLeaseRight(@Header("Authorization") String authorization, @Query("orderNo") String orderNo, @Query("leaseRightNo") String leaseRightNo, @Query("isAggre") Integer isAggre);

    //买家撤销维权
    @GET("arbitration/cancelOrderLeaseRight")
    Flowable<BaseDataResponse<Boolean>> cancelOrderLeaseRight(@Header("Authorization") String authorization, @Query("orderNo") String orderNo);

    //添加收藏
    @GET("buyer/addCollection")
    Flowable<BaseDataResponse<String>> addCollection(@Header("Authorization") String authorization, @Query("goodsId") String goodsId);

    //获取收藏列表
    @GET("buyer/getCollectionList")
    Flowable<BaseDataResponse<BaseData<AccSCBean>>> getCollectionList(@Header("Authorization") String authorization, @Query("pageIndex") Integer pageIndex, @Query("pageSize") Integer pageSize);

    //查询商品是否有收藏
    @GET("buyer/getCollection")
    Flowable<BaseDataResponse<String>> getCollection(@Header("Authorization") String authorization, @Query("goodsId") String goodsId);

    //我租赁的账号
    @GET("buyer/myLeaseList")
    Flowable<BaseDataResponse<BaseData<OutOrderBean>>> myLeaseList(@Header("Authorization") String authorization, @QueryMap() Map<String, Object> map);

    //上单号 获取 最近的 支付流水号 NOTE 包含绑定的各类型的订单
    @GET("buyer/getOrderBalanceNo")
    Flowable<BaseDataResponse<String>> getOrderBalanceNo(@Header("Authorization") String authorization, @Query("gameNo") String gameNo);

    //创建订单0-按小时租，1-按天租，2-按周租，3-通宵畅玩
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("buyer/createOrder")
    Flowable<BaseDataResponse<OrderPayBean>> createOrder(@Header("Authorization") String authorization, @Body RequestBody jsonBody);

    //续租
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("buyer/reletPay")
    Flowable<BaseDataResponse<OrderPayBean>> reletPay(@Header("Authorization") String authorization, @Body RequestBody jsonBody);

    //商品详情
    @GET("usercenter/getGoodsDetail")
    Flowable<BaseDataResponse<OutGoodsDetailBean>> getGoodsDetail(@Header("Authorization") String authorization, @Query("goodsId") String goodsId);

    //订单详情
    @GET("usercenter/getOrderDetail")
    Flowable<BaseDataResponse<OutOrderBean>> getOrderDetail(@Header("Authorization") String authorization, @Query("orderNo") String orderNo);


    //找回支付密码
    @GET("acctSafe/forgetPayPwd")
    Flowable<BaseDataResponse<String>> forgetPayPwd(@Header("Authorization") String authorization, @Query("mobile") String mobile, @Query("payPwd") String payPwd, @Query("code") String code);

    //身份认证
    @GET("acctSafe/identityAuth")
    Flowable<BaseDataResponse<String>> identityAuth(@Header("Authorization") String authorization, @Query("realName") String realName, @Query("idNo") String idNo);

    //修改手机号
    @GET("acctSafe/updatePhone")
    Flowable<BaseDataResponse<String>> updatePhone(@Header("Authorization") String authorization, @Query("mobile") String mobile, @Query("verificationCode") String verificationCode);

    //微信/支付宝支付结算支付类型 余额支付:yue,支付宝app支付:alipay_app,微信App支付:wechat_app  金币支付:pay_coin
    @GET("pay/payLaunchExt")
    Flowable<BaseDataResponse<String>> payLaunchExt(@Header("Authorization") String authorization, @Query("balanceNo") String balanceNo, @Query("payMode") String payMode, @Query("orderType") String orderType, @Query("payPwd") String payPwd);

    //查询充值订单详情
    @GET("pay/getTopupDetail")
    Flowable<BaseDataResponse<SelectOrderPayBean>> getTopupDetail(@Header("Authorization") String authorization, @Query("rechargeNo") String rechargeNo);

    //租赁/续租查询结算单
    @GET("pay/getOrderBalance")
    Flowable<BaseDataResponse<SelectOrderPayBean>> getOrderBalance(@Header("Authorization") String authorization, @Query("balanceNo") String balanceNo);

    //充值
    @GET("fund/recharge")
    Flowable<BaseDataResponse<RechargeBean>> recharge(@Header("Authorization") String authorization, @Query("rechargeAmt") String rechargeAmt, @Query("rechargeType") String rechargeType);

    //资金明细 cashflow 资金流向 0是进，1是出
    @GET("fund/fundDetail")
    Flowable<BaseDataResponse<BaseData<FundDetailBean>>> fundDetail(@Header("Authorization") String authorization, @Query("cashflow") Integer cashflow, @Query("pageIndex") Integer pageIndex, @Query("pageSize") Integer pageSize);

    //冻结明细
    @GET("fund/getFreezeDetail")
    Flowable<BaseDataResponse<BaseData<FreezeDetailBean>>> getFreezeDetail(@Header("Authorization") String authorization, @Query("pageIndex") Integer pageIndex, @Query("pageSize") Integer pageSize);


    //用户拥有的商品的游戏列表
    @GET("saller/getGoodsManageOfGame")
    Flowable<BaseDataResponse<List<GameBean>>> getGoodsManageOfGame(@Header("Authorization") String authorization, @Query("stickStatus") String stickStatus);


    //查询商品助力活动接口 (免费玩)
    @GET("activity/addInitiator")
    Flowable<BaseDataResponse<MianFeiBean>> addInitiator(@Header("Authorization") String authorization, @Query("activNo") String activNo, @Query("bigGameId") String bigGameId, @Query("goodsId") String goodsId);

    //用户已经分享过的商品列表 (免费玩)
    @GET("activity/getUserGoodslist")
    Flowable<BaseDataResponse<BaseData<UserAccFreeBean>>> getUserGoodslist(@Header("Authorization") String authorization, @Query("activNo") String activNo, @Query("pageIndex") Integer pageIndex, @Query("pageSize") Integer pageSize);

}
