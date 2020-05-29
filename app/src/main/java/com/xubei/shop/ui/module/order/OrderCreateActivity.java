package com.xubei.shop.ui.module.order;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ObjectUtils;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActOrderCreateBinding;
import com.xubei.shop.databinding.CommonDialogInputSixPasswordBinding;
import com.xubei.shop.ui.module.account.model.OutGoodsDetailBean;
import com.xubei.shop.ui.module.base.ABaseActivity;
import com.xubei.shop.ui.module.order.contract.OrderCreateContract;
import com.xubei.shop.ui.module.order.presenter.OrderCreatePresenter;
import com.xubei.shop.utlis.Tools;

import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 创建订单和支付订单
 * date：2017/12/4 15:34
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.ORDER_CREATE, extras = AppConstants.ARAction.LOGIN)
public class OrderCreateActivity extends ABaseActivity<OrderCreateContract.Presenter> implements OrderCreateContract.View {


    private ActOrderCreateBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_order_create);
        binding.setActivity(this);
    }

    @Inject
    OrderCreatePresenter presenter;

    @Override
    protected OrderCreatePresenter getMVPPresenter() {
        return presenter;
    }

    @Inject
    LayoutInflater layoutInflater;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("订单支付");
        binding.ndv.setOnClickListener(v -> presenter.start());
        presenter.start();
    }


    @Override
    public void setUIShow(OutGoodsDetailBean detailBean) {
        //时租
        binding.tv1Price.setText(String.format(Locale.getDefault(), "¥%.2f", Double.valueOf(detailBean.hourLease)));
        //天租
        if (ObjectUtils.isNotEmpty(detailBean.dayLease)) {
            binding.tv2Price.setText(String.format(Locale.getDefault(), "¥%.2f", Double.valueOf(detailBean.dayLease)));
        } else {
            binding.tv2Price.setText("-");
            binding.llType2.setEnabled(false);
        }
        //10小时
        if (ObjectUtils.isNotEmpty(detailBean.allNightPlay)) {
            binding.tv3Price.setText(String.format(Locale.getDefault(), "¥%.2f", Double.valueOf(detailBean.allNightPlay)));
        } else {
            binding.tv3Price.setText("-");
            binding.llType3.setEnabled(false);
        }
        //周租
        if (ObjectUtils.isNotEmpty(detailBean.weekLease)) {
            binding.tv4Price.setText(String.format(Locale.getDefault(), "¥%.2f", Double.valueOf(detailBean.weekLease)));
        } else {
            binding.tv4Price.setText("-");
            binding.llType4.setEnabled(false);
        }
        //最短租赁时间
        binding.sptv.setMinCount(Integer.valueOf(detailBean.shortLease));
        // binding.tvMinTime.setText(String.format(Locale.getDefault(), "%s小时起", detailBean.shortLease));
        //更新数量
        presenter.doTimeUpdata(Integer.valueOf(detailBean.shortLease));
        //设置监听
        binding.sptv.setGoodsCountChangeListener(count -> presenter.doTimeUpdata(count));
        //押金
        binding.tvDeposit.setText(String.format(Locale.getDefault(), "¥ %.2f", ObjectUtils.isNotEmpty(detailBean.foregift) ? Double.valueOf(detailBean.foregift) : 0d));

    }

    //设置账号余额
    @Override
    public void setAccountMoney(Double tempAccountMoney) {
        binding.tvAccountBalance.setText(String.format(Locale.getDefault(), "¥ %,.2f", tempAccountMoney));
    }

    //显示输入支付密码
    @Override
    public void onShowPayPw() {
        inputSixPasswordDialog();
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
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
        }
    }

    @Override
    public void setNoDataView(int type) {
        binding.ndv.setType(type);
    }

    //显示需要支付的金额
    @Override
    public void setPayMoney(Double tempShowRent, Double tempShowPayPrice) {
        //租金
        binding.tvRent.setText(String.format(Locale.getDefault(), "¥ %,.2f", tempShowRent));
        //实际付款
        binding.tvPlayAmount.setText(String.format(Locale.getDefault(), "实际付款：¥ %,.2f", tempShowPayPrice));
    }


    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_type_1://小时租赁
                presenter.doSelectTimeType(0);
                break;
            case R.id.ll_type_2://日租赁
                presenter.doSelectTimeType(1);
                break;
            case R.id.ll_type_3://10小时租赁
                presenter.doSelectTimeType(3);
                break;
            case R.id.ll_type_4://周租赁
                presenter.doSelectTimeType(2);
                break;
            case R.id.cl_pay_type_balance://余额支付
                presenter.doSelectPayType(1);
                break;
            case R.id.ll_pay_type_wechat://微信支付
                presenter.doSelectPayType(2);
                break;
            case R.id.ll_pay_type_alipay://支付宝支付
                presenter.doSelectPayType(3);
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

    //选择租赁方式
    @Override
    public void selectTimeType(int timeType) {
        //初始化所有状态
        binding.llType1.setBackground(null);
        binding.llType2.setBackground(null);
        binding.llType3.setBackground(null);
        binding.llType4.setBackground(null);
        binding.tv1Price.setTextColor(0xff69b6ff);
        binding.tv2Price.setTextColor(0xff69b6ff);
        binding.tv3Price.setTextColor(0xff69b6ff);
        binding.tv4Price.setTextColor(0xff69b6ff);
        binding.tv1Type.setTextColor(0xff666666);
        binding.tv2Type.setTextColor(0xff666666);
        binding.tv3Type.setTextColor(0xff666666);
        binding.tv4Type.setTextColor(0xff666666);
        switch (timeType) {
            case 0:
                binding.llType1.setBackgroundResource(R.drawable.act_time_type_item_selected_bg);
                binding.tv1Price.setTextColor(0xffffffff);
                binding.tv1Type.setTextColor(0xffffffff);
                //显示数量加减
                binding.llTimeNo.setVisibility(View.VISIBLE);
                break;
            case 1:
                binding.llType2.setBackgroundResource(R.drawable.act_time_type_item_selected_bg);
                binding.tv2Price.setTextColor(0xffffffff);
                binding.tv2Type.setTextColor(0xffffffff);
                binding.llTimeNo.setVisibility(View.GONE);
                break;
            case 3:
                binding.llType3.setBackgroundResource(R.drawable.act_time_type_item_selected_bg);
                binding.tv3Price.setTextColor(0xffffffff);
                binding.tv3Type.setTextColor(0xffffffff);
                binding.llTimeNo.setVisibility(View.GONE);
                break;
            case 2:
                binding.llType4.setBackgroundResource(R.drawable.act_time_type_item_selected_bg);
                binding.tv4Price.setTextColor(0xffffffff);
                binding.tv4Type.setTextColor(0xffffffff);
                binding.llTimeNo.setVisibility(View.GONE);
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
        pop.setFocusable(false);
        //pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(false);
        inputSixbinding.ivClose.setOnClickListener(v -> presenter.payClose());
        pop.setOnDismissListener(() -> {
            inputSixbinding.tvSix1.setText("");
            inputSixbinding.tvSix2.setText("");
            inputSixbinding.tvSix3.setText("");
            inputSixbinding.tvSix4.setText("");
            inputSixbinding.tvSix5.setText("");
            inputSixbinding.tvSix6.setText("");
            presenter.doCleanTempPassWord();
            Tools.setWindowAlpha(OrderCreateActivity.this, 1f);
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
        //移除最后一位
        inputSixbinding.tvNoDelete.setOnClickListener(v -> presenter.doDeleteInputItemPassword());
        //忘记密码
        inputSixbinding.tvForgetPassword.setOnClickListener(v -> presenter.doForgetPassword());
        if (!pop.isShowing()) {
            pop.showAtLocation(binding.llRoot, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            Tools.setWindowAlpha(OrderCreateActivity.this, 0.5f);
        }
    }


    @Subscribe(tags = {@Tag(AppConstants.RxBusAction.TAG_ACCOUNT_UPDATA)})
    public void updateAccountBalance(Boolean balance) {
        //更新账户余额
        presenter.doAccountMoney();
    }

    @Subscribe(tags = {@Tag(AppConstants.RxBusAction.TAG_WX_PAY)})
    public void wxPay(Boolean aBoolean) {
        presenter.doWxPay(aBoolean);
    }

}
