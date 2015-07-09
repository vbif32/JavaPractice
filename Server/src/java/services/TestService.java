package services;

import managers.FileSystemManager;
import query.*;
import reply.*;

import java.io.File;

/**
 */
public class TestService {



    //Возвращает тесты для лаб
    public static Reply getTest(TestRequest testRequest) {
        Integer idTmp = testRequest.id;
        String subjectTmp = testRequest.subject;
        Integer termTmp = testRequest.term;
        Integer labNumberTmp = testRequest.labNumber;
        Integer variantTmp = testRequest.variant;
        return  new Test(FileSystemManager.searchFile("input.txt", "./"+subjectTmp+File.separator+termTmp+File.separator+labNumberTmp+File.separator+variantTmp),FileSystemManager.searchFile("output.txt", "./"+subjectTmp+File.separator+termTmp+File.separator+labNumberTmp+File.separator+variantTmp));
    }

    //Заполняет БД датой сдачи лабы
    public static Reply submitTest(TestResultRequest testResultRequest) {

        return  new TestResult();//true
    }

    //Добавляет файлы тестов на сервер
    public static Reply uploadTest (TestUploadRequest testUploadRequest){
        boolean res = false;
        Integer idTmp = testUploadRequest.id;
        String subjectTmp = testUploadRequest.subject;
        Integer termTmp = testUploadRequest.term;
        Integer labNumberTmp = testUploadRequest.labNumber;
        Integer variantTmp = testUploadRequest.variant;
        File tmpInput = new File(".");
        tmpInput = testUploadRequest.input;
        File tmpOutput = new File (".");
        tmpOutput = testUploadRequest.output;
        if(!FileSystemManager.isFileHere(""+subjectTmp,".")){
            FileSystemManager.createEmptyDir("./"+subjectTmp);
        }
        if(!FileSystemManager.isFileHere(""+termTmp,"./"+subjectTmp)){
            FileSystemManager.createEmptyDir("./"+subjectTmp+File.separator+termTmp);
        }
        if(!FileSystemManager.isFileHere(""+labNumberTmp,"./"+subjectTmp+File.separator+termTmp)){
            FileSystemManager.createEmptyDir("./"+subjectTmp+File.separator+termTmp+File.separator+labNumberTmp);
        }
        if(!FileSystemManager.isFileHere(""+variantTmp,"./"+subjectTmp+File.separator+termTmp+File.separator+labNumberTmp)){
            FileSystemManager.createEmptyDir("./"+subjectTmp+File.separator+termTmp+File.separator+labNumberTmp+File.separator+variantTmp);
        }
        if (FileSystemManager.isFileHere(""+variantTmp, "./"+subjectTmp+File.separator+termTmp+File.separator+labNumberTmp)){
            FileSystemManager.copyFiles(tmpInput.getPath(), "./"+subjectTmp+File.separator+termTmp+File.separator+labNumberTmp+File.separator+variantTmp);
            FileSystemManager.copyFiles(tmpOutput.getPath(), "./"+subjectTmp+File.separator+termTmp+File.separator+labNumberTmp+File.separator+variantTmp);
        }
        if (FileSystemManager.isFileHere(tmpInput.getName(), "./"+subjectTmp+File.separator+termTmp+File.separator+labNumberTmp+File.separator+variantTmp)&&(FileSystemManager.isFileHere(tmpOutput.getName(), "./"+subjectTmp+File.separator+termTmp+File.separator+labNumberTmp+File.separator+variantTmp))){
            res = true;
        }
        FileSystemManager.deleteFile("./"+tmpInput.getName());
        FileSystemManager.deleteFile("./"+tmpOutput.getName());
        return new TestUploading(res);
    }
}