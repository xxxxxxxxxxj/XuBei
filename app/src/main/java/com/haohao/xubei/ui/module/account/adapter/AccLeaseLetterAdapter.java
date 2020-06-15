package com.haohao.xubei.ui.module.account.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haohao.xubei.R;

import java.util.List;

/**
 * 字母排序
 * date：2018/10/9 17:53
 * author：xiongj
 **/
public class AccLeaseLetterAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int selectLetterPosition;

    public AccLeaseLetterAdapter(List<String> letterList) {
        super(R.layout.act_acc_lease_all_type_letter_item, letterList);

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        int backgroundRes;
        int textColor;
        if (selectLetterPosition == helper.getAdapterPosition()) {
            backgroundRes = R.drawable.act_lease_letter_item_ok_bg;
            textColor = 0xffffffff;
        } else {
            backgroundRes = R.drawable.act_lease_letter_item_unok_bg;
            textColor = 0xff333333;
        }
        helper.setText(R.id.tv_item_letter, item)
                .setBackgroundRes(R.id.tv_item_letter, backgroundRes)
                .setTextColor(R.id.tv_item_letter, textColor);

    }

    //设置字母选中的位置
    public void selectLetterPosition(int selectLetterPosition) {
        this.selectLetterPosition = selectLetterPosition;
        notifyDataSetChanged();
    }
}
