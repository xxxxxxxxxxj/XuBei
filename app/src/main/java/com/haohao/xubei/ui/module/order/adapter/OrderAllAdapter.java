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
 * 租赁订单适配器
 * date：2017/12/11 18:59
 * author：Seraph
 **/
public class OrderAllAdapter extends BaseQuickAdapter<OutOrderBean, BaseViewHolder> {

    public OrderAllAdapter(List<OutOrderBean> list) {
        super(R.layout.act_order_all_fag_item, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, OutOrderBean item) {
        helper.setText(R.id.tv_product_number, item.gameNo)
                .setText(R.id.tv_pay_status, item.getOrderStatusText())
                .setText(R.id.tv_goods_title, item.goodTitle)
                .setText(R.id.tv_area, item.gameAllName)
                .setText(R.id.tv_price, String.format(Locale.getDefault(), "¥%s", item.orderAllAmount))//总计
                .setText(R.id.tv_last_time, String.format(Locale.getDefault(), "服务结束时间：%s", ObjectUtils.isNotEmpty(item.endTime) ? TimeUtils.millis2String(item.endTime) : "--"))//剩余时间
                .setText(R.id.tv_foregift_amount, String.format(Locale.getDefault(), "¥%s", item.orderForegiftAmount))//押金
                .addOnClickListener(R.id.tv_wq)//申请维权
                .addOnClickListener(R.id.tv_xz)//续租
                .addOnClickListener(R.id.tv_gopay)//去支付
                .addOnClickListener(R.id.tv_r_zy)//再次租用
                .addOnClickListener(R.id.tv_wqjl)//查看维权记录
                .addOnClickListener(R.id.tv_cx_wq)//撤销维权
                .setGone(R.id.tv_gopay, item.isShowPay == 1)//去支付
                .setGone(R.id.tv_xz, item.orderStatus == 2)//租赁中（续租）
                .setGone(R.id.tv_wq, item.isCanArb == 1)  //申请维权
                .setGone(R.id.tv_cx_wq, item.isCancelArb == 1)//撤销维权
                .setGone(R.id.tv_wqjl, item.isShowArb == 1)//查看维权）
                .setGone(R.id.tv_r_zy, item.orderStatus == 100);//交易成功（再次租用）
        ImageView imageView = helper.getView(R.id.iv_image);
        if (ObjectUtils.isEmpty(item.imagePath) || !item.imagePath.contains("http")) {
            item.imagePath = "http:" + item.imagePath;
        }
        GlideApp.with(mContext).load(item.imagePath).override(120).centerCrop().into(imageView);

    }

}
