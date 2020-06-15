package com.haohao.xubei.ui.module.order.contract;

import com.haohao.xubei.ui.module.base.IABaseContract;
import com.haohao.xubei.ui.module.order.model.OutOrderBean;

import java.util.List;

/**
 * 全部订单
 * date：2017/12/4 14:58
 * author：Seraph
 * mail：417753393@qq.com
 **/
public interface OrderAllSellerContract extends IABaseContract {

    interface View extends IBaseView {

        void autoRefresh();

        void initLayout();

        void initInflateView(List<OutOrderBean> list);

        void notifyItemRangeChanged(int s, int size);

        void setNoDataView(int type);
    }

    abstract class Presenter extends ABasePresenter<View> {



    }

}
