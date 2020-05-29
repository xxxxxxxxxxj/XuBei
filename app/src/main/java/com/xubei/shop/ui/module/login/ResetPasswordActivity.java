package com.xubei.shop.ui.module.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActLoginResetPasswordBinding;
import com.xubei.shop.ui.module.base.ABaseActivity;
import com.xubei.shop.ui.module.login.contract.ResetPasswordContract;
import com.xubei.shop.ui.module.login.presenter.ResetPasswordPresenter;

import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 重置密码
 * date：2017/10/25 18:18
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.LOGIN_RESETPASSWORD)
public class ResetPasswordActivity extends ABaseActivity<ResetPasswordContract.Presenter> implements ResetPasswordContract.View {


    private ActLoginResetPasswordBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_login_reset_password);
        binding.setActivity(this);
    }

    @Inject
    ResetPasswordPresenter presenter;

    @Override
    protected ResetPasswordContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("重置密码");
        initListener();
    }


    private void initListener() {
        RxTextView.textChanges(binding.etPhone).as(bindLifecycle()).subscribe(phone -> binding.ivShowDelete.setVisibility(phone.length() > 0 ? View.VISIBLE : View.GONE));
        RxTextView.textChanges(binding.etNewPassword).as(bindLifecycle()).subscribe(password -> binding.cbPasswordMode.setVisibility(password.length() > 0 ? View.VISIBLE : View.GONE));
        RxCompoundButton.checkedChanges(binding.cbPasswordMode).as(bindLifecycle()).subscribe(aBoolean -> {
            //切换密码显示和隐藏
            binding.etNewPassword.setTransformationMethod(aBoolean ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
            binding.etNewPassword.setSelection(binding.etNewPassword.getText().length());
        });
    }


    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_show_delete://删除手机号
                binding.etPhone.setText("");
                break;
            case R.id.tv_get_code://获取验证码
                getCode();
                break;
            case R.id.btn_ok:
                //提交网络
                String code = binding.etCode.getText().toString().trim();
                String password = binding.etNewPassword.getText().toString().trim();
                String phone = binding.etPhone.getText().toString().trim();
                if (!RegexUtils.isMobileSimple(phone)) {
                    ToastUtils.showShort("请输入正确的手机号");
                    return;
                }
                if (StringUtils.isEmpty(code)) {
                    ToastUtils.showShort("请输入验证码");
                    return;
                }
                if (StringUtils.isEmpty(password)) {
                    ToastUtils.showShort("请输入新的登录密码");
                    return;
                }
                if (password.length() < 6) {
                    ToastUtils.showShort("新密码不能少于6位");
                    return;
                }
                presenter.onSetPassword(phone, code, password);
                break;
        }
    }

    private String phone;
    //获取验证码
    private void getCode() {
        if (binding.tvGetCode.getCurrentTextColor() == 0xff0e90e8) {
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
        binding.tvGetCode.setTextColor(!isCountdown ? 0xff0e90e8 : 0xffcccccc);
        binding.tvGetCode.setText(isCountdown ? String.format(Locale.getDefault(), "%d秒后重试", time) : "获取验证码");
    }

    @Override
    public void doShowCodeOK(String code) {
        binding.etCode.setText(code);
    }


    @Override
    public void gotoVerifyFullScreenActivity(String jsurl){
        try {
            ARouter.getInstance().build(AppConstants.PagePath.LOGIN_VERIFYPOPUP)
                    .withString("jsurl", jsurl)
                    .navigation(this,1);
        }catch(Exception e){
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
                presenter.onGetCode(phone,ticket);
            }
        }
    }

}
