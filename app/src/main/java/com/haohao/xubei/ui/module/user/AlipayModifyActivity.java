package com.haohao.xubei.ui.module.user;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActUserAlipayModityBinding;
import com.haohao.xubei.ui.module.base.ABaseActivity;
import com.haohao.xubei.ui.module.user.contract.AlipayModifyContract;
import com.haohao.xubei.ui.module.user.presenter.AlipayModifyPresenter;

import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import io.reactivex.Observable;

/**
 * 修改支付宝界面
 * date：2017/12/25 10:18
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.USER_ALIPAYMODIFY)
public class AlipayModifyActivity extends ABaseActivity<AlipayModifyPresenter> implements AlipayModifyContract.View {

    private ActUserAlipayModityBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_user_alipay_modity);
        binding.setActivity(this);
    }

    @Inject
    AlipayModifyPresenter presenter;

    @Override
    protected AlipayModifyPresenter getMVPPresenter() {
        return presenter;
    }


    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("修改绑定支付宝账号");
        initLayout();
        presenter.start();
    }

    private void initLayout() {
        Observable<CharSequence> code = RxTextView.textChanges(binding.etCode);
        Observable<CharSequence> idCard = RxTextView.textChanges(binding.etAlipayNewNo);
        Observable.combineLatest(code, idCard, (code2, alipayNo) -> (code2.toString().trim().length() == 6 && alipayNo.length() > 0))
                .as(bindLifecycle())
                .subscribe(aBoolean -> binding.btnOk.setEnabled(aBoolean));
    }

    @Override
    public void setCountdownText(long time) {
        boolean isCountdown = time > 0;
        binding.tvGetCode.setTextColor(!isCountdown ? 0xff0099cc : 0xffcccccc);
        binding.tvGetCode.setText(isCountdown ? String.format(Locale.getDefault(), "%d秒后重试", time) : "获取验证码");
    }

    @Override
    public void setUserPhone(String phone) {
        binding.tvBindPhone.setText(String.format(Locale.getDefault(), "您绑定的手机：%s", phone));
    }

    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
//                if (binding.tvGetCode.getCurrentTextColor() == 0xff0099cc) {
//                    presenter.doGetCode();
//                }
                break;
            case R.id.btn_ok:
              //  presenter.doUpdateAlipayAccount(binding.etAlipayNewNo.getText().toString(), binding.etCode.getText().toString());
                break;
        }
    }
}
