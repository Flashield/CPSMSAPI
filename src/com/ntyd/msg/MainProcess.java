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
				//ѭ�������ܵ���Ϣ
				while (true)
				{
					Thread.sleep(500);
					if(dalHelper.InitConnection())
					{						
						//������
					    //dalHelper.conn.setAutoCommit(false);			    
					    //����SMS
					    send_cnt = SendSMS(dalHelper, listener, send_cnt);			    			  
					    
					    //����MMS
					    send_cnt = SendMMS(dalHelper, listener, send_cnt);			    
					    //�����ύ
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
			System.out.print("���Žӿ�����");
		}
		
	    //System.out.print("ϵͳ�쳣�ж�");	    
	    //System.exit(-1); 
	    }

	/**
	 * ���Ͷ���
	 * @param dalHelper
	 * @param listener
	 */
	private static int SendSMS(DALHelper dalHelper, LocalListener listener, int cnt) throws Exception {				
		ArrayList<SMSProperty> arrayList;
		SMSProperty smsProperty;
		//ȡ�÷��͵Ķ���(wap)��Ϣ
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
			
			//������Ϣ
			smsProperty.status = String.valueOf(listener.sendSMS(smsProperty));		    				
		}
		
		//��ӵ���־		    
		dalHelper.AddSmsMtLog(arrayList);		    
		//ɾ��wait���е���Ϣ
		dalHelper.RemoveSmsMtWait(arrayList);
		return cnt;
	}
		
	/**
	 * ���Ͳ���
	 * @param dalHelper
	 * @param listener
	 */
	private static int SendMMS(DALHelper dalHelper, LocalListener listener, int cnt) throws Exception {				
		//String rootPath = System.class.getClassLoader().getResource("").getPath();
		//String rootPath  = new java.io.File("").getAbsolutePath();				
		ArrayList<MMSProperty> arrayList;
		MMSProperty mmsProperty;
		//ȡ�÷��͵Ķ���(wap)��Ϣ
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
			//������Ϣ
			mmsProperty.sendSate = String.valueOf(listener.sendMMS(mmsProperty));				
		}
		
		//��ӵ���־		    
		dalHelper.AddMmsMtLog(arrayList);		    
		//ɾ��wait���е���Ϣ
		dalHelper.RemoveMmsMtWait(arrayList);
		
		return cnt;
	}	
		
}
