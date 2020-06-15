package com.haohao.xubei.ui.module.account.adapter;

import com.blankj.utilcode.util.ObjectUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haohao.xubei.R;
import com.haohao.xubei.data.network.glide.GlideApp;
import com.haohao.xubei.ui.module.account.model.GameBean;
import com.haohao.xubei.ui.views.CircleImageView;

import java.util.List;

/**
 * 租号类型列表
 * date：2018/10/9 17:55
 * author：xiongj
 **/
public class AccLeaseAdapter extends BaseQuickAdapter<GameBean, BaseViewHolder> {

    public AccLeaseAdapter(List<GameBean> gameList) {
        super(R.layout.act_acc_lease_all_type_item, gameList);
    }

    @Override
    protected void convert(BaseViewHolder helper, GameBean item) {
        helper.setText(R.id.tv_name, item.getGameName());
        CircleImageView civ = helper.getView(R.id.civ_game_image);
        if (ObjectUtils.isEmpty(item.img_url) || !item.img_url.contains("http")) {
            item.img_url = "http:" + item.img_url;
        }
        GlideApp.with(mContext).load(item.img_url).override(180).centerCrop().into(civ);
    }
}
