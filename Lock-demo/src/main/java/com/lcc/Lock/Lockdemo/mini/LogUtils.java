package com.lcc.Lock.Lockdemo.mini;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogUtils {
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




