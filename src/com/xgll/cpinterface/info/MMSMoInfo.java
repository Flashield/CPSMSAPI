package com.xgll.cpinterface.info;

public class MMSMoInfo
{
    public MMSMoInfo()
    {
    }

    public String getZipFileName()
    {
        return zipFileName;
    }

    public void setZipFileName(String zipFileName)
    {
        this.zipFileName = zipFileName;
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

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getLinkid()
    {
        return linkid;
    }

    public void setLinkid(String linkid)
    {
        this.linkid = linkid;
    }

    private String title; // 标题

    private String srcTerminalId; // 源手机号码

    private String zipFileName; // 压缩文件路径

    private String spCode; // 特服号

    private String linkid; // Link Id

}
