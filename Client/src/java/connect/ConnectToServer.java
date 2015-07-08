package connect;

import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import protocol.ClientSide;

import query.*;
import reply.*;
import transfer.*;

import gui.LocalUser;

import javax.jws.soap.SOAPBinding;
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
    /*
    public Integer id; // идентификатор доступа в системе
    public String surname;  // Фамилия
    public String name;     // Имя
    public String secondName;// Отчество
    public String group; // Группа
    public Boolean isLecturer;
    */
    User SaveFromServer ;
    QueryError Error;

    /*
    SaveFromServer.surname=" ";

    User user=new User();
    user.group=group;
    user.name=name;
    user.surname=surname;
    user.secondName=secondName;
    user.isLecturer=isLecturer;
    */
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

    public boolean  LoginIn(Query query) //Готово
    {
        if(query.getClass().equals(LoginRequest.class))
        {
            return true; //Для тестирования GUI
            /*
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
            */

        }
        else
        {
            Error = new QueryError("Not LoginApply class");

            return false;
        }
    }

    public Boolean RegisterUser(Query query) //Готово
    {
        if(query.getClass().equals(RegisterRequest.class))
        {
            return true; //Для тестирования GUI
            /*
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
            */
        }
        else
        {
            Error = new QueryError("Not RegisterApply class");

            return false;
        }

    }

    public User UserRequest()
    {
        SaveFromServer = new User();

        SaveFromServer.group="ИИБ-1-14";
        SaveFromServer.name="Вася";
        SaveFromServer.surname="Петин";
        SaveFromServer.secondName="Петюнин";
        SaveFromServer.isLecturer=true;
        return SaveFromServer;
    }

    public String ErrorRequest()
    {
        return ((QueryError)Error).message;
    }

    public static void main(String[] args) {
        new ConnectToServer();
    }
}
