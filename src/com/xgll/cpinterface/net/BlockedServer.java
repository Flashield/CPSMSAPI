package com.xgll.cpinterface.net;
import java.net.ServerSocket;
import java.net.Socket;

import com.xgll.Config;
import com.xgll.cpinterface.MoListener;
import com.xgll.util.Log;

public class BlockedServer extends Thread
{
    public BlockedServer(MoListener moListener) throws Exception
    {
        this.port = Config.port;
        this.moListener = moListener;
        this.server = new ServerSocket(port);
    }

    public BlockedServer(int port, MoListener moListener) throws Exception
    {
        this.port = port;
        this.moListener = moListener;
        this.server = new ServerSocket(port);
    }

    public void run()
    {
        System.out.println("Http����������...");
        Socket socket = null;
        try
        {
            while (! Thread.interrupted())
            {
                socket = server.accept();
                new ReceiveThread(socket, moListener).start();
                Thread.sleep(20);
            }
        }
        catch (Exception e)
        {
            Log.printError(e, "", Config.logDir + "error.log");
        }
    }
    
    private int port; // �˿ں�
    
    private MoListener moListener; // ������Ϣ������
    
    private ServerSocket server; // �˿ڼ���������
}