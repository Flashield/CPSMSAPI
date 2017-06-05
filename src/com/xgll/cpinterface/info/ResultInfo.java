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

    private String transactionId; // 回应包标志，和请求包对应

    private String resposeType; // 回应包类型

    private int result; // 结果
}
