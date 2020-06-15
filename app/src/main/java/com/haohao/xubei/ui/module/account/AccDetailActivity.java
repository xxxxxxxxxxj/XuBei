package com.haohao.xubei.ui.module.account;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.StringUtils;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;
import com.tmall.ultraviewpager.UltraViewPager;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActAccDetailBinding;
import com.haohao.xubei.ui.module.account.adapter.AccDetailBannerAdapter;
import com.haohao.xubei.ui.module.account.contract.AccDetailContract;
import com.haohao.xubei.ui.module.account.model.OutGoodsDetailBean;
import com.haohao.xubei.ui.module.account.presenter.AccDetailPresenter;
import com.haohao.xubei.ui.module.base.ABaseActivity;
import com.haohao.xubei.ui.module.common.photopreview.PhotoPreviewActivity;
import com.haohao.xubei.ui.module.common.photopreview.PhotoPreviewBean;
import com.haohao.xubei.utlis.AlertDialogUtils;
import com.haohao.xubei.utlis.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 商品详情
 * date：2017/11/30 11:47
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.ACC_DETAIL)
public class AccDetailActivity extends ABaseActivity<AccDetailContract.Presenter> implements AccDetailContract.View {

    private ActAccDetailBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_acc_detail);
        binding.setActivity(this);
    }

    @Inject
    AccDetailPresenter presenter;

    @Override
    protected AccDetailContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Inject
    AccDetailBannerAdapter bannerAdapter;

    @Inject
    AlertDialogUtils dialogUtils;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("账号详情");
        binding.ndv.setOnClickListener(v -> presenter.start());
        presenter.start();
        initLayout();
    }

    private void initLayout() {
        RxToolbar.itemClicks(binding.appbar.toolbar)
                .as(bindLifecycle())
                .subscribe(menuItem -> ARouter.getInstance().build(AppConstants.PagePath.ACC_LEASENOTICE).navigation());
        initUltraViewPager(binding.uvpBanner, position -> presenter.doBannerClick(position));
    }


    @Override
    public void setAccountDetail(OutGoodsDetailBean outGoodsDetailBean) {
        //解决picture为空导致的部分手机崩溃异常
        if (outGoodsDetailBean.picture != null) {
            bannerAdapter.repData(outGoodsDetailBean.picture);//图片Banner
            binding.uvpBanner.refresh();
            if (outGoodsDetailBean.picture.size() <= 1) {
                binding.uvpBanner.disableAutoScroll();//如果只有一条数据，则停止自动滚动
            }
        }
        //标题
        binding.tvGoodsTitle.setText(outGoodsDetailBean.goodsTitle);
        //活动
        if (!StringUtils.isEmpty(outGoodsDetailBean.actity)) {
            binding.tvActivityStr.setText(outGoodsDetailBean.actity);
            binding.tvActivityStr.setVisibility(View.VISIBLE);
        }
        //副标题
//        if (!StringUtils.isEmpty(outGoodsDetailBean.searchTitle)) {
//            binding.tvSearchTitle.setText(outGoodsDetailBean.searchTitle);
//            binding.tvSearchTitle.setVisibility(View.VISIBLE);
//        }
        //区服
        String tempArea = outGoodsDetailBean.gameAllName;
        if ("1".equals(outGoodsDetailBean.isPhone)) {
            tempArea = outGoodsDetailBean.phoneTypeText + "-" + tempArea;
        }
        binding.tvNameArea.setText(tempArea);
        //上号方式
        binding.tvIsShow.setText(outGoodsDetailBean.isShowText);
        //出租次数
        binding.tvNumberRentals.setText(String.format(Locale.getDefault(), "%s次", outGoodsDetailBean.buyCount));
        //最短租赁时间
        binding.tvShortLease.setText(String.format(Locale.getDefault(), "%s小时起租", outGoodsDetailBean.shortLease));
        //押金
        binding.tvForegift.setText(String.format(Locale.getDefault(), "押金%s元", outGoodsDetailBean.foregift));
        //到时不下线
        binding.tvDsbxx.setVisibility(outGoodsDetailBean.isDeadlineOnline ? View.VISIBLE : View.GONE);
        //时租价
        binding.tvHourPrice.setText(outGoodsDetailBean.hourLease);
        //商品状态
        binding.tvRent.setText(outGoodsDetailBean.getGoodsStatusStr());
        //判断是否是签约卖家或者热门推荐
        if (outGoodsDetailBean.signseller == 1) {
            binding.llFwbz.setVisibility(View.VISIBLE);
            binding.tvIsQy.setVisibility(View.VISIBLE);
        }

        //动态渲染字段（账号信息）
        List<OutGoodsDetailBean.NewGoodsWzDtoBean> protoTypeBeanList = outGoodsDetailBean.prototypelist;
        if (protoTypeBeanList != null && protoTypeBeanList.size() > 0) {
            binding.llGoodsAttr.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(this);
            for (int i = 0; i < protoTypeBeanList.size(); i++) {
                OutGoodsDetailBean.NewGoodsWzDtoBean protoTypeBean = protoTypeBeanList.get(i);
                View view = inflater.inflate(R.layout.act_acc_detail_goods_attr, binding.llGoodsAttr, false);
                LinearLayout rootView = view.findViewById(R.id.ll_root_view);
                if (i % 2 == 0) {
                    rootView.setBackgroundColor(0xfff5f5f5);
                } else {
                    rootView.setBackgroundColor(0xfff9f9f9);
                }
                TextView key = view.findViewById(R.id.tv_key);
                TextView value = view.findViewById(R.id.tv_value);
                key.setText(protoTypeBean.keyName);
                value.setText(protoTypeBean.getStrValue());
                binding.llGoodsAttr.addView(view);
            }
        }
        if (outGoodsDetailBean.remark != null) {
            binding.tvMs.setText(outGoodsDetailBean.remark.trim());
        }
        //停服配置
        if (outGoodsDetailBean.gameMaintain != null && outGoodsDetailBean.gameMaintain.status == 1) {
            //设置按钮文字游戏维护中
            binding.tvRent.setText("游戏维护中...");
        }
        //商品不可以
        if ("1".equals(outGoodsDetailBean.goodsFlag)) {
            binding.tvRent.setText("商品不可用");
        }
    }

    @Override
    public void startLookImage(ArrayList<PhotoPreviewBean> list, int position) {
        PhotoPreviewActivity.startPhotoPreview(list, position);
    }

    //显示收藏
    @Override
    public void onShowCollection(String type) {
        if ("收藏成功".equals(type) || "1".equals(type)) {//收藏
            binding.ivSc.setImageResource(R.mipmap.act_acc_ic_conllection_selected);
        } else {
            binding.ivSc.setImageResource(R.mipmap.act_acc_ic_conllection_normal);
        }
    }

    @Override
    public void setNoDataView(int type) {
        binding.ndv.setType(type);
    }

    @Override
    public void showPayInputPop(String orderNo, String timeLong, String kyGold, String payGoldNo) {

    }

    @Override
    public void showShareDialog() {

    }

    /**
     * 初始化banner
     */
    private void initUltraViewPager(UltraViewPager ultraViewpager, AccDetailBannerAdapter.OnItemClickListener onItemClickListener) {
        ultraViewpager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
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

    //点击事件
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_sc://收藏切换
                presenter.doSwitchCollection();
                break;
            case R.id.tv_rent://租赁
                presenter.doClickRent();
                break;
            case R.id.ll_share://分享
                dialogUtils.createShareDialog(binding.llRoot, str -> presenter.doShowShare(str));
                break;
            case R.id.ll_kf://客服
                Tools.startQQCustomerService(this, AppConfig.SERVICE_QQ);
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_zlxz, menu);
        return super.onCreateOptionsMenu(menu);
    }


}
