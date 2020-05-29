package com.xubei.shop.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xubei.shop.AppConfig;
import com.xubei.shop.AppConstants;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, AppConfig.getAppId());
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
//        switch (req.getType()) {
//            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
//                goToGetMsg();
//                break;
//            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
//                goToShowMsg((ShowMessageFromWX.Req) req);
//                break;
//            default:
//                break;
//        }
    }

    @Override
    public void onResp(BaseResp resp) {
        String result = null;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                RxBus.get().post(AppConstants.RxBusAction.TAG_WX_PAY, true);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "支付取消";
                RxBus.get().post(AppConstants.RxBusAction.TAG_WX_PAY, false);
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "支付被拒绝";
                RxBus.get().post(AppConstants.RxBusAction.TAG_WX_PAY, false);
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = "支付不支持错误";
                RxBus.get().post(AppConstants.RxBusAction.TAG_WX_PAY, false);
                break;
            default:
                result = "支付返回";
                RxBus.get().post(AppConstants.RxBusAction.TAG_WX_PAY, false);
                break;
        }
        if (ObjectUtils.isNotEmpty(result)) {
            ToastUtils.showShort(result);
        }
        finish();
    }

//    private void goToGetMsg() {
//		Intent intent = new Intent(this, GetFromWXActivity.class);
//		intent.putExtras(getIntent());
//		startActivity(intent);
//		finish();
//    }

//    private void goToShowMsg(ShowMessageFromWX.Req showReq) {
//        WXMediaMessage wxMsg = showReq.message;
//        WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;

//        StringBuffer msg = new StringBuffer();
//        msg.append("description: ");
//        msg.append(wxMsg.description);
//        msg.append("\n");
//        msg.append("extInfo: ");
//        msg.append(obj.extInfo);
//        msg.append("\n");
//        msg.append("filePath: ");
//        msg.append(obj.filePath);

//		Intent intent = new Intent(this, ShowFromWXActivity.class);
//		intent.putExtra(Constants.ShowMsgActivity.STitle, wxMsg.title);
//		intent.putExtra(Constants.ShowMsgActivity.SMessage, msg.toString());
//		intent.putExtra(Constants.ShowMsgActivity.BAThumbData, wxMsg.thumbData);
//		startActivity(intent);
//		finish();
//    }
}