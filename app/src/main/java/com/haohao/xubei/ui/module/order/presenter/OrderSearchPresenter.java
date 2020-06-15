package com.haohao.xubei.ui.module.order.presenter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.ApiUserNewService;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.base.BaseData;
import com.haohao.xubei.ui.module.order.contract.OrderSearchContract;
import com.haohao.xubei.ui.module.order.model.OutOrderBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import androidx.appcompat.app.AlertDialog;

/**
 * 订单搜索
 * date：2018/3/15 14:20
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class OrderSearchPresenter extends OrderSearchContract.Presenter {

    private UserBeanHelp userBeanHelp;

    private ApiUserNewService apiUserNewService;

    @Inject
    OrderSearchPresenter(ApiUserNewService apiUserNewService, UserBeanHelp userBeanHelp) {
        this.userBeanHelp = userBeanHelp;
        this.apiUserNewService = apiUserNewService;
    }

    private String tempSearch;

    private List<OutOrderBean> list = new ArrayList<>();

    //页码
    private int pageNo = 0;

    @Override
    public void start() {
        mView.initLayout(list);
    }

    public void doSearch(String inputStr) {
        if (StringUtils.isEmpty(inputStr)) {
            ToastUtils.showShort("请输入需要搜索的关键字");
            return;
        }
        tempSearch = inputStr;
        mView.showLoading();
        doRefresh();
    }

    //下拉刷新
    public void doRefresh() {
        findOrderList(1);
    }

    //上拉加载
    public void doNextPage() {
        findOrderList(pageNo + 1);
    }

    //跳转订单详情
    public void doItemClick(int position) {
        ARouter.getInstance().build(AppConstants.PagePath.ORDER_DETAIL)
                .withString("orderNo",list.get(position).gameNo)
                .navigation();
    }

    //续租
    public void doCheckOrderIsPay(int p) {
        OutOrderBean orderBean = list.get(p);
        //订单可以续租，跳转续租界面
        ARouter.getInstance().build(AppConstants.PagePath.ORDER_RENEW)
                .withSerializable("bean",orderBean)
                .navigation();
    }

    //跳转维权
    public void doStartRights(int p) {
        OutOrderBean orderBean = list.get(p);
        ARouter.getInstance().build(AppConstants.PagePath.RIGHTS_APPLY)
                .withString("orderNo",orderBean.gameNo)
                .navigation();
    }

    //维权详情
    public void doStartRightsDetail(int p) {
        OutOrderBean orderBean = list.get(p);
        ARouter.getInstance().build(AppConstants.PagePath.RIGHTS_DETAIL)
                .withString("orderNo",orderBean.gameNo)
                .navigation();
    }

    //撤销维权
    public void doCxRights(int p) {
        OutOrderBean orderBean = list.get(p);
        new AlertDialog.Builder(mView.getContext())
                .setMessage("确定要撤销本次维权吗？撤销后不可再次维权")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialog, which) -> overArbitration(orderBean.gameNo))
                .show();
    }

    //商品点击
    public void doAccClick(int p) {
        ARouter.getInstance().build(AppConstants.PagePath.ACC_DETAIL).withString("id",list.get(p).goodId).navigation();
    }

    //去支付
    public void doPayClick(int p) {
        OutOrderBean orderBean = list.get(p);
        ARouter.getInstance().build(AppConstants.PagePath.ORDER_PAY)
                .withSerializable("orderBean",orderBean)
                .navigation();
    }


    //获取订单信息
    private void findOrderList(final int tempPageNo) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("keyWords", tempSearch);//订单标记,1-查询所有订单(不包含维权订单),2-查询维权订单
        map.put("pageIndex", tempPageNo);
        map.put("pageSize", AppConfig.PAGE_SIZE);
        apiUserNewService.myLeaseList(userBeanHelp.getAuthorization(), map)
                .compose(RxSchedulers.io_main_business())
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<BaseData<OutOrderBean>>(mView) {
                    @Override
                    public void onSuccess(BaseData<OutOrderBean> orderBeanBaseData) {
                        if (tempPageNo == 1) {
                            list.clear();
                        }
                        if (mView == null) {
                            return;
                        }
                        list.addAll(orderBeanBaseData.data);
                        mView.notifyItemRangeChanged((tempPageNo - 1) * AppConfig.PAGE_SIZE, orderBeanBaseData.data.size());
                        if (orderBeanBaseData.data.size() == 0 && tempPageNo != 1) {
                            ToastUtils.showShort("没有更多数据");
                        } else {
                            pageNo = tempPageNo;
                        }
                        if (list.size() == 0) {
                          ToastUtils.showShort("没有相关订单");
                        }
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                        mView.notifyItemRangeChanged(-1, 0);
                    }
                });
    }

    //撤销维权
    private void overArbitration(String orderNo) {
        apiUserNewService.cancelOrderLeaseRight(userBeanHelp.getAuthorization(), orderNo)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<Boolean>(mView) {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if (aBoolean) {
                            ToastUtils.showShort("撤销维权成功");
                            RxBus.get().post(AppConstants.RxBusAction.TAG_ORDER_LIST, true);
                            mView.finish();
                        } else {
                            ToastUtils.showShort("撤销维权失败");
                        }
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }
}