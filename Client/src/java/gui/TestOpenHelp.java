package gui;

import java.awt.*;
import java.io.*;

public class TestOpenHelp {

    private static String res_name = "test.txt";

    public static File streamIntoFile(InputStream input, String name) throws IOException {
        byte[] buffer = new byte[input.available()];
        input.read(buffer);
        File targetFile = new File(name);
        OutputStream out = new FileOutputStream(targetFile);
        out.write(buffer);
        out.close();
        return targetFile;
    }

    public static File resToFile(String res_name, String name) throws IOException {
        InputStream input = TestOpenHelp.class.getResourceAsStream("/" + res_name);
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

    public static void main(String[] args) throws IOException {
        openHelp();
    }
}