package com.xubei.shop.ui.module.welcome;

import android.content.Intent;
import android.net.Uri;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.xubei.shop.AppConstants;
import com.xubei.shop.data.db.help.UserBeanHelp;
import com.xubei.shop.data.network.rx.RxSchedulers;
import com.xubei.shop.data.network.service.Api8Service;
import com.xubei.shop.data.network.service.ApiPassportService;
import com.xubei.shop.ui.module.base.ABaseSubscriber;
import com.xubei.shop.ui.module.base.BaseData;
import com.xubei.shop.ui.module.base.BaseDataCms;
import com.xubei.shop.ui.module.main.MainActivity;
import com.xubei.shop.ui.module.welcome.model.ADBean;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.lifecycle.Lifecycle;
import io.reactivex.Flowable;

/**
 * 欢迎页逻辑
 * date：2017/5/3 10:12
 * author：Seraph
 **/
class WelcomeActivityPresenter extends WelcomeActivityContract.Presenter {

    private ApiPassportService apiPassportService;

    private Api8Service api8Service;

    private UserBeanHelp userBeanHelp;

    private SPUtils spUtils;

    @Inject
    WelcomeActivityPresenter(Api8Service api8Service, ApiPassportService apiPassportService, UserBeanHelp userBeanHelp) {
        this.apiPassportService = apiPassportService;
        this.userBeanHelp = userBeanHelp;
        this.api8Service = api8Service;
    }

    @Override
    public void start() {
        spUtils = SPUtils.getInstance(AppConstants.SPAction.SP_NAME);
        //更新广告信息
        updateADInfo();
        //调用统计
        openApp();
        //开始倒计时显示广告或者跳转
        CountDown();
    }

    //是否第一次打开app
    public boolean isFirstOpenApp() {
        return spUtils.getBoolean(AppConstants.SPAction.IS_FIRST, true);
    }

    /**
     * 固定2秒倒计时app默认欢迎页
     */
    private void CountDown() {
        Flowable.intervalRange(0, 1, 2, 1, TimeUnit.SECONDS)
                .compose(RxSchedulers.io_main())
                .as(mView.bindLifecycle(Lifecycle.Event.ON_STOP))
                .subscribe(aLong -> {
                    if (mView != null) {
                        //加载缓存的广告信息
                        loadingADInfo();
                    }
                });
    }


    //广告3秒倒计时
    private void CountDownAD() {
        Flowable.intervalRange(0, 5, 0, 1, TimeUnit.SECONDS)
                .compose(RxSchedulers.io_main())
                .as(mView.bindLifecycle(Lifecycle.Event.ON_STOP))
                .subscribe(aLong -> {
                    if (mView != null) {
                        long tempCount = 3 - aLong;
                        if (tempCount < 0) {
                            mView.jumpNextActivity();
                        } else {
                            mView.setADShowCount(tempCount);
                        }

                    }
                });
    }


    private ADBean adBean;

    //加载广告图片
    private void loadingADInfo() {
        //如果没有广告信息则直接跳转
        if (StringUtils.isEmpty(spUtils.getString(AppConstants.SPAction.AD_TYPE)) || isFirstOpenApp()) {
            mView.jumpNextActivity();
        } else {
            //加载广告
            adBean = new ADBean();
            adBean.dataId = spUtils.getString(AppConstants.SPAction.AD_DATA_ID);
            adBean.ad_image = spUtils.getString(AppConstants.SPAction.AD_IMAGE_URL);
            adBean.ad_type = spUtils.getString(AppConstants.SPAction.AD_TYPE);
            adBean.ad_url = spUtils.getString(AppConstants.SPAction.AD_GOTO_URL);
            adBean.game_id = spUtils.getString(AppConstants.SPAction.AD_GAME_ID);
            adBean.game_type = spUtils.getString(AppConstants.SPAction.AD_GAME_TYPE);
            adBean.setGameName(spUtils.getString(AppConstants.SPAction.AD_GAME_NAME));
            mView.setADImageUrl(adBean.ad_image);
            //开始倒计时
            CountDownAD();
        }
    }

    //保存广告信息
    private void saveAdInfo(ADBean adBean) {
        spUtils.put(AppConstants.SPAction.AD_DATA_ID, StringUtils.isEmpty(adBean.dataId) ? "" : adBean.dataId);
        spUtils.put(AppConstants.SPAction.AD_IMAGE_URL, StringUtils.isEmpty(adBean.ad_image) ? "" : adBean.ad_image);
        spUtils.put(AppConstants.SPAction.AD_TYPE, StringUtils.isEmpty(adBean.ad_type) ? "" : adBean.ad_type);
        spUtils.put(AppConstants.SPAction.AD_GOTO_URL, StringUtils.isEmpty(adBean.ad_url) ? "" : adBean.ad_url);
        spUtils.put(AppConstants.SPAction.AD_GAME_ID, StringUtils.isEmpty(adBean.game_id) ? "" : adBean.game_id);
        spUtils.put(AppConstants.SPAction.AD_GAME_TYPE, StringUtils.isEmpty(adBean.game_type) ? "" : adBean.game_type);
        spUtils.put(AppConstants.SPAction.AD_GAME_NAME, StringUtils.isEmpty(adBean.getGameName()) ? "" : adBean.getGameName());
    }


    //跳转广告
    void doJumpAD() {
        if (adBean != null) {
            //调用广告点击统计
            updateClickCount(adBean.dataId);
            Intent intentStartAd = null;
            switch (adBean.ad_type) {
                case "1"://游戏列表
                    ARouter.getInstance().build(AppConstants.PagePath.ACC_LIST).withSerializable("bean", adBean).navigation();
                    break;
                case "2"://超链接
                    intentStartAd = new Intent(Intent.ACTION_VIEW, Uri.parse(adBean.ad_url));
                    break;
            }
            if (intentStartAd != null) {
                mView.getContext().startActivities(new Intent[]{new Intent(mView.getContext(), MainActivity.class), intentStartAd});
            }
        }
    }

    //广告点击统计
    private void updateClickCount(String dataId) {
        api8Service.updateClickCount(dataId)
                .compose(RxSchedulers.io_main_business())
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>() {
                    @Override
                    protected void onSuccess(String s) {

                    }

                    @Override
                    protected void onError(String errStr) {

                    }
                });
    }


    //获取广告
    private void updateADInfo() {
        api8Service.getWelcomeAd()
                .compose(RxSchedulers.io_main_business())
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<BaseData<BaseDataCms<ADBean>>>() {
                    @Override
                    protected void onSuccess(BaseData<BaseDataCms<ADBean>> baseData) {
                        if (baseData.datas.size() > 0) {
                            //获取到广告图片地址存起来
                            ADBean adBean = baseData.datas.get(0).properties;
                            adBean.dataId = baseData.datas.get(0).dataId;
                            saveAdInfo(adBean);
                        } else {
                            saveAdInfo(new ADBean());
                        }

                    }

                    @Override
                    protected void onError(String errStr) {

                    }
                });

    }

    //在用户登录的情况下调用统计
    private void openApp() {
        if (userBeanHelp.isLogin()) {
            apiPassportService.openAppOpr(userBeanHelp.getAuthorization())
                    .compose(RxSchedulers.io_main_business())
                    .as(mView.bindLifecycle())
                    .subscribe(new ABaseSubscriber<String>() {
                        @Override
                        public void onSuccess(String s) {

                        }

                        @Override
                        public void onError(String errStr) {

                        }
                    });
        }
    }


}
