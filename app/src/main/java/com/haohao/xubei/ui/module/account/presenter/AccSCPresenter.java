package com.haohao.xubei.ui.module.account.presenter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.ApiUserNewService;
import com.haohao.xubei.ui.module.account.contract.AccSCContract;
import com.haohao.xubei.ui.module.account.model.AccSCBean;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.base.BaseData;
import com.haohao.xubei.ui.views.NoDataView;
import com.haohao.xubei.utlis.PageUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 收藏
 * date：2018/6/5 09:54
 * author：Seraph
 **/
public class AccSCPresenter extends AccSCContract.Presenter {

    private ApiUserNewService apiUserNewService;

    private UserBeanHelp mUserBeanHelp;

    private NoDataView noDataView;

    @Inject
    AccSCPresenter(ApiUserNewService apiUserNewService, UserBeanHelp userBeanHelp, NoDataView noDataView) {
        this.mUserBeanHelp = userBeanHelp;
        this.apiUserNewService = apiUserNewService;
        this.noDataView = noDataView.setNoDataMsg(R.mipmap.icon_no_collection, "快去收藏你喜欢的账号吧");
    }

    private int pageNo = 0;


    //收藏列表
    private List<AccSCBean> list = new ArrayList<>();

    @Override
    public void start() {
        mView.initLayout(list, noDataView);
        noDataView.setOnClickListener(v -> doRefresh());
        doRefresh();
    }

    //列表点击
    public void onItemClick(int position) {
        ARouter.getInstance().build(AppConstants.PagePath.ACC_DETAIL).withString("id", list.get(position).id).navigation();
    }

    public void doRefresh() {
        getAccountSCList(1);
    }

    public void doLoadMore() {
        getAccountSCList(pageNo + 1);
    }


    //请求列表
    private void getAccountSCList(final int tempPageNo) {
        apiUserNewService.getCollectionList(mUserBeanHelp.getAuthorization(), tempPageNo, AppConfig.PAGE_SIZE)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> noDataView.setType(NoDataView.LOADING))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<BaseData<AccSCBean>>(mView) {
                    @Override
                    public void onSuccess(BaseData<AccSCBean> listBaseData) {
                        pageNo = PageUtils.doSuccess(tempPageNo, list, listBaseData.data,
                                (start, size) -> mView.notifyDataSetChanged(start, size),
                                noDataView,
                                mView.getSrl());
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                        PageUtils.doError(noDataView, mView.getSrl());
                    }
                });
    }


    //收藏状态点击
    public void onItemSCClick(final int position) {
        apiUserNewService.addCollection(mUserBeanHelp.getAuthorization(), list.get(position).id)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(
                        dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String type) {
                        if ("收藏成功".equals(type)) {
                            list.get(position).isSC = true;
                            ToastUtils.showShort("账号收藏成功");
                        } else {
                            list.get(position).isSC = false;
                            ToastUtils.showShort("已取消账号收藏");
                        }
                        mView.notifyDataSetChanged(position, 1);
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }
}
