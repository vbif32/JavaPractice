package tester;

import gui.ForTest;

import java.io.*;

public class Tester {

    /*!!!*/
    ForTest labInf;
    private File labFile;
    private File inputTestFile;
    private File outputTestFIle;
    private boolean isCorrected;

    private Tester(String code, ForTest labInf){
        this.labInf = labInf;
        String labId = String.valueOf(labInf.term + labInf.subject + labInf.number);
        File tempLabFile;
        if (labInf.subject == 0) { //исправить идентификаторы предметов
            /*tempLabFile = new File("Client/src/tester/temp/labFile.jar");
            this.labFile = Compiler.compileJar(code, tempLabFile);
            tempLabFile.deleteOnExit();*/
        } else if (labInf.subject == 1) { //исправить идентификаторы предметов
            this.labFile  = Compiler.compileCpp(code, labId);
        }
        this.getTests(labInf.userId, labInf.subject, labInf.term, labInf.number);
    }

    private Tester(File labFile, ForTest labInf){
        this.labInf = labInf;
        this.labFile = labInf.laba;
        this.getTests(labInf.userId, labInf.subject, labInf.term, labInf.number);
    }

    /**
     * Запрос тестов
     */
    private void getTests(Integer userId, Integer subject, Integer term, Integer labNumber){

    }

    private void sendResult(){

    }

    /**
     * Сравнение выходных данных программы с данными из теста
     */
    private void compareOutput(){
        String labOutput = "";
        if (this.labFile != null) {
            if (this.labInf.subject == 0) { //исправить идентификаторы предметов
                labOutput = Launcher.getJavaOutput(this.labFile.getAbsolutePath(), this.inputTestFile);
            } else if (this.labInf.subject == 1) { //исправить идентификаторы предметов
                labOutput = Launcher.getCppOutput(this.labFile.getAbsolutePath(), this.inputTestFile);
            }
        } else {
            this.isCorrected = false;
            System.out.println("File with lab not found.");
            return;
        }
        System.out.println("Reading test file (out): ");
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

    public static boolean labTestExecute(ForTest labInf){
        Tester tester;
        if (!labInf.code.equals("") && labInf.code != null){
            tester = new Tester(labInf.code, labInf);
            tester.compareOutput();
        } else if (labInf.laba != null) {
            tester = new Tester(labInf.laba, labInf);
            tester.compareOutput();
        } else {
            return false;
        }
        tester.sendResult();
        return tester.isCorrected;
    }

}
