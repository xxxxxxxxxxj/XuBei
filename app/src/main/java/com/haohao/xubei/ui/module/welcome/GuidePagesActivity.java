package com.haohao.xubei.ui.module.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ConvertUtils;
import com.tmall.ultraviewpager.UltraViewPager;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActWelcomeGuidePagesBinding;
import com.haohao.xubei.ui.module.base.ABaseActivity;
import com.haohao.xubei.ui.module.main.MainActivity;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 引导页
 * date：2017/5/3 10:40
 * author：Seraph
 *
 **/
@Route(path = AppConstants.PagePath.WELCOME_GUIDEPAGES)
public class GuidePagesActivity extends ABaseActivity<GuidePagesActivityContract.Presenter> implements GuidePagesActivityContract.View {


    @Inject
    GuidePagesActivityPresenter presenter;

    @Override
    protected GuidePagesActivityPresenter getMVPPresenter() {
        return presenter;
    }

    private ActWelcomeGuidePagesBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_welcome_guide_pages);
    }

    @Inject
    UltraPagerAdapter mUltraPagerAdapter;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        initViewPager();
        mPresenter.start();
    }

    private void initViewPager() {
        mUltraPagerAdapter.setOnClickListener(position -> presenter.onItemClick(position));
        binding.ultraViewpager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        //UltraPagerAdapter 绑定子view到UltraViewPager
        binding.ultraViewpager.setOffscreenPageLimit(3);
        binding.ultraViewpager.setAdapter(mUltraPagerAdapter);
        //内置indicator初始化
        binding.ultraViewpager.initIndicator();
        //设置indicator样式
        binding.ultraViewpager.getIndicator()
                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                .setFocusColor(0xFFf4f3f3)
                .setNormalColor(0xffdddddd)
                .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));
        //设置indicator对齐方式
        binding.ultraViewpager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        binding.ultraViewpager.getIndicator().setMargin(10, 10, 10, ConvertUtils.dp2px(10));
        //构造indicator,绑定到UltraViewPager
        binding.ultraViewpager.getIndicator().build();
    }


    @Override
    public void jumpNextActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void setImageList(Integer[] images) {
        mUltraPagerAdapter.setListImage(images);
        binding.ultraViewpager.refresh();
    }

}
