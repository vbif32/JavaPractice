package gui;

import java.awt.*;
import java.io.*;

/**
 * Класс описывающий получение справки пользователем
 */
public class Help {

    private static String res_name = "test.txt";

    public static File resToFile(String res_name, String name) throws IOException {
        InputStream input = Help.class.getResourceAsStream("/" + res_name);
        byte[] buffer = new byte[input.available()];
        input.read(buffer);
        File targetFile = new File(name);
        OutputStream out = new FileOutputStream(targetFile);
        out.write(buffer);
        out.close();
        return targetFile;
    }

    public static void openHelp() throws IOException {
        Desktop desktop = Desktop.getDesktop();
        File help = resToFile(res_name, res_name);
        desktop.open(help);
        help.deleteOnExit();
    }
}