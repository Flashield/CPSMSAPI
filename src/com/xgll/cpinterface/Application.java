package com.xgll.cpinterface;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.xgll.Config;
import com.xgll.cpinterface.Constants;
import com.xgll.cpinterface.ToolKits;
import com.xgll.util.XMLParser;
import com.xgll.cpinterface.net.BlockedServer;
import com.xgll.util.Log;
import java.sql.Blob;

public class Application
{
    /** 线程同步控制确保模块仅有一个实例 */
    static synchronized public Application getInstance(MoListener moListener)
    {
        if (instance == null)
        {
            instance = new Application(moListener);
        }
        return instance;
    }

    private Application(MoListener moListener)
    {
        if (moListener == null)
        {
            System.out.println("您注册的上行监听器为空，这将会导致系统无法正常运行！");
            System.exit(-1);
        }
        try
        {
            new Config().initConfig();
            this.url = new URL(Config.url);
            this.buffer = new StringBuffer();
            new BlockedServer(moListener).start();
        }
        catch (Exception e)
        {
            System.out.println(e.getLocalizedMessage());
            System.exit(-1);
        }
    }

    /**
     * 发送短信
     * @param actId, 活动代码
     * @param spCode, 特服号
     * @param feeTerminalId, 付费手机号码
     * @param destTerminalId, 接收方手机号码
     * @param serviceId, 业务代码
     * @param feeType, 计费类型
     * @param feeCode, 计费代码
     * @param needReport, 是否需要状态报告：0、不需要；1、需要
     * @param msgContent, 短信内容
     * @param linkId, Link Id
     * @return Integer, 错误码
     */
    public synchronized int sendSMS(String actId, String spCode,
            String feeTerminalId, String destTerminalId, String serviceId,
            String feeType, String feeCode, String needReport,
            String msgFormat, String msgContent, String linkId)
    {
        try
        {
            XMLParser parser = new XMLParser(Config.smsReqTemplateFile);
            parser.setElement("header.user", Config.user);
            parser.setElement("header.passwd", Config.passwd);
            parser.setElement("body.actId", actId);
            parser.setElement("body.spCode", spCode);
            parser.setElement("body.feeTerminalId", feeTerminalId);
            parser.setElement("body.destTerminalId", destTerminalId);
            parser.setElement("body.serviceId", serviceId);
            parser.setElement("body.feeType", feeType);
            parser.setElement("body.feeCode", feeCode);
            parser.setElement("body.needReport", needReport);
            parser.setElement("body.msgFormat", msgFormat);
            parser.setElement("body.msgContent", msgContent);
            parser.setElement("body.linkid", linkId);
            int result = sendRequest(parser);
            // if (Config.isDebug)
            // {
            // parser.saveAsFile(Config.logDir + "/sms_req.xml");
            // }
            if (Config.isLog)
            {
                if (result == Constants.SUCCESS)
                {
                    buffer.setLength(0);
                    buffer.append("成功发送了一个下行短信请求包，手机号码为：");
                    buffer.append(destTerminalId);
                    buffer.append(" 短信内容为：");
                    buffer.append(msgContent);
                    Log.printEvent(buffer.toString(), Config.logDir
                            + "sms_send.log");
                }
                else
                {
                    buffer.setLength(0);
                    buffer.append("发送了一个短信请求包，没有成功。手机号码为：");
                    buffer.append(destTerminalId);
                    buffer.append(" 彩信标题为：");
                    buffer.append(msgContent);
                    buffer.append(" 错误码为：");
                    buffer.append(result);
                    Log.printEvent(buffer.toString(), Config.logDir
                            + "sms_send.log");
                }
            }
            return result;
        }
        catch (Exception e)
        {
            return Constants.XML_FORMAT_ERROR;
        }
    }

    /**
     * 发送彩信
     * @param actId, 活动代码
     * @param feeTerminalId, 付费方手机号码
     * @param destTerminalId, 接收方手机号码
     * @param ccTerminalId, 抄送方手机号码
     * @param serviceId, 业务代码
     * @param fee, 费率
     * @param msgTitle, 彩信标题
     * @param zipFileName, 彩信内容zip包所在路径（绝对路径）
     * @param needReport, 是否需要状态报告：0、不需要；1、需要
     * @param linkId, Link Id
     * @return Integer, 错误码
     */
    public synchronized int sendMMS(String actId, String feeTerminalId,
            String destTerminalId, String ccTerminalId, String serviceId,
            String fee, String msgTitle, String zipFileName, String needReport,
            String linkId)
    {
        if (zipFileName == null)
        {
            return Constants.MSG_CONTENT_NULL;
        }
        String encodeString = ToolKits.encodeFile(zipFileName);
        if (encodeString == null)
        {
            return Constants.BASE64_ENCODE_ERROR;
        }
        if (encodeString.equals(""))
        {
            return Constants.ZIP_FILE_NOT_EXIST;
        }
        try
        {
            XMLParser parser = new XMLParser(Config.mmsReqTemplateFile);
            parser.setElement("header.user", Config.user);
            parser.setElement("header.passwd", Config.passwd);
            parser.setElement("body.actId", actId);
            parser.setElement("body.feeTerminalId", feeTerminalId);
            parser.setElement("body.destTerminalId", destTerminalId);
            parser.setElement("body.ccTerminalId", ccTerminalId);
            parser.setElement("body.serviceId", serviceId);
            parser.setElement("body.fee", fee);
            parser.setElement("body.needReport", needReport);
            parser.setElement("body.msgTitle", msgTitle);
            parser.setElement("body.msgContent", encodeString);
            parser.setElement("body.linkid", linkId);
            int result = sendRequest(parser);
            // if (Config.isDebug)
            // {
            // parser.saveAsFile(Config.logDir + "/mms_req.xml");
            // }
            if (Config.isLog)
            {
                if (result == Constants.SUCCESS)
                {
                    buffer.setLength(0);
                    buffer.append("成功发送了一个下行彩信请求包，手机号码为：");
                    buffer.append(destTerminalId);
                    buffer.append(" 彩信标题为：");
                    buffer.append(msgTitle);
                    Log.printEvent(buffer.toString(), Config.logDir
                            + "mms_send.log");
                }
                else
                {
                    buffer.setLength(0);
                    buffer.append("发送了一个彩信请求包，没有成功。手机号码为：");
                    buffer.append(destTerminalId);
                    buffer.append(" 彩信标题为：");
                    buffer.append(msgTitle);
                    buffer.append(" 错误码为：");
                    buffer.append(result);
                    Log.printEvent(buffer.toString(), Config.logDir
                            + "mms_send.log");
                }
            }
            return result;
        }
        catch (Exception e)
        {
            return Constants.XML_FORMAT_ERROR;
        }
    }
    
