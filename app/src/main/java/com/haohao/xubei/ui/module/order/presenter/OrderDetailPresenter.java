package com.haohao.xubei.ui.module.order.presenter;

import android.content.ClipData;
import android.content.ClipboardManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.ApiPCService;
import com.haohao.xubei.data.network.service.ApiUserNewService;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.order.contract.OrderDetailContract;
import com.haohao.xubei.ui.module.order.model.OutOrderBean;
import com.haohao.xubei.ui.views.NoDataView;

import javax.inject.Inject;

import androidx.appcompat.app.AlertDialog;

/**
 * 订单支付
 * date：2017/12/4 15:04
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class OrderDetailPresenter extends OrderDetailContract.Presenter {

    private UserBeanHelp userBeanHelp;

    private ApiPCService apiPCService;

    private ClipboardManager clipboardManager;

    private ApiUserNewService apiUserNewService;

    private String orderNo;

    @Inject
    OrderDetailPresenter(ApiUserNewService apiUserNewService, UserBeanHelp userBeanHelp, ApiPCService apiPCService, ClipboardManager clipboardManager, String orderNo) {
        this.userBeanHelp = userBeanHelp;
        this.apiPCService = apiPCService;
        this.clipboardManager = clipboardManager;
        this.orderNo = orderNo;
        this.apiUserNewService = apiUserNewService;
    }

    private OutOrderBean mOrderBean;


    @Override
    public void start() {
        // AccessibilityOperator.getInstance().init(mView.getContext().getApplicationContext());
        doFindOrderItem();
    }


    //订单不同的状态进行不同的操作
//    public void doPay() {
//        switch (mOrderBean.orderStatus) {
//            case 0://支付
//                mView.onPayClick();
//                break;
//            case 2://续租
//                doCheckOrderIsPay();
//                break;
//        }
//    }

//    private SharedPreferences preferences = null;

//    //王者荣耀自动登录
//    public void doAutoLogin() {
//        //判断服务是否启动，如果没有启动则启动
//        if (AccessibilityOperator.getInstance().isServiceRunning()) {
//            if (preferences == null) {
//                preferences = mView.getContext().getApplicationContext().getSharedPreferences(AppConstants.SPAction.SP_NAME, Context.MODE_MULTI_PROCESS);
//            }
//            boolean isOK = preferences.edit().putString(AppConstants.SPAction.TEMP_USER_NAME, mOrderBean.game_account)
//                    .putString(AppConstants.SPAction.TEMP_PASSWORD, mOrderBean.game_pwd).commit();
//            if (!isOK) {
//                ToastUtils.showShort("启动失败");
//                return;
//            }
//            //2秒后启动游戏
//            Flowable.intervalRange(0, 1, 1, 1, TimeUnit.SECONDS).compose(RxSchedulers.io_main())
//                    .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
//                    .subscribe(aLong -> {
//                        mView.hideLoading();
//                        Intent intent = IntentUtils.getLaunchAppIntent("com.tencent.tmgp.sgame");
//                        if (intent == null) {
//                            ToastUtils.showShort("您尚未安装游戏，请先安装游戏");
//                            return;
//                        }
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        mView.getContext().startActivity(intent);
//                        mView.finish();
//                    });
//        } else {
//            new AlertDialog.Builder(mView.getContext()).setMessage("请在设置-辅助功能-虚贝选项，先打开虚贝辅助功能以确保能够正确的上号。在游戏打开后，点击与QQ好友玩。在添加账号界面会自动填入账号密码。")
//                    .setNegativeButton("确定", (dialog, which) -> OpenAccessibilitySettingHelper.jumpToSettingPage(mView.getContext()))
//                    .show();
//        }
//    }


    //复制订单到剪切板
    public void doCopyOrderNo() {
        if (mOrderBean == null || mOrderBean.orderStatus == null) {
            ToastUtils.showShort("获取订单状态失败");
            return;
        }
        if (mOrderBean.orderStatus != 2) {
            ToastUtils.showShort("您暂未租赁此账号");
            return;
        }
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(ClipData.newPlainText(null, orderNo));
            ToastUtils.showShort("复制成功");
        }
    }

    //复制用户名
    public void doCopyUser() {
        if (mOrderBean == null || mOrderBean.orderStatus == null) {
            ToastUtils.showShort("获取订单状态失败");
            return;
        }
        if (mOrderBean.orderStatus != 2) {
            ToastUtils.showShort("您暂未租赁此账号");
            return;
        }
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(ClipData.newPlainText(null, mOrderBean.outGoodsDetail.gameAccount));
            ToastUtils.showShort("游戏账号复制成功");
        }
    }

    //复制密码
    public void doCopyParssword() {
        if (mOrderBean == null || mOrderBean.orderStatus == null) {
            ToastUtils.showShort("获取订单状态失败");
            return;
        }
        if (mOrderBean.orderStatus != 2) {
            ToastUtils.showShort("您暂未租赁此账号");
            return;
        }
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(ClipData.newPlainText(null, mOrderBean.outGoodsDetail.gamePwd));
            ToastUtils.showShort("游戏密码复制成功");
        }
    }


    //去支付
    public void doPayClick() {
        ARouter.getInstance().build(AppConstants.PagePath.ORDER_PAY)
                .withSerializable("orderBean",mOrderBean)
                .navigation();
    }

    //撤销维权
    public void doCxRights() {
        new AlertDialog.Builder(mView.getContext())
                .setMessage("确定要撤销本次维权吗？撤销后不可再次维权")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialog, which) -> overArbitration())
                .show();
    }

    //商品点击
    public void doAccClick() {
        ARouter.getInstance().build(AppConstants.PagePath.ACC_DETAIL).withString("id",mOrderBean.outGoodsDetail.id).navigation();
    }

    //检查订单是否可以续租
    public void doOrderRenew() {
        //订单可以续租，跳转续租界面
        ARouter.getInstance().build(AppConstants.PagePath.ORDER_RENEW)
                .withSerializable("bean",mOrderBean)
                .navigation();
    }

    //跳转维权
    public void doStartRights() {
        ARouter.getInstance().build(AppConstants.PagePath.RIGHTS_APPLY)
                .withString("orderNo",mOrderBean.gameNo)
                .navigation();
    }

    //维权详情
    public void doStartRightsDetail() {
        ARouter.getInstance().build(AppConstants.PagePath.RIGHTS_DETAIL)
                .withString("orderNo",mOrderBean.gameNo)
                .navigation();
    }

    //pc的上号器登录
    public void onPCLogin(int resultType, final String resultStr) {
        //判断是否上号器的二维码
        if (resultStr.contains(AppConfig.SCAN_QC_PC_STR)) {
            new AlertDialog.Builder(mView.getContext())
                    .setTitle("上号器登录")
                    .setMessage("是否确认登录？")
                    .setPositiveButton("确认", (dialog, which) -> {
                        String SCAN_QC_PC_STR = resultStr.replace(AppConfig.SCAN_QC_PC_STR, "");
                        onPcOrderLogin(SCAN_QC_PC_STR);
                    })
                    .setNegativeButton("取消", null).show();

        } else {
            ToastUtils.showShort("请扫描正确的二维码");
        }
    }


    //获取订单详情
    private void doFindOrderItem() {
        apiUserNewService.getOrderDetail(userBeanHelp.getAuthorization(), orderNo)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.setNoDataView(NoDataView.LOADING))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<OutOrderBean>(mView) {

                    @Override
                    public void onSuccess(OutOrderBean orderBean) {
                        mOrderBean = orderBean;
                        if (mView == null) {
                            return;
                        }
                        mView.setOrderBean(mOrderBean);
                        mView.setNoDataView(NoDataView.LOADING_OK);
                    }

                    @Override
                    public void onError(String errStr) {
                        mView.setNoDataView(NoDataView.NET_ERR);
                        ToastUtils.showShort(errStr);
                    }
                });
    }


    //撤销维权
    private void overArbitration() {
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

    //pc上号器扫码登录
    private void onPcOrderLogin(String authStr) {
        apiPCService.mobileLogin(authStr, mOrderBean.gameNo, TimeUtils.millis2String(mOrderBean.endTime))
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String s) {
                        ToastUtils.showShort("请求登录成功");
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }


}
