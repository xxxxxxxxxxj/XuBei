package com.xubei.shop.ui.module.account.contract;

import com.xubei.shop.ui.module.account.model.OutGoodsDetailBean;
import com.xubei.shop.ui.module.base.IABaseContract;

/**
 * 我的账号详情
 * date：2017/11/28 11:15
 * author：Seraph
 * mail：417753393@qq.com
 **/
public interface AccRDetailContract extends IABaseContract {

    interface View extends IBaseView {

        void onGoodsDetail(OutGoodsDetailBean outGoodsDetailBean);

        void setNoDataView(int type);
    }

    abstract class Presenter extends ABasePresenter<View> {

    }

}
