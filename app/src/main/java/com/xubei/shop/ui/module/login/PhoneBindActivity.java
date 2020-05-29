package com.xubei.shop.ui.module.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActLoginPhoneBindBinding;
import com.xubei.shop.ui.module.base.ABaseActivity;
import com.xubei.shop.ui.module.login.contract.PhoneBindContract;
import com.xubei.shop.ui.module.login.presenter.PhoneBindPresenter;

import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 手机绑定
 * date：2018/10/9 15:33
 * author：xiongj
 **/
@Route(path = AppConstants.PagePath.LOGIN_PHONEBIND)
public class PhoneBindActivity extends ABaseActivity<PhoneBindContract.Presenter> implements PhoneBindContract.View {

    private ActLoginPhoneBindBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_login_phone_bind);
        binding.setActivity(this);
    }

    @Inject
    PhoneBindPresenter presenter;

    @Override
    protected PhoneBindContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.appbar.setBackgroundColor(Color.TRANSPARENT);
        binding.appbar.toolbar.setBackgroundColor(Color.TRANSPARENT);
        RxTextView.textChanges(binding.etPhone).as(bindLifecycle())
                .subscribe(phone -> binding.ivShowDelete.setVisibility(phone.length() > 0 ? View.VISIBLE : View.GONE));
        presenter.start();
    }

    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_show_delete://删除手机号
                binding.etPhone.setText("");
                break;
            case R.id.tv_get_code://获取验证码
                getCode();
                break;
            case R.id.tv_agreement://服务条款
                presenter.doLookAgreement();
                break;
            case R.id.btn_ok://提交注册
                String phone = binding.etPhone.getText().toString().trim();
                String code = binding.etCode.getText().toString().trim();
                if (!RegexUtils.isMobileSimple(phone)) {
                    ToastUtils.showShort("请输入正确的手机号");
                    return;
                }
                if (StringUtils.isEmpty(code)) {
                    ToastUtils.showShort("请输入验证码");
                    return;
                }
                presenter.onBind(phone, code);
                break;
        }
    }

    private String phone;

    //获取验证码
    private void getCode() {
        if (binding.tvGetCode.getCurrentTextColor() == 0xff00c1fb) {
            phone = binding.etPhone.getText().toString().trim();
            if (!RegexUtils.isMobileSimple(phone)) {
                ToastUtils.showShort("请输入正确的手机号");
                return;
            }
            presenter.onVerifyImageCode();
        }
    }

    @Override
    public void setCountdownText(long time) {
        boolean isCountdown = time > 0;
        binding.tvGetCode.setTextColor(!isCountdown ? 0xff00c1fb : 0xffcccccc);
        binding.tvGetCode.setText(isCountdown ? String.format(Locale.getDefault(), "%d秒后重试", time) : "获取验证码");
    }

    @Override
    public void doShowCodeOK() {
        binding.tvCodeOk.setVisibility(View.VISIBLE);
    }


    @Override
    public void doShowCodeOK(String code) {
        doShowCodeOK();
        binding.etCode.setText(code);
    }

    @Override
    public void gotoVerifyFullScreenActivity(String jsurl) {
        try {
            ARouter.getInstance().build(AppConstants.PagePath.LOGIN_VERIFYPOPUP)
                    .withString("jsurl", jsurl)
                    .navigation(this,1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doGetCode() {
        presenter.onGetCode(phone, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String ticket = data.getStringExtra("ticket");
                // String randstr = data.getStringExtra("randstr");
                // Toast.makeText(this, "验证成功,票据为" + ticket, Toast.LENGTH_LONG).show();
                presenter.onGetCode(phone, ticket);
            }
        }
    }

}
