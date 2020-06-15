package com.haohao.xubei.ui.module.account.adapter;

import android.widget.ImageView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haohao.xubei.R;
import com.haohao.xubei.data.network.glide.GlideApp;
import com.haohao.xubei.ui.module.account.model.OutGoodsBean;

import java.util.List;
import java.util.Locale;

/**
 * 我的账号发布
 * date：2017/12/11 18:59
 * author：Seraph
 **/
public class AccRListAdapter extends BaseQuickAdapter<OutGoodsBean, BaseViewHolder> {


    public AccRListAdapter(List<OutGoodsBean> list) {
        super(R.layout.act_acc_r_fag_item, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, OutGoodsBean item) {
        String tempEndTime = item.endTime;
        //如果是置顶商品，则处理时间
        if (item.isStick == 2 && !StringUtils.isEmpty(tempEndTime) && tempEndTime.length() > 16) {
            tempEndTime = tempEndTime.substring(0, 16);
        }
        helper.setText(R.id.tv_order_no, String.format(Locale.getDefault(), "商品编号：%s", item.goodCode))//编号
                .setText(R.id.tv_foregift_amount, String.format(Locale.getDefault(), "¥%.2f", item.foregift))//押金
                .setText(R.id.tv_pay_status, item.goodsStatusText())//商品状态
                .setText(R.id.tv_goods_title, item.goodTitle)//标题
                .setText(R.id.tv_area, item.gameAllName)//区服
                .setText(R.id.tv_lease_hour, ObjectUtils.isEmpty(item.leasePrice) ? "--" : String.format(Locale.getDefault(), "¥%.2f", item.leasePrice))//时租
                .setText(R.id.tv_lease_day, ObjectUtils.isEmpty(item.dayHours) ? "--" : String.format(Locale.getDefault(), "¥%.2f", item.dayHours))//天租
                .setText(R.id.tv_lease_overnight, ObjectUtils.isEmpty(item.tenHours) ? "--" : String.format(Locale.getDefault(), "¥%.2f", item.tenHours))//10小时
                .addOnClickListener(R.id.tv_goods_title)//跳转商品详情
                .addOnClickListener(R.id.iv_image)//跳转商品详情
                .addOnClickListener(R.id.tv_delete)//删除
                .addOnClickListener(R.id.tv_edit)//编辑商品
                .addOnClickListener(R.id.tv_modify)//修改密码
                .addOnClickListener(R.id.tv_shelf)//上架商品
                .addOnClickListener(R.id.tv_obtained)//下架商品
                .addOnClickListener(R.id.tv_update_price)//更新价格
                .setText(R.id.tv_zd_ic, item.isStick == 2 ? "顶" : "排")
                .setText(R.id.tv_zd_msg, item.isStick == 2 ? ("置顶结束时间：" + tempEndTime) : "待置顶（排队中）")
                .setGone(R.id.tv_zd_ic, item.isStick != 0)    //如果是排队或者置顶中，则显示
                .setGone(R.id.tv_zd_msg, item.isStick != 0)
                .setGone(R.id.tv_delete, item.goodsStatus != 3 && item.goodsStatus != 4)//在可租赁和出租中不显示删除
                .setGone(R.id.tv_edit, item.goodsStatus == 1)//在仓库中显示编辑
                .setGone(R.id.tv_shelf, item.goodsStatus == 1)//在仓库中显示上架
                .setGone(R.id.tv_obtained, item.goodsStatus == 3)//在可租赁状态下显示下架
                .setGone(R.id.tv_update_price, item.goodsStatus == 3);//在可租赁（展示中）状态下显示改价
        ImageView ivImage = helper.getView(R.id.iv_image);
        if (ObjectUtils.isEmpty(item.imagePath) || !item.imagePath.contains("http")) {
            item.imagePath = "http:" + item.imagePath;
        }
        GlideApp.with(mContext).load(item.imagePath).override(120).centerCrop().into(ivImage);
    }

}
