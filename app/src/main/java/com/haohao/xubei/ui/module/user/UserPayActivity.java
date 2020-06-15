package com.haohao.xubei.ui.module.user;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActUserRechargeBinding;
import com.haohao.xubei.ui.module.base.ABaseActivity;
import com.haohao.xubei.ui.module.user.adapter.AmountAdapter;
import com.haohao.xubei.ui.module.user.contract.UserPayContract;
import com.haohao.xubei.ui.module.user.presenter.UserPayPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;


/**
 * 用户充值
 * date：2017/12/21 16:01
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.USER_PAY, extras = AppConstants.ARAction.LOGIN)
public class UserPayActivity extends ABaseActivity<UserPayPresenter> implements UserPayContract.View {

    private ActUserRechargeBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_user_recharge);
        binding.setActivity(this);
    }

    @Inject
    UserPayPresenter presenter;

    @Override
    protected UserPayPresenter getMVPPresenter() {
        return presenter;
    }

    private AmountAdapter amountAdapter;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("账号充值");
        presenter.start();
    }

    @Override
    public void initView(ArrayList<String> amountList) {
        amountAdapter = new AmountAdapter(amountList);
        amountAdapter.setOnItemClickListener((adapter, view, position) -> {
            amountAdapter.setSelectAmount(position);
            binding.etInput.setText("");
        });
        binding.rv.setLayoutManager(new GridLayoutManager(this, 3));
        binding.rv.setAdapter(amountAdapter);

        //进行过滤限制小数点后2位
        binding.etInput.setFilters(new InputFilter[]{
                (source, start, end, dest, dstart, dend) -> {
                    String destContent = dest.toString();
                    String newStr = dest.toString() + source.toString();
                    //如果大于9999，则不让输入 （原本输入框的数字+新输入的数字不能大于9999）
                    try {
                        if (!StringUtils.isEmpty(newStr) && Float.valueOf(newStr) > 9999) {
                            return "";
                        }
                    } catch (Exception e) {
                        return "";
                    }

                    if (destContent.contains(".")) {
                        int index = destContent.indexOf(".");
                        if (dend - index >= 3) {
                            return "";
                        }
                    }
                    return null;
                }
        });
        RxTextView.textChanges(binding.etInput).as(bindLifecycle()).subscribe(charSequence -> {
            if (charSequence.length() > 0) {
                amountAdapter.setSelectAmount(-1);
            }
        });
    }


    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_pay:
                String input = binding.etInput.getText().toString().trim();
                if (ObjectUtils.isEmpty(input)) {
                    //获取选择的金额
                    int selectAmount = amountAdapter.getSelectAmount();
                    if (selectAmount >= 0) {
                        //调用选择的金额充值
                        presenter.onStartSelectPay(selectAmount);
                        return;
                    }
                    ToastUtils.showShort("请输入充值金额");
                    return;
                }
                presenter.onStartPay(input);
                break;
            case R.id.ll_pay_type_wechat://微信支付
                presenter.doPayType(UserPayPresenter.WECHAT_APP);

                break;
            case R.id.ll_pay_type_alipay://支付宝支付
                presenter.doPayType(UserPayPresenter.ALIPAY_APP);
                break;
        }
    }


    //显示选择的支付
    @Override
    public void selectPayType(String type) {
        //初始化所以选择状态
        binding.ivWechatSelect.setImageResource(R.mipmap.ic_pay_type_normal);
        binding.ivAlipaySelect.setImageResource(R.mipmap.ic_pay_type_normal);
        switch (type) {
            case UserPayPresenter.WECHAT_APP:
                binding.ivWechatSelect.setImageResource(R.mipmap.ic_pay_type_selected);
                break;
            case UserPayPresenter.ALIPAY_APP:
                binding.ivAlipaySelect.setImageResource(R.mipmap.ic_pay_type_selected);
                break;
        }
    }


    @Subscribe(tags = {@Tag(AppConstants.RxBusAction.TAG_WX_PAY)})
    public void wxPay(Boolean aBoolean) {
        if (aBoolean) {
            presenter.wxPay();
        }
    }


}
