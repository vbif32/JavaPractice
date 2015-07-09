package tester;

import gui.ForTest;

import java.io.*;
import java.util.Objects;

public class Tester {

    /*!!!*/
    ForTest labInf;
    private File labFile;
    private File inputTestFile;
    private File outputTestFIle;
    private boolean isCorrected;
    private String stringResult = "";
    String labId;

    private Tester(String code, ForTest labInf){
        this.labInf = labInf;
        this.labId = "_" + labInf.term + "_" +  labInf.number + "_" + labInf.variant;
        if (labInf.subject.equals("Программирование")) {
            this.labFile = Compiler.compileJar(code, this.labId);
        } else if (labInf.subject.equals("АиСД")) {
            this.labFile  = Compiler.compileCpp(code, this.labId);
        }
        if (this.labFile!=null) {
            this.getTests(labInf.userId, labInf.subject, labInf.term, labInf.number);
        } else {
            this.stringResult = "Ошибка при компиляции файла.";
        }
    }

    private Tester(File labFile, ForTest labInf){
        this.labInf = labInf;
        this.labFile = labInf.laba;
        if (new File(this.labFile.getAbsolutePath()).exists()) {
            this.getTests(labInf.userId, labInf.subject, labInf.term, labInf.number);
        } else {
            this.stringResult = "Файл не найден.";
        }
    }

    /**
     * Запрос тестов
     */
    private void getTests(Integer userId, String subject, Integer term, Integer labNumber){

    }

    private String sendResult(){
        return null;
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
            this.isCorrected = false;
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
            this.isCorrected = false;
            return;
        }
        System.out.println("------------");
        this.isCorrected = labOutput.equals(testOutput);
//        this.isCorrected = labOutput.contains(testOutput);
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

    public static String labTestExecute(ForTest labInf){
        Tester tester;
        if (labInf.code != null && !labInf.code.equals("")){
            tester = new Tester(labInf.code, labInf);
            if (tester.labFile != null){
                tester.compareOutput();
            }
        } else if (labInf.laba != null && new File(labInf.laba.getAbsolutePath()).exists()) {
            tester = new Tester(labInf.laba, labInf);
            tester.compareOutput();
        } else {
            return "Файл или код не найден.";
        }
        if (tester.stringResult.equals("Файл не найден.") || tester.stringResult.equals("Ошибка при компиляции файла.")){
            deleteDirectory(new File("temp"));
            return tester.stringResult;
        }
        String reply = tester.sendResult();
        if (!(reply == null || Objects.equals(reply, ""))){
            tester.stringResult = reply;
        } else {
            if (tester.isCorrected){
                tester.stringResult = "Тест пройден успешно.";
            } else {
                tester.stringResult = "Тест не пройден.";
            }
        }
        deleteDirectory(new File("temp"));
        return tester.stringResult;
    }

}
