package com.xubei.shop.ui.module.order;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActOrderPayBinding;
import com.xubei.shop.databinding.CommonDialogInputSixPasswordBinding;
import com.xubei.shop.ui.module.base.ABaseActivity;
import com.xubei.shop.ui.module.order.contract.OrderPayContract;
import com.xubei.shop.ui.module.order.model.OutOrderBean;
import com.xubei.shop.ui.module.order.presenter.OrderPayPresenter;
import com.xubei.shop.utlis.Tools;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import java.math.BigDecimal;
import java.util.Locale;

import javax.inject.Inject;

/**
 * 订单选择支付
 * date：2018/3/15 15:32
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.ORDER_PAY)
public class OrderPayActivity extends ABaseActivity<OrderPayPresenter> implements OrderPayContract.View {

    private ActOrderPayBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_order_pay);
        binding.setActivity(this);
    }

    @Inject
    OrderPayPresenter presenter;

    @Override
    protected OrderPayPresenter getMVPPresenter() {
        return presenter;
    }

    @Inject
    LayoutInflater layoutInflater;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("订单支付");
        presenter.start();
    }


    @Override
    public void setUIShow(OutOrderBean orderBean) {
        //租金
        double rent = BigDecimal.valueOf(Double.valueOf(orderBean.orderAllAmount)).subtract(BigDecimal.valueOf(Double.valueOf(orderBean.orderForegiftAmount))).doubleValue();
        binding.tvRent.setText(String.format(Locale.getDefault(), "¥ %,.2f", rent));
        //押金
        binding.tvDeposit.setText(String.format(Locale.getDefault(), "¥ %s", orderBean.orderForegiftAmount));
        //总计
        binding.tvAllMount.setText(String.format(Locale.getDefault(), "¥ %s", orderBean.orderAllAmount));
        //实际付款
        binding.tvPlayAmount.setText(String.format(Locale.getDefault(), "实际付款：¥ %s", orderBean.orderAllAmount));

    }

    @Override
    public void onSetPasswordShow(String tempPassword) {
        String[] showText = new String[6];
        for (int i = 0; i < (tempPassword.length() >= 6 ? 6 : tempPassword.length()); i++) {
            showText[i] = "●";
        }
        inputSixbinding.tvSix1.setText(showText[0]);
        inputSixbinding.tvSix2.setText(showText[1]);
        inputSixbinding.tvSix3.setText(showText[2]);
        inputSixbinding.tvSix4.setText(showText[3]);
        inputSixbinding.tvSix5.setText(showText[4]);
        inputSixbinding.tvSix6.setText(showText[5]);
    }

    @Override
    public void onCloseInput() {
        pop.dismiss();
    }

    @Override
    public void onShowPayPw() {
        inputSixPasswordDialog();
    }

    @Override
    public void setAccountMoney(Double tempAccountMoney) {
        binding.tvAccountBalance.setText(String.format(Locale.getDefault(), "¥ %,.2f", tempAccountMoney));
    }


    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cl_pay_type_balance://余额支付
                mPresenter.doSelectPayType(1);
                break;
            case R.id.ll_pay_type_wechat://微信支付
                mPresenter.doSelectPayType(2);
                break;
            case R.id.ll_pay_type_alipay://支付宝支付
                mPresenter.doSelectPayType(3);
                break;
            case R.id.btn_recharge://去充值
                ARouter.getInstance().build(AppConstants.PagePath.USER_PAY).navigation();
                break;
            case R.id.tv_order_ok://支付订单
                presenter.doPay();
                break;
        }
    }


    //显示选择的支付
    @Override
    public void selectPayType(int type) {
        //初始化所以选择状态
        binding.ivBalanceSelect.setImageResource(R.mipmap.ic_pay_type_normal);
        binding.ivWechatSelect.setImageResource(R.mipmap.ic_pay_type_normal);
        binding.ivAlipaySelect.setImageResource(R.mipmap.ic_pay_type_normal);
        switch (type) {
            case 1:
                binding.ivBalanceSelect.setImageResource(R.mipmap.ic_pay_type_selected);
                break;
            case 2:
                binding.ivWechatSelect.setImageResource(R.mipmap.ic_pay_type_selected);
                break;
            case 3:
                binding.ivAlipaySelect.setImageResource(R.mipmap.ic_pay_type_selected);
                break;
        }
    }


    @Subscribe(tags = {@Tag(AppConstants.RxBusAction.TAG_ACCOUNT_UPDATA)})
    public void updateAccountBalance(Boolean balance) {
        //更新账户余额
        presenter.doAccountMoney();
    }


    //数字输入
    class OnInputClickListener implements View.OnClickListener {

        private String inputStr;

        private OnInputClickListener(String inputStr) {
            this.inputStr = inputStr;
        }

        @Override
        public void onClick(View v) {
            presenter.doInputItemPassword(inputStr);
        }
    }

    private PopupWindow pop;
    private CommonDialogInputSixPasswordBinding inputSixbinding;

    /**
     * 输入6位数密码
     */
    public void inputSixPasswordDialog() {
        inputSixbinding = DataBindingUtil.inflate(layoutInflater, R.layout.common_dialog_input_six_password, null, false);
        pop = new PopupWindow(inputSixbinding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setAnimationStyle(R.style.common_pop_head_selected);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        inputSixbinding.ivClose.setOnClickListener(v -> pop.dismiss());
        pop.setOnDismissListener(() -> {
            inputSixbinding.tvSix1.setText("");
            inputSixbinding.tvSix2.setText("");
            inputSixbinding.tvSix3.setText("");
            inputSixbinding.tvSix4.setText("");
            inputSixbinding.tvSix5.setText("");
            inputSixbinding.tvSix6.setText("");
            presenter.doCleanTempPassWord();
            Tools.setWindowAlpha(OrderPayActivity.this, 1f);
        });
        inputSixbinding.tvNo0.setOnClickListener(new OnInputClickListener("0"));
        inputSixbinding.tvNo1.setOnClickListener(new OnInputClickListener("1"));
        inputSixbinding.tvNo2.setOnClickListener(new OnInputClickListener("2"));
        inputSixbinding.tvNo3.setOnClickListener(new OnInputClickListener("3"));
        inputSixbinding.tvNo4.setOnClickListener(new OnInputClickListener("4"));
        inputSixbinding.tvNo5.setOnClickListener(new OnInputClickListener("5"));
        inputSixbinding.tvNo6.setOnClickListener(new OnInputClickListener("6"));
        inputSixbinding.tvNo7.setOnClickListener(new OnInputClickListener("7"));
        inputSixbinding.tvNo8.setOnClickListener(new OnInputClickListener("8"));
        inputSixbinding.tvNo9.setOnClickListener(new OnInputClickListener("9"));
        inputSixbinding.tvNoDelete.setOnClickListener(v ->
                //移除最后一位
                presenter.doDeleteInputItemPassword());
        inputSixbinding.tvForgetPassword.setOnClickListener(v ->
                //忘记密码
                presenter.doForgetPassword());
        if (!pop.isShowing()) {
            pop.showAtLocation(binding.llRoot, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            Tools.setWindowAlpha(OrderPayActivity.this, 0.5f);
        }
    }


    @Subscribe(tags = {@Tag(AppConstants.RxBusAction.TAG_WX_PAY)})
    public void wxPay(Boolean aBoolean) {
        presenter.doWxPay(aBoolean);
    }

}
