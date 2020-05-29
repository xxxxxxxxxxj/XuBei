package com.xubei.shop.ui.module.user.contract;

import android.net.Uri;

import com.xubei.shop.data.db.table.UserTable;
import com.xubei.shop.ui.module.base.IABaseContract;

/**
 * 个人中心
 * date：2017/12/4 14:58
 * author：Seraph
 *
 **/
public interface UserInfoContract extends IABaseContract {

    interface View extends IBaseView {

        void setUserInfo(UserTable userBean);

        void setIdCartAuth();

        void onUCropImage(Uri sourceUri, Uri destinationUri);
    }

    abstract class Presenter extends ABasePresenter<View> {

    }

}
