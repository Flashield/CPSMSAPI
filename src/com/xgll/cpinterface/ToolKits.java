package com.xgll.cpinterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.sql.*;

import com.xgll.Config;
import com.xgll.util.Base64;
import com.xgll.util.Log;


public class ToolKits
{   
    /**
     * ��ѹ�ļ�����ѹ���ļ���ѹ
     * @param zipFile, ѹ���ļ���
     * @param destDir, Ŀ��·��
     * @return String, ��ѹĿ��Ŀ¼
     */
    public static String unzip(String zipFile, String destDir)
    {
        /* ѹ���ļ���Ϊ�գ�����NULL */
        if (zipFile == null)
        {
            return null;
        }
        
        File destFile = new File(destDir); // ���������ļ���
        if (! destFile.exists())
        {
            destFile.mkdirs(); // ���Ŀ¼�����ڣ�����֮
        }
        String fileName = "";
        FileOutputStream fileOut = null; // ���������ļ�
        ZipEntry entry = null;
        byte[] bytes = new byte[READ_BYTES]; // ������
        int read = 0; // ÿ�ζ�д���ֽ���
        try
        {
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
            
            while ((entry = zin.getNextEntry()) != null)
            {
                if (entry.isDirectory()) // Ŀ¼����Ҫ����
                {
                    /* ����Ŀ¼ */
                    fileName = destDir + File.separatorChar + entry.getName();
                    destFile = new File(fileName);
                    destFile.mkdir();
                }
                else // ��д�ļ�
                {
                    fileName = destDir + File.separatorChar + entry.getName();
                    fileOut = new FileOutputStream(fileName);
                    while ((read = zin.read(bytes, 0, READ_BYTES)) > 0)
                    {
                        fileOut.write(bytes, 0, read);
                    }
                    fileOut.close();
                }
                zin.closeEntry();
            }
            zin.close();
        }
        catch (IOException ioe)
        {
            Log.printError(ioe, "��ѹ�ļ�" + zipFile + "�����쳣", Config.logDir + "/error.log");
        }
        
        return destDir;
    }
    
    /**
     * �����ļ���ɾ��Դ�ļ�
     * @param srcFileName, Դ�ļ���
     * @param destFileName, Ŀ���ļ���
     */
    public static void move(String srcFileName, String destFileName)
    {
        if (srcFileName == null || destFileName == null)
        {
            return;
        }

        byte[] bytes = new byte[1024]; // ������
        int read = 0; // ÿ�ζ�д���ֽ���
        File srcFile = new File(srcFileName);
        File destFile = new File(destFileName);
        try
        {
            if (srcFile.exists() && srcFile.canRead()) // Դ�ļ����ڲ��ɶ�
            {
                FileInputStream in = new FileInputStream(srcFile);
                FileOutputStream out = new FileOutputStream(destFile);
                while ((read = in.read(bytes, 0, READ_BYTES)) != -1)
                {
                    out.write(bytes, 0, read);
                }

                in.close();
                out.close();
                srcFile.delete();
            }
        }
        catch (FileNotFoundException fnfe)
        {
            Log.printError(fnfe, "�Ҳ����ļ�", Config.logDir + "/error.log");
        }
        catch (IOException ioe)
        {
            Log.printError(ioe, "��д�ļ�����", Config.logDir + "/error.log");
        }
    }     
    
    /**
     * ���ļ�����Ϊ�ַ���
     * @param fileName, �ļ���
     * @return String
     */
    public static String encodeFile(String fileName)
    {
        File file = new File(fileName);
        if (!file.exists())
        {
            return "";
        }
        FileInputStream in = null;
        try
        {
            byte[] bytes = new byte[(int)file.length()];
            in = new FileInputStream(file);
            in.read(bytes);
            return Base64.encode(bytes);
        }
        catch (IOException ioe)
        {
            Log.printError(ioe, "���ļ�����", Config.logDir + "/error.log");
            return null;
        }
        finally
        {
            try
            {
                in.close();
            }
            catch (Exception e)
            {
                
            }
        }
    }
    
    /**
     * ��Blob���ݼ���Ϊ�ַ���
     * @param zipContent, ��ѹ���õ�zip��Blob����
     * @return String
     */
public static String encodeBlob(Blob zipContent)
    {
        
        if (zipContent == null)
        {
            return "";
        }
        InputStream inStream = null;
        try
        {
            byte[] bytes = new byte[(int)zipContent.length()];
            inStream = zipContent.getBinaryStream();
            inStream.read(bytes);
            return Base64.encode(bytes);
        }
        catch (Exception ioe)
        {
            Log.printError(ioe, "���������ݳ���", Config.logDir + "/error.log");
            return null;
        }
        finally
        {
            try
            {
                inStream.close();
            }
            catch (Exception e)
            {
                
            }
        }
    }
    
    /**
     * ����������ݲ���֮����Ϊzip��ʽ��ѹ���ļ�
     * @param fileName, �ļ���
     * @param content, Base64���ܵĲ�������
     */
    public static void decodeFile(String fileName, String content)
    {
        FileOutputStream out = null;
        byte[] bytess = Base64.decode(content);
        try
        {
            out = new FileOutputStream(new File(fileName));
            out.write(bytess);
            out.flush();
        }
        catch (IOException ioe)
        {
            Log.printError(ioe, "д�ļ�����", Config.logDir + "/error.log");
        }
        finally
        {
            try
            {
                out.close();
            }
            catch (Exception e)
            {
                
            }
        }
    }
	
    /**
     * ��õ�ǰ���ڣ�YYYYMMDD��
     * @return String
     */
    public static String getDate()
    {
        StringBuffer buffer = new StringBuffer();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        buffer.append(year);
        if (month < 10)
        {
            buffer.append("0");
        }
        buffer.append(month);
        if (day < 10)
        {
            buffer.append("0");
        }
        buffer.append(day);
        return buffer.toString();
    }
    
    /**
     * ����sequence������
     * @return String
     */
    public static String makeSequence()
    {
        if ((sequence++) >= 0x7fffffff)
        {
            sequence = 1;
        }
        return String.valueOf(sequence);
    }
    
    /**
     * ��������ַ���
     * @return String
     */
    public static String getDayOfWeek()
    {
        int i = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        switch (i)
        {
            case 2:
                return "Mon";
            case 3:
                return "Tues";
            case 4:
                return "Wed";
            case 5:
                return "Thurs";
            case 6:
                return "Fri";
            case 7:
                return "Sat";
            case 1:
                return "Sun";
            default:
                return "";
        }
    }
	
    private static final int READ_BYTES = 1024;
    
    private static int sequence = 0;
}
