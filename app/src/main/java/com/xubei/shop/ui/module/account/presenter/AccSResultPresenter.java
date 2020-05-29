package com.xubei.shop.ui.module.account.presenter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.xubei.shop.AppConfig;
import com.xubei.shop.AppConstants;
import com.xubei.shop.data.network.rx.RxSchedulers;
import com.xubei.shop.data.network.service.Api8Service;
import com.xubei.shop.ui.module.account.contract.AccSResultContract;
import com.xubei.shop.ui.module.account.model.AccBean;
import com.xubei.shop.ui.module.base.ABaseSubscriber;
import com.xubei.shop.ui.module.base.BaseData;
import com.xubei.shop.ui.views.NoDataView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * 账号搜索结果列表
 * date：2017/11/28 11:23
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class AccSResultPresenter extends AccSResultContract.Presenter {

    private Api8Service api8Service;

    private String searchStr;

    @Inject
    AccSResultPresenter(Api8Service api8Service, String searchStr) {
        this.api8Service = api8Service;
        this.searchStr = searchStr;
    }

    private List<AccBean> list = new ArrayList<>();

    private int pageNo = 0;

    @Override
    public void start() {
        mView.setTitle(searchStr);
        mView.initLayout(list);
        doRefresh();
    }

    public void onItemClick(int position) {
        ARouter.getInstance().build(AppConstants.PagePath.ACC_DETAIL).withString("id",list.get(position).id).navigation();
    }


    public void doRefresh() {
        //刷新第一页
        findGoodsList(1);
    }

    public void nextPage() {
        //下一页
        findGoodsList(pageNo + 1);
    }


    private void findGoodsList(final int tempPageNo) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("searchText", searchStr);
        map.put("pageSize", AppConfig.PAGE_SIZE);
        map.put("pageIndex", tempPageNo);
        map.put("businessNo", AppConfig.getChannelValue());
        api8Service.findGoodsList(map)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.setNoDataView(NoDataView.LOADING))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<BaseData<AccBean>>(mView) {
                    @Override
                    public void onSuccess(BaseData<AccBean> accountList) {
                        if (tempPageNo == 1) {
                            list.clear();
                        }
                        list.addAll(accountList.list);
                        mView.notifyItemRangeChanged((tempPageNo - 1) * AppConfig.PAGE_SIZE, accountList.list.size());
                        if (accountList.list.size() == 0 && tempPageNo != 1) {
                            ToastUtils.showShort("没有更多数据");
                        } else {
                            pageNo = tempPageNo;
                        }
                        if (list.size() == 0) {
                            mView.setNoDataView(NoDataView.NO_DATA);
                        } else {
                            mView.setNoDataView(NoDataView.LOADING_OK);
                        }
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                        mView.setNoDataView(NoDataView.NET_ERR);
                    }
                });
    }


}
