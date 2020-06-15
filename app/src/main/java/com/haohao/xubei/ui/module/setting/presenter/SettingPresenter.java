package com.haohao.xubei.ui.module.setting.presenter;

import android.app.Application;

import com.blankj.utilcode.util.AppUtils;
import com.tencent.bugly.beta.Beta;
import com.haohao.xubei.AppApplication;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.ui.module.setting.contract.SettingContract;

import javax.inject.Inject;

import androidx.appcompat.app.AlertDialog;

/**
 * 设置
 * date：2017/12/4 14:38
 * author：Seraph
 **/
public class SettingPresenter extends SettingContract.Presenter {

    private UserBeanHelp userBeanHelp;

    private Application application;

    @Inject
    SettingPresenter(Application application, UserBeanHelp userBeanHelp) {
        this.userBeanHelp = userBeanHelp;
        this.application = application;
    }

    @Override
    public void start() {
        //获取程序版本号
        String appVersionName = AppUtils.getAppVersionName();
        mView.setAppCode(appVersionName, AppConfig.getBusinessNo());
    }


    public void doLogOut() {
        //注销
        new AlertDialog.Builder(mView.getContext())
                .setTitle("账号退出")
                .setMessage("您是否确定注销登录？").setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialog, which) -> {
                    userBeanHelp.cleanUserBean();
                    ((AppApplication) application).addAndDeleteJPushAlias();
                    mView.finish();
                }).show();
    }

    //检查更新
    public void checkUpgrade() {
        Beta.checkUpgrade();
    }

}
