package tester;

import java.io.*;

public class Tester {

    /*!!!*/
    private File labFile;
    private boolean isJava;
    private String id;
    private File inputTestFile;
    private File outputTestFIle;

    private Tester(String code, boolean isJava, String id){
        this.id = id;
        this.isJava = isJava;
        File tempLabFile;
        if (isJava) {
            /*tempLabFile = new File("Client/src/tester/temp/labFile.jar");
            this.labFile = Compiler.compileJar(code, tempLabFile);
            tempLabFile.deleteOnExit();*/
        } else {
            this.labFile  = Compiler.compileCpp(code);
        }
        this.getTests();
    }

    private Tester(File labFile, boolean isJava, String id){
        this.id = id;
        this.isJava = isJava;
        this.labFile = labFile;
        this.getTests();
    }

    /**
     * Запрос тестов
     */
    private void getTests(){
    }

    /**
     * Сравнение выходных данных программы с данными из теста
     * @return true/false (недоработано)
     */
    private boolean compareOutput(boolean willDel){
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
            return false;
        }
        System.out.println("------------");
//        return labOutput.equals(testOutput);
        return labOutput.contains(testOutput);
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

    /*private boolean singleTestExecute(){
        return true;
    }*/

    /**
     * Запуск тестирования(в виде исходного кода)
     * @param code - осходный код
     * @param isJava - предмет
     * @param id
     * @return (недоработано)
     */
    public static boolean testsExecute(String code, boolean isJava, String id){
        return new Tester(code, isJava, id).compareOutput(true);
    }

    /**
     * Запуск тестирования (в виде исполняемого файла)
     * @param labFile исполняемый файл
     * @param isJava - предмет
     * @param id
     * @return (недоработано)
     */
    public static boolean testsExecute(File labFile, boolean isJava, String id){
        return new Tester(labFile, isJava, id).compareOutput(false);
    }

}
