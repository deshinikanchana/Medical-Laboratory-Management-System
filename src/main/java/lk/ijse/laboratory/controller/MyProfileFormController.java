package lk.ijse.laboratory.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.laboratory.Dto.employeeDto;
import lk.ijse.laboratory.Dto.userDto;
import lk.ijse.laboratory.Model.employeeModel;
import lk.ijse.laboratory.Model.userModel;

import java.io.IOException;
import java.sql.SQLException;

import static lk.ijse.laboratory.db.DbConnection.newDto;

public class MyProfileFormController {
    public TextField txtUserId;
    public TextField txtCurrentPassword;
    public TextField txtUserType;
    public TextField txtNewPassword;
    public CheckBox cbShowPW;
    public TextField txtUserName;
    public PasswordField pwfNewPassword;
    public PasswordField pwfCurrentPassword;
    public TextField txtConfirmPassword;
    public PasswordField pwfConfirmPw;

    public TextField txtEmail;
    @FXML
    private AnchorPane root;

    public void initialize(){
        setUserDetails();

    }

    private void setUserDetails() {
        txtUserId.setText(newDto.getUserId());
        txtUserType.setText(newDto.getUserType());
        txtUserName.setText(newDto.getUserName());
        txtEmail.setText(newDto.getEmail());
    }

    public void onActionBackBtn(ActionEvent event) throws IOException {
        if(newDto.getUserType().equals("Admin")){

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/adminDashboard_form.fxml"));
            Stage stage = (Stage) root.getScene().getWindow();

            stage.setScene(new Scene(anchorPane));
            stage.centerOnScreen();

        }else {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/userDashboard_form.fxml"));
            Stage stage = (Stage) root.getScene().getWindow();

            stage.setScene(new Scene(anchorPane));
            stage.centerOnScreen();

        }
    }

    public void onActioncbShowPw(ActionEvent event) {
        if(cbShowPW.isSelected()){
            txtCurrentPassword.setText(pwfCurrentPassword.getText());
            pwfCurrentPassword.setVisible(false);
            txtCurrentPassword.setVisible(true);

            txtNewPassword.setText(pwfNewPassword.getText());
            pwfNewPassword.setVisible(false);
            txtNewPassword.setVisible(true);

            txtConfirmPassword.setText(pwfConfirmPw.getText());
            pwfConfirmPw.setVisible(false);
            txtConfirmPassword.setVisible(true);

            return;
        }
        pwfCurrentPassword.setText(txtCurrentPassword.getText());
        txtCurrentPassword.setVisible(false);
        pwfCurrentPassword.setVisible(true);

        pwfNewPassword.setText(txtNewPassword.getText());
        txtNewPassword.setVisible(false);
        pwfNewPassword.setVisible(true);

        pwfConfirmPw.setText(txtConfirmPassword.getText());
        txtConfirmPassword.setVisible(false);
        pwfConfirmPw.setVisible(true);
    }

    public void onActionUpdateBtn(ActionEvent event) {
        txtCurrentPassword.setText(pwfCurrentPassword.getText());
        txtNewPassword.setText(pwfNewPassword.getText());
        txtConfirmPassword.setText(pwfConfirmPw.getText());

        String userName = txtUserName.getText();
        String currentpassword = txtCurrentPassword.getText();
        String newPassword = txtNewPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();
        String email = txtEmail.getText();

        if(currentpassword.equals(newDto.getPassword()) &&  confirmPassword.equals(newPassword)){
            var dto = new userDto(newDto.getUserId(),userName,newDto.getUserType(),confirmPassword,email);

            var model = new userModel();
            try {
                boolean isUpdated = model.updateMyProfile(dto);

                if(isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Your Profile is updated !!!").show();
                    newDto.setPassword(confirmPassword);
                    newDto.setUserName(userName);
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }

        }else{

            new Alert(Alert.AlertType.ERROR, "Passwords not matched !!!").show();
        }
    }

    public void clearFields() {
        pwfConfirmPw.setText("");
        txtConfirmPassword.setText("");
        pwfCurrentPassword.setText("");
        txtCurrentPassword.setText("");
        pwfNewPassword.setText("");
        txtNewPassword.setText("");
        txtEmail.setText(" ");


    }

    public void onActionTxtUName(ActionEvent event) {
        txtEmail.requestFocus();
    }

    public void onActionNewPw(ActionEvent event) {
        if(cbShowPW.isSelected()){
            txtConfirmPassword.requestFocus();
        }
        pwfConfirmPw.requestFocus();
    }

    public void onActionCurrentPw(ActionEvent event) {
        if(cbShowPW.isSelected()){
            txtNewPassword.requestFocus();
        }
        pwfNewPassword.requestFocus();
    }

    public void onActiontxtemail(ActionEvent event) {
        if(cbShowPW.isSelected()){
            txtCurrentPassword.requestFocus();
        }
        pwfCurrentPassword.requestFocus();
    }
}

