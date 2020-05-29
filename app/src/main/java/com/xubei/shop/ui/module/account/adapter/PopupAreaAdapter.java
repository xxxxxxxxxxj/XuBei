package com.xubei.shop.ui.module.account.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xubei.shop.R;
import com.xubei.shop.ui.module.account.model.GameAllAreaBean;

import javax.inject.Inject;

/**
 * 服务器筛选适配器(大区)
 * date：2018/3/5 11:10
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class PopupAreaAdapter extends BaseQuickAdapter<GameAllAreaBean,BaseViewHolder>{

    private int selectPosition = 0;

    @Inject
    PopupAreaAdapter() {
        super(R.layout.pop_area);
    }

    @Override
    protected void convert(BaseViewHolder helper, GameAllAreaBean item) {
        boolean isSelect = helper.getAdapterPosition() == selectPosition;
        helper.setText(R.id.tv_game_type, item.gameName)
                .setVisible(R.id.icon_red, isSelect)
                .setBackgroundColor(R.id.cl_item, isSelect ? 0xFFFFFFFF : 0xFFf2f2f2)
                .setVisible(R.id.v_line, !isSelect);
    }

    public void onSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }
}
