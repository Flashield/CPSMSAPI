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
     * ��xml�����ļ��ж�ȡ������Ϣ
     */
    public void initConfig()
    {
        String temp = xmlParser.getElement("server.url");
        if (temp == null)
        {
            System.out.println("�����÷���˵�ַ��");
            System.exit(-1);
        }
        else
        {
            url = temp.trim();
        }
        temp = xmlParser.getElement("server.user");
        if (temp == null)
        {
            System.out.println("�����õ�½����˵��û�����");
            System.exit(-1);
        }
        else
        {
            user = temp.trim();
        }
        temp = xmlParser.getElement("server.passwd");
        if (temp == null)
        {
            System.out.println("�����õ�½����˵����롣");
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
            System.out.println("�����ÿͻ��������˿ڡ�");
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
                System.out.println("�˿�ӦΪ���֡�");
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
            System.out.println("�����ö��������ģ��·����");
            System.exit(-1);
        }
        else
        {
            smsReqTemplateFile = temp.trim();
        }
        
        temp = xmlParser.getElement("template.mmsReqXml");
        if (temp == null)
        {
            System.out.println("�����ò��������ģ��·����");
            System.exit(-1);
        }
        else
        {
            mmsReqTemplateFile = temp.trim();
        }
        
        temp = xmlParser.getElement("template.responseXml");
        if (temp == null)
        {
            System.out.println("�����û�Ӧ��ģ��·����");
            System.exit(-1);
        }
        else
        {
            respTemplateFile = temp.trim();
        }
        
        
        temp = xmlParser.getElement("system.logDir");
        if (temp == null)
        {
            logDir = "./log/api"; // Ĭ��Ϊ./log/api
        }
        else
        {
            logDir = temp.trim() + File.separatorChar;
        }
        
        temp = xmlParser.getElement("system.zipDir");
        if (temp == null)
        {
            zipDir = "./zip"; // Ĭ��Ϊ./zip
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
            isLog = false; // Ĭ��Ϊfalse
        }

        temp = xmlParser.getElement("system.isDebug");
        if (temp.equalsIgnoreCase("YES"))
        {
            isDebug = true;
        }
        else
        {
            isDebug = false; // Ĭ��Ϊfalse
        }
        
        temp = xmlParser.getElement("DBConn.url");
        if (temp == null)
        {
            System.out.println("���������ݿ�����url");
            System.exit(-1);
        }
        else
        {
            DBurl=temp.trim(); // Ĭ��Ϊfalse
        }
        
        temp = xmlParser.getElement("DBConn.user");
        if (temp == null)
        {
            System.out.println("���������ݿ������û���");
            System.exit(-1);
        }
        else
        {
            DBuser=temp.trim(); // Ĭ��Ϊfalse
        }
        
        temp = xmlParser.getElement("DBConn.passwd");
        if (temp == null)
        {
            System.out.println("���������ݿ�����url");
            System.exit(-1);
        }
        else
        {
            DBpass=temp.trim(); // Ĭ��Ϊfalse
        }
        
        
    }

    private XMLParser xmlParser; // XML������

    public static String url; // ������URL

    public static int port; // �ͻ��˶˿�

    public static boolean isDebug; // �Ƿ����

    public static boolean isLog;

    public static String logDir; // ��־Ŀ¼
    
    public static String user; // �û���
    
    public static String passwd; // ����
    
    public static String zipDir; // ѹ���ļ����·��

    public static String mmsReqTemplateFile; // ���������ģ���ļ�
    
    public static String smsReqTemplateFile; // ���������ģ���ļ�

    public static String respTemplateFile; // ��Ӧ��ģ���ļ�

    public static String timeOut; // ����������ȴ���Ӧ��ʱʱ��
    
    public static String DBurl; //���ݿ�����url
    
    public static String DBuser; //���ݿ������û���
    
    public static String DBpass; //���ݿ���������
}
