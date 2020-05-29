package com.xubei.shop.ui.module.main.presenter;

import com.xubei.shop.data.db.help.UserBeanHelp;
import com.xubei.shop.data.db.table.UserTable;
import com.xubei.shop.ui.module.common.photopreview.PhotoPreviewActivity;
import com.xubei.shop.ui.module.common.photopreview.PhotoPreviewBean;
import com.xubei.shop.ui.module.main.contract.MainMeBuySellContract;

import javax.inject.Inject;

/**
 * 我的逻辑处理层
 * date：2017/2/15 11:27
 * author：Seraph
 **/
public class MainMeBuySellPresenter extends MainMeBuySellContract.Presenter {


    private UserBeanHelp userBeanHelp;

    @Inject
    MainMeBuySellPresenter(UserBeanHelp userBeanHelp) {
        this.userBeanHelp = userBeanHelp;
    }

    @Override
    public void start() {

    }

    public void onSeeBigAvatar() {
        UserTable userTable = userBeanHelp.getUserBean(true);
        if (userTable != null) {
            //查看大图
            PhotoPreviewBean photoPreviewBean = new PhotoPreviewBean();
            photoPreviewBean.objURL = userTable.getAvatar();
            PhotoPreviewActivity.startPhotoPreview(photoPreviewBean);
        }
    }


    //是否登录
    public boolean isLogin() {
        return userBeanHelp.isLogin(true);
    }


    //更新登录信息
    public void updateUserBean() {
        mView.setUserInfo(userBeanHelp.getUserBean());
    }

}
