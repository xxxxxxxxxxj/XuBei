package com.xubei.shop.ui.module.user.adapter;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xubei.shop.R;
import com.xubei.shop.ui.module.user.model.FreezeDetailBean;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;

/**
 * 冻结资金明细
 * date：2018/10/9 10:26
 * author：xiongj
 **/
public class FreezeDetailAdapter extends BaseQuickAdapter<FreezeDetailBean, BaseViewHolder> {
    private SimpleDateFormat dateFormat;

    public FreezeDetailAdapter(@Nullable List<FreezeDetailBean> data) {
        super(R.layout.act_user_freeze_details_item, data);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd\nHH:mm:ss", Locale.CHINA);
    }

    @Override
    protected void convert(BaseViewHolder helper, FreezeDetailBean item) {
        helper.setText(R.id.tv_detail, item.remarks)
                .setText(R.id.tv_time, TimeUtils.millis2String(item.time, dateFormat))
                .setText(R.id.tv_order, item.orderNo)
                .setText(R.id.tv_amt, String.format(Locale.getDefault(), "%.2f", item.freezeAmount));
    }
}
