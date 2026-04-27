package org.example.studentregistrationsystem;

import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.List;

public class ExcelExporter implements DataExportable{
    @Override
    public void exportData(List<Student> students, String filePath) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Studentai");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Vardas");
        header.createCell(1).setCellValue("El. paštas");
        header.createCell(2).setCellValue("Grupė");
        header.createCell(3).setCellValue("Lankomumas %");

        int rowNum = 1;
        for (Student s : students) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(s.getName());
            row.createCell(1).setCellValue(s.getEmail());
            row.createCell(2).setCellValue(s.getGroup());
            row.createCell(3).setCellValue(String.format("%.1f%%", s.calculateAttendancePercentage()));
        }

        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } finally {
            workbook.close();
        }
    }
}
