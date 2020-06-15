package com.haohao.xubei.ui.module.rights.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.ApiUserNewService;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.rights.contract.RightsDetailSellerContract;
import com.haohao.xubei.ui.module.rights.model.LeaseeArbBean;
import com.haohao.xubei.ui.views.NoDataView;

import javax.inject.Inject;

/**
 * 卖家维权详情
 * date：2017/12/13 09:58
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class RightsDetailSellerPresenter extends RightsDetailSellerContract.Presenter {

    private UserBeanHelp userBeanHelp;
    private ApiUserNewService apiUserNewService;
    private String orderNo;

    @Inject
    RightsDetailSellerPresenter(UserBeanHelp userBeanHelp, ApiUserNewService apiUserNewService, String orderNo) {
        this.userBeanHelp = userBeanHelp;
        this.apiUserNewService = apiUserNewService;
        this.orderNo = orderNo;
    }

    private LeaseeArbBean mLeaseeArbBean;

    @Override
    public void start() {
        doFindArbitrationItem();
    }


    //查看维权详情
    private void doFindArbitrationItem() {
        apiUserNewService.toLeaseeArbList(userBeanHelp.getAuthorization(), orderNo)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.setNoDataView(NoDataView.LOADING))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<LeaseeArbBean>(mView) {
                    @Override
                    public void onSuccess(LeaseeArbBean leaseeArbBean) {
                        mLeaseeArbBean = leaseeArbBean;
                        mView.setLeaseeArbBean(mLeaseeArbBean);
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
