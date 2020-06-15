package com.haohao.xubei.utlis;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.CommDialogInputSixPasswordBinding;
import com.haohao.xubei.databinding.CommDialogOrderInputSixPasswordBinding;

import java.util.Locale;

import javax.inject.Inject;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 常用的一些弹出框
 * date：2017/5/11 11:35
 * author：Seraph
 **/
public class AlertDialogUtils {


    public interface SelectedTextListener {
        void onSelectedText(String str);
    }

    public interface SelectedItemListener {
        void onSelectedItem(int position);
    }

    public interface InputTextListener {
        void onInputText(String inputText);
    }

    private Activity mContext;

    @Inject
    public AlertDialogUtils(Activity mContext) {
        this.mContext = mContext;
    }

    /**
     * 选择性别
     */
    public void createGenderSelectedDialog(final SelectedTextListener selectedTextListener) {
        View view = View.inflate(mContext, R.layout.common_dialog_gender_selected, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).setView(view).show();

        View.OnClickListener onClickListener = v -> {
            if (selectedTextListener != null) {
                switch (v.getId()) {
                    case R.id.ll_gender_female:
                        selectedTextListener.onSelectedText("女");
                        break;
                    case R.id.ll_gender_male:
                        selectedTextListener.onSelectedText("男");
                        break;
                }
            }
            alertDialog.dismiss();
        };
        view.findViewById(R.id.ll_gender_female).setOnClickListener(onClickListener);
        view.findViewById(R.id.ll_gender_male).setOnClickListener(onClickListener);
    }


    /**
     * 头像选择
     */
    public void createHeadSelectedDialog(View parent, final SelectedItemListener selectedItemListener) {
        createButtonSelectedDialog(parent, selectedItemListener, "选择相册照片", "拍照");
    }

    /**
     * 选择文件（照片），小视频（视频或者照片）
     */
    public void createFileSelectedDialog(View parent, final SelectedItemListener selectedItemListener) {
        View view = View.inflate(mContext, R.layout.common_dialog_file_selected, null);
        final PopupWindow pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setAnimationStyle(R.style.common_pop_head_selected);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setOnDismissListener(() ->
                // 关闭蒙层效果
                Tools.setWindowAlpha(mContext, 1f));
        View.OnClickListener onClickListener = v -> {
            pop.dismiss();
            if (selectedItemListener != null) {
                switch (v.getId()) {
                    case R.id.tv_option0:
                        selectedItemListener.onSelectedItem(0);
                        break;
                    case R.id.tv_option1:
                        selectedItemListener.onSelectedItem(1);
                        break;
                    case R.id.tv_option2:
                        selectedItemListener.onSelectedItem(2);
                        break;
                }
            }
        };
        view.findViewById(R.id.tv_option0).setOnClickListener(onClickListener);
        view.findViewById(R.id.tv_option1).setOnClickListener(onClickListener);
        view.findViewById(R.id.tv_option2).setOnClickListener(onClickListener);
        view.findViewById(R.id.tv_cancel).setOnClickListener(onClickListener);
        if (!pop.isShowing()) {
            pop.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            Tools.setWindowAlpha(mContext, 0.5f);
        }
    }


