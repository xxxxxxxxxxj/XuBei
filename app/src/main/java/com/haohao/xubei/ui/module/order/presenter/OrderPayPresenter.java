package com.haohao.xubei.ui.module.order.presenter;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.ApiUserNewService;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.order.OrderPayActivity;
import com.haohao.xubei.ui.module.order.contract.OrderPayContract;
import com.haohao.xubei.ui.module.order.model.OutOrderBean;
import com.haohao.xubei.ui.module.order.model.SelectOrderPayBean;
import com.haohao.xubei.ui.module.user.model.AcctManageBean;
import com.haohao.xubei.utlis.Tools;
import com.haohao.xubei.utlis.alipay.PayResult;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * 订单支付选择逻辑
 * date：2018/3/15 15:36
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class OrderPayPresenter extends OrderPayContract.Presenter {


    private UserBeanHelp userBeanHelp;

    private ApiUserNewService apiUserNewService;

    private OutOrderBean mOrderBean;

    @Inject
    OrderPayPresenter(UserBeanHelp userBeanHelp, ApiUserNewService apiUserNewService, OutOrderBean orderBean) {
        this.userBeanHelp = userBeanHelp;
        this.apiUserNewService = apiUserNewService;
        this.mOrderBean = orderBean;
        if (orderBean == null) {
            ToastUtils.showShort("订单数据异常");
            mView.finish();
        }
    }

    private Double tempAccountMoney;//账号可用金额

    //1余额，2微信，3支付宝
    private int payType = 1; //支付方式

    @Override
    public void start() {
        mView.setUIShow(mOrderBean);
        //查询账户余额
        doAccountMoney();
    }


    //选择租赁支付的方式
    public void doSelectPayType(int payType) {
        this.payType = payType;
        mView.selectPayType(payType);
    }

    //临时密码
    private String tempPassword = "";

    //支付密码
    private String payPassword = "";

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
            //输入密码后再创建订单
            payPassword = tempPassword;
            createOrder();
            //关闭输入框
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


    public void doPay() {
        if (mOrderBean.orderAllAmount == null || tempAccountMoney == null) {
            ToastUtils.showShort("网络异常");
            mView.finish();
            return;
        }
        //如果是余额支付，先弹出密码输入框
        if (payType == 1) {
            if (Double.valueOf(mOrderBean.orderAllAmount) > tempAccountMoney) {
                ToastUtils.showShort("余额不足，请先充值");
                return;
            }
            //检查是否有支付密码
            checkPayPw();
        } else {
            //获取结算单号
            createOrder();
        }

    }

    //支付失败
    private void payClose() {
        mView.hideLoading();
        ToastUtils.showShort("验证结果失败，请稍后查看！");
        RxBus.get().post(AppConstants.RxBusAction.TAG_ACCOUNT_UPDATA, true);
        mView.finish();
    }

    //支付成功
    private void paySuccess() {
        RxBus.get().post(AppConstants.RxBusAction.TAG_ACCOUNT_UPDATA, true);
        ARouter.getInstance().build(AppConstants.PagePath.ORDER_SUCCESS)
                .withString("orderNo", mOrderBean.gameNo)
                .navigation();
        mView.finish();
    }


    private String orderBalanceNo;

    //获取结算单号
    private void createOrder() {
        //如果结算点号存在
        if (ObjectUtils.isNotEmpty(orderBalanceNo)) {
            startPay();
        } else {
            //开始获取结算单号
            apiUserNewService.getOrderBalanceNo(userBeanHelp.getAuthorization(), mOrderBean.gameNo)
                    .compose(RxSchedulers.io_main_business())
                    .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                    .as(mView.bindLifecycle())
                    .subscribe(new ABaseSubscriber<String>() {
                        @Override
                        public void onSuccess(String s) {
                            orderBalanceNo = s;
                            startPay();
                        }

                        @Override
                        public void onError(String errStr) {
                            ToastUtils.showShort(errStr);
                        }
                    });
        }
    }


    //进行支付
    private void startPay() {
        switch (payType) {
            case 1://余额
                payLaunchExt(orderBalanceNo, "yue");
                break;
            case 2://微信
                payLaunchExt(orderBalanceNo, "wechat_app");
                break;
            case 3://支付宝
                payLaunchExt(orderBalanceNo, "alipay_app");
                break;
        }
    }


    //支付
    private void payLaunchExt(String balanceNo, String payMode) {
        apiUserNewService.payLaunchExt(userBeanHelp.getAuthorization(), balanceNo, payMode, "lease", payPassword)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>() {
                    @Override
                    public void onSuccess(String result) {
                        switch (payType) {
                            case 1://余额
                                paySuccess();
                                break;
                            case 2://微信支付
                                mView.hideLoading();
                                Tools.weChatPay(mView.getContext(), result);
                                break;
                            case 3://支付宝
                                mView.hideLoading();
                                alipay(result);
                                break;
                        }
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                        mView.hideLoading();
                    }
                });

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

    //获取账号余额信息
    public void doAccountMoney() {
        apiUserNewService.acctManageIndex(userBeanHelp.getAuthorization())
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
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
                        mView.finish();
                    }
                });
    }

    //微信支付结果
    public void doWxPay(boolean aBoolean) {
        if (aBoolean) {
            //微信支付成功
            mView.showLoading("验证支付结果中");
            cont = 120;
            getTopupDetail();
        }
    }

    private static final int SDK_PAY_FLAG = 1;


    //支付宝支付宝
    private void alipay(final String orderInfo) {
        Runnable payRunnable = () -> {
            PayTask alipay = new PayTask((OrderPayActivity) mView.getContext());
            Map<String, String> result = alipay.payV2(orderInfo, true);
            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    //支付宝的同步结果回调
    private Handler mHandler = new Handler(message -> {
        switch (message.what) {
            case SDK_PAY_FLAG: {
                @SuppressWarnings("unchecked")
                PayResult payResult = new PayResult((Map<String, String>) message.obj);
                    /*
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                // String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    mView.showLoading("验证支付结果中");
                    cont = 120;
                    getTopupDetail();
                } else {
                    ToastUtils.showShort("支付失败");
                }
                break;
            }
            default:
                break;
        }
        return false;
    });

    //倒计时验证次数
    private int cont = 120;

    private Disposable disposable;

    //验证充值是否成功
    private void getTopupDetail() {
        //延时1秒查询结果
        if (mOrderBean == null) {
            return;
        }
        cont--;
        LogUtils.i("cont：" + cont);
        disposable = Observable.intervalRange(0, 1, 1, 1, TimeUnit.SECONDS)
                .compose(RxSchedulers.io_main2())
                .as(mView.bindLifecycle())
                .subscribe(aLong -> apiUserNewService.getOrderBalance(userBeanHelp.getAuthorization(), orderBalanceNo)
                        .compose(RxSchedulers.io_main_business())
                        .as(mView.bindLifecycle())
                        .subscribe(new ABaseSubscriber<SelectOrderPayBean>() {
                            @Override
                            public void onSuccess(SelectOrderPayBean selectOrderPayBean) {
                                if (disposable != null && !disposable.isDisposed()) {
                                    disposable.dispose();
                                }
                                if (selectOrderPayBean.gameOrderStatus == 2) {
                                    mView.hideLoading();
                                    paySuccess();
                                } else if (cont <= 0) {
                                    payClose();
                                } else {//继续验证
                                    getTopupDetail();
                                }
                            }

                            @Override
                            public void onError(String errStr) {
                                if (cont <= 0) {
                                    payClose();
                                } else {
                                    getTopupDetail();
                                }
                            }
                        })
                );
    }


}
