package lk.ijse.laboratory.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.laboratory.Dto.Tm.attendanceTm;
import lk.ijse.laboratory.Dto.attendanceDto;
import lk.ijse.laboratory.Dto.designationDto;
import lk.ijse.laboratory.Dto.employeeDto;
import lk.ijse.laboratory.Dto.testDto;
import lk.ijse.laboratory.Model.attendanceModel;
import lk.ijse.laboratory.Model.employeeModel;
import lk.ijse.laboratory.Model.designationModel;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

public class ViewAttendanceFormController {

    public TextField txtDate;
    public JFXComboBox cmbEmployeeId;
    public TextField txtEmployeeName;
    public TextField txtOtHours;
    public TextField txtWorkedDaysCount;
    public TableView<attendanceTm> tblAttendance;
    public TableColumn<?,?> colEmployeeId;
    public TableColumn<?,?> colDate;
    public TableColumn<?,?> colInTime;
    public TableColumn<?,?> colOutTime;
    public TableColumn<?,?> colStatus;

    public TableColumn<?,?> colName;

    public AnchorPane root;
    public Label lblEmployees;
    public Label lblDesignation;
    public Label lblViewAttendance;
    public Label lblSalary;
    public TextField txtNopayHrs;

    public void initialize() {
        setCellValueFactory();
        loadAllAttendances();
        loadAllEmpIds();
        setDates();
    }

    private void setDates() {
        Date date = Date.valueOf(LocalDate.now());
        txtDate.setText(String.valueOf(date));
    }

    private void loadAllEmpIds() {
        ObservableList<String> obListId = FXCollections.observableArrayList();
        try {
            List<employeeDto> testList = employeeModel.loadAllemployees();

            for (employeeDto dto : testList) {
                obListId.add(dto.getEmpId());
            }
            cmbEmployeeId.setItems(obListId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAllAttendances() {
        var model = new attendanceModel();

        ObservableList<attendanceTm> obList = FXCollections.observableArrayList();

        try {
            List<attendanceDto> dtoList = model.getAllAttendances();

            for(attendanceDto dto : dtoList) {

                employeeDto edto = employeeModel.searchEmployeeById(dto.getEmpId());
                obList.add(
                        new attendanceTm(
                                dto.getDate(),
                                dto.getEmpId(),
                                edto.getName(),
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
        colInTime.setCellValueFactory(new PropertyValueFactory<>("inTime"));
        colOutTime.setCellValueFactory(new PropertyValueFactory<>("outTime"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    public void onActionBackBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/adminDashboard_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionEmployeeManageBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/employee_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();

    }

    public void onActionManageSalariesBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/salaryManage_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionManageDesignationsBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/designationManage_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void cmbEmployeeIdSearchOnAction(ActionEvent event) {
        String id = String.valueOf(cmbEmployeeId.getValue());

        var model = new attendanceModel();
        try {
            attendanceDto dto = model.searchAttendance(id);

            if(dto != null) {
                fieldDetails(dto);

            } else {
                new Alert(Alert.AlertType.INFORMATION, "Employee not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void fieldDetails(attendanceDto dto) throws SQLException {

        Date date = Date.valueOf(txtDate.getText());
        int mon = (date.getMonth() + 1);


        employeeDto edto = employeeModel.searchEmployeeById(dto.getEmpId());
        txtEmployeeName.setText(edto.getName());
        designationDto Ddto = designationModel.searchDesignation(edto.getJobId());

        int days = attendanceModel.getCount(dto.getEmpId(),mon);
        float hours = attendanceModel.calculateTotalHours(dto.getEmpId(), mon);

            LocalDate day = LocalDate.now();
           // float hrs = (hours * days);
            float Ot = (hours - Ddto.getWorkingHoursPerMonth());
            if(day.getDayOfMonth() < 25 && Ot >0){
                txtOtHours.setText(String.valueOf(Ot));
                txtNopayHrs.setText("-");
            }else if(day.getDayOfMonth() > 26 && Ot <0){
                float nop = (Ddto.getWorkingHoursPerMonth() - hours);
                txtNopayHrs.setText(String.valueOf(nop));
                txtOtHours.setText("-");
            }else{
                txtOtHours.setText("-");
                txtNopayHrs.setText("-");
            }


        txtWorkedDaysCount.setText(String.valueOf(days));

    }

    public void onActionTxtDate(ActionEvent event) {
// show table
    }

    public void onActionSalaryMouseEntered(MouseEvent mouseEvent) {
    lblSalary.setVisible(true);
    }

    public void onActionSalaryMouseExited(MouseEvent mouseEvent) {
lblSalary.setVisible(false);
    }

    public void onActionEmployeesMouseExited(MouseEvent mouseEvent) {
lblEmployees.setVisible(false);
    }

    public void onActionEmployeesMouseEntered(MouseEvent mouseEvent) {
        lblEmployees.setVisible(true);
    }

    public void onActionDesignationsMouseEntered(MouseEvent mouseEvent) {
lblDesignation.setVisible(true);
    }

    public void onActionDesignationsMouseExited(MouseEvent mouseEvent) {
        lblDesignation.setVisible(false);
    }

    public void onActionViewAttendanceMouseExited(MouseEvent mouseEvent) {
lblViewAttendance.setVisible(false);
    }

    public void onActionViewAttendanceMouseEntered(MouseEvent mouseEvent) {
        lblViewAttendance.setVisible(true);
    }
}
