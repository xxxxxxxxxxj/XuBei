package com.xubei.shop.ui.module.order.presenter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.xubei.shop.AppConfig;
import com.xubei.shop.AppConstants;
import com.xubei.shop.data.db.help.UserBeanHelp;
import com.xubei.shop.data.network.rx.RxSchedulers;
import com.xubei.shop.data.network.service.Api8Service;
import com.xubei.shop.data.network.service.ApiUserNewService;
import com.xubei.shop.ui.module.account.model.OutGoodsDetailBean;
import com.xubei.shop.ui.module.base.ABaseSubscriber;
import com.xubei.shop.ui.module.order.contract.OrderRenewContract;
import com.xubei.shop.ui.module.order.model.OrderPayBean;
import com.xubei.shop.ui.module.order.model.OutOrderBean;
import com.xubei.shop.ui.module.user.model.AcctManageBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import javax.inject.Inject;

import okhttp3.RequestBody;

/**
 * 订单续租
 * date：2017/12/25 15:24
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class OrderRenewPresenter extends OrderRenewContract.Presenter {

    private Api8Service api8Service;

    private ApiUserNewService apiUserNewService;

    private UserBeanHelp userBeanHelp;

    private OutOrderBean orderBean;

    @Inject
    public OrderRenewPresenter(Api8Service api8Service, ApiUserNewService apiUserNewService, UserBeanHelp userBeanHelp, OutOrderBean orderBean) {
        this.api8Service = api8Service;
        this.apiUserNewService = apiUserNewService;
        this.userBeanHelp = userBeanHelp;
        this.orderBean = orderBean;
    }

    private OutGoodsDetailBean outGoodsDetailBean;

    private Double tempShowPayPrice;//显示的支付金额
    private Double tempAccountMoney;//账号可用金额

    //选择的数量
    private int count = 1;

    @Override
    public void start() {
        mView.setOrderInfo(orderBean);
        doGoodsDetail();
        doAccountMoney();
    }


    public void onUpdateNumber(int count) {
        this.count = count;
        if (outGoodsDetailBean == null) {
            ToastUtils.showShort("获取账号详情失败");
            mView.finish();
            return;
        }
        //计算价格
        calculatePrice();
    }

    //计算价格
    private void calculatePrice() {
        //续租只支持小时租
        String price = outGoodsDetailBean.hourLease; //单价
        BigDecimal priceBig;
        if (ObjectUtils.isNotEmpty(price)) {
            priceBig = new BigDecimal(price);
        } else {
            priceBig = new BigDecimal(0);
        }
        priceBig = priceBig.multiply(BigDecimal.valueOf(count));
        tempShowPayPrice = priceBig.doubleValue();
        mView.setAllPrice(tempShowPayPrice);
    }


    private String tempPassword = "";

    private String payPassword;

    //输入单个密码。一旦满足6位则调用支付接口
    public void doInputItemPassword(String inputStr) {
        if (tempPassword.length() >= 6) {
            return;
        }
        tempPassword = tempPassword + inputStr;
        //回填输入
        mView.onSetPasswordShow(tempPassword);
        //满足6个调用支付关闭输入，清理临时输入密码
        if (tempPassword.length() == 6) {
            payPassword = tempPassword;
            orderRelet();
            mView.onCloseInput();
        }
    }

    //清理临临时密码
    public void doCleanTempPassWord() {
        tempPassword = "";
    }

    //移除最后一位密码
    public void doDeleteInputItemPassword() {
        if (tempPassword.length() > 0) {
            tempPassword = tempPassword.substring(0, tempPassword.length() - 1);
        }
        mView.onSetPasswordShow(tempPassword);
    }


    //忘记支付密码
    public void doForgetPassword() {
        ARouter.getInstance().build(AppConstants.PagePath.LOGIN_RESETPAYPW).navigation();
    }

    //检查余额是否可以支付
    public void doPay() {
        if (tempShowPayPrice == null || tempAccountMoney == null) {
            ToastUtils.showShort("获取数据失败");
            mView.finish();
            return;
        }
        if (tempShowPayPrice > tempAccountMoney) {
            ToastUtils.showShort("余额不足，请先充值");
            return;
        }
        //提示输入支付密码
        checkPayPw();
    }

    //检查是否有支付密码
    private void checkPayPw() {
        apiUserNewService.acctManageIndex(userBeanHelp.getAuthorization())
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<AcctManageBean>(mView) {
                    @Override
                    public void onSuccess(AcctManageBean acctManageBean) {
                        if (acctManageBean.isPayPwd) {
                            //提示输入支付密码
                            mView.onShowPayPw();
                        } else {
                            ToastUtils.showShort("请先设置支付密码");
                            ARouter.getInstance().build(AppConstants.PagePath.LOGIN_RESETPAYPW).navigation();
                        }
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });

    }

    //获取详情
    private void doGoodsDetail() {
        api8Service.goodsDetail(orderBean.goodId, AppConfig.getChannelValue())
                .compose(RxSchedulers.io_main_business())
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<OutGoodsDetailBean>(mView) {
                    @Override
                    public void onSuccess(OutGoodsDetailBean detailBean) {
                        outGoodsDetailBean = detailBean;
                        if (outGoodsDetailBean != null) {
                            //设置默认的
                            mView.setMinNumber(count);
                            calculatePrice();
                        }
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });

    }


    //获取账号余额信息
    public void doAccountMoney() {
        apiUserNewService.acctManageIndex(userBeanHelp.getAuthorization())
                .compose(RxSchedulers.io_main_business())
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<AcctManageBean>(mView) {
                    @Override
                    public void onSuccess(AcctManageBean acctManageBean) {
                        tempAccountMoney = acctManageBean.aviableAmt;
                        mView.setAccountMoney(tempAccountMoney);
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }


    //续租
    private void orderRelet() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("businessNo", AppConfig.getChannelValue());
            jsonObject.put("orderGameNo", orderBean.gameNo);
            jsonObject.put("count", count);
        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtils.showShort("创建续租订单异常");
            return;
        }
        RequestBody jsonBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        apiUserNewService.reletPay(userBeanHelp.getAuthorization(), jsonBody)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<OrderPayBean>() {
                    @Override
                    public void onSuccess(OrderPayBean orderPayBean) {
                        //如果是自己租自己则不调用下支付
                        if (orderBean.buyerId.equals(orderBean.sellerId)) {
                            mView.hideLoading();
                            ToastUtils.showShort("订单续租成功");
                            RxBus.get().post(AppConstants.RxBusAction.TAG_ORDER_DETAIL, true);
                            RxBus.get().post(AppConstants.RxBusAction.TAG_ORDER_LIST, true);
                            mView.finish();
                            return;
                        }
                        //调用对应的支付
                        payLaunchExt(orderPayBean.balanceNo);
                    }

                    @Override
                    public void onError(String errStr) {
                        mView.hideLoading();
                        ToastUtils.showShort(errStr);
                    }
                });

    }


    //支付
    private void payLaunchExt(String balanceNo) {
        apiUserNewService.payLaunchExt(userBeanHelp.getAuthorization(), balanceNo, "yue", "lease", payPassword)
                .compose(RxSchedulers.io_main_business())
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String result) {
                        ToastUtils.showShort("订单续租成功");
                        RxBus.get().post(AppConstants.RxBusAction.TAG_ORDER_DETAIL, true);
                        RxBus.get().post(AppConstants.RxBusAction.TAG_ORDER_LIST, true);
                        mView.finish();
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });

    }


}
