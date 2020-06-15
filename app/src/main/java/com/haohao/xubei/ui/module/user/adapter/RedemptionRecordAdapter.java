package com.haohao.xubei.ui.module.user.adapter;

import android.widget.ImageView;

import com.blankj.utilcode.util.ObjectUtils;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haohao.xubei.R;
import com.haohao.xubei.data.network.glide.GlideApp;
import com.haohao.xubei.ui.module.user.model.RedemptionRecordBean;

import java.util.List;
import java.util.Locale;

/**
 * 兑换记录适配器
 * date：2018/12/5 18:25
 * author：xiongj
 **/
public class RedemptionRecordAdapter extends BaseQuickAdapter<RedemptionRecordBean, BaseViewHolder> {

    public RedemptionRecordAdapter(List<RedemptionRecordBean> list) {
        super(R.layout.act_redemption_record_item, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, RedemptionRecordBean item) {
        boolean isPhone = "2".equals(item.loginGameMode);
        String tempStartTime = item.gameBeginTime;
        String tempEndTime = item.gameEndTime;
        if (tempStartTime.length() > 16) {
            tempStartTime = tempStartTime.substring(0, 19);
        }
        if (tempEndTime.length() > 16) {
            tempEndTime = tempEndTime.substring(0, 19);
        }

        helper.setGone(R.id.ll_login_code, !isPhone)
                .setGone(R.id.ll_acc, isPhone)
                .setGone(R.id.ll_pw, isPhone)
                .setGone(R.id.ll_scan, !isPhone)
                .setVisible(R.id.tv_ty_type, isPhone)
                .setVisible(R.id.tv_ty_type_text, isPhone)
                .setVisible(R.id.tv_ty_type_zh, isPhone)
                .setText(R.id.tv_login_code, item.exchangeOrderNo == null ? "" : item.exchangeOrderNo)
                .setText(R.id.tv_acc, item.gameAccount == null ? "" : item.gameAccount)
                .setText(R.id.tv_pw, item.gamePwd == null ? "" : item.gamePwd)
                .addOnClickListener(R.id.tv_login_code_copy)
                .addOnClickListener(R.id.tv_acc_copy)
                .addOnClickListener(R.id.tv_pw_copy)
                .addOnClickListener(R.id.tv_scan)
                .addOnClickListener(R.id.tv_ty)
                .setText(R.id.tv_title, item.goodTitle)
                .setText(R.id.tv_acc_area, item.exchangeType == null ? "" : item.exchangeType)
                .setText(R.id.tv_time_all, String.format(Locale.getDefault(), "试玩时间：%s—%s", tempStartTime, tempEndTime));
        ImageView ivLogo = helper.getView(R.id.iv_game_logo);
        if (ObjectUtils.isEmpty(item.goodImg) || !item.goodImg.contains("http")) {
            item.goodImg = "http:" + item.goodImg;
        }
        GlideApp.with(mContext).load(item.goodImg).transform(new RoundedCorners(8)).into(ivLogo);

    }
}
