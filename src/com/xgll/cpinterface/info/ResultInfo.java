/**
 * 
 */
package com.xgll.cpinterface.info;

/**
 * @author ywh
 */
public class ResultInfo
{
    public ResultInfo()
    {

    }

    public String getResposeType()
    {
        return resposeType;
    }

    public void setResposeType(String resposeType)
    {
        this.resposeType = resposeType;
    }

    public int getResult()
    {
        return result;
    }

    public void setResult(int result)
    {
        this.result = result;
    }

    public String getTransactionId()
    {
        return transactionId;
    }

    public void setTransactionId(String transactionId)
    {
        this.transactionId = transactionId;
    }

    private String transactionId; // ��Ӧ����־�����������Ӧ

    private String resposeType; // ��Ӧ������

    private int result; // ���
}
