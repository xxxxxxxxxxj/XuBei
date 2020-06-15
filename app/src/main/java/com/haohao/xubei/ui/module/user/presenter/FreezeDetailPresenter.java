package com.haohao.xubei.ui.module.user.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.ApiUserNewService;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.base.BaseData;
import com.haohao.xubei.ui.module.user.contract.FreezeDetailContract;
import com.haohao.xubei.ui.module.user.model.FreezeDetailBean;
import com.haohao.xubei.ui.views.NoDataView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 冻结明细
 * date：2018/10/24 15:45
 * author：xiongj
 **/
public class FreezeDetailPresenter extends FreezeDetailContract.Presenter {

    private ApiUserNewService apiUserNewService;

    private UserBeanHelp userBeanHelp;

    @Inject
    FreezeDetailPresenter(ApiUserNewService apiUserNewService, UserBeanHelp userBeanHelp) {
        this.apiUserNewService = apiUserNewService;
        this.userBeanHelp = userBeanHelp;
    }


    private List<FreezeDetailBean> list = new ArrayList<>();

    private int pageNo = 0;

    @Override
    public void start() {
        mView.initLayout(list);
        doRefresh();
    }


    //刷新第一页
    public void doRefresh() {
        getFreezeDetail(1);
    }

    //下一页
    public void nextPage() {
        getFreezeDetail(pageNo + 1);
    }


    //获取资金明细
    private void getFreezeDetail(int tempPageNo) {
        apiUserNewService.getFreezeDetail(userBeanHelp.getAuthorization(), tempPageNo, AppConfig.PAGE_SIZE)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.setNoDataView(NoDataView.LOADING))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<BaseData<FreezeDetailBean>>(mView) {
                    @Override
                    public void onSuccess(BaseData<FreezeDetailBean> baseData) {
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
