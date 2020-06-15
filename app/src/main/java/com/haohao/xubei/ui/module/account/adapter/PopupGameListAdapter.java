package com.haohao.xubei.ui.module.account.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haohao.xubei.R;
import com.haohao.xubei.ui.module.account.model.GameBean;

import java.util.List;

/**
 * poplist选择适配器（游戏筛选）
 * date：2018/2/7 09:55
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class PopupGameListAdapter extends BaseQuickAdapter<GameBean, BaseViewHolder> {

    private GameBean selectGame;

    public PopupGameListAdapter(List<GameBean> list) {
        super(R.layout.popup_game_list_select_layout_item, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, GameBean gameBean) {
        boolean isSelect = (selectGame != null && gameBean.game_id.equals(selectGame.game_id));
        helper.setText(R.id.tv_type, gameBean.game)
                .setBackgroundRes(R.id.tv_type, isSelect ? R.drawable.act_game_item_select_bg : R.drawable.act_game_item_bg)
                .setTextColor(R.id.tv_type, isSelect ? 0xffffffff : 0xff666666);
    }

    //设置选中项目
    public void setSelectGame(GameBean selectGame) {
        this.selectGame = selectGame;
        notifyDataSetChanged();
    }
}
