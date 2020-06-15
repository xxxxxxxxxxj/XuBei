package com.haohao.xubei.ui.module.order.presenter;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.ApiUserNewService;
import com.haohao.xubei.ui.module.account.model.OutGoodsDetailBean;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.order.OrderCreateActivity;
import com.haohao.xubei.ui.module.order.contract.OrderCreateContract;
import com.haohao.xubei.ui.module.order.model.OrderPayBean;
import com.haohao.xubei.ui.module.order.model.SelectOrderPayBean;
import com.haohao.xubei.ui.module.user.model.AcctManageBean;
import com.haohao.xubei.ui.views.NoDataView;
import com.haohao.xubei.utlis.Tools;
import com.haohao.xubei.utlis.alipay.PayResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;


/**
 * 创建订单
 * date：2017/12/4 15:44
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class OrderCreatePresenter extends OrderCreateContract.Presenter {

    private ApiUserNewService apiUserNewService;

    private UserBeanHelp userBeanHelp;

    //商品详情
    private OutGoodsDetailBean detailBean;

    @Inject
    OrderCreatePresenter(ApiUserNewService apiUserNewService, UserBeanHelp userBeanHelp, OutGoodsDetailBean detailBean) {
        this.apiUserNewService = apiUserNewService;
        this.userBeanHelp = userBeanHelp;
        this.detailBean = detailBean;
    }

    //0-按小时租，1-按天租，2-按周租，3-通宵畅玩
    private int timeType;   //类型
    //在小时的时候有用
    private int count;  //数量

    //1余额，2微信，3支付宝
    private int payType = 1; //支付方式

    private Double tempShowPayPrice;//显示的支付金额

    private Double tempAccountMoney;//账号可用金额

    public void start() {
        //显示详情
        mView.setUIShow(detailBean);
        //转换和计算需要展示的内容
        showPayMoney();
        //查询账户余额
        doAccountMoney();
        //默认选择时租
        doSelectTimeType(0);
    }

    //选择租赁方式的时间类型
    public void doSelectTimeType(int timeType) {
        this.timeType = timeType;
        mView.selectTimeType(timeType);
        showPayMoney();
    }

    //选择租赁支付的方式
    public void doSelectPayType(int payType) {
        this.payType = payType;
        mView.selectPayType(payType);
        showPayMoney();
    }

    //显示到界面
    private void showPayMoney() {
        Double tempShowRent = calculatePrice();//租金
        tempShowPayPrice = getAllPayPrice(tempShowRent);//需要支付的金额
        //显示需要支付的金额
        mView.setPayMoney(tempShowRent, tempShowPayPrice);
    }

    //时间加减（时租有效）
    public void doTimeUpdata(int count) {
        this.count = count;
        //当前押金
        showPayMoney();
    }

    //支付失败
    public void payClose() {
        if (mView == null) {
            return;
        }
        mView.onCloseInput();
        if (mOrderBean != null) {
            ARouter.getInstance().build(AppConstants.PagePath.ORDER_ALL).navigation();
            mView.finish();
        }
    }

    //支付成功
    private void paySuccess() {
        ARouter.getInstance().build(AppConstants.PagePath.ORDER_SUCCESS)
                .withString("orderNo", mOrderBean.orderGameNo)
                .navigation();
        mView.finish();
    }


    //计算全部价格
    private Double getAllPayPrice(Double rentPrice) {
        return BigDecimal.valueOf(rentPrice).add(BigDecimal.valueOf(Double.valueOf(detailBean.foregift))).doubleValue();
    }

    //计算价格
    private Double calculatePrice() {
        if (timeType == -1) {
            return 0.00d;
        }
        String price = null; //单价
        switch (timeType) {
            case 0://小时
                price = detailBean.hourLease;
                break;
            case 1://天
                price = detailBean.dayLease;
                break;
            case 3://优惠租
                price = detailBean.allNightPlay;
                break;
            case 2://周
                price = detailBean.weekLease;
                break;
        }
        BigDecimal priceBig;
        if (ObjectUtils.isNotEmpty(price)) {
            priceBig = new BigDecimal(price);
        } else {
            priceBig = new BigDecimal(0d);
        }
        //如果是时租，则启用数量
        if (timeType == 0) {
            priceBig = priceBig.multiply(BigDecimal.valueOf(count));
        }
        return priceBig.doubleValue();
    }

    //临时可修改的密码
    private String tempPassword = "";

    //支付使用的密码
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
        }
    }

    //清理临时密码
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

    //开始支付流程
    public void doPay() {
        if (tempShowPayPrice == null || tempAccountMoney == null) {
            ToastUtils.showShort("网络异常");
            mView.finish();
            return;
        }
        //如果是余额支付，先弹出密码输入框
        if (payType == 1) {
            if (tempShowPayPrice > tempAccountMoney) {
                ToastUtils.showShort("余额不足，请先充值");
                return;
            }
            //检查是否有支付密码
            checkPayPw();
        } else {
            createOrder();
        }

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
                .doOnSubscribe(subscription -> mView.setNoDataView(NoDataView.LOADING))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<AcctManageBean>(mView) {
                    @Override
                    public void onSuccess(AcctManageBean acctManageBean) {
                        if (acctManageBean == null || acctManageBean.aviableAmt == null) {
                            ToastUtils.showShort("获取余额失败");
                            mView.setNoDataView(NoDataView.NET_ERR);
                            return;
                        }
                        tempAccountMoney = acctManageBean.aviableAmt;
                        mView.setAccountMoney(tempAccountMoney);
                        mView.setNoDataView(NoDataView.LOADING_OK);
                    }

                    @Override
                    public void onError(String errStr) {
                        mView.setNoDataView(NoDataView.NET_ERR);
                        ToastUtils.showShort(errStr);
                    }
                });
    }

    private OrderPayBean mOrderBean;

    //创建订单
    private void createOrder() {
        //如果结算bean不存在或者选择的数量不一致，则下单
        if (mOrderBean != null) {
            startPay();
        } else {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("businessNo", AppConfig.getChannelValue());
                jsonObject.put("goodsId", detailBean.id);
                jsonObject.put("count", timeType == 0 ? count : 1);
                jsonObject.put("leaseType", timeType);
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtils.showShort("创建订单异常");
                return;
            }
            RequestBody jsonBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
            apiUserNewService.createOrder(userBeanHelp.getAuthorization(), jsonBody)
                    .compose(RxSchedulers.io_main_business())
                    .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                    .as(mView.bindLifecycle())
                    .subscribe(new ABaseSubscriber<OrderPayBean>() {
                        @Override
                        public void onSuccess(OrderPayBean orderBean) {
                            //调用对应的支付
                            if (tempShowPayPrice == null || tempAccountMoney == null) {
                                ToastUtils.showShort("网络异常");
                                return;
                            }
                            mOrderBean = orderBean;
                            startPay();
                        }

                        @Override
                        public void onError(String errStr) {
                            mView.hideLoading();
                            ToastUtils.showShort(errStr);
                        }
                    });
        }

    }


    //进行支付
    private void startPay() {
        switch (payType) {
            case 1://余额
                payLaunchExt(mOrderBean.orderBalanceNo, "yue");
                break;
            case 2://微信
                payLaunchExt(mOrderBean.orderBalanceNo, "wechat_app");
                break;
            case 3://支付宝
                payLaunchExt(mOrderBean.orderBalanceNo, "alipay_app");
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
                                //关闭输入框
                                mView.onCloseInput();
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


    private static final int SDK_PAY_FLAG = 1;


    //支付宝支付宝
    private void alipay(final String orderInfo) {
        Runnable payRunnable = () -> {
            PayTask alipay = new PayTask((OrderCreateActivity) mView.getContext());
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
                    //微信支付成功
                    mView.showLoading("验证支付结果中");
                    cont = 120;
                    getTopupDetail();
                } else {
                    ToastUtils.showShort("支付失败");
                    payClose();
                }
                break;
            }
            default:
                break;
        }
        return false;
    });


    //微信支付结果
    public void doWxPay(boolean aBoolean) {
        if (aBoolean) {
            //微信支付成功
            mView.showLoading("验证支付结果中");
            cont = 120;
            getTopupDetail();
        } else {
            payClose();
        }
    }


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
                .subscribe(aLong -> apiUserNewService.getOrderBalance(userBeanHelp.getAuthorization(), mOrderBean.orderBalanceNo)
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
                                    mView.hideLoading();
                                    //验证失败
                                    ToastUtils.showShort("验证结果失败，请稍后查看！");
                                    payClose();
                                } else {//继续验证
                                    getTopupDetail();
                                }
                            }

                            @Override
                            public void onError(String errStr) {
                                if (cont <= 0) {
                                    mView.hideLoading();
                                    //验证失败
                                    ToastUtils.showShort("验证结果失败，请稍后查看！");
                                    payClose();
                                } else {
                                    getTopupDetail();
                                }
                            }
                        })
                );
    }

}
