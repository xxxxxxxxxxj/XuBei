package com.xubei.shop.ui.module.main;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ConvertUtils;
import com.xubei.shop.AppConfig;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActMainHomeBinding;
import com.xubei.shop.ui.module.account.model.AccBean;
import com.xubei.shop.ui.module.base.ABaseFragment;
import com.xubei.shop.ui.module.base.BaseDataCms;
import com.xubei.shop.ui.module.main.adapter.HomeBannerAdapter;
import com.xubei.shop.ui.module.main.adapter.HomeGameAdapter;
import com.xubei.shop.ui.module.main.adapter.HomeHotAdapter;
import com.xubei.shop.ui.module.main.adapter.HomeWelfareAdapter;
import com.xubei.shop.ui.module.main.contract.MainHomeContract;
import com.xubei.shop.ui.module.main.model.BannerBean;
import com.xubei.shop.ui.module.main.model.HomeMultipleItem;
import com.xubei.shop.ui.module.main.model.WelfareBean;
import com.xubei.shop.ui.module.main.presenter.MainHomePresenter;
import com.xubei.shop.utlis.GallerySnapHelper;
import com.xubei.shop.utlis.Tools;
import com.xubei.shop.utlis.myDividerItemDecoration;
import com.tmall.ultraviewpager.UltraViewPager;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import javax.inject.Inject;

/**
 * 首页（包含热门，推荐，banner，通知相关）
 * date：2017/2/20 16:38
 **/
public class MainHome extends ABaseFragment<MainHomeContract.Presenter> implements MainHomeContract.View {

    private final String mPageName = "MainHomeFragment";

    private ActMainHomeBinding binding;

    @Inject
    MainHomePresenter presenter;

    @Override
    protected MainHomeContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Override
    protected View initDataBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.act_main_home, container, false);
        binding.appbar.setHome(this);
        return binding.getRoot();
    }


    @Inject
    HomeGameAdapter adapter;

    @Inject
    HomeBannerAdapter bannerAdapter;

    @Inject
    HomeHotAdapter hotAdapter;

    @Inject
    HomeWelfareAdapter welfareAdapter;

    @Inject
    GallerySnapHelper mGalleryHotSnapHelper;

    @Inject
    GallerySnapHelper mGalleryWelfareSnapHelper;

    @Inject
    public MainHome() {
    }

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        //banner
        initUltraViewPager(binding.uvpBanner, position -> presenter.doBannerClick(position));
        //game
        binding.recyclerview.setLayoutManager(new GridLayoutManager(getContext(), 4));
        binding.recyclerview.setAdapter(adapter);
        adapter.setSpanSizeLookup((gridLayoutManager, position) -> (adapter.getData().get(position).getItemType() == HomeMultipleItem.TYPE_ACCOUNT_LIST ? 1 : 4));
        adapter.setOnItemClickListener((adapter, view, position) -> presenter.onItemClick(position));
        //间距
        myDividerItemDecoration decoration = new myDividerItemDecoration(getContext());

        binding.rvHot.addItemDecoration(decoration);
        binding.rvWelfare.addItemDecoration(decoration);
        //热销
        binding.rvHot.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvHot.setAdapter(hotAdapter);
        hotAdapter.setOnItemClickListener((adapter, view, position) -> presenter.onItemHotClick(position));
        mGalleryHotSnapHelper.attachToRecyclerView(binding.rvHot);
        //福利中心
        binding.rvWelfare.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvWelfare.setAdapter(welfareAdapter);
        welfareAdapter.setOnItemClickListener((adapter, view, position) -> presenter.onItemWelfareClick(position));
        mGalleryWelfareSnapHelper.attachToRecyclerView(binding.rvWelfare);
        //加载空白
        binding.ndv.setOnClickListener(v -> presenter.start());
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        presenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }


    @Override
    public void setGameList(List<BaseDataCms<BannerBean>> bannerList,
                            List<HomeMultipleItem> list,
                            List<BaseDataCms<AccBean>> hotList,
                            List<BaseDataCms<WelfareBean>> welfareList) {
        //第一个是banner
        bannerAdapter.repData(bannerList);
        binding.uvpBanner.refresh();
        adapter.replaceData(list);
        //热销商品
        if (hotList != null && hotList.size() > 0) {
            binding.tvHotTitle.setVisibility(View.VISIBLE);
            binding.rvHot.setVisibility(View.VISIBLE);
            hotAdapter.replaceData(hotList);
        }
        //福利中心
        if (welfareList != null && welfareList.size() > 0) {
            binding.tvWelfareTitle.setVisibility(View.VISIBLE);
            binding.rvWelfare.setVisibility(View.VISIBLE);
            welfareAdapter.replaceData(welfareList);
        }
    }


    @Override
    public void setNoData(int type) {
        binding.ndv.setType(type);
    }


    /**
     * 初始化banner
     */
    private void initUltraViewPager(UltraViewPager ultraViewpager, HomeBannerAdapter.OnItemClickListener onItemClickListener) {
        ultraViewpager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        ultraViewpager.setHGap(100);
        ultraViewpager.setMultiScreen(0.95f);
        //UltraPagerAdapter 绑定子view到UltraViewPager
        ultraViewpager.setOffscreenPageLimit(5);
        bannerAdapter.setOnItemClickListener(onItemClickListener);
        ultraViewpager.setAdapter(bannerAdapter);
        //内置indicator初始化
        ultraViewpager.initIndicator();
        //设置indicator样式
        ultraViewpager.getIndicator()
                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                .setFocusColor(0xFF0084FF)
                .setNormalColor(0xFF98A9BA)
                .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));
        //设置indicator对齐方式
        ultraViewpager.getIndicator().setMargin(10, 10, 10, ConvertUtils.dp2px(10));
        ultraViewpager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        //构造indicator,绑定到UltraViewPager
        ultraViewpager.getIndicator().build();
        //set an infinite loop
        ultraViewpager.setInfiniteLoop(true);
        //enable auto-scroll mode
        ultraViewpager.setAutoScroll(3000);
    }


    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_msg://公告
                ARouter.getInstance().build(AppConstants.PagePath.USER_NOTICE).navigation();
                break;
            case R.id.tv_search://商品搜索
                ARouter.getInstance().build(AppConstants.PagePath.ACC_SEARCH).navigation();
                break;
            case R.id.iv_customer_service://客服
                Tools.startQQCustomerService(getContext(), AppConfig.SERVICE_QQ);
                break;
        }
    }


}
