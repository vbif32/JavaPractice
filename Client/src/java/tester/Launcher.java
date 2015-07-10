package tester;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Класс запускающий код теста
 * Ответственный: Глеб Додонов
 */
public class Launcher {

    /**
     * Запуск jar-файла c определенными входными данными
     * @param labPath путь к программе
     * @param inputTestFile файл с входными данными
     * @return результат работы программы
     */
    private static String runJava(String labPath, File inputTestFile){
        if (!labPath.contains(".jar")){
            return null;
        }
        System.out.println("Running \".jar\" file: " + labPath);
        System.out.println("Reading test file (in): ");
        String inputTest = Tester.readFile(inputTestFile);
        System.out.println("input string: " + inputTest);
        String outputString = "";
        List<String> params = java.util.Arrays.asList("java", "-jar", labPath, "\"" + inputTest + "\"");
        ProcessBuilder builder = new ProcessBuilder(params);
        Process runProcess = null;
        try {
            runProcess = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
            String line = reader.readLine();
            while(line != null){
                outputString += line;
                line = reader.readLine();
            }
            reader.close();
            runProcess.waitFor();
        } catch (IOException e) {
            if (runProcess != null) {
                try {
                    runProcess.waitFor();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            System.out.println("Error occurred while running file.");
            System.out.println("------------");
//            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("File ran successfully");
        System.out.println("------------");
        return outputString;
    }

    /**
     * Запуск out-файла c определенными входными данными
     * @param labPath путь к программе
     * @param inputTestFile файл с входными данными
     * @return результат работы программы
     */
    private static String runCpp(String labPath, File inputTestFile){
        if (!labPath.contains(".exe") && !labPath.contains(".out")){
            return null;
        }
        System.out.println("Running \".out\" or \".exe\" file: " + labPath);
        System.out.println("Reading test file (in): ");
        String inputTest = Tester.readFile(inputTestFile);
        System.out.println("input string: " + inputTest);
        String outputString = "";
        List<String> params = java.util.Arrays.asList(labPath, "\"" + inputTest + "\"");
        ProcessBuilder builder = new ProcessBuilder(params);
        Process runProcess = null;
        try {
            runProcess = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
            String line = reader.readLine();
            while(line != null){
                outputString += line;
                line = reader.readLine();
            }
            reader.close();
            runProcess.waitFor();
        } catch (IOException e) {
            if (runProcess != null) {
                try {
                    runProcess.waitFor();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            System.out.println("Error occurred while running file.");
            System.out.println("------------");
//            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("File ran successfully");
        System.out.println("------------");
        return outputString;
    }

    protected static String getJavaOutput(String labName, File inputTestFile){
        return runJava(labName, inputTestFile);
    }

    protected static String getCppOutput(String labName, File inputTestFile){
        return runCpp(labName, inputTestFile);
    }

}
