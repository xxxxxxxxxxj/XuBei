package com.haohao.xubei.ui.module.account.presenter;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.data.network.FileUploadHelp;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.Api8Service;
import com.haohao.xubei.data.network.service.ApiGoodsService;
import com.haohao.xubei.data.network.service.ApiUserNewService;
import com.haohao.xubei.ui.module.account.AccReleaseActivity;
import com.haohao.xubei.ui.module.account.contract.AccReleaseContract;
import com.haohao.xubei.ui.module.account.model.GameActivityBean;
import com.haohao.xubei.ui.module.account.model.GameAreaBean;
import com.haohao.xubei.ui.module.account.model.GameBean;
import com.haohao.xubei.ui.module.account.model.GameConfigBean;
import com.haohao.xubei.ui.module.account.model.GameReleaseAllBean;
import com.haohao.xubei.ui.module.account.model.SaveGoodsBean;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.base.BaseData;
import com.haohao.xubei.ui.module.common.photolist.LocalImageListActivity;
import com.haohao.xubei.ui.module.common.photopreview.PhotoPreviewActivity;
import com.haohao.xubei.ui.views.NoDataView;
import com.haohao.xubei.utlis.TakePhoto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import androidx.appcompat.app.AlertDialog;
import io.reactivex.Flowable;
import okhttp3.RequestBody;

/**
 * 账号发布
 * date：2017/12/5 10:23
 * author：Seraph
 **/
public class AccReleasePresenter extends AccReleaseContract.Presenter {

    private TakePhoto takePhoto;
    private ApiGoodsService apiGoodsService;
    private UserBeanHelp userBeanHelp;
    private ApiUserNewService apiUserNewService;
    private GameBean gameBean;

    private Api8Service api8Service;

    @Inject
    AccReleasePresenter(Api8Service api8Service, ApiGoodsService apiGoodsService, ApiUserNewService apiUserNewService, TakePhoto takePhoto, UserBeanHelp userBeanHelp, GameBean gameBean) {
        this.apiGoodsService = apiGoodsService;
        this.takePhoto = takePhoto;
        this.userBeanHelp = userBeanHelp;
        this.gameBean = gameBean;
        this.api8Service = api8Service;
        this.apiUserNewService = apiUserNewService;
    }

    //获取的游戏界面配置数据模型
    private GameReleaseAllBean gameReleaseBean;

    //需要提交到服务器的值
    //选择的区服
    private GameAreaBean selectAreaBean;
    //选择的优惠活动
    private GameActivityBean gameActivityBean;
    //选择的本地图片
    private ArrayList<String> imageList = new ArrayList<>();


    @Override
    public void start() {
        //设置对应的游戏相关固定参数
        mView.setGameInfo(gameBean);
        //根据游戏获取对应需要传递的参数。来确定布局
        //获取活动相关数据信息
        doGetNetDate();
    }

    //选择大区
    public void doSelectArea() {
        ARouter.getInstance().build(AppConstants.PagePath.ACC_AREASELECT).withString("id", gameBean.game_id).navigation();
    }

    //单选值
    public void doSelectValue(View view, GameConfigBean configBean) {
        KeyboardUtils.hideSoftInput((AccReleaseActivity) mView.getContext());
        //判断如果configBean.defaultVal没有值，则用接口获取
        if (ObjectUtils.isNotEmpty(configBean.defaultVal)) {
            showSelectValueView(view, configBean.defaultVal);
        } else {
            getOptionData(view, configBean.id);
        }
    }

    //复选
    public void doSelectValues(View view, GameConfigBean configBean) {
        KeyboardUtils.hideSoftInput((AccReleaseActivity) mView.getContext());
        //判断如果configBean.defaultVal没有值，则用接口获取
        if (ObjectUtils.isNotEmpty(configBean.defaultVal)) {
            showSelectValuesView(view, configBean.defaultVal);
        } else {
            getOptionData(view, configBean.id);
        }
    }

