package com.xubei.shop.ui.module.account;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActAccRListBinding;
import com.xubei.shop.di.QualifierType;
import com.xubei.shop.ui.module.account.adapter.AccVPAdapter;
import com.xubei.shop.ui.module.base.ABaseActivity;
import com.xubei.shop.ui.module.base.IABaseContract;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;


/**
 * 卖家发布的账号列表
 * date：2017/12/19 15:19
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.ACC_R_LIST,extras = AppConstants.ARAction.LOGIN)
public class AccRListActivity extends ABaseActivity<IABaseContract.ABasePresenter> {


    private ActAccRListBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_acc_r_list);
        binding.setActivity(this);
    }

    @Override
    protected IABaseContract.ABasePresenter getMVPPresenter() {
        return null;
    }

    @Inject
    AccVPAdapter vpAdapter;

    @QualifierType("type")
    @Inject
    Integer type;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("我的账号管理");
        RxToolbar.itemClicks(binding.appbar.toolbar).as(bindLifecycle()).subscribe(menuItem ->
                ARouter.getInstance().build(AppConstants.PagePath.ACC_TOP).navigation()
        );

        initLayout();
    }

    private void initLayout() {
        binding.vp.setAdapter(vpAdapter);
        binding.xtl.setupWithViewPager(binding.vp);
        binding.vp.setCurrentItem(type);
    }


    public void onViewClicked(View view) {
        //跳转我的账号管理搜索
        ARouter.getInstance().build(AppConstants.PagePath.ACC_R_LISTSEARCH).navigation();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_acc_r_list, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Subscribe(tags = {@Tag(AppConstants.RxBusAction.TAG_ACCOUNT_SELLER_LIST_TYPE)})
    public void setCurrentItem(Integer itemType) {
        binding.vp.setCurrentItem(itemType);
    }


}
