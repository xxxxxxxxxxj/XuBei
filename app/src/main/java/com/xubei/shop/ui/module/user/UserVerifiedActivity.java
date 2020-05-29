package com.xubei.shop.ui.module.user;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActUserVerifiedBinding;
import com.xubei.shop.ui.module.base.ABaseActivity;
import com.xubei.shop.ui.module.user.contract.UserVerifiedContract;
import com.xubei.shop.ui.module.user.presenter.UserVerifiedPresenter;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import io.reactivex.Observable;

/**
 * 身份验证
 * date：2017/12/22 16:46
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.USER_VERIFIED)
public class UserVerifiedActivity extends ABaseActivity<UserVerifiedPresenter> implements UserVerifiedContract.View {

    private ActUserVerifiedBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_user_verified);
        binding.setActivity(this);
    }

    @Inject
    UserVerifiedPresenter presenter;

    @Override
    protected UserVerifiedPresenter getMVPPresenter() {
        return presenter;
    }


    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("身份验证");
        initLayout();
        presenter.start();
    }

    private void initLayout() {
        Observable<CharSequence> name = RxTextView.textChanges(binding.etName);
        Observable<CharSequence> idCard = RxTextView.textChanges(binding.etIdcard);
        Observable.combineLatest(name, idCard, (name2, idCard2) -> {
            //验证输入
            return name2.toString().trim().length() > 0 && idCard2.length() >= 15;
        }).as(bindLifecycle())
                .subscribe(aBoolean -> binding.btnOk.setEnabled(aBoolean));
    }


    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:   //提交信息
                String name = binding.etName.getText().toString().trim();
                String idCard = binding.etIdcard.getText().toString().trim();
                if (StringUtils.isEmpty(name)) {
                    ToastUtils.showShort("请输入您的真实姓名");
                    return;
                }
                if (StringUtils.isEmpty(idCard)) {
                    ToastUtils.showShort("请输入您的身份证号");
                    return;
                }
                presenter.doVerified(name, idCard);
                break;
        }
    }

}
