package com.xubei.shop.ui.module.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActLoginBinding;
import com.xubei.shop.ui.module.base.ABaseActivity;
import com.xubei.shop.ui.module.login.contract.LoginContract;
import com.xubei.shop.ui.module.login.presenter.LoginPresenter;

import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 登录
 * date：2017/10/25 10:28
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.LOGIN_LOGIN)
public class LoginActivity extends ABaseActivity<LoginContract.Presenter> implements LoginContract.View {

    private ActLoginBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_login);
        binding.setActivity(this);
    }

    @Inject
    LoginPresenter presenter;

    @Override
    protected LoginContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.appbar.setBackgroundColor(Color.TRANSPARENT);
        binding.appbar.toolbar.setBackgroundColor(Color.TRANSPARENT);
        initListener();
        presenter.start();
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        RxTextView.textChanges(binding.etPhone).as(bindLifecycle()).subscribe(phone -> binding.ivShowDelete.setVisibility(phone.length() > 0 ? View.VISIBLE : View.GONE));
        RxTextView.textChanges(binding.etPassword).as(bindLifecycle()).subscribe(password -> binding.cbPasswordMode.setVisibility(password.length() > 0 ? View.VISIBLE : View.GONE));
        RxCompoundButton.checkedChanges(binding.cbPasswordMode).as(bindLifecycle()).subscribe(aBoolean -> {
            //切换密码显示和隐藏
            binding.etPassword.setTransformationMethod(aBoolean ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
            binding.etPassword.setSelection(binding.etPassword.getText().length());
        });
    }

    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_show_delete://清除输入信息
                binding.etPhone.setText("");
                binding.etPassword.setText("");
                binding.etCode.setText("");
                break;
            case R.id.tv_get_code://获取验证码
                getCode();
                break;
            case R.id.tv_forget_password://忘记密码
                ARouter.getInstance().build(AppConstants.PagePath.LOGIN_RESETPASSWORD).navigation();
                break;
            case R.id.btn_login://登录
                String phone = binding.etPhone.getText().toString().trim();
                String password = binding.etPassword.getText().toString().trim();
                String code = binding.etCode.getText().toString().trim();
                if (StringUtils.isEmpty(phone)) {
                    ToastUtils.showShort("请输入用户名");
                    return;
                }
                presenter.onLogin(phone, password, code);
                break;
            case R.id.tv_code_login://切换登录模式
                presenter.setLoginType();
                break;
            case R.id.tv_new_user://新用户
                ARouter.getInstance().build(AppConstants.PagePath.LOGIN_REGISTERED).navigation();
                break;
            case R.id.iv_login_wx:
                presenter.onOtherLogin(Wechat.NAME);
                break;
            case R.id.iv_login_qq:
                presenter.onOtherLogin(QQ.NAME);
                break;
        }
    }

    private String phone;

    //获取验证码
    public void getCode() {
        if (binding.tvGetCode.getCurrentTextColor() == 0xff00c1fb) {
            phone = binding.etPhone.getText().toString().trim();
            if (!RegexUtils.isMobileSimple(phone)) {
                ToastUtils.showShort("请输入正确的手机号");
                return;
            }
            presenter.onVerifyImageCode(1);
        }
    }

    @Override
    public void setUserLoginInfo(String username, String passWord) {
        binding.etPhone.setText(username);
        binding.etPassword.setText(passWord);
        binding.etPhone.setSelection(username.length());
    }

    @Override
    public void setLoginUI(boolean isPwLogin) {
        if (isPwLogin) {
            binding.tvCodeLogin.setText("验证码登录");
            //隐藏短信验码框
            binding.textView40.setVisibility(View.GONE);
            binding.etCode.setVisibility(View.GONE);
            binding.tvGetCode.setVisibility(View.GONE);
            binding.view7.setVisibility(View.GONE);
            //显示密码输入框
            binding.textView32.setVisibility(View.VISIBLE);
            binding.etPassword.setVisibility(View.VISIBLE);
            //判断是否有输入密码
            if (binding.etPassword.getText().length() > 0) {
                binding.cbPasswordMode.setVisibility(View.VISIBLE);
            }
            binding.tvForgetPassword.setVisibility(View.VISIBLE);
            binding.view4.setVisibility(View.VISIBLE);
            //隐藏验证码发送成功通知
            binding.tvCodeOk.setVisibility(View.GONE);
        } else {
            binding.tvCodeLogin.setText("账号密码登录");
            binding.textView40.setVisibility(View.VISIBLE);
            binding.etCode.setVisibility(View.VISIBLE);
            binding.tvGetCode.setVisibility(View.VISIBLE);
            binding.view7.setVisibility(View.VISIBLE);
            binding.textView32.setVisibility(View.GONE);
            binding.etPassword.setVisibility(View.GONE);
            binding.cbPasswordMode.setVisibility(View.GONE);
            binding.tvForgetPassword.setVisibility(View.GONE);
            binding.view4.setVisibility(View.GONE);
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

    @Subscribe(tags = {@Tag(AppConstants.RxBusAction.TAG_OTHER_LOGIN)})
    public void otherLogin(Boolean aBoolean) {
        if (aBoolean) {
            presenter.toLoginByOpenId();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_null, R.anim.anim_slide_out_to_bottom);
    }

    @Override
    public void gotoVerifyFullScreenActivity(String jsurl, int requestCode) {
        try {
            ARouter.getInstance()
                    .build(AppConstants.PagePath.LOGIN_VERIFYPOPUP)
                    .withString("jsurl", jsurl)
                    .navigation(this, requestCode);
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
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {//短信登录
                String ticket = data.getStringExtra("ticket");
//                String randstr = data.getStringExtra("randstr");
//                Toast.makeText(this, "验证成功,票据为" + ticket, Toast.LENGTH_LONG).show();
                presenter.onGetCode(phone, ticket);
            }
        }

    }

}
