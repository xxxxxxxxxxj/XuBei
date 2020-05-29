package com.xubei.shop.ui.views;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.media.AudioManager;
import android.media.SoundPool;
import androidx.annotation.NonNull;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.xubei.shop.R;

import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

/**
 * 红包弹出dialog
 */
public class RedMoneyDialog extends Dialog implements View.OnClickListener {

    public interface RedMoneyListener {
        void onOpen();
    }

    private RedMoneyListener moneyListener;

    private ViewFlipper viewFlipper;

    private TextView tvMenoy;

    private SoundPool soundPool;

    private int soundId;

    @Inject
    RedMoneyDialog(Activity activity) {
        super(activity, R.style.progress_dialog);
        setContentView(R.layout.dialog_red_money);
        //设置属性
        setCanceledOnTouchOutside(true);
        setCancelable(false);
        try {
            Objects.requireNonNull(getWindow()).setGravity(Gravity.CENTER);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        getWindow().setWindowAnimations(R.style.dialog_red_money);
        initLayout();
        setOnDismissListener(dialog -> {
            if (rotationAnimator != null) {
                rotationAnimator.end();
            }
            if (soundPool != null) {
                soundPool.release();
            }
        });
        //初始化声音
        soundPool = new SoundPool(100, AudioManager.STREAM_MUSIC, 0);
        soundId = soundPool.load(activity, R.raw.jbdl, 1);
    }

    private ObjectAnimator rotationAnimator;


    private void initLayout() {
        viewFlipper = findViewById(R.id.vf);
        findViewById(R.id.iv_close).setOnClickListener(this);
        findViewById(R.id.iv_close_end).setOnClickListener(this);
        View vOpen = findViewById(R.id.iv_open);
        tvMenoy = findViewById(R.id.tv_menoy);
        vOpen.setOnClickListener(this);
        //旋转动画
        rotationAnimator = ObjectAnimator.ofFloat(vOpen, "rotationY", 0, 180, 0);
        //动画时间
        rotationAnimator.setDuration(800);
        //循环播放
        rotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        //平滑过渡
        rotationAnimator.setInterpolator(new LinearInterpolator());
        rotationAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (moneyListener != null) {
                    moneyListener.onOpen();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                dismiss();
                break;
        }
        return true;
    }


    /**
     * 设置红包金额
     */
    public void setRedValue(double money) {
        //动画播放完毕播放声音
        if (rotationAnimator != null) {
            rotationAnimator.cancel();
        }
        if (money == -1) {
            return;
        }
        //显示金额
        tvMenoy.setText(String.format(Locale.getDefault(), "%.2f元", money));
        //播放声音，soundID:音频id（这个id来自load的返回值）； left/rightVolume:左右声道(默认1,1)；loop:循环次数(-1无限循环，0代表不循环)；rate:播放速率(1为标准)，该方法会返回一个streamID,如果StreamID为0表示播放失败，否则为播放成功
        soundPool.play(soundId, 1, 1, 1, 0, 1);//播放，得到StreamId
        //切换界面
        viewFlipper.setDisplayedChild(1);
    }

    /**
     * 设置宽高
     */
    public RedMoneyDialog setDialogWidthHeight(int width, int height) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = width;
        layoutParams.height = height;
        getWindow().setAttributes(layoutParams);
        return this;
    }

    /**
     * 设置监听
     */
    public RedMoneyDialog setOpenClickListener(RedMoneyListener redMoneyListener) {
        this.moneyListener = redMoneyListener;
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close://关闭
            case R.id.iv_close_end:
                dismiss();
                break;
            case R.id.iv_open://打开红包
                //开始播放动画
                rotationAnimator.start();
                break;
        }

    }


}