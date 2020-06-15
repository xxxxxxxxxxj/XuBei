package com.haohao.xubei;

/**
 * 对应项目通用常量
 * date：2017/11/13 10:51
 * author：Seraph
 **/
public class AppConstants {


    public class ARAction {

        //表示此界面需要登录
        public static final int LOGIN = 1;

    }


    /**
     * 页面路径
     */
    public class PagePath {


        //主页
        public static final String APP_MAIN = "/app/main";

        //**欢迎页**
        public static final String WELCOME_GUIDEPAGES = "/welcome/guidePages";
        public static final String WELCOME_HOME = "/welcome/home";


        //**账号**
        public static final String ACC_SRESULT = "/acc/SResult";
        public static final String ACC_AREASELECT = "/acc/areaSelect";
        public static final String ACC_DETAIL = "/acc/detail";
        public static final String ACC_DETAIL_FREE = "/acc/detailFree";
        public static final String ACC_LEASEALLTYPE = "/acc/leaseAllType";
        public static final String ACC_LEASENOTICE = "/acc/leaseNotice";
        public static final String ACC_LIST = "/acc/list";
        public static final String ACC_SC = "/acc/sc";
        public static final String ACC_SEARCH = "/acc/search";
        public static final String ACC_TOP = "/acc/top";
        public static final String ACC_TOPDES = "/acc/topDes";

        public static final String ACC_R_AGREEMENT = "/acc/r/agreement";
        public static final String ACC_R_DETAIL = "/acc/r/detail";
        public static final String ACC_R_EDIT = "/acc/r/edit";
        public static final String ACC_R_LIST = "/acc/r/list";
        public static final String ACC_R_LISTSRESULT = "/acc/r/listSResult";
        public static final String ACC_R_LISTSEARCH = "/acc/r/listSearch";
        public static final String ACC_R_SEARCH = "/acc/r/search";
        public static final String ACC_R_SUCCESS = "/acc/r/success";
        public static final String ACC_RELEASE = "/acc/release";

        public static final String ACC_USER_FREE = "/acc/userFree";


        //**通用**
        public static final String COMM_AGENTWEB = "/comm/agentWeb";
        public static final String COMM_CAPTURE = "/comm/capture";
        public static final String COMM_LOCALIMAGELIST = "/comm/localImageList";
        public static final String COMM_PHOTOPREVIEW = "/comm/photoPreview";
        public static final String COMM_WEBLOCAL = "/comm/webLocal";


        //**维权**
        public static final String RIGHTS_APPLY = "/rights/apply";
        public static final String RIGHTS_DETAIL = "/rights/detail";
        public static final String RIGHTS_PROCESS = "/rights/process";


        //**登录**
        public static final String LOGIN_LOGIN = "/login/login";
        public static final String LOGIN_PHONEBIND = "/login/phoneBind";
        public static final String LOGIN_REGISTERED = "/login/registered";
        public static final String LOGIN_RESETPASSWORD = "/login/resetPassword";
        public static final String LOGIN_RESETPAYPW = "/login/resetPayPw";
        public static final String LOGIN_TYPESELECT = "/login/typeSelect";
        public static final String LOGIN_VERIFYPOPUP = "/login/verifyPopup";


        //**用户**
        public static final String USER_ALIPAYADD = "/user/alipayAdd";
        public static final String USER_ALIPAYMODIFY = "/user/alipayModify";
        public static final String USER_FREEZEDETAILS = "/user/freezeDetails";
        public static final String USER_FUNDDETAILS = "/user/fundDetails";
        public static final String USER_FUNDDETAILS_JB = "/user/jbFundDetails";
        public static final String USER_GETMONEY = "/user/getMoney";
        public static final String USER_INFO = "/user/info";
        public static final String USER_MYMSG = "/user/myMsg";
        public static final String USER_MYPURSE = "/user/myPurse";
        public static final String USER_NOTICE = "/user/notice";
        public static final String USER_PAY = "/user/pay";
        public static final String USER_REDEMPTIONCENTER = "/user/redemptionCenter";
        public static final String USER_REDEMPTIONRECORD = "/user/redemptionRecord";
        public static final String USER_UPDATEBINDPHONE = "/user/updateBindPhone";
        public static final String USER_UPDATENICKNAME = "/user/updateNickName";
        public static final String USER_VERIFIED = "/user/verified";

        //**订单**
        public static final String ORDER_ALL = "/order/all";
        public static final String ORDER_CREATE = "/order/create";
        public static final String ORDER_DETAIL = "/order/detail";
        public static final String ORDER_PAY = "/order/pay";
        public static final String ORDER_SUCCESS = "/order/success";
        public static final String ORDER_RENEW = "/order/renew";
        public static final String ORDER_SEARCH = "/order/search";

