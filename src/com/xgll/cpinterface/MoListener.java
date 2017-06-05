package com.xgll.cpinterface;

import com.xgll.cpinterface.info.MMSMoInfo;
import com.xgll.cpinterface.info.SMSMoInfo;

public interface MoListener
{
    /**
     * 处理上行短信
     * 用户实现此方法操作SMSMoInfo对象
     * @param info, 上行短信信息
     */
    /* SMSMoInfo为一信息对象，提供四条信息：特服号、手机号码、短信内容和Linkid。
        此四条信息均为String类型，分别由四个公共方法获取。
        * getSpCode ()方法获取特服号，返回值类型为String
        * getSrcTerminalId()方法获取手机号码，返回值类型为String
        * getMsgContent ()方法获取短信内容，返回值类型为String
        * getLinkid()方法获取LINK ID，返回值类型为String
        注：同步定购关系信息也由此方法获取，定购指令存发在msgContent字段，
        定购类型（1、定购，2、退定，3、激活， 4、暂停）存放在linkid字段。
       CP客户端判断，如果linkid为1/2/3/4，则可以确定此上行短信为同步定购关系短信。
    */
    public void handleSMSMo(SMSMoInfo info);
    
    /**
     * 处理上行彩信
     * 用户实现此方法操作MMSMoInfo对象
     * @param info, 上行彩信信息
     */
    /* MMSMoInfo为一信息对象，提供五条信息：特服号、手机号码、彩信标题、
        彩信内容压缩文件名和Linkid。
        此五条信息均为String类型，分别由五个公共方法获取。
        * getSpCode ()方法获取特服号，返回值类型为String
        * getSrcTerminalId()方法获取手机号码，返回值类型为String
        * getTitle()方法获取彩信标题，返回值类型为String
        * getMsgContent ()方法获取彩信内容压缩文件名，返回值类型为String
        * getLinkId()方法获取LINK ID，返回值类型为String
     */
    public void handleMMSMo(MMSMoInfo info);
}
