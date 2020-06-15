package com.haohao.xubei.ui.module.rights;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActRightsProcessSellerBinding;
import com.haohao.xubei.ui.module.base.ABaseActivity;
import com.haohao.xubei.ui.module.common.photopreview.PhotoPreviewActivity;
import com.haohao.xubei.ui.module.common.photopreview.PhotoPreviewBean;
import com.haohao.xubei.ui.module.rights.contract.RightsProcessSellerContract;
import com.haohao.xubei.ui.module.rights.model.LeaseeArbBean;
import com.haohao.xubei.ui.module.rights.presenter.RightsProcessSellerPresenter;
import com.haohao.xubei.utlis.AlertDialogUtils;
import com.haohao.xubei.utlis.TakePhoto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;


/**
 * 维权处理
 * date：2017/12/13 09:54
 * author：Seraph
 * mail：417753393@qq.com
 **/
@Route(path = AppConstants.PagePath.RIGHTS_PROCESS)
public class RightsProcessSellerActivity extends ABaseActivity<RightsProcessSellerContract.Presenter> implements RightsProcessSellerContract.View {

    private ActRightsProcessSellerBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_rights_process_seller);
        binding.setActivity(this);
    }

    @Inject
    RightsProcessSellerPresenter presenter;

    @Override
    protected RightsProcessSellerContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Inject
    AlertDialogUtils dialogUtils;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("维权处理");
        presenter.start();
    }


    @Override
    public void setLeaseeArbBean(LeaseeArbBean leaseeArbBean) {
        if (leaseeArbBean.outOrderLeaseRightList == null || leaseeArbBean.outOrderLeaseRightList.size() == 0) {
            ToastUtils.showShort("暂无查询到维权信息");
            finish();
            return;
        }
        //最近一笔
        LeaseeArbBean.OutOrderLeaseRight outOrderLeaseRight = leaseeArbBean.outOrderLeaseRightList.get(0);
        //最近一比维权的状态进度
        if (outOrderLeaseRight.rightStatus == 0) {
            binding.rapv.setProgress(2);
        } else {
            binding.rapv.setProgress(3);
        }
        //维权方
        binding.rightsDetailItem.tvRightSource.setText(outOrderLeaseRight.rightSource());
        //维权类型
        binding.rightsDetailItem.tvType.setText(outOrderLeaseRight.rightReasonDesc);
        //维权时间
        binding.rightsDetailItem.tvTime.setText(outOrderLeaseRight.createTime);
        //希望退款金额
        binding.rightsDetailItem.tvWantRefundAmount.setText(String.format(Locale.getDefault(), "¥%s", outOrderLeaseRight.wantRefundAmount));
        //维权说明
        binding.rightsDetailItem.tvRightExplain.setText(outOrderLeaseRight.rightExplain);
        //维权状态
        binding.rightsDetailItem.tvRightStatus.setText(outOrderLeaseRight.rightStatusText());
        if (outOrderLeaseRight.imgUrls != null) {
            binding.rightsDetailItem.wbgvRightsImages.setPhotoAdapter(outOrderLeaseRight.imgUrls, (parent, view, position, id) -> {
                ArrayList<PhotoPreviewBean> list = new ArrayList<>();
                for (String imgUrl : outOrderLeaseRight.imgUrls) {
                    PhotoPreviewBean previewBean = new PhotoPreviewBean();
                    previewBean.objURL = imgUrl;
                    list.add(previewBean);
                }
                PhotoPreviewActivity.startPhotoPreview(list, position);
            });
        }
        //根据接口是否处理过维权来控制按钮
        if ("1".equals(outOrderLeaseRight.isAgreeArb)) {
            binding.tvOk.setVisibility(View.VISIBLE);
        } else {
            binding.tvOk.setVisibility(View.GONE);
        }
        //显示不同意维权
        if ("1".equals(outOrderLeaseRight.isNoAgreeArb)) {
            binding.tvNoAdd.setText("不同意维权/添加凭证");
        } else {
            binding.tvNoAdd.setText("添加凭证");
        }
    }

    //设置图片
    @Override
    public void setImageList(List<String> imageList) {
        binding.civgImages.setItemPaths(imageList);
        //滚动到底部
        binding.civgImages.post(() -> binding.nsv.fullScroll(ScrollView.FOCUS_DOWN));
    }


    //点击事件
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_ok://同意维权
                presenter.doAggreOk();
                break;
            case R.id.tv_update_pw://更新密码
                presenter.changePassword();
                break;
            case R.id.tv_no_add://不同意，添加凭证
                initAddLayout();
                break;
            case R.id.tv_submit://凭证提交
                presenter.doSubmit();
                break;
        }
    }

    //初始化添加凭证界面
    private void initAddLayout() {
        binding.llAddTitle.setVisibility(View.VISIBLE);
        binding.civgImages.setVisibility(View.VISIBLE);
        binding.tvSubmit.setVisibility(View.VISIBLE);
        //向下滚动到底部
        binding.nsv.post(() -> binding.nsv.fullScroll(ScrollView.FOCUS_DOWN));
        //设置功能
        //查看图片监听
        binding.civgImages.setOnClickPicListener((v, position) -> {
            switch ((int) v.getTag()) {
                case 0://添加照片
                    dialogUtils.createHeadSelectedDialog(binding.civgImages, position2 -> {
                        switch (position2) {
                            case 1://相册
                                presenter.doSelectPhoto();
                                break;
                            case 2://拍照
                                presenter.doSelectCamera();
                                break;
                        }
                    });
                    break;
                default://点击照片
                    presenter.doImageItemClick(position);
                    break;
            }
        });
        //删除监听
        binding.civgImages.setOnContentChangeListener(path -> presenter.doImageItemDelete(path));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case TakePhoto.CAMERA_WITH_DATA://相机拍照返回
                    presenter.onCameraComplete();
                    break;
                case AppConfig.CODE_IMAGE_LIST_REQUEST://相册选择
                    presenter.onLocalImageResult(data);
                    break;
            }

        }
    }

}