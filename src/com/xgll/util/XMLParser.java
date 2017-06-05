package com.xgll.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * XML文档解析器
 * 
 * @author ywh
 */
public class XMLParser
{
    /**
     * 构造器
     * 
     * @param in, InputStream
     */
    public XMLParser(InputStream in) throws Exception
    {
        propertyCache = new HashMap();
        SAXBuilder builder = new SAXBuilder();
        doc = builder.build(in);
    }

    /**
     * 构造器
     * 
     * @param fileName, 文件名
     */
    public XMLParser(String fileName) throws Exception
    {
        propertyCache = new HashMap();
        file = new File(fileName);
        SAXBuilder builder = new SAXBuilder();
        doc = builder.build(file);
    }

    /**
     * 获得节点的值
     * 
     * @param name, 节点标签名
     * @return String
     */
    public String getElement(String name)
    {
        // 如果是属性缓冲区里已经存在，就把它直接取出来
        if (propertyCache.containsKey(name))
        {
            return (String) propertyCache.get(name);
        }
        // 从XML文档中查找
        Element element = findElement(parsePropertyName(name));
        if (element == null) // 节点没找到
        {
            return null;
        }
        String value = element.getText();

        if (value.equals("")) // 节点找到了但是值为空
        {
            return null;
        }
        else
        {
            value = value.trim();
            propertyCache.put(name, value); // 添加进属性缓冲区，以备下次使用
            return value;
        }
    }

    /**
     * 得到指定节点的所有子节点名
     * 
     * @param parent, 节点名
     * @return String Array
     */
    public String[] getChildren(String parent)
    {
        Element element = findElement(parsePropertyName(parent));
        List children = element.getChildren();
        int childCount = children.size();
        String childrenNames[] = new String[childCount];
        for (int i = 0; i < childCount; i++)
        {
            childrenNames[i] = ((Element) children.get(i)).getName();
        }

        return childrenNames;
    }

    /**
     * 修改节点的值
     * 
     * @param name, 节点名
     * @param value, 节点值
     */
    public void setElement(String name, String value)
    {
        propertyCache.put(name, value);
        Element element = findElement(parsePropertyName(name));
        if (element != null)
        {
            element.setText(value);
        }
    }

    /**
     * 添加节点
     * 
     * @param parent, 父节点
     * @param name, 要添加的子节点
     * @param value, 子节点的值
     */
    public void addElement(String parent, String name, String value)
    {
        String elementName = parent + "." + name;
        Element element = findElement(parsePropertyName(elementName));
        if (element == null)
        {
            element = findElement(parsePropertyName(parent));
            element.addContent(new Element(name));
            element = element.getChild(name);
            element.setText(value);
        }
        else
        {
            element.setText(value);
        }
        propertyCache.put(elementName, value);
    }

    /**
     * 删除节点
     * 
     * @param name, 节点名
     */
    public void removeElement(String name)
    {
        String[] propName = parsePropertyName(name);
        Element element = findElement(propName);
        if (element != null)
        {
            element = element.getParentElement();
            element.removeChild(propName[propName.length - 1], Namespace.getNamespace(DSMP_NAMESPACE));
        }
    }

    /**
     * 将文档写到输出流
     * 
     * @param out
     */
    public synchronized void output(OutputStream out) throws IOException
    {
        new XMLOutputter().output(doc, out);
    }

    /**
     * 将XML文档保存为文件 以文件方式打开的XML文档若修改后可使用此方法保存
     * 
     * @return null
     */
    public synchronized void saveAsFile()
    {
        boolean error = false;
        File tempFile = null;
        BufferedOutputStream out = null;

        try
        {
            tempFile = new File(file.getParentFile(), file.getName() + ".tmp");
            saveAsFile(tempFile);
        }
        catch (Exception e)
        {
            error = true;
        }
        finally
        {
            try
            {
                out.close();
            }
            catch (Exception e)
            {
                error = true;
            }
        }
        if (!error)
        {
            file.delete();
            tempFile.renameTo(file);
        }
    }

    /**
     * 将XML文档保存为文件
     * 
     * @param fileName, 文件名
     * @throws Exception
     */
    public synchronized void saveAsFile(String fileName) throws Exception
    {
        File file = new File(fileName);
        saveAsFile(file);
    }

    /**
     * 将xml文档保存为文件
     * 
     * @param file 文件
     * @return null
     */
    public synchronized void saveAsFile(File file) throws Exception
    {
        BufferedOutputStream out = new BufferedOutputStream(
                new FileOutputStream(file));
        output(out);
        out.close();

    }
    
    /**
     * 分析节点名
     * 
     * @param name, 节点名
     * @return String Array
     */
    private String[] parsePropertyName(String name)
    {
        int size = 1;
        for (int i = 0; i < name.length(); i++)
        {
            if (name.charAt(i) == '.')
            {
                size++;
            }
        }

        String propName[] = new String[size];
        StringTokenizer tokenizer = new StringTokenizer(name, ".");
        for (int i = 0; tokenizer.hasMoreTokens(); i++)
        {
            propName[i] = tokenizer.nextToken();
        }

        return propName;
    }

    /**
     * 查找节点
     * 
     * @param propName, 节点名数组（代层结构）
     * @return 节点元素
     */
    private Element findElement(String[] propName)
    {
        Element element = doc.getRootElement();

        int index = 0;
        boolean hasNamespace = false;
        for (int i = 0; i < propName.length; i++)
        {
            if (element == null)
            {
                return null;
            }
            index = propName[i].indexOf(":");
            if (index != -1)
            {
                hasNamespace = true;
                element = element.getChild(propName[i].substring(index + 1), element.getNamespace());
            }
            else
            {
                if (hasNamespace)
                {
                    element = element.getChild(propName[i], Namespace.getNamespace(DSMP_NAMESPACE));
                }
                else
                {
                    element = element.getChild(propName[i], element.getNamespace());
                }
            }
        }

        return element;
    }
    
    public static void main(String[] args)
    {
        try
        {
            XMLParser parser = new XMLParser("log/buffer.xml");
            System.out.println(parser.getElement("body.msgContent"));
            parser.saveAsFile("log/req.xml");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    private File file;

    private Document doc;

    private Map propertyCache;

    private static String DSMP_NAMESPACE = "http://www.monternet.com/dsmp/schemas/";
}
