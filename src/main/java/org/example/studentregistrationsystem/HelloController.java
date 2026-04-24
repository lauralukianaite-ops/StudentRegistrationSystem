package org.example.studentregistrationsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

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

    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colGroup.setCellValueFactory(new PropertyValueFactory<>("group"));
        colAttendance.setCellValueFactory(new PropertyValueFactory<>("attendanceRate"));

        studentList.add(new Student("Pavyzdys Pavyzdinis", "pavyzdys@stud.lt", "GR-1", "0%"));

        tableStudent.setItems(studentList);
    }

    @FXML
    void onAddStudentClick() {
        if (txtName.getText().isEmpty() || txtEmail.getText().isEmpty()) return;

        studentList.add(new Student(txtName.getText(), txtEmail.getText(), txtGroup.getText(), "0%"));

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
            int index = studentList.indexOf(selectedStudent);
            Student updatedStudent = new Student(txtName.getText(), txtEmail.getText(), txtGroup.getText(), selectedStudent.getAttendanceRate());

            studentList.set(index, updatedStudent);

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

    @FXML void onReviewClick() { hideAllPanes(); paneReview.setVisible(true); }
    @FXML void onStudentClick() { hideAllPanes(); paneStudent.setVisible(true); }
    @FXML void onGroupsClick() { hideAllPanes(); paneGroups.setVisible(true); }
    @FXML void onAttendanceClick() { hideAllPanes(); paneAttendance.setVisible(true); }
    @FXML void onReportsClick() { hideAllPanes(); paneReports.setVisible(true); }
    @FXML void onImportExportClick() { hideAllPanes(); paneImportExport.setVisible(true); }
}
