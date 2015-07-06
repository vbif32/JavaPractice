package tester;

import java.io.*;

public class Tester {

    /*!!!*/
    private File labFile;
    private boolean isJava;
    private File inputTestFile;
    private File outputTestFIle;

    private Integer userId;
    private String subject;
    private Integer term;
    private Integer labNumber;
    private boolean isCorrected;

    private Tester(String code, boolean isJava, Integer userId, String subject, Integer term, Integer labNumber){
        this.userId = userId;
        this.subject = subject;
        this.term = term;
        this.labNumber = labNumber;
        this.isJava = isJava;
        File tempLabFile;
        if (isJava) {
            /*tempLabFile = new File("Client/src/tester/temp/labFile.jar");
            this.labFile = Compiler.compileJar(code, tempLabFile);
            tempLabFile.deleteOnExit();*/
        } else {
            this.labFile  = Compiler.compileCpp(code);
        }
        this.getTests(userId, subject, term, labNumber);
    }

    private Tester(File labFile, boolean isJava, Integer userId, String subject, Integer term, Integer labNumber){
        this.userId = userId;
        this.subject = subject;
        this.term = term;
        this.labNumber = labNumber;
        this.isJava = isJava;
        this.labFile = labFile;
        this.getTests(userId, subject, term, labNumber);
    }

    /**
     * Запрос тестов
     */
    private void getTests(Integer userId, String subject, Integer term, Integer labNumber){

    }


    private void sendResult(){

    }

    /**
     * Сравнение выходных данных программы с данными из теста
     */
    private void compareOutput(boolean willDel){
        String labOutput = "";
        String testOutput = readFile(this.outputTestFIle);
        if (this.isJava){
            labOutput = Launcher.getJavaOutput(this.labFile.getAbsolutePath(), this.inputTestFile, willDel);
        } else {
            labOutput = Launcher.getCppOutput(this.labFile.getAbsolutePath(), this.inputTestFile, willDel);
        }
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
        String result = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
            System.out.println("Test file not found");
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

    public static boolean labTestExecute(File labFile, String code, boolean isJava, Integer userId, String subject, Integer term,
                                 Integer labNumber){
        Tester tester;
        if (code != null){
            tester = new Tester(code, isJava, userId, subject, term, labNumber);
            tester.compareOutput(true);
        } else if (labFile != null) {
            tester = new Tester(labFile, isJava, userId, subject, term, labNumber);
            tester.compareOutput(false);
        } else {
            return false;
        }
        tester.sendResult();
        return tester.isCorrected;
    }

}
