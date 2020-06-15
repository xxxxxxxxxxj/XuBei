package com.haohao.xubei.ui.module.user.presenter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.ApiUserNewService;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.user.contract.UserGetMoneyContract;
import com.haohao.xubei.ui.module.user.model.AcctManageBean;

import javax.inject.Inject;

/**
 * 提现
 * date：2017/12/22 11:55
 * author：Seraph
 *
 **/
public class UserGetMoneyPresenter extends UserGetMoneyContract.Presenter {

    private ApiUserNewService apiUserService;

    private UserBeanHelp userBeanHelp;

    private AcctManageBean mAcctManageBean;

    @Inject
    public UserGetMoneyPresenter(ApiUserNewService apiUserService, UserBeanHelp userBeanHelp) {
        this.apiUserService = apiUserService;
        this.userBeanHelp = userBeanHelp;
    }


    @Override
    public void start() {
        //初始化界面
        acctManageIndex(false);
    }

    //添加支付卡
    public void doAddPayCar() {
        //判断是否实名认证了
        acctManageIndex(true);
    }


    //提现
    public void doWithdraw(String money) {
        if (ObjectUtils.isEmpty(mAcctManageBean.alipayAcct)) {
            ToastUtils.showShort("请添加支付宝账号");
            return;
        }
        ToastUtils.showShort("提现->" + money);
        RxBus.get().post(AppConstants.RxBusAction.TAG_ACCOUNT_UPDATA, true);
    }


    //账号管理，true是否检查身份认证   false获取其它信息
    private void acctManageIndex(boolean isCertificate) {
        apiUserService.acctManageIndex(userBeanHelp.getAuthorization())
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<AcctManageBean>(mView) {
                    @Override
                    public void onSuccess(AcctManageBean acctManageBean) {
                        mAcctManageBean = acctManageBean;
                        if (isCertificate) {
                            if (acctManageBean.isCertificate) {
                                //已经验证，直接跳转添加支付宝账号
                                ARouter.getInstance().build(AppConstants.PagePath.USER_ALIPAYADD).navigation();
                            } else {
                                //跳转实名认证界面
                                ToastUtils.showShort("请先进行身份认证");
                                ARouter.getInstance().build(AppConstants.PagePath.USER_VERIFIED).navigation();
                            }
                        } else {
                            mView.initLayout(mAcctManageBean);
                        }

                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }

}
