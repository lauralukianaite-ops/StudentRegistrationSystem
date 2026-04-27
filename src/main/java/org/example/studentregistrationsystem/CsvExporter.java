package org.example.studentregistrationsystem;

import java.io.PrintWriter;
import java.util.List;

public class CsvExporter implements DataExportable{
    @Override
    public void exportData(List<Student> students, String filePath) throws Exception {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.println("Vardas;El.pastas;Grupe;Lankomumas");
            for (Student s : students) {
                writer.println(s.getName() + ";" + s.getEmail() + ";" +
                        s.getGroup() + ";" +
                        String.format("%.1f%%", s.calculateAttendancePercentage()));
            }
        }
    }
}
