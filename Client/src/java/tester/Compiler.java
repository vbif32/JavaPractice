package tester;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Compiler {

    /*
    Создание временной папки для хранения файлов
     */
    private static void createFolder(String dir){
        File theDir = new File(dir);
        if (!theDir.exists()) {
            System.out.println("creating " + dir +" directory");
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
        createFolder("temp");
        File tempLabFile = new File("temp/labFile" + id + ".cpp"); //!!!указать путь
        String compileError = null;
        BufferedReader eis;
        Process compileProcess = null;
        String newFileAbsolutePath = (tempLabFile.getAbsolutePath()).replace(".cpp", ".out");
        try {
            FileWriter fw = new FileWriter(tempLabFile);
            fw.write(code);
            fw.close();
            List<String> params = java.util.Arrays.asList("g++", tempLabFile.getAbsolutePath(), "-o", newFileAbsolutePath);
            ProcessBuilder builder = new ProcessBuilder(params);
            compileProcess = builder.start();
            eis = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
            compileError = readBufferedStream(eis);
            compileProcess.waitFor();
            eis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        tempLabFile.deleteOnExit();
        if (!new File(newFileAbsolutePath).exists()){
            System.out.println("File is not compiled!");
            System.out.println(compileError);
            System.out.println("------------");
            return null;
        } else {
            System.out.println("File is compiled!");
            System.out.println("------------");
            return new File(newFileAbsolutePath);
        }

    }

    /*
     Создание build-файла для ant
     */
    private static File createBuildFile(String mainClass, String id){
        File antBuildFile = new File("temp/build.xml");
        String build = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "\n" +
                "<project name=\"labFile\" default=\"start\" basedir=\".\">\n" +
                "\n" +
                "    <property name=\"src\" location=\"buildsrc\"/>\n" +
                "    <property name=\"build\" location=\"build\"/>\n" +
                "    <property name=\"dist\"  location=\"dist\"/>\n" +
                "\n" +
                "    <target name=\"init\">\n" +
                "        <tstamp/>\n" +
                "        <mkdir dir=\"${build}\"/>\n" +
                "        <mkdir dir=\"${src}\"/>\n" +
                "        <mkdir dir=\"${dist}\"/>\n" +
                "    </target>\n" +
                "\n" +
                "    <target name=\"compile\" depends=\"init\"\n" +
                "            description=\"compile the source\">\n" +
                "        <javac srcdir=\"${src}\" destdir=\"${build}\" includeantruntime=\"true\"/>\n" +
                "    </target>\n" +
                "\n" +
                "    <target name=\"run\" depends=\"compile\"\n" +
                "            description=\"generate the distribution\" >\n" +
                "        <jar jarfile=\"${dist}/labFile" + id + ".jar\">\n" +
                "            <fileset dir=\"${build}\" includes=\"**/**\"/>\n" +
                "            <manifest>\n" +
                "                <attribute name=\"Main-Class\" value=\"" + mainClass + "\"/>\n" +
                "                <attribute name=\"Class-Path\" value=\"\"/>\n" +
                "            </manifest>\n" +
                "        </jar>\n" +
                "    </target>\n" +
                "\n" +
                "    <target name=\"start\" depends=\"run\">\n" +
                "        <!-- Delete the ${build} and ${dist} directory trees -->\n" +
                "        <delete dir=\"${build}\"/>\n" +
                "        <delete dir=\"${src}\"/>\n" +
                "    </target>\n" +
                "\n" +
                "</project>";
        FileWriter fw = null;
        try {
            fw = new FileWriter(antBuildFile);
            fw.write(build);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return antBuildFile;
    }

    /*
    Поиск главного класса в коде
     */
    private static String findMainClass(String code){
        String mainClass;
        code = code.replaceAll("\n", "");
        Matcher m = Pattern.compile(".*(class.*)\\{.*public static void main\\(String\\[\\] args\\).*").matcher(code);
        if (m.find()) {
            mainClass = m.group(1);
            mainClass = mainClass.substring(mainClass.indexOf("class") + 6, mainClass.indexOf(" ", mainClass.indexOf("class") + 6));
            System.out.println("MainClass is: " + mainClass + "<");
        } else {
            System.out.println("MainClass not found.");
            mainClass = "";
        }
        return mainClass;
    }

    /*
    Нахождение пути к ant среди переменных среды
     */
    private static String getAntDist(ProcessBuilder builder){
        String antDist = "";
        Map<String, String> envMap = builder.environment();
        Set<String> keys = envMap.keySet();
        for(String key:keys){
//            System.out.println(key+" ==> "+envMap.get(key));
            if (key.equals("ANT_HOME")){
                antDist = envMap.get(key);
                break;
            }
        }
        return antDist;
    }

    /**
     * Сборка jar-файла
     */
    public static File compileJar(String code, String id){
        System.out.println("------------");
        System.out.println("Compiling jar file");
        if (code == null){
            System.out.println("    There is no code.");
            return null;
        }
        createFolder("temp");
        createFolder("temp/buildsrc");
        code = code.replaceAll("public class", "class").replaceAll("public interface", "interface")
                .replaceAll("public Enum", "Enum").replaceAll("public Annotation", "Annotation");
        File tempLabFile = new File("temp/buildsrc/labFile.java");
        FileWriter fw = null;
        try {
            fw = new FileWriter(tempLabFile);
            fw.write(code);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String mainClass = findMainClass(code);
        File antBuildFile = createBuildFile(mainClass, id);
        String compileError = null;
        String compileInf = null;
        BufferedReader eis;
        BufferedReader is;
        ProcessBuilder builder = new ProcessBuilder();
        String antDist = getAntDist(builder);
        if (antDist == null){
            System.out.println("Ant is not found.");
            return null;
        }
        List<String> params = java.util.Arrays.asList("cmd", "/c", antDist+"\\bin\\ant.bat", "-buildfile",
                antBuildFile.getAbsolutePath());
        builder.command(params);
        Process compileProcess = null;
        try {
            compileProcess = builder.start();
            eis = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
            compileError = readBufferedStream(eis);
            is = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()));
            compileInf = readBufferedStream(is);
            compileProcess.waitFor();
            eis.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String newFileAbsolutePath = new File("temp/dist/labFile" + id + ".jar").getAbsolutePath();
        if (compileError != null && compileError.equals("\n")) {
            System.out.println("-");
            System.out.println("    Сompile errors:");
            System.out.println(compileError);
            System.out.println("-");
        }
        if (!new File(newFileAbsolutePath).exists()){
            System.out.println("File is not compiled!");
            System.out.println(compileError);
            System.out.println("------------");
            return null;
        } else {
            System.out.println(compileInf);
            System.out.println("File is compiled!");
            System.out.println("------------");
            return new File(newFileAbsolutePath);
        }
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
