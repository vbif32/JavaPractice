package connect;


import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
        server.createLogFile("C:\\Users\\Пользователь\\Documents\\ServerLog.txt");
        server.startServer();

    }


}
