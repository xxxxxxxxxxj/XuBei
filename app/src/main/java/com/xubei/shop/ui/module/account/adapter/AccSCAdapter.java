package com.xubei.shop.ui.module.account.adapter;

import android.widget.ImageView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xubei.shop.R;
import com.xubei.shop.data.network.glide.GlideApp;
import com.xubei.shop.ui.module.account.model.AccSCBean;

import java.util.List;
import java.util.Locale;

/**
 * 收藏适配器
 * date：2018/6/5 10:00
 * author：Seraph
 **/
public class AccSCAdapter extends BaseQuickAdapter<AccSCBean, BaseViewHolder> {


    public AccSCAdapter(List<AccSCBean> list) {
        super(R.layout.act_acc_sc_item, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, AccSCBean item) {
        helper
                //游戏区服
                .setText(R.id.tv_game_name, item.gameAllName == null ? "" : item.gameAllName)
                //标题
                .setText(R.id.tv_goods_title, item.goodsTitle)
                //最短租赁时间
                .setText(R.id.tv_short_lease, String.format(Locale.getDefault(), "%s小时起租", item.shortLease))
                //小时价格
                .setText(R.id.tv_lease_price, String.format(Locale.getDefault(), "%s", item.leasePrice))
                //押金
                .setText(R.id.tv_deposit, String.format(Locale.getDefault(), "押金：%.2f元", item.foregift))
                //活动
                .setVisible(R.id.tv_hd, !StringUtils.isEmpty(item.actity))
                .setText(R.id.tv_hd, item.actity)
                //商品状态
                .setText(R.id.tv_account_type, item.goodsStatusStr())
                .setGone(R.id.tv_xx, "1".equals(item.deadLineOnLine))//到时不下线
                //收藏状态
                .setImageResource(R.id.iv_account_sc, item.isSC ? R.mipmap.btn_collection_selected : R.mipmap.btn_collection_normal)
                .addOnClickListener(R.id.iv_account_sc);
        ImageView logo = helper.getView(R.id.tv_game_logo);
        if (ObjectUtils.isEmpty(item.url) || !item.url.contains("http")) {
            item.url = "http:" + item.url;
        }
        GlideApp.with(mContext).load(item.url).transform(new RoundedCorners(8)).into(logo);
    }
}
