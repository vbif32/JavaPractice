package connect;

import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import protocol.ClientSide;
import shed.*;
import shed.queryResult.*;
import shed.query.*;

import java.lang.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;


/**
 * Created by Andrey on 06.07.2015.
 *
 * Сори все кто ждал
 */
public class ConnectToServer {
    Socket s;

    int serverPort;
    String address;

    QueryResult SaveFromServer;

    public boolean TryToConnect()
    {
        try
        {
            System.out.println("Try socket with IP address " + address + " and port " + serverPort + " ");
            s = new Socket(address, serverPort);

            return true;

        }
        catch (IOException e)
        {
            e.printStackTrace();

            return false;
        }
    }

    public ConnectToServer() {

        serverPort = 6666;
        address = "127.0.0.1";
    }

    public ConnectToServer(int Port,String ip) {

        serverPort = Port;
        address = ip;
    }

    public boolean  LoginIn(Query query)
    {
        if(query.getClass().equals(LoginApply.class))
        {

            try
            {
                System.out.println("Try socket with IP address " + address + " and port " + serverPort + " ");
                s = new Socket(address, serverPort);

                InputStream request = s.getInputStream();
                OutputStream response = s.getOutputStream();

                SaveFromServer = ClientSide.transmit( query ,response ,request );

                if(SaveFromServer.getClass().equals(User.class))
                {
                    return true;
                }else
                {
                    return false;
                }

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
        else
        {
            SaveFromServer = new QueryError("Not LoginApply class");

            return false;
        }

        SaveFromServer = new QueryError("Endless error...");
        return false;
    }

    public Boolean RegisterUser(Query query)
    {
        if(query.getClass().equals(RegisterApply.class))
        {
            try
            {
                System.out.println("Try socket with IP address " + address + " and port " + serverPort + " ");
                s = new Socket(address, serverPort);

                InputStream request = s.getInputStream();
                OutputStream response = s.getOutputStream();

                SaveFromServer = ClientSide.transmit( query ,response ,request );

                if(SaveFromServer.getClass().equals(RegisterResult.class))
                {
                    return ((RegisterResult)SaveFromServer).isRegistered ;

                }else
                {
                    return false;
                }

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
        else
        {
            SaveFromServer = new QueryError("Not RegisterApply class");

            return false;
        }

        SaveFromServer = new QueryError("Endless error...");
        return false;
    }

    public User UserRequest()
    {
        return (User)SaveFromServer;
    }

    public String ErrorRequest()
    {
        return ((QueryError)SaveFromServer).message;
    }

    public static void main(String[] args) {
        new ConnectToServer();
    }
}
