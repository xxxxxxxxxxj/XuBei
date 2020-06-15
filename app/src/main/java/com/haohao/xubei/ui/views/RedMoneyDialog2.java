package com.haohao.xubei.ui.views;

import android.app.Activity;
import android.app.Dialog;
import android.media.AudioManager;
import android.media.SoundPool;
import androidx.annotation.NonNull;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.haohao.xubei.R;

import java.util.Locale;

import javax.inject.Inject;

/**
 * 红包弹出dialog2
 */
public class RedMoneyDialog2 extends Dialog implements View.OnClickListener {

    public final static int TYPE_OPEN_RED = 1;

    public final static int TYPE_LOOK_RED = 2;

    public interface RedMoneyListener {
        /**
         * 1是领取红包点击。2是查看钱包
         */
        void onOpen(int type);
    }

    private RedMoneyListener moneyListener;

    private ViewFlipper viewFlipper;

    private TextView tvMenoy;

    private SoundPool soundPool;

    private int soundId;

    @Inject
    public RedMoneyDialog2(Activity activity) {
        super(activity, R.style.progress_dialog);
        setContentView(R.layout.dialog_red_money2);
        //设置属性
        setCanceledOnTouchOutside(true);
        setCancelable(false);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setWindowAnimations(R.style.dialog_red_money);
        initLayout();
        setOnDismissListener(dialog -> {
            if (soundPool != null) {
                soundPool.release();
            }
        });
        //初始化声音
        soundPool = new SoundPool(100, AudioManager.STREAM_MUSIC, 0);
        soundId = soundPool.load(activity, R.raw.jbdl, 1);
    }

    private void initLayout() {
        viewFlipper = findViewById(R.id.vf);
        findViewById(R.id.iv_close).setOnClickListener(this);
        findViewById(R.id.iv_close_end).setOnClickListener(this);
        findViewById(R.id.tv_look).setOnClickListener(this);
        View vOpen = findViewById(R.id.tv_open);
        tvMenoy = findViewById(R.id.tv_menoy);
        vOpen.setOnClickListener(this);
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
        if (money == -1) {
            return;
        }
        //显示金额
        tvMenoy.setText(String.format(Locale.getDefault(), "%.1f元", money));
        //播放声音，soundID:音频id（这个id来自load的返回值）； left/rightVolume:左右声道(默认1,1)；loop:循环次数(-1无限循环，0代表不循环)；rate:播放速率(1为标准)，该方法会返回一个streamID,如果StreamID为0表示播放失败，否则为播放成功
        soundPool.play(soundId, 1, 1, 1, 0, 1);//播放，得到StreamId
        //切换界面
        viewFlipper.setDisplayedChild(1);
    }

    /**
     * 设置宽高
     */
    public RedMoneyDialog2 setDialogWidthHeight(int width, int height) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = width;
        layoutParams.height = height;
        getWindow().setAttributes(layoutParams);
        return this;
    }

    /**
     * 设置监听
     */
    public RedMoneyDialog2 setOpenClickListener(RedMoneyListener redMoneyListener) {
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
            case R.id.tv_open://打开红包
                if (moneyListener != null) {
                    moneyListener.onOpen(TYPE_OPEN_RED);
                }
                break;
            case R.id.tv_look://查看钱包
                dismiss();
                if (moneyListener != null) {
                    moneyListener.onOpen(TYPE_LOOK_RED);
                }
                break;
        }

    }


}