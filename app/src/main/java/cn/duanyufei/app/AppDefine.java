/**
 * Copyright (c) 2015 Mindray. All rights reserved.
 * <p/>
 * desc：文件描述
 */
package cn.duanyufei.app;

import cn.duanyufei.memory.BuildConfig;

/**
 * desc：全局配置文件
 */
public class AppDefine {

    /**
     * 服务器URL
     */
    public final static String SERVER = "https://api.64clouds.com/v1/";
    public final static String ID = "?veid=";
    public final static String KEY = "&api_key=";
    public final static String SERVICE_INFO = "getServiceInfo";
    public final static String LIVE_SERVICE_INFO = "getLiveServiceInfo";

    public static final String UPDATE_URL = "http://api.fir.im/apps/latest/58dbacf5959d694dbd00024a";
    public static final String FIR_TOKEN = "?api_token=" + BuildConfig.FIR_TOKEN;
    public static final String DOWNLOAD_URL = "http://fir.im/faymemory";

    public static final String BACKUP_URL = "http://duanyufei.cn:8088/upload";
    public static final String RECOVER_URL = "http://duanyufei.cn:8088/download";

    public static final String DATA_FORMAT = "{\"name\":\"%s\",\"check\":\"%s\",\"data\":\"%s\"}";

}
