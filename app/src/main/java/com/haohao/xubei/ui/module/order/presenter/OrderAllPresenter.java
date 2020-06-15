package com.haohao.xubei.ui.module.order.presenter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.ApiUserNewService;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.base.BaseData;
import com.haohao.xubei.ui.module.order.contract.OrderAllContract;
import com.haohao.xubei.ui.module.order.model.OutOrderBean;
import com.haohao.xubei.ui.views.NoDataView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.appcompat.app.AlertDialog;
import io.reactivex.Flowable;

/**
 * 全部订单
 * date：2017/12/4 15:04
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class OrderAllPresenter extends OrderAllContract.Presenter {

    private UserBeanHelp userBeanHelp;

    private ApiUserNewService apiUserNewService;

    @Inject
    OrderAllPresenter(UserBeanHelp userBeanHelp, ApiUserNewService apiUserNewService) {
        this.userBeanHelp = userBeanHelp;
        this.apiUserNewService = apiUserNewService;
    }

    private List<OutOrderBean> list = new ArrayList<>();

    //订单类型
    private Integer orderStatus = null;

    //订单标记(1-查询所有订单不包含维权订单,2-查询维权订单)
    private Integer orderFlag = null;

    public void setPosition(int position) {
        //转换需要查询的订单状态
        switch (position) {
            case 2://待付款
                orderStatus = 0;
                orderFlag = null;
                break;
            case 1://租赁中（支付成功）
                orderStatus = 2;
                orderFlag = null;
                break;
            case 4://维权中
                orderStatus = null;
                orderFlag = 1;
                break;
            case 3://交易完成
                orderStatus = 100;
                orderFlag = null;
                break;
            default:
                orderStatus = null;
                orderFlag = null;
                break;
        }

    }


    @Override
    public void start() {
        mView.initInflateView(list);
        Flowable.intervalRange(0, 1, 500, 500, TimeUnit.MILLISECONDS)
                .compose(RxSchedulers.io_main())
                .as(mView.bindLifecycle())
                .subscribe(aLong -> doRefresh());
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

    // private TimeSelectBean timeSelectBean;

    //时间赛选
//    public void doTimeSelect(TimeSelectBean bean) {
//        timeSelectBean = bean;
//        mView.autoRefresh();
//    }

    //页码
    private int pageNo = 0;


    //获取订单信息
    private void findOrderList(final int tempPageNo) {
        if (!userBeanHelp.isLogin(true)) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        if (orderStatus != null) {
            map.put("orderStatus", orderStatus);
        }
        if (orderFlag != null) {
            map.put("arbing", orderFlag);//订单标记,1-查询所有订单(不包含维权订单),2-查询维权订单
        }
        map.put("pageIndex", tempPageNo);
        map.put("pageSize", AppConfig.PAGE_SIZE);
        apiUserNewService.myLeaseList(userBeanHelp.getAuthorization(), map)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.setNoDataView(NoDataView.LOADING))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<BaseData<OutOrderBean>>(mView) {
                    @Override
                    public void onSuccess(BaseData<OutOrderBean> orderBeanBaseData) {
                        if (mView == null || list == null) {
                            return;
                        }
                        if (tempPageNo == 1) {
                            list.clear();
                        }
                        list.addAll(orderBeanBaseData.data);
                        mView.notifyItemRangeChanged((tempPageNo - 1) * AppConfig.PAGE_SIZE, orderBeanBaseData.data.size());
                        if (orderBeanBaseData.data.size() == 0 && tempPageNo != 1) {
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


    //检查订单是否可以续租
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
                            mView.autoRefresh();
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
