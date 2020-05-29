package com.xubei.shop.ui.module.login.presenter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.xubei.shop.AppConfig;
import com.xubei.shop.AppConstants;
import com.xubei.shop.data.network.rx.RxSchedulers;
import com.xubei.shop.data.network.service.ApiCommonService;
import com.xubei.shop.data.network.service.ApiPassportService;
import com.xubei.shop.di.QualifierType;
import com.xubei.shop.ui.module.base.ABaseSubscriber;
import com.xubei.shop.ui.module.login.contract.PhoneBindContract;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * 手机绑定
 * date：2017/10/25 10:43
 * author：Seraph
 **/
public class PhoneBindPresenter extends PhoneBindContract.Presenter {


    private ApiPassportService apiPassportService;

    private ApiCommonService apiCommonService;

    private String openId;

    private String unionId;

    private String source;

    @Inject
    PhoneBindPresenter(ApiPassportService apiPassportService, ApiCommonService apiCommonService, @QualifierType("openId") String openId, @QualifierType("unionId") String unionId, @QualifierType("source") String source) {
        this.apiPassportService = apiPassportService;
        this.apiCommonService = apiCommonService;
        this.openId = openId;
        this.unionId = unionId;
        this.source = source;
    }

    @Override
    public void start() {
        ToastUtils.showShort("请绑定您的手机号");
    }


    //开始倒计时
    private void startCountdown(final int count) {
        Flowable.intervalRange(1, count, 0, 1, TimeUnit.SECONDS)
                .compose(RxSchedulers.io_main())
                .as(mView.bindLifecycle())
                .subscribe(aLong -> mView.setCountdownText(count - aLong));
    }

    public void doLookAgreement() {
        //《虚贝分享平台服务协议》
        ARouter.getInstance().build(AppConstants.PagePath.COMM_AGENTWEB)
                .withString("title", "服务与隐私条款")
                .withString("webUrl", AppConstants.AgreementAction.AGREEMENT)
                .navigation();
    }

    //获取url连接验证
    public void onVerifyImageCode() {
        apiCommonService.getUrl(AppConfig.CLIENT_TYPE, 2)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>() {
                    @Override
                    public void onSuccess(String jsurl) {
                        mView.hideLoading();
                        mView.gotoVerifyFullScreenActivity(jsurl);
                    }

                    @Override
                    public void onError(String errStr) {
                        if ("601".equals(errStr)){
                            mView.doGetCode();
                        }else {
                            mView.hideLoading();
                            ToastUtils.showShort(errStr);
                        }
                    }
                });
    }

    //获取验证码
    public void onGetCode(String phone, String ticket) {
        apiCommonService.sendCode(phone, ticket, AppConfig.getChannelValue(), 2)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading("获取验证码").setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>(mView) {

                    @Override
                    public void onSuccess(String str) {
                        //获取验证码成功，开始倒计时
                        startCountdown(60);
                        if (AppConfig.BASE_HTTP_IP == 3) {
                            mView.doShowCodeOK(str);
                        } else {
                            mView.doShowCodeOK();
                        }
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }

    //绑定手机
    public void onBind(String phone, String code) {
        apiPassportService.bind(openId, unionId, phone, code, source, AppConfig.getChannelValue())
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading("绑定中").setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String str) {
                        ToastUtils.showShort("绑定成功");
                        RxBus.get().post(AppConstants.RxBusAction.TAG_OTHER_LOGIN, true);
                        mView.finish();
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }

}

