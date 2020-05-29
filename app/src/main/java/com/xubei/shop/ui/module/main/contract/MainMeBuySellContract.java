package com.xubei.shop.ui.module.main.contract;

import com.xubei.shop.data.db.table.UserTable;
import com.xubei.shop.ui.module.base.IABaseContract;

/**
 * 个人中心界面
 * date：2017/2/20 17:06
 * author：Seraph
 *
 **/
public interface MainMeBuySellContract extends IABaseContract {

    interface View extends IBaseView {

        void setUserInfo(UserTable userTable);

    }

    abstract class Presenter extends ABasePresenter<View> {

    }
}
