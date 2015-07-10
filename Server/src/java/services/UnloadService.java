package services;

import connect.MainServer;
import managers.DataBase.DatabaseManager;
import managers.FileSystemManager;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import transfer.LabSubmitDate;
import transfer.StudentResult;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Map;

/**
 * Класс сервиса выгрузки списка студентов в xls таблицу
 * Ответственный: Сергей Груздев
 */
public class UnloadService {
    public static File getStatistic() {
        Map<String, ArrayList<StudentResult>> data;
        DatabaseManager dbm = MainServer.dbManager;
        data = dbm.getData();
        HSSFWorkbook book = new HSSFWorkbook();
        for (Map.Entry e : data.entrySet()) {
            String title_sheet = (String) e.getKey();
            title_sheet = title_sheet.replaceAll("[^a-zA-Zа-яА-Я\\s]", "");
            HSSFSheet sheet = (title_sheet.length() == 0) ? book.createSheet("Предмет") : book.createSheet(title_sheet);
            ArrayList<StudentResult> list = (ArrayList<StudentResult>) e.getValue();
            int id_row = 1;
            for (StudentResult o : list) {
                if (o == null) {
                    id_row++;
                    continue;
                }
                if (id_row == 1) {
                    HSSFRow head = sheet.createRow(0);
                    head.createCell(0).setCellValue("Студент");
                    head.createCell(1).setCellValue("Группа");
                    for (int i = 1; i <= o.dates.size(); i++) {
                        head.createCell(i + 1).setCellValue("ЛР " + i);
                    }
                }
                HSSFRow row = sheet.createRow(id_row);
                row.createCell(0).setCellValue((o.surname.length() != 0 ? (o.surname + " ") : "") + (o.name.length() != 0 ? (o.name + " ") : "") + (o.secondName.length() != 0 ? (o.secondName + " ") : ""));
                row.createCell(1).setCellValue(o.group);
                int id_col = 2;
                for (LabSubmitDate date : o.dates) {
                    if (date != null) {
                        try {
                            row.createCell(id_col).setCellValue(date.toString());
                        } catch (Exception ex) {
                            // Неверный формат даты. (Верный: ГГГГ-ММ-ДД)
                        }
                    }
                    id_col++;
                }
                id_row++;
            }
        }
        try {
            if (FileSystemManager.isFileHere("data.xls", "")) FileSystemManager.deleteFile("data.xls");
            FileOutputStream file = new FileOutputStream("data.xls");
            book.write(file);
            file.close();
            return new File("data.xls");
        } catch (Exception ex) {
            // Не создался файл .xls
            return null;
        }
    }
}
