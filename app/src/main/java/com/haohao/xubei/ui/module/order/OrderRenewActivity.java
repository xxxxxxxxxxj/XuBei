package com.haohao.xubei.ui.module.order;

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
import com.blankj.utilcode.util.TimeUtils;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActOrderRenewBinding;
import com.haohao.xubei.databinding.CommonDialogInputSixPasswordBinding;
import com.haohao.xubei.ui.module.base.ABaseActivity;
import com.haohao.xubei.ui.module.order.contract.OrderRenewContract;
import com.haohao.xubei.ui.module.order.model.OutOrderBean;
import com.haohao.xubei.ui.module.order.presenter.OrderRenewPresenter;
import com.haohao.xubei.utlis.Tools;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import java.util.Locale;

import javax.inject.Inject;

/**
 * 订单续租界面
 * date：2017/12/25 15:21
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.ORDER_RENEW)
public class OrderRenewActivity extends ABaseActivity<OrderRenewContract.Presenter> implements OrderRenewContract.View {


    private ActOrderRenewBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_order_renew);
        binding.setActivity(this);
    }

    @Inject
    OrderRenewPresenter presenter;

    @Override
    protected OrderRenewPresenter getMVPPresenter() {
        return presenter;
    }

    @Inject
    LayoutInflater layoutInflater;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("订单续租");
        initLayout();
        presenter.start();
    }

    private void initLayout() {
        binding.sptvNumber.setGoodsCountChangeListener(count -> presenter.onUpdateNumber(count));
    }

    @Override
    public void setOrderInfo(OutOrderBean orderBean) {
        binding.tvOrderNo.setText(orderBean.gameNo);
        binding.tvEndTime.setText(TimeUtils.millis2String(orderBean.endTime));
    }


    @Override
    public void setAllPrice(double allPrice) {
        binding.tvPrice.setText(String.format(Locale.getDefault(), "¥%.2f", allPrice));
    }

    @Override
    public void setMinNumber(int count) {
        binding.sptvNumber.setMinCount(count);
        binding.sptvNumber.setGoodsCount(count);
    }

    //显示输入支付密码
    @Override
    public void onShowPayPw() {
        inputSixPasswordDialog();
    }

    //设置账号余额
    @Override
    public void setAccountMoney(Double tempAccountMoney) {
        binding.tvAccountBalance.setText(String.format(Locale.getDefault(), "%.2f元", tempAccountMoney));
    }


    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_gopay://去充值
                ARouter.getInstance().build(AppConstants.PagePath.USER_PAY).navigation();
                break;
            case R.id.btn_pay://去支付
                presenter.doPay();
                break;
        }
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
            Tools.setWindowAlpha(OrderRenewActivity.this, 1f);
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
            Tools.setWindowAlpha(OrderRenewActivity.this, 0.5f);
        }

    }

    @Override
    public void onSetPasswordShow(String tempPassword) {
        String[] showText = new String[6];
        for (int i = 0; i < tempPassword.length(); i++) {
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


    @Subscribe(tags = {@Tag(AppConstants.RxBusAction.TAG_ACCOUNT_UPDATA)})
    public void updateAccountBalance(Boolean balance) {
        //更新账户余额
        presenter.doAccountMoney();
    }
}
