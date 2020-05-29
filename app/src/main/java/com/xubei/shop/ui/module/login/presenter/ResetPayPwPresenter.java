package com.xubei.shop.ui.module.login.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.xubei.shop.AppConfig;
import com.xubei.shop.data.db.help.UserBeanHelp;
import com.xubei.shop.data.network.rx.RxSchedulers;
import com.xubei.shop.data.network.service.ApiCommonService;
import com.xubei.shop.data.network.service.ApiUserNewService;
import com.xubei.shop.ui.module.base.ABaseSubscriber;
import com.xubei.shop.ui.module.login.contract.ResetPayPwContract;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * 验证手机号逻辑
 * date：2017/10/25 10:43
 * author：Seraph
 **/
public class ResetPayPwPresenter extends ResetPayPwContract.Presenter {

    private ApiUserNewService apiUserNewService;

    private ApiCommonService apiCommonService;

    private UserBeanHelp userBeanHelp;

    @Inject
    ResetPayPwPresenter(ApiCommonService apiCommonService,ApiUserNewService apiUserNewService, UserBeanHelp userBeanHelp) {
        this.apiUserNewService = apiUserNewService;
        this.apiCommonService = apiCommonService;
        this.userBeanHelp = userBeanHelp;
    }

    @Override
    public void start() {
        //设置用户手机号
        mView.setUserInfo(userBeanHelp.getUserBean());
    }


    //获取url连接验证
    public void onVerifyImageCode() {
        apiCommonService.getUrl(AppConfig.CLIENT_TYPE, 4)
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
                        if ("601".equals(errStr)) {
                            onGetCode(null);
                        } else {
                            mView.hideLoading();
                            ToastUtils.showShort(errStr);
                        }
                    }
                });
    }

    //获取验证码
    public void onGetCode(String ticket) {
        apiCommonService.sendCode(userBeanHelp.getUserBean().getMobile(), ticket, AppConfig.getChannelValue(), 4)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading("获取验证码").setOnDismissListener(dialog -> subscription.cancel()))
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

    public void onSetPassword(String code, String password) {
        apiUserNewService.forgetPayPwd(userBeanHelp.getAuthorization(), userBeanHelp.getUserBean().getMobile(), password, code)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading("提交信息").setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String s) {
                        ToastUtils.showShort("支付密码修改成功");
                        //如果用户不为空，则更新Userid
                        mView.finish();
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }

    //开始倒计时
    private void startCountdown(final int count) {
        Flowable.intervalRange(1, count, 0, 1, TimeUnit.SECONDS)
                .compose(RxSchedulers.io_main())
                .as(mView.bindLifecycle())
                .subscribe(aLong -> mView.setCountdownText(count - aLong));
    }
}
