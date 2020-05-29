package com.xubei.shop.ui.module.login.contract;

import com.xubei.shop.ui.module.base.IABaseContract;

/**
 * 注册契约类
 * date：2017/10/25 10:29
 * author：Seraph
 **/
public interface RegisteredContract extends IABaseContract {

    interface View extends IBaseView{

        void setCountdownText(long time);

        void doShowCodeOK(String code);

        void gotoVerifyFullScreenActivity(String jsurl);

        void doGetCode();
    }

    abstract class Presenter extends ABasePresenter<View>{

    }

}
