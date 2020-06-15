package com.haohao.xubei.ui.module.main.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haohao.xubei.R;
import com.haohao.xubei.ui.module.main.model.GameTypeBean;

import javax.inject.Inject;

/**
 * 游戏分类适配器
 * date：2017/11/29 09:34
 * author：Seraph
 *
 **/
public class GameTypeAdapter extends BaseQuickAdapter<GameTypeBean, BaseViewHolder> {

    private int selectPosition = 0;


    @Inject
    GameTypeAdapter() {
        super(R.layout.act_main_game_type);
    }

    @Override
    protected void convert(BaseViewHolder helper, GameTypeBean item) {
        boolean isSelect = helper.getAdapterPosition() == selectPosition;
        helper.setText(R.id.tv_game_type, item.name)
                .setVisible(R.id.icon_red, isSelect)
                .setBackgroundColor(R.id.cl_item, isSelect ? 0xFFFFFFFF : 0xFFf2f2f2)
                .setTextColor(R.id.tv_game_type, isSelect ? 0xFF0EA8F0 : 0xFF484848)
                .setVisible(R.id.v_line, !isSelect);
    }

    public void onSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }
}
