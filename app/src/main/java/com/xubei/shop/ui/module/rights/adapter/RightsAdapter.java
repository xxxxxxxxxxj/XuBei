package com.xubei.shop.ui.module.rights.adapter;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xubei.shop.R;
import com.xubei.shop.ui.module.common.photopreview.PhotoPreviewActivity;
import com.xubei.shop.ui.module.common.photopreview.PhotoPreviewBean;
import com.xubei.shop.ui.module.rights.model.LeaseeArbBean;
import com.xubei.shop.ui.views.WeiBoGridView;

import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

/**
 * 维权记录适配器
 * date：2018/8/22 10:09
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class RightsAdapter extends BaseQuickAdapter<LeaseeArbBean.OutOrderLeaseRight, BaseViewHolder> {

    @Inject
    public RightsAdapter() {
        super(R.layout.act_rights_detail_seller_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, LeaseeArbBean.OutOrderLeaseRight item) {
        //判断维权时间的长度如果大于19 则截取   (去掉多余的毫秒数)
        if (!StringUtils.isEmpty(item.createTime) && item.createTime.length() > 19) {
            item.createTime = item.createTime.substring(0, 19);
        }
        helper.setText(R.id.tv_right_source, item.rightSource())//维权方
                .setText(R.id.tv_type, item.rightReasonDesc)//维权类型
                .setText(R.id.tv_time, item.createTime)//维权时间
                .setText(R.id.tv_want_refund_amount, String.format(Locale.getDefault(), "¥%s", item.wantRefundAmount))//希望退款金额
                .setText(R.id.tv_right_explain, item.rightExplain)//维权说明
                .setText(R.id.tv_right_status, item.rightStatusText());//维权状态
        WeiBoGridView weiBoGridView = helper.getView(R.id.wbgv_rights_images);
        if (item.imgUrls != null) {
            weiBoGridView.setPhotoAdapter(item.imgUrls, (parent, view, position, id) -> {
                ArrayList<PhotoPreviewBean> list = new ArrayList<>();
                for (String imgUrl : item.imgUrls) {
                    PhotoPreviewBean previewBean = new PhotoPreviewBean();
                    previewBean.objURL = imgUrl;
                    list.add(previewBean);
                }
                PhotoPreviewActivity.startPhotoPreview(list, position);
            });
        }
    }
}
