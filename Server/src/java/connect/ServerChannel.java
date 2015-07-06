package connect;

import protocol.ServerSide;
import services.StatsService;
import services.TestService;
import services.UserService;
import shed.Queries;
import shed.Query;
import shed.QueryResult;
import shed.query.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;

/**
 * Класс для работы с одним клиентом в одном потоке
 */
public class ServerChannel implements Runnable {

    private Socket clientSocket;

    public ServerChannel(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    //Выполняет запрос и возвращает результат
    private QueryResult getQueryResult(Query query) throws IOException {

        QueryResult result = null;
        if (clientSocket.isConnected()) {

            switch (query.getType()) {
                case REGISTRATION:
                    result = UserService.registerUser((RegisterApply)query);
                    break;
                case LOGIN:
                    result = UserService.authenticateUser((LoginApply)query);
                    break;
                case TEST:
                    result = TestService.getTest((TestRequest)query);
                    break;
                case RESULT:
                    result = TestService.submitTest((TestSubmit)query);
                    break;
                case STATS:
                    result = StatsService.getStats((StatsRequest)query);
                    break;

            }

        }

        return result;
    }

    //Функция, выполняемая действия до смерти потока
    @Override
    public void run() {
        if (clientSocket.isConnected()) {
            try {
                //Отправляет результаты о запросе
                Query query = ServerSide.receive(clientSocket.getOutputStream(), clientSocket.getInputStream());
                if(query.getType() != Queries.ERROR)
                {
                    QueryResult queryResult = getQueryResult(query);
                    ServerSide.transmit(queryResult,clientSocket.getOutputStream(),clientSocket.getInputStream());
                }
                else
                    log((ErrorReceived)query);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Закрывает соеденение
            try {
                clientSocket.close();
                System.out.println("Client disconnected");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void log(ErrorReceived error) throws IOException {

        final String logFilePath = "C:\\Users\\Пользователь\\Documents\\ServerLog.txt";
        FileWriter fileWriter = new FileWriter(logFilePath,true);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println("[" + Calendar.getInstance().getTime().toString() + "] " + error.message);
        printWriter.flush();
        printWriter.close();

    }
}
