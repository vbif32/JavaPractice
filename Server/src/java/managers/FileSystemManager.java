package managers;

import java.io.*;

/**
 * класс для работы с файловой системой
 * Ответственный: Елизавета Левина
 */
public class FileSystemManager {

    /**
     * Функция удаления объекта класса File
     * если он есть, то если это файл - удалить
     * если директория - удалить
     *
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        if (file != null)
            return file.delete() ? true : deleteDir(file);
        else return false;
    }

    /**
     * Функция удаления директории, если на руках имеется ее путь.
     * получили список того, что в ней есть
     * если он не пустой, то для каждого имени:
     * постараться его удалить и удалить папку, содержимое которой мы удаляли
     *
     * @param path
     * @return
     */
    public static boolean deleteDir(String path) {
        File f = new File(path);
        String[] names = f.list();
        if ((names != null) && (names.length > 0))
            for (String currentName : names)
                return !deleteFile(path + File.separator + currentName) ? false : f.delete(); //исправлено, теперь не только под шиндус
        return f.delete();
    }

    /**
     * Функция удаления директории
     *
     * @param file
     * @return
     */
    public static boolean deleteDir(File file) {
        String path = file.getAbsolutePath();
        return deleteDir(path);
    }

    /**
     * у нас есть строка с путем чего-то ("D:\\Programming\\Java\\readme.txt")
     * создали объект класса file для этого пути
     * если существует что-либо по этому пути, то
     * если это файл - удалить файл
     * иначе это директория - удалить директорию.
     * иначе этого не существует или что-то пошло не так.
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        File f1 = new File(path);
        if (f1.exists()) {
            if (f1.isFile())
                return deleteFile(f1);
            else return deleteDir(f1);
        } else return false;
    }

    /**
     * Функция создания нового пустого файла
     *
     * @param path - путь к файлу
     * @return
     */
    public static boolean createEmptyFile(String path) {
        try (FileOutputStream newFileOrDir = new FileOutputStream(path)) {
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    /**
     * Функция создания пустой директории
     * т.е. если хочешь создать D:\\Users\\IIB-1-14
     * надо писать createEmptyDir("D:\\Users\\IIB-1-14")
     *
     * @param pathDir - путь к директории
     * @return флаг успешности выполнения операции
     */
    public static boolean createEmptyDir(String pathDir) {
        File newFold = new File(pathDir);
        return newFold.mkdirs();
    }


    /**
     * Функция поиска файла
     * создаем список содержимого папки для каждого имени:
     * если текущее имя совпадает с искомым,
     * то возвращаем найденный файл,
     * иначе идем дальше - если это папка, то спускаемся по ней.
     * если ничего не нашли, то идем дальше, если нашли, то ретерним.
     *
     * @param requiredName - искомое имя ("input.txt", "NewFolder")
     * @param initialPath  - папка, в которой ведется поиск ("С:\\", "D:\\Programming\\Java")
     * @return
     */
    public static File searchFile(String requiredName, String initialPath) {
        File f = new File(initialPath);
        String[] list = f.list();

        if ((list != null) && (list.length > 0)) for (int i = 0; i < list.length; i++) {
            String currentName = list[i];
            if (currentName.equals(requiredName)) {
                String foundPath = initialPath + File.separator + currentName; //исправлено
                return new File(foundPath);
            } else {
                if (f.isDirectory()) {
                    File couldBeFound = searchFile(requiredName, initialPath + File.separator + currentName); //исправлено
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

    /**
     * Функция проверки на существование файла/папки в нужной директории
     * isFileHere("input.txt", "D:\\Users\\IIB-1-14") проверяется, есть ли input.txt в папке группы
     *
     * @param requiredName
     * @param initialPath
     * @return
     */
    public static boolean isFileHere(String requiredName, String initialPath) {
        File f = new File(initialPath);
        String[] list = f.list();
        for (String currentName : list)
            if (currentName.equals(requiredName))
                return true;
        return false;
    }

    /**
     * Функция копирования файлов, если на руках объекты
     * пока не пробовала копировать папки
     *
     * @param oldFile
     * @param newFile
     * @return
     */
    public static boolean copyFiles(File oldFile, File newFile) {
        if (oldFile.isFile() != newFile.isFile())
            return false;
        else {
            try (FileInputStream is = new FileInputStream(oldFile)) {
                try {
                    try (FileOutputStream os = new FileOutputStream(newFile)) {
                        byte[] buffer = new byte[4096];
                        int length;
                        while ((length = is.read(buffer)) > 0) {
                            os.write(buffer, 0, length);
                        }
                    } catch (IOException ex) {
                        return false;
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

    /**
     * Функция копирования файла,
     * если на руках путь того, откуда копируют и путь, куда это копируют.
     * если это разные вещи, то ретернится false
     * пока не пробовала копировать папки
     *
     * @param oldFilePath
     * @param newFilePath
     * @return
     */
    public static boolean copyFiles(String oldFilePath, String newFilePath) {
        File f1 = new File(oldFilePath);
        File f2 = new File(newFilePath);
        return f2.isFile() != f1.isFile() ? false : copyFiles(f1, f2);
    }

/*
    public static void main(String[] args) {
    System.out.println(createEmptyDir("D:\\NewFolder"));
    }
*/
}