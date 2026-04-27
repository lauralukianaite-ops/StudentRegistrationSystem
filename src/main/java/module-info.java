module org.example.studentregistrationsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires itextpdf;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens org.example.studentregistrationsystem to javafx.fxml;
    exports org.example.studentregistrationsystem;
}