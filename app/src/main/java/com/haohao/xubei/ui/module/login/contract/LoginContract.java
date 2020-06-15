package com.haohao.xubei.ui.module.login.contract;


import com.haohao.xubei.ui.module.base.IABaseContract;

/**
 * 登录契约类
 * date：2017/10/25 10:29
 * author：Seraph
 **/
public interface LoginContract extends IABaseContract {

    interface View extends IBaseView{

        void setUserLoginInfo(String username,String passWord);

        void setLoginUI(boolean isPwLogin);

        void setCountdownText(long time);

        void doShowCodeOK();

        void doShowCodeOK(String code);

        void gotoVerifyFullScreenActivity(String jsurl,int requestCode);

        void doGetCode();
    }

    abstract class Presenter extends ABasePresenter<View>{

    }

}
