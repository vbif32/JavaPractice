package connect;

import protocol.ServerSide;
import shed.Query;
import shed.QueryResult;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

/**
 * Класс для работы с одним клиентом в одном потоке
 */
public class ServerChanel implements Runnable {

    private Socket clientSocket;

    public ServerChanel(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    //Выполняет запрос и возвращает результат
    private QueryResult getQueryResult(Query query) throws IOException {

        QueryResult result = null;
        if (clientSocket.isConnected()) {

            switch (query.getType()) {
                //TODO: Действия над объектами, полученными из запроса
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
                ServerSide.transmit(
                        getQueryResult(ServerSide.receive(clientSocket.getOutputStream(), clientSocket.getInputStream())),
                        clientSocket.getOutputStream(),
                        clientSocket.getInputStream()
                );
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
}
