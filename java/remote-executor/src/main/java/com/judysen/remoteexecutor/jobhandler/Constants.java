package com.judysen.remoteexecutor.jobhandler;


/**
 * 常量
 * 
 * @author chiyong
 * @version 1.0
 */
public class Constants {

	/**
	 * 基础定义
	 */	
	// sql configure
	public final static String CLIENT_PROPERTY = "/client.properties";
	
	/**
	 * 用户自定义
	 */
	// Request JSON key
	public final static String USER_NAME = "user";
	public final static String METHOD = "method";
	public final static String URL = "url";
	public final static String PARAMS = "params";
	
	/**
	 * method
	 */
	public final static String METHOD_GET = "GET";
	public final static String METHOD_POST = "POST";
	public final static String METHOD_PUT = "PUT";
	public final static String METHOD_DELETE = "DELETE";
	
	/**
	 * 运行模式
	 */
	public final static String MODE_LOCAL = "local";
	public final static String MODE_DEV = "dev";
	public final static String MODE_TEST = "test";
	public final static String MODE_UAT = "uat";
	public final static String MODE_DEPLOY = "deploy";
	public final static String MODE_ALL = "all";
	
	public final static String LOG_ENV_JSON = "ENV";
	public final static String LOG_SYSTEM_DIFF_JSON = "SYS";
	public final static String LOG_DATA_JSON = "DATA";
	public final static String LOG_DATA_COLUMN = "LOGMSG";
	public final static String LOG_DATA_COLUMN_TIME = "LOGTIME";
	
	// 模式区分
	public final static String ENV_LOCAL = "LOC";
	public final static String ENV_DEVELOP = "DEV";
	public final static String ENV_TEST = "TST";
	public final static String ENV_UAT = "UAT";
	public final static String ENV_DEPLOY = "DEP";

	// 系统区分
	// TaskJob
	public final static String SYSTEM_DIFF_TSKJB = "TSKJB";
	
	//动态修改任务调度时间的自检任务
	public final static String TASK_SELF_CHECKING = "taskSelfChecking";
	
	public final static String MLTP_CLEARING_TASK = "mltpClearingTask";
	public final static String MLTP_CLEARING_DELIVERY_TASK = "mltpClearingDeliveryTask";
	
	/**
	 * DB区分
	 */
	public final static int DB_CWP = 1;
	public final static int DB_ONLINE = 2;
	
	/**
	 * 自定义
	 */
	public final static int JOB_UPD_START = 1;
	public final static int JOB_UPD_SUCCESS = 2;
	public final static int JOB_UPD_FAIL = 3;
	
	public final static int NON_DEAL_DAY = 0;
	public final static int DEAL_DAY = 1;
	public final static int DELIVERY_DAY = 2;
	
	public final static int NORMAL_CLEARING = 0;
	public final static int DELIVERY_CLEARING = 1;
}
