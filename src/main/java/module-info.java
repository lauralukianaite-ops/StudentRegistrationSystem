module org.example.studentregistrationsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens org.example.studentregistrationsystem to javafx.fxml;
    exports org.example.studentregistrationsystem;
}