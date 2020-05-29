package com.xubei.shop.ui.module.user.adapter;

import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xubei.shop.R;
import com.xubei.shop.ui.module.user.model.FundDetailBean;

import java.util.List;
import java.util.Locale;

/**
 * 资金明细
 * date：2018/10/9 10:26
 * author：xiongj
 **/
public class FundDetailsAdapter extends BaseQuickAdapter<FundDetailBean, BaseViewHolder> {

    public FundDetailsAdapter(@Nullable List<FundDetailBean> data) {
        super(R.layout.act_user_fund_details_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FundDetailBean item) {
        helper.setText(R.id.tv_detail, item.detail)
                .setText(R.id.tv_type, item.type)
                .setText(R.id.tv_time, TimeUtils.millis2String(item.createTime))
                .setText(R.id.tv_order, item.orderNo)
                .setText(R.id.tv_change_amt, String.format(Locale.getDefault(), "%.2f", item.changeAmt))
                .setTextColor(R.id.tv_change_amt, item.cashflow == 0 ? 0xffFF5137 : 0xff33CC64);
        ImageView imageView = helper.getView(R.id.iv_cashflow);
        imageView.setImageResource(item.cashflow == 0 ? R.mipmap.ic_fund_add : R.mipmap.ic_fund_jian);
    }
}
