package managers;

//import java.io.File;
import java.io.*;
/**
 * Created by Елизавета on 02.07.2015.
 */

public class FileSystemManager {
    /* удалить объект
    */
    static boolean deleteFile(File file)
    {
        //todo exc
        if (file.delete())//если это файл или пустая директория
            return true;
        else return deleteDir(file);
    }

    static boolean deleteDir(String path){
        File f = new File(path);
        String[] names = f.list();
        if ((names != null) && (names.length > 0))
            for (String currentName : names)
                if (!deleteFile(path + "\\" + currentName))
                    return false;//если тут тру, то не надо ретернить - это может быть не последний файл в листе
                else return f.delete();
        return f.delete();
    }

    static boolean deleteDir(File file){//удвлить директорию. она уже существует
        String path = file.getAbsolutePath();
        return deleteDir(path);
    }


    /* у нас есть строка с путем чего-то
    создали объект класса file для этого пути
    если существует что-либо по этому пути
    то если это файл
    то удалить файл
    иначе это директория. удалить директорию.
    иначе этого не существует. возвращать тру или фолс?*/
    static boolean deleteFile(String path) {
        File f1 = new File(path);
        if (f1.exists()) {
            if (f1.isFile())
                return deleteFile(f1);
            else return deleteDir(f1);
        }

        // todo что тут возвращать?
        else return false;
    }

    static boolean createEmptyFile(String path){
        try (FileOutputStream newFileOrDir = new FileOutputStream(path)) {
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }


}
