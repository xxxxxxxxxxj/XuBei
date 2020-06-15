package com.haohao.xubei.utlis;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.haohao.xubei.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AlertDialog;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 通用工具类
 * 一些工具集见：https://github.com/Blankj/AndroidUtilCode
 */
@SuppressLint("SimpleDateFormat")
public class Tools {

    /**
     * 格式化价格(保留小数点后两位)
     */
    public static String getFloatDotStr(String argStr) {
        float arg = Float.valueOf(argStr);
        DecimalFormat fnum = new DecimalFormat("##0.00");
        return fnum.format(arg);
    }


    /**
     * 判断 多个字段的值否为空
     */
    public static boolean isNull(String... ss) {
        for (String s : ss) {
            if (null == s || s.equals("") || s.equalsIgnoreCase("null")) {
                return true;
            }
        }
        return false;
    }


    /**
     * bitmap转文件
     */
    public static void bitmapToFile(Bitmap mBitmap, File file) throws IOException {
        // String url = MediaStore.Images.Media.insertImage(mView.getContext().getContentResolver(), bitmap, "title", "description");

        FileOutputStream outputStream = new FileOutputStream(file);
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        try {
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * 获取相册文件路径
     */
    public static File getDCIMFile(String imageName) {
        //如果sd卡不可用返回null
        if (!SDCardUtils.isSDCardEnableByEnvironment()) {
            return null;
        }
        //获取对应存储照片的文件夹
        File dirs = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), AppConfig.SAVE_IMAGE_FOLDERS_NAME);
        //如果没有此文件夹，并且创建失败，则返回null
        if (!dirs.exists() && !dirs.mkdirs()) {
            return null;
        }
        //获取对应文件
        File file = new File(dirs, imageName);
        try {
            //有此文件，或者没有此文件但是创建成就，则返回对应的文件
            if (file.exists() || file.createNewFile()) {
                return file;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加照片到画廊
     *
     * @param currentPhotoPath 照片路径
     */
    private static void galleryAddPic(Context context, String currentPhotoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }


    /**
     * 扫描相册对应文件
     */
    public static void scanAppImageFile(Context context, String fileName) {
        String photoPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/" + AppConfig.SAVE_IMAGE_FOLDERS_NAME + "/" + fileName;
        galleryAddPic(context, photoPath);
        // context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/" + AppConfig.SAVE_IMAGE_FOLDERS_NAME + "/" + fileName)));
    }


    /**
     * text文本所有关键字变色
     */
    public static CharSequence setColor(String text, String keyWord) {

        if (Tools.isNull(text)) {
            return "";
        }
        if (Tools.isNull(keyWord)) {
            return text;
        }

        // 获取关键字所有的开始下标
        List<Integer> ints = getStart(text, keyWord);

        if (ints.size() == 0) {
            return text;
        }

        SpannableStringBuilder style = new SpannableStringBuilder(text);
        for (int i : ints) {
            style.setSpan(new ForegroundColorSpan(Color.BLUE), i, i + keyWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return style;
    }


    /**
     * 获取开始下标集合
     */
    private static List<Integer> getStart(String text, String keyWord) {
        List<Integer> ints = new ArrayList<>();
        //取反，保证第一次从0开始查找
        int tempStart = ~keyWord.length() + 1;
        do {
            //如果找到了，则更新下次查找位置开始
            tempStart = text.indexOf(keyWord, tempStart + keyWord.length());
            if (tempStart != -1) {
                ints.add(tempStart);
            }
        } while (tempStart != -1);


        return ints;
    }

    /**
     * 百度图片api
     *
     * @param key    关键字
     * @param pageNo 页数
     */
    public static String getBaiduImagesUrl(String key, int pageNo) {

        int pageSize = 48;//每次查询数据的条数
        int start = ((pageNo <= 0 ? 1 : pageNo) - 1) * pageSize; //开始查询的数据

        String base = "search/avatarjson?tn=resultjsonavatarnew&ie=utf-8&word=" + key + "&cg=star&";
        // 生成api
        return base + "pn=" + start + "&rn=" + pageSize + "&itg=0&z=0&fr=&width=&height=&lm=-1&ic=0&s=0&st=-1&gsm=3c";
    }

    /**
     * 原始宽高，根据新宽度等比返回对应的高度
     */
    public static int getNewHeight(int oldWidth, int oldHeight, int newWidth) {
        return newWidth * oldHeight / oldWidth;
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     */
    public static String subZeroAndDot(float s) {
        String tempS = String.valueOf(s);
        if (tempS.indexOf(".") > 0) {
            tempS = tempS.replaceAll("0+?$", "");//去掉多余的0
            tempS = tempS.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return tempS;
    }

    /**
     * 设置蒙层的透明度
     *
     * @param alpha 0-1之间
     */
    public static void setWindowAlpha(Activity activity, float alpha) {
        // 1. 设置半透明主题
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        // 2. 设置window的alpha值 (0.0 - 1.0)
        if (alpha < 0) {
            alpha = 0;
        } else if (alpha > 1) {
            alpha = 1;
        }
        lp.alpha = alpha;
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 判断此str是否只有数字
     */
    public static boolean stringIsNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }


    /**
     * 显示缺失权限提示
     */
    public static void showMissingPermissionDialog(final Context context, final View.OnClickListener onClickListener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("帮助");
        builder.setMessage("缺少必要权限。\n请点击\"设置\"-\"权限\"-打开所需权限。");
        // 拒绝, 退出应用
        builder.setNegativeButton("退出", (dialog, which) -> {
            if (onClickListener != null) {
                onClickListener.onClick(null);
            }
        });
        builder.setPositiveButton("设置", (dialog, which) -> startAppSettings(context));
        builder.show();
    }


    /**
     * 判断是否数字
     */
    public static boolean isNum(String str) {
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    /**
     * 启动应用的设置
     */
    private static void startAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    /**
     * 判断 用户是否安装QQ客户端
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equalsIgnoreCase("com.tencent.qqlite") || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }


    //跳转qq客服
    public static void startQQCustomerService(Context context, String qq) {
        // 跳转之前，可以先判断手机是否安装QQ
        if (Tools.isQQClientAvailable(context)) {
            // 跳转到客服的QQ
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=crm&uin=" + qq)));
            } catch (ActivityNotFoundException ex) {
                ToastUtils.showShort("打开QQ客户端失败");
            }
        } else {
            ToastUtils.showShort("您还未安装QQ客户端");
        }
    }


    /**
     * html适配手机(背景透明，文本字颜色为灰色)
     *
     * @param htmlContent html文本
     */
    public static String setHtmlHeadBody(String htmlContent) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"utf-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\">" +
                "<meta content=\"yes\" name=\"apple-mobile-web-app-capable\">" +
                "<meta content=\"black\" name=\"apple-mobile-web-app-status-bar-style\">" +
                "<meta content=\"telephone=no\" name=\"format-detection\">" +
                "<style type=\"text/css\">" +
                "body { font-family: Arial,\"microsoft yahei\",Verdana; padding:0; margin:0; font-size:13px; color:#666666; background: none; overflow-x:hidden; }" +
                "body,div,fieldset,form,h1,h2,h3,h4,h5,h6,html,p,span { -webkit-text-size-adjust: none}" +
                "h1,h2,h3,h4,h5,h6 { font-weight:normal; }" +
                "applet,dd,div,dl,dt,h1,h2,h3,h4,h5,h6,html,iframe,img,object,p,span {	padding: 0;	margin: 0;	border: none}" +
                "img {padding:0; margin:0; vertical-align:top; border: none}" +
                "li,ul {list-style: none outside none; padding: 0; margin: 0}" +
                "input[type=text],select {-webkit-appearance:none; -moz-appearance: none; margin:0; padding:0; background:none; border:none; font-size:14px; text-indent:3px; font-family: Arial,\"microsoft yahei\",Verdana;}" +
                "body { width:100%; padding:10px; box-sizing:border-box;}" +
                "p { color:#666; line-height:1.6em;} " +
                "img { max-width:100%; width:auto !important; height:auto !important;}" +
                "</style>" +
                "</head>" +
                "<body>" +
                htmlContent +
                "</body>" +
                "</html>";
    }

    /**
     * 去掉html文本里面的超级链接<a></a>
     *
     * @param html 需要处理的html文本
     */
    public static String html2Txt(String html) {
        //去掉超级链接;;
        return html.replaceAll("<a[^>]*>[\\s\\S]*?</a>(?i)", "");
    }

    //去掉所有的空格符号
    public static String html2Txt2(String html) {
        html = html.replaceAll("<head>[\\s\\S]*?</head>(?i)", "");//去掉head
        html = html.replaceAll("<!--[\\s\\S]*?-->", "");//去掉注释
        html = html.replaceAll("<![\\s\\S]*?>", "");
        html = html.replaceAll("<style[^>]*>[\\s\\S]*?</style>(?i)", "");//去掉样式
        html = html.replaceAll("<script[^>]*>[\\s\\S]*?</script>(?i)", "");//去掉js
        html = html.replaceAll("<w:[^>]+>[\\s\\S]*?</w:[^>]+>(?i)", "");//去掉word标签
        html = html.replaceAll("<xml>[\\s\\S]*?</xml>(?i)", "");
        html = html.replaceAll("<html[^>]*>|<body[^>]*>|</html>|</body>(?i)", "");
        html = html.replaceAll("\r\n|\n|\r|\t", "");//去掉换行
        html = html.replaceAll("<br[^>]*>(?i)", "");//去掉换行
        html = html.replaceAll("</p>(?i)", "");//去掉换行
        html = html.replaceAll("<[^>]+>", ""); //去掉所有的<>标签
        html = html.replaceAll("&ldquo;", "\\“");
        html = html.replaceAll("&rdquo;", "\\”");
        html = html.replaceAll("&mdash;", "\\-");
        html = html.replaceAll("&nbsp;", "");
        html = html.replaceAll("&hellip;", "\\...");
        html = html.replaceAll("&middot;", "\\·");
        return html;
    }


    //调用微信支付
    public static void weChatPay(Context context, String payCode) {
        String tempPayJson = payCode.replace("\\", "");
        try {
            JSONObject jsonObject = new JSONObject(tempPayJson);
            IWXAPI api = WXAPIFactory.createWXAPI(context, null);
            PayReq payReq = new PayReq();

            payReq.appId = (String) jsonObject.get("appid");
            api.registerApp(payReq.appId);
            AppConfig.setAppId(payReq.appId);
            payReq.packageValue = (String) jsonObject.get("package");
            payReq.sign = (String) jsonObject.get("sign");
            payReq.partnerId = (String) jsonObject.get("partnerid");
            payReq.prepayId = (String) jsonObject.get("prepayid");
            payReq.nonceStr = (String) jsonObject.get("noncestr");
            payReq.timeStamp = (String) jsonObject.get("timestamp");
            api.sendReq(payReq);
        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtils.showShort("支持参数异常");
        }
    }

    public static void showShare(String platformStr, String title, String remark, String id, String imageUrl, Context context) {
        showShare(platformStr, title, remark, id, imageUrl, context, false, "");
    }

    //一键分享
    public static void showShare(String platformStr, String title, String remark, String id, String imageUrl, Context context, boolean isFree, String spread) {
        OnekeyShare oks = new OnekeyShare();
        if (platformStr != null) {
            oks.setPlatform(platformStr);
        }
        //网页跳转
        String url = "http://m.xubei.com/#/goodsdetail?goodsId=" + id;
        if (isFree) {
            url = "http://list.xubei.com/goods_details?goodsId=" + id + "&isfree=true&spread=" + spread + "&channel=mfzq";
        }
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(title);
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(remark);
        if (ObjectUtils.isNotEmpty(imageUrl)) {
            oks.setImageUrl(imageUrl);
        }
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl(url);
        //site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(title);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);
        if (!isFree) {
            //微信使用小程序
            oks.setShareContentCustomizeCallback((platform, paramsToShare) -> {
                if (Wechat.NAME.equals(platform.getName())) {
                    paramsToShare.setShareType(Platform.SHARE_WXMINIPROGRAM);
                    paramsToShare.setWxUserName("gh_c4fcc354a438");
                    paramsToShare.setWxPath("/pages/product/product_detail/product_detail?goodsId=" + id);
                }
            });
        }

        // 启动分享GUI
        oks.show(context);
    }

    //第三方登录
    public static void doOtherLogin(String platformName, PlatformActionListener platformActionListener) {
        Platform plat = ShareSDK.getPlatform(platformName);
        plat.removeAccount(true); //移除授权状态和本地缓存，下次授权会重新授权
        plat.SSOSetting(false); //SSO授权，传false默认是客户端授权，没有客户端授权或者不支持客户端授权会跳web授权
        plat.setPlatformActionListener(platformActionListener);//授权回调监听，监听oncomplete，onerror，oncancel三种状态
        //判断是否已经存在授权状态，可以根据自己的登录逻辑设置
//        if (plat.isAuthValid()) {
//            // Toast.makeText(this, "已经授权过了", 0).show();
//            return;
//        }
        plat.authorize();    //要数据不要功能，主要体现在不会重复出现授权界面
    }


    //判断app状态  1:程序在前台运行 2:程序在后台运行 3.程序未启动
    public static int getAppStatus() {
        ActivityManager am = (ActivityManager) Utils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        //noinspection ConstantConditions
        List<ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        if (info == null || info.size() == 0) {
            return 3;
        }
        for (ActivityManager.RunningAppProcessInfo aInfo : info) {
            //获取当前程序的的信息
            if (aInfo.processName.equals(Utils.getApp().getPackageName())) {
                //如果当前程序在运行栈中，则判断是否是前台程序 如果是是 返回1 否则2
                if (aInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return 1;
                } else {
                    return 2;
                }
            }
        }
        //如果没找到当前程序则返回3
        return 3;
    }


}