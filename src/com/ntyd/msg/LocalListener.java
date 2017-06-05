package com.ntyd.msg;

import com.xgll.Config;
import com.xgll.cpinterface.Application;
import com.xgll.cpinterface.MoListener;
import com.xgll.cpinterface.info.MMSMoInfo;
import com.xgll.cpinterface.info.SMSMoInfo;
import com.xgll.util.Log;

public class LocalListener implements MoListener {
	private Application app;
	public static String logFileName = Config.logDir + "error.log";

	public LocalListener() {
		app = Application.getInstance(this);
	}

	public void handleSMSMo(SMSMoInfo info) {
		// 处理上行短信信息，输出到文本，保存到数据库等操作
		if (info.getLinkid().length() == 1) {
			System.out.println("收到一个同步定购关系短信：");
			System.out.println("特服号：" + info.getSpCode());
			System.out.println("手机号码：" + info.getSrcTerminalId());
			System.out.println("定购指令：" + info.getMsgContent());
			System.out.println("定购类型：" + info.getLinkid());
		} else {
			System.out.println("收到一个上行短信：");
			System.out.println("特服号：" + info.getSpCode());
			System.out.println("手机号码：" + info.getSrcTerminalId());
			System.out.println("短信内容：" + info.getMsgContent());
			System.out.println("Link Id：" + info.getLinkid());
		}
	}

	public void handleMMSMo(MMSMoInfo info) {
		// 这里放置你自己的代码处理上行彩信信息，输出到文本，保存到数据库等操作
		System.out.println("收到一个上行彩信：");
		System.out.println("特服号：" + info.getSpCode());
		System.out.println("手机号码：" + info.getSrcTerminalId());
		System.out.println("彩信标题：" + info.getTitle());
		System.out.println("彩信内容压缩文件名：" + info.getZipFileName());
		System.out.println("Link Id：" + info.getLinkid());
	}

	public int sendSMS(SMSProperty smsProperty) {
		// 上行发送一条短信
		int result = app.sendSMS(smsProperty.actCode, // 活动代码
				smsProperty.spCode, // 特服号
				smsProperty.feeTerminalId, // 计费手机号码
				smsProperty.destTerminalId, // 接收方手机号码
				smsProperty.serviceId, // 业务代码
				smsProperty.feeType, // 计费类型
				smsProperty.feeCode, // 计费代码
				smsProperty.registerDelivery, // 是否需要状态报告
				smsProperty.msgFormat, // 信息格式，如果发送WAP PUSH填2
				smsProperty.msgContent, // 内容
				smsProperty.linkid // link id
				);

		System.out.println("发送信息一条短信给 " + smsProperty.destTerminalId
				+ " —— 结果代码为 " + result);
		Log.printState("发送信息一条短信给 " + smsProperty.destTerminalId + " —— 结果代码为 "
				+ result, logFileName);
		return result;
	}

	public int sendMMS(MMSProperty mmsProperty) {
		int result = app.sendMMS(mmsProperty.actCode, mmsProperty.sfrom,
				mmsProperty.sto, mmsProperty.sto2, mmsProperty.ywdm,
				mmsProperty.fee, mmsProperty.subject, mmsProperty.zipContent,
				mmsProperty.needreply, mmsProperty.linkid);
		System.out.println("发送信息一条彩信给 " + mmsProperty.sto + " —— 结果代码为 "
				+ result);
		Log.printState("发送信息一条短信给 " + mmsProperty.sto + " —— 结果代码为 " + result,
				logFileName);
		return result;
	}
}
