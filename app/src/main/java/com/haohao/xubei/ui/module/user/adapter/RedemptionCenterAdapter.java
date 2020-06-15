package com.haohao.xubei.ui.module.user.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haohao.xubei.R;
import com.haohao.xubei.ui.module.user.model.RedemptionCenterBean;

import java.util.List;
import java.util.Locale;

/**
 * 兑换中心适配器
 * date：2018/12/5 18:25
 * author：xiongj
 **/
public class RedemptionCenterAdapter extends BaseQuickAdapter<RedemptionCenterBean, BaseViewHolder> {

    public RedemptionCenterAdapter(List<RedemptionCenterBean> list) {
        super(R.layout.act_redemption_center_item, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, RedemptionCenterBean item) {
        int tempResId = -1;
        String tempTitle = "";
        if (item.userSource == 1) {
            tempResId = R.mipmap.act_redemption_center_item_type_yx;
            tempTitle = "兑换礼品包含：绝地求生、英雄联盟、穿越火线、QQ飞车...";
        } else if (item.userSource == 2) {
            tempResId = R.mipmap.act_redemption_center_item_type_ys;
            tempTitle = "兑换礼品包含：优酷、爱奇艺、乐视、腾讯视频、芒果TV...";
        }
        String tempDH = "";
        int tempDHColour = -1;
        int tempDHBgResId = -1;
        if (item.status == 1) {
            tempDH = "未兑换";
            tempDHColour = 0xFFEF6800;
            tempDHBgResId = R.drawable.act_redemption_center_item_btn_bg_c;
        } else if (item.status == 2) {
            tempDH = "已兑换";
            tempDHColour = 0xFFc6c6c6;
            tempDHBgResId = R.drawable.act_redemption_center_item_btn_bg_h;
        } else if (item.status == 3) {
            tempDH = "已失效";
            tempDHColour = 0xFFc6c6c6;
            tempDHBgResId = R.drawable.act_redemption_center_item_btn_bg_h;
        }
        String tempEndTime = item.termOfValidity;
        if (tempEndTime.length() >= 16) {
            tempEndTime = tempEndTime.substring(0, 16);
        }

        helper.setBackgroundRes(R.id.iv_type_icon, tempResId)
                .setText(R.id.tv_title, tempTitle)
                .setText(R.id.tv_end_time, String.format(Locale.getDefault(), "有效期至：%s", tempEndTime))
                .setText(R.id.tv_dh, tempDH)
                .setTextColor(R.id.tv_dh, tempDHColour)
                .setBackgroundRes(R.id.tv_dh, tempDHBgResId);
    }
}
