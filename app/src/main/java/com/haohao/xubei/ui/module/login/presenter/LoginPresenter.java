package com.haohao.xubei.ui.module.login.presenter;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.AppApplication;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.data.db.table.UserTable;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.ApiCommonService;
import com.haohao.xubei.data.network.service.ApiPassportService;
import com.haohao.xubei.data.network.service.ApiUserNewService;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.login.contract.LoginContract;
import com.haohao.xubei.ui.module.login.model.UserBean;
import com.haohao.xubei.ui.module.main.MainActivity;
import com.haohao.xubei.utlis.Tools;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import io.reactivex.Flowable;

/**
 * 登录逻辑
 * date：2017/10/25 10:43
 * author：Seraph
 **/
public class LoginPresenter extends LoginContract.Presenter {

    private ApiUserNewService apiService;

    private UserBeanHelp userBeanHelp;

    private ApiPassportService apiPassportService;

    private ApiCommonService apiCommonService;

    //是否清理用户登录信息
    private boolean isCleanUser;

    private Application application;

    @Inject
    LoginPresenter(Application application, ApiPassportService apiPassportService, ApiUserNewService apiService, ApiCommonService apiCommonService, UserBeanHelp userBeanHelp, Boolean isCleanUser) {
        this.apiService = apiService;
        this.userBeanHelp = userBeanHelp;
        this.apiPassportService = apiPassportService;
        this.apiCommonService = apiCommonService;
        this.isCleanUser = isCleanUser;
        this.application = application;
    }

    //是否是账号密码登录
    private boolean isPwLogin = true;

    @Override
    public void start() {
        //是否清理用户信息
        if (isCleanUser) {
            userBeanHelp.cleanUserBean();
        }
        //判断如果是账号密码登录则进行账号密码回填
        SPUtils spUtils = SPUtils.getInstance(AppConstants.SPAction.SP_NAME);
        //进行账号回填
        mView.setUserLoginInfo(spUtils.getString(AppConstants.SPAction.USERNAME), spUtils.getString(AppConstants.SPAction.PASSWORD));
        //设置界面
        mView.setLoginUI(isPwLogin);
    }

    //切换登录类型
    public void setLoginType() {
        isPwLogin = !isPwLogin;
        mView.setLoginUI(isPwLogin);
    }

    //保存用户账号密码
    private void saveLoginUserAccount(String userName, String password) {
        SPUtils spUtils = SPUtils.getInstance(AppConstants.SPAction.SP_NAME);
        if (!StringUtils.isEmpty(userName)) {
            spUtils.put(AppConstants.SPAction.USERNAME, userName);
        }
        if (!StringUtils.isEmpty(password)) {
            spUtils.put(AppConstants.SPAction.PASSWORD, password);
        }
    }


    //保存登录信息
    private void saveUserInfo(UserBean userBean) {
        UserTable userTable = new UserTable();
        userTable.setAuthorization(userBean.authorization);
        userTable.setUserid(userBean.userId);
        userTable.setQqnumber(userBean.qq);
        userTable.setMobile(userBean.mobile);
        userTable.setUsername(userBean.userName);
        userTable.setUsernick(userBean.nickName);
        userTable.setAvatar(userBean.imgUrl);
        userBeanHelp.saveUserBean(userTable);

        //打开推送
        ((AppApplication) application).addAndDeleteJPushAlias();
    }


    //登录
    public void onLogin(final String phone, final String password, final String code) {
        //判断登录模式
        if (isPwLogin) {
            if (StringUtils.isEmpty(password)) {
                ToastUtils.showShort("请输入密码");
                return;
            }
            //正常登陆
            toLogin(phone, password);
        } else {
            if (StringUtils.isEmpty(code)) {
                ToastUtils.showShort("请输入验证码");
                return;
            }
            toSMSLogin(phone, code);
        }

    }

    //开始倒计时
    private void startCountdown(final int count) {
        Flowable.intervalRange(1, count, 0, 1, TimeUnit.SECONDS)
                .compose(RxSchedulers.io_main())
                .as(mView.bindLifecycle())
                .subscribe(aLong -> mView.setCountdownText(count - aLong));
    }


