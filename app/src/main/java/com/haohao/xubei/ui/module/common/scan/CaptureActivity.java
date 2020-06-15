package com.haohao.xubei.ui.module.common.scan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.CommonActivityScanCaptureBinding;
import com.haohao.xubei.ui.module.base.ABaseActivity;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;


/**
 * 扫一扫
 */
@Route(path = AppConstants.PagePath.COMM_CAPTURE)
public class CaptureActivity extends ABaseActivity<CapturePresenter> implements CaptureContract.View {

    private CommonActivityScanCaptureBinding binding;

    @Inject
    CapturePresenter mPresenter;

    @Override
    protected CapturePresenter getMVPPresenter() {
        return mPresenter;
    }

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.common_activity_scan_capture);
    }


    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("扫一扫");
        mPresenter.start();
    }

    private void initScanQECodeView() {
        ZXingLibrary.initDisplayOpinion(this);
        //执行扫面Fragment的初始化操作
        CaptureFragment captureFragment = new CaptureFragment();
        //为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.common_activity_scan_capture_camera);
        captureFragment.setAnalyzeCallback(new CodeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                setScanResult(CodeUtils.RESULT_SUCCESS, result);
            }

            @Override
            public void onAnalyzeFailed() {
                setScanResult(CodeUtils.RESULT_FAILED, "");
            }
        });
        //替换我们的扫描控件
        try {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
        } catch (java.lang.IllegalStateException e) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commitAllowingStateLoss();
        }
    }

    private void setScanResult(int type, String result) {
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt(CodeUtils.RESULT_TYPE, type);
        bundle.putString(CodeUtils.RESULT_STRING, result);
        resultIntent.putExtras(bundle);
        setResult(RESULT_OK, resultIntent);
        finish();
    }


    @Override
    public void startScanQECode() {
        initScanQECodeView();
    }

    @Override
    public void closedWin() {
        finish();
    }


}

