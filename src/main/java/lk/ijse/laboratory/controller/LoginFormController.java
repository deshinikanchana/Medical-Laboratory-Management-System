package lk.ijse.laboratory.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.laboratory.Dto.stockItemDto;
import lk.ijse.laboratory.Dto.userDto;
import lk.ijse.laboratory.Model.stockItemModel;
import lk.ijse.laboratory.Model.userModel;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static lk.ijse.laboratory.db.DbConnection.newDto;

public class LoginFormController {
   @FXML
   public CheckBox CheckBoxShowPw;
    public Hyperlink HyperlinkForgetPw;
    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private PasswordField pwFieldPw;

    @FXML
    private AnchorPane root;

public void initialize() throws SQLException {
    Date today = Date.valueOf(LocalDate.now());
    Time just = Time.valueOf(LocalTime.now());
    List<userDto> userList = userModel.searchNewUsers(today,just);

    for (userDto dto : userList) {
       boolean saved = userModel.saveUser(dto);
       if(saved) {
           boolean isDeleted = userModel.deleteTempUsers(dto);
       }
    }
}

@FXML
    public void OnActionLoginBtn(ActionEvent e) throws SQLException, IOException {
    String userName = txtUsername.getText();
    String pw = txtPassword.getText();
    String hidePw = pwFieldPw.getText();
    Date date = Date.valueOf(LocalDate.now());
    Time time = Time.valueOf(LocalTime.now());


    pw = hidePw;
    var dto = new userDto();
    dto.setUserName(userName);
    dto.setPassword(pw);

    var model = new userModel();
        newDto = model.CheckCredential(dto);
    if (newDto.getUserType() != null) {
        if (newDto.getUserType().equals("Admin")) {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/adminDashboard_form.fxml"));
            Stage stage = (Stage) root.getScene().getWindow();

            stage.setScene(new Scene(anchorPane));
            stage.centerOnScreen();
        } else {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/userDashboard_form.fxml"));
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(new Scene(anchorPane));
            stage.centerOnScreen();
        }
    }
}

    @FXML
    public void onActionCheckBoxShowPw(ActionEvent event) {

        if(CheckBoxShowPw.isSelected()){
            txtPassword.setText(pwFieldPw.getText());
            pwFieldPw.setVisible(false);
            txtPassword.setVisible(true);

            return;
        }
        pwFieldPw.setText(txtPassword.getText());
        txtPassword.setVisible(false);
        pwFieldPw.setVisible(true);

    }

    public void onActionForgetPw(ActionEvent event) throws IOException {
        Parent anchorPane = FXMLLoader.load(getClass().getResource("/view/forgetPassword_form.fxml"));
        Scene scene = new Scene(anchorPane);

        Stage stage = new Stage();
        stage.setTitle("Reset Password");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void onActionUserName(ActionEvent event) {
        if (CheckBoxShowPw.isSelected()) {
            txtPassword.requestFocus();
        } else {
            pwFieldPw.requestFocus();
        }
    }

    public void onActionBackBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/welcomeScreen_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }
}
