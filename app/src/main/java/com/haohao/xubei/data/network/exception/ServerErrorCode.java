package com.haohao.xubei.data.network.exception;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.haohao.xubei.AppConstants;

import retrofit2.HttpException;

/**
 * 业务逻辑异常信息处理
 * date：2017/2/17 09:48
 * author：Seraph
 **/
public class ServerErrorCode {

    public ServerErrorCode() {
    }

    /**
     * 根据对应的code进行不同的处理
     */
    private String errorCodeToMessage(ServerErrorException errorCode) {
        //通过view的持有，获取对应的对象进行业务处理
        switch (errorCode.getCode()) {
            case ServerErrorException.CODE_NET_ERR://网络连接失败
            case ServerErrorException.CODE_STATUS_ERR://业务失败
                return "";
            case ServerErrorException.CODE_CLOSE_IMAGE_CODE: //图片验证码关闭
                return String.valueOf(ServerErrorException.CODE_CLOSE_IMAGE_CODE);
            case ServerErrorException.CODE_TOKEN_ERR_401://token 失效
                //清理保存的用户信息
                ARouter.getInstance().build(AppConstants.PagePath.LOGIN_TYPESELECT)
                        .withBoolean("isCleanUser", true).navigation();
                return ServerErrorException.CODE_TOKEN_ERR_401_STR;
        }
        return "";
    }

    /**
     * 显示错误信息
     *
     * @see ServerErrorException 自定义异常可以配合 {@link #errorCodeToMessage(ServerErrorException)} 进行异常处理
     */
    public String errorCodeToMessageShow(Throwable e) {
        String message = null;
        if (e != null) {
            message = e.getMessage();
            if (e instanceof ServerErrorException) { //自定义异常，进行自定义词典进行转义（包含自定义和业务逻辑失败）
                String tempMessage = errorCodeToMessage((ServerErrorException) e);
                //如果返回的信息为""则，使用之前信息
                if (ObjectUtils.isNotEmpty(tempMessage)) {
                    message = tempMessage;
                }
            } else if (e instanceof HttpException) {
                HttpException httpException = (HttpException) e;
                message = "网络异常 " + httpException.code();
            } else { //统一提示网络异常
                if (StringUtils.isEmpty(message)) {
                    message = "网络异常";
                }
            }
            e.printStackTrace();
        }
        return ObjectUtils.isEmpty(message) ? "未知异常" : message;
    }

}
