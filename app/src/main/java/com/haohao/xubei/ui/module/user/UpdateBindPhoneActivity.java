package com.haohao.xubei.ui.module.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActUserUpdateBindphoneBinding;
import com.haohao.xubei.ui.module.base.ABaseActivity;
import com.haohao.xubei.ui.module.user.contract.UpdateBindPhoneContract;
import com.haohao.xubei.ui.module.user.presenter.UpdateBindPhonePresenter;

import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import io.reactivex.Observable;

/**
 * 更改绑定
 * date：2018/9/26 15:01
 * author：xiongj
 **/
@Route(path = AppConstants.PagePath.USER_UPDATEBINDPHONE)
public class UpdateBindPhoneActivity extends ABaseActivity<UpdateBindPhoneContract.Presenter> implements UpdateBindPhoneContract.View {

    private ActUserUpdateBindphoneBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_user_update_bindphone);
        binding.setActivity(this);
    }


    @Inject
    UpdateBindPhonePresenter presenter;

    @Override
    protected UpdateBindPhoneContract.Presenter getMVPPresenter() {
        return presenter;
    }


    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("更改绑定");
        initView();
    }

    private void initView() {
        Observable<CharSequence> inputPhone = RxTextView.textChanges(binding.etPhone);
        Observable<CharSequence> inputCode = RxTextView.textChanges(binding.etCode);
        Observable.combineLatest(inputPhone, inputCode, (phone, code) -> {
            //清除按钮
            binding.ivShowDelete.setVisibility(phone.length() > 0 ? View.VISIBLE : View.GONE);
            return RegexUtils.isMobileSimple(phone) && code.length() == 6;
        }).as(bindLifecycle()).subscribe(aBoolean -> binding.btnOk.setEnabled(aBoolean));
    }

    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_show_delete://删除手机号
                binding.etPhone.setText("");
                binding.etCode.setText("");
                break;
            case R.id.tv_get_code://获取验证码
                getCode();
                break;
            case R.id.btn_ok://更改绑定
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
                presenter.onUpdateBindPhone(phone, code);
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
//                String randstr = data.getStringExtra("randstr");
//                Toast.makeText(this, "验证成功,票据为" + ticket, Toast.LENGTH_LONG).show();
                presenter.onGetCode(phone, ticket);
            }
        }
    }

}
