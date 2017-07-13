package cn.edu.zju.se_g01.nfc_pay.config;

/**
 * Created by dddong on 2017/7/9.
 */

public class Config {
    public static final String URL_PROTOCOL = "http://";
    public static final String URL_DOMAIN_NAME = "120.77.34.254";
    public static final String URL_PATH_PREFIX = "/business-system/backend/public/";

    public static String getFullUrl(String name) {
        return URL_PROTOCOL + URL_DOMAIN_NAME + URL_PATH_PREFIX + name;
    }
}
