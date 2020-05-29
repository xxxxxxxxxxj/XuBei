package com.xubei.shop.ui.module.account.adapter;

import android.widget.ImageView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xubei.shop.R;
import com.xubei.shop.data.network.glide.GlideApp;
import com.xubei.shop.ui.module.account.model.AccBean;

import java.util.List;
import java.util.Locale;

/**
 * 账号列表适配器
 * date：2017/11/30 14:00
 * author：Seraph
 **/
public class AccAdapter extends BaseQuickAdapter<AccBean, BaseViewHolder> {

    private boolean isFree;

    public AccAdapter(List<AccBean> list, boolean isFree) {
        super(R.layout.act_acc_list_item, list);
        this.isFree = isFree;
    }

    @Override
    protected void convert(BaseViewHolder helper, AccBean item) {

        helper.setText(R.id.tv_game_name, item.game_all_name)//游戏区服
                .setText(R.id.tv_goods_title, item.goods_title)//标题
                .setText(R.id.tv_title2, item.search_title)
                .setText(R.id.tv_short_lease, String.format(Locale.getDefault(), "%d小时起租", item.short_lease))//最短租赁时间
                .setGone(R.id.tv_short_lease, !isFree)//最短租赁时间
                .setText(R.id.tv_lease_price, String.format(Locale.getDefault(), "%.2f", item.lease_price))//小时价格
                .setGone(R.id.tv_lease_price, !isFree)//小时价格
                .setText(R.id.tv_deposit, String.format(Locale.getDefault(), "押金%.2f元", item.foregift))//押金
                .setGone(R.id.tv_deposit, !isFree)//押金
                .setGone(R.id.textView8, !isFree) // ¥
                .setGone(R.id.textView16, !isFree)  // /小时
                .setText(R.id.tv_hd, item.actity)//活动
                .setVisible(R.id.tv_hd, !StringUtils.isEmpty(item.actity))
                .setGone(R.id.tv_title2, !StringUtils.isEmpty(item.search_title))
                .setGone(R.id.tv_qy, item.signseller == 1)//是否签约
                .setGone(R.id.tv_rt, "1".equals(item.stickorder))//是否热推
                .setGone(R.id.tv_xx, "1".equals(item.deadLineOnLine))
                .setGone(R.id.tv_free, isFree);//是否免费专区

        ImageView logo = helper.getView(R.id.tv_game_logo);
        if (ObjectUtils.isEmpty(item.imageurl) || !item.imageurl.contains("http")) {
            item.imageurl = "http:" + item.imageurl;
        }
        GlideApp.with(mContext).load(item.imageurl).override(140).centerCrop().transform(new RoundedCorners(8)).into(logo);
    }
}
