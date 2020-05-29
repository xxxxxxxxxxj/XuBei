package com.xubei.shop.ui.module.user.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.xubei.shop.AppConfig;
import com.xubei.shop.data.db.help.UserBeanHelp;
import com.xubei.shop.data.network.rx.RxSchedulers;
import com.xubei.shop.data.network.service.ApiUserNewService;
import com.xubei.shop.ui.module.base.ABaseSubscriber;
import com.xubei.shop.ui.module.base.BaseData;
import com.xubei.shop.ui.module.user.contract.FundDetailsContract;
import com.xubei.shop.ui.module.user.model.FundDetailBean;
import com.xubei.shop.ui.views.NoDataView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 资金明细
 * date：2017/12/4 15:04
 * author：Seraph
 **/
public class FundDetailsPresenter extends FundDetailsContract.Presenter {

    private UserBeanHelp userBeanHelp;
    private ApiUserNewService apiUserService;

    @Inject
    public FundDetailsPresenter(UserBeanHelp userBeanHelp, ApiUserNewService apiUserService) {
        this.userBeanHelp = userBeanHelp;
        this.apiUserService = apiUserService;
    }

    private List<FundDetailBean> list = new ArrayList<>();

    private int pageNo = 0;

    //资金流向 0是进，1是出
    private Integer cashflow = null;

    @Override
    public void start() {
        mView.initLayout(list);
        doRefresh();
    }

    //设置类型
    public void setTabSelectd(int position) {
        switch (position) {
            case 0://全部
                cashflow = null;
                break;
            case 1://收入
                cashflow = 0;
                break;
            case 2://支出
                cashflow = 1;
                break;
        }
        mView.onAutoRefresh();
    }

    //刷新第一页
    public void doRefresh() {
        fundDetail(1);
    }

    //下一页
    public void nextPage() {
        fundDetail(pageNo + 1);
    }

    //获取资金明细
    private void fundDetail(int tempPageNo) {
        apiUserService.fundDetail(userBeanHelp.getAuthorization(), cashflow, tempPageNo, AppConfig.PAGE_SIZE)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.setNoDataView(NoDataView.LOADING))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<BaseData<FundDetailBean>>(mView) {
                    @Override
                    public void onSuccess(BaseData<FundDetailBean> baseData) {
                        if (tempPageNo == 1) {
                            list.clear();
                        }
                        //如果请求的页面和返回的页面不一致
                        if (baseData.currentPage == tempPageNo || baseData.currentPage == 0) {
                            list.addAll(baseData.data);
                            mView.notifyItemRangeChanged((tempPageNo - 1) * AppConfig.PAGE_SIZE, baseData.data.size());
                        } else {
                            ToastUtils.showShort("没有更多数据");
                        }
                        if (list.size() == 0) {
                            mView.setNoDataView(NoDataView.NO_DATA);
                        } else {
                            mView.setNoDataView(NoDataView.LOADING_OK);
                        }
                        pageNo = baseData.currentPage;
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                        mView.setNoDataView(NoDataView.NET_ERR);
                    }
                });
    }


}