    public synchronized int sendMMS(String actId, String feeTerminalId,
            String destTerminalId, String ccTerminalId, String serviceId,
            String fee, String msgTitle, Blob zipContent, String needReport,
            String linkId)
    {
        if (zipContent == null)
        {
            return Constants.MSG_CONTENT_NULL;
        }
        
        String encodeString = ToolKits.encodeBlob(zipContent);
        if (encodeString == null)
        {
            return Constants.BASE64_ENCODE_ERROR;
        }
        if (encodeString.equals(""))
        {
            return Constants.ZIP_FILE_NOT_EXIST;
        }
        try
        {
            XMLParser parser = new XMLParser(Config.mmsReqTemplateFile);
            parser.setElement("header.user", Config.user);
            parser.setElement("header.passwd", Config.passwd);
            parser.setElement("body.actId", actId);
            parser.setElement("body.feeTerminalId", feeTerminalId);
            parser.setElement("body.destTerminalId", destTerminalId);
            parser.setElement("body.ccTerminalId", ccTerminalId);
            parser.setElement("body.serviceId", serviceId);
            parser.setElement("body.fee", fee);
            parser.setElement("body.needReport", needReport);
            parser.setElement("body.msgTitle", msgTitle);
            parser.setElement("body.msgContent", encodeString);
            parser.setElement("body.linkid", linkId);
            int result = sendRequest(parser);
            // if (Config.isDebug)
            // {
            // parser.saveAsFile(Config.logDir + "/mms_req.xml");
            // }
            if (Config.isLog)
            {
                if (result == Constants.SUCCESS)
                {
                    buffer.setLength(0);
                    buffer.append("成功发送了一个下行彩信请求包，手机号码为：");
                    buffer.append(destTerminalId);
                    buffer.append(" 彩信标题为：");
                    buffer.append(msgTitle);
                    Log.printEvent(buffer.toString(), Config.logDir
                            + "mms_send.log");
                }
                else
                {
                    buffer.setLength(0);
                    buffer.append("发送了一个彩信请求包，没有成功。手机号码为：");
                    buffer.append(destTerminalId);
                    buffer.append(" 彩信标题为：");
                    buffer.append(msgTitle);
                    buffer.append(" 错误码为：");
                    buffer.append(result);
                    Log.printEvent(buffer.toString(), Config.logDir
                            + "mms_send.log");
                }
            }
            return result;
        }
        catch (Exception e)
        {
            return Constants.XML_FORMAT_ERROR;
        }
    }

    /**
     * 发送请求包
     * @param info, MessageInfo
     * @return Integer
     */
    private int sendRequest(XMLParser parser)
    {
        HttpURLConnection con = null;
        OutputStream out = null;
        InputStream in = null;

        try
        {
            con = (HttpURLConnection) url.openConnection();
            System.setProperty("sun.net.client.defaultConnectTimeout",
                    Config.timeOut);
            System.setProperty("sun.net.client.defaultReadTimeout",
                    Config.timeOut);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "text/plain;charset=GB2312");
            con.connect();
            out = con.getOutputStream();
            parser.output(out);
            in = con.getInputStream();
            return receiveResp(in); // 接收回包
        }
        catch (Exception e)
        {
            Log.printError(e, "", Config.logDir + "/error.log");
            return Constants.CONNECT_ERROE;
        }
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
                if (in != null)
                {
                    in.close();
                }
                if (con != null)
                {
                    con.disconnect();
                }
            }
            catch (Exception e)
            {
                // do nothing
            }
        }
    }

    /**
     * 接收回包
     * @param in, 输入流
     * @return Integer
     * @throws Exception
     */
    private int receiveResp(InputStream in) throws Exception
    {
        XMLParser parser = new XMLParser(in);
        int result = 0;
        String transactionId = parser.getElement("header.transactionId");
        if (!transactionId.equals(String.valueOf(sequence)))
        {
            result = -1001; // 请求序列不匹配
        }
        String temp = parser.getElement("header.packageType");
        if (!temp.equals("SMSMTResponse") || !temp.equals("MMSMTResponse"))
        {
            result = -1002; // 回包类型不对
        }
        temp = parser.getElement("body.result");
        if (temp == null)
        {
            result = -1003;
        }
        else
        {
            result = Integer.parseInt(temp);
        }
        return result;
    }

    static private Application instance; // 应用模块实例

    private StringBuffer buffer; // 字符串缓冲区

    private int sequence; // 序列

    private URL url; // URL
}
