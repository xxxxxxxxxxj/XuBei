package com.xubei.shop.ui.module.user.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xubei.shop.R;
import com.xubei.shop.ui.module.user.model.RedemptionGameBean;

import java.util.List;

/**
 * 兑换游戏类型适配器
 * date：2018/12/10 16:36
 * author：xiongj
 **/
public class ActRcPopAdapter extends BaseQuickAdapter<RedemptionGameBean, BaseViewHolder> {

    private int selectP = 0;

    public ActRcPopAdapter(List<RedemptionGameBean> list) {
        super(R.layout.act_rc_pop_item, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, RedemptionGameBean item) {
        helper.setText(R.id.tv_title, item.title)
                .setText(R.id.tv_description, item.description)
                .setImageResource(R.id.iv_select, helper.getAdapterPosition() == selectP ? R.mipmap.ic_acc_top_item_ok : R.drawable.act_acc_top_not_all);
    }


    //设置选中的
    public void setSelectP(int selectP) {
        this.selectP = selectP;
    }

    //获取选中的位置

    public int getSelectP() {
        return selectP;
    }
}
