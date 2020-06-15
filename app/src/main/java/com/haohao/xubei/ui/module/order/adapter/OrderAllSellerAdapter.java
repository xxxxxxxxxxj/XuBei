package com.haohao.xubei.ui.module.order.adapter;

import android.widget.ImageView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haohao.xubei.R;
import com.haohao.xubei.data.network.glide.GlideApp;
import com.haohao.xubei.ui.module.order.model.OutOrderBean;

import java.util.List;
import java.util.Locale;

/**
 * 出租订单适配器
 * date：2017/12/11 18:59
 * author：Seraph
 *
 **/
public class OrderAllSellerAdapter extends BaseQuickAdapter<OutOrderBean, BaseViewHolder> {

    public OrderAllSellerAdapter(List<OutOrderBean> list) {
        super(R.layout.act_order_all_seller_fag_item, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, OutOrderBean item) {
        //获取订单结束时间
        helper.setText(R.id.tv_product_number, item.gameNo)
                .setText(R.id.tv_pay_status, item.getOrderStatusText())
                .setText(R.id.tv_goods_title, item.goodTitle)
                .setText(R.id.tv_area, item.gameAllName)
                .setText(R.id.tv_price, String.format(Locale.getDefault(), "¥%s", item.orderAllAmount))//总计
                .setText(R.id.tv_last_time, String.format(Locale.getDefault(), "服务结束时间：%s", ObjectUtils.isNotEmpty(item.endTime) ? TimeUtils.millis2String(item.endTime) : "--"))//剩余时间
                .setText(R.id.tv_foregift_amount, String.format(Locale.getDefault(), "¥%s", item.orderForegiftAmount))//押金
                .addOnClickListener(R.id.tv_rights_apply)//申请维权
                .addOnClickListener(R.id.tv_rights_handling)//处理维权
                .addOnClickListener(R.id.tv_rights_look)//查看维权
                //申请维权
                .setGone(R.id.tv_rights_apply, item.isCanArb == 1)
                //处理维权
                .setGone(R.id.tv_rights_handling, item.isDealArb == 1)
                //查看维权
                .setGone(R.id.tv_rights_look, item.isShowArb == 1);
        ImageView imageView = helper.getView(R.id.iv_image);
        if (item.imagePath != null && !item.imagePath.contains("http")) {
            item.imagePath = "http:" + item.imagePath;
        }
        GlideApp.with(mContext).load(item.imagePath).override(120).centerCrop().into(imageView);
    }

}
