package gui;

import java.awt.*;
import java.io.*;

/**
 * Класс описывающий получение справки пользователем
 */
public class Help {

    private static String res_name = "help.chm";

    public static File resToFile(String res_name, String name) throws IOException {
        BufferedInputStream input = new BufferedInputStream(Help.class.getResourceAsStream("/" + res_name));
        byte[] buffer = new byte[1024];
        File targetFile = new File(name);
        BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream(targetFile));
        int len;
        while((len = input.read(buffer)) > 0) {
            bs.write(buffer, 0, len);
        }
        input.close();
        bs.close();
        return targetFile;
    }

    public static void openHelp() throws IOException {
        Desktop desktop = Desktop.getDesktop();
        File help = resToFile(res_name, res_name);
        desktop.open(help);
        help.deleteOnExit();
    }
}