package tester;

import java.io.*;
import java.util.List;

public class Compiler {

    private static void createFolder(){
        File theDir = new File("temp");
        if (!theDir.exists()) {
            System.out.println("creating temp directory");
            boolean result = false;

            try{
                theDir.mkdir();
                result = true;
            }
            catch(SecurityException se){
                //
            }
            if(result) {
                System.out.println("DIR created");
            }
        }
    }

    /**
     * Компиляция cpp файла при помощи g++
     * @param code исходный код
     * @return
     */
    public static File compileCpp(String code, String id){
        System.out.println("------------");
        System.out.println("Compiling cpp file");
        if (code == null){
            System.out.println("    There is no code.");
            return null;
        }
        createFolder();
        File tempLabFile = new File("temp/labFile" + id + ".cpp"); //!!!указать путь
        String compileError = null;
        BufferedReader eis;
        String newFileAbsolutePath = (tempLabFile.getAbsolutePath()).replace(".cpp", ".out");
        try {
            FileWriter fw = new FileWriter(tempLabFile);
            fw.write(code);
            fw.close();
            List<String> params = java.util.Arrays.asList("g++", tempLabFile.getAbsolutePath(), "-o", newFileAbsolutePath);
            ProcessBuilder builder = new ProcessBuilder(params);
            Process compileProcess = builder.start();
            eis = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
            compileError = readBufferedStream(eis);
            compileProcess.waitFor();
            eis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tempLabFile.deleteOnExit();
        if (compileError != null && compileError.equals("\n")) {
            System.out.println("-");
            System.out.println("    Сompile errors:");
            System.out.println(compileError);
            System.out.println("-");
        }
        if (!new File(newFileAbsolutePath).exists()){
            System.out.println("File is not compiled!");
            System.out.println(compileError);
        } else {
            System.out.println("File is compiled!");
        }
        System.out.println("------------");
        return new File(newFileAbsolutePath);
    }

    /**
     * Сборка jar-файла
     */
    public static File compileJar(String code, File labFIle){
        return null;
    }

    private static String readBufferedStream(BufferedReader rdr) throws IOException {
        String result = "";
        char[] buf = new char[256];
        int res = -1;
        while ((res = rdr.read(buf)) != -1) {
            result += new String(buf, 0, res);
        }
        return result;
    }

}
