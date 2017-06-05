package com.ntyd.msg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.xgll.Config;
import com.xgll.util.Log;
import com.xgll.util.*;

public class DALHelper {
	private static String oracleUrToConnect  = null;
	private static String oracleDriverName = "oracle.jdbc.driver.OracleDriver";
	//private static String oracleDriverName = "oracle.jdbc";
	private static String user = null;
	private static String password = null;
	public Connection conn = null;	
	public static String logFileName = Config.logDir.replace("/", "\\") + "error.log";
	private PreparedStatement preparedStatement = null;
	
	public DALHelper() throws Exception
	{
		try
		{
			Class.forName(oracleDriverName);					
		}
		catch(Exception ex)
		{
			Log.printError(ex, "", logFileName);
			System.out.println("Error in DALHelper, is :\n" + ex.toString());
			System.exit(-1);
		}
	}	
	
	public boolean InitConnection()
	{		
		try
		{
			oracleUrToConnect=Config.DBurl;
			user=Config.DBuser;
			password=Config.DBpass;
			if(conn == null)
			  conn = DriverManager.getConnection(oracleUrToConnect, user, password);					
		}
		catch(Exception ex)
		{
			Log.printError(ex, "Excepion in method named InitConnection", logFileName);		
		}
		
	    return conn == null ? false : true;				
	}	
	