    private void createButtonSelectedDialog(View parent, final SelectedItemListener selectedItemListener, String btn1Text, String btn2Text) {
        View view = View.inflate(mContext, R.layout.common_dialog_head_selected, null);
        final PopupWindow pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setAnimationStyle(R.style.common_pop_head_selected);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setOnDismissListener(() ->
                // 关闭蒙层效果
                Tools.setWindowAlpha(mContext, 1f));
        View.OnClickListener onClickListener = v -> {
            pop.dismiss();
            if (selectedItemListener != null) {
                switch (v.getId()) {
                    case R.id.tv_option1:
                        selectedItemListener.onSelectedItem(1);
                        break;
                    case R.id.tv_option2:
                        selectedItemListener.onSelectedItem(2);
                        break;
                }
            }
        };
        TextView option1 = view.findViewById(R.id.tv_option1);
        if (!StringUtils.isEmpty(btn1Text)) {
            option1.setText(btn1Text);
        }
        option1.setOnClickListener(onClickListener);
        TextView option2 = view.findViewById(R.id.tv_option2);
        if (!StringUtils.isEmpty(btn2Text)) {
            option2.setText(btn2Text);
        }
        option2.setOnClickListener(onClickListener);
        view.findViewById(R.id.tv_cancel).setOnClickListener(onClickListener);
        if (!pop.isShowing()) {
            pop.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            Tools.setWindowAlpha(mContext, 0.5f);
        }
    }

