package com.xubei.shop.ui.module.order.presenter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.xubei.shop.AppConfig;
import com.xubei.shop.AppConstants;
import com.xubei.shop.data.db.help.UserBeanHelp;
import com.xubei.shop.data.network.rx.RxSchedulers;
import com.xubei.shop.data.network.service.ApiUserNewService;
import com.xubei.shop.ui.module.base.ABaseSubscriber;
import com.xubei.shop.ui.module.base.BaseData;
import com.xubei.shop.ui.module.order.contract.OrderAllSellerContract;
import com.xubei.shop.ui.module.order.model.OutOrderBean;
import com.xubei.shop.ui.views.NoDataView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.appcompat.app.AlertDialog;
import io.reactivex.Flowable;

/**
 * 我发布的订单
 * date：2017/12/4 15:04
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class OrderAllSellerPresenter extends OrderAllSellerContract.Presenter {


    private UserBeanHelp userBeanHelp;

    private ApiUserNewService apiUserNewService;

    @Inject
    OrderAllSellerPresenter(UserBeanHelp userBeanHelp, ApiUserNewService apiUserNewService) {
        this.userBeanHelp = userBeanHelp;
        this.apiUserNewService = apiUserNewService;
    }

    //订单状态，0为未支付，1为支付中，2为支付成功，3为支付失败，4维权中，5退款成功，6位退款失败,7取消订单 ，100 交易完成   
    private Integer orderStatus = null;

    public void setPosition(int position) {
        //转换需要查询的订单状态
        switch (position) {
            case 1://已取消
                orderStatus = 7;
                break;
            case 2://待付款
                orderStatus = 0;
                break;
            case 3://租赁中
                orderStatus = 2;
                break;
            case 4://租赁完成
                orderStatus = 100;
                break;
            case 5://维权中
                orderStatus = 4;
                break;
            default://全部
                orderStatus = null;
                break;
        }

    }

    private List<OutOrderBean> list = new ArrayList<>();

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

    //下拉刷新
    public void doNextPage() {
        findOrderList(pageNo + 1);
    }

    //跳转订单详情
    public void doItemClick(int position) {
        if (list.size() <= position) {
            ToastUtils.showShort("查看订单详情失败，请刷新列表后重试");
            return;
        }
        ARouter.getInstance().build(AppConstants.PagePath.ORDER_R_DETAIL)
                .withString("orderNo", list.get(position).gameNo)
                .navigation();
    }

    //卖家申请维权
    public void doRightsApply(int position) {
        new AlertDialog.Builder(mView.getContext())
                .setTitle("维权提示")
                .setMessage("\t\t\t\t租方在租赁中享有一次撤销维权和一次维权达成的机会，维权达成后将不能再登录游戏。出租方在租赁中可发起非法操作维权，同时租赁结束后的48小时押金解冻期内享有一次维权机会。")
                .setPositiveButton("继续维权", (dialog, which) ->
                        ARouter.getInstance().build(AppConstants.PagePath.RIGHTS_APPLY)
                                .withString("orderNo", list.get(position).gameNo)
                                .navigation())
                .setNegativeButton("取消维权", null)
                .show();
    }

    //处理维权
    public void doRightsHandling(int position) {
        if (list.size() <= position) {
            ToastUtils.showShort("查看维权信息失败，请刷新列表后重试");
            return;
        }
        ARouter.getInstance().build(AppConstants.PagePath.RIGHTS_PROCESS)
                .withString("orderNo", list.get(position).gameNo)
                .navigation();
    }

    //查看维权记录
    public void doRightsLook(int position) {
        ARouter.getInstance().build(AppConstants.PagePath.RIGHTS_DETAIL)
                .withString("orderNo", list.get(position).gameNo)
                .navigation();
    }

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
        map.put("pageIndex", tempPageNo);
        map.put("pageSize", AppConfig.PAGE_SIZE);
        apiUserNewService.myRentList(userBeanHelp.getAuthorization(), map)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.setNoDataView(NoDataView.LOADING))
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