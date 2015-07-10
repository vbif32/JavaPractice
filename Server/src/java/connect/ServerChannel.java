package connect;

import protocol.ServerSide;
import query.*;
import reply.Reply;
import services.StatsService;
import services.TestService;
import services.UserService;

import java.io.IOException;
import java.net.Socket;

/**
 * Класс для работы с одним клиентом в одном потоке
 * Ответственный: Антон Баширов
 */
public class ServerChannel implements Runnable {

    private Socket clientSocket;

    public ServerChannel(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    //Выполняет запрос и возвращает результат
    private Reply getQueryResult(Query query) throws IOException {

        Reply result = null;
        if (clientSocket.isConnected()) {

            switch (query.getType()) {
                case REGISTRATION:
                    result = UserService.registerUser((RegisterRequest) query);
                    break;
                case LOGIN:
                    result = UserService.authenticateUser((LoginRequest) query);
                    break;
                case TESTDOWNLOAD:
                    result = TestService.getTest((TestRequest) query);
                    break;
                case TESTRESULT:
                    result = TestService.submitTest((TestResultRequest) query);
                    break;
                case STATS:
                    result = StatsService.getStats((StatsRequest) query);
                    break;
                case TESTUPLOAD:
                    result = TestService.uploadTest((TestUploadRequest) query);
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
                if (query.getType() != Queries.ERROR) {
                    Reply queryResult = getQueryResult(query);
                    if (queryResult == null)
                        MainServer.log("No reply on query");
                    else if (!ServerSide.transmit(queryResult, clientSocket.getOutputStream(), clientSocket.getInputStream()))
                        MainServer.log("Transmit to client failed");
                } else
                    MainServer.log((ErrorReceived) query);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Закрывает соеденение
            try {
                clientSocket.close();
                System.out.println("Client disconnected");
                MainServer.log("Client disconnected");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