    //显示选择单选控件
    private void showSelectValueView(View view, String listStr) {
        List<String> list = Arrays.asList(listStr.split("-"));
        OptionsPickerView<String> pickerView = new OptionsPickerBuilder(mView.getContext(), (options1, options2, options3, v) -> ((TextView) view).setText(list.get(options1))).build();
        pickerView.setPicker(list);
        pickerView.show();
    }

    //显示选择多选控件
    private void showSelectValuesView(View view, String listStr) {
        List<String> list = Arrays.asList(listStr.split("-"));
        boolean[] bs = new boolean[list.size()];
        new AlertDialog.Builder(mView.getContext())
                .setTitle("请选择")
                .setMultiChoiceItems(list.toArray(new String[list.size()]), bs, (dialog, which, isChecked) -> bs[which] = isChecked)
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", (dialog, which) -> {
                    //获取值
                    StringBuilder tempSb = new StringBuilder();
                    for (int i = 0; i < bs.length; i++) {
                        //如果选中则添加
                        if (bs[i]) {
                            tempSb.append(list.get(i)).append(";");
                        }
                    }
                    String tempStrs = tempSb.toString();
                    if (ObjectUtils.isNotEmpty(tempStrs)) {
                        tempStrs = tempStrs.substring(0, tempStrs.length() - 1);
                    }
                    ((TextView) view).setText(tempStrs);

                })
                .show();
    }


    //选择优惠活动
    public void doSelectActivity() {
        if (gameReleaseBean == null || gameReleaseBean.gameActivityList == null || gameReleaseBean.gameActivityList.size() == 0) {
            ToastUtils.showShort("暂无优惠活动可选");
            return;
        }
        //关闭输入法
        KeyboardUtils.hideSoftInput((AccReleaseActivity) mView.getContext());
        OptionsPickerView pickerView = new OptionsPickerBuilder(mView.getContext(), (options1, options2, options3, v) -> {
            gameActivityBean = gameReleaseBean.gameActivityList.get(options1);
            mView.setSelectActivity(gameActivityBean.activityRuleName);
        }).build();
        pickerView.setPicker(gameReleaseBean.gameActivityList);
        pickerView.show();
    }

    public void onBackPressed() {
        //确定返回退出
        new AlertDialog.Builder(mView.getContext())
                .setTitle("提示")
                .setMessage("确认放弃发布么")
                .setPositiveButton("确定",
                        (dialog, which) -> mView.finish())
                .setNegativeButton("取消", null)
                .show();
    }


    //设置选择的区服
    public void setSelectArea(GameAreaBean areaBean) {
        this.selectAreaBean = areaBean;
    }


    //移除选择
    public void doImageItemDelete(String path) {
        imageList.remove(path);
    }

    //查看选择的图片
    public void doImageItemClick(int position) {
        PhotoPreviewActivity.startPhotoPreview(imageList, position);
    }

    //拍照
    public void doSelectCamera() {
        takePhoto.doTakePhoto();
    }

    //相册选择
    public void doSelectPhoto() {
        ARouter.getInstance().build(AppConstants.PagePath.COMM_LOCALIMAGELIST)
                .withStringArrayList(LocalImageListActivity.SELECTED_PATH, imageList)
                .navigation((AccReleaseActivity) mView.getContext(), AppConfig.CODE_IMAGE_LIST_REQUEST);
    }

    //拍照完毕，获取拍照文件
    public void onCameraComplete() {
        if (takePhoto.getCurrentPhotoFile() == null) {
            ToastUtils.showShort("拍照异常");
            return;
        }
        //  Uri photoUri = Uri.fromFile(takePhoto.getCurrentPhotoFile());
        imageList.add(takePhoto.getCurrentPhotoFile().getPath());
        mView.setImageList(imageList);
    }

    //选择的照片返回
    public void onLocalImageResult(Intent data) {
        if (data != null && data.getStringArrayListExtra(LocalImageListActivity.SELECT_PATH) != null) {
            imageList.clear();
            imageList.addAll(data.getStringArrayListExtra(LocalImageListActivity.SELECT_PATH));
            mView.setImageList(imageList);
        } else {
            ToastUtils.showShort("无法获取照片");
        }
    }


    //需要提交的参数jsonObject
    private JSONObject jsonObject = new JSONObject();

    //提交
    public void doSubmit(SaveGoodsBean saveGoodsBean) throws JSONException {
        //判断是否登录
        if (!userBeanHelp.isLogin(true)) {
            return;
        }
        //用户id    userId = 1330528     //测试loginToken = ed5ff61d-9f3c-440b-8bbc-45bc5c641c7d
        //   jsonObject.put("loginToken", userBeanHelp.getLoginToken());
        //0 出售 1 出租
        jsonObject.put("tradingWay", 1);
        //游戏id
        jsonObject.put("bigGameId", gameBean.game_id);

        //扩展字段
        JSONObject jsonParams = new JSONObject();

        //1先把固定字段放上去
        //如果网络异常则gameReleaseBean为空
        if (gameReleaseBean == null) {
            ToastUtils.showShort("网络异常");
            return;
        }
        //选择的区服
        if (gameReleaseBean.gameAreaList != null && gameReleaseBean.gameAreaList.size() > 0) {
            if (selectAreaBean == null) {
                ToastUtils.showShort("请选择游戏区服");
                return;
            }
            //末级区服ID
            jsonObject.put("gameId", selectAreaBean.id);
        } else {
            jsonObject.put("gameId", gameBean.game_id);
        }
        //在基础信息里面找是否有需要上传截图的类型，有的话，验证图片
        if (imageList.size() == 0) {
            ToastUtils.showShort("请上传至少一张截图");
            return;
        }
        if (!RegexUtils.isMobileSimple(saveGoodsBean.phone)) {
            ToastUtils.showShort("请输入正确的手机号");
            return;
        }
        jsonObject.put("phone", saveGoodsBean.phone);
        if (ObjectUtils.isEmpty(saveGoodsBean.qq)) {
            ToastUtils.showShort("请填写您的QQ号");
            return;
        }
        jsonObject.put("qq", saveGoodsBean.qq);
        //如果有活动选项并且选择的活动优惠
        if (gameReleaseBean.gameActivityList != null && gameReleaseBean.gameActivityList.size() > 0 && gameActivityBean != null && !"-1".equals(gameActivityBean.id)) {
            jsonObject.put("actity", gameActivityBean.id);
        }

        //动态的属性
        for (HashMap<String, String> cValue : saveGoodsBean.configValues) {
            String key = cValue.get("k");
            String value = cValue.get("v");
            jsonObject.put(key, value);
            //封装一层list
            if (ObjectUtils.isNotEmpty(value)) {
                //如果值有“,”隔开，则拆分
                JSONArray jsonParam = new JSONArray();
                if (value.contains(";")) {
                    String[] vs = value.split(";");
                    for (String v : vs) {
                        jsonParam.put(v);
                    }
                } else {
                    jsonParam.put(value);
                }
                jsonParams.put(key, jsonParam);
            }
        }

        //添加扩展字段
        jsonObject.put("params", jsonParams);

        //如果
        //先开始上传图片
        //清理
        netImages.clear();
        doUploadImage(0);
    }


    //把图片json添加到上传json
    private void addImageJson() throws JSONException {
        //拼装图片json
        JSONArray jsonImageArray = new JSONArray();
        for (String url : netImages) {
            JSONObject jsonImageObject = new JSONObject();
            jsonImageObject.put("location", url);
            //商品图片
            jsonImageObject.put("imgType", 0);
            jsonImageArray.put(jsonImageObject);
        }
        //把图片json放入上传json
        jsonObject.put("imgInfos", jsonImageArray);

    }


    //上传的网络图片列表
    private ArrayList<String> netImages = new ArrayList<>();

    //上传图片接口 i为当前开始上传图片的位置
    private void doUploadImage(final int i) {
        String tempImage = imageList.get(i);
        Map<String, File> fileParams = new HashMap<>();
        fileParams.put("file", new File(tempImage));
        apiGoodsService.upTemp(FileUploadHelp.multipartRequestBody(null, fileParams))
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading("提交信息").setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<BaseData<String>>() {
                    @Override
                    public void onSuccess(BaseData<String> stringBaseData) {
                        //添加网络地址到集合
                        netImages.add(stringBaseData.image_path);
                        if (i >= imageList.size() - 1) {
                            //如果上传的位置到最后一个，则调用提交接口
                            //把对应的图片json添加到上传json
                            try {
                                addImageJson();
                                doSave();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                ToastUtils.showShort("图片上传失败");
                            }
                        } else {
                            doUploadImage(i + 1);//如果没上传完，则递归调用
                        }
                    }

                    @Override
                    public void onError(String errStr) {
                        mView.hideLoading();
                        ToastUtils.showShort(errStr);
                    }
                });
    }


    //发布商品
    private void doSave() {
        RequestBody jsonBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        apiUserNewService.publicGoods(userBeanHelp.getAuthorization(), jsonBody)
                .compose(RxSchedulers.io_main_business())
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<BaseData<Long>>(mView) {
                    @Override
                    public void onSuccess(BaseData<Long> baseData) {
                        ARouter.getInstance().build(AppConstants.PagePath.ACC_R_SUCCESS).navigation();
                        mView.finish();
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }


    //获取对应的选项值
    private void getOptionData(View view, String id) {
        api8Service.optionData(id)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String s) {
                        //调用单选框
                        showSelectValueView(view, s);
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }

    //获取游戏发布配置
    private Flowable<List<GameConfigBean>> doFindGoodsConfig() {
        return api8Service.findGoodsConfig("12", gameBean.game_id)
                .compose(RxSchedulers.io_main_business());
    }

    //获取游戏对应大区
    private Flowable<List<GameAreaBean>> doFindChdByGameParent() {
        return api8Service.findChdByGameParent(Long.valueOf(gameBean.game_id))
                .compose(RxSchedulers.io_main_business());

    }

    //获取活动
    private Flowable<List<GameActivityBean>> doFindGoodsActity() {
        return api8Service.findGoodsActity(gameBean.game_id)
                .compose(RxSchedulers.io_main_business());

    }

    private void doGetNetDate() {
        Flowable.combineLatest(doFindGoodsConfig(), doFindChdByGameParent(), doFindGoodsActity(), (configBeanList, gameAreaBeans, gameActivityBeans) -> {
            GameReleaseAllBean gameReleaseBean = new GameReleaseAllBean();
            //进行数据类型拆分
            for (GameConfigBean bean : configBeanList) {
                switch (bean.moduleType) {//1:基础信息，2：扩展信息，3：交易信息
                    case "0"://基础信息
                        gameReleaseBean.gameConfigList1.add(bean);
                        break;
                    case "1"://扩展信息
                        gameReleaseBean.gameConfigList2.add(bean);
                        break;
                    case "2"://交易信息
                        gameReleaseBean.gameConfigList3.add(bean);
                        break;
                }
            }
            gameReleaseBean.gameAreaList = gameAreaBeans;
            if (gameActivityBeans != null && gameActivityBeans.size() > 0) {
                gameActivityBeans.add(new GameActivityBean("-1", "不参与"));
            }
            gameReleaseBean.gameActivityList = gameActivityBeans;
            return gameReleaseBean;
        }).doOnSubscribe(subscription -> mView.setNoDataView(NoDataView.LOADING))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<GameReleaseAllBean>() {
                    @Override
                    public void onSuccess(GameReleaseAllBean gameReleaseAllBean) {
                        gameReleaseBean = gameReleaseAllBean;
                        //进行数据结构转换成数据模型
                        mView.setLayoutModel(gameReleaseBean);
                        mView.setNoDataView(NoDataView.LOADING_OK);
                    }

                    @Override
                    public void onError(String errStr) {
                        mView.setNoDataView(NoDataView.NET_ERR);
                        ToastUtils.showShort(errStr);
                    }
                });
    }


}
