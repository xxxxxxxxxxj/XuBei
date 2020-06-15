package com.haohao.xubei.ui.module.user.presenter;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.ApiUserNewService;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.order.model.SelectOrderPayBean;
import com.haohao.xubei.ui.module.user.UserPayActivity;
import com.haohao.xubei.ui.module.user.contract.UserPayContract;
import com.haohao.xubei.ui.module.user.model.RechargeBean;
import com.haohao.xubei.utlis.Tools;
import com.haohao.xubei.utlis.alipay.PayResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * 账户充值支付
 * date：2017/12/4 15:04
 * author：Seraph
 **/
public class UserPayPresenter extends UserPayContract.Presenter {

    private UserBeanHelp userBeanHelp;

    private ApiUserNewService apiUserNewService;

    public static final String WECHAT_APP = "wechat_app";

    public static final String ALIPAY_APP = "alipay_app";

    private String payType;

    @Inject
    public UserPayPresenter(UserBeanHelp userBeanHelp, ApiUserNewService apiUserNewService) {
        this.userBeanHelp = userBeanHelp;
        this.apiUserNewService = apiUserNewService;
    }

    //金额选择列表
    private ArrayList<String> amountList = new ArrayList<>(Arrays.asList("5", "50", "100", "300", "500", "1000"));

    private RechargeBean mRechargeBean;


    @Override
    public void start() {
        //初始化选择金额
        mView.initView(amountList);
        //默认选择第一个
        doPayType(ALIPAY_APP);
    }

    //选择支付方式
    public void doPayType(String payType) {
        this.payType = payType;
        mView.selectPayType(payType);
    }


    //选择金额支付
    public void onStartSelectPay(int selectAmount) {
        onStartPay(amountList.get(selectAmount));
    }

    //直接输入金额支付
    public void onStartPay(String input) {
        if (!Tools.isNum(input)) {
            ToastUtils.showShort("请输入正确的充值金额");
            return;
        }
        if (Float.valueOf(input) < 5) {
            ToastUtils.showShort("充值金额不能少于5元");
            return;
        }
        recharge(input, payType);
    }


    private static final int SDK_PAY_FLAG = 1;

    //支付
    private void recharge(String amount, String payMode) {
        apiUserNewService.recharge(userBeanHelp.getAuthorization(), amount, payMode)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<RechargeBean>(mView) {
                    @Override
                    public void onSuccess(RechargeBean rechargeBean) {
                        mRechargeBean = rechargeBean;
                        switch (payType) {
                            case UserPayPresenter.WECHAT_APP:
                                //转换微信支付需要的参数
                                Tools.weChatPay(mView.getContext(), rechargeBean.payCode);
                                break;
                            case UserPayPresenter.ALIPAY_APP:
                                //支付宝
                                alipay(rechargeBean.payCode);
                                break;
                        }
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }

    //支付宝支付宝
    private void alipay(final String orderInfo) {
        Runnable payRunnable = () -> {
            PayTask alipay = new PayTask((UserPayActivity) mView.getContext());
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

    private Handler mHandler = new Handler(message -> {
        switch (message.what) {
            case SDK_PAY_FLAG: {
                @SuppressWarnings("unchecked")
                PayResult payResult = new PayResult((Map<String, String>) message.obj);
                    /*
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                //     String resultInfo = payResult.getResult();// 同步返回需要验证的信息
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


    public void wxPay() {
        //微信支付成功
        mView.showLoading("验证支付结果中");
        cont = 120;
        getTopupDetail();
    }

    //倒计时验证次数
    private int cont = 120;

    private Disposable disposable;

    //验证充值是否成功
    private void getTopupDetail() {
        //延时1秒查询结果
        if (mRechargeBean == null) {
            return;
        }
        cont--;
        LogUtils.i("cont：" + cont);
        disposable = Observable.intervalRange(0, 1, 1, 1, TimeUnit.SECONDS)
                .compose(RxSchedulers.io_main2())
                .as(mView.bindLifecycle())
                .subscribe(aLong -> apiUserNewService.getTopupDetail(userBeanHelp.getAuthorization(), mRechargeBean.rechargeNo)
                        .compose(RxSchedulers.io_main_business())
                        .as(mView.bindLifecycle())
                        .subscribe(new ABaseSubscriber<SelectOrderPayBean>() {
                            @Override
                            public void onSuccess(SelectOrderPayBean selectOrderPayBean) {
                                if (disposable != null && !disposable.isDisposed()) {
                                    disposable.dispose();
                                }
                                if (selectOrderPayBean.orderStatus == 4) {
                                    mView.hideLoading();
                                    ToastUtils.showShort("恭喜支付成功！");
                                    RxBus.get().post(AppConstants.RxBusAction.TAG_ACCOUNT_UPDATA, true);
                                    mView.finish();
                                } else if (cont <= 0) {
                                    mView.hideLoading();
                                    //验证失败
                                    ToastUtils.showShort("验证结果失败，请稍后查看！");
                                    mView.finish();
                                } else {//继续验证
                                    getTopupDetail();
                                }
                            }

                            @Override
                            public void onError(String errStr) {
                                getTopupDetail();
                            }
                        })
                );
    }


}
