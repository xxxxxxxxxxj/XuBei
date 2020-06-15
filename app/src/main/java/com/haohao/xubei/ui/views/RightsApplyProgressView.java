package com.haohao.xubei.ui.views;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActRightsApplySellerTitleProgressBinding;

/**
 * 维权进度View
 * date：2018/8/13 15:50
 * author：Seraph
 *
 **/
public class RightsApplyProgressView extends LinearLayout {

    private ActRightsApplySellerTitleProgressBinding binding;


    private int colorSelect = 0xff118eea;
    private int colorUnSelect = 0xffd6d6d6;

    public RightsApplyProgressView(Context context) {
        this(context, null);
    }

    public RightsApplyProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RightsApplyProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.act_rights_apply_seller_title_progress, this, false);
        addView(binding.getRoot());
    }

    //设置进度 默认1
    public void setProgress(int progress) {
        //先初始化进度
        binding.tvItem2Text1.setTextColor(colorUnSelect);
        binding.tvItem2Text2.setBackgroundResource(R.drawable.act_rights_apply_progress2);
        binding.tvItem2Text3.setTextColor(colorUnSelect);
        binding.tvItem2View.setBackgroundColor(colorUnSelect);
        binding.tvItem3Text1.setTextColor(colorUnSelect);
        binding.tvItem3Text2.setBackgroundResource(R.drawable.act_rights_apply_progress2);
        binding.tvItem3Text3.setTextColor(colorUnSelect);
        binding.tvItem3View.setBackgroundResource(R.drawable.act_rights_apply_progress_bg3);
        switch (progress) {
            case 1:
                break;
            case 2:
                binding.tvItem2Text1.setTextColor(colorSelect);
                binding.tvItem2Text2.setBackgroundResource(R.drawable.act_rights_apply_progress);
                binding.tvItem2Text3.setTextColor(colorSelect);
                binding.tvItem2View.setBackgroundColor(colorSelect);
                break;
            case 3:
                binding.tvItem2Text1.setTextColor(colorSelect);
                binding.tvItem2Text2.setBackgroundResource(R.drawable.act_rights_apply_progress);
                binding.tvItem2Text3.setTextColor(colorSelect);
                binding.tvItem2View.setBackgroundColor(colorSelect);
                binding.tvItem3Text1.setTextColor(colorSelect);
                binding.tvItem3Text2.setBackgroundResource(R.drawable.act_rights_apply_progress);
                binding.tvItem3Text3.setTextColor(colorSelect);
                binding.tvItem3View.setBackgroundResource(R.drawable.act_rights_apply_progress_bg2);
                break;
        }
    }

}
