package com.haohao.xubei.ui.module.main.adapter;

import com.blankj.utilcode.util.ObjectUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haohao.xubei.R;
import com.haohao.xubei.data.network.glide.GlideApp;
import com.haohao.xubei.ui.module.account.model.GameBean;
import com.haohao.xubei.ui.module.main.model.HomeMultipleItem;
import com.haohao.xubei.ui.views.CustomSelfProportionImageView;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * 首页游戏列表适配器
 * date：2017/11/28 13:57
 * author：Seraph
 **/
public class HomeGameAdapter extends BaseMultiItemQuickAdapter<HomeMultipleItem, BaseViewHolder> {


    @Inject
    HomeGameAdapter() {
        super(new ArrayList<>());
        addItemType(HomeMultipleItem.TYPE_ACCOUNT_TYPE, R.layout.act_main_home_game_item_type);
        addItemType(HomeMultipleItem.TYPE_ACCOUNT_LIST, R.layout.act_main_home_game_item_game);
        addItemType(HomeMultipleItem.TYPE_ACCOUNT_MORE, R.layout.act_main_home_game_item_more);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeMultipleItem item) {
        switch (helper.getItemViewType()) {
            case HomeMultipleItem.TYPE_ACCOUNT_TYPE://账号类型
                String typeName = (String) item.date;
                helper.setText(R.id.tv_type_name, typeName);
                break;
            case HomeMultipleItem.TYPE_ACCOUNT_LIST://账号item
                GameBean accountBean = (GameBean) item.date;
                helper.setText(R.id.tv_game_name, accountBean.getGameName());
                CustomSelfProportionImageView iv = helper.getView(R.id.iv_image);
                if (ObjectUtils.isEmpty(accountBean.img_url) || !accountBean.img_url.contains("http")) {
                    accountBean.img_url = "http:" + accountBean.img_url;
                }
                GlideApp.with(mContext).load(accountBean.img_url).override(160).centerCrop().into(iv);
                break;
            case HomeMultipleItem.TYPE_ACCOUNT_MORE://更多游戏
                break;
        }
    }
}
