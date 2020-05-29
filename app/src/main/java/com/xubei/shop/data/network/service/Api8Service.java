package com.xubei.shop.data.network.service;

import com.xubei.shop.ui.module.account.model.AccBean;
import com.xubei.shop.ui.module.account.model.AccRSResultBean;
import com.xubei.shop.ui.module.account.model.GameActivityBean;
import com.xubei.shop.ui.module.account.model.GameAllAreaBean;
import com.xubei.shop.ui.module.account.model.GameAreaBean;
import com.xubei.shop.ui.module.account.model.GameConfigBean;
import com.xubei.shop.ui.module.account.model.GameSearchRelationBean;
import com.xubei.shop.ui.module.account.model.GoldBlBean;
import com.xubei.shop.ui.module.account.model.OutGoodsDetailBean;
import com.xubei.shop.ui.module.account.model.ShareMianFeiBean;
import com.xubei.shop.ui.module.base.BaseData;
import com.xubei.shop.ui.module.base.BaseDataCms;
import com.xubei.shop.ui.module.base.BaseDataResponse;
import com.xubei.shop.ui.module.main.model.BannerBean;
import com.xubei.shop.ui.module.main.model.NoticeBean;
import com.xubei.shop.ui.module.main.model.WelfareBean;
import com.xubei.shop.ui.module.setting.model.ProblemBean;
import com.xubei.shop.ui.module.setting.model.QuestionBean;
import com.xubei.shop.ui.module.welcome.model.ADBean;

import java.util.HashMap;
import java.util.List;

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
 * 帮助中心
 * date：2017/2/16 11:57
 * author：Seraph
 **/
public interface Api8Service {

    String BASE_URL = "https://api8.xubei.com/";

    String TEST_URL = "http://api8.xubei-test.com/";

    String PRD_URL = "http://api8.xubei-prd.com/";

    //欢迎页广告
    @GET("xubei-page-cloud/anon/cms/getModDataByModId?modId=welcome_ad")
    Flowable<BaseDataResponse<BaseData<BaseDataCms<ADBean>>>> getWelcomeAd();

    //广告页点击
    @GET("xubei-page-cloud/anon/cms/pageModuleData/updateClickCount")
    Flowable<BaseDataResponse<String>> updateClickCount(@Query("dataId") String dataId);

    //首页banner
    @GET("xubei-page-cloud/anon/cms/getModDataByModId?modId=home_banner")
    Flowable<BaseDataResponse<BaseData<BaseDataCms<BannerBean>>>> getHomeBanner();

    //热销榜单
    @GET("xubei-page-cloud/anon/cms/getModDataByModId?modId=hot_list")
    Flowable<BaseDataResponse<BaseData<BaseDataCms<AccBean>>>> getHotList();

    //福利中心
    @GET("xubei-page-cloud/anon/cms/getModDataByModId?modId=welfare_center")
    Flowable<BaseDataResponse<BaseData<BaseDataCms<WelfareBean>>>> getWelfareCenter();

    //免费试玩助力
    @GET("xubei-page-cloud/anon/cms/getModDataByModId?modId=xubei_share_mianfei_wenan")
    Flowable<BaseDataResponse<BaseData<BaseDataCms<ShareMianFeiBean>>>> getShareMianfeiWenan();

    //金币比例
    @GET("xubei-page-cloud/anon/cms/getModDataByModId?modId=gongyong_jinbipeizhi")
    Flowable<BaseDataResponse<BaseData<BaseDataCms<GoldBlBean>>>> getJinbipeizhi();

    //帮助中心列表
    @GET("xubei-common-cloud/anon/helpCenter/categoryList")
    Flowable<BaseDataResponse<List<ProblemBean>>> categoryList();

    //帮助类型列表
    @GET("xubei-common-cloud/anon/helpCenter/getArticleByMenuId")
    Flowable<BaseDataResponse<List<QuestionBean>>> getArticleByMenuId(@Query("menuId") String menuId);

    //帮助文章详情
    @GET("xubei-common-cloud/anon/helpCenter/getArticleDetail")
    Flowable<BaseDataResponse<List<WelfareBean>>> getArticleDetail(@Query("articleId") String articleId);

    //获取推送消息1:所有装机用户,2虚贝用户,3:指定用户
    @GET("xubei-common-web/anon/message/push/getUserMessage")
    Flowable<BaseDataResponse<BaseData<NoticeBean>>> getUserMessage(@Header("Authorization") String authorization, @Query("type") String type, @Query("pageNo") Integer pageNo, @Query("pageSize") Integer pageSize);

    //更新用户消息已读状态
    @GET("xubei-common-web/anon/message/push/updateStatus")
    Flowable<BaseDataResponse<String>> updateStatus(@Header("Authorization") String authorization, @Query("messageId") Long messageId);

    //查询是否有未读消息
    @GET("xubei-common-web/anon/message/push/hasUnreadMessage")
    Flowable<BaseDataResponse<Boolean>> hasUnreadMessage(@Header("Authorization") String authorization);

    //获取游戏配置
    @GET("xubei-goods-cloud/anonapi/server/game/getPublishShowField")
    Flowable<BaseDataResponse<List<GameConfigBean>>> findGoodsConfig(@Query("serverId") String serverId, @Query("bigGameId") String bigGameId);

    //通过字段ID返回数据(select,radio,checkbox)
    @GET("xubei-goods-cloud/anonapi/server/gamewordscol/option/data")
    Flowable<BaseDataResponse<String>> optionData(@Query("fieldId") String serverId);

    //获取商品详情
    @GET("xubei-goods-cloud/anonapi/goodsDetail")
    Flowable<BaseDataResponse<OutGoodsDetailBean>> goodsDetail(@Query("goodsId") String goodsId, @Query("businessNo") String businessNo);

    //获取区服数据(所有)
    @GET("xubei-goods-cloud/anonapi/findGameAreaById")
    Flowable<BaseDataResponse<GameAllAreaBean>> findGameAreaById(@Query("id") String id);

    //获取区服数据(根据父节点查询子节点)
    @GET("xubei-goods-cloud/anonapi/findChdByGameParent")
    Flowable<BaseDataResponse<List<GameAreaBean>>> findChdByGameParent(@Query("parentId") Long parentId);

    //获取游戏活动
    @GET("xubei-goods-cloud/anonapi/findGoodsActity")
    Flowable<BaseDataResponse<List<GameActivityBean>>> findGoodsActity(@Query("gameId") String gameId);

    //获取账号列表
    @GET("b/goods/findGoodsList")
    Flowable<BaseDataResponse<BaseData<AccBean>>> findGoodsList(@QueryMap HashMap<String, Object> map);

    //搜索游戏类目
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("xubei-goods-cloud/anonapi/indexGameList")
    Flowable<BaseDataResponse<List<AccRSResultBean>>> indexGameList(@Body RequestBody requestBody);


    //游戏高级筛选
    @GET("xubei-goods-cloud/anonapi/getGameSearchRelation")
    Flowable<BaseDataResponse<List<GameSearchRelationBean>>> getGameSearchRelation(@Query("gameId") String gameId);


}
