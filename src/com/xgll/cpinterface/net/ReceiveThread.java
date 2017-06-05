package com.xgll.cpinterface.net;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Calendar;

import com.xgll.cpinterface.Constants;
import com.xgll.cpinterface.MoListener;
import com.xgll.cpinterface.ToolKits;
import com.xgll.util.XMLParser;
import com.xgll.cpinterface.info.MMSMoInfo;
import com.xgll.cpinterface.info.ResultInfo;
import com.xgll.cpinterface.info.SMSMoInfo;
import com.xgll.util.Log;
import com.xgll.Config;

public class ReceiveThread extends Thread
{

    public ReceiveThread(Socket socket, MoListener moListener)
    {
        this.socket = socket;
        this.moListener = moListener;
    }

    public void run()
    {
        InputStream in = null;
        OutputStream out = null;
        try
        {
            socket.setSoTimeout(10000);
            in = socket.getInputStream();
            out = socket.getOutputStream();
            byte[] bytes = receiveRequest(in);
            if (bytes == null)
            {
                out.write("Http Package Format Error.".getBytes());
                out.flush();
            }
            else
            {
                sendResp(parserRequest(bytes), out);
            }
        }
        catch (Exception e)
        {
            Log.printError(e, "", Config.logDir + "error.log");
        }
        finally
        {
            try
            {
                in.close();
                out.close();
                this.socket.close();
                this.socket = null;
            }
            catch (Exception e)
            {

            }
        }
    }

    /**
     * 接收请求，如果是http请求返回true
     * @param in, InputStream
     * @return Boolean
     */
    private byte[] receiveRequest(InputStream in)
    {
        try
        {
            DataInputStream din = new DataInputStream(in);
            boolean isHeader = true;
            byte[] bytes = null;
            String lineIn = "";
            int nullTimes = 0;
            int length = 0;
            int position = 0;
            while (true)
            {
                if (isHeader)
                {
                    lineIn = din.readLine();
                    if (lineIn == null)
                    {
                        if (nullTimes > 10)
                        {
                            return null;
                        }
                        nullTimes++;
                        continue;
                    }

                    if (lineIn.indexOf("Content-Length") != -1)
                    {
                        int index = lineIn.indexOf(":");
                        if (index != -1)
                        {
                            String str = lineIn.substring(index + 1);
                            if (str == null)
                            {
                                return null;
                            }
                            else
                            {
                                length = Integer.parseInt(str.trim());
                                if (length <= 0) // 没有身子，直接return
                                {
                                    return null;
                                }
                            }
                        }
                        else
                        {
                            return null;
                        }
                    }

                    if (lineIn.equals(""))
                    {
                        isHeader = false;
                        bytes = new byte[length];
                    }
                }
                else
                {
                    int left = length - position;
                    if (left == 0)
                    {
                        return bytes;
                    }
                    int read = din.read(bytes, position, left < 1024 ? left : 1024);
                    if (read == -1) // -1说明网络出错，返回
                    {
                        return null;
                    }
                    position += read;
                }
            }
        }
        catch (Exception e)
        {
            Log.printError(e, "", Config.logDir + "error.log");
            return null;
        }
    }

