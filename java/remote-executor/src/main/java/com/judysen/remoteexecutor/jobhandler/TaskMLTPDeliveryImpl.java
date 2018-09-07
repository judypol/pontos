package com.judysen.remoteexecutor.jobhandler;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.judysen.remoteexecutor.core.MLTPUtils;
import com.judysen.remoteexecutor.core.OkHttpUtils;
import com.judysen.remoteexecutor.models.ProcessModel;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * TaskMLTPImpl Implementation
 * 
 * @author chiyong
 */
@JobHandler(value="mltpDeliveryHandler")
@Component
public class TaskMLTPDeliveryImpl extends IJobHandler {
	@Value("${mltp.middle.url}")
	private String Url;
	@Value("${mail.url}")
	private String mailUrl;
	@Value("${mode}")
	private String mode;
	/**
	 * execute handler, invoked when executor receives a scheduling request
	 *
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		try{
			run(param);
			return SUCCESS;
		}catch (Exception ex){
			XxlJobLogger.log(ex);
			return FAIL;
		}
	}
	/**
	 * 执行自动Job
	 */
	public void run(String param) throws Exception {
		JSONObject jsonObject= JSON.parseObject(param);

		// 取得执行参数
		String serviceName = jsonObject.getString("servicename");
		String methodName = jsonObject.getString("methodname");
		String message = jsonObject.getString("message");
		String receiver = jsonObject.getString("receiver");

		String GUID = UUID.randomUUID().toString();

		StringBuilder sb = new StringBuilder();
		sb.append("-----------------" + message + "开始-----------------。</br>");
		sb.append("GUID:" + GUID + "</br>");
		XxlJobLogger.log(sb.toString());

		// 执行服务
		JSONObject judgeDayType = MLTPUtils.generataPostData("cmltp.job.service.IJobMgrService",
				"judgeDayType", null);
		JSONObject jsRtnJudgeDayType = null;

		JSONObject pm = new JSONObject();
		pm.put("differ", Constants.DELIVERY_CLEARING);
		pm.put("key", Constants.JOB_UPD_START);
		JSONObject updJobStatusStart = MLTPUtils.generataPostData("cmltp.job.service.IJobMgrService", "updateJobSt",
				pm.toString());
		pm.put("key", Constants.JOB_UPD_SUCCESS);
		JSONObject updJobStatusSuccess = MLTPUtils.generataPostData("cmltp.job.service.IJobMgrService", "updateJobSt",
				pm.toString());
		pm.put("key", Constants.JOB_UPD_FAIL);
		JSONObject updJobStatusFail = MLTPUtils.generataPostData("cmltp.job.service.IJobMgrService", "updateJobSt", pm.toString());

		JSONObject pmClear = new JSONObject();
		// 本次为普通结算
		pmClear.put("differ", Constants.DELIVERY_CLEARING);
		JSONObject jsCancelLeads = MLTPUtils.generataPostData(serviceName, methodName, pmClear.toString());
		JSONObject jsRtnCancelLeads = null;
		JSONObject jsAddDeposite = MLTPUtils.generataPostData("cmltp.job.service.IJobMgrService", "addDeposits",
				pmClear.toString());
		JSONObject jsRtnAddDeposite = null;
		JSONObject jsClearing = MLTPUtils.generataPostData("cmltp.job.service.IJobMgrService", "clearing", pmClear.toString());
		JSONObject jsRtnClearing = null;

		boolean actionFlag = false;

		String proxyIp=this.Url;
		// 判断是否是交易交收日
		jsRtnJudgeDayType = MLTPUtils.httpPost(proxyIp, judgeDayType);
		// 交收日场合继续执行
		if (MLTPUtils.ifDeliveryday(jsRtnJudgeDayType)) {

			String dlCnToday = MLTPUtils.getDeliveryContractToday(jsRtnJudgeDayType);

			// Job状态更新: Job开始
			MLTPUtils.httpPost(proxyIp, updJobStatusStart);
			/**
			 * 1.0 每日自动撤盘任务
			 */
			jsRtnCancelLeads = MLTPUtils.httpPost(proxyIp, jsCancelLeads);
			actionFlag = MLTPUtils.isSuccess(jsRtnCancelLeads);
			XxlJobLogger.log(mode, "交收日自动撤盘任务结果：" + jsRtnCancelLeads.toString());

			/**
			 * 2.0 追加保证金任务
			 */
			if (actionFlag) {
				jsRtnAddDeposite = MLTPUtils.httpPost(proxyIp, jsAddDeposite);
				actionFlag = MLTPUtils.isSuccess(jsRtnAddDeposite);
				XxlJobLogger.log(mode, "交收日追加保证金任务结果：" + jsRtnAddDeposite.toString());
			}

			/**
			 * 3.0 每日结算任务
			 */
			if (actionFlag) {
				jsRtnClearing = MLTPUtils.httpPost(proxyIp, jsClearing);
				actionFlag = MLTPUtils.isSuccess(jsRtnClearing);
				XxlJobLogger.log(mode, "交收日结算任务结果：" + jsRtnClearing.toString());
			}

			/**
			 * 5.0 发送结果Mail
			 */
			StringBuilder result = new StringBuilder();
			result.append("<h1>自贸区交收日结算任务内容如下：</h1>");
			result.append("<h1>  (今日为合约(" + dlCnToday + ")的交收日，"
					+ "该合约的出金审核已在之前的Job中执行过，本次不再执行)</h1>");
			result.append("1. 自动撤盘任务</br>");
			result.append("2. 追加保证金任务(需追加保证金当日)</br>");
			result.append("3. 结算任务</br>");
			result.append("  3.1. 本日浮动盈亏结算</br>");
			result.append("  3.2. 追加保证金结算</br>");
			result.append("  3.3. 交收保证金结算</br>");
			result.append("  3.4. 本日真实盈亏结算</br>");
			result.append("  3.5. 企业负债(当日结算后已收保证金金额+可用资金+待补资金)更新</br>");
			result.append("<h1>自贸区交收日结算任务执行结果如下：</h1>");
			if (jsRtnCancelLeads != null) {
				result.append("<h2>1. 交收日自动撤盘任务结果:</h2>");
				result.append(jsRtnCancelLeads.toString());
			}
			if (jsRtnAddDeposite != null) {
				result.append("</br><h2>2. 交收日追加保证金任务结果:</h2>");
				result.append(jsRtnAddDeposite.toString());
			} else {
				result.append("</br><h2>2. 交收日追加保证金任务尚未执行</h2>");
			}
			if (jsRtnClearing != null) {
				result.append("</br><h2>3. 交收日结算任务结果:</h2>");
				result.append(jsRtnClearing.toString());
			} else {
				result.append("</br><h2>3. 交收日结算任务尚未执行</h2>");
			}
			result.append("</br></br></br></br>");

			if (actionFlag) {
				// Job状态更新: Job成功
				MLTPUtils.httpPost(proxyIp, updJobStatusSuccess);
			} else {
				// Job状态更新: Job失败
				MLTPUtils.httpPost(proxyIp, updJobStatusFail);
			}

			sendMail(result.toString(), mailUrl, receiver, mode, actionFlag);
		} else {

			XxlJobLogger.log(mode, "本日为非交易日或没有交收合约,不需执行交收结算任务！");
		}

		XxlJobLogger.log(mode, "-----------------" + message + "结束-----------------。</br>" + "GUID:" + GUID + "</br>");
	}

