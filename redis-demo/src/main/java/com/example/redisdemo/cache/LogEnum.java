package com.example.redisdemo.cache;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * log日志打印枚举
 */
@AllArgsConstructor
public enum LogEnum {

    /**
     * 缓存
     */
    CACHE_INFO("cacheInfo:"),

    SYNC_SALES_FORECAST("dispatchSyncSaleForecast:"),

    DUBBO_DATA_INFO("dubboDataInfo远程RPC调用:"),

    EVENT_TRIGGER_INFO("eventTrigger:"),

    EXCEL_EXPORT_INFO("excelExportInfo:"),


    ;

    @Slf4j
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class LogUtils {
        public static void error(String message, Object... objects) {
            log.error(message, objects);
        }

        public static void info(String message, Object... objects) {
            log.info(message, objects);
        }

        public static void warn(String message, Object... objects) {
            log.warn(message, objects);
        }

        public static void debug(String message, Object... objects) {
            log.debug(message, objects);
        }
    }


    private final String prefix;

    public void error(String message, Object... objects) {
        LogUtils.error(prefix + message, objects);
    }

    public void info(String message, Object... objects) {
        LogUtils.info(prefix + message, objects);
    }

    public void warn(String message, Object... objects) {
        LogUtils.warn(prefix + message, objects);
    }

}
