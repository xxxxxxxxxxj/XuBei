package com.haohao.xubei.ui.module.account.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haohao.xubei.R;
import com.haohao.xubei.ui.module.account.model.GameAllAreaBean;

import javax.inject.Inject;

/**
 * 服务器筛选适配器（服务器）
 * date：2018/3/5 11:10
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class PopupServerAdapter extends BaseQuickAdapter<GameAllAreaBean, BaseViewHolder> {


    @Inject
    PopupServerAdapter() {
        super(R.layout.pop_server);
    }

    @Override
    protected void convert(BaseViewHolder helper, GameAllAreaBean item) {
        helper.setText(R.id.tv_game_srever, item.gameName);
    }
}