        public static final String ORDER_R_DETAIL = "/order/r/detail";

        public static final String ORDER_SELLER_ALL = "/order/seller/all";


        //**设置**
        public static final String SETTING_ABOUT = "/setting/about";
        public static final String SETTING_HELPCENTER = "/setting/helpCenter";
        public static final String SETTING_HELPLIST = "/setting/helpList";
        public static final String SETTING_HOME = "/setting/home";
        public static final String SETTING_NEWMSG = "/setting/newMsg";


    }

    /**
     * 偏好常量
     **/
    public class SPAction {

        public static final String SP_NAME = "xubeishop";

        /**
         * 是否第一次进入APP
         */
        public static final String IS_FIRST = "is_first";

        //是否打开过福利中心
        public static final String IS_OPEN_WELFARE = "IS_OPEN_WELFARE";

        /**
         * 是否打开推送
         */
        public static final String IS_OPEN_PUSH = "is_open_push";
        /**
         * 保存登录用户的账号密码
         */
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";

        //db搜索租号
        public static final String SEARCH_RENT_NO = "search_rent_no";
        //db搜索我的账号
        public static final String SEARCH_MY_ACC = "search_my_acc";

        //自动登录临时使用
        public static final String TEMP_USER_NAME = "temp_user_name";
        public static final String TEMP_PASSWORD = "temp_password";

        //广告相关
        public static final String AD_DATA_ID = "ad_data_id";
        public static final String AD_IMAGE_URL = "ad_image_url";
        public static final String AD_TYPE = "ad_type";
        public static final String AD_GOTO_URL = "ad_goto_url";
        public static final String AD_GAME_ID = "ad_game_id";
        public static final String AD_GAME_TYPE = "ad_game_type";
        public static final String AD_GAME_NAME = "ad_game_name";
    }

    /**
     * RxBus事件总线Tags
     */
    public final class RxBusAction {
        //首页菜单跳转
        public final static String TAG_MAIN_MENU = "MAIN_MENU";
        //首页登录信息更新
        public final static String TAG_LOGIN = "LOGIN";
        //订单详情
        public static final String TAG_ORDER_DETAIL = "ORDER_DETAIL";
        //订单列表
        public static final String TAG_ORDER_LIST = "ORDER_LIST";
        //我的订单出租列表
        public static final String TAG_ORDER_SELLER_LIST = "ORDER_SELLER_LIST";
        //我的账号管理列表
        public static final String TAG_ACCOUNT_SELLER_LIST = "ACCOUNT_SELLER_LIST";
        //我的账号管理列表跳转的type
        public static final String TAG_ACCOUNT_SELLER_LIST_TYPE = "ACCOUNT_SELLER_LIST_TYPE";
        //选择区域
        public static final String TAG_SELECT_AREA = "SELECT_AREA";
        //添加账号
        public static final String TAG_ADD_ALIPAY = "ADD_ALIPAY";
        //账号更新
        public static final String TAG_ACCOUNT_UPDATA = "ACCOUNT_UPDATA";
        //微信支付
        public static final String TAG_WX_PAY = "WX_PAY";
        //我的界面
        public static final String TAG_MAIN_ME = "MAIN_ME";
        public static final String TAG_MAIN_ME2 = "MAIN_ME2";
        public static final String TAG_MAIN_ME3 = "MAIN_ME3";

        //第三方自动登录
        public static final String TAG_OTHER_LOGIN = "OTHER_LOGIN";
    }


    /**
     * 协议对应的网站地址
     */
    public static final class AgreementAction {

        //服务与隐私条款
        public final static String AGREEMENT = "http://app.xubei.com/privacy/index.html";

        //上号器步骤
        public final static String DEVICE_STEPS = "http://app.xubei.com/PrivacyAndServerClause/index.html#/login/one";

    }

    //渠道对应的编号
    public static final class ChannelAction {
        //渠道 0-xubei  1-app_yimi 2-xiaomi 3-vivo 4-应用宝 5-华为 6-网侠

        public static final String SEND_SMS_CHANNEN = "xubei_android";

        public static String getChannelNo(String channelValue) {
            switch (channelValue) {
                case "xubei_android":       //虚贝主站
                    return "0";
                case "APP_yimi":            //推广渠道(薏米)
                    return "1";
                case "xiaomi":              //推广渠道(小米)
                    return "2";
                case "vivo":                //推广渠道（vivo）
                    return "3";
                case "yingyongbao":         //推广渠道(应用宝)
                    return "4";
                case "huawei":              //推广渠道（华为）
                    return "5";
                case "Android_wangxia":     //推广渠道（网侠）
                    return "6";
                default:
                    return "";
            }
        }

    }


}
