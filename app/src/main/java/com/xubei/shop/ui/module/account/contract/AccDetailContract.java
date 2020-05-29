package com.xubei.shop.ui.module.account.contract;

import com.xubei.shop.ui.module.account.model.OutGoodsDetailBean;
import com.xubei.shop.ui.module.base.IABaseContract;
import com.xubei.shop.ui.module.common.photopreview.PhotoPreviewBean;

import java.util.ArrayList;

/**
 * 账号详情
 * date：2017/11/28 11:15
 * author：Seraph
 * mail：417753393@qq.com
 **/
public interface AccDetailContract extends IABaseContract {

    interface View extends IBaseView {

        void setAccountDetail(OutGoodsDetailBean outGoodsDetailBean);

        void startLookImage(ArrayList<PhotoPreviewBean> list, int position);

        void onShowCollection(String type);

        void setNoDataView(int type);

        void showPayInputPop(String orderNo, String timeLong, String kyGold, String payGoldNo);

        void showShareDialog();
    }

    abstract class Presenter extends ABasePresenter<View> {

    }

}
