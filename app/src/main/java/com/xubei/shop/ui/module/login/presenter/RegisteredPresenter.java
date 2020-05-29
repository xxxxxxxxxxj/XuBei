package com.xubei.shop.ui.module.login.presenter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.xubei.shop.AppConfig;
import com.xubei.shop.AppConstants;
import com.xubei.shop.data.network.rx.RxSchedulers;
import com.xubei.shop.data.network.service.ApiCommonService;
import com.xubei.shop.data.network.service.ApiPassportService;
import com.xubei.shop.ui.module.base.ABaseSubscriber;
import com.xubei.shop.ui.module.login.contract.RegisteredContract;
import com.xubei.shop.ui.module.login.model.UserBean;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * 注册逻辑
 * date：2017/10/25 10:43
 * author：Seraph
 **/
public class RegisteredPresenter extends RegisteredContract.Presenter {


    private ApiPassportService apiPassportService;

    private ApiCommonService apiCommonService;

    @Inject
    RegisteredPresenter(ApiPassportService apiPassportService, ApiCommonService apiCommonService) {
        this.apiPassportService = apiPassportService;
        this.apiCommonService = apiCommonService;
    }

    @Override
    public void start() {
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
        apiCommonService.getUrl(AppConfig.CLIENT_TYPE,2)
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
        apiCommonService.sendCode(phone, ticket, AppConfig.getChannelValue(),2)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>(mView) {

                    @Override
                    public void onSuccess(String str) {
                        ToastUtils.showShort("发送验证码成功");
                        //获取验证码成功，开始倒计时
                        startCountdown(60);
                        if (AppConfig.BASE_HTTP_IP == 3) {
                            mView.doShowCodeOK(str);
                        }
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }

    public void onRegistered(String phone, String code, String password) {
        apiPassportService.registerUser(phone, password, code, AppConfig.getChannelValue())
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading("注册中").setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<UserBean>(mView) {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        ToastUtils.showShort("注册成功");
                        mView.finish();
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }

}

