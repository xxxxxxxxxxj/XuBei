package com.haohao.xubei.ui.module.user;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ObjectUtils;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActUserGetMoneyBinding;
import com.haohao.xubei.ui.module.base.ABaseActivity;
import com.haohao.xubei.ui.module.user.contract.UserGetMoneyContract;
import com.haohao.xubei.ui.module.user.model.AcctManageBean;
import com.haohao.xubei.ui.module.user.presenter.UserGetMoneyPresenter;

import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 提现
 * date：2017/12/21 17:22
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.USER_GETMONEY)
public class UserGetMoneyActivity extends ABaseActivity<UserGetMoneyContract.Presenter> implements UserGetMoneyContract.View {

    private ActUserGetMoneyBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_user_get_money);
        binding.setActivity(this);
    }

    @Inject
    UserGetMoneyPresenter presenter;

    @Override
    protected UserGetMoneyPresenter getMVPPresenter() {
        return presenter;
    }


    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("提现");
        presenter.start();
    }

    public void initLayout(AcctManageBean moneyBean) {
        RxTextView.textChanges(binding.etInputAmount)
                .as(bindLifecycle())
                .subscribe(inputStr -> {
            if (inputStr.length() == 0) {
                setAccountMoney(moneyBean);
                binding.btnOk.setEnabled(false);
                return;
            }
            if (Double.valueOf(inputStr.toString().trim()) > moneyBean.aviableAmt) {
                //超过提现金额
                binding.tvAmount.setText("金额已超过提现余额");
                binding.tvAmount.setTextColor(0xFFFF0000);
                binding.btnOk.setEnabled(false);
            } else {
                setAccountMoney(moneyBean);
                binding.btnOk.setEnabled(true);
            }
        });
        //进行过滤限制小数点后2位
        binding.etInputAmount.setFilters(new InputFilter[]{
                (source, start, end, dest, dstart, dend) -> {
                    String lastInputContent = dest.toString();
                    if (lastInputContent.contains(".")) {
                        int index = lastInputContent.indexOf(".");
                        if (dend - index >= 3) {
                            return "";
                        }
                    }
                    return null;
                }
        });
        //如果有卡号显示卡号，没有显示添加卡号
        if (ObjectUtils.isNotEmpty(moneyBean.alipayAcct)) {
            binding.clAddPay.setVisibility(View.GONE);
            binding.clShowPay.setVisibility(View.VISIBLE);
            binding.tvNo.setText(moneyBean.alipayAcct);
        } else {
            binding.clShowPay.setVisibility(View.GONE);
            binding.clAddPay.setVisibility(View.VISIBLE);
        }
    }

    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cl_show_pay://修改支付宝账号
                //ARouter.getInstance().build(AppConstants.PagePath.USER_ALIPAYMODIFY).navigation();
                break;
            case R.id.tv_add_pay://添加支付
                presenter.doAddPayCar();
                break;
            case R.id.btn_ok://提现
                presenter.doWithdraw(binding.etInputAmount.getText().toString().trim());
                break;
        }
    }


    private void setAccountMoney(AcctManageBean acctManageBean) {
        binding.tvAmount.setText(String.format(Locale.getDefault(), "可用余额 %.2f元", acctManageBean.aviableAmt));
        binding.tvAmount.setTextColor(0xFF999999);
    }


    @Subscribe(tags = {@Tag(AppConstants.RxBusAction.TAG_ADD_ALIPAY)})
    public void addAlipay(Boolean aBoolean) {
        //添加了支付宝账号
        presenter.start();
    }
}
