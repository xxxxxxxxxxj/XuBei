package com.haohao.xubei.data.network.service;

import com.haohao.xubei.data.network.FileUploadHelp;
import com.haohao.xubei.ui.module.base.BaseData;
import com.haohao.xubei.ui.module.base.BaseDataResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * 游戏账户相关请求接口
 * date：2017/2/16 11:57
 * author：Seraph
 *
 **/
public interface ApiGoodsService {


    String BASE_URL = "https://good-api.xubei.com/";

    String TEST_URL = "http://good-api.xubei-test.com/";

    String PRD_URL = "http://good-api.xubei-prd.com/";
    /**
     * 通用多文件和字段上传（使用RequestBody方式）
     *
     * @param url         上传地址
     * @param requestBody 请求体，生成对应上传requestBody参见
     *                    {@link FileUploadHelp#multipartRequestBody(Map, List, String)}
     *                    {@link FileUploadHelp#multipartRequestBody(Map, Map)}
     */
    @POST()
    Flowable<BaseDataResponse> multipart(@Url() String url, @Body RequestBody requestBody);

    //上传图片
    @POST("goods-api/arbitration/upTemp?businessNo=xubei_android")
    Flowable<BaseDataResponse<BaseData<String>>> upTemp(@Body RequestBody requestBody);


}
