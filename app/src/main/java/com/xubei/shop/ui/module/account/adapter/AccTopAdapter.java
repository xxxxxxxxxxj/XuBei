package com.xubei.shop.ui.module.account.adapter;

import android.widget.ImageView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xubei.shop.R;
import com.xubei.shop.data.network.glide.GlideApp;
import com.xubei.shop.ui.module.account.model.OutGoodsBean;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 商品置顶
 * date：2018/12/7 09:54
 * author：xiongj
 **/
public class AccTopAdapter extends BaseQuickAdapter<OutGoodsBean, BaseViewHolder> {


    public AccTopAdapter(@Nullable List<OutGoodsBean> data) {
        super(R.layout.act_acc_top_item, data);
    }


    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            //局部刷新
            String payload = (String) payloads.get(0);
            //如果是选择
            OutGoodsBean item = getItem(position);
            if ("select".equals(payload) && item != null) {
                holder.setImageResource(R.id.iv_isok, item.isSelect ? R.mipmap.ic_acc_top_item_ok : R.drawable.act_acc_top_not_all);
            }
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, OutGoodsBean item) {
        helper.setText(R.id.tv_title, item.goodTitle)
                .setText(R.id.tv_acc_no, String.format(Locale.getDefault(), "商品编号：%s", item.goodCode))
                .setText(R.id.tv_zd_time, String.format(Locale.getDefault(), "%s分钟", item.stickTime))
                .setText(R.id.tv_hot, StringUtils.isEmpty(item.hotValue) ? "0" : item.hotValue)//热度值
                .setText(R.id.tv_zd_price, String.format(Locale.getDefault(), "¥%s", item.stickPrice))
                .setImageResource(R.id.iv_isok, item.isSelect ? R.mipmap.ic_acc_top_item_ok : R.drawable.act_acc_top_not_all);
        ImageView ivLogo = helper.getView(R.id.iv_acc_logo);
        if (ObjectUtils.isEmpty(item.imagePath) || !item.imagePath.contains("http")) {
            item.imagePath = "http:" + item.imagePath;
        }
        GlideApp.with(mContext).load(item.imagePath).transform(new RoundedCorners(8)).into(ivLogo);
    }


}
