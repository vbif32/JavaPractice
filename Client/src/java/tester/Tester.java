package tester;

import connect.ConnectToServer;
import gui.ForTest;
import query.TestRequest;
import query.TestResultRequest;
import reply.Test;

import java.io.*;
import java.util.Objects;

public class Tester {

    private ForTest labInf;
    private File labFile;
    private File inputTestFile;
    private File outputTestFIle;
    private boolean isCorrect;
    private String stringResult = "";
    private String labId;
    private ConnectToServer connection = new ConnectToServer();

    private Tester(String code, ForTest labInf){
        this.labInf = labInf;
        this.labId = "_" + labInf.term + "_" +  labInf.number + "_" + labInf.variant;
        if (labInf.subject.equals("Программирование")) {
            this.labFile = Compiler.compileJar(code, this.labId);
        } else if (labInf.subject.equals("АиСД")) {
            this.labFile  = Compiler.compileCpp(code, this.labId);
        }
        if (this.labFile!=null) {
            this.setTests(labInf.userId, labInf.subject, labInf.term, labInf.number, labInf.variant);
        } else {
            this.stringResult = "Ошибка при компиляции файла.";
        }
    }

    private Tester(File labFile, ForTest labInf){
        this.labInf = labInf;
        this.labFile = labFile;
        if (new File(this.labFile.getAbsolutePath()).exists()) {
            this.setTests(labInf.userId, labInf.subject, labInf.term, labInf.number, labInf.variant);
        } else {
            this.stringResult = "Файл не найден.";
        }
    }

    /**
     * Запрос тестов
     */
    private void setTests(Integer userId, String subject, Integer term, Integer labNumber, Integer variant){
        TestRequest testRequest = new TestRequest();
        testRequest.id = userId;
        testRequest.labNumber = labNumber;
        testRequest.subject = subject;
        testRequest.term = term;
        testRequest.variant = variant;
        Test test = this.connection.TestInOut(testRequest);
        if (test != null ){
            this.inputTestFile = test.input;
            this.outputTestFIle = test.output;
        } else {
            this.stringResult = "Ошибка при получении тестовых файлов.\n" + this.connection.ErrorRequest();
        }
    }

    private String sendResult(){
        TestResultRequest testResultRequest = new TestResultRequest();
        testResultRequest.id = this.labInf.userId;
        testResultRequest.isCorrect = this.isCorrect;
        testResultRequest.labNumber = this.labInf.number;
        testResultRequest.term = this.labInf.term;
        testResultRequest.subject = this.labInf.subject;
        if(this.connection.UploadTestResult(testResultRequest)){
            return null;
        } else {
            return "Ошибка при отправлении результата на сервер.\n" + this.connection.ErrorRequest();
        }
    }

    /**
     * Сравнение выходных данных программы с данными из теста
     */
    private void compareOutput(){
        String labOutput = "";
        if (this.labFile != null) {
            if (this.labInf.subject.equals("Программирование")) {
                labOutput = Launcher.getJavaOutput(this.labFile.getAbsolutePath(), this.inputTestFile);
            } else if (this.labInf.subject.equals("АиСД")) {
                labOutput = Launcher.getCppOutput(this.labFile.getAbsolutePath(), this.inputTestFile);
            }
        } else {
            this.isCorrect = false;
            System.out.println("File with lab not found.");
            return;
        }
        System.out.println("Reading test file (out).");
        String testOutput = readFile(this.outputTestFIle);
        System.out.println("-");
        System.out.println("Lab output:");
        System.out.println(labOutput);
        System.out.println("-");
        System.out.println("TestOutput: ");
        System.out.println(testOutput);
        System.out.println("-");
        if (testOutput == null || labOutput == null){
            System.out.println("Something went wrong");
            System.out.println("------------");
            this.isCorrect = false;
            return;
        }
        System.out.println("------------");
        this.isCorrect = labOutput.equals(testOutput);
//        this.isCorrect = labOutput.contains(testOutput);
    }

    protected static String readFile(File file){
        if (file == null){
            System.out.println("File not found");
            return null;
        }
        String result = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
            System.out.println("File not found");
            System.out.println("------------");
            return null;
        }
        char[] buf = new char[256];
        int res = -1;
        try {
            while ((res = br.read(buf)) != -1) {
                result += new String(buf, 0, res);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static void deleteDirectory(File theDir){
        if( theDir.exists()) {
            File[] files = theDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        System.out.println("deleting: " + file.getAbsolutePath() + " -> " +  file.delete());
                    }
                }
            }
            System.out.println("deleting: " + theDir.getAbsolutePath() + " -> " + theDir.delete());
        }
    }

    private boolean errorAccured(){
        if (this.stringResult.equals("Файл не найден.") || this.stringResult.equals("Ошибка при компиляции файла.")
                || this.stringResult.contains("Ошибка при получении тестовых файлов.")){
            deleteDirectory(new File("temp"));
            return true;
        }
        return false;
    }

    public static String labTestExecute(ForTest labInf){
        Tester tester;
        if (labInf.code != null && !labInf.code.equals("")){
            tester = new Tester(labInf.code, labInf);
            if (!tester.errorAccured()) {
                tester.compareOutput();
            } else {
                return tester.stringResult;
            }
        } else if (labInf.laba != null && new File(labInf.laba.getAbsolutePath()).exists()) {
            tester = new Tester(labInf.laba, labInf);
            if (!tester.errorAccured()) {
                tester.compareOutput();
            } else {
                return tester.stringResult;
            }
        } else {
            return "Файл или код не найден.";
        }
        String reply = tester.sendResult();
        if (!(reply == null || Objects.equals(reply, ""))){
            tester.stringResult = reply;
        } else {
            if (tester.isCorrect){
                tester.stringResult = "Тест пройден успешно.";
            } else {
                tester.stringResult = "Тест не пройден.";
            }
        }
        deleteDirectory(new File("temp"));
        return tester.stringResult;
    }

}
