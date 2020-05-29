package com.xubei.shop.di.module;

import com.xubei.shop.AppConfig;
import com.xubei.shop.data.network.ApiBuild;
import com.xubei.shop.data.network.service.Api8Service;
import com.xubei.shop.data.network.service.ApiCommonService;
import com.xubei.shop.data.network.service.ApiGoodsService;
import com.xubei.shop.data.network.service.ApiPCService;
import com.xubei.shop.data.network.service.ApiPHPService;
import com.xubei.shop.data.network.service.ApiPassportService;
import com.xubei.shop.data.network.service.ApiUserNewService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * 网络请求module
 * date：2017/5/5 15:18
 * author：Seraph
 **/
@Module
public abstract class ApiServiceModule {

    @Provides
    @Singleton
    static ApiCommonService apiCommonService(ApiBuild apiBuild) {
        String tempHttp = ApiCommonService.BASE_URL;
        switch (AppConfig.BASE_HTTP_IP) {
            case 1:
                tempHttp = ApiCommonService.BASE_URL;
                break;
            case 2:
                tempHttp = ApiCommonService.PRD_URL;
                break;
            case 3:
                tempHttp = ApiCommonService.TEST_URL;
                break;
        }
        return apiBuild.buildApiInterface(tempHttp, ApiCommonService.class);
    }


    @Provides
    @Singleton
    static Api8Service apiCloudCommonService(ApiBuild apiBuild) {
        String tempHttp = Api8Service.BASE_URL;
        switch (AppConfig.BASE_HTTP_IP) {
            case 1:
                tempHttp = Api8Service.BASE_URL;
                break;
            case 2:
                tempHttp = Api8Service.PRD_URL;
                break;
            case 3:
                tempHttp = Api8Service.TEST_URL;
                break;
        }
        return apiBuild.buildApiInterface(tempHttp, Api8Service.class);
    }

    @Provides
    @Singleton
    static ApiPassportService apiPassportService(ApiBuild apiBuild) {
        String tempHttp = ApiPassportService.BASE_URL;
        switch (AppConfig.BASE_HTTP_IP) {
            case 1:
                tempHttp = ApiPassportService.BASE_URL;
                break;
            case 2:
                tempHttp = ApiPassportService.PRD_URL;
                break;
            case 3:
                tempHttp = ApiPassportService.TEST_URL;
                break;
        }
        return apiBuild.buildApiInterface(tempHttp, ApiPassportService.class);
    }

    @Provides
    @Singleton
    static ApiUserNewService apiUserNewService(ApiBuild apiBuild) {
        String tempHttp = ApiUserNewService.BASE_URL;
        switch (AppConfig.BASE_HTTP_IP) {
            case 1:
                tempHttp = ApiUserNewService.BASE_URL;
                break;
            case 2:
                tempHttp = ApiUserNewService.PRD_URL;
                break;
            case 3:
                tempHttp = ApiUserNewService.TEST_URL;
                break;
        }
        return apiBuild.buildApiInterface(tempHttp, ApiUserNewService.class);
    }

    @Provides
    @Singleton
    static ApiGoodsService apiService(ApiBuild apiBuild) {
        String tempHttp = ApiGoodsService.BASE_URL;
        switch (AppConfig.BASE_HTTP_IP) {
            case 1:
                tempHttp = ApiGoodsService.BASE_URL;
                break;
            case 2:
                tempHttp = ApiGoodsService.PRD_URL;
                break;
            case 3:
                tempHttp = ApiGoodsService.TEST_URL;
                break;
        }
        return apiBuild.buildApiInterface(tempHttp, ApiGoodsService.class);
    }


    @Provides
    @Singleton
    static ApiPHPService apiPHPService(ApiBuild apiBuild) {
        String tempHttp = ApiPHPService.BASE_URL;
        switch (AppConfig.BASE_HTTP_IP) {
            case 1:
                tempHttp = ApiPHPService.BASE_URL;
                break;
            case 2:
                tempHttp = ApiPHPService.BASE_URL;
                break;
            case 3:
                tempHttp = ApiPHPService.TEST_URL;
                break;
        }
        return apiBuild.buildApiInterface(tempHttp, ApiPHPService.class);
    }

    @Provides
    @Singleton
    static ApiPCService apiPCService(ApiBuild apiBuild) {
        String tempHttp = ApiPCService.BASE_URL;
        switch (AppConfig.BASE_HTTP_IP) {
            case 1:
                tempHttp = ApiPCService.BASE_URL;
                break;
            case 2:
                tempHttp = ApiPCService.BASE_URL;
                break;
            case 3:
                tempHttp = ApiPCService.TEST_URL;
                break;
        }
        return apiBuild.buildApiInterface(tempHttp, ApiPCService.class);
    }

}
