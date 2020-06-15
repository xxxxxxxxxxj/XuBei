package com.haohao.xubei.ui.module.user.contract;

import com.haohao.xubei.ui.module.base.IABaseContract;

/**
 * 更新绑定手机
 * date：2017/10/25 10:29
 * author：Seraph
 **/
public interface UpdateBindPhoneContract extends IABaseContract {

    interface View extends IBaseView {

        void setCountdownText(long time);

        void gotoVerifyFullScreenActivity(String jsurl);

        void doGetCode();
    }

    abstract class Presenter extends ABasePresenter<View> {

    }

}
