package com.xubei.shop.ui.module.user;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ObjectUtils;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActUserUpdateNicknameBinding;
import com.xubei.shop.ui.module.base.ABaseActivity;
import com.xubei.shop.ui.module.user.contract.UpdateNickNameContract;
import com.xubei.shop.ui.module.user.presenter.UpdateNickNamePresenter;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 昵称修改
 * date：2018/9/26 14:05
 * author：xiongj
 **/
@Route(path = AppConstants.PagePath.USER_UPDATENICKNAME)
public class UpdateNickNameActivity extends ABaseActivity<UpdateNickNameContract.Presenter> implements UpdateNickNameContract.View {

    private ActUserUpdateNicknameBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_user_update_nickname);
    }


    @Inject
    UpdateNickNamePresenter presenter;

    @Override
    protected UpdateNickNameContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("昵称");
        initView();
        presenter.start();
    }

    //初始化
    private void initView() {
        //保存
        RxToolbar.itemClicks(binding.appbar.toolbar).as(bindLifecycle()).subscribe(menuItem -> presenter.doSave(binding.etInputNickname.getText().toString().trim()));
        //清理
        RxView.clicks(binding.ivDelete).as(bindLifecycle()).subscribe(o -> binding.etInputNickname.setText(""));
        //设置输入后才显示删除
        RxTextView.textChanges(binding.etInputNickname).as(bindLifecycle()).subscribe(phone -> binding.ivDelete.setVisibility(phone.length() > 0 ? View.VISIBLE : View.GONE));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void setUserNickName(String usernick) {
        if (ObjectUtils.isNotEmpty(usernick)) {
            binding.etInputNickname.setText(usernick);
        }
    }
}
