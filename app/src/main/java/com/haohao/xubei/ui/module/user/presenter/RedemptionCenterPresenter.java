package com.haohao.xubei.ui.module.user.presenter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.ApiCommonService;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.base.BaseData;
import com.haohao.xubei.ui.module.user.contract.RedemptionCenterContract;
import com.haohao.xubei.ui.module.user.model.RedemptionCenterBean;
import com.haohao.xubei.ui.module.user.model.RedemptionGameBean;
import com.haohao.xubei.ui.views.NoDataView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.appcompat.app.AlertDialog;

/**
 * 兑换中心
 * date：2017/12/4 15:04
 * author：Seraph
 **/
public class RedemptionCenterPresenter extends RedemptionCenterContract.Presenter {

    private UserBeanHelp userBeanHelp;

    private ApiCommonService apiCommonService;

    //游戏类型列表
    private List<RedemptionGameBean> gameList;

    @Inject
    RedemptionCenterPresenter(UserBeanHelp userBeanHelp, ApiCommonService apiCommonService, List<RedemptionGameBean> gameList) {
        this.userBeanHelp = userBeanHelp;
        this.apiCommonService = apiCommonService;
        this.gameList = gameList;
    }

    //兑换列表
    private List<RedemptionCenterBean> list = new ArrayList<>();

    //当前选择的兑换券
    private RedemptionCenterBean centerBean;

    @Override
    public void start() {
        mView.initLayout(list);
        onRefresh();
    }

    public void onItemClick(int position) {
        centerBean = list.get(position);
        if (centerBean.status != 1) {
            ToastUtils.showShort("已兑换或已失效");
            return;
        }
        //游戏试玩类型
        if (centerBean.userSource == 1) {
            mView.showExchangePop(gameList);
        } else {
            new AlertDialog.Builder(mView.getContext())
                    .setTitle("提示：")
                    .setMessage("确认立即兑换？")
                    .setPositiveButton("确认兑换", (dialog, which) -> {
                        //视频类型id 1826
                        exchange("1826");
                    })
                    .setNegativeButton("取消", null)
                    .show();

        }

    }

    //兑换的列表位置
    public void doDHPosition(int selectP) {
        exchange(gameList.get(selectP).gameId);
    }

    public void onRefresh() {
        getByMobile();
    }

    //获取兑换券列表
    private void getByMobile() {
        apiCommonService.getByMobile(userBeanHelp.getAuthorization(), userBeanHelp.getUserBean().getMobile())
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.setNoDataView(NoDataView.LOADING))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<List<RedemptionCenterBean>>() {
                    @Override
                    protected void onSuccess(List<RedemptionCenterBean> centerBeanList) {
                        list.clear();
                        if (centerBeanList.size() == 0) {
                            mView.setNoDataView(NoDataView.NO_DATA);
                        } else {
                            list.addAll(centerBeanList);
                            mView.setNoDataView(NoDataView.LOADING_OK);
                        }
                        mView.notifyDataSetChanged();
                    }

                    @Override
                    protected void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                        mView.setNoDataView(NoDataView.NET_ERR);
                    }
                });
    }

    //兑换
    private void exchange(String gameId) {
        apiCommonService.exchange(userBeanHelp.getAuthorization(), userBeanHelp.getUserBean().getMobile(), centerBean.id, gameId)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<BaseData>(mView) {
                    @Override
                    protected void onSuccess(BaseData baseData) {
                        ToastUtils.showShort("恭喜您，兑换成功！");
                        //刷新列表
                        mView.onAutoRefresh();
                        //跳转记录
                        ARouter.getInstance().build(AppConstants.PagePath.USER_REDEMPTIONRECORD).navigation();
                    }

                    @Override
                    protected void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }

}
