package com.haohao.xubei.ui.module.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.data.db.table.UserTable;
import com.haohao.xubei.databinding.ActLoginResetPwPayBinding;
import com.haohao.xubei.ui.module.base.ABaseActivity;
import com.haohao.xubei.ui.module.login.contract.ResetPayPwContract;
import com.haohao.xubei.ui.module.login.presenter.ResetPayPwPresenter;

import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 重置支付密码
 * date：2017/10/25 18:18
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.LOGIN_RESETPAYPW)
public class ResetPayPwActivity extends ABaseActivity<ResetPayPwPresenter> implements ResetPayPwContract.View {

    private ActLoginResetPwPayBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_login_reset_pw_pay);
        binding.setActivity(this);
    }

    @Inject
    ResetPayPwPresenter presenter;

    @Override
    protected ResetPayPwPresenter getMVPPresenter() {
        return presenter;
    }

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("重置支付密码");
        initListener();
        presenter.start();
    }


    @Override
    public void setUserInfo(UserTable userBean) {
        if (ObjectUtils.isEmpty(userBean.getMobile())) {
            ToastUtils.showShort("请先绑定手机号");
            finish();
            return;
        }
        if (ObjectUtils.isNotEmpty(userBean.getMobile()) && userBean.getMobile().length() == 11) {
            String tempMobile = userBean.getMobile().substring(0, 3) + "****" + userBean.getMobile().substring(7, 11);
            binding.etPhone.setText(tempMobile);
        }

    }


    private void initListener() {
        //    RxTextView.textChanges(binding.etPhone).subscribe(phone -> binding.ivShowDelete.setVisibility(phone.length() > 0 ? View.VISIBLE : View.GONE));
        RxTextView.textChanges(binding.etNewPassword).as(bindLifecycle()).subscribe(password -> binding.cbPasswordMode.setVisibility(password.length() > 0 ? View.VISIBLE : View.GONE));

        RxCompoundButton.checkedChanges(binding.cbPasswordMode).as(bindLifecycle()).subscribe(aBoolean -> {
            //切换密码显示和隐藏
            binding.etNewPassword.setTransformationMethod(aBoolean ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
            binding.etNewPassword.setSelection(binding.etNewPassword.getText().length());
        });
    }

    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.iv_show_delete://删除手机号
//                binding.etPhone.setText("");
//                break;
            case R.id.tv_get_code://获取验证码
                getCode();
                break;
            case R.id.btn_ok:
                //提交网络
                String code = binding.etCode.getText().toString().trim();
                String password = binding.etNewPassword.getText().toString().trim();
                if (StringUtils.isEmpty(code)) {
                    ToastUtils.showShort("请输入验证码");
                    return;
                }
                if (StringUtils.isEmpty(password)) {
                    ToastUtils.showShort("请输入新的支付密码");
                    return;
                }
                if (password.length() != 6) {
                    ToastUtils.showShort("新密码必须为6位数字");
                    return;
                }
                presenter.onSetPassword(code, password);
                break;
        }
    }

    private void getCode() {
        if (binding.tvGetCode.getCurrentTextColor() == 0xff0e90e8) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String ticket = data.getStringExtra("ticket");
                //  String randstr = data.getStringExtra("randstr");
                //  Toast.makeText(this, "验证成功,票据为" + ticket, Toast.LENGTH_LONG).show();
                presenter.onGetCode(ticket);
            }
        }
    }
}
