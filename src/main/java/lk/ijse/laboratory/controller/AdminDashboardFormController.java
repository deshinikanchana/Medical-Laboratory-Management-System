package lk.ijse.laboratory.controller;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lk.ijse.laboratory.Model.attendanceModel;
import lk.ijse.laboratory.Model.reportModel;
import lk.ijse.laboratory.Model.ordersModel;
import lk.ijse.laboratory.Model.stockItemModel;
import static lk.ijse.laboratory.db.DbConnection.newDto;

public class AdminDashboardFormController {

    public Label txtTodayempCount;
    public Label txtTodayTestCount;
    public Label lblTime;
    public Label lblDate;
    public Label txtPendingOrders;
    public Label txtStockWarnings;
    public Label txtPendingReports;
    @FXML
  private TextField txtUserName;
  @FXML
  private TextField txtUserType;
  @FXML
  private AnchorPane root;

  public void initialize() throws SQLException {
      addUserDetails();
      setDashBoardDetails();
  }

    private void setDashBoardDetails() throws SQLException {
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
        int od = ordersModel.getOrderCount();
        txtPendingOrders.setText(String.valueOf(od));
        int stock = stockItemModel.warningStocks();
        txtStockWarnings.setText(String.valueOf(stock));
        int rep = reportModel.getPendingReports();
        txtPendingReports.setText(String.valueOf(rep));
    }

    @FXML
    public void onActionLogOutButton(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/welcomeScreen_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionEmployeesBtn(ActionEvent event) throws IOException {

        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/employee_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionTestsBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/test_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionStockBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/stocks_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionUsersBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/user_form.fxml"));
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


    public void addUserDetails() {
        txtUserType.setText(newDto.getUserType());
        txtUserName.setText(newDto.getUserName());
    }

    public void onActionPendingOrders(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/orders_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionBtnWarning(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/stocks_form.fxml"));
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
