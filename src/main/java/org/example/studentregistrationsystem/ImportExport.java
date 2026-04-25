package org.example.studentregistrationsystem;

public interface ImportExport {
    void exportToCsv(String filePath);
    void importFromCsv(String filePath);
}
