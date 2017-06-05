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
    /** �߳�ͬ������ȷ��ģ�����һ��ʵ�� */
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
            System.out.println("��ע������м�����Ϊ�գ��⽫�ᵼ��ϵͳ�޷��������У�");
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
     * ���Ͷ���
     * @param actId, �����
     * @param spCode, �ط���
     * @param feeTerminalId, �����ֻ�����
     * @param destTerminalId, ���շ��ֻ�����
     * @param serviceId, ҵ�����
     * @param feeType, �Ʒ�����
     * @param feeCode, �ƷѴ���
     * @param needReport, �Ƿ���Ҫ״̬���棺0������Ҫ��1����Ҫ
     * @param msgContent, ��������
     * @param linkId, Link Id
     * @return Integer, ������
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
                    buffer.append("�ɹ�������һ�����ж�����������ֻ�����Ϊ��");
                    buffer.append(destTerminalId);
                    buffer.append(" ��������Ϊ��");
                    buffer.append(msgContent);
                    Log.printEvent(buffer.toString(), Config.logDir
                            + "sms_send.log");
                }
                else
                {
                    buffer.setLength(0);
                    buffer.append("������һ�������������û�гɹ����ֻ�����Ϊ��");
                    buffer.append(destTerminalId);
                    buffer.append(" ���ű���Ϊ��");
                    buffer.append(msgContent);
                    buffer.append(" ������Ϊ��");
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
     * ���Ͳ���
     * @param actId, �����
     * @param feeTerminalId, ���ѷ��ֻ�����
     * @param destTerminalId, ���շ��ֻ�����
     * @param ccTerminalId, ���ͷ��ֻ�����
     * @param serviceId, ҵ�����
     * @param fee, ����
     * @param msgTitle, ���ű���
     * @param zipFileName, ��������zip������·��������·����
     * @param needReport, �Ƿ���Ҫ״̬���棺0������Ҫ��1����Ҫ
     * @param linkId, Link Id
     * @return Integer, ������
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
                    buffer.append("�ɹ�������һ�����в�����������ֻ�����Ϊ��");
                    buffer.append(destTerminalId);
                    buffer.append(" ���ű���Ϊ��");
                    buffer.append(msgTitle);
                    Log.printEvent(buffer.toString(), Config.logDir
                            + "mms_send.log");
                }
                else
                {
                    buffer.setLength(0);
                    buffer.append("������һ�������������û�гɹ����ֻ�����Ϊ��");
                    buffer.append(destTerminalId);
                    buffer.append(" ���ű���Ϊ��");
                    buffer.append(msgTitle);
                    buffer.append(" ������Ϊ��");
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
                    buffer.append("�ɹ�������һ�����в�����������ֻ�����Ϊ��");
                    buffer.append(destTerminalId);
                    buffer.append(" ���ű���Ϊ��");
                    buffer.append(msgTitle);
                    Log.printEvent(buffer.toString(), Config.logDir
                            + "mms_send.log");
                }
                else
                {
                    buffer.setLength(0);
                    buffer.append("������һ�������������û�гɹ����ֻ�����Ϊ��");
                    buffer.append(destTerminalId);
                    buffer.append(" ���ű���Ϊ��");
                    buffer.append(msgTitle);
                    buffer.append(" ������Ϊ��");
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
     * ���������
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
            return receiveResp(in); // ���ջذ�
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
     * ���ջذ�
     * @param in, ������
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
            result = -1001; // �������в�ƥ��
        }
        String temp = parser.getElement("header.packageType");
        if (!temp.equals("SMSMTResponse") || !temp.equals("MMSMTResponse"))
        {
            result = -1002; // �ذ����Ͳ���
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

    static private Application instance; // Ӧ��ģ��ʵ��

    private StringBuffer buffer; // �ַ���������

    private int sequence; // ����

    private URL url; // URL
}