	public ArrayList<SMSProperty> GetMtSMS() throws Exception 
	{		
		ArrayList<SMSProperty> arrayList = new ArrayList<SMSProperty>();
		SMSProperty smsProperty = null;		
		
		try
		{					    		
		    String sql ="SELECT * FROM SMS_MT_WAIT WHERE (REQUEST_TIME IS NULL OR to_date(REQUEST_TIME,'yyyymmddhh24miss')<SYSDATE) AND substr(sp_code,1,8)='10658339'";
		    preparedStatement =	conn.prepareStatement(sql);
		    ResultSet resultSet = preparedStatement.executeQuery();
		    //ResultSet resultSet = preparedStatement.getResultSet();
		    //resultSet.beforeFirst();
		    while(resultSet.next())
		    {
		    	smsProperty = new SMSProperty();
		    	smsProperty.sequenceId = resultSet.getString("SEQUENCE_ID");
		    	smsProperty.actCode = resultSet.getString("ACT_CODE");
		    	smsProperty.spCode = resultSet.getString("SP_CODE");
		    	smsProperty.feeTerminalId = resultSet.getString("FEE_TERMINAL_ID");
		    	smsProperty.destTerminalId = resultSet.getString("DEST_TERMINAL_ID");
		    	smsProperty.registerDelivery = resultSet.getString("REGISTER_DELIVERY");
		    	smsProperty.msgContent = resultSet.getString("MSG_CONTENT");
		    	smsProperty.requestTime = resultSet.getString("REQUEST_TIME");
		    	smsProperty.serviceId = resultSet.getString("SERVICE_ID");
		    	smsProperty.feeType = resultSet.getString("FEE_TYPE");
		    	smsProperty.feeCode = resultSet.getString("FEE_CODE");
		    	smsProperty.msgFormat = resultSet.getString("MSG_FORMAT");
		    	smsProperty.msgLevel = resultSet.getString("MSG_LEVEL");
		    	smsProperty.validTime = resultSet.getString("VALID_TIME");
		    	smsProperty.areaCode = resultSet.getString("AREA_CODE");
		    	smsProperty.operatorCode = resultSet.getString("OPERATOR_CODE");
		    	smsProperty.pid = resultSet.getString("PID");
		    	smsProperty.userid = resultSet.getString("USERID");
		    	smsProperty.linkid = resultSet.getString("LINKID");
			    
			    arrayList.add(smsProperty);
		    }		    		    		   
		}		
		catch(Exception ex)
		{
			arrayList = null;
			Log.printError(ex, "Excepion in method named GetMtSMS", logFileName);					
		}	
		finally
		{			
			preparedStatement.close();					
		}
		
		return arrayList;
	}
	
	
	public boolean AddSmsMtLog(ArrayList<SMSProperty> arrayList) throws Exception
	{				
		try
		{	
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("INSERT INTO SMS_MT_LOG(SEQUENCE_ID, ACT_CODE, SP_CODE, FEE_TERMINAL_ID, DEST_TERMINAL_ID, REGISTER_DELIVERY, MSG_CONTENT, REQUEST_TIME,  SERVICE_ID, FEE_TYPE, FEE_CODE, MSG_FORMAT, MSG_LEVEL, VALID_TIME, AREA_CODE, OPERATOR_CODE, PID, USERID, LINKID, MSG_ID, SUBMIT_TIME, STATUS) ");
			stringBuilder.append("SELECT seq_sms_log.nextval, ACT_CODE, SP_CODE, FEE_TERMINAL_ID, DEST_TERMINAL_ID, REGISTER_DELIVERY, ?, REQUEST_TIME,  SERVICE_ID, FEE_TYPE, FEE_CODE, MSG_FORMAT, MSG_LEVEL, VALID_TIME, AREA_CODE, OPERATOR_CODE, PID, USERID, LINKID, null, to_char(sysdate, 'yyyymmddhh24miss'), ? ");
			stringBuilder.append("FROM sms_mt_wait where SEQUENCE_ID = ? ");
			  
			preparedStatement = conn.prepareStatement(stringBuilder.toString());				
		    
			for(int i = 0; i < arrayList.size(); i++)
			{
				SMSProperty smsProperty = arrayList.get(i);
				//新增接口加密功能（20080806）
				if (smsProperty.registerDelivery.equals("1"))
				{preparedStatement.setString(1, new MD5().getMD5ofStr(smsProperty.msgContent));}
				else
				{preparedStatement.setString(1, smsProperty.msgContent);}
				
				preparedStatement.setString(2, smsProperty.status);
			    preparedStatement.setString(3, smsProperty.sequenceId);	
			    preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();
			return true;
		}	
		catch(Exception ex)
		{
			Log.printError(ex, "Excepion in method named AddSmsMtLog", logFileName);	
			return false;					
		}	
		finally
		{
			preparedStatement.close();
		}
	}
	
	public boolean RemoveSmsMtWait(ArrayList<SMSProperty> arrayList) throws Exception
	{		
		boolean isSuccess = true;
		try
		{
			String sql ="delete from SMS_MT_WAIT where sequence_id = ?";
		    preparedStatement =	conn.prepareStatement(sql);
		    for(int i = 0; i < arrayList.size(); i++)
			{	SMSProperty smsProperty = arrayList.get(i);													
			    preparedStatement.setString(1, smsProperty.sequenceId);	
			    preparedStatement.addBatch();
			}
		    
			preparedStatement.executeBatch();			   
		}	
		catch(Exception ex)
		{
			isSuccess = false;
			Log.printError(ex, "Excepion in method named RemoveSmsMtWait", logFileName);			
		}
		finally
		{
			preparedStatement.close();
		}
		
		return isSuccess;
	}	
	
	
	public ArrayList<MMSProperty> GetMtMMS()throws Exception
	{		
		ArrayList<MMSProperty> arrayList = new ArrayList<MMSProperty>();
		MMSProperty mmsProperty = null;
		
		try
		{					    		
		    String sql ="SELECT * FROM MMS_MT_WAIT WHERE (REQUEST_TIME IS NULL OR to_date(REQUEST_TIME,'yyyymmddhh24miss')<SYSDATE) AND substr(sp_code,1,8)='10658339'";
		    preparedStatement =	conn.prepareStatement(sql);
		    ResultSet resultSet = preparedStatement.executeQuery();		  
		    while(resultSet.next())
		    {
		    	mmsProperty = new MMSProperty();
		    	mmsProperty.sequenceID = resultSet.getString("SEQUENCE_ID"); 
		    	mmsProperty.actCode = resultSet.getString("ACT_CODE");  // 活动代码
		    	mmsProperty.spCode = resultSet.getString("SP_CODE");		    			  		    			 
        		mmsProperty.sfrom = resultSet.getString("SFROM");// 计费手机号码
        		mmsProperty.sto = resultSet.getString("STO"); // 接受方手机号码
        		mmsProperty.sto2 = resultSet.getString("STO2"); // 抄送方手机号码
        		mmsProperty.ywdm = resultSet.getString("YWDM"); // 业务代码
        		mmsProperty.fee = resultSet.getString("FEE"); // 费率
        		mmsProperty.subject = resultSet.getString("SUBJECT"); // 彩信标题
        		mmsProperty.zipContent = resultSet.getBlob("CONTENTS"); // 彩信内容zip格式的压缩包
        		mmsProperty.needreply = resultSet.getString("NEEDREPLY"); // 是否需要状态报告
        		mmsProperty.linkid  = resultSet.getString("LINKID");// link id		    			   
			    
			    arrayList.add(mmsProperty);
		    }		    		    		   
		}
		catch(Exception ex)
		{
			arrayList = null;
			Log.printError(ex, "Excepion in method named GetMtMMS", logFileName);							
		}
		finally
		{
			preparedStatement.close();
		}
		
		return arrayList;
	}
	
	public boolean AddMmsMtLog(ArrayList<MMSProperty> arrayList)throws Exception
	{		
		try
		{	
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("INSERT INTO MMS_MT_LOG( SEQUENCE_ID, XXLY, SUBJECT, SFROM, STO, STO2, REQUEST_TIME, SEND_TIME, SENDSTAT, RETMESSAGE, SMILNAME, CONTENTS, FEE,  YWDM,  NEEDREPLY, ACT_CODE, LINKID, MMSC_NUMBER, SP_CODE) ");
			stringBuilder.append("SELECT seq_mms_log.nextval, XXLY, SUBJECT, SFROM, STO, STO2, REQUEST_TIME, to_char(sysdate, 'yyyymmddhh24miss'), ?, NULL,  SMILNAME, CONTENTS, FEE,  YWDM,  NEEDREPLY, ACT_CODE, LINKID, MMSC_NUMBER, SP_CODE ");
			stringBuilder.append("FROM mms_mt_wait where SEQUENCE_ID = ? ");
			  
			preparedStatement = conn.prepareStatement(stringBuilder.toString());				
		    
			for(int i = 0; i < arrayList.size(); i++)
			{	MMSProperty mmsProperty = arrayList.get(i);				   
				preparedStatement.setString(1, mmsProperty.sendSate);
				//preparedStatement.setBlob(2, mmsProperty.zipContent);
			    preparedStatement.setString(2, mmsProperty.sequenceID);	
			    preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();			
		}	
		catch(Exception ex)
		{
			Log.printError(ex, "Excepion in method named AddMmsMtLog", logFileName);	
			return false;					
		}	
		finally
		{
			preparedStatement.close();
		}
		
		return true;
	}
	
	public boolean RemoveMmsMtWait(ArrayList<MMSProperty> arrayList)throws Exception
	{		
		boolean isSuccess = true;
		try
		{
			String sql ="delete from MMS_MT_WAIT where sequence_id = ?";			
		    preparedStatement =	conn.prepareStatement(sql);
		    for(int i = 0; i < arrayList.size(); i++)
			{	MMSProperty mmsProperty = arrayList.get(i);				   				
			    preparedStatement.setString(1, mmsProperty.sequenceID);	
			    preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();		    
		}	
		catch(Exception ex)
		{
			isSuccess = false;
			Log.printError(ex, "Excepion in method named RemoveMmsMtWait", logFileName);			
		}
		finally
		{
			preparedStatement.close();
		}
		
		return isSuccess;
	}		   	
}

