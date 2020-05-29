package com.xubei.shop.ui.module.user;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActUserAlipayAddBinding;
import com.xubei.shop.ui.module.base.ABaseActivity;
import com.xubei.shop.ui.module.user.contract.AlipayAddContract;
import com.xubei.shop.ui.module.user.presenter.AlipayAddPresenter;

import java.util.Locale;

import javax.inject.Inject;

/**
 * 绑定支付宝
 * date：2017/12/25 10:18
 * author：Seraph
 *
 **/
@Route(path = AppConstants.PagePath.USER_ALIPAYADD)
public class AlipayAddActivity extends ABaseActivity<AlipayAddPresenter> implements AlipayAddContract.View {

    private ActUserAlipayAddBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_user_alipay_add);
        binding.setActivity(this);
    }

    @Inject
    AlipayAddPresenter presenter;

    @Override
    protected AlipayAddPresenter getMVPPresenter() {
        return presenter;
    }

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("绑定支付宝");
        presenter.start();
    }


    @Override
    public void setCountdownText(long time) {
        boolean isCountdown = time > 0;
        binding.tvGetCode.setTextColor(!isCountdown ? 0xff108de7 : 0xffcccccc);
        binding.tvGetCode.setText(isCountdown ? String.format(Locale.getDefault(), "%d秒后重试", time) : "获取验证码");
    }

    @Override
    public void setUserPhone(String phone) {
        binding.tvBindPhone.setText(String.format(Locale.getDefault(), "%s", phone));
    }

    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
//                if (binding.tvGetCode.getCurrentTextColor() == 0xff108de7) {
//                    presenter.doGetCode();
//                }
                break;
            case R.id.btn_ok:
                String code = binding.etCode.getText().toString().trim();
                String aliNo = binding.etAlipayNo.getText().toString().trim();
                if (StringUtils.isEmpty(code)) {
                    ToastUtils.showShort("请输入验证码");
                    return;
                }
                if (StringUtils.isEmpty(aliNo)) {
                    ToastUtils.showShort("请输入需要绑定的支付宝账号");
                    return;
                }
               // presenter.doAdd(code, aliNo);
                break;
        }
    }
}
