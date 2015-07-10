package connect;

import protocol.ClientSide;
import query.*;
import reply.*;
import transfer.LabSubmitDate;
import transfer.StudentResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectToServer {

    Socket s;
    int serverPort;
    String address;
    User SaveFromServer;
    String Error;
    Reply tmpqueryResult;

    public boolean TryToConnect() {
        try {
            s = new Socket(address, serverPort);
            s.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Error = "Не удалось подключится к серверу";
            return false;
        }
    }

    public ConnectToServer() {
        serverPort = 1080;
        address = "127.0.0.1";
        Error = "";
    }

    public ConnectToServer(int Port, String ip) {
        serverPort = Port;
        address = ip;
    }

    public boolean LoginIn(LoginRequest loginRequest) //Готово
    {
        try {
            s = new Socket(address, serverPort);
            InputStream request = s.getInputStream();
            OutputStream response = s.getOutputStream();
            tmpqueryResult = ClientSide.transmit(loginRequest, response, request);
            s.close();
            if (tmpqueryResult.getClass().equals(User.class)) {
                SaveFromServer = (User) tmpqueryResult;
                return true;
            } else {
                if (tmpqueryResult.getClass().equals(QueryError.class)) {
                    Error = ((QueryError) tmpqueryResult).message;
                    return false;
                } else {
                    Error = "Незапланированный ответ сервера";
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Error = "IOException";
            return false;
        }
    }

    public Boolean RegisterUser(RegisterRequest registerRequest) //Готово
    {
        try {
            s = new Socket(address, serverPort);
            InputStream request = s.getInputStream();
            OutputStream response = s.getOutputStream();
            tmpqueryResult = ClientSide.transmit(registerRequest, response, request);
            s.close();
            if (tmpqueryResult.getClass().equals(Registration.class)) {
                return ((Registration) tmpqueryResult).isSuccess;
            } else {
                if (tmpqueryResult.getClass().equals(QueryError.class)) {
                    Error = ((QueryError) tmpqueryResult).message;
                    return false;
                } else {
                    Error = "Незапланированный ответ сервера";
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Error = "IOException";
            return false;
        }
    }

    public User UserRequest() {
        return SaveFromServer;
    }

    public String ErrorRequest() {
        return Error;
    }

    public Test TestInOut(TestRequest testRequest) {
        try {
            s = new Socket(address, serverPort);
            InputStream request = s.getInputStream();
            OutputStream response = s.getOutputStream();
            Reply answer = ClientSide.transmit(testRequest, response, request);
            s.close();
            if (answer.getClass().equals(Test.class)) {
                return (Test) answer;
            } else {
                if (answer.getClass().equals(QueryError.class)) {
                    Error = ((QueryError) answer).message;
                    return null;
                } else {
                    Error = "Незапланированный ответ сервера";
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Error = "IOException";
            return null;
        }
    }

    public boolean TestUpload(TestUploadRequest testUploadRequest) {
        try {
            s = new Socket(address, serverPort);
            InputStream request = s.getInputStream();
            OutputStream response = s.getOutputStream();
            Reply answer = ClientSide.transmit(testUploadRequest, response, request);
            s.close();
            if (answer.getClass().equals(TestUploading.class)) {
                return ((TestUploading) answer).isSuccess;
            } else {
                if (answer.getClass().equals(QueryError.class)) {
                    Error = ((QueryError) answer).message;
                    return false;
                } else {
                    Error = "Незапланированный ответ сервера";
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Error = "IOException";
            return false;
        }
    }

    public boolean UploadTestResult(TestResultRequest testResultRequest) {
        try {
            s = new Socket(address, serverPort);
            InputStream request = s.getInputStream();
            OutputStream response = s.getOutputStream();
            Reply answer = ClientSide.transmit(testResultRequest, response, request);
            s.close();
            if (answer.getClass().equals(TestResult.class)) {
                return ((TestResult) answer).isSuccess;
            } else {
                if (answer.getClass().equals(QueryError.class)) {
                    Error = ((QueryError) answer).message;
                    return false;
                } else {
                    Error = "Незапланированный ответ сервера";
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Error = "IOException";
            return false;
        }
    }

    //Дела добрых фей
    public String userStatsRequest(StatsRequest statsRequest) {
        try {
            s = new Socket(address, serverPort);
            Reply reply = ClientSide.transmit(statsRequest, s.getOutputStream(), s.getInputStream());
            s.close();
            if (reply.getClass().equals(Stats.class)) {
                Stats stats = (Stats) reply;
                StudentResult studentResult = null;
                if (stats.list != null && stats.list.size() != 1) {
                    Error = "Незапланированный ответ сервера";
                    return null;
                } else studentResult = stats.list.get(0);     //Не дай бог положат null-элементы...
                StringBuilder sb = new StringBuilder();
                if (studentResult != null && studentResult.dates != null) {
                    for (int i = 0; i < studentResult.dates.size(); i++) {
                        if (sb.length() != 0) sb.append(System.lineSeparator());
                        LabSubmitDate date = studentResult.dates.get(i);
                        sb.append("Лабораторная работа №").append(i + 1).append(": ");
                        sb.append(date.day).append('.').append(date.month).append('.').append(date.year);
                    }
                } else sb.append("Нет данных.");

                return sb.toString();
            } else if (reply.getClass().equals(QueryError.class)) {
                Error = ((QueryError) reply).message;
                return null;
            } else {
                Error = "Незапланированный ответ сервера";
                return null;
            }
        } catch (IOException ioe) {
            Error = ioe.getMessage();
            return null;
        }
    }

    public Stats lecturerStatsRequest(StatsRequest statsRequest) {
        try {
            s = new Socket(address, serverPort);
            Reply reply = ClientSide.transmit(statsRequest, s.getOutputStream(), s.getInputStream());
            s.close();
            if (reply.getClass().equals(Stats.class))
                return ((Stats) reply);
            else if (reply.getClass().equals(QueryError.class)) {
                Error = ((QueryError) reply).message;
                return null;
            } else {
                Error = "Незапланированный ответ сервера";
                return null;
            }
        } catch (IOException ioe) {
            Error = ioe.getMessage();
            return null;
        }
    }

}
