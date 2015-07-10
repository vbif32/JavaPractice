package connect;


import query.ErrorReceived;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;

/**
 * Работа сервера
 */
public class MainServer {

    private int port; //Порт сервера
    private ServerSocket serverSocket;
    private boolean isServerStopped;

    public MainServer(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Запускает сервер
    public void startServer() throws IOException {

        System.out.println("Waiting for client connection...");
        Socket clientSocket = this.serverSocket.accept();


        System.out.println("Connected");
        log("Connected");

        //Открывает новый поток для клиента
        Thread chanelThread = new Thread(new ServerChannel(clientSocket));
        chanelThread.start();

        if (!isServerStopped)
            startServer();
    }

    //Останавливает сервер
    public void stopServer() {
        isServerStopped = true;
    }

    public void createLogFile(String fullFileName) throws IOException {
        File logFile = new File(fullFileName);
        if(logFile.exists())
            logFile = logFile.getAbsoluteFile();
        else
            logFile.createNewFile();
    }

    public static void main(String[] args) throws IOException{
        MainServer server = new MainServer(444);
        server.createLogFile("ServerLog.txt");
        server.startServer();

    }

    public static void log(ErrorReceived error) throws IOException {

        final String logFilePath = "ServerLog.txt";
        FileWriter fileWriter = new FileWriter(logFilePath,true);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println("[" + Calendar.getInstance().getTime().toString() + "] " + error.message);
        printWriter.flush();
        printWriter.close();

    }

    public static void log(String errorMsg) throws IOException {

        final String logFilePath = "ServerLog.txt";
        FileWriter fileWriter = new FileWriter(logFilePath,true);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println("[" + Calendar.getInstance().getTime().toString() + "] " + errorMsg);
        printWriter.flush();
        printWriter.close();

    }
}
