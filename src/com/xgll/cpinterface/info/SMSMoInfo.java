package com.xgll.cpinterface.info;

public class SMSMoInfo
{
    public SMSMoInfo()
    {
    }

    public String getLinkid()
    {
        return linkid;
    }

    public void setLinkid(String linkid)
    {
        this.linkid = linkid;
    }

    public String getMsgContent()
    {
        return msgContent;
    }

    public void setMsgContent(String msgContent)
    {
        this.msgContent = msgContent;
    }

    public String getSpCode()
    {
        return spCode;
    }

    public void setSpCode(String spCode)
    {
        this.spCode = spCode;
    }

    public String getSrcTerminalId()
    {
        return srcTerminalId;
    }

    public void setSrcTerminalId(String srcTerminalId)
    {
        this.srcTerminalId = srcTerminalId;
    }

    private String srcTerminalId; // Դ�ֻ�����

    private String msgContent; // ��������

    private String spCode; // �ط���

    private String linkid; // Link ID

}