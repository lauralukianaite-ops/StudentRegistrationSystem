package org.example.studentregistrationsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.*;
import java.time.LocalDate;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class HelloController {
    @FXML private TableView<Student> tableStudent;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, String> colEmail;
    @FXML private TableColumn<Student, String> colGroup;
    @FXML private TableColumn<Student, String> colAttendance;
    @FXML private AnchorPane paneReview;
    @FXML private AnchorPane paneStudent;
    @FXML private AnchorPane paneGroups;
    @FXML private AnchorPane paneAttendance;
    @FXML private AnchorPane paneReports;
    @FXML private AnchorPane paneImportExport;

    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtGroup;

    @FXML private Button buttonSaveUpdate;
    @FXML private Button buttonAddStudent;

    private Student selectedStudent;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    @FXML private TableView<String> tableGroups;
    @FXML private TableColumn<String, String> colGroupName;
    @FXML private TableColumn<String, String> colGroupCount;
    @FXML private TextField txtNewGroupName;
    @FXML private TableColumn<Student, String> colMemberName;
    @FXML private TableColumn<Student, String> colMemberEmail;

    private ObservableList<String> groupList = FXCollections.observableArrayList();

    @FXML private ComboBox<Student> comboAvailableStudents;
    @FXML private TableView<Student> tableGroupMembers;

    private String selectedGroupName;

    @FXML private TableColumn<Student, Boolean> colAttendanceCheck;
    @FXML private TableColumn<Student, String> colAttendanceEmail;
    @FXML private TableColumn<Student, String> colAttendanceName;
    @FXML private TableView<Student> tableAttendance;
    @FXML private ComboBox<String> comboAttendanceGroup;
    @FXML private DatePicker datePickerAttendance;

    @FXML private Button buttonRemoveFromGroup;
    @FXML private Label labelRemoveFromGroup;

    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;

    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colGroup.setCellValueFactory(new PropertyValueFactory<>("group"));
        colAttendance.setCellValueFactory(data -> {
            double percentage = data.getValue().calculateAttendancePercentage();
            return new javafx.beans.property.SimpleStringProperty(String.format("%.1f%%", percentage));
        });

        studentList.add(new Student("Pavyzdys Pavyzdinis", "pavyzdys@stud.lt", "GR-1"));
        studentList.add(new Student("Pavyzdė Pavyzdienytė", "pavyzdyspavpav@stud.lt", "GR-2"));
        studentList.add(new Student("Pavyzdauskas Pavyzda", "pavyzdys@stud.lt", "GR-2"));
        studentList.add(new Student("Pavyzdyninis Pavyzdininininininis", "mmmmm@stud.lt", "GR-3"));
        studentList.add(new Student("Pavydinininininininins Pavyzdys", "lalla@stud.lt", "GR-1"));
        studentList.add(new Student("Vardis Bepavardis", "vardas@stud.lt", "GR-1"));
        studentList.add(new Student("Pavardis Vardis", "wwww@stud.lt", "GR-1"));

        tableStudent.setItems(studentList);

        colGroupName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()));
        tableGroups.setItems(groupList);

        tableGroups.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedGroupName = newSelection;
                updateMembersTable(newSelection);
            }
        });

        colGroupCount.setCellValueFactory(data -> {
            String groupName = data.getValue();

            long count = studentList.stream()
                    .filter(s -> s.getGroup() != null && s.getGroup().equals(groupName))
                    .count();

            return new javafx.beans.property.SimpleStringProperty(String.valueOf(count));
        });

        tableGroups.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedGroupName = newSelection;
                updateMembersTable(newSelection);

                ObservableList<Student> freeStudents = studentList.filtered(s -> s.getGroup() == null || s.getGroup().isEmpty());
                comboAvailableStudents.setItems(freeStudents);
            }
        });

        colMemberName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colMemberEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        tableAttendance.setEditable(true);

        if (tableAttendance != null) {
            tableAttendance.setEditable(true);
            colAttendanceName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colAttendanceEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            colAttendanceCheck.setCellValueFactory(new PropertyValueFactory<>("attendingNow"));

            colAttendanceCheck.setCellFactory(column -> {
                javafx.scene.control.cell.CheckBoxTableCell<Student, Boolean> cell = new javafx.scene.control.cell.CheckBoxTableCell<>();
                cell.setSelectedStateCallback(index -> {
                    return tableAttendance.getItems().get(index).attendingNowProperty();
                });
                return cell;
            });
        }

        if (tableGroupMembers != null && buttonRemoveFromGroup != null) {
            tableGroupMembers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    buttonRemoveFromGroup.setVisible(true);
                    labelRemoveFromGroup.setVisible(true);
                } else {
                    labelRemoveFromGroup.setVisible(false);
                    buttonRemoveFromGroup.setVisible(false);
                }
            });
        }
    }

    @FXML
    void onAddStudentClick() {
        if (txtName.getText().isEmpty() || txtEmail.getText().isEmpty()) return;

        studentList.add(new Student(txtName.getText(), txtEmail.getText(), txtGroup.getText()));

        txtName.clear();
        txtEmail.clear();
        txtGroup.clear();
    }

    private void hideAllPanes() {
        paneReview.setVisible(false);
        paneStudent.setVisible(false);
        paneGroups.setVisible(false);
        paneAttendance.setVisible(false);
        paneReports.setVisible(false);
        paneImportExport.setVisible(false);
    }

    @FXML
    void onTableClick() {
        selectedStudent = tableStudent.getSelectionModel().getSelectedItem();

        if (selectedStudent != null) {
            txtName.setText(selectedStudent.getName());
            txtEmail.setText(selectedStudent.getEmail());
            txtGroup.setText(selectedStudent.getGroup());

            buttonAddStudent.setVisible(false);
            buttonSaveUpdate.setVisible(true);
        }
    }

    @FXML
    void onSaveUpdateClick() {
        if (selectedStudent != null) {
            selectedStudent.setGroup(txtGroup.getText());
            tableStudent.refresh();
            clearFields();
        }
    }

    private void clearFields() {
        txtName.clear();
        txtEmail.clear();
        txtGroup.clear();
        buttonAddStudent.setVisible(true);
        buttonSaveUpdate.setVisible(false);
        selectedStudent = null;
        tableStudent.getSelectionModel().clearSelection();
    }

    @FXML
    void onAddGroupClick() {
        String name = txtNewGroupName.getText().trim();

        if (!name.isEmpty()) {
            if (!groupList.contains(name)) {
                groupList.add(name);
                txtNewGroupName.clear();
                System.out.println("Grupė pridėta: " + name);
            } else {
                System.out.println("Tokia grupė jau egzistuoja!");
            }
        }
    }

    @FXML
    void onRemoveFromGroupClick() {
        Student selectedMember = tableGroupMembers.getSelectionModel().getSelectedItem();
        if (selectedMember != null && selectedGroupName != null) {
            selectedMember.setGroup(null);
            updateMembersTable(selectedGroupName);
            tableGroups.refresh();
            tableStudent.refresh();

            ObservableList<Student> freeStudents = studentList.filtered(s -> s.getGroup() == null || s.getGroup().isEmpty());
            comboAvailableStudents.setItems(freeStudents);
            buttonRemoveFromGroup.setVisible(false);
        }
    }

    private void refreshGroupMembers() {
        ObservableList<Student> members = studentList.filtered(s -> s.getGroup().equals(selectedGroupName));
        tableGroupMembers.setItems(members);

        ObservableList<Student> freeStudents = studentList.filtered(s -> s.getGroup() == null || s.getGroup().isEmpty());

        comboAvailableStudents.setItems(null);
        comboAvailableStudents.setItems(freeStudents);
    }

    @FXML
    void onAssignStudentClick() {
        Student studentToAssign = comboAvailableStudents.getSelectionModel().getSelectedItem();

        if (studentToAssign != null && selectedGroupName != null) {
            studentToAssign.setGroup(selectedGroupName);

            updateMembersTable(selectedGroupName);

            comboAvailableStudents.getSelectionModel().clearSelection();

            refreshGroupMembers();
            tableGroups.refresh();
            tableStudent.refresh();

        }
    }

    private void refreshGroups() {
        groupList.clear();

        for (Student s : studentList) {
            String gName = s.getGroup();
            if (gName != null && !gName.isEmpty() && !groupList.contains(gName)) {
                groupList.add(gName);
            }
        }
    }

    private void updateMembersTable(String groupName) {
        if (groupName == null) return;

        ObservableList<Student> filteredList = studentList.filtered(student ->
                student.getGroup() != null && student.getGroup().trim().equalsIgnoreCase(groupName.trim())
        );

        tableGroupMembers.setItems(filteredList);

        System.out.println("Ieškoma grupės: [" + groupName + "], Rasta: " + filteredList.size());
    }

    @FXML
    void onAttendanceGroupSelected() {
        String selectedGroup = comboAttendanceGroup.getValue();
        if (selectedGroup != null) {
            ObservableList<Student> groupMembers = studentList.filtered(s ->
                    s.getGroup() != null && s.getGroup().equals(selectedGroup));
            tableAttendance.setItems(groupMembers);
        }
    }

    @FXML
    void onDateChanged() {
        LocalDate selectedDate = datePickerAttendance.getValue();
        if (selectedDate != null && tableAttendance.getItems() != null) {
            for (Student s : tableAttendance.getItems()) {
                s.setAttendingNow(s.wasPresent(selectedDate));
            }
            tableAttendance.refresh();
        }
    }

    @FXML
    void onSaveAttendanceClick() {
        LocalDate selectedDate = datePickerAttendance.getValue();
        if (selectedDate == null) {
            System.out.println("Pirmiausia pasirinkite datą!");
            return;
        }
        for (Student s : tableAttendance.getItems()) {
            boolean isPresent = s.isAttendingNow();
            s.markAttendance(selectedDate, isPresent);
        }

        tableStudent.refresh();
        tableAttendance.refresh();
    }

    @FXML
    void onGeneratePDFReport() {
        LocalDate from = dateFrom.getValue();
        LocalDate to = dateTo.getValue();

        if (from == null || to == null) return;

        FileChooser fc = new FileChooser();
        fc.setInitialFileName("Lankomumo_Ataskaita.pdf");
        File file = fc.showSaveDialog(paneReports.getScene().getWindow());

        if (file != null) {
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                document.add(new Paragraph("Lankomumo ataskaita nuo " + from + " iki " + to));
                document.add(new Paragraph("--------------------------------------------------"));

                for (Student s : studentList) {
                    long count = s.getAttendanceRecord().entrySet().stream()
                            .filter(e -> !e.getKey().isBefore(from) && !e.getKey().isAfter(to))
                            .filter(Map.Entry::getValue)
                            .count();
                    document.add(new Paragraph(s.getName() + " [" + s.getGroup() + "]: " + count + " lankyti kartai"));
                }

                document.close();
                System.out.println("PDF sukurtas!");
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    @FXML
    void onExportExcel() {
        saveFile(new ExcelExporter(), "Excel failas", "*.xlsx");
    }

    @FXML
    void onExportCSV() {
        saveFile(new CsvExporter(), "CSV failas", "*.csv");
    }

    private void saveFile(DataExportable exporter, String desc, String ext) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(desc, ext));
        File file = fc.showSaveDialog(paneImportExport.getScene().getWindow());

        if (file != null) {
            try {
                exporter.exportData(studentList, file.getAbsolutePath());
                System.out.println("Eksportas sėkmingas!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onImportCSV() {
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(paneImportExport.getScene().getWindow());

        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                studentList.clear();
                String line;
                reader.readLine();
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(";");
                    if (data.length >= 3) {
                        studentList.add(new Student(data[0], data[1], data[2]));
                    }
                }
                tableStudent.refresh();
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    @FXML void onReviewClick() { hideAllPanes(); paneReview.setVisible(true); }
    @FXML void onStudentClick() { hideAllPanes(); paneStudent.setVisible(true); }
    @FXML void onGroupsClick() { hideAllPanes(); paneGroups.setVisible(true); refreshGroups(); }
    @FXML void onAttendanceClick() { hideAllPanes(); paneAttendance.setVisible(true); refreshGroups(); comboAttendanceGroup.setItems(groupList);}
    @FXML void onReportsClick() { hideAllPanes(); paneReports.setVisible(true); }
    @FXML void onImportExportClick() { hideAllPanes(); paneImportExport.setVisible(true); }
}
