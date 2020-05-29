package com.xubei.shop.ui.module.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.launcher.ARouter;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActMainMeBinding;
import com.xubei.shop.ui.module.base.ABaseFragment;
import com.xubei.shop.ui.module.main.adapter.MeTypeAdapter;
import com.xubei.shop.ui.module.main.contract.MainMeContract;
import com.xubei.shop.ui.module.main.presenter.MainMePresenter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.umeng.analytics.MobclickAgent;

import javax.inject.Inject;


/**
 * 我的
 * date：2017/2/20 16:38
 * author：Seraph
 **/
public class MainMe extends ABaseFragment<MainMeContract.Presenter> implements MainMeContract.View {

    private final String mPageName = "MainMeFragment";

    private ActMainMeBinding binding;

    @Override
    protected View initDataBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.act_main_me, container, false);
        binding.setContext(this);
        return binding.getRoot();
    }

    @Inject
    MainMePresenter presenter;

    @Override
    protected MainMeContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Inject
    MeTypeAdapter meTypeAdapter;

    //是否打开眼睛查看具体金额
    public static boolean isShowKyJbAcct = true;

    public static boolean isShowDjAcct = true;

    public static boolean isShowKyAcct = true;

    @Inject
    public MainMe() {
    }

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.vp.setAdapter(meTypeAdapter);
        binding.xtl.setupWithViewPager(binding.vp);
    }

    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_setting://设置
                ARouter.getInstance().build(AppConstants.PagePath.SETTING_HOME).navigation();
                break;
            case R.id.fl_msg://我的消息
                ARouter.getInstance().build(AppConstants.PagePath.USER_MYMSG).navigation();
                break;
        }
    }

    //子界面是否加载完成
    private boolean isShowOk = false;


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        //如果子界面加载完成则又获取焦点，则请求数据
        if (isShowOk) {
            presenter.updateUserBean();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }


    @Subscribe(tags = {@Tag(AppConstants.RxBusAction.TAG_MAIN_ME3)})
    public void showOk(Boolean aBoolean) {
        isShowOk = aBoolean;
        presenter.updateUserBean();
    }

    @Override
    public void updateIsNewMsg(boolean isNewMsg) {
        //是否有新消息
        binding.vMsgNew.setVisibility(isNewMsg ? View.VISIBLE : View.GONE);
    }
}