    /**
     * 修改字段
     */
    public void createUpdateInputDialog(String titleText, String hint, int type, final String oldStr, final InputTextListener inputTextListener) {
        View view = View.inflate(mContext, R.layout.common_dialog_input_updata_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(mContext, R.style.custom_dialog_style)
                .setView(view).show();
        final TextView okText = dialog.findViewById(R.id.tv_ok);
        final EditText editText = dialog.findViewById(R.id.tv_input_content);
        final TextView title = dialog.findViewById(R.id.tv_title);
        if (!Tools.isNull(titleText)) {
            title.setText(titleText);
        }
        editText.setText(oldStr);
        editText.setSelection(oldStr.length());
        editText.setHint(hint);
        if (type != -1) {
            editText.setInputType(type);
        }
        okText.setTextColor(Color.parseColor("#666666"));
        dialog.findViewById(R.id.rv_cancel).setOnClickListener(v -> dialog.dismiss());
        okText.setOnClickListener(v -> {
            if (inputTextListener != null) {
                if (oldStr.equals(editText.getText().toString().trim())) {
                    ToastUtils.showShort("没有修改内容");
                    return;
                }
                if (ObjectUtils.isEmpty(editText.getText().toString().trim())) {
                    ToastUtils.showShort("输入不能为空");
                    return;
                }
                inputTextListener.onInputText(editText.getText().toString().trim());
            }
            dialog.dismiss();
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence sequence, int start, int before, int count) {
                //如果之前输入的笔记和现在一样，点击按钮文字变灰色
                if (oldStr.equals(editText.getText().toString())) {
                    okText.setTextColor(Color.parseColor("#666666"));
                } else {
                    okText.setTextColor(Color.parseColor("#007aff"));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    /**
     * 修改价格
     */
    public void createUpdatePriceDialog(String oldStr, final InputTextListener inputTextListener) {
        View view = View.inflate(mContext, R.layout.dialog_update_price_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(mContext, R.style.custom_dialog_style)
                .setView(view).show();
        final TextView okText = dialog.findViewById(R.id.tv_ok);
        final EditText editText = dialog.findViewById(R.id.tv_input_content);
        editText.setText(oldStr);
        editText.setSelection(oldStr.length());
        okText.setTextColor(0xFF666666);
        //限制只能输入小数点后2位
        editText.setFilters(new InputFilter[]{
                (source, start, end, dest, dstart, dend) -> {
                    String lastInputContent = dest.toString();
                    if (lastInputContent.length() > 6) {
                        return "";
                    }
                    if (lastInputContent.contains(".")) {
                        int index = lastInputContent.indexOf(".");
                        if (dend - index >= 3) {
                            return "";
                        }
                    }
                    return null;
                }
        });

        dialog.findViewById(R.id.iv_cancel).setOnClickListener(v -> dialog.dismiss());
        okText.setOnClickListener(v -> {
            if (inputTextListener != null) {
                if (oldStr.equals(editText.getText().toString().trim())) {
                    ToastUtils.showShort("与原价格相同");
                    return;
                }
                if (ObjectUtils.isEmpty(editText.getText().toString().trim())) {
                    ToastUtils.showShort("请输入商品单价");
                    return;
                }
                inputTextListener.onInputText(editText.getText().toString().trim());
            }
            dialog.dismiss();
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence sequence, int start, int before, int count) {
                //如果之前输入的笔记和现在一样，点击按钮文字变灰色
                if (oldStr.equals(editText.getText().toString())) {
                    okText.setTextColor(0xFF666666);
                } else {
                    okText.setTextColor(0xFF128DE0);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }


    /**
     * 输入笔记对话框
     */
    public void createInputDialog(final String oldStr, String hintStr, final InputTextListener inputTextListener) {
        View view = View.inflate(mContext, R.layout.common_dialog_input_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setView(view).show();
        final EditText editText = dialog.findViewById(R.id.tv_input_content);
        final TextView okText = dialog.findViewById(R.id.tv_ok);
        //回填笔记到输入框
        editText.setText(oldStr);
        editText.setHint(hintStr);
        okText.setTextColor(Color.parseColor("#666666"));
        dialog.findViewById(R.id.rv_cancel).setOnClickListener(v -> dialog.dismiss());
        okText.setOnClickListener(v -> {
            if (inputTextListener != null) {
                if (oldStr.equals(editText.getText().toString().trim())) {
                    ToastUtils.showShort("没有修改内容");
                    return;
                }
                if (ObjectUtils.isEmpty(editText.getText().toString().trim())) {
                    ToastUtils.showShort("输入不能为空");
                    return;
                }
                inputTextListener.onInputText(editText.getText().toString().trim());
            }
            dialog.dismiss();

        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence sequence, int start, int before, int count) {
                //如果之前输入的笔记和现在一样，点击按钮文字变灰色
                if (oldStr.equals(editText.getText().toString())) {
                    okText.setTextColor(Color.parseColor("#666666"));
                } else {
                    okText.setTextColor(Color.parseColor("#007aff"));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    /**
     * 输入密码
     */
    public void createInputPassWordDialog(final InputTextListener inputTextListener) {
        View view = View.inflate(mContext, R.layout.common_dialog_input_password, null);
        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setView(view).show();
        final EditText editText = dialog.findViewById(R.id.tv_input_content);
        dialog.findViewById(R.id.tv_ok).setOnClickListener(v -> {
            if (inputTextListener != null) {
                inputTextListener.onInputText(editText.getText().toString().trim());
            }
            dialog.dismiss();
        });
        dialog.findViewById(R.id.rv_cancel).setOnClickListener(v -> dialog.dismiss());

    }


    /**
     * 选择分享
     */
    public void createShareDialog(View parent, final SelectedTextListener selectedTextListener) {
        View view = View.inflate(mContext, R.layout.common_dialog_share, null);
        final PopupWindow pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setAnimationStyle(R.style.common_pop_head_selected);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        // 关闭蒙层效果
        pop.setOnDismissListener(() -> Tools.setWindowAlpha(mContext, 1f));
        View.OnClickListener onClickListener = v -> {
            pop.dismiss();
            if (selectedTextListener != null) {
                switch (v.getId()) {
                    case R.id.tv_wechat_moments:
                        selectedTextListener.onSelectedText(WechatMoments.NAME);
                        break;
                    case R.id.tv_wechat:
                        selectedTextListener.onSelectedText(Wechat.NAME);
                        break;
                    case R.id.tv_qq:
                        selectedTextListener.onSelectedText(QQ.NAME);
                        break;
                    case R.id.tv_qzone:
                        selectedTextListener.onSelectedText(QZone.NAME);
                        break;
                }
            }
        };
        view.findViewById(R.id.tv_wechat_moments).setOnClickListener(onClickListener);
        view.findViewById(R.id.tv_wechat).setOnClickListener(onClickListener);
        view.findViewById(R.id.tv_qq).setOnClickListener(onClickListener);
        view.findViewById(R.id.tv_qzone).setOnClickListener(onClickListener);
        if (!pop.isShowing()) {
            pop.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            Tools.setWindowAlpha(mContext, 0.5f);
        }


    }

    public interface OnSixPasswordListener {

        void onInputPassWord(String pass);

        void onForgetPassword();
    }

    private PopupWindow inputSixPop;

    /**
     * 支付密码键盘（输入6位数密码）
     */
    public void inputSixPasswordDialog(View view, OnSixPasswordListener listener) {
        CommDialogInputSixPasswordBinding inputSixbinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.comm_dialog_input_six_password, null, false);
        inputSixPop = new PopupWindow(inputSixbinding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        inputSixPop.setAnimationStyle(R.style.common_pop_head_selected);
        inputSixPop.setFocusable(false);
        inputSixPop.setOutsideTouchable(false);
        inputSixbinding.ivClose.setOnClickListener(v -> {
            if (inputSixPop != null && inputSixPop.isShowing()) {
                inputSixPop.dismiss();
            }
        });
        inputSixbinding.tvSix1.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        inputSixbinding.tvSix2.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        inputSixbinding.tvSix3.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        inputSixbinding.tvSix4.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        inputSixbinding.tvSix5.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        inputSixbinding.tvSix6.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        inputSixPop.setOnDismissListener(() -> {
            inputSixbinding.tvSix1.setText("");
            inputSixbinding.tvSix2.setText("");
            inputSixbinding.tvSix3.setText("");
            inputSixbinding.tvSix4.setText("");
            inputSixbinding.tvSix5.setText("");
            inputSixbinding.tvSix6.setText("");
            Tools.setWindowAlpha(mContext, 1f);
        });
        inputSixbinding.tvNo0.setOnClickListener(v -> setInput(inputSixbinding, listener, "0"));
        inputSixbinding.tvNo1.setOnClickListener(v -> setInput(inputSixbinding, listener, "1"));
        inputSixbinding.tvNo2.setOnClickListener(v -> setInput(inputSixbinding, listener, "2"));
        inputSixbinding.tvNo3.setOnClickListener(v -> setInput(inputSixbinding, listener, "3"));
        inputSixbinding.tvNo4.setOnClickListener(v -> setInput(inputSixbinding, listener, "4"));
        inputSixbinding.tvNo5.setOnClickListener(v -> setInput(inputSixbinding, listener, "5"));
        inputSixbinding.tvNo6.setOnClickListener(v -> setInput(inputSixbinding, listener, "6"));
        inputSixbinding.tvNo7.setOnClickListener(v -> setInput(inputSixbinding, listener, "7"));
        inputSixbinding.tvNo8.setOnClickListener(v -> setInput(inputSixbinding, listener, "8"));
        inputSixbinding.tvNo9.setOnClickListener(v -> setInput(inputSixbinding, listener, "9"));
        //移除最后一位
        inputSixbinding.tvNoDelete.setOnClickListener(v -> setInput(inputSixbinding, listener, ""));
        //忘记密码
        inputSixbinding.tvForgetPassword.setOnClickListener(v -> {
            if (listener != null) {
                listener.onForgetPassword();
            }
        });
        if (!inputSixPop.isShowing()) {
            inputSixPop.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            Tools.setWindowAlpha(mContext, 0.5f);
        }
    }

    //关闭当前的支付弹出框
    public void closeInputPayPop() {
        if (inputSixPop != null && inputSixPop.isShowing()) {
            inputSixPop.dismiss();
        }
    }

    private void setInput(CommDialogInputSixPasswordBinding binding, OnSixPasswordListener listener, String str) {
        if (StringUtils.isEmpty(str)) {
            //如果是删除按键，则从最后开始删除
            if (!StringUtils.isEmpty(binding.tvSix6.getText())) {
                binding.tvSix6.setText("");
            } else if (!StringUtils.isEmpty(binding.tvSix5.getText())) {
                binding.tvSix5.setText("");
            } else if (!StringUtils.isEmpty(binding.tvSix4.getText())) {
                binding.tvSix4.setText("");
            } else if (!StringUtils.isEmpty(binding.tvSix3.getText())) {
                binding.tvSix3.setText("");
            } else if (!StringUtils.isEmpty(binding.tvSix2.getText())) {
                binding.tvSix2.setText("");
            } else if (!StringUtils.isEmpty(binding.tvSix1.getText())) {
                binding.tvSix1.setText("");
            }
        } else if (StringUtils.isEmpty(binding.tvSix1.getText())) {
            binding.tvSix1.setText(str);
        } else if (StringUtils.isEmpty(binding.tvSix2.getText())) {
            binding.tvSix2.setText(str);
        } else if (StringUtils.isEmpty(binding.tvSix3.getText())) {
            binding.tvSix3.setText(str);
        } else if (StringUtils.isEmpty(binding.tvSix4.getText())) {
            binding.tvSix4.setText(str);
        } else if (StringUtils.isEmpty(binding.tvSix5.getText())) {
            binding.tvSix5.setText(str);
        } else if (StringUtils.isEmpty(binding.tvSix6.getText())) {
            binding.tvSix6.setText(str);
        }
        if (listener != null) {
            //每次输入完毕，给出输入之后得密码
            listener.onInputPassWord(binding.tvSix1.getText().toString()
                    + binding.tvSix2.getText().toString()
                    + binding.tvSix3.getText().toString()
                    + binding.tvSix4.getText().toString()
                    + binding.tvSix5.getText().toString()
                    + binding.tvSix6.getText().toString());
        }

    }


    private class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }

            public char charAt(int index) {
                return '●'; // This is the important part
            }

            public int length() {
                return mSource.length(); // Return default
            }

            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }

        }

    }

    private PopupWindow inputGoldSixPop;

    /**
     * 金币支付密码输入框
     *
     * @param view
     * @param orderNo   订单号
     * @param timeLong  时间长度
     * @param kyGold    可用金币
     * @param payGoldNo 支付金币数量
     * @param listener
     */
    public void inputGoldSixPasswordDialog(View view, String orderNo, String timeLong, String kyGold, String payGoldNo, OnSixPasswordListener listener) {
        CommDialogOrderInputSixPasswordBinding inputSixbinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.comm_dialog_order_input_six_password, null, false);
        inputGoldSixPop = new PopupWindow(inputSixbinding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        inputGoldSixPop.setAnimationStyle(R.style.common_pop_head_selected);
        inputGoldSixPop.setFocusable(false);
        inputGoldSixPop.setOutsideTouchable(false);
        inputSixbinding.ivClose.setOnClickListener(v -> {
            if (inputGoldSixPop != null && inputGoldSixPop.isShowing()) {
                inputGoldSixPop.dismiss();
            }
        });
        inputSixbinding.tvSix1.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        inputSixbinding.tvSix2.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        inputSixbinding.tvSix3.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        inputSixbinding.tvSix4.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        inputSixbinding.tvSix5.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        inputSixbinding.tvSix6.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        inputGoldSixPop.setOnDismissListener(() -> {
            inputSixbinding.tvSix1.setText("");
            inputSixbinding.tvSix2.setText("");
            inputSixbinding.tvSix3.setText("");
            inputSixbinding.tvSix4.setText("");
            inputSixbinding.tvSix5.setText("");
            inputSixbinding.tvSix6.setText("");
            Tools.setWindowAlpha(mContext, 1f);
        });
        inputSixbinding.tvNo0.setOnClickListener(v -> setGoldInput(inputSixbinding, listener, "0"));
        inputSixbinding.tvNo1.setOnClickListener(v -> setGoldInput(inputSixbinding, listener, "1"));
        inputSixbinding.tvNo2.setOnClickListener(v -> setGoldInput(inputSixbinding, listener, "2"));
        inputSixbinding.tvNo3.setOnClickListener(v -> setGoldInput(inputSixbinding, listener, "3"));
        inputSixbinding.tvNo4.setOnClickListener(v -> setGoldInput(inputSixbinding, listener, "4"));
        inputSixbinding.tvNo5.setOnClickListener(v -> setGoldInput(inputSixbinding, listener, "5"));
        inputSixbinding.tvNo6.setOnClickListener(v -> setGoldInput(inputSixbinding, listener, "6"));
        inputSixbinding.tvNo7.setOnClickListener(v -> setGoldInput(inputSixbinding, listener, "7"));
        inputSixbinding.tvNo8.setOnClickListener(v -> setGoldInput(inputSixbinding, listener, "8"));
        inputSixbinding.tvNo9.setOnClickListener(v -> setGoldInput(inputSixbinding, listener, "9"));
        //移除最后一位
        inputSixbinding.tvNoDelete.setOnClickListener(v -> setGoldInput(inputSixbinding, listener, ""));

        inputSixbinding.tvOrderNo.setText(orderNo);
        inputSixbinding.tvTime.setText(timeLong);
        inputSixbinding.tvPayType.setText(String.format(Locale.getDefault(), "金币支付(可用金币%s)", kyGold));
        inputSixbinding.tvPayGold.setText(payGoldNo);


        if (!inputGoldSixPop.isShowing()) {
            inputGoldSixPop.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            Tools.setWindowAlpha(mContext, 0.5f);
        }
    }

    //关闭当前金币支付弹出框
    public void closeGoldInputPayPop() {
        if (inputGoldSixPop != null && inputGoldSixPop.isShowing()) {
            inputGoldSixPop.dismiss();
        }
    }

    private void setGoldInput(CommDialogOrderInputSixPasswordBinding binding, OnSixPasswordListener listener, String str) {
        if (StringUtils.isEmpty(str)) {
            //如果是删除按键，则从最后开始删除
            if (!StringUtils.isEmpty(binding.tvSix6.getText())) {
                binding.tvSix6.setText("");
            } else if (!StringUtils.isEmpty(binding.tvSix5.getText())) {
                binding.tvSix5.setText("");
            } else if (!StringUtils.isEmpty(binding.tvSix4.getText())) {
                binding.tvSix4.setText("");
            } else if (!StringUtils.isEmpty(binding.tvSix3.getText())) {
                binding.tvSix3.setText("");
            } else if (!StringUtils.isEmpty(binding.tvSix2.getText())) {
                binding.tvSix2.setText("");
            } else if (!StringUtils.isEmpty(binding.tvSix1.getText())) {
                binding.tvSix1.setText("");
            }
        } else if (StringUtils.isEmpty(binding.tvSix1.getText())) {
            binding.tvSix1.setText(str);
        } else if (StringUtils.isEmpty(binding.tvSix2.getText())) {
            binding.tvSix2.setText(str);
        } else if (StringUtils.isEmpty(binding.tvSix3.getText())) {
            binding.tvSix3.setText(str);
        } else if (StringUtils.isEmpty(binding.tvSix4.getText())) {
            binding.tvSix4.setText(str);
        } else if (StringUtils.isEmpty(binding.tvSix5.getText())) {
            binding.tvSix5.setText(str);
        } else if (StringUtils.isEmpty(binding.tvSix6.getText())) {
            binding.tvSix6.setText(str);
        }
        if (listener != null) {
            //每次输入完毕，给出输入之后得密码
            listener.onInputPassWord(binding.tvSix1.getText().toString()
                    + binding.tvSix2.getText().toString()
                    + binding.tvSix3.getText().toString()
                    + binding.tvSix4.getText().toString()
                    + binding.tvSix5.getText().toString()
                    + binding.tvSix6.getText().toString());
        }

    }
}