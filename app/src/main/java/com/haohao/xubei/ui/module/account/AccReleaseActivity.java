package com.haohao.xubei.ui.module.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActAccReleaseBinding;
import com.haohao.xubei.ui.module.account.contract.AccReleaseContract;
import com.haohao.xubei.ui.module.account.model.GameAreaBean;
import com.haohao.xubei.ui.module.account.model.GameBean;
import com.haohao.xubei.ui.module.account.model.GameConfigBean;
import com.haohao.xubei.ui.module.account.model.GameReleaseAllBean;
import com.haohao.xubei.ui.module.account.model.SaveGoodsBean;
import com.haohao.xubei.ui.module.account.presenter.AccReleasePresenter;
import com.haohao.xubei.ui.module.base.ABaseActivity;
import com.haohao.xubei.ui.views.addImage.CustomImageViewGroup;
import com.haohao.xubei.utlis.AlertDialogUtils;
import com.haohao.xubei.utlis.TakePhoto;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 账号发布
 * date：2017/12/5 10:14
 * author：Seraph
 * mail：417753393@qq.com
 **/
@Route(path = AppConstants.PagePath.ACC_RELEASE)
public class AccReleaseActivity extends ABaseActivity<AccReleaseContract.Presenter> implements AccReleaseContract.View {


