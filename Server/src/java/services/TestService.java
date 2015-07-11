package services;

import connect.MainServer;
import managers.FileSystemManager;
import query.TestRequest;
import query.TestResultRequest;
import query.TestUploadRequest;
import reply.Reply;
import reply.Test;
import reply.TestResult;
import reply.TestUploading;

import java.io.File;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс сервиса тестов
 * Ответственный: Денис Власов
 */
public class TestService {

    /**
     * Возвращает тесты для лаб
     *
     * @param testRequest
     * @return
     */
    public static Reply getTest(TestRequest testRequest) {
        Integer idTmp = testRequest.id;
        String subjectTmp = testRequest.subject;
        Integer termTmp = testRequest.term;
        Integer labNumberTmp = testRequest.labNumber;
        Integer variantTmp = testRequest.variant;
        return new Test(FileSystemManager.searchFile("input.txt", "./" + subjectTmp + File.separator + termTmp + File.separator + labNumberTmp + File.separator + variantTmp), FileSystemManager.searchFile("output.txt", "./" + subjectTmp + File.separator + termTmp + File.separator + labNumberTmp + File.separator + variantTmp));
    }

    /**
     * Заполняет БД датой сдачи лабы
     *
     * @param testResultRequest
     * @return
     */
    public static Reply submitTest(TestResultRequest testResultRequest) {

        if(!testResultRequest.isCorrect)
            return  new TestResult(false);
        else
        {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();

            java.sql.Date sqlDate = java.sql.Date.valueOf(df.format(date));

            MainServer.dbManager.addLabDates(testResultRequest.id,testResultRequest.subject,testResultRequest.term,testResultRequest.labNumber,sqlDate);
        }
        return new TestResult(true);//true
    }

    /**
     * Добавляет файлы тестов на сервер
     *
     * @param testUploadRequest
     * @return
     */
    public static Reply uploadTest(TestUploadRequest testUploadRequest) {
        boolean res = false;
        Integer idTmp = testUploadRequest.id;
        String subjectTmp = testUploadRequest.subject;
        Integer termTmp = testUploadRequest.term;
        Integer labNumberTmp = testUploadRequest.labNumber;
        Integer variantTmp = testUploadRequest.variant;
        File tmpInput = new File(".");
        tmpInput = testUploadRequest.input;
        File tmpOutput = new File(".");
        tmpOutput = testUploadRequest.output;
        if (!FileSystemManager.isFileHere("" + subjectTmp, ".")) {
            FileSystemManager.createEmptyDir("./" + subjectTmp);
        }
        if (!FileSystemManager.isFileHere("" + termTmp, "./" + subjectTmp)) {
            FileSystemManager.createEmptyDir("./" + subjectTmp + File.separator + termTmp);
        }
        if (!FileSystemManager.isFileHere("" + labNumberTmp, "./" + subjectTmp + File.separator + termTmp)) {
            FileSystemManager.createEmptyDir("./" + subjectTmp + File.separator + termTmp + File.separator + labNumberTmp);
        }
        if (!FileSystemManager.isFileHere("" + variantTmp, "./" + subjectTmp + File.separator + termTmp + File.separator + labNumberTmp)) {
            FileSystemManager.createEmptyDir("./" + subjectTmp + File.separator + termTmp + File.separator + labNumberTmp + File.separator + variantTmp);
        }
        if (FileSystemManager.isFileHere("" + variantTmp, "./" + subjectTmp + File.separator + termTmp + File.separator + labNumberTmp)) {
            FileSystemManager.copyFiles(tmpInput.getPath(), "./" + subjectTmp + File.separator + termTmp + File.separator + labNumberTmp + File.separator + variantTmp);
            FileSystemManager.copyFiles(tmpOutput.getPath(), "./" + subjectTmp + File.separator + termTmp + File.separator + labNumberTmp + File.separator + variantTmp);
        }
        if (FileSystemManager.isFileHere(tmpInput.getName(), "./" + subjectTmp + File.separator + termTmp + File.separator + labNumberTmp + File.separator + variantTmp) && (FileSystemManager.isFileHere(tmpOutput.getName(), "./" + subjectTmp + File.separator + termTmp + File.separator + labNumberTmp + File.separator + variantTmp))) {
            res = true;
        }
        FileSystemManager.deleteFile("./" + tmpInput.getName());
        FileSystemManager.deleteFile("./" + tmpOutput.getName());
        return new TestUploading(res);
    }
}