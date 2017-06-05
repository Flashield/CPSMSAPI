package com.xgll.cpinterface;

public interface Constants
{
    // ��Ϣ����
    public static final int SMS_MO = 1; // ����
    public static final int MMS_MO = 2; // ����
    
    // �������
    public static final int SUCCESS = 0; // ����ɹ�
    public static final int TRANSACTION_ID_ERROR = -1; // ��������Ϊ��
    public static final int UNKNOW_USER = -2; // δ֪�û���
    public static final int AUTH_FAILURE = -3; // ��������Ϊ��
    public static final int UNKNOW_REQUEST_TYPE = -4; // δ֪���������
    public static final int XML_FORMAT_ERROR = -5; // XML����ʽ����
    public static final int SP_CODE_NULL = -6; // �ط���Ϊ��
    public static final int DEST_TERMINAL_ID_NULL = -7; // ���ܷ��ֻ�����Ϊ��
    public static final int ACT_ID_NULL = -8; // �����Ϊ��
    public static final int SERVICE_ID_NULL = -9; // ҵ�����Ϊ��
    public static final int FEE_CODE_NULL = -10; // �ƷѴ���Ϊ��
    public static final int FEE_TYPE_NULL = -11; // �Ʒ�����Ϊ��
    public static final int BASE64_ENCODE_ERROR = -12; // Base64���ܳ���
    public static final int ZIP_FORMAT_ERROR = -13; // Zip����ʽ����
    public static final int MSG_CONTENT_NULL = -14; // ��Ϣ����Ϊ��
    public static final int ZIP_FILE_NOT_EXIST = -100; // �޷��ҵ�ѹ���ļ�
    public static final int CONNECT_ERROE = -500; // ��������ʱ�������ӳ���
    public static final int UNKNOW_ERROR = -1000; // ��������
}