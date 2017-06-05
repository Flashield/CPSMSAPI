package com.ntyd.msg;

import java.util.ArrayList;

public class MainProcess {		
	/**
	 * @param args
	 */
	
	
	public static void main(String[] args) throws Exception {				
		// TODO Auto-generated method stub
		
		//System.out.println(System.getProperty("file.encoding"));
		
		while(true)
		{
			DALHelper dalHelper = null;
			int send_cnt = 0;
			try
			{
				LocalListener listener = new LocalListener();
					
				dalHelper = new DALHelper();
				//循环侦听管道消息
				while (true)
				{
					Thread.sleep(500);
					if(dalHelper.InitConnection())
					{						
						//打开事务
					    //dalHelper.conn.setAutoCommit(false);			    
					    //发送SMS
					    send_cnt = SendSMS(dalHelper, listener, send_cnt);			    			  
					    
					    //发送MMS
					    send_cnt = SendMMS(dalHelper, listener, send_cnt);			    
					    //事务提交
					    //dalHelper.conn.commit();				    				   
					}		
				}						
			}
			catch(Exception ex)
			{			
				System.out.println("Error: "+ ex.toString());
				try{
				dalHelper.conn.rollback();}
				catch (Exception ex1)
				{
					ex1.printStackTrace();
				}
			}
			Thread.sleep(300000);
			System.out.print("短信接口重启");
		}
		
	    //System.out.print("系统异常中断");	    
	    //System.exit(-1); 
	    }

	/**
	 * 发送短信
	 * @param dalHelper
	 * @param listener
	 */
	private static int SendSMS(DALHelper dalHelper, LocalListener listener, int cnt) throws Exception {				
		ArrayList<SMSProperty> arrayList;
		SMSProperty smsProperty;
		//取得发送的短信(wap)信息
		arrayList = dalHelper.GetMtSMS();
				    
		for(int i = 0; i< arrayList.size(); i++)
		{
			smsProperty = arrayList.get(i);
			/*if(i == 0)
				minSequenceId = smsProperty.sequenceId;
			
			if(i == arrayList.size() - 1)
				maxSequeceId = smsProperty.sequenceId;*/
			
			cnt++;
			
			if(cnt%1000 == 0)
			{
				cnt = 0;
				Thread.sleep(1000);
				System.gc();
			}
			
			//发送信息
			smsProperty.status = String.valueOf(listener.sendSMS(smsProperty));		    				
		}
		
		//添加到日志		    
		dalHelper.AddSmsMtLog(arrayList);		    
		//删除wait表中的信息
		dalHelper.RemoveSmsMtWait(arrayList);
		return cnt;
	}
		
	/**
	 * 发送彩信
	 * @param dalHelper
	 * @param listener
	 */
	private static int SendMMS(DALHelper dalHelper, LocalListener listener, int cnt) throws Exception {				
		//String rootPath = System.class.getClassLoader().getResource("").getPath();
		//String rootPath  = new java.io.File("").getAbsolutePath();				
		ArrayList<MMSProperty> arrayList;
		MMSProperty mmsProperty;
		//取得发送的短信(wap)信息
		arrayList = dalHelper.GetMtMMS();
				    
		for(int i = 0; i< arrayList.size(); i++)
		{
			mmsProperty = arrayList.get(i);
			/*if(i == 0)
				minSequenceId = mmsProperty.sequenceID;			
			
			if(i == arrayList.size() - 1)
				maxSequeceId = mmsProperty.sequenceID;	*/									
			
			//if(mmsProperty.zipContent != null)
			  //mmsProperty.zipFile =  rootPath + Config.zipDir.indexOf("/") +  + mmsProperty.zipFile;	
				//mmsProperty.zipFile =  rootPath.substring(1, rootPath.length() - 4) + Config.zipDir.substring(2) + "/" + mmsProperty.zipFile;	
			
			cnt++;
			
			if(cnt%1000 == 0)
			{
				cnt = 0;
				Thread.sleep(1000);
				System.gc();
			}
			//发送信息
			mmsProperty.sendSate = String.valueOf(listener.sendMMS(mmsProperty));				
		}
		
		//添加到日志		    
		dalHelper.AddMmsMtLog(arrayList);		    
		//删除wait表中的信息
		dalHelper.RemoveMmsMtWait(arrayList);
		
		return cnt;
	}	
		
}
