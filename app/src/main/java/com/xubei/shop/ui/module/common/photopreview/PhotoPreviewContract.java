package com.xubei.shop.ui.module.common.photopreview;

import com.xubei.shop.ui.module.base.IABaseContract;

import java.util.ArrayList;

/**
 * 图片预览契约类
 * date：2017/2/27 14:24
 * author：Seraph
 * mail：417753393@qq.com
 **/
interface PhotoPreviewContract extends IABaseContract {

    /**
     * 操作ui
     */
    interface View extends IBaseView {

        void finish();

        void setPhotoList(ArrayList<PhotoPreviewBean> mPhotoList);

        void showPageSelected(int position, int size);

        void hideSaveBtn();

    }

    abstract class Presenter extends ABasePresenter<View> {


    }


}
