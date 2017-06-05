package com.xgll.cpinterface;

import com.xgll.cpinterface.info.MMSMoInfo;
import com.xgll.cpinterface.info.SMSMoInfo;

public interface MoListener
{
    /**
     * �������ж���
     * �û�ʵ�ִ˷�������SMSMoInfo����
     * @param info, ���ж�����Ϣ
     */
    /* SMSMoInfoΪһ��Ϣ�����ṩ������Ϣ���ط��š��ֻ����롢�������ݺ�Linkid��
        ��������Ϣ��ΪString���ͣ��ֱ����ĸ�����������ȡ��
        * getSpCode ()������ȡ�ط��ţ�����ֵ����ΪString
        * getSrcTerminalId()������ȡ�ֻ����룬����ֵ����ΪString
        * getMsgContent ()������ȡ�������ݣ�����ֵ����ΪString
        * getLinkid()������ȡLINK ID������ֵ����ΪString
        ע��ͬ��������ϵ��ϢҲ�ɴ˷�����ȡ������ָ��淢��msgContent�ֶΣ�
        �������ͣ�1��������2���˶���3����� 4����ͣ�������linkid�ֶΡ�
       CP�ͻ����жϣ����linkidΪ1/2/3/4�������ȷ�������ж���Ϊͬ��������ϵ���š�
    */
    public void handleSMSMo(SMSMoInfo info);
    
    /**
     * �������в���
     * �û�ʵ�ִ˷�������MMSMoInfo����
     * @param info, ���в�����Ϣ
     */
    /* MMSMoInfoΪһ��Ϣ�����ṩ������Ϣ���ط��š��ֻ����롢���ű��⡢
        ��������ѹ���ļ�����Linkid��
        ��������Ϣ��ΪString���ͣ��ֱ����������������ȡ��
        * getSpCode ()������ȡ�ط��ţ�����ֵ����ΪString
        * getSrcTerminalId()������ȡ�ֻ����룬����ֵ����ΪString
        * getTitle()������ȡ���ű��⣬����ֵ����ΪString
        * getMsgContent ()������ȡ��������ѹ���ļ���������ֵ����ΪString
        * getLinkId()������ȡLINK ID������ֵ����ΪString
     */
    public void handleMMSMo(MMSMoInfo info);
}
