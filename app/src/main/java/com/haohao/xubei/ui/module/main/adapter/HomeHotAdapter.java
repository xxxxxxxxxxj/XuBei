package com.haohao.xubei.ui.module.main.adapter;

import com.blankj.utilcode.util.ObjectUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haohao.xubei.R;
import com.haohao.xubei.data.network.glide.GlideApp;
import com.haohao.xubei.ui.module.account.model.AccBean;
import com.haohao.xubei.ui.module.base.BaseDataCms;
import com.haohao.xubei.ui.views.CustomSelfProportionImageView;

import javax.inject.Inject;

/**
 * 首页热门商品适配器
 * date：2018/9/28 10:09
 * author：xiongj
 **/
public class HomeHotAdapter extends BaseQuickAdapter<BaseDataCms<AccBean>, BaseViewHolder> {

    @Inject
    public HomeHotAdapter() {
        super(R.layout.act_main_home_game_item_hot_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, BaseDataCms<AccBean> item) {
        helper.setText(R.id.tv_name, item.properties.big_game_name);
        CustomSelfProportionImageView iv = helper.getView(R.id.iv_image);
        if (ObjectUtils.isEmpty(item.properties.imageurl) || !item.properties.imageurl.contains("http")) {
            item.properties.imageurl = "http:" + item.properties.imageurl;
        }
        GlideApp.with(mContext).load(item.properties.imageurl).override(280, 160).centerCrop().into(iv);
    }
}
