package com.xubei.shop.ui.module.main.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.xubei.shop.AppConstants;
import com.xubei.shop.data.db.help.UserBeanHelp;
import com.xubei.shop.data.network.rx.RxSchedulers;
import com.xubei.shop.data.network.service.Api8Service;
import com.xubei.shop.data.network.service.ApiUserNewService;
import com.xubei.shop.ui.module.base.ABaseSubscriber;
import com.xubei.shop.ui.module.main.contract.MainMeContract;
import com.xubei.shop.ui.module.user.model.AcctManageBean;

import javax.inject.Inject;

/**
 * 我的逻辑处理层
 * date：2017/2/15 11:27
 * author：Seraph
 **/
public class MainMePresenter extends MainMeContract.Presenter {


    private UserBeanHelp userBeanHelp;

    private ApiUserNewService apiUserService;

    private Api8Service api8Service;

    @Inject
    MainMePresenter(UserBeanHelp userBeanHelp, ApiUserNewService apiUserService, Api8Service api8Service) {
        this.userBeanHelp = userBeanHelp;
        this.apiUserService = apiUserService;
        this.api8Service = api8Service;
    }

    @Override
    public void start() {
    }

    public boolean isLogin() {
        return userBeanHelp.isLogin(true);
    }

    public void updateUserBean() {
        RxBus.get().post(AppConstants.RxBusAction.TAG_MAIN_ME2, true);
        if (userBeanHelp.getUserBean() != null) {
            doAccountInfo();
            hasUnreadMessage();
        }
    }

    //获取账号资金信息
    private void doAccountInfo() {
        apiUserService.acctManageIndex(userBeanHelp.getAuthorization())
                .compose(RxSchedulers.io_main_business())
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<AcctManageBean>(mView) {
                    @Override
                    protected void onSuccess(AcctManageBean acctManageBean) {
                        if (mView == null || acctManageBean == null) {
                            return;
                        }
                        //发通知告诉我的界面资金数据更新
                        RxBus.get().post(AppConstants.RxBusAction.TAG_MAIN_ME, acctManageBean);
                    }

                    @Override
                    protected void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }


    //查询是否有未读消息
    private void hasUnreadMessage() {
        api8Service.hasUnreadMessage(userBeanHelp.getAuthorization())
                .compose(RxSchedulers.io_main_business())
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<Boolean>() {
                    @Override
                    protected void onSuccess(Boolean isNewMsg) {
                        mView.updateIsNewMsg(isNewMsg);
                    }

                    @Override
                    protected void onError(String errStr) {

                    }
                });
    }

}
