package com.haohao.xubei.ui.module.main.adapter;

import android.widget.ImageView;

import com.blankj.utilcode.util.ObjectUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haohao.xubei.R;
import com.haohao.xubei.data.network.glide.GlideApp;
import com.haohao.xubei.ui.module.base.BaseDataCms;
import com.haohao.xubei.ui.module.main.model.WelfareBean;

import javax.inject.Inject;

/**
 * 首页福利中心适配器
 * date：2018/9/28 10:09
 * author：xiongj
 **/
public class HomeWelfareAdapter extends BaseQuickAdapter<BaseDataCms<WelfareBean>, BaseViewHolder> {

    @Inject
    public HomeWelfareAdapter() {
        super(R.layout.act_main_home_game_item_welfare_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, BaseDataCms<WelfareBean> item) {
        ImageView iv = helper.getView(R.id.iv_image);
        if (ObjectUtils.isEmpty(item.properties.location) || !item.properties.location.contains("http")) {
            item.properties.location = "http:" + item.properties.location;
        }
        GlideApp.with(mContext).load(item.properties.location).override(400,200).centerCrop().into(iv);
    }
}
