package com.haohao.xubei.ui.module.order.presenter;

import android.content.ClipData;
import android.content.ClipboardManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.ApiPCService;
import com.haohao.xubei.data.network.service.ApiUserNewService;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.order.contract.OrderSuccessContract;
import com.haohao.xubei.ui.module.order.model.OutOrderBean;

import javax.inject.Inject;

import androidx.appcompat.app.AlertDialog;

/**
 * pc端游成功界面
 * date：2018/2/26 11:42
 * author：Seraph
 **/
public class OrderSuccessPresenter extends OrderSuccessContract.Presenter {


    private ApiPCService apiPCService;

    private ClipboardManager clipboardManager;

    private ApiUserNewService apiUserNewService;

    private UserBeanHelp userBeanHelp;

    private String orderNo;

    @Inject
    public OrderSuccessPresenter(ApiUserNewService apiUserNewService, UserBeanHelp userBeanHelp, ApiPCService apiPCService, String orderNo, ClipboardManager clipboardManager) {
        this.apiPCService = apiPCService;
        this.orderNo = orderNo;
        this.clipboardManager = clipboardManager;
        this.apiUserNewService = apiUserNewService;
        this.userBeanHelp = userBeanHelp;
    }

    private OutOrderBean mOutOrderBean;

    @Override
    public void start() {
        //请求订单成功之后相关的详情
        getOrderDetail();
    }


    //复制登录码
    public void doCopyLoginCode() {
        if (ObjectUtils.isNotEmpty(mOutOrderBean)) {
            clipboardManager.setPrimaryClip(ClipData.newPlainText(null, mOutOrderBean.gameNo));
            ToastUtils.showShort("复制登录码成功");
        }

    }

    //订单续租
    public void doRenewLease() {
        doCheckOrderIsPay();
    }


    //查看订单详情
    public void doOrderLook() {
        if (mOutOrderBean == null || mOutOrderBean.gameNo == null) {
            ToastUtils.showShort("查询订单详情失败");
            return;
        }
        ARouter.getInstance().build(AppConstants.PagePath.ORDER_DETAIL)
                .withString("orderNo",mOutOrderBean.gameNo)
                .navigation();
    }


    //复制下载链接
    public void doCopyLink() {
        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, AppConfig.PC_DOWNLOAD));
        ToastUtils.showShort("复制下载链接成功");
    }

    //查看上号步骤
    public void doStepLook() {
        ARouter.getInstance().build(AppConstants.PagePath.COMM_AGENTWEB)
                .withString("title", "查看上号步骤")
                .withString("webUrl", AppConstants.AgreementAction.DEVICE_STEPS)
                .navigation();
    }


    //复制账号
    public void doCopyAcc() {
        if (StringUtils.isEmpty(mOutOrderBean.outGoodsDetail.gameAccount)) {
            return;
        }
        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, mOutOrderBean.outGoodsDetail.gameAccount));
        ToastUtils.showShort("复制账号成功");
    }

    //复制密码
    public void doCopyPwd() {
        if (StringUtils.isEmpty(mOutOrderBean.outGoodsDetail.gamePwd)) {
            return;
        }
        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, mOutOrderBean.outGoodsDetail.gamePwd));
        ToastUtils.showShort("复制密码成功");
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

    //检查订单是否可以续租
    private void doCheckOrderIsPay() {
        //订单可以续租，跳转续租界面
        ARouter.getInstance().build(AppConstants.PagePath.ORDER_RENEW)
                .withSerializable("bean",mOutOrderBean)
                .navigation();
    }

    //pc上号器扫码登录
    private void onPcOrderLogin(String authStr) {
        if (mOutOrderBean.endTime != null)
            apiPCService.mobileLogin(authStr, mOutOrderBean.gameNo, TimeUtils.millis2String(mOutOrderBean.endTime))
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


    //获取订单详情
    private void getOrderDetail() {
        if (ObjectUtils.isEmpty(orderNo)) {
            ToastUtils.showShort("订单号异常");
            mView.finish();
            return;
        }
        apiUserNewService.getOrderDetail(userBeanHelp.getAuthorization(), orderNo)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<OutOrderBean>(mView) {

                    @Override
                    public void onSuccess(OutOrderBean orderBean) {
                        mOutOrderBean = orderBean;
                        if (mView == null) {
                            return;
                        }
                        mView.doShowOrder(mOutOrderBean);
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                        mView.finish();
                    }
                });
    }

}
