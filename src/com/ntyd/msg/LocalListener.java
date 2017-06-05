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
		// �������ж�����Ϣ��������ı������浽���ݿ�Ȳ���
		if (info.getLinkid().length() == 1) {
			System.out.println("�յ�һ��ͬ��������ϵ���ţ�");
			System.out.println("�ط��ţ�" + info.getSpCode());
			System.out.println("�ֻ����룺" + info.getSrcTerminalId());
			System.out.println("����ָ�" + info.getMsgContent());
			System.out.println("�������ͣ�" + info.getLinkid());
		} else {
			System.out.println("�յ�һ�����ж��ţ�");
			System.out.println("�ط��ţ�" + info.getSpCode());
			System.out.println("�ֻ����룺" + info.getSrcTerminalId());
			System.out.println("�������ݣ�" + info.getMsgContent());
			System.out.println("Link Id��" + info.getLinkid());
		}
	}

	public void handleMMSMo(MMSMoInfo info) {
		// ����������Լ��Ĵ��봦�����в�����Ϣ��������ı������浽���ݿ�Ȳ���
		System.out.println("�յ�һ�����в��ţ�");
		System.out.println("�ط��ţ�" + info.getSpCode());
		System.out.println("�ֻ����룺" + info.getSrcTerminalId());
		System.out.println("���ű��⣺" + info.getTitle());
		System.out.println("��������ѹ���ļ�����" + info.getZipFileName());
		System.out.println("Link Id��" + info.getLinkid());
	}

	public int sendSMS(SMSProperty smsProperty) {
		// ���з���һ������
		int result = app.sendSMS(smsProperty.actCode, // �����
				smsProperty.spCode, // �ط���
				smsProperty.feeTerminalId, // �Ʒ��ֻ�����
				smsProperty.destTerminalId, // ���շ��ֻ�����
				smsProperty.serviceId, // ҵ�����
				smsProperty.feeType, // �Ʒ�����
				smsProperty.feeCode, // �ƷѴ���
				smsProperty.registerDelivery, // �Ƿ���Ҫ״̬����
				smsProperty.msgFormat, // ��Ϣ��ʽ���������WAP PUSH��2
				smsProperty.msgContent, // ����
				smsProperty.linkid // link id
				);

		System.out.println("������Ϣһ�����Ÿ� " + smsProperty.destTerminalId
				+ " ���� �������Ϊ " + result);
		Log.printState("������Ϣһ�����Ÿ� " + smsProperty.destTerminalId + " ���� �������Ϊ "
				+ result, logFileName);
		return result;
	}

	public int sendMMS(MMSProperty mmsProperty) {
		int result = app.sendMMS(mmsProperty.actCode, mmsProperty.sfrom,
				mmsProperty.sto, mmsProperty.sto2, mmsProperty.ywdm,
				mmsProperty.fee, mmsProperty.subject, mmsProperty.zipContent,
				mmsProperty.needreply, mmsProperty.linkid);
		System.out.println("������Ϣһ�����Ÿ� " + mmsProperty.sto + " ���� �������Ϊ "
				+ result);
		Log.printState("������Ϣһ�����Ÿ� " + mmsProperty.sto + " ���� �������Ϊ " + result,
				logFileName);
		return result;
	}
}
