package com.jetinno.common;


import android.os.Environment;

import com.example.commonui.BuildConfig;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.LogLog;

import java.io.File;

import log4j.LogCatAppender;
import log4j.MyDailyRollingFileAppender;

/**
 * 由于 mars-xlog 暂未支持单线程多文件写入
 * net、operarion 两类的log使用log4j打印并写入文件
 * 其余logcat使用高性能xlog打印
 */
public class Log4jUtils {

    private static boolean isDebug = BuildConfig.DEBUG;

    private static String ROOT_PATH = Environment.getExternalStorageDirectory() + File.separator + "Jetinno" + File.separator + "Log";

    // log4j 配置
    private static final String LOGGER_NAME_NET = "Net";
    private static final String LOGGER_NAME_USER_OPERATION = "UserOperation";
    private static final String LOGGER_NAME_XLOG = "Xlog";
    private static final String LOGGER_NAME_MDB = "Mdb";//mdb日志
    private static final String LOGGER_NAME_ORDER = "Order";//订单日志
    private static final String LOGGER_NAME_HEARTBEAT = "HeartBeat";//心跳包日志
    private static final String LOGGER_NAME_EXCEPTION = "Exception";//异常日志
    private static final int LOGGER_SAVE_DAYS = 30;     //保留最近30个文件
    private static final String FILE_PATTERN = "%d{yyyy:MM:dd HH:mm:ss} [%t] %m%n";
    private static final String LOGCAT_PATTERN = "%m%n";
    private static final String DATE_PATTERN = "'_'yyyyMMdd'.txt'";
    private static final Logger mLoggerNet = Logger.getLogger(LOGGER_NAME_NET);
    private static final Logger mLoggerOperation = Logger.getLogger(LOGGER_NAME_USER_OPERATION);
    private static final Logger mLoggerXlog = Logger.getLogger(LOGGER_NAME_XLOG);
    private static final Logger mLoggerMdb = Logger.getLogger(LOGGER_NAME_MDB);
    private static final Logger mLoggerOrder = Logger.getLogger(LOGGER_NAME_ORDER);
    private static final Logger mLoggerHeartBeat = Logger.getLogger(LOGGER_NAME_HEARTBEAT);
    private static final Logger mLoggerException = Logger.getLogger(LOGGER_NAME_EXCEPTION);


    private Log4jUtils() {

    }

    public static void init(String rootPath) {
        ROOT_PATH=rootPath;
        log4JConfigure();
    }

    public static void setDebug(boolean flag) {
        isDebug = flag;
    }


    /**
     * log4j 初始化
     */
    private static void log4JConfigure() {
        LogManager.getLoggerRepository().resetConfiguration();
        LogLog.setInternalDebugging(false);

        // xlog
        String xlogFileName = "Xlog" + File.separator + "xlog";
        mLoggerXlog.setLevel(Level.ALL);
        mLoggerXlog.addAppender(new LogCatAppender(new PatternLayout(LOGCAT_PATTERN)));
        mLoggerXlog.addAppender(getAppender(xlogFileName));


        // net
        String netFileName = "Net" + File.separator + "net";
        mLoggerNet.setLevel(Level.ALL);
        mLoggerNet.addAppender(new LogCatAppender(new PatternLayout(LOGCAT_PATTERN)));
        mLoggerNet.addAppender(getAppender(netFileName));

        // operation
        String operationFileName = "UserOperation" + File.separator + "operation";
        mLoggerOperation.setLevel(Level.ALL);
        mLoggerOperation.addAppender(new LogCatAppender(new PatternLayout(LOGCAT_PATTERN)));
        mLoggerOperation.addAppender(getAppender(operationFileName));

        // mdb
        String mdbFileName = "UserMdb" + File.separator + "mdb";
        mLoggerMdb.setLevel(Level.ALL);
//        mLoggerMdb.addAppender(new LogCatAppender(new PatternLayout(LOGCAT_PATTERN)));
        mLoggerMdb.addAppender(getAppender(mdbFileName));

        // order
        String orderFileName = "Order" + File.separator + "order";
        mLoggerOrder.setLevel(Level.ALL);
        mLoggerOrder.addAppender(new LogCatAppender(new PatternLayout(LOGCAT_PATTERN)));
        mLoggerOrder.addAppender(getAppender(orderFileName));

        // heart
        String heartBeatFileName = "HeartBeat" + File.separator + "heartbeat";
        mLoggerHeartBeat.setLevel(Level.ALL);
        mLoggerHeartBeat.addAppender(getAppender(heartBeatFileName));

        // exception
        String exceptionFileName = "Exception" + File.separator + "exception";
        mLoggerException.setLevel(Level.ALL);
        mLoggerException.addAppender(getAppender(exceptionFileName));
    }

    private static Appender getAppender(String fileName) {
        MyDailyRollingFileAppender dailyRollingFileAppender = new MyDailyRollingFileAppender();
        dailyRollingFileAppender.setFile(ROOT_PATH + File.separator + fileName);
        dailyRollingFileAppender.setAppend(true);
        dailyRollingFileAppender.setLayout(new PatternLayout(FILE_PATTERN));
        dailyRollingFileAppender.setDatePattern(DATE_PATTERN);
        dailyRollingFileAppender.setMaxBackupIndex(LOGGER_SAVE_DAYS);
        dailyRollingFileAppender.activateOptions();
        return dailyRollingFileAppender;
    }


    public static void d(String tag, String msg) {
        if (null != mLoggerXlog) {
            mLoggerXlog.debug(tag + " " + msg);
        }
    }

    public static void i(String tag, String msg) {
        if (null != mLoggerXlog) {
            mLoggerXlog.info(tag + " " + msg);
        }
    }

    public static void e(String tag, String msg) {
        if (null != mLoggerXlog) {
            mLoggerXlog.error(tag + " " + msg);
        }
    }

    public static void e(String tag, Throwable msg) {
        if (null != mLoggerXlog) {
            mLoggerXlog.error(tag, msg);
        }
    }

    /**
     * 写入card操作
     */
    public static void logXlog(String tag, String printToText) {
        if (null != mLoggerXlog) {
            mLoggerXlog.debug(tag + " " + printToText);
        }
    }

    /**
     * 写入网络状态
     */
    public static void logNet(String tag, String msg) {
        if (null != mLoggerNet) {
            mLoggerNet.info(tag + " " + msg);
        }
    }

    /**
     * 写入用户操作
     */
    public static void logOperation(String tag, String msg) {
        if (null != mLoggerOperation) {
            mLoggerOperation.info(tag + " " + msg);
        }
    }

    /**
     * 写入mdb操作
     */
    public static void logMdb(String tag, String msg) {
        if (null != mLoggerMdb) {
            mLoggerMdb.debug(tag + " " + msg);
        }
    }

    /**
     * 写入card操作
     */
    public static void logOrder(String tag, String printToText) {
        if (null != mLoggerOrder) {
            mLoggerOrder.debug(tag + " " + printToText);
        }
    }

    /**
     * 写入异常操作
     */
    public static void logException(String tag, String printToText) {
        if (null != mLoggerException) {
            mLoggerException.debug(tag + " " + printToText);
        }
    }

    /**
     * 写入心跳包日志操作
     */
    public static void logHeartBeat(String tag, String printToText) {
        if (null != mLoggerHeartBeat) {
            mLoggerHeartBeat.debug(tag + " " + printToText);
        }
    }

}
