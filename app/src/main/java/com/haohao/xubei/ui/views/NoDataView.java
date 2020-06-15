package com.haohao.xubei.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.StringUtils;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.CommonNoDataTextBinding;

import javax.inject.Inject;

/**
 * 界面没数据占位符
 * date：2018/4/27 15:01
 * author：Seraph
 **/
public class NoDataView extends LinearLayout implements View.OnClickListener {

    private OnClickListener onClickListener;

    public static final int LOADING = 1;
    public static final int NET_ERR = 2;
    public static final int NO_DATA = 3;
    public static final int LOADING_OK = 4;

    private int noDataResId = -1;

    private CharSequence noDataMsg;

    private CommonNoDataTextBinding binding;

    @Inject
    public NoDataView(Context context) {
        this(context, null);
    }

    public NoDataView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.common_no_data_text, null, false);
        addView(binding.getRoot(), 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }


    @Override
    public void onClick(View v) {
        if (onClickListener != null) {
            onClickListener.onClick(v);
        }
    }

    public NoDataView setNoDataMsg(int noDataResId) {
        return setNoDataMsg(noDataResId, null);
    }

    public NoDataView setNoDataMsg(CharSequence noDataMsg) {
        return setNoDataMsg(-1, noDataMsg);
    }

    /**
     * 设置没有数据的的信息展示
     */
    public NoDataView setNoDataMsg(int noDataResId, CharSequence noDataMsg) {
        this.noDataResId = noDataResId;
        this.noDataMsg = noDataMsg;
        return this;
    }

    /**
     * 设置状态
     */
    public void setType(int type) {
        switch (type) {
            case LOADING:
                binding.ivIcon.setVisibility(GONE);
                binding.tvShowStr.setVisibility(GONE);
                binding.lavLoading.setVisibility(VISIBLE);
                binding.getRoot().setOnClickListener(null);
                break;
            case NET_ERR:
                binding.lavLoading.setVisibility(GONE);
                binding.ivIcon.setVisibility(VISIBLE);
                binding.tvShowStr.setVisibility(VISIBLE);
                binding.tvShowStr.setText("抱歉，你的网络走丢了\n点击重试");
                binding.getRoot().setOnClickListener(this);
                break;
            case NO_DATA:
                binding.ivIcon.setVisibility(VISIBLE);
                binding.tvShowStr.setVisibility(VISIBLE);
                binding.lavLoading.setVisibility(GONE);
                if (noDataResId != -1) {
                    binding.ivIcon.setImageResource(noDataResId);
                }
                binding.tvShowStr.setText(StringUtils.isEmpty(noDataMsg) ? "暂无数据" : noDataMsg);
                binding.getRoot().setOnClickListener(this);
                this.setVisibility(VISIBLE);
                break;
            case LOADING_OK:
                binding.getRoot().setOnClickListener(null);
                this.setVisibility(GONE);
                break;
        }
    }

    /**
     * 点击监听
     */
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
