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
     * 解压文件，将压缩文件解压
     * @param zipFile, 压缩文件名
     * @param destDir, 目的路径
     * @return String, 解压目的目录
     */
    public static String unzip(String zipFile, String destDir)
    {
        /* 压缩文件名为空，返回NULL */
        if (zipFile == null)
        {
            return null;
        }
        
        File destFile = new File(destDir); // 用来处理文件夹
        if (! destFile.exists())
        {
            destFile.mkdirs(); // 如果目录不存在，创建之
        }
        String fileName = "";
        FileOutputStream fileOut = null; // 用来处理文件
        ZipEntry entry = null;
        byte[] bytes = new byte[READ_BYTES]; // 缓冲区
        int read = 0; // 每次读写的字节数
        try
        {
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
            
            while ((entry = zin.getNextEntry()) != null)
            {
                if (entry.isDirectory()) // 目录，需要创建
                {
                    /* 创建目录 */
                    fileName = destDir + File.separatorChar + entry.getName();
                    destFile = new File(fileName);
                    destFile.mkdir();
                }
                else // 读写文件
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
            Log.printError(ioe, "解压文件" + zipFile + "出现异常", Config.logDir + "/error.log");
        }
        
        return destDir;
    }
    
    /**
     * 拷贝文件并删除源文件
     * @param srcFileName, 源文件名
     * @param destFileName, 目标文件名
     */
    public static void move(String srcFileName, String destFileName)
    {
        if (srcFileName == null || destFileName == null)
        {
            return;
        }

        byte[] bytes = new byte[1024]; // 缓冲区
        int read = 0; // 每次读写的字节数
        File srcFile = new File(srcFileName);
        File destFile = new File(destFileName);
        try
        {
            if (srcFile.exists() && srcFile.canRead()) // 源文件存在并可读
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
            Log.printError(fnfe, "找不到文件", Config.logDir + "/error.log");
        }
        catch (IOException ioe)
        {
            Log.printError(ioe, "读写文件出错", Config.logDir + "/error.log");
        }
    }     
    
    /**
     * 把文件加密为字符串
     * @param fileName, 文件名
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
            Log.printError(ioe, "读文件出错", Config.logDir + "/error.log");
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
     * 把Blob内容加密为字符串
     * @param zipContent, 已压缩好的zip包Blob对象
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
            Log.printError(ioe, "读彩信内容出错", Config.logDir + "/error.log");
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
     * 解码彩信内容并将之保存为zip格式的压缩文件
     * @param fileName, 文件名
     * @param content, Base64加密的彩信内容
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
            Log.printError(ioe, "写文件出错", Config.logDir + "/error.log");
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
     * 获得当前日期（YYYYMMDD）
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
     * 产生sequence，递增
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
     * 获得星期字符串
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
