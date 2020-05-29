package com.xubei.shop.ui.module.common.web;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.CommonActWebBinding;
import com.xubei.shop.di.QualifierType;
import com.xubei.shop.ui.module.base.ABaseActivity;
import com.xubei.shop.ui.module.base.IABaseContract;
import com.xubei.shop.utlis.Tools;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 通用web界面(显示本地Str)
 * date：2018/3/5 19:20
 * author：Seraph
 * mail：417753393@qq.com
 **/
@Route(path = AppConstants.PagePath.COMM_WEBLOCAL)
public class CommonWebLocalActivity extends ABaseActivity<IABaseContract.ABasePresenter> {

    private CommonActWebBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.common_act_web);
    }

    @Override
    protected IABaseContract.ABasePresenter getMVPPresenter() {
        return null;
    }

    @QualifierType("webContent")
    @Inject
    String webContent;

    @QualifierType("title")
    @Inject
    String title;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle(title);
        initLayout();
        if ("公告详情".equals(title)) {
            //去掉超链接
            webContent = Tools.html2Txt(webContent);
        }
        binding.wb.loadDataWithBaseURL("", Tools.setHtmlHeadBody(webContent), "text/html", "UTF-8", "");
    }

    private void initLayout() {
        binding.wb.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    binding.pbWeb.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    binding.pbWeb.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    binding.pbWeb.setProgress(newProgress);//设置进度值
                }
            }
        });
        binding.wb.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        //声明WebSettings子类
        WebSettings webSettings = binding.wb.getSettings();

//如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

//支持插件
        //  webSettings.setPluginsEnabled(true);

//设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

//缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

//其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
        // destory()
        ViewParent parent = binding.wb.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(binding.wb);
        }

        binding.wb.stopLoading();
        // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
        binding.wb.getSettings().setJavaScriptEnabled(false);
        binding.wb.clearHistory();
        binding.wb.removeAllViews();
        try {
            binding.wb.destroy();
        } catch (Throwable ex) {
          ex.printStackTrace();
        }
    }
}
