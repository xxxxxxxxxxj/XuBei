package com.xubei.shop.ui.module.account.adapter;

import android.widget.ImageView;

import com.blankj.utilcode.util.ObjectUtils;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xubei.shop.R;
import com.xubei.shop.data.network.glide.GlideApp;
import com.xubei.shop.ui.module.account.model.UserAccFreeBean;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * 用户分享免费玩的列表适配器
 * date：2019/3/6 11:20
 * author：xiongj
 * mail：417753393@qq.com
 **/
public class AccUserFreeAdapter extends BaseQuickAdapter<UserAccFreeBean, BaseViewHolder> {


    public AccUserFreeAdapter(@Nullable List<UserAccFreeBean> data) {
        super(R.layout.act_user_free_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserAccFreeBean item) {
        helper.setText(R.id.tv_title, item.goodsTitle)
                .setText(R.id.tv_area, item.gameAllName)
                .addOnClickListener(R.id.tv_share);
        ImageView logo = helper.getView(R.id.iv_game_logo);
        if (ObjectUtils.isEmpty(item.imgPath) || !item.imgPath.contains("http")) {
            item.imgPath = "http:" + item.imgPath;
        }
        GlideApp.with(mContext).load(item.imgPath).transform(new RoundedCorners(8)).into(logo);
    }
}
