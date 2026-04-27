package org.example.studentregistrationsystem;

import java.util.List;

public interface DataExportable {
    void exportData(List<Student> students, String filePath) throws Exception;

    default void printExportStatus(String format) {
        System.out.println("Eksportuojami duomenys " + format + " formatu.");
    }
}