    /**
     * 解析请求包
     * @param xmlStream
     * @return ResultInfo
     */
    private ResultInfo parserRequest(byte[] bytes)
    {
        ResultInfo resultInfo = new ResultInfo();
        StringBuffer buffer = new StringBuffer(); // 构造zip文件名
        
        try
        {
            XMLParser parser = new XMLParser(new ByteArrayInputStream(bytes));
            String transId = parser.getElement("header.transactionId");
            if (transId == null || transId.equals(""))
            {
                resultInfo.setTransactionId("0000000001");
                resultInfo.setResposeType("ERROR");
                resultInfo.setResult(Constants.TRANSACTION_ID_ERROR);
                return resultInfo;
            }
            resultInfo.setTransactionId(transId);
            /* 先要获知请求包类型 */
            String str = parser.getElement("header.packageType");
           
            if (str.equals("SMSMORequest")) // 收到递送短信请求
            {
                resultInfo.setResposeType("SMSMOResponse");
                SMSMoInfo smsMoInfo = new SMSMoInfo();
                str = parser.getElement("body.spCode");
                if (str == null || str.equals(""))
                {
                    resultInfo.setResult(Constants.SP_CODE_NULL);
                    return resultInfo;
                }
                smsMoInfo.setSpCode(str);
                str = parser.getElement("body.terminalId");
                if (str == null || str.equals(""))
                {
                    resultInfo.setResult(Constants.DEST_TERMINAL_ID_NULL);
                    return resultInfo;
                }
                smsMoInfo.setSrcTerminalId(str);
                str = parser.getElement("body.msgContent");
                if (str == null || str.equals(""))
                {
                    resultInfo.setResult(Constants.MSG_CONTENT_NULL);
                    return resultInfo;
                }
                smsMoInfo.setMsgContent(str);
                str = parser.getElement("body.linkid");
                if (str == null )
                {
                    str = "";
                }
                smsMoInfo.setLinkid(str);
                if (Config.isLog)
                {
                    buffer.setLength(0);
                    buffer.append("成功接收了一条上行短信，手机号码：");
                    buffer.append(smsMoInfo.getSrcTerminalId());
                    buffer.append(" 短信内容：");
                    buffer.append(smsMoInfo.getMsgContent());
                    Log.printEvent(buffer.toString(), Config.logDir + "sms_mo.log");
                }  
                moListener.handleSMSMo(smsMoInfo);
                resultInfo.setResult(Constants.SUCCESS);
            }
            else if (str.equals("MMSMORequest")) // 收到递送彩信请求
            {
                resultInfo.setResposeType("MMSMOResponse");
                MMSMoInfo mmsMoInfo = new MMSMoInfo();
                str = parser.getElement("body.spCode");
                if (str == null || str.equals(""))
                {
                    resultInfo.setResult(Constants.SP_CODE_NULL);
                    return resultInfo;
                }
                mmsMoInfo.setSpCode(str);
                str = parser.getElement("body.srcTerminalId");
                if (str == null || str.equals(""))
                {
                    resultInfo.setResult(Constants.DEST_TERMINAL_ID_NULL);
                    return resultInfo;
                }
                mmsMoInfo.setSrcTerminalId(str);
                str = parser.getElement("body.msgTitle");
                if (str == null)
                {
                    str = "";
                }
                mmsMoInfo.setTitle(str);
                
                str = parser.getElement("body.msgContent");
                if (str == null || str.equals(""))
                {
                    resultInfo.setResult(Constants.MSG_CONTENT_NULL);
                    return resultInfo;
                }
                
                buffer.append(System.currentTimeMillis());
                buffer.append(".zip");
                mmsMoInfo.setZipFileName(buffer.toString()); // zip文件名
                buffer.setLength(0);
                buffer.append(Config.zipDir);
                buffer.append(File.separatorChar);
                buffer.append(mmsMoInfo.getZipFileName());
                ToolKits.decodeFile(buffer.toString(), str);
                
                str = parser.getElement("body.linkid");
                if (str == null )
                {
                    str = "";
                }
                mmsMoInfo.setLinkid(str.trim());
                if (Config.isLog)
                {
                    buffer.setLength(0);
                    buffer.append("成功接收了一条上行彩信，手机号码：");
                    buffer.append(mmsMoInfo.getSrcTerminalId());
                    buffer.append(" 彩信标题：");
                    buffer.append(mmsMoInfo.getTitle());
                    buffer.append(" 彩信内容压缩文件名：");
                    buffer.append(mmsMoInfo.getZipFileName());
                    Log.printEvent(buffer.toString(), Config.logDir + "mms_mo.log");
                }
                moListener.handleMMSMo(mmsMoInfo);
                resultInfo.setResult(Constants.SUCCESS);
            }
            else
            // 未知包
            {
                resultInfo.setResposeType("ERROR");
                resultInfo.setResult(Constants.UNKNOW_REQUEST_TYPE);
            }
            return resultInfo;
        }
        catch (Exception e) // XML格式错误或者压根就不是XML包
        {
            resultInfo.setTransactionId("0000000001");
            resultInfo.setResposeType("ERROR");
            resultInfo.setResult(Constants.XML_FORMAT_ERROR);
            Log.printError(e, "处理请求时出错", Config.logDir + "error.log");
            return resultInfo;
        }
    }

    /**
     * 发送回包
     * @param info
     * @param out
     * @throws Exception
     */
    private void sendResp(ResultInfo info, OutputStream out) throws Exception
    {
        XMLParser parser = new XMLParser(Config.respTemplateFile);
        parser.setElement("header.transactionId", info.getTransactionId());
        parser.setElement("header.packageType", info.getResposeType());
        parser.setElement("body.result", String.valueOf(info.getResult()));
        if (Config.isDebug)
        {
            parser.saveAsFile(Config.logDir + "resp.xml");
        }
        out.write(getHttpHeader().getBytes()); // 先写http头
        parser.output(out);
        out.flush();
    }

    /**
     * 构造http头
     * @return String
     */
    private String getHttpHeader()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("HTTP/1.1 200 OK").append("\r\n");
        buffer.append("Date: ");
        buffer.append(ToolKits.getDayOfWeek());
        buffer.append(", ");
        buffer.append(Calendar.getInstance().getTime().toGMTString());
        buffer.append("\r\n");
        buffer.append("Content-Type: text/xml; charset=utf-8").append("\r\n");
        buffer.append("Connection: close").append("\r\n");
        buffer.append("\r\n");
        buffer.append("");
        return buffer.toString();
    }

    private Socket socket; // 端口连接
    
    private MoListener moListener; // 上行消息监听器
}