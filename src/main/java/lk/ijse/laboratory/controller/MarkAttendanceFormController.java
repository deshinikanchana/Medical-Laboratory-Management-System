package lk.ijse.laboratory.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.laboratory.Dto.Tm.attendanceTm;
import lk.ijse.laboratory.Dto.attendanceDto;
import lk.ijse.laboratory.Dto.employeeDto;
import lk.ijse.laboratory.Model.employeeModel;
import lk.ijse.laboratory.Model.attendanceModel;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.eclipse.jdt.internal.compiler.codegen.ConstantPool.Name;


public class MarkAttendanceFormController {
    private final attendanceModel AttendanceModel = new attendanceModel();
    public TableView<attendanceTm> tblAttendance;
    public TableColumn<?,?> colDate;
    public TableColumn<?,?> colEmployeeId;
    public TableColumn<?,?> colEmployeeName;
    public TableColumn<?,?> colInTime;
    public TableColumn<?,?> colOutTime;
    public TableColumn<?,?> colStatus;
    public TextField txtEmpName;
    public JFXComboBox cmbEmpId;
    public CheckBox checkBoxIn;
    public CheckBox checkBoxOut;
    public TextField txtStatus;
    public JFXButton updateBtn;
    public Label lbaDate;

    @FXML
    private AnchorPane root;


        public void initialize() {
            loadEmployeeIds();
            setCellValueFactory();
            loadAllAttendance();
            setCurrentDate();

        }

    private void setCurrentDate() {
            Date date = Date.valueOf(LocalDate.now());
        lbaDate.setText(String.valueOf(date));
    }

    private void loadAllAttendance() {
        ObservableList<attendanceTm> obList = FXCollections.observableArrayList();

        try {
            List<attendanceDto> dtoList = AttendanceModel.getAllAttendances();
            for(attendanceDto dto : dtoList) {
                employeeDto EmpName = employeeModel.searchEmployeeById(dto.getEmpId());

                obList.add(
                        new attendanceTm(
                                dto.getDate(),
                                dto.getEmpId(),
                                EmpName.getName(),
                                dto.getInTime(),
                                dto.getOutTime(),
                                dto.getStatus()

                        )
                );
            }

            tblAttendance.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        colEmployeeName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colInTime.setCellValueFactory(new PropertyValueFactory<>("inTime"));
        colOutTime.setCellValueFactory(new PropertyValueFactory<>("outTime"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

        private void clearFields() {
        txtStatus.setText(" ");
        checkBoxIn.setSelected(false);
        checkBoxOut.setSelected(false);
        txtEmpName.setText(" ");
        cmbEmpId.setValue(" ");
        }

    private void loadEmployeeIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<employeeDto> empList = AttendanceModel.loadAllEmployeeIds();

            for (employeeDto dto : empList) {
                obList.add(dto.getEmpId());
            }
            cmbEmpId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public void onActionBackBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/userDashboard_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionClearBtn(ActionEvent event){
            clearFields();

    }
    public void onActionSaveBtn(ActionEvent event) {
            setStatus();
        Date date = Date.valueOf(lbaDate.getText());
        String empId = (String) cmbEmpId.getValue();
        Time inTime = onActionCheckboxIn();
        Time outTime = null;
        String status = txtStatus.getText();


        var dto = new attendanceDto(empId,date,inTime,outTime,status);


        try {
            boolean isSaved = AttendanceModel.saveAttendance(dto);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Attendance Mark Successfully !!!").show();
                loadAllAttendance();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }


    private void fillFields(attendanceDto dto,String name) {
            if(dto!= null) {
                txtStatus.setText(dto.getStatus());
                txtEmpName.setText(name);
                checkBoxIn.setSelected(dto.getInTime() != null);
                checkBoxOut.setSelected(dto.getOutTime() != null);
                return;
            }
        txtEmpName.setText(name);
        checkBoxIn.setSelected(false);
        checkBoxOut.setSelected(false);
        txtStatus.setText(" ");
    }

    public void onActionSearchCmbEmpId(ActionEvent event) throws SQLException {
        String id = (String) cmbEmpId.getValue();
        boolean mark = AttendanceModel.isMArked(id);
        if(mark) {
            try {
                attendanceDto dto = AttendanceModel.searchAttendancesById(id,lbaDate.getText());
                employeeDto Edto = employeeModel.searchEmployeeById(id);
                fillFields(dto, Edto.getName());

            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                clearFields();
            }
        }else{
            new Alert(Alert.AlertType.ERROR, "Attendance Already Marked !!!").show();
        }
    }

    public Time onActionCheckboxIn() {
        if(checkBoxIn.isSelected()) {
            setStatus();
            checkBoxIn.setDisable(true);
            SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm:ss");
           Time time = Time.valueOf(dateFormat.format(new java.util.Date()));
            return time;
        }
        return null;
    }

    public void setStatus(){
            if(checkBoxIn.isSelected() && checkBoxOut.isSelected()){
                txtStatus.setText("WORKED");
                return;
            }else if(checkBoxIn.isSelected()){
                txtStatus.setText("IN");
                return;
        } else {
                txtStatus.setText("ABSENT");
            }
    }

    public Time onActionCheckBoxOut() {
            if(checkBoxOut.isSelected()){
                setStatus();
                checkBoxOut.setDisable(true);
                SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm:ss");
                Time time = Time.valueOf(dateFormat.format(new java.util.Date()));
               return time;
            }
      return null;
    }

    public void onActionUpdateBtn(ActionEvent event) {
            setStatus();
            Date date = null;
        String empId = (String) cmbEmpId.getValue();
        Time inTime = null;
        Time outTime = onActionCheckBoxOut();
        String status = txtStatus.getText();


        var dto = new attendanceDto(empId,date,inTime,outTime,status);

        try {
            boolean isUpdated = AttendanceModel.updateattendance(dto);

            if(isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Attendance Mark updated !!!").show();
                loadAllAttendance();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            System.out.println("wisaala karadarayakne meka");
            clearFields();
        }
    }
}
