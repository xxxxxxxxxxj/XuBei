package com.xubei.shop.ui.module.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.TimeUtils;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActOrderSuccessBinding;
import com.xubei.shop.ui.module.base.ABaseActivity;
import com.xubei.shop.ui.module.order.contract.OrderSuccessContract;
import com.xubei.shop.ui.module.order.model.OutOrderBean;
import com.xubei.shop.ui.module.order.presenter.OrderSuccessPresenter;

import java.math.BigDecimal;
import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 订单支付成功
 * date：2018/2/26 10:31
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.ORDER_SUCCESS)
public class OrderSuccessActivity extends ABaseActivity<OrderSuccessContract.Presenter> implements OrderSuccessContract.View {

    private ActOrderSuccessBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_order_success);
        binding.itmePhone.setActivity(this);
        binding.itmePc.setActivity(this);
    }

    @Inject
    OrderSuccessPresenter presenter;

    @Override
    protected OrderSuccessContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("支付成功");
        presenter.start();
    }

    private final int SCAN_QC = 100;


    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_renew_lease://续租
                presenter.doRenewLease();
                break;
            case R.id.tv_copy://复制登录码
                presenter.doCopyLoginCode();
                break;
            case R.id.btn_order_look://查看订单详情
            case R.id.btn_order_look2://查看订单详情
                presenter.doOrderLook();
                break;
            case R.id.tv_scan://扫一扫
                ARouter.getInstance().build(AppConstants.PagePath.COMM_CAPTURE).navigation(this, SCAN_QC);
                break;
            case R.id.tv_step_look://查看上号步骤
                presenter.doStepLook();
                break;
            case R.id.tv_copy_link://复制下载链接
                presenter.doCopyLink();
                break;
            case R.id.tv_copy_acc://复制游戏账号
                presenter.doCopyAcc();
                break;
            case R.id.tv_copy_password://复制游戏密码
                presenter.doCopyPwd();
                break;
        }
    }

    @Override
    public void doShowOrder(OutOrderBean orderBean) {
        //判断是端游还是手游
        if ("0".equals(orderBean.outGoodsDetail.isPhone)) {
            //端游
            binding.vs.setDisplayedChild(0);
            //游戏名称
            binding.itmePc.tvGameName.setText(orderBean.gameAllName);
            //账号
            binding.itmePc.tvOrder.setText(orderBean.gameNo);
            //租赁开始时间
            binding.itmePc.tvTimeStart.setText(TimeUtils.millis2String(orderBean.beginTime));
            //租赁结束时间
            binding.itmePc.tvTimeEnd.setText(TimeUtils.millis2String(orderBean.endTime));
        } else {
            //手游
            binding.vs.setDisplayedChild(1);
            //合计
            binding.itmePhone.orderTitle.tvTotalAmount.setText(orderBean.orderAllAmount);
            double rent = BigDecimal.valueOf(Double.valueOf(orderBean.orderAllAmount)).subtract(BigDecimal.valueOf(Double.valueOf(orderBean.orderForegiftAmount))).doubleValue();
            //租金
            binding.itmePhone.orderTitle.tvRent.setText(String.format(Locale.getDefault(), "%.2f", rent));
            //押金
            binding.itmePhone.orderTitle.tvDeposit.setText(orderBean.orderForegiftAmount);
            //游戏名称
            binding.itmePhone.tvGameName.setText(orderBean.gameAllName);
            //账号
            binding.itmePhone.tvGameAcc.setText(orderBean.outGoodsDetail.gameAccount);
            //密码
            binding.itmePhone.tvGamePassword.setText(orderBean.outGoodsDetail.gamePwd);
            //租赁时间
            binding.itmePhone.tvTime.setText(String.format(Locale.getDefault(), "%s到%s", TimeUtils.millis2String(orderBean.beginTime), TimeUtils.millis2String(orderBean.endTime)));
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCAN_QC && resultCode == Activity.RESULT_OK) {
            int ResultType = data.getIntExtra(CodeUtils.RESULT_TYPE, -1);
            String resultStr = data.getStringExtra(CodeUtils.RESULT_STRING);
            //调用登录确认弹框
            presenter.onPCLogin(ResultType, resultStr);
        }
    }
}