	/**
	 * Job执行完毕，自动发送WarningMail
	 * 
	 * @param message
	 * @param mode
	 */
	public void sendMail(String message, String mailUrl, String receiver, String mode,
			boolean actionFlag) {

		ProcessModel psBean = new ProcessModel();
		psBean.setBody(message);
		psBean.setRecipient(receiver);
		psBean.setSender("Shcem_service@shcem.com");

		if (actionFlag) {
			psBean.setSubject("自贸区本日交收结算任务执行成功(本邮件为系统自动发送)(" + mode + "环境)");
		} else {
			psBean.setSubject("自贸区本日交收结算任务执行失败(本邮件为系统自动发送)(" + mode + "环境)");
		}
		psBean.setIsHtmlBody("True");

		String postErrorMail = "";
		JSONObject jso = null;
		try {
			XxlJobLogger.log( "开始发送邮件:" + jso.toString());

			Map<String, String> data = new HashMap<>();
			data.put("sendBody", JSON.toJSONString(psBean));
			postErrorMail = OkHttpUtils.init().postByFrom(mailUrl,data);

			XxlJobLogger.log(mode, "发送邮件Receipt:" + receiver);
			XxlJobLogger.log(mode, "发送邮件url:" + mailUrl);
			XxlJobLogger.log(mode, "发送邮件成功!");
		} catch (Exception e) {
			postErrorMail = "发送邮件失败：" + e.getMessage();
		}

		XxlJobLogger.log(mode, "发送邮件：" + postErrorMail);
	}
}
