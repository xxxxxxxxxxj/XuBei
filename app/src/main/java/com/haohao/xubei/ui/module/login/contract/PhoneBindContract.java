package com.haohao.xubei.ui.module.login.contract;

import com.haohao.xubei.ui.module.base.IABaseContract;

/**
 * 绑定手机
 * date：2017/10/25 10:29
 * author：Seraph
 **/
public interface PhoneBindContract extends IABaseContract {

    interface View extends IBaseView{

        void setCountdownText(long time);

        void doShowCodeOK();

        void doShowCodeOK(String code);

        void gotoVerifyFullScreenActivity(String jsurl);

        void doGetCode();
    }

    abstract class Presenter extends ABasePresenter<View>{

    }

}
