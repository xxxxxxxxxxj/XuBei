package com.haohao.xubei.ui.module.user.presenter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.ApiUserNewService;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.user.contract.PurseContract;
import com.haohao.xubei.ui.module.user.model.AcctManageBean;

import javax.inject.Inject;

/**
 * 我的钱包
 * date：2017/12/4 15:04
 * author：Seraph
 **/
public class PursePresenter extends PurseContract.Presenter {

    private UserBeanHelp userBeanHelp;
    private ApiUserNewService apiUserService;

    @Inject
    public PursePresenter(UserBeanHelp userBeanHelp, ApiUserNewService apiUserService) {
        this.userBeanHelp = userBeanHelp;
        this.apiUserService = apiUserService;
    }

    private AcctManageBean acctManageBean;

    @Override
    public void start() {
        doAccountInfo(false, 0);
    }


    //获取账号资金信息(是否检查密码) (1,充值，2，提现)
    public void doAccountInfo(boolean isCheckPayPwd, int type) {
        apiUserService.acctManageIndex(userBeanHelp.getAuthorization())
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<AcctManageBean>(mView) {
                    @Override
                    public void onSuccess(AcctManageBean accountMoney) {
                        acctManageBean = accountMoney;
                        mView.setAccountMoney(acctManageBean);
                        //是否是检查设置支付密码
                        if (isCheckPayPwd) {
                            if (acctManageBean.isPayPwd) {
                                switch (type) {
                                    case 1://充值
                                        ARouter.getInstance().build(AppConstants.PagePath.USER_PAY).navigation();
                                        break;
                                    case 2://提现
                                        ARouter.getInstance().build(AppConstants.PagePath.USER_GETMONEY)
                                                .withSerializable("bean", acctManageBean).navigation();
                                        break;
                                }
                            } else {
                                ToastUtils.showShort("请先设置支付密码");
                                ARouter.getInstance().build(AppConstants.PagePath.LOGIN_RESETPAYPW).navigation();
                            }
                        }
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }

}
