package com.effectivemobile.bankoperationsservice.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogFormatter {
    public static void logInfo(String msg) {
        log.info("\u001B[32m" + "Message info log : " + msg + "\u001B[0m");
    }

    public static void logDebug(String msg) {
        log.warn("\u001B[34m" + "Message warn log : " + msg + "\u001B[0m");
    }

    public static void logError(String msg) {
        log.error("\u001B[31m" + "Message error log : " + msg + "\u001B[0m");
    }
}
