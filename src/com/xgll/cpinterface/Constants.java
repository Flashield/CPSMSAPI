package com.xgll.cpinterface;

public interface Constants
{
    // 信息类型
    public static final int SMS_MO = 1; // 短信
    public static final int MMS_MO = 2; // 彩信
    
    // 错误代码
    public static final int SUCCESS = 0; // 请求成功
    public static final int TRANSACTION_ID_ERROR = -1; // 请求包编号为空
    public static final int UNKNOW_USER = -2; // 未知用户名
    public static final int AUTH_FAILURE = -3; // 请求包编号为空
    public static final int UNKNOW_REQUEST_TYPE = -4; // 未知请求包类型
    public static final int XML_FORMAT_ERROR = -5; // XML包格式错误
    public static final int SP_CODE_NULL = -6; // 特服号为空
    public static final int DEST_TERMINAL_ID_NULL = -7; // 接受方手机号码为空
    public static final int ACT_ID_NULL = -8; // 活动代码为空
    public static final int SERVICE_ID_NULL = -9; // 业务代码为空
    public static final int FEE_CODE_NULL = -10; // 计费代码为空
    public static final int FEE_TYPE_NULL = -11; // 计费类型为空
    public static final int BASE64_ENCODE_ERROR = -12; // Base64加密出错
    public static final int ZIP_FORMAT_ERROR = -13; // Zip包格式错误
    public static final int MSG_CONTENT_NULL = -14; // 信息内容为空
    public static final int ZIP_FILE_NOT_EXIST = -100; // 无法找到压缩文件
    public static final int CONNECT_ERROE = -500; // 发送请求时网络连接出错
    public static final int UNKNOW_ERROR = -1000; // 其它错误
}