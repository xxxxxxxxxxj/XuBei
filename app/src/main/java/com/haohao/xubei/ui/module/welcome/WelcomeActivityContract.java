package com.haohao.xubei.ui.module.welcome;

import com.haohao.xubei.ui.module.base.IABaseContract;

/**
 * 欢迎页
 * date：2017/5/3 10:10
 * author：Seraph
 *
 **/
interface WelcomeActivityContract extends IABaseContract {

    interface View extends IBaseView {

        void jumpNextActivity();

        void setADImageUrl(String adUrl);

        void setADShowCount(long tempCount);
    }

    abstract class Presenter extends ABasePresenter<View> {

    }

}
