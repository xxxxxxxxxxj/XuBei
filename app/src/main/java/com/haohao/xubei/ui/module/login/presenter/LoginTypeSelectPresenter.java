package com.haohao.xubei.ui.module.login.presenter;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.AppApplication;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.data.db.table.UserTable;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.ApiPassportService;
import com.haohao.xubei.data.network.service.ApiUserNewService;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.login.contract.LoginTypeSelectContract;
import com.haohao.xubei.ui.module.login.model.UserBean;
import com.haohao.xubei.ui.module.main.MainActivity;
import com.haohao.xubei.utlis.Tools;

import java.util.HashMap;

import javax.inject.Inject;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;

/**
 * 登录选择逻辑
 * date：2017/10/25 10:43
 * author：Seraph
 **/
public class LoginTypeSelectPresenter extends LoginTypeSelectContract.Presenter {

    private ApiUserNewService apiService;

    private UserBeanHelp userBeanHelp;

    private ApiPassportService apiPassportService;

    private Application application;

    //是否清理用户登录信息
    private boolean isCleanUser;

    @Inject
    LoginTypeSelectPresenter(Application application, ApiPassportService apiPassportService, ApiUserNewService apiService, UserBeanHelp userBeanHelp, Boolean isCleanUser) {
        this.application = application;
        this.apiService = apiService;
        this.userBeanHelp = userBeanHelp;
        this.apiPassportService = apiPassportService;
        this.isCleanUser = isCleanUser;
    }

    @Override
    public void start() {
        //是否清理用户信息
        if (isCleanUser) {
            userBeanHelp.cleanUserBean();
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

        //打开推送绑定
        ((AppApplication) application).addAndDeleteJPushAlias();
    }


    private Handler handler = new Handler(msg -> {
        Bundle bundle = msg.getData();
        openId = bundle.getString("openId");
        unionId = bundle.getString("unionId");
        source = bundle.getString("source");
        if (mView != null) {
            toLoginByOpenId();
        }
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
                if (mView != null) {
                    mView.hideLoading();
                }
            }

            @Override
            public void onCancel(Platform platform, int i) {
                if (mView != null) {
                    mView.hideLoading();
                }
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
                        userInfoIndex(userBean.authorization);
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


    //请求用户信息
    private void userInfoIndex(String authorization) {
        apiService.userInfoIndex(authorization)
                .compose(RxSchedulers.io_main_business())
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<UserBean>(mView) {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        ToastUtils.showShort("登录成功");
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


    //跳转账号密码登录
    public void startLogin() {
        ARouter.getInstance().build(AppConstants.PagePath.LOGIN_LOGIN).withBoolean("isCleanUser", isCleanUser).navigation();
        mView.finish();
    }
}
