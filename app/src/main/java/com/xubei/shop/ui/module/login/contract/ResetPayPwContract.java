package com.xubei.shop.ui.module.login.contract;

import com.xubei.shop.data.db.table.UserTable;
import com.xubei.shop.ui.module.base.IABaseContract;

/**
 * 验证手机号契约类
 * date：2017/10/25 10:29
 * author：Seraph
 **/
public interface ResetPayPwContract extends IABaseContract {

    interface View extends IBaseView{

        void setCountdownText(long time);

        void setUserInfo(UserTable userBean);

        void doShowCodeOK(String code);

        void gotoVerifyFullScreenActivity(String jsurl);

    }

    abstract class Presenter extends ABasePresenter<View>{

    }

}
