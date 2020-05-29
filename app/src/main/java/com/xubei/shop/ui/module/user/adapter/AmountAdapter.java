package com.xubei.shop.ui.module.user.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xubei.shop.R;

import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;

/**
 * 金额适配器
 * date：2019/1/9 11:46
 * author：xiongj
 **/
public class AmountAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int selectAmount = 0;

    public AmountAdapter(@Nullable List<String> data) {
        super(R.layout.act_user_pay_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_amount, String.format(Locale.getDefault(), "%s元", item));
        if (helper.getAdapterPosition() == selectAmount) {
            helper.setTextColor(R.id.tv_amount, 0xffffffff)
                    .setBackgroundRes(R.id.tv_amount, R.drawable.act_user_pay_item_select_bg);
        } else {
            helper.setTextColor(R.id.tv_amount, 0xff333333)
                    .setBackgroundRes(R.id.tv_amount, R.drawable.act_user_pay_item_bg);
        }
    }

    public int getSelectAmount() {
        return selectAmount;
    }

    public void setSelectAmount(int selectAmount) {
        this.selectAmount = selectAmount;
        notifyDataSetChanged();
    }
}