    private Handler handler = new Handler(msg -> {
        Bundle bundle = msg.getData();
        openId = bundle.getString("openId");
        unionId = bundle.getString("unionId");
        source = bundle.getString("source");
        toLoginByOpenId();
        return false;
    });

    //第三方登录
    public void onOtherLogin(String platformName) {
        mView.showLoading();
        Tools.doOtherLogin(platformName, new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                PlatformDb platformDb = platform.getDb();
                String unionId = platformDb.get("unionid");
                String openId = platformDb.getUserId();
                String source = "APPQQ";
                if ("Wechat".equals(platformDb.getPlatformNname())) {
                    source = "APPWX";
                }
                Message message = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("openId", openId);
                bundle.putString("unionId", unionId);
                bundle.putString("source", source);
                message.setData(bundle);
                handler.sendMessageAtFrontOfQueue(message);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                mView.hideLoading();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                mView.hideLoading();
            }
        });
    }

    private String openId;

    private String unionId;

    private String source;

    //第三方登录
    public void toLoginByOpenId() {
        apiPassportService.toLoginByOpenId(openId, unionId)
                .compose(RxSchedulers.io_main_business())
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<UserBean>() {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        userInfoIndex(userBean.authorization, null, null);
                    }

                    @Override
                    public void onError(String errStr) {
                        mView.hideLoading();
                        if ("无绑定信息".equals(errStr)) {
                            ARouter.getInstance().build(AppConstants.PagePath.LOGIN_PHONEBIND)
                                    .withString("openId", openId)
                                    .withString("unionId", unionId)
                                    .withString("source", source)
                                    .navigation();
                        } else {
                            ToastUtils.showShort(errStr);
                        }
                    }
                });
    }


    //账号密码登录
    private void toLogin(final String phone, final String password) {
        apiPassportService.toLogin(phone, password, AppConfig.getChannelValue())
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading("登录中").setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<UserBean>() {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        userInfoIndex(userBean.authorization, phone, password);
                    }

                    @Override
                    public void onError(String errStr) {
                        mView.hideLoading();
                        ToastUtils.showShort(errStr);
                    }
                });
    }


    //验证码登录
    private void toSMSLogin(final String phone, final String code) {
        apiPassportService.toSMSLogin(phone, code, AppConfig.getChannelValue())
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading("登录中").setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<UserBean>() {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        userInfoIndex(userBean.authorization, phone, null);
                    }

                    @Override
                    public void onError(String errStr) {
                        mView.hideLoading();
                        ToastUtils.showShort(errStr);
                    }
                });
    }

    //请求用户信息
    private void userInfoIndex(String authorization, final String phone, final String password) {
        apiService.userInfoIndex(authorization)
                .compose(RxSchedulers.io_main_business())
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<UserBean>(mView) {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        ToastUtils.showShort("登录成功");
                        saveLoginUserAccount(phone, password);
                        userBean.authorization = authorization;
                        saveUserInfo(userBean);
                        if (isCleanUser) {
                            Intent intent = new Intent(mView.getContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mView.getContext().startActivity(intent);
                        } else {
                            //如果是主页过来，则跳转主页
                            ((Activity) mView.getContext()).onBackPressed();
                        }
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }

    //获取url连接验证(1，短信登录，2，登录频繁)
    public void onVerifyImageCode(int typeCode) {
        apiCommonService.getUrl(AppConfig.CLIENT_TYPE, 1)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>() {
                    @Override
                    public void onSuccess(String jsurl) {
                        mView.hideLoading();
                        mView.gotoVerifyFullScreenActivity(jsurl, typeCode);
                    }


                    @Override
                    public void onError(String errStr) {
                        if ("601".equals(errStr)) {
                            mView.doGetCode();
                        } else {
                            mView.hideLoading();
                            ToastUtils.showShort(errStr);
                        }

                    }
                });
    }


    //发送验证码
    public void onGetCode(String phone, String ticket) {
        apiCommonService.sendCode(phone, ticket, AppConfig.getChannelValue(), 1)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading("获取验证码").setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>(mView) {

                    @Override
                    public void onSuccess(String str) {
                        //获取验证码成功，开始倒计时
                        startCountdown(60);
                        //如果是测试环境则直接显示
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

}
