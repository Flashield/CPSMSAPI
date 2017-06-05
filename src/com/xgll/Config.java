package com.xgll;

import java.io.File;

import com.xgll.util.MD5;
import com.xgll.util.XMLParser;

public class Config
{
    public Config()
    {
        try
        {
            xmlParser = new XMLParser("config" + File.separator + "config.xml");
        }
        catch (Exception e)
        {
            System.out.println(e.getLocalizedMessage());
            System.exit(-1);
        }
    }

    /**
     * 从xml配置文件中读取配置信息
     */
    public void initConfig()
    {
        String temp = xmlParser.getElement("server.url");
        if (temp == null)
        {
            System.out.println("请配置服务端地址。");
            System.exit(-1);
        }
        else
        {
            url = temp.trim();
        }
        temp = xmlParser.getElement("server.user");
        if (temp == null)
        {
            System.out.println("请配置登陆服务端的用户名。");
            System.exit(-1);
        }
        else
        {
            user = temp.trim();
        }
        temp = xmlParser.getElement("server.passwd");
        if (temp == null)
        {
            System.out.println("请配置登陆服务端的密码。");
            System.exit(-1);
        }
        else
        {
            passwd = temp.trim();
            passwd = new MD5().getMD5ofStr(passwd);
        }
        temp = xmlParser.getElement("client.port");
        if (temp == null)
        {
            System.out.println("请配置客户端侦听端口。");
            System.exit(-1);
        }
        else
        {
            try
            {
                port = Integer.parseInt(temp.trim());
            }
            catch (Exception e)
            {
                System.out.println("端口应为数字。");
                System.exit(-1);
            }
        }
        
        temp = xmlParser.getElement("client.timeOut");
        if (temp == null)
        {
            timeOut = "10000";
        }
        else
        {
            timeOut = temp.trim();
        }
        
        temp = xmlParser.getElement("template.smsReqXml");
        if (temp == null)
        {
            System.out.println("请配置短信请求包模板路径。");
            System.exit(-1);
        }
        else
        {
            smsReqTemplateFile = temp.trim();
        }
        
        temp = xmlParser.getElement("template.mmsReqXml");
        if (temp == null)
        {
            System.out.println("请配置彩信请求包模板路径。");
            System.exit(-1);
        }
        else
        {
            mmsReqTemplateFile = temp.trim();
        }
        
        temp = xmlParser.getElement("template.responseXml");
        if (temp == null)
        {
            System.out.println("请配置回应包模板路径。");
            System.exit(-1);
        }
        else
        {
            respTemplateFile = temp.trim();
        }
        
        
        temp = xmlParser.getElement("system.logDir");
        if (temp == null)
        {
            logDir = "./log/api"; // 默认为./log/api
        }
        else
        {
            logDir = temp.trim() + File.separatorChar;
        }
        
        temp = xmlParser.getElement("system.zipDir");
        if (temp == null)
        {
            zipDir = "./zip"; // 默认为./zip
        }
        else
        {
            zipDir = temp.trim();
            File dir = new File(zipDir);
            if (!dir.exists())
            {
                dir.mkdirs();
            }
        }
        
        temp = xmlParser.getElement("system.isLog");
        if (temp.equalsIgnoreCase("YES"))
        {
            isLog = true;
        }
        else
        {
            isLog = false; // 默认为false
        }

        temp = xmlParser.getElement("system.isDebug");
        if (temp.equalsIgnoreCase("YES"))
        {
            isDebug = true;
        }
        else
        {
            isDebug = false; // 默认为false
        }
        
        temp = xmlParser.getElement("DBConn.url");
        if (temp == null)
        {
            System.out.println("请配置数据库连接url");
            System.exit(-1);
        }
        else
        {
            DBurl=temp.trim(); // 默认为false
        }
        
        temp = xmlParser.getElement("DBConn.user");
        if (temp == null)
        {
            System.out.println("请配置数据库连接用户名");
            System.exit(-1);
        }
        else
        {
            DBuser=temp.trim(); // 默认为false
        }
        
        temp = xmlParser.getElement("DBConn.passwd");
        if (temp == null)
        {
            System.out.println("请配置数据库连接url");
            System.exit(-1);
        }
        else
        {
            DBpass=temp.trim(); // 默认为false
        }
        
        
    }

    private XMLParser xmlParser; // XML解析器

    public static String url; // 服务器URL

    public static int port; // 客户端端口

    public static boolean isDebug; // 是否调试

    public static boolean isLog;

    public static String logDir; // 日志目录
    
    public static String user; // 用户名
    
    public static String passwd; // 密码
    
    public static String zipDir; // 压缩文件存放路径

    public static String mmsReqTemplateFile; // 彩信请求包模板文件
    
    public static String smsReqTemplateFile; // 短信请求包模板文件

    public static String respTemplateFile; // 回应包模板文件

    public static String timeOut; // 发送请求包等待回应超时时间
    
    public static String DBurl; //数据库连接url
    
    public static String DBuser; //数据库连接用户名
    
    public static String DBpass; //数据库连接密码
}
