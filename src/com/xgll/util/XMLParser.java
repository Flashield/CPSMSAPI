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
 * XML�ĵ�������
 * 
 * @author ywh
 */
public class XMLParser
{
    /**
     * ������
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
     * ������
     * 
     * @param fileName, �ļ���
     */
    public XMLParser(String fileName) throws Exception
    {
        propertyCache = new HashMap();
        file = new File(fileName);
        SAXBuilder builder = new SAXBuilder();
        doc = builder.build(file);
    }

    /**
     * ��ýڵ��ֵ
     * 
     * @param name, �ڵ��ǩ��
     * @return String
     */
    public String getElement(String name)
    {
        // ��������Ի��������Ѿ����ڣ��Ͱ���ֱ��ȡ����
        if (propertyCache.containsKey(name))
        {
            return (String) propertyCache.get(name);
        }
        // ��XML�ĵ��в���
        Element element = findElement(parsePropertyName(name));
        if (element == null) // �ڵ�û�ҵ�
        {
            return null;
        }
        String value = element.getText();

        if (value.equals("")) // �ڵ��ҵ��˵���ֵΪ��
        {
            return null;
        }
        else
        {
            value = value.trim();
            propertyCache.put(name, value); // ��ӽ����Ի��������Ա��´�ʹ��
            return value;
        }
    }

    /**
     * �õ�ָ���ڵ�������ӽڵ���
     * 
     * @param parent, �ڵ���
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
     * �޸Ľڵ��ֵ
     * 
     * @param name, �ڵ���
     * @param value, �ڵ�ֵ
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
     * ��ӽڵ�
     * 
     * @param parent, ���ڵ�
     * @param name, Ҫ��ӵ��ӽڵ�
     * @param value, �ӽڵ��ֵ
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
     * ɾ���ڵ�
     * 
     * @param name, �ڵ���
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
     * ���ĵ�д�������
     * 
     * @param out
     */
    public synchronized void output(OutputStream out) throws IOException
    {
        new XMLOutputter().output(doc, out);
    }

    /**
     * ��XML�ĵ�����Ϊ�ļ� ���ļ���ʽ�򿪵�XML�ĵ����޸ĺ��ʹ�ô˷�������
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
     * ��XML�ĵ�����Ϊ�ļ�
     * 
     * @param fileName, �ļ���
     * @throws Exception
     */
    public synchronized void saveAsFile(String fileName) throws Exception
    {
        File file = new File(fileName);
        saveAsFile(file);
    }

    /**
     * ��xml�ĵ�����Ϊ�ļ�
     * 
     * @param file �ļ�
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
     * �����ڵ���
     * 
     * @param name, �ڵ���
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
     * ���ҽڵ�
     * 
     * @param propName, �ڵ������飨����ṹ��
     * @return �ڵ�Ԫ��
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
