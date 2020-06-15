package com.haohao.xubei.ui.module.order.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.ApiUserNewService;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.order.contract.OrderRDetailContract;
import com.haohao.xubei.ui.module.order.model.OutOrderBean;
import com.haohao.xubei.ui.views.NoDataView;

import javax.inject.Inject;

/**
 * 订单发布详情
 * date：2018/8/2 11:44
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class OrderRDetailPresenter extends OrderRDetailContract.Presenter {

    private UserBeanHelp userBeanHelp;

    private ApiUserNewService apiUserNewService;

    private String orderNo;


    @Inject
    public OrderRDetailPresenter(ApiUserNewService apiUserNewService, UserBeanHelp userBeanHelp, String orderNo) {
        this.userBeanHelp = userBeanHelp;
        this.apiUserNewService = apiUserNewService;
        this.orderNo = orderNo;
    }

    @Override
    public void start() {
        doFindOrderItem();
    }


    //获取订单详情
    private void doFindOrderItem() {
        apiUserNewService.getOrderDetail(userBeanHelp.getAuthorization(), orderNo)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.setNoDataView(NoDataView.LOADING))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<OutOrderBean>(mView) {

                    @Override
                    public void onSuccess(OutOrderBean orderBean) {
                        mView.setOrderBean(orderBean);
                        mView.setNoDataView(NoDataView.LOADING_OK);
                    }

                    @Override
                    public void onError(String errStr) {
                        mView.setNoDataView(NoDataView.NET_ERR);
                        ToastUtils.showShort(errStr);
                    }
                });
    }


}
