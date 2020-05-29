package com.xubei.shop.ui.module.account;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tmall.ultraviewpager.UltraViewPager;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActAccRDetailBinding;
import com.xubei.shop.ui.module.account.adapter.AccRDetailBannerAdapter;
import com.xubei.shop.ui.module.account.contract.AccRDetailContract;
import com.xubei.shop.ui.module.account.model.OutGoodsDetailBean;
import com.xubei.shop.ui.module.account.presenter.AccRDetailPresenter;
import com.xubei.shop.ui.module.base.ABaseActivity;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 我的账号详情
 * date：2018/8/1 17:10
 * author：Seraph
 * mail：417753393@qq.com
 **/
@Route(path = AppConstants.PagePath.ACC_R_DETAIL)
public class AccRDetailActivity extends ABaseActivity<AccRDetailContract.Presenter> implements AccRDetailContract.View {

    private ActAccRDetailBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_acc_r_detail);
    }

    @Inject
    AccRDetailPresenter presenter;

    @Override
    protected AccRDetailContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Inject
    AccRDetailBannerAdapter bannerAdapter;


    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("我的账号详情");
        binding.ndv.setOnClickListener(v -> presenter.start());
        presenter.start();
        initLayout();
    }

    private void initLayout() {
        initUltraViewPager(binding.uvpBanner, position -> presenter.doBannerClick(position));
    }

    /**
     * 初始化banner
     */
    private void initUltraViewPager(UltraViewPager ultraViewpager, AccRDetailBannerAdapter.OnItemClickListener onItemClickListener) {
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

    @Override
    public void onGoodsDetail(OutGoodsDetailBean outGoodsDetailBean) {
        if (outGoodsDetailBean == null) {
            ToastUtils.showShort("获取详情失败");
            finish();
            return;
        }
        //商品详情数据
        //商品状态
        binding.tvProductStatus.setText("立即租赁".equals(outGoodsDetailBean.getGoodsStatusStr())||"立即租用".equals(outGoodsDetailBean.getGoodsStatusStr()) ? "展示中" : outGoodsDetailBean.getGoodsStatusStr());
        //标题
        binding.tvGoodsTitle.setText(outGoodsDetailBean.goodsTitle);
        //时租价
        binding.tvLeaseHour.setText(String.format(Locale.getDefault(), "¥%s", outGoodsDetailBean.leasePrice));
        //天租价
        if (outGoodsDetailBean.dayLease == null) {
            binding.tvLeaseDay.setText("--");
        } else {
            binding.tvLeaseDay.setText(String.format(Locale.getDefault(), "¥%s", outGoodsDetailBean.dayLease));
        }
        //10小时畅玩价
        if (outGoodsDetailBean.allNightPlay == null) {
            binding.tvLeaseOvernight.setText("--");
        } else {
            binding.tvLeaseOvernight.setText(String.format(Locale.getDefault(), "¥%s", outGoodsDetailBean.allNightPlay));
        }
        //押金
        binding.tvForegift.setText(String.format(Locale.getDefault(), "¥%s", outGoodsDetailBean.foregift));
        //商品编号
        binding.tvProductNumber.setText(String.format(Locale.getDefault(), "商品编号：%s", outGoodsDetailBean.goodsCode));
        //发布时间
        String tempTime = outGoodsDetailBean.createTime;
        if (ObjectUtils.isNotEmpty(tempTime) && tempTime.length() > 19) {
            tempTime = tempTime.substring(0, 19);
        }
        binding.tvTime.setText(String.format(Locale.getDefault(), "创建时间：%s", tempTime));
        //商品描述
        binding.tvMs.setText(outGoodsDetailBean.remark.trim());

        //商品信息（动态渲染字段）
        List<OutGoodsDetailBean.NewGoodsWzDtoBean> protoTypeBeanList = outGoodsDetailBean.prototypelist;
        if (protoTypeBeanList != null && protoTypeBeanList.size() > 0) {
            binding.llGoodsAttr.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(this);
            for (OutGoodsDetailBean.NewGoodsWzDtoBean protoTypeBean : protoTypeBeanList) {
                View view = inflater.inflate(R.layout.act_acc_detail_goods_attr, binding.llGoodsAttr, false);
                TextView key = view.findViewById(R.id.tv_key);
                TextView value = view.findViewById(R.id.tv_value);
                key.setText(protoTypeBean.keyName);
                value.setText(protoTypeBean.getStrValue());
                binding.llGoodsAttr.addView(view);
            }
        }

        //商品图片（解决picture为空导致的部分手机崩溃异常）
        if (outGoodsDetailBean.picture != null) {
            bannerAdapter.repData(outGoodsDetailBean.picture);//图片Banner
            binding.uvpBanner.refresh();
            if (outGoodsDetailBean.picture.size() <= 1) {
                binding.uvpBanner.disableAutoScroll();//如果只有一条数据，则停止自动滚动
            }
        }

    }

    @Override
    public void setNoDataView(int type) {
        binding.ndv.setType(type);
    }
}
