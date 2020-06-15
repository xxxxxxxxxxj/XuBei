package com.haohao.xubei.ui.module.main.adapter;

import android.widget.ImageView;

import com.blankj.utilcode.util.ObjectUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haohao.xubei.R;
import com.haohao.xubei.data.network.glide.GlideApp;
import com.haohao.xubei.ui.module.account.model.GameBean;

import javax.inject.Inject;

/**
 * 游戏名称显示适配器
 * date：2017/11/29 09:34
 * author：Seraph
 *
 **/
public class GameShowAdapter extends BaseQuickAdapter<GameBean, BaseViewHolder> {


    @Inject
    GameShowAdapter() {
        super(R.layout.act_main_game_show);
    }

    @Override
    protected void convert(BaseViewHolder helper, GameBean item) {
        helper.setText(R.id.tv_game_name, item.getGameName());
        ImageView logoView = helper.getView(R.id.iv_logo);
        if (ObjectUtils.isEmpty(item.img_url) || !item.img_url.contains("http")) {
            item.img_url = "http:" + item.img_url;
        }
        GlideApp.with(mContext).load(item.img_url).override(160).centerCrop().into(logoView);
    }
}
