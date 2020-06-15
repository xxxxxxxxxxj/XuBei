package com.haohao.xubei.ui.module.user.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.ApiUserNewService;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.user.contract.UserVerifiedContract;

import javax.inject.Inject;

/**
 * 实名验证
 * date：2017/12/22 16:54
 * author：Seraph
 *
 **/
public class UserVerifiedPresenter extends UserVerifiedContract.Presenter {

    private UserBeanHelp userBeanHelp;

    private ApiUserNewService apiUserNewService;

    @Inject
    public UserVerifiedPresenter(UserBeanHelp userBeanHelp, ApiUserNewService apiUserNewService) {
        this.userBeanHelp = userBeanHelp;
        this.apiUserNewService = apiUserNewService;
    }

    @Override
    public void start() {

    }


    //简单身份认证
    public void doVerified(String name, String idCard) {
        apiUserNewService.identityAuth(userBeanHelp.getAuthorization(), name, idCard)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String s) {
                        ToastUtils.showShort("信息提交成功");
                        mView.finish();
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }
}
