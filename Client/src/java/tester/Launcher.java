package tester;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class Launcher {

    /**
     * Запуск jar-файла c определенными входными данными
     * @param labPath путь к программе
     * @param inputTestFile файл с входными данными
     * @param willDel подлежит ли файл удалению
     * @return результат работы программы
     */
    private static String runJava(String labPath, File inputTestFile, boolean willDel){
        String inputTest = Tester.readFile(inputTestFile);
        System.out.println("Running \".jar\" file: " + labPath);
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
            runProcess.destroy();
            if (willDel) {
                Files.deleteIfExists(new File(labPath).toPath());
            }
        } catch (IOException e) {
            if (runProcess != null) {
                runProcess.destroy();
            }
            if (willDel) {
                try {
                    Files.deleteIfExists(new File(labPath).toPath());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            System.out.println("Error occurred while running file.");
            System.out.println("------------");
//            e.printStackTrace();
            return null;
        }
        System.out.println("File ran successfully");
        System.out.println("------------");
        return outputString;
    }

    /**
     * Запуск out-файла c определенными входными данными
     * @param labPath путь к программе
     * @param inputTestFile файл с входными данными
     * @param willDel подлежит ли файл удалению
     * @return результат работы программы
     */
    private static String runCpp(String labPath, File inputTestFile, boolean willDel){
        String inputTest = Tester.readFile(inputTestFile);
        System.out.println("Running \".out\" or \".exe\" file: " + labPath);
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
            runProcess.destroy();
            if (willDel) {
                Files.deleteIfExists(new File(labPath).toPath());
            }
        } catch (IOException e) {
            if (runProcess != null) {
                runProcess.destroy();
            }
            if (willDel) {
                try {
                    Files.deleteIfExists(new File(labPath).toPath());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            System.out.println("Error occurred while running file.");
            System.out.println("------------");
//            e.printStackTrace();
            return null;
        }
        System.out.println("File ran successfully");
        System.out.println("------------");
        return outputString;
    }

    public static String getJavaOutput(String labName, File inputTestFile, boolean willDel){
        return runJava(labName, inputTestFile, willDel);
    }

    public static String getCppOutput(String labName, File inputTestFile, boolean willDel){
        return runCpp(labName, inputTestFile, willDel);
    }

}
