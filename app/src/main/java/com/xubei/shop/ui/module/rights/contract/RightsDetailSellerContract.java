package com.xubei.shop.ui.module.rights.contract;

import com.xubei.shop.ui.module.base.IABaseContract;
import com.xubei.shop.ui.module.rights.model.LeaseeArbBean;

/**
 * 维权详情契约
 * date：2017/12/13 09:55
 * author：Seraph
 * mail：417753393@qq.com
 **/
public interface RightsDetailSellerContract extends IABaseContract {

    interface View extends IBaseView {

        void setLeaseeArbBean(LeaseeArbBean leaseeArbBean);

        void setNoDataView(int type);
    }

    abstract class Presenter extends ABasePresenter<View> {


    }

}
