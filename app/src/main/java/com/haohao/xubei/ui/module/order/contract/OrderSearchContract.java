package com.haohao.xubei.ui.module.order.contract;

import com.haohao.xubei.ui.module.base.IABaseContract;
import com.haohao.xubei.ui.module.order.model.OutOrderBean;

import java.util.List;

/**
 * 订单搜索
 * date：2017/12/4 14:58
 * author：Seraph
 *
 **/
public interface OrderSearchContract extends IABaseContract {

    interface View extends IBaseView {

        void notifyItemRangeChanged(int s, int size);

        void initLayout(List<OutOrderBean> list);
    }

    abstract class Presenter extends ABasePresenter<View> {


    }

}
