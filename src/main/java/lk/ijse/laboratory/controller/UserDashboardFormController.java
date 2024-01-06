package lk.ijse.laboratory.controller;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.laboratory.Model.attendanceModel;
import lk.ijse.laboratory.Model.ordersModel;
import lk.ijse.laboratory.Model.reportModel;
import lk.ijse.laboratory.Model.prescriptionModel;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static lk.ijse.laboratory.db.DbConnection.newDto;

public class UserDashboardFormController {


    public AnchorPane root;
    public Label lblPendingPayments;
    public Label lblTime;
    public Label lblDate;
    public Label txtTodayempCount;
    public Label txtTodayTestCount;
    public Label txtPendingReports;
    @FXML
    private Label userTypeLabel;
    @FXML
    private Label userNameLabel;

    public void initialize() throws SQLException {
       setUserDetails();
      setDashBordDetails();
    }

    private void setDashBordDetails() throws SQLException {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                lblTime.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            }
        };
        timer.start();
        lblDate.setText(String.valueOf(LocalDate.now()));
        int emp = attendanceModel.getDailyCount();
        txtTodayempCount.setText(String.valueOf(emp));
        int test = reportModel.getCount();
        txtTodayTestCount.setText(String.valueOf(test));
        int pay = prescriptionModel.getCount();
        lblPendingPayments.setText(String.valueOf(pay));
        int rep = reportModel.getPendingReports();
        txtPendingReports.setText(String.valueOf(rep));
    }

    public void setUserDetails(){
        userTypeLabel.setText(newDto.getUserType());
        userNameLabel.setText(newDto.getUserName());
    }
    public void onActionPatientsBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/patient_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionReportsBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/reports_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionAttendanceBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/markAttendance_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionTestDetailsBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/testDetails_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionMyProfileBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/myProfile_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionLogOutButton(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/welcomeScreen_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionPendingPayments(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/prescription_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionPendingReports(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/reports_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }
}
