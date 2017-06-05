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
        System.out.println("Http服务器启动...");
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
    
    private int port; // 端口号
    
    private MoListener moListener; // 上行消息监听器
    
    private ServerSocket server; // 端口监听服务器
}