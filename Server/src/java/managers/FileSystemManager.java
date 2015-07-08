package managers;

import java.awt.*;
import java.io.*;

/**
 * Created by Елизавета on 02.07.2015.
 */

public class FileSystemManager {

    /*удаления объекта класса File
    * если он есть
    * то если это файл - удалить
    * если директория - удалить*/
    static boolean deleteFile(File file) {
        if (file != null)
            return file.delete() ? true : deleteDir(file);
        else return false;
    }

    /*удаление директории, если на руках имеется ее путь.
    * получили список того, что в ней есть
    * если он не пустой,
    * то для каждого имени:
    * постараться его удалить
    * и удалить папку, содержимое которой мы удаляли*/
    static boolean deleteDir(String path) {
        File f = new File(path);
        String[] names = f.list();
        if ((names != null) && (names.length > 0))
            for (String currentName : names)
                return !deleteFile(path + "\\" + currentName) ? false : f.delete();
        return f.delete();
    }

    /*удаление директории*/
    static boolean deleteDir(File file) {
        String path = file.getAbsolutePath();
        return deleteDir(path);
    }

    /* у нас есть строка с путем чего-то ("D:\\Programming\\Java\\readme.txt")
    * создали объект класса file для этого пути
    * если существует что-либо по этому пути
    * то если это файл - удалить файл
    * иначе это директория - удалить директорию.
    * иначе этого не существует или что-то пошло не так. */
    static boolean deleteFile(String path) {
        File f1 = new File(path);
        if (f1.exists()) {
            if (f1.isFile())
                return deleteFile(f1);
            else return deleteDir(f1);
        } else return false;
    }

    /*создать новый пустой файл
    * путь к которому path*/
    static boolean createEmptyFile(String path) {
        try (FileOutputStream newFileOrDir = new FileOutputStream(path)) {
            }
        catch (IOException ex) {return false;}
        return true;
    }

    /*поиск файла
    * первый параметр - искомое имя ("input.txt", "NewFolder")
    * второй параметр - папка, в которой ведется поиск ("С:\\", "D:\\Programming\\Java")
    *
    * создаем список содержимого папки
    * для каждого имени:
    * если текущее имя совпадает с искомым,
    * то возвращаем найденный файл,
    * иначе идем дальше - если это папка, то спускаемся по ней. если ничего не нашли, то идем дальше, если нашли, то ретерним*/
    static File searchFile(String requiredName, String initialPath) {
        File f = new File(initialPath);
        String[] list = f.list();

        if ((list != null) && (list.length > 0)) for (int i = 0; i < list.length; i++) {
            String currentName = list[i];
            if (currentName.equals(requiredName)) {
                String foundPath = initialPath + "\\" + currentName;
                return new File(foundPath);
            } else {
                if (f.isDirectory()) {
                    File couldBeFound = searchFile(requiredName, initialPath + "\\" + currentName);
                    if ((couldBeFound == null) && (i >= list.length))
                        return null;
                    else {
                        if (couldBeFound != null) return couldBeFound;
                    }
                }
            }
        }
        return f.getName().equals(requiredName) ? f : null;
    }

    /*копирование файлов, если на руках объекты
    * пока не пробовала копировать папки*/
    static boolean copyFiles(File oldFile, File newFile)  {
        if (oldFile.isFile()!=newFile.isFile())
            return false;
        else {
            try (FileInputStream is = new FileInputStream(oldFile)) {
                try {
                    FileOutputStream os = new FileOutputStream(newFile);
                    try {
                        byte[] buffer = new byte[4096];
                        int length;
                        while ((length = is.read(buffer)) > 0) {
                            os.write(buffer, 0, length);
                        }
                    } catch (IOException ex) {
                        return false;
                    } finally {
                        os.close();
                    }
                } finally {
                    is.close();
                }
            } catch (FileNotFoundException e) {
                return false;
            } catch (IOException e) {
                return false;
            }
            return true;
        }
    }

    /*копирование файла,
    * если на руках путь того, откуда копируют и путь, куда это копируют.
    * если это разные вещи, то ретернится false
    * пока не пробовала копировать папки*/
    static boolean copyFiles(String oldFilePath, String newFilePath){
        File f1 = new File(oldFilePath);
        File f2 = new File(newFilePath);
        return f2.isFile() != f1.isFile() ? false : copyFiles(f1, f2);
    }
}