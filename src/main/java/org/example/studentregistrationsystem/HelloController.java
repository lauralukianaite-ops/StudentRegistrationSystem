package org.example.studentregistrationsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    @FXML private DatePicker datePicker;

    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colGroup.setCellValueFactory(new PropertyValueFactory<>("group"));
        colAttendance.setCellValueFactory(new PropertyValueFactory<>("attendanceRate"));

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
        colAttendanceCheck.setCellValueFactory(cellData -> {
            return new javafx.beans.property.SimpleBooleanProperty(false);
        });

        colAttendanceCheck.setCellFactory(javafx.scene.control.cell.CheckBoxTableCell.forTableColumn(colAttendanceCheck));

        if (tableAttendance != null) {
            tableAttendance.setEditable(true);

            if (colAttendanceName != null) {
                colAttendanceName.setCellValueFactory(new PropertyValueFactory<>("name"));
            }

            if (colAttendanceEmail != null) {
                colAttendanceEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            }

            if (colAttendanceCheck != null) {
                colAttendanceCheck.setCellValueFactory(cellData -> new javafx.beans.property.SimpleBooleanProperty(false));
                colAttendanceCheck.setCellFactory(javafx.scene.control.cell.CheckBoxTableCell.forTableColumn(colAttendanceCheck));
            }
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
            int index = studentList.indexOf(selectedStudent);
            Student updatedStudent = new Student(txtName.getText(), txtEmail.getText(), txtGroup.getText());

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
        System.out.println("Bandoma pašalinti studentą iš grupės...");
    }

    private void refreshGroupMembers() {
        ObservableList<Student> members = studentList.filtered(s -> s.getGroup().equals(selectedGroupName));
        tableGroupMembers.setItems(members);

        ObservableList<Student> freeStudents = studentList.filtered(s -> s.getGroup() == null || s.getGroup().isEmpty());
        comboAvailableStudents.setItems(freeStudents);
    }

    @FXML
    void onAssignStudentClick() {
        Student studentToAssign = comboAvailableStudents.getSelectionModel().getSelectedItem();

        if (studentToAssign != null && selectedGroupName != null) {
            studentToAssign.setGroup(selectedGroupName);

            refreshGroupMembers();
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
    void onSaveAttendanceClick() {
        if (datePicker.getValue() == null) {
            System.out.println("Pirmiausia pasirinkite datą!");
            return;
        }
        for (Student s : tableAttendance.getItems()) {
            s.addAttendance(true);
        }

        tableStudent.refresh();
        tableAttendance.refresh();
    }

    @FXML void onReviewClick() { hideAllPanes(); paneReview.setVisible(true); }
    @FXML void onStudentClick() { hideAllPanes(); paneStudent.setVisible(true); }
    @FXML void onGroupsClick() { hideAllPanes(); paneGroups.setVisible(true); refreshGroups(); }
    @FXML void onAttendanceClick() { hideAllPanes(); paneAttendance.setVisible(true); refreshGroups(); comboAttendanceGroup.setItems(groupList);}
    @FXML void onReportsClick() { hideAllPanes(); paneReports.setVisible(true); }
    @FXML void onImportExportClick() { hideAllPanes(); paneImportExport.setVisible(true); }
}