    private ActAccReleaseBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_acc_release);
        binding.setActivity(this);
    }

    @Inject
    AccReleasePresenter presenter;

    @Override
    protected AccReleaseContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Inject
    AlertDialogUtils dialogUtils;

    private LayoutInflater inflater;


    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("填写资料");
        binding.ndv.setOnClickListener(v -> presenter.start());
        presenter.start();
    }

    @Override
    public void setGameInfo(GameBean gameBean) {
        //游戏名称
        binding.tvGameName.setText(gameBean.getGameName());
    }

    //优惠活动
    @Override
    public void setSelectActivity(String strRule) {
        binding.tvActivity.setText(strRule);
    }


    @Override
    public void setNoDataView(int type) {
        binding.ndv.setType(type);
    }


    @Override
    public void setLayoutModel(GameReleaseAllBean allBean) {
        //根据是否有大区数据来显示或者隐藏大区选择
        if (allBean.gameAreaList != null && allBean.gameAreaList.size() > 0) {
            //显示选择大区
            binding.clArea.setVisibility(View.VISIBLE);
        } else {
            binding.clArea.setVisibility(View.GONE);
        }
        //如果有优惠活动，则显示
        if (allBean.gameActivityList != null && allBean.gameActivityList.size() != 0) {
            binding.clActivitys.setVisibility(View.VISIBLE);
        } else {
            binding.clActivitys.setVisibility(View.GONE);
        }
        //后台动态需要的参数
        addLayout(allBean.gameConfigList1, binding.llModuleType1);
        addLayout(allBean.gameConfigList2, binding.llModuleType3);
        addLayout(allBean.gameConfigList3, binding.llModuleType2);
    }

    //图片
    private CustomImageViewGroup images;


    private void addLayout(List<GameConfigBean> configList, LinearLayout linearLayout) {
        int tempSize = configList.size();
        if (tempSize > 0) {//如果有需要的配置，则添加
            if (inflater == null) {
                inflater = LayoutInflater.from(this);
            }
            for (int i = 0; i < tempSize; i++) {
                GameConfigBean configBean = configList.get(i);
                View view;
                switch (configBean.type) {
                    case "1"://文本输入
                        view = inflater.inflate(R.layout.act_acc_release_item_type1, linearLayout, false);
                        break;
                    case "3"://3：下拉框
                    case "4"://4：单选框
                        view = inflater.inflate(R.layout.act_acc_release_item_type0, linearLayout, false);
                        view.findViewById(R.id.tv_value).setOnClickListener(v -> presenter.doSelectValue(v, configBean));
                        break;
                    case "5"://图片上传
                        view = inflater.inflate(R.layout.act_acc_release_item_type5, linearLayout, false);
                        images = view.findViewById(R.id.cvg_add_image);
                        images.setOnContentChangeListener(path -> presenter.doImageItemDelete(path));
                        images.setOnClickPicListener((v, position) -> {
                            //添加或者查看
                            switch ((int) v.getTag()) {
                                case 0://添加
                                    dialogUtils.createHeadSelectedDialog(binding.tvSubmit, position2 -> {
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
                                default://查看
                                    presenter.doImageItemClick(position);
                                    break;
                            }
                        });
                        break;
                    case "6"://密码类型
                        view = inflater.inflate(R.layout.act_acc_release_item_type1, linearLayout, false);
                        break;
                    case "7"://文本域
                        view = inflater.inflate(R.layout.act_acc_release_item_type7, linearLayout, false);
                        break;
                    case "9"://复选框
                        view = inflater.inflate(R.layout.act_acc_release_item_type0, linearLayout, false);
                        view.findViewById(R.id.tv_value).setOnClickListener(v -> presenter.doSelectValues(v, configBean));
                        break;
                    case "10"://输入为数字类型
                        //如果有单位则使用另外的一个布局
                        if (ObjectUtils.isNotEmpty(configBean.unit)) {
                            view = inflater.inflate(R.layout.act_acc_release_item_type10, linearLayout, false);
                            ((TextView) view.findViewById(R.id.tv_unit)).setText(configBean.unit);//单位
                        } else {
                            view = inflater.inflate(R.layout.act_acc_release_item_type1, linearLayout, false);
                            ((EditText) view.findViewById(R.id.tv_value)).setInputType(InputType.TYPE_CLASS_NUMBER);
                        }
                        break;
                    default:
                        view = inflater.inflate(R.layout.act_acc_release_item_type1, linearLayout, false);
                        break;
                }
                ((TextView) view.findViewById(R.id.tv_key)).setText(configBean.fieldName);//属性名
                TextView valueView = view.findViewById(R.id.tv_value);
                if (valueView != null) {
                    valueView.setTag(configBean);//属性名
                    //设置输入最大字符
                    if (configBean.maxLength != null) {
                        valueView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(configBean.maxLength)});
                    }
                    //设置输入框内提示语
                    //如果是必填项，则加上此提示
                    String tempPlaceHolder = configBean.placeHolder;
                    if ("0".equals(configBean.required)) {
                        tempPlaceHolder = tempPlaceHolder + "（必填）";
                    }
                    if (ObjectUtils.isNotEmpty(tempPlaceHolder)) {
                        valueView.setHint(tempPlaceHolder);
                    }
                    //设置输入框外提示
                    if (ObjectUtils.isNotEmpty(configBean.tips) && view.findViewById(R.id.tv_item_directions) != null) {
                        TextView tips = view.findViewById(R.id.tv_item_directions);
                        tips.setText(configBean.tips);
                        tips.setVisibility(View.VISIBLE);
                    }
                }
                linearLayout.addView(view);
            }
        }
    }

    //设置图片
    @Override
    public void setImageList(List<String> imageList) {
        if (imageList != null && images != null) {
            images.setItemPaths(imageList);
        }
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

    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cl_area://选择大区
                presenter.doSelectArea();
                break;
            case R.id.tv_activity://选择优惠活动
                presenter.doSelectActivity();
                break;
            case R.id.tv_agreement://查看发布协议
                ARouter.getInstance().build(AppConstants.PagePath.ACC_R_AGREEMENT).navigation();
                break;
            case R.id.tv_submit://提交
                //判断是否勾选协议
                if (binding.cbAgreement.isChecked()) {
                    ToastUtils.showShort("请勾选确认《虚贝悬赏发布规则及交易协议》");
                    return;
                }
                //开始获取填入的值
                initValues();
                break;
        }
    }

    private SaveGoodsBean saveGoodsBean = new SaveGoodsBean();

    //循环获取布局下，TextView 和 EditText的值(有tag的)
    private void initValues() {
        //手机
        saveGoodsBean.phone = binding.etPhone.getText().toString().trim();
        //qq
        saveGoodsBean.qq = binding.etQq.getText().toString().trim();
        //动态的值
        saveGoodsBean.configValues.clear();
        try {
            getConfigValues(binding.llRootView);
        } catch (Exception e) {
            //清理之前获取的值
            saveGoodsBean.configValues.clear();
            //显示信息
            ToastUtils.showShort(e.getMessage());
            return;
        }
        //提交发布
        try {
            presenter.doSubmit(saveGoodsBean);
        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtils.showShort("发布失败");
        }
    }

    private void getConfigValues(ViewGroup viewGroup) throws Exception {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                getConfigValues((ViewGroup) view);
            } else if ((view instanceof EditText || view instanceof TextView) && view.getTag() != null) {
                GameConfigBean bean = (GameConfigBean) view.getTag();
                //如果是必填，则判断一下是否填值了
                String textStr = ((TextView) view).getText().toString().trim();
                //提交的时候有必填项是没有填的时候，清理一下获取值，显示对应的报错信息
                if ("0".equals(bean.required) && ObjectUtils.isEmpty(textStr)) {
                    //如果是textView则是选择，是EditText 则是输入
                    String excMsg;
                    if (view instanceof EditText) {
                        excMsg = "请输入";
                    } else {
                        excMsg = "请选择";
                    }
                    throw new Exception(excMsg + bean.fieldName);
                }
                //判断一下输入的长度是否在规定的范围内
                if (textStr.length() != 0) {
                    if (bean.minLength != null && textStr.length() < bean.minLength) {
                        throw new Exception(bean.errorMsg);
                    }
                    if (bean.maxLength != null && textStr.length() > bean.maxLength) {
                        throw new Exception(bean.errorMsg);
                    }
                }
                //获取填入的值
                String tempValue = ((TextView) view).getText().toString().trim();
                //如果表达式不为null.则验证表达式。使用表达式校验
                if (ObjectUtils.isNotEmpty(bean.reg) && ObjectUtils.isNotEmpty(tempValue)) {
                    if (tempValue.matches(bean.reg)) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("k", bean.sqlName);
                        map.put("v", tempValue);
                        saveGoodsBean.configValues.add(map);
                    } else {
                        throw new Exception(bean.errorMsg);
                    }
                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("k", bean.sqlName);
                    map.put("v", tempValue);
                    saveGoodsBean.configValues.add(map);
                }
            }
        }
    }


    @Subscribe(tags = {@Tag(AppConstants.RxBusAction.TAG_SELECT_AREA)})
    public void selectArea(GameAreaBean areaBean) {
        presenter.setSelectArea(areaBean);
        binding.tvArea.setText(areaBean.allName);
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed();
    }
}
